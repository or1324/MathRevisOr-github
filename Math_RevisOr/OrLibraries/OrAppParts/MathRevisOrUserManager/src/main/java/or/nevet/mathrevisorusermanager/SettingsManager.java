package or.nevet.mathrevisorusermanager;

import android.content.Context;

public class SettingsManager {
    public static SettingsObject getSettingsObject() {
        return convertGeneralSettingsToSettings(or.nevet.orusermanager.SettingsManager.getSettingsObject());
    }
    public static void restoreSettings(Context context) {
        or.nevet.orusermanager.SettingsManager.restoreSettings(context, UserManagerConstants.defaultSettingsObject);
    }

    public static void saveLockScreenExerciseSign(char exercisesSign, Context context) {
        or.nevet.orusermanager.SettingsManager.updateSettingAndSaveInStorage(UserManagerConstants.settingsLockScreenExerciseSign, exercisesSign, context);
    }

    public static void switchIsLockScreenModeOnAndSave(Context context) {
        or.nevet.orusermanager.SettingsManager.updateSettingAndSaveInStorage(UserManagerConstants.settingsIsLockScreenModeEnabled, !getSettingsObject().isLockScreenModeOn(), context);
    }

    private static SettingsObject convertGeneralSettingsToSettings(or.nevet.orusermanager.SettingsObject settings) {
        return new SettingsObject(new Settings(settings.getProperty(UserManagerConstants.settingsIsLockScreenModeEnabled), settings.getProperty(UserManagerConstants.settingsLockScreenExerciseSign)));
    }
}
