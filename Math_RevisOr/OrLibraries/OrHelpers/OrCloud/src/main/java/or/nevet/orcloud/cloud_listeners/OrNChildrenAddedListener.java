package or.nevet.orcloud.cloud_listeners;

import java.util.LinkedList;

import or.nevet.orcloud.RealtimeField;

public interface OrNChildrenAddedListener {
    void onAllAdded(RealtimeField[] fields);
    void onChildAdded(RealtimeField field);
}
