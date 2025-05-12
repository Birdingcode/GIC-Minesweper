package com.filbertgoh.minesweeper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board in Minesweeper.
 */
public class Board {
    private final int size;
    private final Cell[][] cells;
    private final int totalMines;
    private int revealedCount;

    /**
     * Creates a new board with the specified size and number of mines.
     *
     * @param size      The size of the board
     * @param numMines  The number of mines to place on the board
     */
    public Board(int size, int numMines) {
        this.size = size;
        this.totalMines = numMines;
        this.cells = new Cell[size][size];
        this.revealedCount = 0;

        initializeCells();
    }

    /**
     * Initializes all cells on the board.
     */
    private void initializeCells() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /*
    * Gets the revealed count of the board.
    *
    * @return The revealed count
    *
    */
    public int getRevealedCount() {
        return revealedCount;
    }

    /**
     * Gets the size of the board.
     *
     * @return The board size
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the total number of mines on the board.
     *
     * @return The total number of mines
     */
    public int getTotalMines() {
        return totalMines;
    }

    /**
     * Gets the cell at the specified position.
     *
     * @param row The row position
     * @param col The column position
     * @return The cell at the specified position
     * @throws IndexOutOfBoundsException if the position is outside the board
     */
    public Cell getCell(int row, int col) {
        validateCoordinates(row, col);
        return cells[row][col];
    }

    /**
     * Checks if the specified position is within the board boundaries.
     *
     * @param row The row position to check
     * @param col The column position to check
     * @return true if the position is valid else false
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Validates that the specified coordinates are within the board boundaries.
     *
     * @param row The row position to validate
     * @param col The column position to validate
     * @throws IndexOutOfBoundsException if the position is outside the board
     */
    private void validateCoordinates(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IndexOutOfBoundsException("Position (" + row + ", " + col + ") is outside the board boundaries");
        }
    }

    /**
     * Gets all adjacent cells to the specified position.
     *
     * @param row The row position
     * @param col The column position
     * @return A list of adjacent cells
     */
    public List<Cell> getAdjacentCells(int row, int col) {
        validateCoordinates(row, col);
        List<Cell> adjacentCells = new ArrayList<>();

        for (int r = Math.max(0, row - 1); r <= Math.min(size - 1, row + 1); r++) {
            for (int c = Math.max(0, col - 1); c <= Math.min(size - 1, col + 1); c++) {
                if (r != row || c != col) {
                    adjacentCells.add(cells[r][c]);
                }
            }
        }

        return adjacentCells;
    }

    /**
     * Calculates adjacent mines of each cell and adds that figure to it
     *
     */
    public void calculateAdjacentMines() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (cells[row][col].hasMine()) {
                    continue;
                }

                int mineCount = 0;
                for (Cell adjacentCell : getAdjacentCells(row, col)) {
                    if (adjacentCell.hasMine()) {
                        mineCount++;
                    }
                }
                cells[row][col].setAdjacentMines(mineCount);
            }
        }
    }

    /**
     * Reveals the cell at the specified position.
     *
     * @param row The row position
     * @param col The column position
     * @return true if the revealed cell has a mine else false
     */
    public boolean revealCell(int row, int col) {
        Cell cell = getCell(row, col);

        if (cell.isRevealed()) {
            return false;
        }

        cell.reveal();
        revealedCount++;

        if (cell.hasMine()) {
            return true;
        }

        // If cell has no adjacent mines, reveal all adjacent cells
        if (cell.getAdjacentMines() == 0) {
            for (Cell adjacentCell : getAdjacentCells(row, col)) {
                if (adjacentCell.isCovered()) {
                    revealCell(adjacentCell.getRow(), adjacentCell.getCol());
                }
            }
        }

        return false;
    }

    /**
     * Checks if the game is won.
     *
     * @return true if all non-mine cells are revealed else false
     */
    public boolean isGameWon() {
        return revealedCount == (size * size - totalMines);
    }
}
