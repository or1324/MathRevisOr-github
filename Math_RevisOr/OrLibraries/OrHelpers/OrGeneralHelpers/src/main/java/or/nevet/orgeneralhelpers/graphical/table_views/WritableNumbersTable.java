package or.nevet.orgeneralhelpers.graphical.table_views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;

import java.math.BigInteger;

import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.graphical.or_one_line_auto_size_edit_text.focusable.OrOneLineAutoSizeFocusableImageHintEditText;

public class WritableNumbersTable extends Table {

    private final Drawable imageHint;
    private final Integer[][] initialValues;

    public WritableNumbersTable(Context context, int numOfRows, int numOfColumns, int cellSize, Drawable imageHint, Integer[][] initialValues) {
        super(context, numOfRows, numOfColumns, cellSize);
        this.imageHint = imageHint;
        this.initialValues = initialValues;
    }

    protected OrOneLineAutoSizeFocusableImageHintEditText createTableCell(int row, int column) {
        OrOneLineAutoSizeFocusableImageHintEditText editText = new OrOneLineAutoSizeFocusableImageHintEditText(getContext(), InputType.TYPE_CLASS_NUMBER, imageHint);
        styleCell(editText);
        if (initialValues != null)
            if (initialValues[row][column] != null) {
                editText.setText(String.valueOf(initialValues[row][column]));
                editText.requestFocus();
            }
        return editText;
    }

    public Integer getValueAtCell(int row, int column) {
        String number = "";
        try {
            number = getCellAt(row, column).getText().toString();
            return Integer.parseInt(number);
        } catch (Exception e) {
            try {
                new BigInteger(number);
            } catch (Exception e1) {
                return null;
            }
            // We're here iff s represents a valid integer that's outside
            // of java.lang.Integer range. Consider using custom exception type.
            throw new RuntimeException(UserMessagesConstants.theNumberIsTooBig);
        }
    }
}
