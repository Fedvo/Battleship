package battleship.game;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import battleship.util.FieldPrintHelper;
import battleship.util.GameFieldAlteringResult;

import static battleship.util.CustomMessages.GamePlayMessage.*;
import static battleship.util.CustomMessages.GamePlayErrorMessage.*;

public class BattleshipGame {
    private enum InputValidationState {
        WRONG_SIZE,
        NOT_SAME_LANE_OR_COLUMN,
        WRONG_FORMAT,
        VALID
    }
    private final Scanner scanner = new Scanner(System.in);
    private final GameField fieldPlayer1;
    private final GameField fieldPlayer2;

    public BattleshipGame() {
        fieldPlayer1 = new GameField();
        fieldPlayer2 = new GameField();
    }
    public void play() {
        System.out.println(PLAYER_1_PLANNING_STAGE);
        placeShips(fieldPlayer1);

        System.out.println();
        System.out.println(NEXT_PLAYER_MOVE);
        System.out.print("...");
        scanner.nextLine();

        System.out.println(PLAYER_2_PLANNING_STAGE);
        placeShips(fieldPlayer2);

        System.out.println();
        System.out.println(NEXT_PLAYER_MOVE);
        System.out.print("...");
        scanner.nextLine();

        takeShoots();
    }

    private void takeShoots() {

        while (true) {
            System.out.println();
            FieldPrintHelper.printPVPView(fieldPlayer2, fieldPlayer1);

            if (isLastTakenShot(fieldPlayer2)) {
                break;
            }

            System.out.println();
            System.out.println(NEXT_PLAYER_MOVE);
            System.out.print("...");
            scanner.nextLine();

            System.out.println();
            FieldPrintHelper.printPVPView(fieldPlayer1, fieldPlayer2);

            if (isLastTakenShot(fieldPlayer1)) {
                break;
            }

            System.out.println();
            System.out.println(NEXT_PLAYER_MOVE);
            System.out.print("...");
            scanner.nextLine();
        }

        System.out.println();
        System.out.println(YOU_SANK_LAST_SHIP);
    }

