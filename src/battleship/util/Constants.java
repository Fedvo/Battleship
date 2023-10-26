package battleship.util;

import static battleship.util.Constants.GameGridConstants.*;

public final class Constants {
    public static final class GameGridConstants {
        private GameGridConstants() {

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

    public static final class GameGridPrinterConstants {
        private GameGridPrinterConstants() {

        }
        public static final char LINE_IDENTIFIERS_STARTER = 'A';
        public static final String SEPARATOR = " ";
        public static final String FIRST_SQUARE = "%s";
        public static final String SQUARE = " %s";
        public static final String NEW_LINE = System.lineSeparator();
        public static final String ALLY_FILTER_REGEX = "[" + RESERVED + "]";
        public static final String ENEMY_FILTER_REGEX = "[" + SHIP + RESERVED + "]";
        public static final String TRAINING_FILTER_REGEX = "[" + RESERVED + "]";
        public static final String GAME_GRIDS_SEPARATOR = "---------------------";
    }

    public static final class BattleshipGamePrinterConstants {
        private BattleshipGamePrinterConstants() {

        }
        public static final String NEXT_PLAYER_SEPARATOR = "...";
    }
}
