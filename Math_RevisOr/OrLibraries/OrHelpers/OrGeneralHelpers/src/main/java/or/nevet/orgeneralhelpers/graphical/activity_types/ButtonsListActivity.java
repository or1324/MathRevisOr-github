package or.nevet.orgeneralhelpers.graphical.activity_types;

import android.view.View;

//All you should do for an activity to have a clickable list is to extend this class, implement the listeners, add a ClickableListFragment to your xml,  and return it in the getListFragment() method that you implement.
public abstract class ButtonsListActivity extends ListActivity {

    public abstract View.OnClickListener getOnClickListener(String buttonText);

    public abstract View.OnLongClickListener getOnLongClickListener(String buttonText);

}
