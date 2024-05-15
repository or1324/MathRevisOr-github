package or.nevet.orexercises.helpers.visual.table_painter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import or.nevet.orexercises.R;
import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.TablesConstants;

public class TableCellPainter {

    private final TableLayoutPainter layoutPainter;
    private final Context context;

    TableCellPainter(TableLayoutPainter layoutPainter, Context context) {
        this.layoutPainter = layoutPainter;
        this.context = context;
    }

    public void paintOneLineTextOnCellAt(Bitmap table, String text, int cellRow, int cellColumn, boolean isEmpty) {
        //need the bottom left because text is rendered from bottom to top.
        Point cellBottomLeftPoint = layoutPainter.getBottomLeftPositionInImageOfCellInIndexes(cellRow, cellColumn);
        Paint tableCellPaint = createTableCellPaint(cellRow, cellColumn, isEmpty);
        Canvas canvas = new Canvas(table);
        //the canvas is adding the space beneath the baseline to the y axis when we call drawText, and it is drawing from bottom to top. so we need to start writing from the bottom of the text minus the space beneath the baseline.
        int textHeight = getOneLineTextHeightFromPaint(text, tableCellPaint);
        int textWidth = getTextWidthFromPaint(text, tableCellPaint);
        int startDrawingYLocationForDrawingTextExactlyInBottomOfCell = cellBottomLeftPoint.y - getOneLineTextHeightFromPaintBelowBaseline(text, tableCellPaint);
        int startDrawingXLocationForDrawingTextExactlyInLeftOfCell = cellBottomLeftPoint.x;
        double emptyVerticalSpaceInCellAfterDrawing = layoutPainter.getCellSize()-textHeight;
        double emptyHorizontalSpaceInCellAfterDrawing = layoutPainter.getCellSize()-textWidth;
        //half empty space will be on top, and half on bottom.
        double startDrawingYLocationForDrawingTextOnMiddleOfCell = startDrawingYLocationForDrawingTextExactlyInBottomOfCell - emptyVerticalSpaceInCellAfterDrawing/2;
        double startDrawingXLocationForDrawingTextOnMiddleOfCell = startDrawingXLocationForDrawingTextExactlyInLeftOfCell + emptyHorizontalSpaceInCellAfterDrawing/2;
        canvas.drawText(text, (int)startDrawingXLocationForDrawingTextOnMiddleOfCell, (int)startDrawingYLocationForDrawingTextOnMiddleOfCell, tableCellPaint);
    }

    private int getOneLineTextHeightFromPaint(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    private int getTextWidthFromPaint(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    private int getOneLineTextHeightFromPaintBelowBaseline(String row, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(row, 0, row.length(), bounds);
        //the height in pixels below the baseline
        return bounds.bottom;
    }

    private Paint createTableCellPaint(int cellRow, int cellColumn, boolean isEmpty) {
        Typeface montserrat = ResourcesCompat.getFont(context, R.font.montserrat);
        Paint textPaint = new Paint();
        textPaint.setTypeface(montserrat);
        textPaint.setColor(TablesConstants.tableTextColor);
        boolean isInFirstRowOrFirstColumn = (cellRow == 0) || (cellColumn == 0);
        if (isInFirstRowOrFirstColumn)
            textPaint.setTextSize(50f);
        else if (!isEmpty)
            textPaint.setTextSize(23f);
        else //empty
            textPaint.setTextSize(70f);
        return textPaint;
    }

}
