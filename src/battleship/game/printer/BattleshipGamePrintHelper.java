package battleship.game.printer;

import battleship.game.grid.GameGrid;
import battleship.game.grid.printer.GameGridPrintHelper;
import battleship.game.grid.printer.GameGridViewPerspective;
import battleship.game.ship.ShipType;

import static battleship.util.Constants.BattleshipGamePrinterConstants.*;
import static battleship.util.CustomMessages.GamePlayMessage.*;
import static battleship.util.CustomMessages.GamePlayErrorMessage.*;

public class BattleshipGamePrintHelper {
    public static void printGameGridView(GameGrid grid, GameGridViewPerspective gameGridViewPerspective) {
        emptyLine();
        GameGridPrintHelper.printGameGridView(grid, gameGridViewPerspective);
    }

    public static void printPVPGameGridView(GameGrid playerGameGrid, GameGrid enemyGameGrid) {
        emptyLine();
        GameGridPrintHelper.printPVPGameGridView(playerGameGrid, enemyGameGrid);
    }

    public static void printPlaceShipMessage(ShipType shipType) {
        switch (shipType) {
            case AIRCRAFT_CARRIER -> printBlock(PLACE_AIRCRAFT_CARRIER);
            case BATTLESHIP -> printBlock(PLACE_BATTLESHIP);
            case SUBMARINE -> printBlock(PLACE_SUBMARINE);
            case CRUISER -> printBlock(PLACE_CRUISER);
            case DESTROYER -> printBlock(PLACE_DESTROYER);
        }
    }

    public static void notifyWrongShipPosition() {
        printBlock(WRONG_SHIP_POSITION);
    }

    public static void notifyWrongShipSize(String shipName) {
        printBlock(((WRONG_SHIP_SIZE) + "%n").formatted(shipName));
    }

    public static void notifyTooCloseToAnotherShip() {
        printBlock(TOO_CLOSE_TO_ANOTHER_SHIP);
    }

    public static void notifyNextPlayerMove() {
        printBlock(NEXT_PLAYER_MOVE);
        printSingleLine(NEXT_PLAYER_SEPARATOR);
    }

    public static void notifyNPlayerPlanningStage(int playerNumber) {
        if (playerNumber == 1) {
            printSingleLine(PLAYER_1_PLANNING_STAGE);
        } else {
            printSingleLine(PLAYER_2_PLANNING_STAGE);
        }
    }

    public static void notifyLastShipSinking() {
        printBlock(YOU_SANK_LAST_SHIP);
    }

    public static void emptyLine() {
        System.out.println();
    }

    private static void printSingleLine(String data) {
        System.out.println(data);
    }

    private static void printBlock(String data) {
        emptyLine();
        System.out.println(data);
    }
}
