package battleship.game.grid;

import battleship.game.square.Square;
import battleship.game.ship.Ship;
import battleship.game.ship.ShipType;
import battleship.util.Constants;
import battleship.game.square.SquareType;

import static battleship.util.Constants.GridConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameGrid {
    private final Square[][] grid = new Square[GRID_SIZE][GRID_SIZE];
    private final List<Ship> listOfShips = new ArrayList<>();
    private Ship currentShip;
    private int numberOfSquaresOccupiedByShips = 0;

    public GameGrid() {
        populateInitialField();
    }

    private void populateInitialField() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new Square(i, j, FOG);
            }
        }
    }

    public String getSquareData(Square square) {
        if (square.getLine() < 'A' || square.getLine() > 'A' + GRID_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong line identifier. Can be any character between 'A' and " + (char) ('A' + GRID_SIZE));
        } else if (square.getColumn() < 1 || square.getColumn() > GRID_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong column identifier [" + square.getColumn() + "]. Can be any integer between 1 and " +
                            GRID_SIZE + " (inclusive)");
        }
        return getValueFromGrid(square.getLineAsIndex(), square.getColumnAsIndex());
    }

    private String getValueFromGrid(int i, int j) {
        return grid[i][j].getData();
    }

    private String getValueFromGrid(Square square) {
        return this.getValueFromGrid(square.getLineAsIndex(), square.getColumnAsIndex());
    }

    private void setValueInGrid(int i, int j, String value) {
        grid[i][j].setData(value);
    }

    private void setValueInGrid(Square square, String value) {
        this.setValueInGrid(square.getLineAsIndex(), square.getColumnAsIndex(), value);
    }

    private Square getSquareFromGrid(int i, int j) {
        return grid[i][j];
    }

    public GridModificationResult addShip(Square frontSquare, Square rearSquare, ShipType shipType) {
        if (isSquareNotOnTheGrid(frontSquare) || isSquareNotOnTheGrid(rearSquare)) {
            return GridModificationResult.SHIP_OUT_OF_GRID;
        } else if (isShipToCloseToOther(frontSquare, rearSquare)) {
            return GridModificationResult.SHIPS_TO_CLOSE;
        } else {
            registerShipOnGrid(shipType);
            placeShip(frontSquare, rearSquare);
            return GridModificationResult.SHIP_PLACED;
        }
    }

    private void registerShipOnGrid(ShipType shipType) {
        listOfShips.add(new Ship(shipType));
        currentShip = listOfShips.get(listOfShips.size() - 1);
        numberOfSquaresOccupiedByShips += shipType.getSize();
    }

    private void placeShip(Square front, Square rear) {
        List<Square> squaresToAlter = new ArrayList<>();
        getShipSquares(squaresToAlter, front, rear);
        setShipSquares(squaresToAlter);
    }

    private void getShipSquares(List<Square> listOfSquares, Square front, Square rear) {
        switch (front.getDirectionTo(rear)) {
            case LEFT -> getShipSquaresSameLine(listOfSquares, rear.getColumnAsIndex(), front.getColumnAsIndex(),
                    front.getLineAsIndex());
            case RIGHT -> getShipSquaresSameLine(listOfSquares, front.getColumnAsIndex(), rear.getColumnAsIndex(),
                    front.getLineAsIndex());
            case UP -> getShipSquaresSameColumn(listOfSquares, rear.getLineAsIndex(), front.getLineAsIndex(),
                    front.getColumnAsIndex());
            case DOWN -> getShipSquaresSameColumn(listOfSquares, front.getLineAsIndex(), rear.getLineAsIndex(),
                    front.getColumnAsIndex());
        }
    }

    private void getShipSquaresSameLine(List<Square> listOfSquares, int startColumn, int endColumn, int line) {
        for (int i = startColumn; i <= endColumn; i++) {
            listOfSquares.add(getSquareFromGrid(line, i));
        }
    }

    private void getShipSquaresSameColumn(List<Square> listOfSquares, int startLine, int endLine, int column) {
        for (int i = startLine; i <= endLine; i++) {
            listOfSquares.add(getSquareFromGrid(i, column));
        }
    }

    private void setShipSquares(List<Square> listOfSquares) {
        for (Square square : listOfSquares) {
            setSquare(square, SquareType.SHIP);
            setReservedCells(square);
        }
    }

    private void setReservedCells(Square square) {
        int i = square.getLineAsIndex();
        int j = square.getColumnAsIndex();
        if (i - 1 >= 0 && j - 1 >= 0) {
            setSquare(getSquareFromGrid(i - 1, j - 1), SquareType.RESERVED);
            setSquare(getSquareFromGrid(i, j - 1), SquareType.RESERVED);
            setSquare(getSquareFromGrid(i - 1, j), SquareType.RESERVED);
        } else if (j - 1 >= 0) {
            setSquare(getSquareFromGrid(i, j -1), SquareType.RESERVED);
        } else if (i - 1 >= 0) {
            setSquare(getSquareFromGrid(i - 1, j), SquareType.RESERVED);
        }

        if (i + 1 < GRID_SIZE && j + 1 < GRID_SIZE) {
            setSquare(getSquareFromGrid(i + 1, j + 1), SquareType.RESERVED);
            setSquare(getSquareFromGrid(i + 1, j), SquareType.RESERVED);
            setSquare(getSquareFromGrid(i, j + 1), SquareType.RESERVED);
        } else if (i + 1 < GRID_SIZE) {
            setSquare(getSquareFromGrid(i + 1, j), SquareType.RESERVED);
        } else if (j + 1 < GRID_SIZE) {
            setSquare(getSquareFromGrid(i, j + 1), SquareType.RESERVED);
        }

        if (i + 1  < GRID_SIZE && j - 1 >= 0) {
            setSquare(getSquareFromGrid(i + 1, j - 1), SquareType.RESERVED);
        }
        if (i - 1 >= 0 && j + 1 < GRID_SIZE) {
            setSquare(getSquareFromGrid(i - 1, j + 1), SquareType.RESERVED);
        }
    }

    private void setSquare(Square square, SquareType squareType) {
        switch (squareType) {
            case SHIP -> {
                currentShip.addSquare(square);
                setValueInGrid(square, SHIP);
            }
            case RESERVED -> {
                if (!getValueFromGrid(square).equals(SHIP)) {
                    setValueInGrid(square, RESERVED);
                }
            }
            case SHOT -> {
                String squareValue = getValueFromGrid(square);
                switch (squareValue) {
                    case SHIP -> {
                        setValueInGrid(square, HIT);
                        currentShip = getHittedShip(square);
                        currentShip.destroySquare(square);
                        numberOfSquaresOccupiedByShips--;
                    }
                    case HIT -> currentShip = getHittedShip(square);
                    case FOG, RESERVED -> setValueInGrid(square, MISS_SHOT);
                }
            }
        }
    }

    public GridModificationResult registerShoot(Square shotCoordinates) {
        if (isSquareNotOnTheGrid(shotCoordinates)) {
            return GridModificationResult.SHIP_OUT_OF_GRID;
        } else {
            setSquare(shotCoordinates, SquareType.SHOT);
            if (getSquareData(shotCoordinates).equals(HIT)) {
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

    private Ship getHittedShip(Square square) {
        Ship currentShip = null;
        for (Ship ship : listOfShips) {
            if (ship.isOnSquare(square)) {
                currentShip = ship;
                break;
            }
        }
        return currentShip;
    }

    private boolean isSquareNotOnTheGrid(Square square) {
        return square.getLineAsIndex() < 0
                || square.getLineAsIndex() >= GRID_SIZE
                || square.getColumnAsIndex() < 0
                || square.getColumnAsIndex() >= GRID_SIZE;
    }

    private boolean isShipToCloseToOther(Square frontSquare, Square rearSquare) {
        List<Square> listOfShipsSquares = new ArrayList<>();
        getShipSquares(listOfShipsSquares, frontSquare, rearSquare);
        for (Square square : listOfShipsSquares) {
            if (isSquareReserved(square)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSquareReserved(Square square) {
        return getValueFromGrid(square).equals(RESERVED) || getValueFromGrid(square).equals(SHIP);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < GRID_SIZE; i++) {
            stringBuilder.append(Arrays.toString(grid[i])).append(Constants.GridPrinterConstants.NEW_LINE);
        }
        return stringBuilder.toString();
    }
}
