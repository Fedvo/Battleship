package battleship.game;

import java.util.*;

import battleship.game.square.Square;
import battleship.game.grid.GameGrid;
import battleship.game.grid.GameGridPrintHelper;
import battleship.game.ship.ShipType;
import battleship.game.grid.GridModificationResult;

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
    private final GameGrid gridPlayer1;
    private final GameGrid gridPlayer2;

    public BattleshipGame() {
        gridPlayer1 = new GameGrid();
        gridPlayer2 = new GameGrid();
    }
    public void play() {
        System.out.println(PLAYER_1_PLANNING_STAGE);
        placeShips(gridPlayer1);

        System.out.println();
        System.out.println(NEXT_PLAYER_MOVE);
        System.out.print("...");
        scanner.nextLine();

        System.out.println(PLAYER_2_PLANNING_STAGE);
        placeShips(gridPlayer2);

        System.out.println();
        System.out.println(NEXT_PLAYER_MOVE);
        System.out.print("...");
        scanner.nextLine();

        takeShoots();
    }

    private void takeShoots() {

        while (true) {
            System.out.println();
            GameGridPrintHelper.printPVPView(gridPlayer2, gridPlayer1);

            if (isLastTakenShot(gridPlayer2)) {
                break;
            }

            System.out.println();
            System.out.println(NEXT_PLAYER_MOVE);
            System.out.print("...");
            scanner.nextLine();

            System.out.println();
            GameGridPrintHelper.printPVPView(gridPlayer1, gridPlayer2);

            if (isLastTakenShot(gridPlayer1)) {
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

    private boolean isLastTakenShot(GameGrid grid) {
        main_loop:
        while (true) {
            String userInput = readUsersInputFromConsole();
            InputValidationState inputValidationResult = validateUserInputShoot(userInput);
            if (inputValidationResult != InputValidationState.VALID) {
                Objects.requireNonNull(inputValidationResult);
            } else {
                Square shootCoordinates = new Square(userInput);
                GridModificationResult result = grid.registerShoot(shootCoordinates);

                System.out.println();
                GameGridPrintHelper.printEnemyFieldView(grid);

                switch(result) {
                    case SHIP_OUT_OF_GRID -> {
                        System.out.println();
                        System.out.println(WRONG_COORDINATES);
                    }
                    case HIT_REGISTERED -> {
                        System.out.println();
                        System.out.println(YOU_HIT_A_SHIP);
                        break main_loop;
                    }
                    case MISS_REGISTERED -> {
                        System.out.println();
                        System.out.println(YOU_MISSED);
                        break main_loop;
                    }
                    case SHIP_SANK -> {
                        System.out.println();
                        System.out.println(YOU_SANK_A_SHIP);
                        break main_loop;
                    }
                    case WINNER -> {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void placeShips(GameGrid grid) {
        System.out.println();
        GameGridPrintHelper.printEnemyFieldView(grid);

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
            placeShip(shipType, grid);

            System.out.println();
            GameGridPrintHelper.printAllyFieldView(grid);
        }
    }

    private void placeShip(ShipType shipType, GameGrid grid) {
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
                    case WRONG_FORMAT, WRONG_SIZE -> {}
                }
            } else {
                Square frontSquare = new Square(userProvidedCoordinates.get(0));
                Square rearSquare = new Square(userProvidedCoordinates.get(1));
                if (!isShipOfCorrectSize(getShipSizeFromCoordinates(frontSquare, rearSquare), shipType)) {
                    System.out.println();
                    System.out.printf((WRONG_SHIP_SIZE) + "%n", shipType.getName());
                } else {
                    switch(grid.addShip(frontSquare, rearSquare, shipType)) {
                        case SHIP_OUT_OF_GRID -> {
                            System.out.println();
                            System.out.println(WRONG_SHIP_LOCATION);
                        }
                        case SHIPS_TO_CLOSE -> {
                            System.out.println();
                            System.out.println(TOO_CLOSE_TO_ANOTHER_SHIP);
                        }
                        case SHIP_PLACED -> {
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

    private int getShipSizeFromCoordinates(Square frontCell, Square rearCell) {
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
        } else if (isDiagonal(userProvidedCoordinates)) {
            return InputValidationState.NOT_SAME_LANE_OR_COLUMN;
        } else {
            return InputValidationState.VALID;
        }
    }

    private boolean isDiagonal(List<String> coordinates) {
        switch (new Square(coordinates.get(0)).getDirectionTo(new Square(coordinates.get(1)))) {
            case LEFT_DOWN, LEFT_UP, RIGHT_DOWN, RIGHT_UP -> {
                return true;
            }
            default -> {
                return false;
            }
        }
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
