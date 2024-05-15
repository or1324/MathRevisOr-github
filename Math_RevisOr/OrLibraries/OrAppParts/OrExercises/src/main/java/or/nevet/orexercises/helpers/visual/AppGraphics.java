package or.nevet.orexercises.helpers.visual;

import android.view.View;

import or.nevet.orexercises.R;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.interfaces.ExerciseScreen;
import or.nevet.orexercises.helpers.visual.exercise_keyboard.ExerciseKeyboard;
import or.nevet.orexercises.helpers.visual.table_painter.TablePainter;
import or.nevet.orexercises.main.ExerciseService;
import or.nevet.orexercises.main.PracticeExerciseActivityWithScore;
import or.nevet.orexercises.main.TableActivity;
import or.nevet.orgeneralhelpers.background_running_related.BackgroundRunningHelper;

public class AppGraphics {

    public static void initExerciseStatisticsTable(TableActivity activity, LearnedExercises exercises) {
        activity.photoView = activity.findViewById(or.nevet.orexercises.R.id.photoView);
        activity.back = activity.findViewById(or.nevet.orexercises.R.id.back2);
        activity.welcome = activity.findViewById(or.nevet.orexercises.R.id.welcome3);
        activity.back.setOnClickListener(activity);
        TablePainter painter = new TablePainter(exercises);
        activity.photoView.setImageBitmap(painter.paintTableImage(activity));
        activity.welcome.setText("The '"+exercises.getExerciseSign()+"' operation's statistics table:");
    }

    public static void initScoreExerciseActivity(PracticeExerciseActivityWithScore activity) {
        activity.setScoreLayout(activity.findViewById(R.id.score_layout));
        activity.setInternetImageView(activity.findViewById(R.id.internet));
        activity.getScoreLayout().setVisibility(View.VISIBLE);
    }

    public static void initExerciseKeyboard(ExerciseKeyboard keyboard, View mainView) {
        keyboard.setRoot(mainView.findViewById(R.id.root));
        keyboard.setMusic(mainView.findViewById(R.id.music));
        keyboard.setInput(mainView.findViewById(R.id.input));
        keyboard.setContinueNextExercise(mainView.findViewById(R.id.continue_to_next));
        keyboard.setExerciseView(mainView.findViewById(R.id.exercise));
        keyboard.setAnswerView(mainView.findViewById(R.id.answer));
        keyboard.setKeyboardBackground(mainView.findViewById(R.id.keyboard_background));
        keyboard.setKeyboard(mainView.findViewById(R.id.keyboard));
        keyboard.setAnswerTitle(mainView.findViewById(R.id.answer_title));
        keyboard.setNextAfterAnswerShown(mainView.findViewById(R.id.next));
        keyboard.enableInput();
        //keyboard.getKeyboardBackground().setOnClickListener(v -> keyboard.hideKeyboard());
        //keyboard.getRoot().setOnClickListener(v -> keyboard.hideKeyboard());
    }

    public static void initExerciseActivity(ExerciseActivity activity) {
        activity.setTimeView(activity.findViewById(R.id.time));
        activity.setBackground(activity.findViewById(R.id.background));
        activity.setScoreView(activity.findViewById(R.id.score));
        activity.setGiveUp(activity.findViewById(R.id.give_up));
        activity.getExerciseKeyboard().getContinueNextExercise().setOnClickListener(v -> activity.answerExercise());
        activity.getGiveUp().setOnClickListener(v -> activity.exitScreen(v));
        activity.getExerciseKeyboard().getNextAfterAnswerShown().setOnClickListener(v -> activity.nextAfterAnswerShown());
        initExerciseScreen(activity);
    }

    public static void initExerciseService(ExerciseService service) {
        service.setBackground(service.view.findViewById(R.id.background));
        service.setTimeView(service.view.findViewById(R.id.time));
        service.setErrorView(service.view.findViewById(R.id.error));
        service.setGiveUp(service.view.findViewById(R.id.give_up));
        service.getExerciseKeyboard().getMusic().setVisibility(View.GONE);
        service.exerciseHelper.generateRandomExercise();
        service.getExerciseKeyboard().showExercise(service.exerciseHelper.getCurrentExercise());
        service.getExerciseKeyboard().getContinueNextExercise().setOnClickListener(v -> service.answerExercise());
        service.getGiveUp().setOnClickListener(v -> service.help());
        service.getExerciseKeyboard().getNextAfterAnswerShown().setOnClickListener(v -> service.nextAfterAnswerShown());
        initExerciseScreen(service);
    }

    //Things that every exercise screen does the same way
    public static void initExerciseScreen(ExerciseScreen exerciseScreen) {
        //exerciseScreen.getBackground().setOnClickListener(v -> exerciseScreen.getExerciseKeyboard().hideKeyboard());
    }
}
