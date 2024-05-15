package or.nevet.orcloud;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class FirestoreHelper {

    //blocks the current thread. Should be called by another thread.
    public static List<DocumentSnapshot> getAllDocumentsInCollectionDescendingOrder(String collectionPath, String fieldName) throws Exception {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseSynchronizer<QuerySnapshot> synchronizer = new FirebaseSynchronizer<>();
        QuerySnapshot docsSnapshot = synchronizer.waitForTaskToFinish(firestore.collection(collectionPath).orderBy(fieldName, Query.Direction.DESCENDING).get());
        List<DocumentSnapshot> docsList = docsSnapshot.getDocuments();
        return docsList;
    }

    //blocks the current thread. Should be called by another thread.
    public static void uploadDocumentByMap(String path, Map<String, Object> data) throws Exception {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseSynchronizer<Void> synchronizer = new FirebaseSynchronizer<>();
        synchronizer.waitForTaskToFinish(firestore.document(path).set(data));
    }

    //blocks the current thread. Should be called by another thread.
    public static void uploadDocumentByObject(String path, Object object) throws Exception {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseSynchronizer<Void> synchronizer = new FirebaseSynchronizer<>();
        synchronizer.waitForTaskToFinish(firestore.document(path).set(object));
    }

    //blocks the current thread. Should be called by another thread.
    public static DocumentSnapshot getDocumentFromCloud(String documentPath) throws Exception {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseSynchronizer<DocumentSnapshot> synchronizer = new FirebaseSynchronizer<>();
        DocumentSnapshot snapshot = synchronizer.waitForTaskToFinish(firestore.document(documentPath).get());
        return snapshot;
    }

    public static List<DocumentSnapshot> getDocumentsInCollectionWithField(String collectionPath, String fieldName, String fieldValue) throws Exception {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseSynchronizer<QuerySnapshot> synchronizer = new FirebaseSynchronizer<>();
        QuerySnapshot snapshot = synchronizer.waitForTaskToFinish(firestore.collection(collectionPath).whereEqualTo(fieldName, fieldValue).get());
        return snapshot.getDocuments();
    }

}
