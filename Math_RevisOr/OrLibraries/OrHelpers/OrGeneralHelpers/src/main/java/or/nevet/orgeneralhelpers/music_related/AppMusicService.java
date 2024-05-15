package or.nevet.orgeneralhelpers.music_related;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;

import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.R;
import or.nevet.orgeneralhelpers.SharedPreferencesStorage;
import or.nevet.orgeneralhelpers.constants.MusicConstants;

public class AppMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private static Boolean play;
    private static AppMusicService runningService = null;
    private static Runnable afterMusicStarts;

    public static AppMusicService getInstance() {
        return runningService;
    }

    public void changeMusicState() {
        if (isMusicPlaying()) {
            play = false;
            saveUserMusicPreference(this);
            pauseMusic();
        } else {
            play = true;
            saveUserMusicPreference(this);
            resumeMusicIfUserWants();
        }
    }

    public void resetMusic() {
        stopMusic();
        SharedPreferencesStorage.clearDatabase(MusicConstants.MUSIC_SHARED_PREFS, this);
    }

    private static void saveUserMusicPreference(Context context) {
        SharedPreferencesStorage.saveBoolean(MusicConstants.MUSIC_SHARED_PREFS, MusicConstants.IsPlaying, play, context);
    }

    private static boolean getUserMusicPreference(Context context) {
        return SharedPreferencesStorage.getBoolean(MusicConstants.MUSIC_SHARED_PREFS, MusicConstants.IsPlaying, true, context);
    }

    public static void initMusic(Context context, Runnable afterMusicStarts) {
        AppMusicService.afterMusicStarts = afterMusicStarts;
        play = getUserMusicPreference(context);
        Intent intent = new Intent(context, AppMusicService.class);
        context.startService(intent);
    }

    public void startMusicIfUserWants() {
        if(mediaPlayer != null && play && !mediaPlayer.isPlaying()) {
            mediaPlayer.setVolume((float) 0.08, (float) 0.08);
            mediaPlayer.start();
        }
    }

    public void resumeMusicIfUserWants() {
        play = getUserMusicPreference(this);
        if(mediaPlayer!=null&&!mediaPlayer.isPlaying() && play){
            mediaPlayer.setVolume((float) 0.08, (float) 0.08);
            mediaPlayer.start();
        }
    }
    public void stopMusic() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            play = false;
            saveUserMusicPreference(this);
        }
    }

    public void pauseMusic() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public boolean isMusicPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void changeMusicButtonIconByMusicState(AppCompatImageButton imageButton) {
        if (isMusicPlaying()) {
            imageButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.sound));
        } else
            imageButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.mute));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = MediaPlayer.create(this, R.raw.believe);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume((float) 0.08, (float) 0.08);
        runningService = this;
        if (afterMusicStarts != null)
            afterMusicStarts.run();
        return super.onStartCommand(intent, flags, startId);
    }
}
