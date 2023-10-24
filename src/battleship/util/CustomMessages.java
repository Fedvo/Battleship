package battleship.util;

public class CustomMessages {
    public static final class GamePlayMessage {
        private GamePlayMessage() {

        }
        public static final String PLACE_AIRCRAFT_CARRIER = "Enter the coordinates of the Aircraft Carrier (5 cells):";
        public static final String PLACE_BATTLESHIP = "Enter the coordinates of the Battleship (4 cells):";
        public static final String PLACE_SUBMARINE = "Enter the coordinates of the Submarine (3 cells):";
        public static final String PLACE_CRUISER = "Enter the coordinates of the Cruiser (3 cells):";
        public static final String PLACE_DESTROYER = "Enter the coordinates of the Destroyer (2 cells):";
        public static final String THE_GAME_STARTS = "The game starts!";
        public static final String TAKE_A_SHOT = "Take a shot!";
        public static final String YOU_MISSED = "You missed!";
        public static final String YOU_HIT_A_SHIP = "You hit a ship!";
    }

    public static final class GamePlayErrorMessage {
        private GamePlayErrorMessage() {

        }

        public static final String WRONG_SHIP_SIZE = "Error! Wrong length of the %s! Try again:";
        public static final String WRONG_SHIP_LOCATION = "Error! Wrong ship location! Try again:";
        public static final String TOO_CLOSE_TO_ANOTHER_SHIP =
                "Error! You placed it too close to another one. Try again:";
        public static final String WRONG_COORDINATES = "Error! You entered the wrong coordinates! Try again:";
    }
}
