package battleship.game.grid;

import battleship.game.square.Square;
import battleship.game.ship.Ship;
import battleship.game.ship.ShipType;
import battleship.util.Constants;
import battleship.game.square.SquareType;

import static battleship.util.Constants.FieldConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameGrid {
    private final Square[][] grid = new Square[FIELD_SIZE][FIELD_SIZE];
    private final List<Ship> listOfShips = new ArrayList<>();
    private Ship currentShip;
    private int numberOfSquaresOccupiedByShips = 0;

    public GameGrid() {
        populateInitialField();
    }

    private void populateInitialField() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                grid[i][j] = new Square();
            }
        }
    }

    public String getCellData(Square cell) {
        if (cell.getLine() < 'A' || cell.getLine() > 'A' + FIELD_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong line identifier. Can be any character between 'A' and " + (char) ('A' + FIELD_SIZE));
        } else if (cell.getColumn() < 1 || cell.getColumn() > FIELD_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong column identifier [" + cell.getColumn() + "]. Can be any integer between 1 and " +
                            FIELD_SIZE + " (inclusive)");
        }
        return getValueFromGrid(cell.getLineAsIndex(), cell.getColumnAsIndex());
    }

    private String getValueFromGrid(int i, int j) {
        return grid[i][j].getData();
    }

    private void setValueInGrid(int i, int j, String value) {
        grid[i][j].setData(value);
    }

    public GridModificationResult addShip(Square frontCell, Square rearCell, ShipType shipType) {
        if (isCellNotOnTheField(frontCell) || isCellNotOnTheField(rearCell)) {
            return GridModificationResult.SHIP_OUT_OF_GRID;
        } else if (isToCloseToOtherShip(frontCell, rearCell)) {
            return GridModificationResult.SHIPS_TO_CLOSE;
        } else {
            listOfShips.add(new Ship(shipType));
            currentShip = listOfShips.get(listOfShips.size() - 1);
            setShipCells(frontCell, rearCell);
            numberOfSquaresOccupiedByShips += shipType.getSize();
            return GridModificationResult.SHIP_PLACED;
        }
    }

    public GridModificationResult registerShoot(Square shotCoordinates) {
        if (isCellNotOnTheField(shotCoordinates)) {
            return GridModificationResult.SHIP_OUT_OF_GRID;
        } else {
            setCell(shotCoordinates, SquareType.SHOT);
            if (getCellData(shotCoordinates).equals(HIT)) {
                if (currentShip.isAlive()) {
                    return GridModificationResult.HIT_REGISTERED;
                } else {
                    if (numberOfSquaresOccupiedByShips == 0) {
                        return GridModificationResult.WINNER;
                    } else {
                        return GridModificationResult.SHIP_SANK;
                    }
                }
            }
            return GridModificationResult.MISS_REGISTERED;
        }
    }

    public void setCell(Square shotCoordinates, SquareType cellType) {
        setCell(shotCoordinates.getLineAsIndex(), shotCoordinates.getColumnAsIndex(), cellType);
    }

    private void setShipCells(Square frontCell, Square rearCell) {
        int frontLine = frontCell.getLineAsIndex();
        int rearLine = rearCell.getLineAsIndex();
        if (frontLine == rearLine) {
            setShipCells(frontLine, frontCell.getColumnAsIndex(), rearCell.getColumnAsIndex(), true);
        } else {
            setShipCells(frontCell.getColumnAsIndex(), frontLine, rearLine, false);
        }
    }

    private void setShipCells(int constantCoordinate, int dynamicCoordinateStart, int dynamicCoordinateFinish,
                                      boolean sameLine) {
        int maxDynamic = Math.max(dynamicCoordinateStart, dynamicCoordinateFinish);
        int minDynamic = Math.min(dynamicCoordinateStart, dynamicCoordinateFinish);
        for (int i = minDynamic; i <= maxDynamic; i++) {
            if (sameLine) {
                setCell(constantCoordinate, i, SquareType.SHIP);
                setReservedCells(constantCoordinate, i);
            } else {
                setCell(i, constantCoordinate, SquareType.SHIP);
                setReservedCells(i, constantCoordinate);
            }

        }
    }

    private void setReservedCells(int i, int j) {
        if (i - 1 >= 0 && j - 1 >= 0) {
            setCell(i - 1, j - 1, SquareType.RESERVED);
            setCell(i, j - 1, SquareType.RESERVED);
            setCell(i - 1, j, SquareType.RESERVED);
        } else if (j - 1 >= 0) {
            setCell(i, j -1, SquareType.RESERVED);
        } else if (i - 1 >= 0) {
            setCell(i - 1, j, SquareType.RESERVED);
        }

        if (i + 1 < FIELD_SIZE && j + 1 < FIELD_SIZE) {
            setCell(i + 1, j + 1, SquareType.RESERVED);
            setCell(i + 1, j, SquareType.RESERVED);
            setCell(i, j + 1, SquareType.RESERVED);
        } else if (i + 1 < FIELD_SIZE) {
            setCell(i + 1, j, SquareType.RESERVED);
        } else if (j + 1 < FIELD_SIZE) {
            setCell(i, j + 1, SquareType.RESERVED);
        }

        if (i + 1  < FIELD_SIZE && j - 1 >= 0) {
            setCell(i + 1, j - 1, SquareType.RESERVED);
        }
        if (i - 1 >= 0 && j + 1 < FIELD_SIZE) {
            setCell(i - 1, j + 1, SquareType.RESERVED);
        }
    }


    private void setCell(int i, int j, SquareType cellType) {
        switch (cellType) {
            case SHIP -> {
                currentShip.addCell(new Square(i, j));
                setValueInGrid(i, j, SHIP);
            }
            case RESERVED -> {
                if (!getValueFromGrid(i, j).equals(SHIP)) {
                    setValueInGrid(i, j, RESERVED);
                }
            }
            case SHOT -> {
                String actualCellValue = getValueFromGrid(i, j);
                switch (actualCellValue) {
                    case SHIP -> {
                        setValueInGrid(i, j, HIT);
                        Square hitCell = new Square(i, j);
                        currentShip = getHittedShip(hitCell);
                        currentShip.destroyCell(hitCell);
                        numberOfSquaresOccupiedByShips--;
                    }
                    case HIT -> currentShip = getHittedShip(new Square(i, j));
                    case FOG, RESERVED -> setValueInGrid(i, j, MISS_SHOT);
                }
            }
        }
    }

    private Ship getHittedShip(Square square) {
        Ship currentShip = null;
        for (Ship ship : listOfShips) {
            if (ship.containsCell(square)) {
                currentShip = ship;
                break;
            }
        }
        return currentShip;
    }

    private boolean isCellNotOnTheField(Square cell) {
        return cell.getLineAsIndex() < 0
                || cell.getLineAsIndex() >= FIELD_SIZE
                || cell.getColumnAsIndex() < 0
                || cell.getColumnAsIndex() >= FIELD_SIZE;
    }

    private boolean isToCloseToOtherShip(Square frontCell, Square rearCell) {
        int frontLine = frontCell.getLineAsIndex();
        int rearLine = rearCell.getLineAsIndex();
        if (frontLine == rearLine) {
            return isAnyCellReserved(frontLine, frontCell.getColumnAsIndex(), rearCell.getColumnAsIndex(), true);
        } else {
            return isAnyCellReserved(frontCell.getColumnAsIndex(), frontLine, rearLine, false);
        }
    }

    private boolean isAnyCellReserved(int constantCoordinate, int dynamicFront, int dynamicRear, boolean sameLine) {
        int minDynamic = Math.min(dynamicFront, dynamicRear);
        int maxDynamic = Math.max(dynamicFront, dynamicRear);
        for (int i = minDynamic; i <= maxDynamic; i++) {
            if (sameLine) {
                if (isCellReserved(constantCoordinate, i)) {
                    return true;
                }
            } else {
                if (isCellReserved(i, constantCoordinate)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCellReserved(int i, int j) {
        return getValueFromGrid(i, j).equals(RESERVED) || getValueFromGrid(i, j).equals(SHIP);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < FIELD_SIZE; i++) {
            stringBuilder.append(Arrays.toString(grid[i])).append(Constants.FieldPrinterConstants.NEW_LINE);
        }
        return stringBuilder.toString();
    }
}
