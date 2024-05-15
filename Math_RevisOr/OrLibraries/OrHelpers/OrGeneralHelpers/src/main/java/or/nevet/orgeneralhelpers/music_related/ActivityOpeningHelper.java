package or.nevet.orgeneralhelpers.music_related;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

public class ActivityOpeningHelper {
    private static boolean appActivityOpens = false;
    private static Class<? extends MusicActivity> thisActivityIsClosingButOnResumeWillBeCalledSoon = null;

    //Also deals with music among other things. Insert extras = null for no extras.
    public static void openActivity(MusicActivity source, Class<? extends Activity> target, Bundle extras) {
        if (source.canMoveToAnotherActivity()) {
            source.onAnotherActivityOpenedFromThisActivity();
            if (source.getLastState() == MusicActivity.ActivityState.Paused) {
                //The onResume was not called yet, and during this time this activity opened a new activity. This is a problem, because in a moment the onResume will be called and the appActivityOpens variable will become false, and then the onPause of this activity will be called, and the music will stop. We need to prevent it from stopping.
                thisActivityIsClosingButOnResumeWillBeCalledSoon = source.getClass();
            }
            ActivityOpeningHelper.appActivityOpens = true;
            Intent intent = new Intent(source, target);
            if (extras != null)
                intent.putExtras(extras);
            source.startActivity(intent);
        }
    }

    public static void openActivityAndKillMe(MusicActivity source, Class<? extends Activity> target, Bundle extras, boolean forwardTheResultToMyParent) {
        if (source.canMoveToAnotherActivity()) {
            source.onAnotherActivityOpenedFromThisActivity();
            if (source.getLastState() == MusicActivity.ActivityState.Paused) {
                //The onResume was not called yet, and during this time this activity opened a new activity. This is a problem, because in a moment the onResume will be called and the appActivityOpens variable will become false, and then the onPause of this activity will be called, and the music will stop. We need to prevent it from stopping.
                thisActivityIsClosingButOnResumeWillBeCalledSoon = source.getClass();
            }
            ActivityOpeningHelper.appActivityOpens = true;
            Intent intent = new Intent(source, target);
            if (forwardTheResultToMyParent)
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            if (extras != null)
                intent.putExtras(extras);
            source.startActivity(intent);
            source.finish();
        }
    }

    //Also deals with music among other things. Insert extras = null for no extras.
    public static void openActivityForResult(MusicActivity source, Class<? extends Activity> target, Bundle extras, ActivityResultLauncher<Intent> launcher) {
        if (source.canMoveToAnotherActivity()) {
            source.onAnotherActivityOpenedFromThisActivity();
            if (source.getLastState() == MusicActivity.ActivityState.Paused) {
                //The onResume was not called yet, and during this time this activity opened a new activity. This is a problem, because in a moment the onResume will be called and the appActivityOpens variable will become false, and then the onPause of this activity will be called, and the music will stop. We need to prevent it from stopping.
                thisActivityIsClosingButOnResumeWillBeCalledSoon = source.getClass();
            }
            ActivityOpeningHelper.appActivityOpens = true;
            Intent intent = new Intent(source, target);
            if (extras != null)
                intent.putExtras(extras);
            launcher.launch(intent);
        }
    }

    //In order to deal with music
    static void onSubActivityFinished() {
        ActivityOpeningHelper.appActivityOpens = true;
    }

    //In order to deal with music
    static void onActivityOpened(MusicActivity activity) {
        if (activity.getClass() != thisActivityIsClosingButOnResumeWillBeCalledSoon) {
            ActivityOpeningHelper.appActivityOpens = false;
            AppMusicService.getInstance().resumeMusicIfUserWants();
        } else
            thisActivityIsClosingButOnResumeWillBeCalledSoon = null;
    }

    //In order to deal with music
    static void onActivityPaused() {
        if (!appActivityOpens)
            AppMusicService.getInstance().pauseMusic();
    }
}
