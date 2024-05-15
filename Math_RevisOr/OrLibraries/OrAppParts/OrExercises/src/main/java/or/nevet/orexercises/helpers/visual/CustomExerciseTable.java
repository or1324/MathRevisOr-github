package or.nevet.orexercises.helpers.visual;

import android.content.Context;
import android.view.View;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import or.nevet.orexercises.R;
import or.nevet.orgeneralhelpers.graphical.table_views.TextTable;
import or.nevet.orgeneralhelpers.graphical.table_views.WritableNumbersTable;

public class CustomExerciseTable extends ConstraintLayout {

    private final WritableNumbersTable leftNumbers;
    private final WritableNumbersTable rightNumbers;
    private final WritableNumbersTable answers;
    private final TextTable signTable;
    private final char sign;

    public CustomExerciseTable(Context context, int numOfRows, int numOfColumns, int cellSize, char sign, Integer[] initialLeftValues, Integer[] initialRightValues, Integer[][] initialAnswerValues) {
        super(context);
        this.sign = sign;
        setClipChildren(false);
        Integer[][] initialLeftValuesMatrix = null;
        if (initialLeftValues != null) {
            initialLeftValuesMatrix = new Integer[numOfRows][1];
            for (int i = 0; i < numOfRows; i++)
                initialLeftValuesMatrix[i][0] = initialLeftValues[i];
        }
        Integer[][] initialRightValuesMatrix = null;
        if (initialRightValues != null) {
            initialRightValuesMatrix = new Integer[1][numOfColumns];
            for (int i = 0; i < numOfColumns; i++)
                initialRightValuesMatrix[0][i] = initialRightValues[i];
        }
        leftNumbers = new WritableNumbersTable(getContext(), numOfRows, 1, cellSize, AppCompatResources.getDrawable(getContext(), R.drawable.left), initialLeftValuesMatrix);
        rightNumbers = new WritableNumbersTable(getContext(), 1, numOfColumns, cellSize, AppCompatResources.getDrawable(getContext(), R.drawable.right), initialRightValuesMatrix);
        answers = new WritableNumbersTable(getContext(), numOfRows, numOfColumns, cellSize, AppCompatResources.getDrawable(getContext(), R.drawable.answer), initialAnswerValues);
        signTable = new TextTable(getContext(), 1, 1, cellSize, new String[][]{{String.valueOf(sign)}});
        createTable();
    }

    private void createTable() {
        leftNumbers.createTable();
        rightNumbers.createTable();
        answers.createTable();
        signTable.createTable();
        int signTextViewId = createTopLeftSignTextView();
        int rightNumbersTableId = createRightNumbersTable(signTextViewId);
        int leftNumbersTableId = createLeftNumbersTableId(signTextViewId);
        int answersTableId = createAnswersTableId(rightNumbersTableId, leftNumbersTableId);
    }

    private int createAnswersTableId(int rightNumbersTableId, int leftNumbersTableId) {
        answers.setId(View.generateViewId());
        addView(answers);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(answers.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(answers.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.applyTo(this);
        leftNumbers.measure(0, 0);
        rightNumbers.measure(0, 0);
        ((LayoutParams)answers.getLayoutParams()).leftMargin = findViewById(leftNumbersTableId).getMeasuredWidth();
        ((LayoutParams)answers.getLayoutParams()).topMargin = findViewById(rightNumbersTableId).getMeasuredHeight();
        return answers.getId();
    }

    private int createLeftNumbersTableId(int signTextViewId) {
        leftNumbers.setId(View.generateViewId());
        addView(leftNumbers);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(leftNumbers.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(leftNumbers.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.applyTo(this);
        signTable.measure(0, 0);
        LayoutParams layoutParams = (LayoutParams) leftNumbers.getLayoutParams();
        layoutParams.topMargin = findViewById(signTextViewId).getMeasuredHeight();
        leftNumbers.setLayoutParams(layoutParams);
        return leftNumbers.getId();
    }

    private int createRightNumbersTable(int signTextViewId) {
        rightNumbers.setId(View.generateViewId());
        addView(rightNumbers);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(rightNumbers.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(rightNumbers.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.applyTo(this);
        signTable.measure(0, 0);
        ((LayoutParams)rightNumbers.getLayoutParams()).leftMargin = (findViewById(signTextViewId).getMeasuredWidth());
        return rightNumbers.getId();
    }

    private int createTopLeftSignTextView() {
        signTable.setId(View.generateViewId());
        addView(signTable);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(signTable.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(signTable.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.applyTo(this);
        return signTable.getId();
    }

    public Integer getLeftNumberAtRow(int row) {
        //always has only one column
        return leftNumbers.getValueAtCell(row, 0);
    }

    public Integer getRightNumberAtColumn(int column) {
        //always has only one row
        return rightNumbers.getValueAtCell(0, column);
    }

    public Integer getAnswerAtCell(int row, int column) {
        return answers.getValueAtCell(row, column);
    }

    public char getSign() {
        return sign;
    }


}
