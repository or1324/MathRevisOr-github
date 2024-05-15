package or.nevet.orgeneralhelpers.graphical.activity_types;

import androidx.appcompat.app.AlertDialog;

import or.nevet.orgeneralhelpers.music_related.InteractiveMusicActivity;

public abstract class InteractiveMusicAlertDialogActivity extends InteractiveMusicActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = getAlertDialog();
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    public abstract AlertDialog getAlertDialog();
}
