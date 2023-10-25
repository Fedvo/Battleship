package battleship.util;

import battleship.game.GameField;
import battleship.game.GameCell;

import static battleship.util.Constants.FieldPrinterConstants.*;
import static battleship.util.Constants.FieldConstants.*;

public class FieldPrintHelper {
    public static void printPVPView(GameField enemyField, GameField playersField) {
        printEnemyFieldView(enemyField);
        System.out.println(FIELDS_SEPARATOR);
        printAllyFieldView(playersField);
    }
    public static void printAllyFieldView(GameField field) {
        System.out.println(generateGlobalFieldView((field)).replaceAll(ALLY_FILTER_REGEX, FOG));
    }

    public static void printEnemyFieldView(GameField field) {
        System.out.println(generateGlobalFieldView((field)).replaceAll(ENEMY_FILTER_REGEX, FOG));
    }

    public static void printGodFieldView(GameField field) {
        System.out.println(generateGlobalFieldView((field)));
    }

    private static String generateGlobalFieldView(GameField field) {
        StringBuilder stringBuilder = new StringBuilder(generateColumnNumbersString());
        char lineIdentifier = 'A';
        for (int i = 0; i < FIELD_SIZE; i++) {
            stringBuilder.append(FIRST_CELL.formatted(lineIdentifier));
            for (int j = 0; j < FIELD_SIZE; j++) {
                stringBuilder.append(CELL.formatted(field.getCellData(
                        new GameCell(lineIdentifier, j + 1))));
            }
            stringBuilder.append(NEW_LINE);
            lineIdentifier ++;
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    private static String generateColumnNumbersString() {
        StringBuilder stringBuilder = new StringBuilder(FIRST_CELL.formatted(" "));
        for (int i = 1; i <= FIELD_SIZE; i++) {
            stringBuilder.append(CELL.formatted(i));
        }
        return stringBuilder.append(NEW_LINE).toString();
    }
}
