package or.nevet.orgeneralhelpers.graphical.table_views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import or.nevet.orgeneralhelpers.R;

public class TableTextViewStylist {

    private static Typeface fontTypeface;
    private int cellSize;

    public TableTextViewStylist(Context context, int cellSize) {
        fontTypeface = ResourcesCompat.getFont(context, R.font.montserrat);
        this.cellSize = cellSize;
    }

    public void style(TextView textView) {
        textView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        textView.setTypeface(fontTypeface);
        textView.setMaxLines(1);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(20, 40, 20, 40);
        textView.setWidth(cellSize);
        textView.setHeight(cellSize);
        textView.setLayoutParams(new TableRow.LayoutParams(cellSize, cellSize));
    }
}
