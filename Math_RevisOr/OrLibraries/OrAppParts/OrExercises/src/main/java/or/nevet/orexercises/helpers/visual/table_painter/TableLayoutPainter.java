package or.nevet.orexercises.helpers.visual.table_painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import or.nevet.orexercises.helpers.logic.data_objects.exercise_objects.learned_exercises.LearnedExercises;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.TablesConstants;

public class TableLayoutPainter {
    private final Point[][] grid;
    private final int tableHeight;
    private final int tableWidth;
    private final double cellSize;
    private final int numOfRows;
    private final int numOfColumns;
    TableLayoutPainter(LearnedExercises exercises) {
        //adds one because there are the first row of the right numbers and the first column of the left numbers.
        numOfRows = exercises.getAmountOfLeftNumbers()+1;
        numOfColumns = exercises.getAmountOfRightNumbers()+1;
        //adds one because the number of lines is always equal to the number of rows/columns plus one. That is geometry.
        int numOfHorizontalLines = numOfRows+1;
        int numOfVerticalLines = numOfColumns+1;
        grid = new Point[numOfHorizontalLines][numOfVerticalLines];
        tableWidth = TablesConstants.tableWidth;
        cellSize = (double)tableWidth/((double)(numOfColumns));
        tableHeight = (int)(cellSize*(double)numOfRows);
    }

    public int getGridNumOfHorizontalLines() {
        return grid.length;
    }

    public int getGridNumOfVerticalLines() {
        //can be any number between 0 to the length of each row. All column sizes are equal.
        return grid[0].length;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfColumns() {
        return numOfColumns;
    }

    public double getCellSize() {
        return cellSize;
    }

    public Bitmap createTableLayout() {
        Bitmap tableLayoutBitmap = createGridBackground(tableWidth, tableHeight);
        Paint paint = createTableLayoutPaint();
        Canvas canvas = new Canvas(tableLayoutBitmap);
        canvas.drawRect(TablesConstants.tableSidePadding, TablesConstants.tableSidePadding, tableWidth+ TablesConstants.tableSidePadding, tableHeight+ TablesConstants.tableSidePadding, paint);

        for (int horizontalLine = 0; horizontalLine < getGridNumOfHorizontalLines(); horizontalLine++) {
            //paint horizontal line
            int horizontalLineY = (int)(((double)horizontalLine)*((double) cellSize))+ TablesConstants.tableSidePadding;
            canvas.drawLine(TablesConstants.tableSidePadding, horizontalLineY, tableWidth+ TablesConstants.tableSidePadding, horizontalLineY, paint);

            for (int verticalLine = 0; verticalLine < getGridNumOfVerticalLines(); verticalLine++) {
                //paint vertical line
                int verticalLineX = (int)(((double)verticalLine)*((double)cellSize))+ TablesConstants.tableSidePadding;
                canvas.drawLine(verticalLineX, TablesConstants.tableSidePadding, verticalLineX, tableHeight+ TablesConstants.tableSidePadding, paint);
            }
        }
        return tableLayoutBitmap;
    }

    private Bitmap createGridBackground(int tableWidth, int tableHeight) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(tableWidth+2* TablesConstants.tableSidePadding, tableHeight+2* TablesConstants.tableSidePadding, config);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(TablesConstants.tableBackgroundColor);
        return bitmap;
    }

    private Paint createTableLayoutPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(TablesConstants.tableLayoutColor);
        return paint;
    }

    public Point getTopLeftPositionInImageOfCellInIndexes(int row, int column) {
        int rowBottomLineY = (int)(((double)row)*((double) cellSize))+ TablesConstants.tableSidePadding;
        int columnRightLineX = (int)(((double)column)*((double)cellSize))+ TablesConstants.tableSidePadding;
        //The point is created from the 2 lines crossing. Its x is the x of the vertical line, and its y is the y of the horizontal line.
        return new Point(columnRightLineX, rowBottomLineY);
    }

    public Point getBottomLeftPositionInImageOfCellInIndexes(int row, int column) {
        //adds one because the bottom left position of the cell is equal to the top left position of the cell beneath him.
        return getTopLeftPositionInImageOfCellInIndexes(row+1, column);
    }
}
