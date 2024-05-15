package or.nevet.math_revisor.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import or.nevet.math_revisor.R;
import or.nevet.mathrevisorusermanager.SettingsManager;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orgeneralhelpers.graphical.graphical_concepts.LoadingComponent;
import or.nevet.orgeneralhelpers.music_related.AppMusicService;
import or.nevet.orgeneralhelpers.tts.OrTTS;

public class SplashScreen extends AppCompatActivity implements LoadingComponent {

    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        or.nevet.math_revisor.helpers.AppGraphics.initSplashScreen(this);
        try {
            SettingsManager.restoreSettings(this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        AppMusicService.initMusic(this, () -> {
            //Needs to be created only once, so better to do this in the start.
            OrTTS.createTTS(SplashScreen.this, (succeeded) -> {
                if (succeeded) {
                    stopProgressBar();
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                } else
                    UserMessages.showEmptyDialogMessage(UserMessagesConstants.doesNotSupportTTS, SplashScreen.this);
            });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    @Override
    public void stopProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void restartProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}