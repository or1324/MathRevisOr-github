package or.nevet.orcloud.cloud_listeners;

import or.nevet.orcloud.RealtimeField;

public interface OrSpecificChildAddedListener {
    boolean isItTheCorrectChild(RealtimeField field);

    void onCorrectChildAdded(RealtimeField field);
}
