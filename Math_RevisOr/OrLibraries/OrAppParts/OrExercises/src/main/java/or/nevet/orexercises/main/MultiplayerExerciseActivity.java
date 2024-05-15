package or.nevet.orexercises.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.ArrayList;
import java.util.HashMap;

import or.nevet.multiplayergame.GameOperations;
import or.nevet.multiplayergame.data_objects.User;
import or.nevet.multiplayergame.data_objects.Users;
import or.nevet.multiplayergame.game_listeners.ExerciseEndingNotifier;
import or.nevet.multiplayergame.game_listeners.UserRightDuringGameListener;
import or.nevet.orexercises.helpers.logic.data_objects.exceptions.StopExerciseException;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.high_level.math_operations.RegularMathOperation;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.LearnedExercisesIndexesHelper;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.score_helpers.ScoreHelper;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.GameDataMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.MultiplayerOtherUserGameDataMethod;
import or.nevet.orexercises.helpers.logic.multiplayer_method_objects.MultiplayerOwnerGameDataMethod;
import or.nevet.orexercises.helpers.visual.ExerciseActivity;
import or.nevet.orexercises.helpers.visual.exercise_keyboard.ExerciseKeyboard;
import or.nevet.orexercises.helpers.visual.multiplayer_formatting.GameFormatter;
import or.nevet.orexercises.helpers.visual.multiplayer_list_activities.ShowGameDataBeforeNextGame;
import or.nevet.orgeneralhelpers.constants.GeneralExerciseConstants;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orgeneralhelpers.music_related.ActivityOpeningHelper;
import or.nevet.mathrevisorusermanager.learned_exercises_data.LearnedExerciseData;

public class MultiplayerExerciseActivity extends ExerciseActivity {

    private char exercisesSign;
    private String myFormattedEmail;
    private String ownerFormattedEmail;
    private Users allEmailsWithMine;
    private Users allEmailsWithoutMine;
    private boolean isOwner;
    private LearnedExercise currentExercise;
    private LearnedExercises exercises;
    private GameFormatter formatter;
    private ExerciseEndingNotifier endingNotifier;
    private ScoreHelper scoreHelper;
    private GameOperations.UserWasRightInGameManager userWasRightInGameManager;

    @Override
    protected void getExercises() {
        boolean isOwner = getIntent().getExtras().getBoolean(GeneralExerciseConstants.isMultiplayerGameOwnerExtrasName);
        if (isOwner)
            super.getExercises();
        else {
            char exercisesSign = getIntent().getExtras().getChar(GeneralExerciseConstants.exercisesSignInMultiplayerGameExtrasName);
            keyboard = new ExerciseKeyboard(getWindow().getDecorView(), exercisesSign, this);
        }
    }

    @Override
    protected void saveData() {
    }

