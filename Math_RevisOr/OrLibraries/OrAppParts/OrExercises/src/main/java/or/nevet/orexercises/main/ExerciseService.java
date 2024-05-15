package or.nevet.orexercises.main;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import or.nevet.mathrevisorusermanager.SettingsManager;
import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.PowerReceiver;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.exercise_helpers.EnhancedExerciseHelper;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.learned_exercise_manager.LearnedExerciseManager;
import or.nevet.orexercises.helpers.logic.interfaces.ExerciseScreen;
import or.nevet.orexercises.helpers.visual.AppGraphics;
import or.nevet.orexercises.helpers.visual.exercise_keyboard.ExerciseKeyboard;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.background_running_related.RunOnEachInterval;
import or.nevet.orgeneralhelpers.background_running_related.TimerReference;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.tts.OrTTS;

public class ExerciseService extends AccessibilityService implements ExerciseScreen {
    public static boolean isRunning = false;
    public View view;
    PowerReceiver powerReceiver;
    public static final int numOfSeconds = 5;
    int seconds = numOfSeconds;
    public EnhancedExerciseHelper exerciseHelper;
    private TimerReference timerReference;
    private RunOnEachInterval runOnEachInterval;
    private ConstraintLayout background;
    private AppCompatTextView errorView;
    private AppCompatTextView timeView;
    private AppCompatImageButton giveUp;
    private ExerciseKeyboard keyboard;
    private Thread notifyUserThread = null;
    private boolean isGiveUp = false;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //creates RunOnEachInterval
        runOnEachInterval = new RunOnEachInterval(1, RunOnEachInterval.BlockingMethod.RunOnUi);
        //creates receiver which registers to the screen on and screen off events
        powerReceiver = new PowerReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(powerReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }

    //locks the user until he solves the exercise
    public void lockTheUserInExercise() {
        stop();
        //creates the exercise helper by the last chosen exercise sign
        exerciseHelper = new EnhancedExerciseHelper(LearnedExerciseManager.getSpecificSavedLearnedExercisesBySign(SettingsManager.getSettingsObject().getLockScreenExercisesSign(), getApplicationContext()), getApplicationContext());
        isRunning = true;
        showOverlayAndBlockTouches();
        keyboard = new ExerciseKeyboard(view, exerciseHelper.getExercises().getExerciseSign(), this);
        keyboard.setOnShowKeyboardListener(() -> {
            if (notifyUserThread != null)
                notifyUserThread.interrupt();
        });
        AppGraphics.initExerciseService(this);
        isGiveUp = false;
        //if I will uncomment this, it will be impossible to exit this app without solving the exercise or giving up. (if I do not use this, it is possible to enter the phone by clicking the settings app).
        //blockTheUserFromEnteringOtherApps();
    }

    @Override
    public void exitScreen(View v) {
        stop();
    }

    @Override
    public void answerExercise() {
        try {
            if (exerciseHelper.isTheAnswerRight(keyboard.getInputView()))
                exitScreen(view);
            else
                notifyUser(UserMessagesConstants.wrongAnswer);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void nextAfterAnswerShown() {
        exitScreen(view);
    }

    @Override
    public ConstraintLayout getBackground() {
        return background;
    }

    @Override
    public void setBackground(ConstraintLayout background) {
        this.background = background;
    }

    public void setErrorView(AppCompatTextView errorView) {
        this.errorView = errorView;
    }

    @Override
    public ExerciseKeyboard getExerciseKeyboard() {
        return keyboard;
    }

    @Override
    public boolean getIsInWaitingForUserToClickOnNextProcess() {
        return isGiveUp;
    }

    @Override
    public void showAnswer(int correctAnswer) {
        runOnEachInterval.stop(timerReference);
        timeView.setText("");
        keyboard.showAnswer(correctAnswer);
    }

    public void showExerciseScreen() {
        BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
            isRunning = true;
            if (waitUntilUserUnlocksScreen()) {
                BackgroundRunningHelper.runCodeOnUiThread(() -> lockTheUserInExercise());
            }
        });
    }

    //shows the overlay and block touches to make sure that the user does not leave the screen until he succeeds the exercise
    private void showOverlayAndBlockTouches() {
        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        localLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        localLayoutParams.format = PixelFormat.TRANSPARENT;
        LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        view = (ConstraintLayout) inflater.inflate(R.layout.activity_power_up,null);
        manager.addView(view, localLayoutParams);
    }

    //blocks the user completely.
    private void blockTheUserFromEnteringOtherApps() {
        BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
            while (isRunning) {
                //return to home screen each 200 milli seconds
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //waits until the user completes to enter his password
    private boolean waitUntilUserUnlocksScreen() {
        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        while (myKM.isKeyguardLocked() && isRunning) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isRunning;
    }

    //shows text to the user
    public void notifyUser(String message) {
        keyboard.hideKeyboard();
        errorView.setText(message);
        notifyUserThread = BackgroundRunningHelper.runOnUiAfterSomeTimeAsync(() -> {
            errorView.setText("");
            keyboard.showKeyboard();
        }, 2* GeneralConstants.secondInMillis);
    }

    //gives the answer to the user
    public void help() {
        isGiveUp = true;
        AppCompatImageButton help = view.findViewById(R.id.give_up);
        help.setVisibility(View.GONE);
        if (notifyUserThread != null)
            notifyUserThread.interrupt();
        errorView.setText("");
        startTimer();
    }

    //frees the user
    public void stop() {
        if (notifyUserThread != null)
            notifyUserThread.interrupt();
        runOnEachInterval.stopAll();
        isRunning = false;
        UserManager.removeUserFromMemory();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            manager.removeView(view);
        }
        catch (Exception e) {

        }
    }

    public void setTimeView(AppCompatTextView timeView) {
        this.timeView = timeView;
    }

    @Override
    public void startTimer() {
        keyboard.hideKeyboard();
        keyboard.disableInput();
        seconds = numOfSeconds;
        //initialize the tts in advance to make things faster.
        OrTTS.createTTS(this, succeeded -> {});
        timerReference = runOnEachInterval.startRunning(() -> {
            if (seconds == 0) {
                showAnswer(exerciseHelper.getCorrectAnswer());
                exerciseHelper.readExercise();
            } else
                timeView.setText((seconds--)+"");
        }, GeneralConstants.secondInMillis);
    }

    @Override
    public void setGiveUp(AppCompatImageButton giveUp) {
        this.giveUp = giveUp;
    }

    @Override
    public AppCompatImageButton getGiveUp() {
        return giveUp;
    }

}