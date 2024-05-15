package or.nevet.orgeneralhelpers.background_running_related;

public class TimerReference {
    private int timerId;
    public TimerReference(int timerId) {
        this.timerId = timerId;
    }

    void setTimerId(int timerId) {
        this.timerId = timerId;
    }

    int getTimerId() {
        return timerId;
    }
}
