package battleship.game;

import battleship.util.FieldPrintHelper;

public class BattleshipGame {
    public void play() {
        Field field = new Field();
        FieldPrintHelper.printAllyFieldView(field);

    }
}
