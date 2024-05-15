package or.nevet.orgeneralhelpers.background_running_related;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Timer;

import or.nevet.orgeneralhelpers.background_running_related.exceptions.NotEnoughSpaceForTimerException;

public class TimersContainer {
    private final HashMap<Integer, Timer> timers;
    private final HashSet<Integer> allowedIds;
    private final int numOfIds;
    TimersContainer(int numOfIds) {
        this.numOfIds = numOfIds;
        timers = new HashMap<>();
        allowedIds = new HashSet<>(numOfIds);
        //starts from 1.
        for (int i = 1; i <= numOfIds; i++)
            allowedIds.add(i);
    }
    TimerReference addNewTimer(Timer timer) {
        if (!allowedIds.isEmpty()) {
            int timerId = allowedIds.iterator().next();
            allowedIds.remove(timerId);
            timers.put(timerId, timer);
            return new TimerReference(timerId);
        }
        throw new NotEnoughSpaceForTimerException();
    }

    void stopTimer(TimerReference reference) {
        if (reference != null) {
            int timerId = reference.getTimerId();
            if (timers.containsKey(timerId)) {
                //can never be lower than 1 so will not do anything by mistake.
                reference.setTimerId(0);
                timers.get(timerId).cancel();
                timers.get(timerId).purge();
                timers.remove(timerId);
                allowedIds.add(timerId);
            }
        }
    }

    void stopAll() {
        for (Timer t : timers.values())
            t.cancel();
        timers.clear();
        //no problem adding the same value twice since no duplicates in hashset.
        for (int i = 1; i < numOfIds; i++)
            allowedIds.add(i);
    }
}
