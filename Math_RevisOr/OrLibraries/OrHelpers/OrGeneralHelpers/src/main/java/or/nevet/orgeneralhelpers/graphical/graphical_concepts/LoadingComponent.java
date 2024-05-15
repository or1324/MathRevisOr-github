package or.nevet.orgeneralhelpers.graphical.graphical_concepts;

import android.widget.ProgressBar;

public interface LoadingComponent {
    void stopProgressBar();
    void restartProgressBar();
    ProgressBar getProgressBar();
}
