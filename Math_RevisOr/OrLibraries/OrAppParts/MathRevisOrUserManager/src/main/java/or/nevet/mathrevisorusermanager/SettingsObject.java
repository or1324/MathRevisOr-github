package or.nevet.mathrevisorusermanager;

public class SettingsObject {

    private final Settings settings;

    SettingsObject(Settings settings) {
        this.settings = settings;
    }

    public boolean isLockScreenModeOn() {
        return settings.isLockScreenModeOn();
    }

    public char getLockScreenExercisesSign() {
        return settings.getLockScreenExercisesSign();
    }
}
