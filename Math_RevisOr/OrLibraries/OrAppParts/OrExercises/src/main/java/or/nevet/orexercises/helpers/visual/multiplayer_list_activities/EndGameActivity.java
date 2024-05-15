package or.nevet.orexercises.helpers.visual.multiplayer_list_activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Arrays;
import java.util.List;

import or.nevet.multiplayergame.data_objects.GameUsers;
import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.AllVSAllFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GroupsFormatter;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.graphical.ConstraintLayoutAnimationHelper;
import or.nevet.orgeneralhelpers.graphical.activity_types.ButtonsListActivity;
import or.nevet.orgeneralhelpers.graphical.list_fragments.ListFragment;
import or.nevet.orgeneralhelpers.tts.OrTTS;
import or.nevet.orgeneralhelpers.tts.TTSListener;

public class EndGameActivity extends ButtonsListActivity {

    GameUsers usersAndTheirScoresSorted;
    GameFormatter formatter;
    private ImageButton back;
    private AppCompatImageView title;
    private ConstraintLayout root;
    private AppCompatTextView congratulations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        usersAndTheirScoresSorted = (GameUsers) getIntent().getExtras().getSerializable(GeneralExerciseConstants.endGameUsersAndTheirScoreExtrasName);
        formatter = (GameFormatter) getIntent().getExtras().getSerializable(GeneralExerciseConstants.formatterInMultiplayerGameExtrasName);
        back = findViewById(R.id.back2);
        title = findViewById(R.id.ending_title);
        root = findViewById(R.id.root);
        congratulations = findViewById(R.id.congratulations);
        back.setOnClickListener(v -> {
            setResult(GeneralExerciseConstants.endGameActivityResultCode, null);
            finish();
        });
    }

    @Override
    public View.OnClickListener getOnClickListener(String buttonText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make it do animations
            }
        };
    }

    @Override
    public View.OnLongClickListener getOnLongClickListener(String buttonText) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!buttonText.startsWith("Insight: ")) {
                    OrTTS.createTTS(EndGameActivity.this, new TTSListener() {
                        @Override
                        public void onFinish(boolean succeeded) {
                            if (succeeded) {
                                OrTTS.TTSHelper ttsHelper = OrTTS.getTTSHelper();
                                String text = null;
                                String clickedRowLeaderboardName = formatter.getLeaderboardNameFromLeaderboardRowText(buttonText);
                                if (clickedRowLeaderboardName.equals(formatter.getMyLeaderboardName())) {
                                    //clicked on himself
                                    if (formatter.isThereATie(usersAndTheirScoresSorted) || !formatter.getWinnerLeaderboardName(usersAndTheirScoresSorted).equals(formatter.getMyLeaderboardName())) {
                                        //did not win
                                        if (formatter.getClass() == AllVSAllFormatter.class) {
                                            //alone
                                            text = "you are the best player, even if you do not have the highest score!";
                                        } else if (formatter.getClass() == GroupsFormatter.class) {
                                            //in group
                                            text = "your group is the best, even if you did not win!";
                                        }
                                    } else if (formatter.getWinnerLeaderboardName(usersAndTheirScoresSorted).equals(formatter.getMyLeaderboardName())) {
                                        //won
                                        if (formatter.getClass() == AllVSAllFormatter.class) {
                                            //alone
                                            text = "you are awesome! you won!";
                                        } else if (formatter.getClass() == GroupsFormatter.class) {
                                            //in group
                                            text = "your group is awesome! you won!";
                                        }
                                    }
                                } else {
                                    //clicked on someone else
                                    if (formatter.isMyLeaderboardScoreBetterThanTheScoreOf(clickedRowLeaderboardName, usersAndTheirScoresSorted)) {
                                        //the one with the better score is him/his group
                                        if (formatter.getClass() == AllVSAllFormatter.class) {
                                            //alone
                                            text = "you are better than " + clickedRowLeaderboardName + "!";
                                        } else if (formatter.getClass() == GroupsFormatter.class) {
                                            //in group
                                            text = "your group is better than " + clickedRowLeaderboardName + "!";
                                        }
                                    } else {
                                        //the score of him/his group is worse
                                        if (formatter.getClass() == AllVSAllFormatter.class) {
                                            //alone
                                            text = "you are the best, your score does not matter!";
                                        } else if (formatter.getClass() == GroupsFormatter.class) {
                                            //in group
                                            text = "your group is the best, your score does not matter!";
                                        }
                                    }
                                }
                                ttsHelper.readText(text);
                            }
                        }
                    });
                }
                return false;
            }
        };
    }

    @Override
    public List<CharSequence> generateListItems() {
        setGameTitle();
        ConstraintLayoutAnimationHelper animationHelper = new ConstraintLayoutAnimationHelper(root);
        animationHelper.animateConnectionLeftToLeftAndRunnableChanges(congratulations.getId(), ConstraintSet.PARENT_ID, () -> {
            boolean wasThereATie = formatter.isThereATie(usersAndTheirScoresSorted);
            if (!wasThereATie) {
                String winnerName = formatter.getWinnerLeaderboardName(usersAndTheirScoresSorted);
                if (winnerName.equals(formatter.getMyLeaderboardName())) {
                    if (formatter.getClass() == AllVSAllFormatter.class)
                        congratulations.setText("Congratulations to you!!!\uD83E\uDD73");
                    else if (formatter.getClass() == GroupsFormatter.class)
                        congratulations.setText("Congratulations to your group!!!\uD83E\uDD73");
                    congratulations.setTextColor(Color.rgb(0, 100, 0));
                    congratulations.setTypeface(congratulations.getTypeface(), Typeface.BOLD_ITALIC);
                } else
                    congratulations.setText("Congratulations to " + formatter.getWinnerLeaderboardName(usersAndTheirScoresSorted) + "!!!");
            } else {
                congratulations.setText("There was a tie!\uD83D\uDE31");
                congratulations.setTextColor(Color.rgb(205, 100, 0));
            }
        });
        CharSequence[] formattedDataToDisplay = formatter.formatScoresAtEnding(usersAndTheirScoresSorted);
        return Arrays.asList(formattedDataToDisplay);
    }

    @Override
    public ListFragment getListFragment() {
        ListFragment fragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment;
    }

    private void setGameTitle() {
        BackgroundRunningHelper.runCodeOnUiThread(() -> title.setBackgroundDrawable(AppCompatResources.getDrawable(EndGameActivity.this, R.drawable.game_data_title_on_game)));
    }

    @Override
    public void onBackPressed() {
        setResult(GeneralExerciseConstants.endGameActivityResultCode, null);
        super.onBackPressed();
    }
}
