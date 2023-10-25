package battleship.util;

import static battleship.util.Constants.GridConstants.*;

public final class Constants {
    public static final class GridConstants {
        private GridConstants() {

        }
        public static final int GRID_SIZE = 10;
        public static final int COLUMN_INDEX_DIFFERENCE = 1;
        public static final int LINE_INDEX_DIFFERENCE = 65;
        public static final String FOG = "~";
        public static final String SHIP = "O";
        public static final String MISS_SHOT = "M";
        public static final String HIT = "X";
        public static final String RESERVED = "R";
    }

    public static final class GridPrinterConstants {
        private GridPrinterConstants() {

        }
        public static final String FIRST_SQUARE = "%s";
        public static final String SQUARE = " %s";
        public static final String NEW_LINE = System.lineSeparator();
        public static final String ALLY_FILTER_REGEX = "[" + RESERVED + "]";
        public static final String ENEMY_FILTER_REGEX = "[" + SHIP + RESERVED + "]";
        public static final String TRAINING_FILTER_REGEX = "[" + RESERVED + "]";
        public static final String GRIDS_SEPARATOR = "---------------------";
    }
}
