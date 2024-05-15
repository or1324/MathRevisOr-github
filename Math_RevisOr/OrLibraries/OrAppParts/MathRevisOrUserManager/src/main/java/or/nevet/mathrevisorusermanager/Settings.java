package or.nevet.mathrevisorusermanager;

import java.io.Serializable;

class Settings implements Serializable {
    private boolean isLockScreenModeOn;
    private char lockScreenExercisesSign;
    public Settings(boolean isLockScreenModeOn, char lockScreenExercisesSign) {
        this.isLockScreenModeOn = isLockScreenModeOn;
        this.lockScreenExercisesSign = lockScreenExercisesSign;
    }

    public char getLockScreenExercisesSign() {
        return lockScreenExercisesSign;
    }

    public boolean isLockScreenModeOn() {
        return isLockScreenModeOn;
    }

    public void setLockScreenExercisesSign(char lockScreenExerciseSign) {
        this.lockScreenExercisesSign = lockScreenExerciseSign;
    }

    public void setLockScreenModeOn(boolean lockScreenModeOn) {
        isLockScreenModeOn = lockScreenModeOn;
    }
}
