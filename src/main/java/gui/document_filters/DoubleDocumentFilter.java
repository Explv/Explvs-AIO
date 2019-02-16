package gui.document_filters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubleDocumentFilter extends DocumentFilter {
    private static final Pattern DOUBLE_REGEX = Pattern.compile("\\d*(\\.\\d*)?");

    @Override
    public void replace(DocumentFilter.FilterBypass fb,
                        int offset,
                        int length,
                        String text,
                        AttributeSet attrs) throws BadLocationException {

        Matcher matcher = DOUBLE_REGEX.matcher(text);

        if (!matcher.matches()) {
            return;
        }

        super.replace(fb, offset, length, text, attrs);
    }
}
