package battleship.game.ship;

import battleship.game.square.Square;

import java.util.List;
import java.util.ArrayList;

public class Ship {
    private final List<Square> listOfOccupiedSquares = new ArrayList<>();
    private final List<Square> listOfSegments = new ArrayList<>();

    private int numberOfAliveSegments;

    private final ShipType shipType;

    public Ship(ShipType shipType) {
        this.shipType = shipType;
        numberOfAliveSegments = shipType.getSize();
    }

    public void addSquare(Square square) {
        listOfOccupiedSquares.add(square);
        listOfSegments.add(square);
    }

    public void destroySquare(Square square) {
        if (listOfSegments.contains(square)) {
            listOfSegments.remove(square);
            numberOfAliveSegments--;
        }
    }

    public boolean isAlive() {
        return numberOfAliveSegments > 0;
    }

    public boolean isOnSquare(Square square) {
        return listOfOccupiedSquares.contains(square);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ship ship)) {
            return false;
        } else {
            return this.shipType.equals(ship.shipType);
        }
    }
}
