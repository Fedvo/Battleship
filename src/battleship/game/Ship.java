package battleship.game;

import java.util.List;
import java.util.ArrayList;

public class Ship {
    private final List<GameCell> listOfOccupiedCells = new ArrayList<>();
    private final List<GameCell> listOfExistingCells = new ArrayList<>();

    private int numberOfExistingCells;

    private final ShipType shipType;

    public Ship(ShipType shipType) {
        this.shipType = shipType;
        numberOfExistingCells = shipType.getSize();
    }

    public void addCell(GameCell cell) {
        listOfOccupiedCells.add(cell);
        listOfExistingCells.add(cell);
    }

    public void destroyCell(GameCell cell) {
        if (listOfExistingCells.contains(cell)) {
            listOfExistingCells.remove(cell);
            numberOfExistingCells--;
        }
    }

    public boolean isAlive() {
        return numberOfExistingCells > 0;
    }

    public boolean containsCell(GameCell cell) {
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
