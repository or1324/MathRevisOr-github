package or.nevet.orgeneralhelpers.background_running_related;

import java.util.Timer;
import java.util.TimerTask;

import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;

public class RunOnEachInterval {
    private final TimersContainer timersContainer;
    public enum BlockingMethod {
        RunOnUi, RunOnBackground
    }
    private final BlockingMethod blockingMethod;

    public RunOnEachInterval(int maxNumOfTimersPerMoment, BlockingMethod blockingMethod) {
        timersContainer = new TimersContainer(maxNumOfTimersPerMoment);
        this.blockingMethod = blockingMethod;
    }

    //Returns an identifier of the created timer to allow manual stopping.
    public TimerReference startRunning(Runnable r, long intervalMillis) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (blockingMethod) {
                    case RunOnUi:
                        BackgroundRunningHelper.runCodeOnUiThread(r);
                        break;
                    case RunOnBackground:
                        BackgroundRunningHelper.runCodeInBackgroundAsync(r);
                        break;
                    default:
                        throw new RuntimeException(UserMessagesConstants.forgotAfterAddingBlockingMethod);
                }
            }
        }, 0, intervalMillis);
        return timersContainer.addNewTimer(timer);
    }
    public void stop(TimerReference reference) {
        timersContainer.stopTimer(reference);
    }

    //stops everything
    public void stopAll() {
        timersContainer.stopAll();
    }
}
