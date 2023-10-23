package battleship.util;

import battleship.game.Field;

import static battleship.util.Constants.FieldPrinterConstants.*;
import static battleship.util.Constants.FieldConstants.*;

public class FieldPrintHelper {
    public static void printAllyFieldView(Field field) {
        System.out.println(generateGlobalFieldView((field)).replaceAll("[" + MISS_SHOT + HIT + "]", FOG));
    }

    public static void printEnemyFieldView(Field field) {
        System.out.println(generateGlobalFieldView((field)).replaceAll("[" + SHIP + "]", FOG));
    }

    private static String generateGlobalFieldView(Field field) {
        StringBuilder stringBuilder = new StringBuilder(generateColumnNumbersString());
        char lineIdentifier = 'A';
        for (int i = 0; i < FIELD_SIZE; i++) {
            stringBuilder.append(FIRST_CELL.formatted(lineIdentifier));
            for (int j = 0; j < FIELD_SIZE; j++) {
                stringBuilder.append(CELL.formatted(field.getCellAsString((char) (lineIdentifier + 1), j + 1)));
            }
            stringBuilder.append(NEW_LINE);
            lineIdentifier ++;
        }
        return stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "").toString();
    }

    private static String generateColumnNumbersString() {
        StringBuilder stringBuilder = new StringBuilder(FIRST_CELL.formatted(" "));
        for (int i = 1; i <= FIELD_SIZE; i++) {
            stringBuilder.append(CELL.formatted(i));
        }
        return stringBuilder.append(NEW_LINE).toString();
    }
}
