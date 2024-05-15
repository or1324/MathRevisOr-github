package or.nevet.orgeneralhelpers.graphical;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;

//Works only on views with fixed size that was pre-defined using both setWidth/setHeight and setLayoutParams.
public class MoveCutViewContainOr extends LinearLayout {

    private boolean wasMoving = false;
    private int previousX;
    private int previousY;
    private View child;
    private int viewWidth;
    private int viewHeight;
    private int containerHeight;
    private int containerWidth;
    private boolean wasCreated = false;
    private Runnable onCreated;
    enum Direction {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight,
    }

    public MoveCutViewContainOr(@NonNull Context context) {
        super(context);
        listenForCreation();
    }

    public MoveCutViewContainOr(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listenForCreation();
    }

    private void listenForCreation() {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!wasCreated) {
                    wasCreated = true;
                    if (onCreated != null)
                        onCreated.run();
                }
            }
        });
    }

    public void initView(View child) {
        child.measure(0, 0);
        viewHeight = child.getMeasuredHeight();
        viewWidth = child.getMeasuredWidth();
        //make the child gone until the initialization process completes, because during the initialization process the view is being created in the top left corner and then he is being moved and it looks bad.
        child.setVisibility(GONE);
        addView(child);
        this.child = child;
    }

    //should be used for showing the view in case that the container was created vs xml.
    public void askForShow() {
        if (child == null)
            throw new RuntimeException(UserMessagesConstants.calledShowBeforeInitView);
        //The container must be visible in order for the him to be rendered and in order for onLayoutChange to be called.
        setVisibility(View.VISIBLE);
        if (!wasCreated) {
            onCreated = () -> {
                containerHeight = getMeasuredHeight();
                containerWidth = getMeasuredWidth();
                show();
            };
        } else {
            containerHeight = getMeasuredHeight();
            containerWidth = getMeasuredWidth();
            show();
        }
    }

    private void show() {
        if (viewHeight < containerHeight && viewWidth < containerWidth) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
            params.leftMargin = (containerWidth - viewWidth) / 2;
            params.rightMargin = -viewWidth;
            params.topMargin = (containerHeight - viewHeight) / 2;
            params.bottomMargin = -viewHeight;
        }
        //makes the child visible to show it to the user.
        child.setVisibility(VISIBLE);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getX() >= child.getX() && motionEvent.getX() <= child.getX() + viewWidth && motionEvent.getY() >= child.getY() && motionEvent.getY() <= child.getY() + viewHeight) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //it does not matter if I have edittexts because this will not be called anyway. If I do not have edittexts, it needs to be true in order for me to get the next move events.
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            wasMoving = true;
                            //The distance from the view's left (The view keeps moving, So the distance is the start point plus the amount of distance moved since the last movement of the view).
                            int x = (int) motionEvent.getX();
                            //The distance from the view's top (The view keeps moving, So the distance is the start point plus the amount of distance moved since the last movement of the view).
                            int y = (int) motionEvent.getY();
                            int distanceSinceLastMoveX = x - previousX;
                            int distanceSinceLastMoveY = y - previousY;
                            System.out.println(distanceSinceLastMoveX);
                            if (!isConsideredClick(distanceSinceLastMoveX, distanceSinceLastMoveY)) {
                                Direction direction = whereDoesTheViewNeedToMoveTo(x, y);
                                boolean canGoX = false;
                                boolean canGoY = false;
                                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                                if (direction == Direction.BottomRight || direction == Direction.TopRight)
                                    if (child.getX() + distanceSinceLastMoveX <= 0)
                                        canGoX = true;
                                if (direction == Direction.TopLeft || direction == Direction.TopRight)
                                    if (child.getY() + viewHeight + distanceSinceLastMoveY >= containerHeight)
                                        canGoY = true;
                                if (direction == Direction.BottomLeft || direction == Direction.BottomRight)
                                    if (child.getY() + distanceSinceLastMoveY <= 0)
                                        canGoY = true;
                                if (direction == Direction.TopLeft || direction == Direction.BottomLeft)
                                    if (child.getX() + viewWidth + distanceSinceLastMoveX >= containerWidth)
                                        canGoX = true;
                                if (viewWidth > containerWidth) {
                                    if (canGoX) {
                                        int left = lp.leftMargin + distanceSinceLastMoveX;
                                        lp.leftMargin = left;
                                    } else {
                                        if (direction == Direction.BottomRight || direction == Direction.TopRight)
                                            lp.leftMargin = 0;
                                        else
                                            lp.leftMargin = containerWidth - viewWidth;
                                    }
                                    lp.rightMargin = -viewWidth;
                                }
                                if (viewHeight > containerHeight) {
                                    if (canGoY) {
                                        int top = lp.topMargin + distanceSinceLastMoveY;
                                        lp.topMargin = top;
                                    } else {
                                        if (direction == Direction.BottomLeft || direction == Direction.BottomRight)
                                            lp.topMargin = 0;
                                        else
                                            lp.topMargin = containerHeight - viewHeight;
                                    }
                                    lp.bottomMargin = -viewHeight;
                                }
                                child.setLayoutParams(lp);
                                previousX = x;
                                previousY = y;
                                return true;
                            } else {
                                wasMoving = false;
                                return false;
                            }
                        case MotionEvent.ACTION_UP:
                            if (!wasMoving) {
                                return true;
                            } else {
                                wasMoving = false;
                                return false;
                            }
                    }
                }
                return false;
            }
        });
    }

    //shows and sets the width and the height programmatically (needed in case that the view was not created via xml)
    public void show(int containerWidth, int containerHeight) {
        if (child == null)
            throw new RuntimeException(UserMessagesConstants.calledShowBeforeInitView);
        setLayoutParams(new ConstraintLayout.LayoutParams(containerWidth, containerHeight));
        this.containerHeight = containerHeight;
        this.containerWidth = containerWidth;
        show();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getX() >= child.getX() && motionEvent.getX() <= child.getX()+viewWidth && motionEvent.getY() >= child.getY() && motionEvent.getY() <= child.getY()+viewHeight) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //needs to set these here because the event will not be consumed by the ontouchlistener when we have edittexts because they will consume it before and block my layout.
                    previousX = (int) motionEvent.getX();
                    previousY = (int) motionEvent.getY();
                    //returns false in order for regular clicks on edittexts to work. My ontouchlistener will probably not get this event as a result of that.
                    return false;
                case MotionEvent.ACTION_MOVE:
                    //The distance from the view's left (The view keeps moving, So the distance is the start point plus the amount of distance moved since the last movement of the view).
                    int x = (int) motionEvent.getX();
                    //The distance from the view's top (The view keeps moving, So the distance is the start point plus the amount of distance moved since the last movement of the view).
                    int y = (int) motionEvent.getY();
                    int distanceSinceLastMoveX = x - previousX;
                    int distanceSinceLastMoveY = y - previousY;
                    if (!isConsideredClick(distanceSinceLastMoveX, distanceSinceLastMoveY))
                        //returns true for my ontouchlistener to get this event.
                        return true;
                    else
                        //returns false for the view to recieve this event, since this is basically a click.
                        return false;
                case MotionEvent.ACTION_UP:
                    if (!wasMoving) {
                        //returns false in order for the views to consume the click.
                        return false;
                    } else {
                        wasMoving = false;
                        //returns true in order to block the views from thinking that it was a click.
                        return false;
                    }
            }
        }
        return false;
    }

    private Direction whereDoesTheUserSeeMoreContentFrom(int x, int y) {
        Direction direction =  getSwipeDirection(x, y);
        switch (direction) {
            case BottomLeft:
                return Direction.TopRight;
            case BottomRight:
                return Direction.TopLeft;
            case TopLeft:
                return Direction.BottomRight;
            case TopRight:
                return Direction.BottomLeft;
        }
        return null;
    }

    private Direction whereDoesTheViewNeedToMoveTo(int x, int y) {
        return getSwipeDirection(x, y);
    }

    private Direction getSwipeDirection(int x, int y) {
        boolean isRight = false;
        boolean isBottom = false;
        if (x > previousX)
            isRight = true;
        if (y > previousY)
            isBottom = true;
        if (isRight && isBottom)
            return Direction.BottomRight;
        if (isRight && !isBottom)
            return Direction.TopRight;
        if (!isRight && isBottom)
            return Direction.BottomLeft;
        if (!isRight && !isBottom)
            return Direction.TopLeft;
        return null;
    }

    private boolean isConsideredClick(int distanceSinceLastMoveX, int distanceSinceLastMoveY) {
        return Math.abs(distanceSinceLastMoveX) <= 7 && Math.abs(distanceSinceLastMoveY) <= 7;
    }

}
