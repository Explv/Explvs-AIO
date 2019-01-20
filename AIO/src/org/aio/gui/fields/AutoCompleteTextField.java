package org.aio.gui.fields;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A JTextField that will display a whole word as the user types a portion of
 * it. A better example would be, as the user types the first letter in the
 * word "text" the AutoCompleteTextField will display the letter "t" in the
 * components current foreground color and display the rest of the word "ext"
 * in the specified incomplete color (default is Color.GRAY.brighter()). The
 * displayed text changes as the user types "e" so now getText() would return
 * the string "te" and the incomplete portion displayed by the editor will be
 * "xt." If Enter is pressed while an incomplete word is being processes, then
 * the AutoCompleteTextField will replace what he user has typed with the
 * completed word and consider itself "finished," and if Enter is pressed a
 * second time, then it will fire a KeyEvent normally.
 * @author Brandon Buck
 * @version 1.0
 */
public class AutoCompleteTextField extends JTextField implements KeyListener,
        DocumentListener {
    private ArrayList<String> possibilities;
    private int currentGuess;
    private Color incompleteColor;
    private boolean areGuessing;
    private boolean caseSensitive;

    /**
     * Constructs a new AutoCompleteTextField with the 5 columns by default and
     * no case sensitivity on comparisons of guesses to entered text.
     */
    public AutoCompleteTextField() {
        this(5, false);
    }

    /**
     * Constructs a new AutoCompleteTextField with the specified number of
     * columns with no case sensitivity when comparing the current guess to the
     * text entered.
     * @param columns The number of columns you wish for the width of this AutoCompleteTextField.
     */
    public AutoCompleteTextField(int columns) {
        this(columns, false);
    }

    /**
     * Creates a new AutoCompleteTextField with the given number of columns in
     * width and the setting for case sensitivity when comparing the current
     * guess to the entered text.
     * @param columns The number of columns of text for this component.
     * @param caseSensitive <code>true</code> or <code>false</code> depending on if you want comparisons to be case sensitive or not.
     */
    public AutoCompleteTextField(int columns, boolean caseSensitive) {
        super.setColumns(columns);
        this.possibilities = new ArrayList<String>();
        this.incompleteColor = Color.GRAY.brighter();
        this.currentGuess = -1;
        this.areGuessing = false;
        this.caseSensitive = caseSensitive;
        this.addKeyListener(this);
        this.getDocument().addDocumentListener(this);
    }

    /** Add a new possibility to the list of possibilities.
     * Add a new possibility to the list of possibilities for the
     * AutoCommpleteTextField to process.
     * @param possibility The new possibility to add.
     */
    public void addPossibility(String possibility) {
        this.possibilities.add(possibility);
        Collections.sort(possibilities);
    }

    public void addPosibilities(Collection<String> possibilities) {
        this.possibilities.addAll(possibilities);
        Collections.sort(this.possibilities);
    }

    /** Removes a possibility from the list of possibilities.
     * Removes the given possibility from the list of possibilities so that it
     * will no longer be matched.
     * @param possibility The possibility to remove.
     */
    public void removePossibility(String possibility) {
        this.possibilities.remove(possibility);
    }

    /** Removes all possibilities in the list.
     * Removes every possibility in the list and starts over with an empty list
     * ready for new possibilities to be added.
     */
    public void removeAllPossibilities() {
        this.possibilities = new ArrayList<String>();
    }

    /** Sets the color to draw the incomplete guess in.
     * This sets the color that the incomplete guess text is drawn in.
     * @param incompleteColor The new color to draw the incomplete guess with.
     */
    public void setIncompleteColor(Color incompleteColor) {
        this.incompleteColor = incompleteColor;
    }

    /** Returns the current guess from the list of possibilities.
     * Returns the string at the location of the current guess in the list of
     * possibilities.
     * @return The current guess as a String.
     */
    private String getCurrentGuess() {
        if (this.currentGuess != -1)
            return this.possibilities.get(this.currentGuess);

        return this.getText();
    }

    /**
     * Changes the current case sensitive setting to the given setting.
     * @param caseSensitive <code>true</code> or <code>false</code> depending on if you want comparisons to be case sensitive or not.
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    private void findCurrentGuess() {
        String entered = this.getText();
        if (!this.caseSensitive)
            entered = entered.toLowerCase();

        for (int i = 0; i < this.possibilities.size(); i++) {
            currentGuess = -1;

            String possibility = this.possibilities.get(i);
            if (!this.caseSensitive)
                possibility = possibility.toLowerCase();
            if (possibility.startsWith(entered)) {
                this.currentGuess = i;
                break;
            }
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.areGuessing = false;
        this.currentGuess = -1;
    }

    @Override
    public void paintComponent(Graphics g) {
        String guess = this.getCurrentGuess();
        String drawGuess = guess;

        super.paintComponent(g);
        String entered = this.getText();
        Rectangle2D enteredBounds = g.getFontMetrics().getStringBounds(entered, g);

        if (!(this.caseSensitive)) {
            entered = entered.toLowerCase();
            guess = guess.toLowerCase();
        }

        if (!(guess.startsWith(entered)))
            this.areGuessing = false;

        if (entered != null && !(entered.equals(""))
                && this.areGuessing) {
            String subGuess = drawGuess.substring(entered.length(), drawGuess.length());
            Rectangle2D subGuessBounds = g.getFontMetrics().getStringBounds(drawGuess, g);

            int centeredY = ((getHeight() / 2) + (int)(subGuessBounds.getHeight() / 2));

            g.setColor(this.incompleteColor);
            g.drawString(subGuess, (int)(enteredBounds.getWidth()) + 2, centeredY - 2);
        }
    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.areGuessing) {
                this.setText(this.getCurrentGuess());
                this.areGuessing = false;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (this.areGuessing) {
                this.setText(this.getCurrentGuess());
                this.areGuessing = false;
                e.consume();
            }
        }
    }

    public void keyReleased(KeyEvent e) { }

    public void insertUpdate(DocumentEvent e) {
        String temp = this.getText();

        if (temp.length() == 1)
            this.areGuessing = true;

        if (this.areGuessing)
            this.findCurrentGuess();

    }

    public void removeUpdate(DocumentEvent e) {
        String temp = this.getText();

        if (!(this.areGuessing))
            this.areGuessing = true;

        if (temp.length() == 0)
            this.areGuessing = false;
        else if (this.areGuessing) {
            this.findCurrentGuess();
        }
    }

    public void changedUpdate(DocumentEvent e) { }
}