    //gets called after the ending of the readyUsersListActivity that was called inside the showGameDataBeforeNextGame.
    ActivityResultLauncher<Intent> showGameDataBeforeNextGameActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent intent = result.getData();
            if (result.getResultCode() == GeneralExerciseConstants.readyUsersListActivityResultCode) {
                if (intent != null) {
                    if (!isOwner) {
                        LearnedExerciseData data = (LearnedExerciseData) intent.getExtras().getSerializable(GeneralExerciseConstants.readyUsersReturnedValueMultiplayerGameExtrasName);
                        currentExercise = new LearnedExercise(data.leftNumber, data.rightNumber, 0, data.result);
                    }
                    startGame();
                } else
                    finish();
            } else if (result.getResultCode() == GeneralExerciseConstants.endGameActivityResultCode) {
                finish();
            } else if (result.getResultCode() == GeneralExerciseConstants.showGameDataBeforeNextGameActivityResultCode) {
                if (intent == null)
                    finish();
            }
        }
    });

    @Override
    protected void startDoingExercises(LearnedExercises exercises) {
        this.exercises = exercises;
        scoreHelper = new ScoreHelper();
        currentExercise = (LearnedExercise) getIntent().getExtras().getSerializable(GeneralExerciseConstants.currentExerciseMultiplayerGameExtrasName);
        ownerFormattedEmail = getIntent().getExtras().getString(GeneralExerciseConstants.ownerEmailInMultiplayerGameExtrasName);
        exercisesSign = getIntent().getExtras().getChar(GeneralExerciseConstants.exercisesSignInMultiplayerGameExtrasName);
        allEmailsWithMine = (Users) getIntent().getExtras().getSerializable(GeneralExerciseConstants.usersInMultiplayerGameExtrasName);
        allEmailsWithoutMine = (Users) getIntent().getExtras().getSerializable(GeneralExerciseConstants.usersWithoutMeInMultiplayerGameExtrasName);
        isOwner = getIntent().getExtras().getBoolean(GeneralExerciseConstants.isMultiplayerGameOwnerExtrasName);
        formatter = (GameFormatter) getIntent().getExtras().getSerializable(GeneralExerciseConstants.formatterInMultiplayerGameExtrasName);
        myFormattedEmail = formatter.getMyFormattedIdentifier();
        userWasRightInGameManager = new GameOperations.UserWasRightInGameManager();
        startGame();
    }

    private LearnedExercises getLearnedExercisesFromOneLearnedExercise(LearnedExercise exercise) {
        ArrayList<Integer> leftNumber = new ArrayList<>();
        ArrayList<Integer> rightNumber = new ArrayList<>();
        leftNumber.add(exercise.getLeftNumber());
        rightNumber.add(exercise.getRightNumber());
        HashMap<String, LearnedExercise> exerciseHashMap = new HashMap<>();
        exerciseHashMap.put(LearnedExercisesIndexesHelper.getIndexesStringFromIndexes(0, 0), exercise);
        return new LearnedExercises(exerciseHashMap, leftNumber, rightNumber, exercisesSign);
    }

    @Override
    protected void moveToNextState() {
        try {
            boolean wasRight = mathOperation.moveToNextState(() -> {
                endingNotifier.exerciseEnded();
                openGameDataScreen();
            });
            if (wasRight)
                userWasRightInGameManager.notifyThatIWasRightDuringGame(myFormattedEmail, ownerFormattedEmail);
        } catch (StopExerciseException ignored) {
        }
    }

    private void openGameDataScreen() {
        GameDataMethod method;
        if (mathOperation.wasTheLastAnswerRight())
            scoreHelper.exerciseWasSucceeded();
        else
            scoreHelper.wrong();
        if (isOwner) {
            currentExercise = exercises.getRandomExercise();
            method = new MultiplayerOwnerGameDataMethod(currentExercise, scoreHelper.getScore(), myFormattedEmail, allEmailsWithMine, formatter);
        } else {
            method = new MultiplayerOtherUserGameDataMethod(scoreHelper.getScore(), myFormattedEmail, ownerFormattedEmail, allEmailsWithMine, formatter);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(GeneralExerciseConstants.gameDataMethodExtrasName, method);
        ActivityOpeningHelper.openActivityForResult(this, ShowGameDataBeforeNextGame.class, bundle, showGameDataBeforeNextGameActivityLauncher);
    }



    private void startGame() {
        mathOperation = new RegularMathOperation(MultiplayerExerciseActivity.this, getLearnedExercisesFromOneLearnedExercise(currentExercise));
        endingNotifier = userWasRightInGameManager.listenToUsersRightDuringGame(myFormattedEmail, ownerFormattedEmail, new UserRightDuringGameListener() {
            @Override
            public void userRight(User user) {
                UserMessages.showToastMessage(formatter.formatRightUser(user), MultiplayerExerciseActivity.this);
            }
        });
        try {
            mathOperation.startFirstExercise();
        } catch (StopExerciseException ignored) {
        }
    }
}
