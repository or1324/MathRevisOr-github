package or.nevet.orcloud.listener_stoppers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ValueEventListenerStopper {
    DatabaseReference listenerReference;
    ValueEventListener listener;
    public ValueEventListenerStopper(DatabaseReference reference, ValueEventListener listener) {
        listenerReference = reference;
        this.listener = listener;
    }

    public void removeListener() {
        listenerReference.removeEventListener(listener);
    }
}
