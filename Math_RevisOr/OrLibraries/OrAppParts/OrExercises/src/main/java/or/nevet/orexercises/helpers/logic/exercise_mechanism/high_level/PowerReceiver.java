package or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import or.nevet.mathrevisorusermanager.SettingsManager;
import or.nevet.mathrevisorusermanager.SettingsObject;
import or.nevet.orexercises.main.ExerciseService;

public class PowerReceiver extends BroadcastReceiver {

    ExerciseService exerciseService;
    //Empty constructor needed
    public PowerReceiver(){}
    public PowerReceiver(ExerciseService l) {
        exerciseService = l;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            boolean isPracticeModeOn = getIsPracticeModeOn(context);
            if (isPracticeModeOn)
                exerciseService.showExerciseScreen();
        }
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            if (ExerciseService.isRunning) {
                exerciseService.stop();
            }
        }
    }

    private boolean getIsPracticeModeOn(Context context) {
        boolean isPracticeModeOn = false;
        try {
            SettingsManager.restoreSettings(context);
            SettingsObject object = SettingsManager.getSettingsObject();
            isPracticeModeOn = object.isLockScreenModeOn();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return isPracticeModeOn;
    }
}