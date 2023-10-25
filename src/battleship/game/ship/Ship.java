package battleship.game.ship;

import battleship.game.square.Square;

import java.util.List;
import java.util.ArrayList;

public class Ship {
    private final List<Square> listOfOccupiedCells = new ArrayList<>();
    private final List<Square> listOfExistingCells = new ArrayList<>();

    private int numberOfExistingCells;

    private final ShipType shipType;

    public Ship(ShipType shipType) {
        this.shipType = shipType;
        numberOfExistingCells = shipType.getSize();
    }

    public void addCell(Square cell) {
        listOfOccupiedCells.add(cell);
        listOfExistingCells.add(cell);
    }

    public void destroyCell(Square cell) {
        if (listOfExistingCells.contains(cell)) {
            listOfExistingCells.remove(cell);
            numberOfExistingCells--;
        }
    }

    public boolean isAlive() {
        return numberOfExistingCells > 0;
    }

    public boolean containsCell(Square cell) {
        return listOfOccupiedCells.contains(cell);
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
