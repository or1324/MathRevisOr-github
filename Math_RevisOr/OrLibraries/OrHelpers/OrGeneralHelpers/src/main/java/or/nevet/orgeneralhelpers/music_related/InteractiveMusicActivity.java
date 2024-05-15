package or.nevet.orgeneralhelpers.music_related;

import androidx.appcompat.widget.AppCompatImageButton;

import or.nevet.orgeneralhelpers.graphical.AppGraphics;

public abstract class InteractiveMusicActivity extends MusicActivity implements InteractiveMusicThing {

    @Override
    protected void onResume() {
        super.onResume();
        AppGraphics.onStartOfInteractiveMusic(this);
    }

    public abstract AppCompatImageButton getMusicButton();
}
