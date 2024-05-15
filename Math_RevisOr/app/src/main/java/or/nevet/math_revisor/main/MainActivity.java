package or.nevet.math_revisor.main;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Calendar;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.Permissions;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.GreetingConstants;
import or.nevet.orgeneralhelpers.graphical.activity_types.InteractiveMusicAlertDialogActivity;
import or.nevet.orgeneralhelpers.music_related.AppMusicService;
import or.nevet.orgeneralhelpers.graphical.ConstraintLayoutAnimationHelper;

public class MainActivity extends InteractiveMusicAlertDialogActivity {
    long lastClickTime = 0;
    public ImageButton multiply_button;
    public ImageButton divide_button;
    public ImageButton stats;
    public ImageButton settings;
    public AppCompatImageButton leaderboard;
    public AppCompatImageButton music;
    public AppCompatTextView blessing;
    public ConstraintLayout navigationBar;
    public LottieAnimationView info;
    public AppCompatImageButton customExercises;
    private AlertDialog alertDialog;
    public Button multiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppMusicService.getInstance().startMusicIfUserWants();
        AppGraphics.initMain(MainActivity.this);
        Permissions.checkForDrawPermissions(this);
        alertDialog = Permissions.checkForAccessibilityPermissions(this);
    }

    public boolean isTooFastDoubleClickThatWillCauseDoubleOpening() {
        if(SystemClock.elapsedRealtime()-lastClickTime < 300){
            return true;
        }
        //update last click time
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationBar.post(() -> setGreeting());
    }

    @Override
    public AppCompatImageButton getMusicButton() {
        return music;
    }

    private void setGreeting() {
        ConstraintLayoutAnimationHelper animationHelper = new ConstraintLayoutAnimationHelper(navigationBar);
        animationHelper.animateConnectionLeftToRightAndRunnableChanges(blessing.getId(), music.getId(), () -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String userName = UserManager.getCurrentUserObject(this).getUserName();
            if (hour <= 5 || hour >= 17) {
                //night
                blessing.setText(GreetingConstants.goodNight+" "+userName+"!");
            }
            if (hour >= 6 && hour <= 11) {
                //morning
                blessing.setText(GreetingConstants.goodMorning+" "+userName+"!");
            }
            if (hour == 12) {
                blessing.setText(GreetingConstants.goodNoon+" "+userName+"!");
            }
            if (hour >= 13 && hour <= 16) {
                //noon
                blessing.setText(GreetingConstants.goodAfternoon+" "+userName+"!");
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}