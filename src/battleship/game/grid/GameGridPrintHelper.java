package battleship.game.grid;

import battleship.game.square.Square;

import static battleship.util.Constants.GridPrinterConstants.*;
import static battleship.util.Constants.GridConstants.*;

public class GameGridPrintHelper {
    public static void printPVPView(GameGrid enemyField, GameGrid playersField) {
        printEnemyFieldView(enemyField);
        System.out.println(GRIDS_SEPARATOR);
        printAllyFieldView(playersField);
    }
    public static void printAllyFieldView(GameGrid field) {
        System.out.println(generateGlobalFieldView((field)).replaceAll(ALLY_FILTER_REGEX, FOG));
    }

    public static void printEnemyFieldView(GameGrid field) {
        System.out.println(generateGlobalFieldView((field)).replaceAll(ENEMY_FILTER_REGEX, FOG));
    }

    public static void printGodFieldView(GameGrid field) {
        System.out.println(generateGlobalFieldView((field)));
    }

    private static String generateGlobalFieldView(GameGrid field) {
        StringBuilder stringBuilder = new StringBuilder(generateColumnNumbersString());
        char lineIdentifier = 'A';
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
        StringBuilder stringBuilder = new StringBuilder(FIRST_SQUARE.formatted(" "));
        for (int i = 1; i <= GRID_SIZE; i++) {
            stringBuilder.append(SQUARE.formatted(i));
        }
        return stringBuilder.append(NEW_LINE).toString();
    }
}
