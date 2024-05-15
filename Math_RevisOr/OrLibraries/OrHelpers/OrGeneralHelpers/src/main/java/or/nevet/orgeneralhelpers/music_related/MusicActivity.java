package or.nevet.orgeneralhelpers.music_related;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public abstract class MusicActivity extends AppCompatActivity {

    public enum ActivityState {
        Paused, Started
    }

    private ActivityState lastState = ActivityState.Paused;
    //needed in order to prevent opening 2 activities at the same time.
    private boolean canMoveToAnotherActivity = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lastState = ActivityState.Paused;
        ActivityOpeningHelper.onActivityPaused();
        this.overridePendingTransition(0,0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        canMoveToAnotherActivity = true;
    }

    public void onAnotherActivityOpenedFromThisActivity() {
        canMoveToAnotherActivity = false;
    }

    public boolean canMoveToAnotherActivity() {
        return canMoveToAnotherActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        canMoveToAnotherActivity = true;
        lastState = ActivityState.Started;
        ActivityOpeningHelper.onActivityOpened(this);
        this.overridePendingTransition(0,0);
    }

    public ActivityState getLastState() {
        return lastState;
    }

    public void onBackPressed() {
        if (canMoveToAnotherActivity)
            finish();
    }
}