package or.nevet.orgeneralhelpers.graphical;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import or.nevet.orgeneralhelpers.graphical.recycler_view.RecyclOrView;
import or.nevet.orgeneralhelpers.graphical.recycler_view.RecyclOrViewAdaptOr;
import or.nevet.orgeneralhelpers.graphical.activity_types.ListActivity;
import or.nevet.orgeneralhelpers.music_related.AppMusicService;
import or.nevet.orgeneralhelpers.music_related.InteractiveMusicActivity;
import or.nevet.orgeneralhelpers.music_related.InteractiveMusicThing;

public class AppGraphics {

    public static void onStartOfInteractiveMusic(InteractiveMusicThing activity) {
        AppMusicService.getInstance().changeMusicButtonIconByMusicState(activity.getMusicButton());
        changeMusicStateOnImageButtonClick(activity.getMusicButton());
    }

    private static void changeMusicStateOnImageButtonClick(AppCompatImageButton imageButton) {
        imageButton.setOnClickListener(v -> {
            AppMusicService.getInstance().changeMusicState();
            AppMusicService.getInstance().changeMusicButtonIconByMusicState(imageButton);
        });
    }

    public static void addItemsToListActivity(ListActivity activity, List<CharSequence> items, int recyclerViewRowId, int recyclerViewTextViewId) {
        if (activity.getListFragment() != null) {
            RecyclOrView recyclOrView = activity.getListFragment().getRecyclOrView();
            LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclOrView.setLayoutManager(manager);
            RecyclOrViewAdaptOr adaptOr = new RecyclOrViewAdaptOr(activity, items, recyclerViewRowId, recyclerViewTextViewId);
            recyclOrView.setAdapter(adaptOr);
            recyclOrView.setVisibility(View.VISIBLE);
        }
    }

    public static void refreshRecyclerviewAfterAddingOneItemToListActivity(ListActivity activity, int itemPosition) {
        if (activity.getListFragment() != null)
            activity.getListFragment().getRecyclOrView().getAdapter().notifyItemInserted(itemPosition);
    }

    public static void refreshRecyclerviewAfterRemovingOneItemToListActivity(ListActivity activity, int itemPosition) {
        if (activity.getListFragment() != null)
            activity.getListFragment().getRecyclOrView().getAdapter().notifyItemRemoved(itemPosition);
    }
}
