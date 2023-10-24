package battleship.game;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import battleship.util.CustomMessages;
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
    private final GameField field;

    public BattleshipGame() {
        field = new GameField();
    }
    public void play() {
        FieldPrintHelper.printGodFieldView(field);
        placeShips();
    }

    private void placeShips() {
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
            placeShip(shipType);

            System.out.println();
            FieldPrintHelper.printAllyFieldView(field);
        }
    }

    private void placeShip(ShipType shipType) {
        main_loop:
        while (true) {
            String userRawInput = readUsersInputFromConsole();
            List<String> userProvidedCoordinates = new ArrayList<>(Arrays.asList(userRawInput.split(" ")));
            InputValidationState validationResult = validateUserInput(userProvidedCoordinates);
            if (validationResult != InputValidationState.VALID) {
                switch (validationResult) {
                    case NOT_SAME_LANE_OR_COLUMN -> {}
                    case WRONG_FORMAT -> {}
                    case WRONG_SIZE -> {}
                }
            } else {
                GameCell frontCell = new GameCell(userProvidedCoordinates.get(0));
                GameCell rearCell = new GameCell(userProvidedCoordinates.get(1));
                if (!isCorrectSize(getSizeFromCoordinates(frontCell, rearCell), shipType)) {
                    System.out.println();
                    System.out.printf((WRONG_SHIP_SIZE) + "%n", shipType.getName());
                } else {
                    switch(field.addShip(frontCell, rearCell)) {
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

    private boolean isCorrectSize(int providedShipSize, ShipType shipType) {
        return providedShipSize == shipType.getSize();
    }

    private int getSizeFromCoordinates(GameCell frontCell, GameCell rearCell) {
        return Math.max(
                Math.abs(frontCell.getLine() - rearCell.getLine()),
                Math.abs(frontCell.getColumn() - rearCell.getColumn())
        ) + 1;
    }

    private String readUsersInputFromConsole() {
        System.out.println();
        return scanner.nextLine();
    }

    private InputValidationState validateUserInput(List<String> userProvidedCoordinates) {
        if (userProvidedCoordinates.size() != 2) {
            return InputValidationState.WRONG_SIZE;
        } else if (!isValidCoordinateFormat(userProvidedCoordinates.get(0))
                || !isValidCoordinateFormat(userProvidedCoordinates.get(1))) {
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

    private boolean isValidCoordinateFormat(String coordinate) {
        if (coordinate.length() < 2) {
            return false;
        } else if (coordinate.charAt(0) < 'A' || coordinate.charAt(0) > 'Z') {
            return false;
        } else {
            try {
                Integer.parseInt(coordinate.substring(1));
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
