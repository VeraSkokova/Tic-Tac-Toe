package ru.nsu.ccfit.skokova.tic_tac_toe.model.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Cell;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.CellState;
import ru.nsu.ccfit.skokova.tic_tac_toe.model.field.Field;

public class ComputerStrategy implements PlayerStrategy {
    private final List<int[]> neighbourCoefficients;

    private List<Cell> previousSteps;

    public ComputerStrategy() {
        previousSteps = new ArrayList<>();

        neighbourCoefficients = getNeighbourCoefficients(1);
    }

    @Override
    public Cell nextStep(Field field, Cell lastUserStepCell) {
        previousSteps.add(lastUserStepCell);
        int maxProfit = -1;
        Cell maxProfitCell = null;
        Set<Cell> stepsNeighbours = getStepsNeighbours(field);
        for (Cell cell : stepsNeighbours) {
            int currentProfit = profit(field, cell);
            if (currentProfit > maxProfit) {
                maxProfit = currentProfit;
                maxProfitCell = cell;
            }
        }
        maxProfitCell.setCellState(CellState.NOUGHT);
        previousSteps.add(maxProfitCell);
        return maxProfitCell; //value will always != null, because stepsNeighbours isn't empty
    }

    private int profit(Field field, Cell cell) {
        int profit = 0;

        int rowNumber = cell.getCellY();
        profit += getPartialProfit(field, field.getRow(rowNumber));

        int columnNumber = cell.getCellX();
        profit += getPartialProfit(field, field.getColumn(columnNumber));

        if (rowNumber == columnNumber) { //cell is on main diagonal
            profit += getPartialProfit(field, field.getMainDiagonal());
        }

        if (field.getSize() - rowNumber == columnNumber + 1) { //cell is on indirect diagonal
            profit += getPartialProfit(field, field.getIndirectDiagonal());
        }

        return profit;
    }

    private int getPartialProfit(Field field, List<Cell> cells) {//TODO : maybe computer will use Crosses
        int crossesCount = 0;
        int noughtsCount = 0;

        for (Cell cell : cells) {
            switch (cell.getCellState()) {
                case CROSS:
                    crossesCount++;
                    break;
                case NOUGHT:
                    noughtsCount++;
                    break;
                case UNDEFINED:
                    break;
                default:
                    break;
            }
        }
        if (noughtsCount == field.getSize() - 1) {
            return 10000;
        }
        if (crossesCount == field.getSize() - 1) {
            return 1000;
        }
        if (crossesCount > 0) {
            return 0;
        }
        return noughtsCount;
    }

    private Set<Cell> getStepsNeighbours(Field field) {
        Set<Cell> stepsNeighbours = new HashSet<>();
        for (Cell cell : previousSteps) {
            addCellNeighbours(field, stepsNeighbours, cell);
        }
        return stepsNeighbours;
    }

    private void addCellNeighbours(Field field, Set<Cell> stepsNeighbours, Cell cell) {
        for (int[] neighbourCoefficient : neighbourCoefficients) {
            final int neighbourCellX = cell.getCellX() + neighbourCoefficient[0];
            final int neighbourCellY = cell.getCellY() + neighbourCoefficient[1];
            addCellIfPossible(field, stepsNeighbours, neighbourCellX, neighbourCellY);
        }
    }

    private void addCellIfPossible(Field field, Set<Cell> stepsNeighbours,
                                   int neighbourCellX, int neighbourCellY) {
        if (field.isCellInField(neighbourCellX, neighbourCellY) &&
                field.getCell(neighbourCellX, neighbourCellY).getCellState() == CellState.UNDEFINED) {
            stepsNeighbours.add(field.getCell(neighbourCellX, neighbourCellY));
        }
    }

    private List<int[]> getNeighbourCoefficients(int n) {
        List<int[]> neighboursCoefficients = new ArrayList<>();
        for (int i = -n; i < n + 1; i++) {
            for (int j = -n; j < n + 1; j++) {
                neighboursCoefficients.add(new int[]{i, j});
            }
        }
        neighboursCoefficients.remove(getUselessIndex(n)); //{0, 0} pair will be in list at that index
        return neighboursCoefficients;
    }

    private int getUselessIndex(int n) {
        final int neighbourhoodSize = 2 * n + 1;
        return (neighbourhoodSize * neighbourhoodSize - 1) / 2;
    }
}
