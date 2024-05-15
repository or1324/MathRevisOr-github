package or.nevet.orgeneralhelpers.music_related;

import android.view.View;

public abstract class MusicSubActivity extends MusicActivity implements View.OnClickListener {

    @Override
    public void finish() {
        ActivityOpeningHelper.onSubActivityFinished();
        super.finish();
    }

    //back button clicked
    @Override
    public void onClick(View v) {
        if (canMoveToAnotherActivity())
            finish();
    }
}
