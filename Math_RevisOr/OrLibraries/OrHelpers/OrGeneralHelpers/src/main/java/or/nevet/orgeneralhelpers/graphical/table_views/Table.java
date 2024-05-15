package or.nevet.orgeneralhelpers.graphical.table_views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class Table extends LinearLayout {

    private final int numOfRows;
    private final int numOfColumns;
    private final int width;
    private final int height;
    private final int cellSizeWithLine;
    private final int separatorLineSize = 5;
    private final TableTextViewStylist stylist;
    private final TextView[][] cells;

    public Table(Context context, int numOfRows, int numOfColumns, int cellSize) {
        super(context);
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.cellSizeWithLine = cellSize+separatorLineSize;
        width = numOfColumns*cellSize+(numOfColumns+1)*separatorLineSize+getPaddingLeft()+getPaddingRight();
        height = numOfRows*cellSize+(numOfRows+1)*separatorLineSize+getPaddingTop()+getPaddingBottom();
        setLayoutParams(new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        cells = new TextView[numOfRows][numOfColumns];
        stylist = new TableTextViewStylist(getContext(), cellSize);
    }

    //Must call this for the table to be created.
    public void createTable() {
        //First horizontal line
        View v = createHorizontalTableLine();
        addView(v);
        //creating each row
        for (int row = 0; row < numOfRows; row++) {
            LinearLayout tableRow = createTableRow(row);
            tableRow.setLayoutParams(new RelativeLayout.LayoutParams(width, cellSizeWithLine));
            addView(tableRow);
            v = createHorizontalTableLine();
            addView(v);
        }
    }

    protected void setCellAt(int row, int column, TextView cell) {
        cells[row][column] = cell;
    }

    protected TextView getCellAt(int row, int column) {
        return cells[row][column];
    }

    protected void styleCell(TextView cell) {
        stylist.style(cell);
    }

    protected View createHorizontalTableLine() {
        View v = new View(getContext());
        v.setLayoutParams(new TableRow.LayoutParams(width, separatorLineSize));
        v.setBackgroundColor(Color.BLACK);
        return v;
    }

    protected View createVerticalTableLine() {
        View v = new View(getContext());
        v.setLayoutParams(new TableRow.LayoutParams(separatorLineSize, height));
        v.setBackgroundColor(Color.BLACK);
        return v;
    }

    protected LinearLayout createTableRow(int currentRow) {
        TextView[] row = new TextView[getNumOfColumns()];
        for (int column = 0; column < getNumOfColumns(); column++) {
            row[column] = createTableCell(currentRow, column);
        }
        return createTableRow(row, currentRow);
    }

    protected abstract TextView createTableCell(int row, int column);

    protected int getNumOfColumns() {
        return numOfColumns;
    }

    protected LinearLayout createTableRow(TextView[] rowViews, int currentRow) {
        LinearLayout tableRow = new LinearLayout(getContext());
        tableRow.setOrientation(HORIZONTAL);
        View v = createVerticalTableLine();
        tableRow.addView(v);
        for (int column = 0; column < rowViews.length; column++) {
            tableRow.addView(rowViews[column]);
            v = createVerticalTableLine();
            tableRow.addView(v);
            setCellAt(currentRow, column, rowViews[column]);
        }
        return tableRow;
    }

    protected abstract Object getValueAtCell(int row, int column) throws Exception;
}
