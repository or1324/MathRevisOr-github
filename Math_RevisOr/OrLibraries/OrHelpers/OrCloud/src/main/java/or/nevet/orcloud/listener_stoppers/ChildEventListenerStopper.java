package or.nevet.orcloud.listener_stoppers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ChildEventListenerStopper {
    DatabaseReference listenerReference;
    ChildEventListener listener;
    public ChildEventListenerStopper(DatabaseReference reference, ChildEventListener listener) {
        listenerReference = reference;
        this.listener = listener;
    }

    public void removeListener() {
        listenerReference.removeEventListener(listener);
    }
}
