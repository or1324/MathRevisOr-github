package or.nevet.orgeneralhelpers.graphical.list_fragments;

import or.nevet.orgeneralhelpers.R;

public class ClickableListFragment extends ListFragment {
    @Override
    public int getRecyclerViewRowId() {
        return R.layout.clickable_list_row;
    }

    @Override
    public int getRecyclerViewTextViewId() {
        return R.id.clickable_list_item;
    }
}
