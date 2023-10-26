package battleship.game.grid.printer;

import battleship.game.grid.GameGrid;
import battleship.game.square.Square;

import static battleship.util.Constants.GameGridPrinterConstants.*;
import static battleship.util.Constants.GameGridConstants.*;

public class GameGridPrintHelper {

    public static void printGameGridView(GameGrid gameGrid, GameGridViewPerspective viewType) {
        String view = "";
        switch (viewType) {
            case ALLY_PERSPECTIVE -> view = generateGlobalFieldView((gameGrid)).replaceAll(ALLY_FILTER_REGEX, FOG);
            case ENEMY_PERSPECTIVE -> view = generateGlobalFieldView((gameGrid)).replaceAll(ENEMY_FILTER_REGEX, FOG);
            case GOD_PERSPECTIVE -> view = generateGlobalFieldView((gameGrid));
        }
        System.out.println(view);
    }

    public static void printPVPGameGridView(GameGrid enemyGameGrid, GameGrid playerGameGrid) {
        printGameGridView(enemyGameGrid, GameGridViewPerspective.ENEMY_PERSPECTIVE);
        System.out.println(GAME_GRIDS_SEPARATOR);
        printGameGridView(playerGameGrid, GameGridViewPerspective.ALLY_PERSPECTIVE);
    }

    private static String generateGlobalFieldView(GameGrid field) {
        StringBuilder stringBuilder = new StringBuilder(generateColumnNumbersString());
        char lineIdentifier = LINE_IDENTIFIERS_STARTER;
        for (int i = 0; i < GRID_SIZE; i++) {
            stringBuilder.append(FIRST_SQUARE.formatted(lineIdentifier));
            for (int j = 0; j < GRID_SIZE; j++) {
                stringBuilder.append(SQUARE.formatted(field.getSquareData(
                        new Square(lineIdentifier, j + 1))));
            }
            stringBuilder.append(NEW_LINE);
            lineIdentifier ++;
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    private static String generateColumnNumbersString() {
        StringBuilder stringBuilder = new StringBuilder(FIRST_SQUARE.formatted(SEPARATOR));
        for (int i = 1; i <= GRID_SIZE; i++) {
            stringBuilder.append(SQUARE.formatted(i));
        }
        return stringBuilder.append(NEW_LINE).toString();
    }
}