    private boolean isLastTakenShot(GameField field) {
        main_loop:
        while (true) {
            String userInput = readUsersInputFromConsole();
            InputValidationState inputValidationResult = validateUserInputShoot(userInput);
            if (inputValidationResult != InputValidationState.VALID) {
                switch (inputValidationResult) {
                    case WRONG_FORMAT -> {}
                }
            } else {
                GameCell shootCoordinates = new GameCell(userInput);
                GameFieldAlteringResult result = field.registerShoot(shootCoordinates);

                System.out.println();
                FieldPrintHelper.printEnemyFieldView(field);

                switch(result) {
                    case OUT_OF_FIELD -> {
                        System.out.println();
                        System.out.println(WRONG_COORDINATES);
                    }
                    case HIT -> {
                        System.out.println();
                        System.out.println(YOU_HIT_A_SHIP);
                        break main_loop;
                    }
                    case MISS -> {
                        System.out.println();
                        System.out.println(YOU_MISSED);
                        break main_loop;
                    }
                    case SANK_A_SHIP -> {
                        System.out.println();
                        System.out.println(YOU_SANK_A_SHIP);
                        break main_loop;
                    }
                    case YOU_WON -> {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void placeShips(GameField field) {
        System.out.println();
        FieldPrintHelper.printEnemyFieldView(field);

        for (ShipType shipType : ShipType.values()) {
            switch (shipType) {
                case AIRCRAFT_CARRIER -> {
                    System.out.println();
                    System.out.println(PLACE_AIRCRAFT_CARRIER);
                }
                case BATTLESHIP -> {
                    System.out.println();
                    System.out.println(PLACE_BATTLESHIP);
                }
                case SUBMARINE -> {
                    System.out.println();
                    System.out.println(PLACE_SUBMARINE);
                }
                case CRUISER -> {
                    System.out.println();
                    System.out.println(PLACE_CRUISER);
                }
                case DESTROYER -> {
                    System.out.println();
                    System.out.println(PLACE_DESTROYER);
                }
            }
            placeShip(shipType, field);

            System.out.println();
            FieldPrintHelper.printAllyFieldView(field);
        }
    }

    private void placeShip(ShipType shipType, GameField field) {
        main_loop:
        while (true) {
            String userRawInput = readUsersInputFromConsole();
            List<String> userProvidedCoordinates = new ArrayList<>(Arrays.asList(userRawInput.split(" ")));
            InputValidationState validationResult = validateUserInputStrategyStage(userProvidedCoordinates);
            if (validationResult != InputValidationState.VALID) {
                switch (validationResult) {
                    case NOT_SAME_LANE_OR_COLUMN -> {
                        System.out.println();
                        System.out.println(WRONG_SHIP_LOCATION);
                    }
                    case WRONG_FORMAT -> {}
                    case WRONG_SIZE -> {}
                }
            } else {
                GameCell frontCell = new GameCell(userProvidedCoordinates.get(0));
                GameCell rearCell = new GameCell(userProvidedCoordinates.get(1));
                if (!isShipOfCorrectSize(getShipSizeFromCoordinates(frontCell, rearCell), shipType)) {
                    System.out.println();
                    System.out.printf((WRONG_SHIP_SIZE) + "%n", shipType.getName());
                } else {
                    switch(field.addShip(frontCell, rearCell, shipType)) {
                        case OUT_OF_FIELD -> {
                            System.out.println();
                            System.out.println(WRONG_SHIP_LOCATION);
                        }
                        case TOO_CLOSE -> {
                            System.out.println();
                            System.out.println(TOO_CLOSE_TO_ANOTHER_SHIP);
                        }
                        case ALTERED -> {
                            break main_loop;
                        }
                    }
                }
            }
        }
    }

    private boolean isShipOfCorrectSize(int providedShipSize, ShipType shipType) {
        return providedShipSize == shipType.getSize();
    }

    private int getShipSizeFromCoordinates(GameCell frontCell, GameCell rearCell) {
        return Math.max(
                Math.abs(frontCell.getLine() - rearCell.getLine()),
                Math.abs(frontCell.getColumn() - rearCell.getColumn())
        ) + 1;
    }

    private String readUsersInputFromConsole() {
        System.out.println();
        return scanner.nextLine();
    }

    private InputValidationState validateUserInputShoot(String userProvidedCoordinates) {
        if (isNotValidCoordinateFormat(userProvidedCoordinates)) {
            return InputValidationState.WRONG_FORMAT;
        } else {
            return InputValidationState.VALID;
        }
    }

    private InputValidationState validateUserInputStrategyStage(List<String> userProvidedCoordinates) {
        if (userProvidedCoordinates.size() != 2) {
            return InputValidationState.WRONG_SIZE;
        } else if (isNotValidCoordinateFormat(userProvidedCoordinates.get(0))
                || isNotValidCoordinateFormat(userProvidedCoordinates.get(1))) {
            return InputValidationState.WRONG_FORMAT;
        } else if (!isTheSameLineIdentifier(userProvidedCoordinates)
                && !isTheSameColumnIdentifier(userProvidedCoordinates)) {
            return InputValidationState.NOT_SAME_LANE_OR_COLUMN;
        } else {
            return InputValidationState.VALID;
        }
    }

    private boolean isTheSameLineIdentifier(List<String> coordinates) {
        return coordinates.get(0).charAt(0) == coordinates.get(1).charAt(0);
    }

    private boolean isTheSameColumnIdentifier(List<String> coordinates) {
        return coordinates.get(0).substring(1).equals(coordinates.get(1).substring(1));
    }

    private boolean isNotValidCoordinateFormat(String coordinate) {
        if (coordinate.length() < 2) {
            return true;
        } else if (coordinate.charAt(0) < 'A' || coordinate.charAt(0) > 'Z') {
            return true;
        } else {
            try {
                Integer.parseInt(coordinate.substring(1));
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }
}
