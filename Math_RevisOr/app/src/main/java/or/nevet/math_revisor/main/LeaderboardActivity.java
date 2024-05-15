package or.nevet.math_revisor.main;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.mathrevisorusermanager.UserManager;
import or.nevet.mathrevisorusermanager.UserObject;
import or.nevet.orgeneralhelpers.graphical.activity_types.ListActivity;
import or.nevet.orgeneralhelpers.graphical.list_fragments.ListFragment;

public class LeaderboardActivity extends ListActivity {

    public AppCompatImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        AppGraphics.initLeaderboard(this);
    }

    @Override
    public List<CharSequence> generateListItems() {
        try {
            LinkedList<UserObject> users = UserManager.getAllUsers();
            ArrayList<CharSequence> items = new ArrayList<>(users.size());
            for (UserObject userObject : users)
                items.add("userName: "+userObject.getUserName()+"\n"+"score: "+userObject.getScore());
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ListFragment getListFragment() {
        ListFragment fragment =  (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment;
    }
}