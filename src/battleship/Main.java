package battleship;

import battleship.game.BattleshipGame;

public class Main {
    public static void main(String[] args) {
        BattleshipGame game = new BattleshipGame();
        game.play();
//        Square s1 = new Square(1, 1);
//        Square s2 = new Square(1, 2);
//        System.out.println(s1.getDirectionTo(s2));
    }
}