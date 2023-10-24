package battleship.util;

import static battleship.util.Constants.FieldConstants.*;

public final class Constants {
    public static final class FieldConstants {
        private FieldConstants() {

        }
        public static final int FIELD_SIZE = 10;
        public static final int COLUMN_INDEX_DIFFERENCE = 1;
        public static final int LINE_INDEX_DIFFERENCE = 65;
        public static final String FOG = "~";
        public static final String SHIP = "O";
        public static final String MISS_SHOT = "M";
        public static final String HIT = "X";
        public static final String RESERVED = "R";
    }

    public static final class FieldPrinterConstants {
        private FieldPrinterConstants() {

        }
        public static final String FIRST_CELL = "%s";
        public static final String CELL = " %s";
        public static final String NEW_LINE = System.lineSeparator();
        public static final String ALLY_FILTER_REGEX = "[" + MISS_SHOT + HIT + RESERVED + "]";
        public static final String ENEMY_FILTER_REGEX = "[" + SHIP + RESERVED + "]";
    }
}
