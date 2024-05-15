package or.nevet.orcloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

import or.nevet.orcloud.cloud_listeners.OrChildAddedListener;
import or.nevet.orcloud.cloud_listeners.OrSpecificChildAddedListener;
import or.nevet.orcloud.cloud_listeners.OrNChildrenAddedListener;
import or.nevet.orcloud.cloud_listeners.OrValueChangedListener;
import or.nevet.orcloud.listener_stoppers.ChildEventListenerStopper;
import or.nevet.orcloud.listener_stoppers.ValueEventListenerStopper;
import or.nevet.orgeneralhelpers.constants.CloudConstants;

public class RealtimeHelper {


    public static void updateMultipleFieldsNoOrderSync(HashSet<RealtimeField> fields) {
        CountDownLatch latch = new CountDownLatch(fields.size());
        for (RealtimeField field : fields) {
            RealtimeHelperAsync.updateRealtimeFieldAndDoSomethingWhenCompletedAsync(field, () -> latch.countDown());
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeMultipleFieldsNoOrderSync(HashSet<String> fieldLocations) {
        CountDownLatch latch = new CountDownLatch(fieldLocations.size());
        for (String fieldLocation : fieldLocations) {
            RealtimeHelperAsync.removeRealtimeFieldAndDoSomethingWhenCompletedAsync(fieldLocation, () -> latch.countDown());
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateFieldAndWaitForAFieldToChangeBeforeContinuingSync(RealtimeField update, String waitLocation) {
        CountDownLatch latch = new CountDownLatch(2);
        RealtimeHelperAsync.updateRealtimeFieldAndDoSomethingWhenCompletedAsync(update, () -> latch.countDown());
        RealtimeHelperAsync.waitForFieldToChangeAndThenDoSomethingAsync(waitLocation, snapshot -> latch.countDown());
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //returns the changed new value.
    public static Object waitForAFieldToChangeBeforeContinuingSync(String waitLocation) {
        CountDownLatch latch = new CountDownLatch(1);
        final Object[] newValue = new Object[1];
        RealtimeHelperAsync.waitForFieldToChangeAndThenDoSomethingAsync(waitLocation, new OrValueChangedListener() {
            @Override
            public void onChanged(RealtimeField field) {
                newValue[0] = field.getFieldValue();
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return newValue[0];
    }

    //returns the added new Field.
    public static RealtimeField[] waitForNChildrenToBeAddedBeforeContinuingSync(String collectionLocation, int N) {
        CountDownLatch latch = new CountDownLatch(1);
        final RealtimeField[][] children = new RealtimeField[1][];
        RealtimeHelperAsync.waitForNChildrenToBeInTheCollectionAndThenDoSomethingAsync(collectionLocation, new OrNChildrenAddedListener() {
            @Override
            public void onAllAdded(RealtimeField[] c) {
                children[0] = c;
                latch.countDown();
            }

            @Override
            public void onChildAdded(RealtimeField field) {

            }
        }, N);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return children[0];
    }

    public static void updateRealtimeFieldSync(RealtimeField field) {
        FirebaseSynchronizer<Void> synchronizer = new FirebaseSynchronizer<>();
        try {
            synchronizer.waitForTaskToFinish(RealtimeHelperAsync.updateRealtimeFieldAndReturnTaskAsync(field));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeRealtimeFieldAtLocationSync(String fieldLocation) {
        FirebaseSynchronizer<Void> synchronizer = new FirebaseSynchronizer<>();
        try {
            synchronizer.waitForTaskToFinish(RealtimeHelperAsync.removeRealtimeFieldAtLocationAndReturnTaskAsync(fieldLocation));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getRealtimeFieldValueAtLocationSync(String fieldLocation) {
        FirebaseSynchronizer<DataSnapshot> synchronizer = new FirebaseSynchronizer<>();
        DataSnapshot snapshot;
        try {
            snapshot = synchronizer.waitForTaskToFinish(RealtimeHelperAsync.getRealtimeFieldAtLocationAsync(fieldLocation));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return snapshot.getValue();
    }

    public static RealtimeField[] getRealtimeFieldChildrenValuesAtLocationSync(String fieldLocation) {
        FirebaseSynchronizer<DataSnapshot> synchronizer = new FirebaseSynchronizer<>();
        DataSnapshot snapshot;
        try {
            snapshot = synchronizer.waitForTaskToFinish(RealtimeHelperAsync.getRealtimeFieldAtLocationAsync(fieldLocation));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //O(n)
        RealtimeField[] children = new RealtimeField[(int) snapshot.getChildrenCount()];
        int currentChildIndex = 0;
        //O(n)
        for (DataSnapshot s : snapshot.getChildren())
            children[currentChildIndex++] = new RealtimeField(fieldLocation+CloudConstants.cloudLocationSeparator+s.getKey(), s.getValue());
        return children;
    }

    public static Object getRealtimeFieldValueAtLocationAsCustomObjectSync(String fieldLocation, Class<?> objectClass) {
        FirebaseSynchronizer<DataSnapshot> synchronizer = new FirebaseSynchronizer<>();
        DataSnapshot snapshot;
        try {
            snapshot = synchronizer.waitForTaskToFinish(RealtimeHelperAsync.getRealtimeFieldAtLocationAsync(fieldLocation));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return snapshot.getValue(objectClass);
    }

    public static class RealtimeHelperAsync {

        private static Task<Void> updateRealtimeFieldAndReturnTaskAsync(RealtimeField field) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            return database.getReference().child(field.getFieldLocation()).setValue(field.getFieldValue());
        }

        private static Task<Void> removeRealtimeFieldAtLocationAndReturnTaskAsync(String location) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            return database.getReference().child(location).removeValue();
        }

        private static Task<DataSnapshot> getRealtimeFieldAtLocationAsync(String location) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            return database.getReference().child(location).get();
        }

        public static ValueEventListenerStopper waitForFieldToChangeAndThenDoSomethingAsync(String fieldLocation, OrValueChangedListener whenChanged) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            final boolean[] isFirst = new boolean[1];
            isFirst[0] = true;
            DatabaseReference reference = database.getReference().child(fieldLocation);
            return new ValueEventListenerStopper(reference, reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!isFirst[0]) {
                        if (whenChanged != null)
                            whenChanged.onChanged(new RealtimeField(fieldLocation, snapshot.getValue()));
                        reference.removeEventListener(this);
                    } else
                        isFirst[0] = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        public static ValueEventListenerStopper waitForFieldToHaveThisSpecificValueAsync(String fieldLocation, Object value, OrValueChangedListener whenChanged) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference reference = database.getReference().child(fieldLocation);
            return new ValueEventListenerStopper(reference, reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && value.equals(snapshot.getValue())) {
                        if (whenChanged != null)
                            whenChanged.onChanged(new RealtimeField(fieldLocation, snapshot.getValue()));
                        reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        public static ChildEventListenerStopper waitForNChildrenToBeInTheCollectionAndThenDoSomethingAsync(String collectionLocation, OrNChildrenAddedListener whenAdded, int n) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference reference = database.getReference().child(collectionLocation);
            RealtimeField[] children = new RealtimeField[n];
            int[] currentChild = new int[1];
            return new ChildEventListenerStopper(reference, reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    RealtimeField field = new RealtimeField(collectionLocation+CloudConstants.cloudLocationSeparator+snapshot.getKey(), snapshot.getValue());
                    children[currentChild[0]++] = field;
                    if (whenAdded != null)
                        whenAdded.onChildAdded(field);
                    if (currentChild[0] == n) {
                        if (whenAdded != null)
                            whenAdded.onAllAdded(children);
                        reference.removeEventListener(this);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        public static ChildEventListenerStopper waitForASpecificChildToBeAddedToCollectionAndThenDoSomethingAsync(String collectionLocation, OrSpecificChildAddedListener whenAdded) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference reference = database.getReference().child(collectionLocation);
            return new ChildEventListenerStopper(reference, reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (whenAdded.isItTheCorrectChild(new RealtimeField(collectionLocation+CloudConstants.cloudLocationSeparator+snapshot.getKey(), snapshot.getValue()))) {
                        whenAdded.onCorrectChildAdded(new RealtimeField(collectionLocation+CloudConstants.cloudLocationSeparator+snapshot.getKey(), snapshot.getValue()));
                        reference.removeEventListener(this);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        //First gets triggered for all of the existing children
        public static ChildEventListenerStopper listenToNewChildrenAsync(String collectionLocation, OrChildAddedListener whenAdded) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference reference = database.getReference().child(collectionLocation);
            return new ChildEventListenerStopper(reference, reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (whenAdded != null)
                        whenAdded.onChildAdded(new RealtimeField(collectionLocation+CloudConstants.cloudLocationSeparator+snapshot.getKey(), snapshot.getValue()));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        public static ValueEventListenerStopper listenToValueChangesAsync(String fieldLocation, OrValueChangedListener whenChanged) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            final boolean[] isFirst = new boolean[1];
            isFirst[0] = true;
            DatabaseReference reference = database.getReference().child(fieldLocation);
            return new ValueEventListenerStopper(reference, reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!isFirst[0]) {
                        if (whenChanged != null)
                            whenChanged.onChanged(new RealtimeField(fieldLocation, snapshot.getValue()));
                    } else
                         isFirst[0] = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        public static void updateRealtimeFieldAndDoSomethingWhenCompletedAsync(RealtimeField field, Runnable whenCompleted) {
            RealtimeHelperAsync.updateRealtimeFieldAndReturnTaskAsync(field).addOnCompleteListener(task -> {
                if (whenCompleted != null)
                    whenCompleted.run();
            });
        }

        public static void removeRealtimeFieldAndDoSomethingWhenCompletedAsync(String fieldLocation, Runnable whenCompleted) {
            RealtimeHelperAsync.removeRealtimeFieldAtLocationAndReturnTaskAsync(fieldLocation).addOnCompleteListener(task -> {
                if (whenCompleted != null)
                    whenCompleted.run();
            });
        }

        public static ValueEventListenerStopper waitForAFieldToBeNonExistentAndThenDoSomethingAsync(String fieldLocation, Runnable whenRemoved) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference reference = database.getReference().child(fieldLocation);
            return new ValueEventListenerStopper(reference, reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        if (whenRemoved != null)
                            whenRemoved.run();
                        reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }

        public static ValueEventListenerStopper waitForAFieldToBeRemovedAndThenDoSomethingAsync(String fieldLocation, Runnable whenRemoved) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://math-revisor-default-rtdb.europe-west1.firebasedatabase.app");
            final boolean[] isFirst = new boolean[1];
            isFirst[0] = true;
            DatabaseReference reference = database.getReference().child(fieldLocation);
            return new ValueEventListenerStopper(reference, reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!isFirst[0]) {
                        if (!snapshot.exists()) {
                            if (whenRemoved != null)
                                whenRemoved.run();
                            reference.removeEventListener(this);
                        }
                    } else
                        isFirst[0] = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }));
        }
    }

}
