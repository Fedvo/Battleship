package battleship.game;

import battleship.util.Constants;
import static battleship.util.Constants.FieldConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Field {
    private static final List<List<String>> field = new ArrayList<>();

    public Field() {
        populateInitialField();

    }

    private void populateInitialField() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            field.add(new ArrayList<>(Arrays.asList(FOG.repeat(FIELD_SIZE).split(""))));
        }
    }

    public String getCellAsString(char line, int column) {
        if (line < 'A' || line > 'A' + FIELD_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong line identifier. Can be any character between 'A' and " + (char) ('A' + FIELD_SIZE));
        } else if (column < 1 || column > FIELD_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong column identifier [" + column + "]. Can be any integer between 1 and " +
                            FIELD_SIZE + " (inclusive)");
        }
        return field.get(fieldCoordinateToIndex(line)).get(fieldCoordinateToIndex(column));
    }

    private int fieldCoordinateToIndex(char line) {
        return line - LINE_INDEX_DIFFERENCE;
    }

    private int fieldCoordinateToIndex(int column) {
        return column - COLUMN_INDEX_DIFFERENCE;
    }

    public void addShip(ShipType shipType, char line, int column) {

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < FIELD_SIZE; i++) {
            stringBuilder.append(field.get(i).toString());
        }
        return stringBuilder.toString();
    }
}
