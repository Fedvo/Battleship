package battleship.game;

import java.util.*;

import battleship.game.grid.printer.GameGridViewPerspective;
import battleship.game.square.Square;
import battleship.game.grid.GameGrid;
import battleship.game.ship.ShipType;
import battleship.game.grid.GridModificationResult;

import static battleship.game.printer.BattleshipGamePrintHelper.*;
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
    private boolean isAWinner = false;

    public BattleshipGame() {
        gridPlayer1 = new GameGrid();
        gridPlayer2 = new GameGrid();
    }
    public void play() {
        notifyNPlayerPlanningStage(1);
        placeShips(gridPlayer1);
        beginNextPlayerMove();

        notifyNPlayerPlanningStage(2);
        placeShips(gridPlayer2);
        beginNextPlayerMove();

        takeShoots();
    }

    private void beginNextPlayerMove() {
        notifyNextPlayerMove();
        scanner.nextLine();
    }

    private void takeShoots() {
        while (true) {
            printPVPGameGridView(gridPlayer2, gridPlayer1);
            if (isLastTakenShot(gridPlayer2)) {
                break;
            }
            beginNextPlayerMove();

            printPVPGameGridView(gridPlayer1, gridPlayer2);
            if (isLastTakenShot(gridPlayer1)) {
                break;
            }
            beginNextPlayerMove();
        }
        notifyLastShipSinking();
    }

    private boolean isLastTakenShot(GameGrid grid) {
        while (true) {
            String userInput = readUsersInputFromConsole();
            if (validateShotInput(userInput)) {
                Square shootCoordinates = new Square(userInput);
                GridModificationResult result = grid.registerShoot(shootCoordinates);
                printGameGridView(grid, GameGridViewPerspective.ENEMY_PERSPECTIVE);
                if (isGoodShotResult(result)) {
                    if (isAWinner) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    private boolean validateShotInput(String userInput) {
        return validateUserInputShoot(userInput) == InputValidationState.VALID;
    }

    private boolean isGoodShotResult(GridModificationResult shotResult) {
        switch(shotResult) {
            case OUT_OF_GAME_GRID -> {
                System.out.println();
                System.out.println(WRONG_COORDINATES);
                return false;
            }
            case HIT_REGISTERED -> {
                System.out.println();
                System.out.println(YOU_HIT_A_SHIP);
                return true;
            }
            case MISS_REGISTERED -> {
                System.out.println();
                System.out.println(YOU_MISSED);
                return true;
            }
            case SHIP_SANK -> {
                System.out.println();
                System.out.println(YOU_SANK_A_SHIP);
                return true;
            }
            case WINNER -> {
                isAWinner = true;
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private InputValidationState validateUserInputShoot(String userProvidedCoordinates) {
        if (isNotValidCoordinateFormat(userProvidedCoordinates)) {
            return InputValidationState.WRONG_FORMAT;
        } else {
            return InputValidationState.VALID;
        }
    }

    private void placeShips(GameGrid grid) {
        printGameGridView(grid, GameGridViewPerspective.ENEMY_PERSPECTIVE);
        for (ShipType shipType : ShipType.values()) {
            printPlaceShipMessage(shipType);
            placeShip(shipType, grid);
            printGameGridView(grid, GameGridViewPerspective.ALLY_PERSPECTIVE);
        }
    }

    private void placeShip(ShipType shipType, GameGrid grid) {
        while (true) {
            String userRawInput = readUsersInputFromConsole();
            List<String> userProvidedCoordinates = new ArrayList<>(Arrays.asList(userRawInput.split(" ")));
            if (validateShipPlacementInput(userProvidedCoordinates))  {
                Square frontSquare = new Square(userProvidedCoordinates.get(0));
                Square rearSquare = new Square(userProvidedCoordinates.get(1));
                if (!isShipOfCorrectSize(getShipSizeFromCoordinates(frontSquare, rearSquare), shipType)) {
                    notifyWrongShipSize(shipType.getName());
                } else if (isGoodShipAddingResult(grid.addShip(frontSquare, rearSquare, shipType))){
                        break;
                }
            }
        }
    }

    private boolean validateShipPlacementInput(List<String> userProvidedCoordinates) {
        switch (validateUserInputStrategyStage(userProvidedCoordinates)) {
            case VALID -> {
                return true;
            }
            case NOT_SAME_LANE_OR_COLUMN -> {
                notifyWrongShipPosition();
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isGoodShipAddingResult(GridModificationResult result) {
        switch(result) {
            case OUT_OF_GAME_GRID -> {
                notifyWrongShipPosition();
                return false;
            }
            case SHIPS_TO_CLOSE -> {
                notifyTooCloseToAnotherShip();
                return false;
            }
            case SHIP_PLACED -> {
                return true;
            }
            default -> {
                return false;
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
        emptyLine();
        return scanner.nextLine();
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
