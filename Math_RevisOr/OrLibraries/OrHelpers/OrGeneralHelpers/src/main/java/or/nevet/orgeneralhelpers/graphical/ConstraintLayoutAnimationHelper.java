package or.nevet.orgeneralhelpers.graphical;

import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.animation.BounceInterpolator;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.GreetingConstants;

public class ConstraintLayoutAnimationHelper {
    ConstraintLayout layout;
    public ConstraintLayoutAnimationHelper(ConstraintLayout parent) {
        this.layout = parent;
    }

    //This method can be started before the views are rendered.
    public void animateConnectionLeftToRightAndRunnableChanges(int movingViewId, int viewToAnimateToId, Runnable r) {
        layout.post(() -> {
            Transition transition = new ChangeBounds();
            transition.setDuration(GeneralConstants.constraintAnimationDuration);
            transition.setInterpolator(new BounceInterpolator());
            TransitionManager.beginDelayedTransition(layout, transition);
            r.run();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.connect(movingViewId, ConstraintSet.LEFT, viewToAnimateToId, ConstraintSet.RIGHT);
            constraintSet.applyTo(layout);
        });
    }

    //This method can be started before the views are rendered.
    public void animateConnectionLeftToLeftAndRunnableChanges(int movingViewId, int viewToAnimateToId, Runnable r) {
        layout.post(() -> {
            Transition transition = new ChangeBounds();
            transition.setDuration(GeneralConstants.constraintAnimationDuration);
            transition.setInterpolator(new BounceInterpolator());
            TransitionManager.beginDelayedTransition(layout, transition);
            r.run();
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.connect(movingViewId, ConstraintSet.LEFT, viewToAnimateToId, ConstraintSet.LEFT);
            constraintSet.applyTo(layout);
        });
    }
}
