package battleship.game.square;

import battleship.util.Constants;

public class Square {
    private char line;
    private int column;

    private String data;

    public Square(char line, int column) {
        this.line = line;
        this.column = column;
    }

    public Square(int line, int column) {
        this.line = (char) (line + Constants.FieldConstants.LINE_INDEX_DIFFERENCE);
        this.column = column + Constants.FieldConstants.COLUMN_INDEX_DIFFERENCE;
    }

    public Square(String input) {
        try {
            this.column = Integer.parseInt(input.substring(1));
            this.line = input.substring(0, 1).charAt(0);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input should have the following format: \"ab\", where " +
                    "\"a\" = any letter from 'A' to 'Z', and \"b\" any number from 1 to " +
                    Constants.FieldConstants.FIELD_SIZE);
        }
    }

    public Square() {
        this.data = Constants.FieldConstants.FOG;
    }

    public void setLine(char line) {
        this.line = line;
    }

    public void setLineFromIndex(int i) {
        setLine((char) (i + Constants.FieldConstants.LINE_INDEX_DIFFERENCE));
    }

    public char getLine() {
        return this.line;
    }

    public int getLineAsIndex() {
        return this.line - Constants.FieldConstants.LINE_INDEX_DIFFERENCE;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setColumnFromIndex(int column) {
        setColumn(column + Constants.FieldConstants.COLUMN_INDEX_DIFFERENCE);
    }

    public int getColumn() {
        return this.column;
    }

    public int getColumnAsIndex() {
        return this.column - Constants.FieldConstants.COLUMN_INDEX_DIFFERENCE;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Square other)) {
            return false;
        } else {
            return this.line == other.line && this.column == other.column;
        }
    }
}
