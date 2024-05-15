package or.nevet.math_revisor.main;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatImageButton;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;

public class SettingsActivity extends MusicSubActivity {
    public AppCompatImageButton back;
    public AppCompatImageButton logout;
    public AppCompatImageButton practiceMode;
    public Spinner exercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AppGraphics.initSettings(this);
    }
}
