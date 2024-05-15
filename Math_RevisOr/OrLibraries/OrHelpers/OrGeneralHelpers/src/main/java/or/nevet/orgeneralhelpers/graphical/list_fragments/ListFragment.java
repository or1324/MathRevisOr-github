package or.nevet.orgeneralhelpers.graphical.list_fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.List;

import or.nevet.orgeneralhelpers.R;
import or.nevet.orgeneralhelpers.graphical.AppGraphics;
import or.nevet.orgeneralhelpers.graphical.activity_types.ListActivity;
import or.nevet.orgeneralhelpers.graphical.graphical_concepts.LoadingComponent;
import or.nevet.orgeneralhelpers.graphical.recycler_view.RecyclOrView;

public class ListFragment extends Fragment implements LoadingComponent {
    private final LinkedList<CharSequence> items = new LinkedList<>();
    private boolean wereItemsShown = false;

    private ProgressBar load;
    private RecyclOrView recyclOrView;
    private boolean keepProgressBarOn = false;

    public ListFragment() {
        super(R.layout.list_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load = view.findViewById(R.id.load);
        recyclOrView = view.findViewById(R.id.recyclor_view);
    }

    public void showList(ListActivity activity) {
        activity.runOnUiThread(() -> {
            AppGraphics.addItemsToListActivity(activity, items, getRecyclerViewRowId(), getRecyclerViewTextViewId());
            stopProgressBar();
            recyclOrView.setVisibility(View.VISIBLE);
            wereItemsShown = true;
        });
    }

    public void initializeList(ListActivity activity) {
        recyclOrView.setVisibility(View.GONE);
        items.clear();
        AppGraphics.addItemsToListActivity(activity, items, getRecyclerViewRowId(), getRecyclerViewTextViewId());
    }

    public void addItemToEndOfList(CharSequence item, ListActivity activity) {
        activity.runOnUiThread(() -> {
            items.add(item);
            //refreshes the recyclerView after adding the item in the last position in the recyclerView (list.size()-1).
            if (wereItemsShown)
                AppGraphics.refreshRecyclerviewAfterAddingOneItemToListActivity(activity, items.size() - 1);
        });
    }

    public void addItemToStartOfList(CharSequence item, ListActivity activity) {
        activity.runOnUiThread(() -> {
            items.addFirst(item);
            //refreshes the recyclerView after adding the item in the first position in the recyclerView (0).
            if (wereItemsShown)
                AppGraphics.refreshRecyclerviewAfterAddingOneItemToListActivity(activity, 0);
        });
    }

    public void removeItemFromList(String item, ListActivity activity) {
        activity.runOnUiThread(() -> {
            int itemIndex = items.indexOf(item);
            items.remove(itemIndex);
            //refreshes the recyclerView after removing the item in the specified position in the recyclerView.
            if (wereItemsShown)
                AppGraphics.refreshRecyclerviewAfterRemovingOneItemToListActivity(activity, itemIndex);
        });
    }

    @Override
    public void stopProgressBar() {
        if (!keepProgressBarOn)
            getProgressBar().setVisibility(View.GONE);
    }

    @Override
    public void restartProgressBar() {
        if (getProgressBar().getVisibility() == View.VISIBLE)
            keepProgressBarOn = true;
        else
            getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    public ProgressBar getProgressBar() {
        return load;
    }

    public RecyclOrView getRecyclOrView() {
        return recyclOrView;
    }

    public int getRecyclerViewRowId() {
        return R.layout.list_row;
    }

    public int getRecyclerViewTextViewId() {
        return R.id.list_item;
    }
}
