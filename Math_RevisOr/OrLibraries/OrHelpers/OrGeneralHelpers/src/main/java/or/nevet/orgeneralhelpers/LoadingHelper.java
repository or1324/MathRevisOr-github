package or.nevet.orgeneralhelpers;

import android.view.View;
import android.widget.ProgressBar;

import java.util.HashMap;

import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;

public class LoadingHelper {

    boolean canAccess;
    ProgressBar progressBar;

    public LoadingHelper(ProgressBar progressBar) {
        canAccess = true;
        this.progressBar = progressBar;
    }

    public void loadAndRunIfAllowed(Runnable r) {
        if (canAccess) {
            if (BackgroundRunningHelper.isOnUiThread())
                progressBar.setVisibility(View.VISIBLE);
            else
                BackgroundRunningHelper.runCodeOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
            canAccess = false;
            r.run();
        }
    }

    public void finishedRunning() {
        if (BackgroundRunningHelper.isOnUiThread())
            progressBar.setVisibility(View.GONE);
        else
            BackgroundRunningHelper.runCodeOnUiThread(() -> progressBar.setVisibility(View.GONE));
        canAccess = true;
    }

}
