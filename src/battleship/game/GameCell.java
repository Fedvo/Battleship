package battleship.game;

import battleship.util.Constants;

public class GameCell {
    private char line;
    private int column;

    public GameCell(char line, int column) {
        this.line = line;
        this.column = column;
    }

    public GameCell(int line, int column) {
        this.line = (char) (line + Constants.FieldConstants.LINE_INDEX_DIFFERENCE);
        this.column = column + Constants.FieldConstants.COLUMN_INDEX_DIFFERENCE;
    }

    public GameCell(String input) {
        try {
            this.column = Integer.parseInt(input.substring(1));
            this.line = input.substring(0, 1).charAt(0);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input should have the following format: \"ab\", where " +
                    "\"a\" = any letter from 'A' to 'Z', and \"b\" any number from 1 to " +
                    Constants.FieldConstants.FIELD_SIZE);
        }
    }

    public void setLine(char line) {
        this.line = line;
    }

    public char getLine() {
        return this.line;
    }

    public int getLineIndex() {
        return this.line - Constants.FieldConstants.LINE_INDEX_DIFFERENCE;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getColumn() {
        return this.column;
    }

    public int getColumnIndex() {
        return this.column - Constants.FieldConstants.COLUMN_INDEX_DIFFERENCE;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GameCell other)) {
            return false;
        } else {
            return this.line == other.line && this.column == other.column;
        }
    }
}
