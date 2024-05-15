package or.nevet.orexercises.helpers.visual.table_painter;

import android.content.Context;
import android.graphics.Bitmap;

import java.text.DecimalFormat;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercise.LearnedExercise;
import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orexercises.helpers.logic.exercise_mechanism.low_level.ExercisesIterator;

public class TablePainter {

    private final LearnedExercises exercises;

    public TablePainter(LearnedExercises exercises) {
        this.exercises = exercises;
    }

    public Bitmap paintTableImage(Context context) {
        TableLayoutPainter layoutPainter = new TableLayoutPainter(exercises);
        TableCellPainter cellPainter = new TableCellPainter(layoutPainter, context);
        Bitmap tableImage = layoutPainter.createTableLayout();
        int numberOfRows = layoutPainter.getNumOfRows();
        int numberOfColumns = layoutPainter.getNumOfColumns();
        ExercisesIterator.iterateAllCellsByOrder(numberOfRows, numberOfColumns, (row, column) -> {
            //paint current text
            String text = "";
            //removes one because there is the first row and the first column.
            int exerciseRow = row-1;
            int exerciseColumn = column-1;
            if (column == 0 && row == 0) {
                text = String.valueOf(exercises.getExerciseSign());
            } else if (row == 0) {
                int rightNum = exercises.getRightNumberAt(exerciseColumn);
                text = String.valueOf(rightNum);
            } else if (column == 0) {
                int leftNum = exercises.getLeftNumberAt(exerciseRow);
                text = String.valueOf(leftNum);
            } else {
                LearnedExercise exercise = exercises.getLearnedExerciseAtIndexes(exerciseRow, exerciseColumn);
                if (exercise != null)
                    text = new DecimalFormat("###,###.###%").format(((Math.min(15, ((double) exercise.getNumOfLearned()) / 15d))));
                else
                    text = "-";
            }
            cellPainter.paintOneLineTextOnCellAt(tableImage, text, row, column, text.equals("-"));
        });
        return tableImage;
    }
}
