package battleship.game;

import battleship.util.Constants;
import battleship.util.GameFieldAlteringResult;
import battleship.util.CellType;

import static battleship.util.Constants.FieldConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;

public class GameField {
    private final List<List<String>> field = new ArrayList<>();

    private int numberOfAliveShipCells = 0;

    public GameField() {
        populateInitialField();
    }

    private void populateInitialField() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            field.add(new ArrayList<>(Arrays.asList(FOG.repeat(FIELD_SIZE).split(""))));
        }
    }

    public String getCellData(GameCell cell) {
        if (cell.getLine() < 'A' || cell.getLine() > 'A' + FIELD_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong line identifier. Can be any character between 'A' and " + (char) ('A' + FIELD_SIZE));
        } else if (cell.getColumn() < 1 || cell.getColumn() > FIELD_SIZE) {
            throw new IllegalArgumentException(
                    "Wrong column identifier [" + cell.getColumn() + "]. Can be any integer between 1 and " +
                            FIELD_SIZE + " (inclusive)");
        }
        return field.get(cell.getLineIndex()).get(cell.getColumnIndex());
    }

    public GameFieldAlteringResult addShip(GameCell frontCell, GameCell rearCell) {
        if (isCellNotOnTheField(frontCell) || isCellNotOnTheField(rearCell)) {
            return GameFieldAlteringResult.OUT_OF_FIELD;
        } else if (isToCloseToOtherShip(frontCell, rearCell)) {
            return GameFieldAlteringResult.TOO_CLOSE;
        } else {
            setCells(frontCell, rearCell);
            return GameFieldAlteringResult.ALTERED;
        }
    }

    public GameFieldAlteringResult registerShoot(GameCell shotCoordinates) {
        if (isCellNotOnTheField(shotCoordinates)) {
            return GameFieldAlteringResult.OUT_OF_FIELD;
        } else {
            setCell(shotCoordinates, CellType.SHOT);
            return getCellData(shotCoordinates).equals(HIT)
                    ? GameFieldAlteringResult.HIT
                    : GameFieldAlteringResult.MISS;
        }
    }

    public void setCell(GameCell shotCoordinates, CellType cellType) {
        setCell(shotCoordinates.getLineIndex(), shotCoordinates.getColumnIndex(), cellType);
    }

    private void setCells(GameCell frontCell, GameCell rearCell) {
        int frontLine = frontCell.getLineIndex();
        int rearLine = rearCell.getLineIndex();
        if (frontLine == rearLine) {
            setShipCells(frontLine, frontCell.getColumnIndex(), rearCell.getColumnIndex(), true);
        } else {
            setShipCells(frontCell.getColumnIndex(), frontLine, rearLine, false);
        }
    }

    private void setShipCells(int constantCoordinate, int dynamicCoordinateStart, int dynamicCoordinateFinish,
                                      boolean sameLine) {
        int maxDynamic = Math.max(dynamicCoordinateStart, dynamicCoordinateFinish);
        int minDynamic = Math.min(dynamicCoordinateStart, dynamicCoordinateFinish);
        for (int i = minDynamic; i <= maxDynamic; i++) {
            if (sameLine) {
                setCell(constantCoordinate, i, CellType.SHIP);
                setReservedCells(constantCoordinate, i);
            } else {
                setCell(i, constantCoordinate, CellType.SHIP);
                setReservedCells(i, constantCoordinate);
            }

        }
    }

    private void setReservedCells(int i, int j) {
        if (i - 1 >= 0 && j - 1 >= 0) {
            setCell(i - 1, j - 1, CellType.RESERVED);
            setCell(i, j - 1, CellType.RESERVED);
            setCell(i - 1, j, CellType.RESERVED);
        } else if (j - 1 >= 0) {
            setCell(i, j -1, CellType.RESERVED);
        } else if (i - 1 >= 0) {
            setCell(i - 1, j, CellType.RESERVED);
        }

        if (i + 1 < FIELD_SIZE && j + 1 < FIELD_SIZE) {
            setCell(i + 1, j + 1, CellType.RESERVED);
            setCell(i + 1, j, CellType.RESERVED);
            setCell(i, j + 1, CellType.RESERVED);
        } else if (i + 1 < FIELD_SIZE) {
            setCell(i + 1, j, CellType.RESERVED);
        } else if (j + 1 < FIELD_SIZE) {
            setCell(i, j + 1, CellType.RESERVED);
        }

        if (i + 1  < FIELD_SIZE && j - 1 >= 0) {
            setCell(i + 1, j - 1, CellType.RESERVED);
        }
        if (i - 1 >= 0 && j + 1 < FIELD_SIZE) {
            setCell(i - 1, j + 1, CellType.RESERVED);
        }
    }


    private void setCell(int i, int j, CellType cellType) {
        switch (cellType) {
            case SHIP -> {
                field.get(i).set(j, SHIP);
                numberOfAliveShipCells ++;
            }
            case RESERVED -> {
                if (!field.get(i).get(j).equals(SHIP)) {
                    field.get(i).set(j, RESERVED);
                }
            }
            case SHOT -> {
                String actualCellValue = field.get(i).get(j);
                if (actualCellValue.equals(SHIP)) {
                    field.get(i).set(j, HIT);
                } else if (actualCellValue.equals(FOG) || actualCellValue.equals(RESERVED)) {
                    field.get(i).set(j, MISS_SHOT);
                }
            }
        }
    }

    private boolean isCellNotOnTheField(GameCell cell) {
        return cell.getLineIndex() < 0
                || cell.getLineIndex() >= FIELD_SIZE
                || cell.getColumnIndex() < 0
                || cell.getColumnIndex() >= FIELD_SIZE;
    }

    private boolean isToCloseToOtherShip(GameCell frontCell, GameCell rearCell) {
        int frontLine = frontCell.getLineIndex();
        int rearLine = rearCell.getLineIndex();
        if (frontLine == rearLine) {
            return isAnyCellReserved(frontLine, frontCell.getColumnIndex(), rearCell.getColumnIndex(), true);
        } else {
            return isAnyCellReserved(frontCell.getColumnIndex(), frontLine, rearLine, false);
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
        return field.get(i).get(j).equals(RESERVED) || field.get(i).get(j).equals(SHIP);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < FIELD_SIZE; i++) {
            stringBuilder.append(field.get(i).toString()).append(Constants.FieldPrinterConstants.NEW_LINE);
        }
        return stringBuilder.toString();
    }
}
