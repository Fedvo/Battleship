package battleship;

import battleship.game.Field;
import battleship.util.FieldPrintHelper;

public class Main {

    public static void main(String[] args) {
        Field field = new Field();
        FieldPrintHelper.printAllyFieldView(field);
    }
}
