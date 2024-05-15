package or.nevet.orgeneralhelpers.graphical.activity_types;

import java.util.List;

import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.graphical.list_fragments.ListFragment;
import or.nevet.orgeneralhelpers.music_related.MusicActivity;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;

//All you should do for an activity to have a clickable list is to extend this class, add a ListFragment to your xml, and return it in the getListFragment() method that you implement.

public abstract class ListActivity extends MusicSubActivity {
    private ListFragment listFragment;
    private boolean wasAlreadyCreated = false;

    @Override
    protected void onStart() {
        super.onStart();
        if (!wasAlreadyCreated) {
            wasAlreadyCreated = true;
            listFragment = getListFragment();
            if (listFragment != null) {
                listFragment.initializeList(this);
                BackgroundRunningHelper.runCodeInBackgroundAsync(() -> {
                    for (CharSequence item : generateListItems())
                        addItemToList(item);
                    listFragment.showList(this);
                });
            }
        }
    }

    protected void addItemToList(CharSequence item) {
        listFragment.addItemToEndOfList(item, this);
    }

    public abstract List<CharSequence> generateListItems();

    public abstract ListFragment getListFragment();
}
