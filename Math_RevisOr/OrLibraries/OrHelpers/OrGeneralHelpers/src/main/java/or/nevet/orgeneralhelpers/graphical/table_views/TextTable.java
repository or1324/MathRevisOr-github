package or.nevet.orgeneralhelpers.graphical.table_views;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

public class TextTable extends Table {

    private final String[][] texts;

    public TextTable(Context context, int numOfRows, int numOfColumns, int fontSizeInPx, String[][] texts) {
        super(context, numOfRows, numOfColumns, fontSizeInPx);
        this.texts = texts;
    }

    @Override
    protected String getValueAtCell(int row, int column) {
        return getCellAt(row, column).getText().toString();
    }

    protected TextView createTableCell(int row, int column) {
        TextView textView = new TextView(getContext());
        textView.setText(texts[row][column]);
        styleCell(textView);
        return textView;
    }
}
