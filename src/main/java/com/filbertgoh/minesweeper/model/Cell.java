package com.filbertgoh.minesweeper.model;

/**
 * Represents a single cell in the Minesweeper game board.
 */
public class Cell {
    private final int row;
    private final int col;
    private boolean hasMine;
    private int adjacentMines;
    private CellState state;

    /**
     * Creates a new cell at the specified position.
     *
     * @param row The row position of the cell
     * @param col The column position of the cell
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.hasMine = false;
        this.adjacentMines = 0;
        this.state = CellState.COVERED;
    }

    /**
     * Gets the row position of this cell.
     *
     * @return The row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position of this cell.
     *
     * @return The column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Checks if this cell contains a mine.
     *
     * @return true if the cell contains a mine else false
     */
    public boolean hasMine() {
        return hasMine;
    }

    /**
     * Sets whether this cell contains a mine.
     *
     * @param hasMine true to place a mine on this cell, false to remove it
     */
    public void setMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    /**
     * Gets the number of adjacent mines around this cell.
     *
     * @return The number of adjacent mines
     */
    public int getAdjacentMines() {
        return adjacentMines;
    }

    /**
     * Sets the number of adjacent mines around this cell.
     *
     * @param adjacentMines The number of adjacent mines
     */
    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    /**
     * Gets the current state of this cell.
     *
     * @return The cell state
     */
    public CellState getState() {
        return state;
    }

    /**
     * Sets the state of this cell.
     *
     * @param state The new state
     */
    public void setState(CellState state) {
        this.state = state;
    }

    /**
     * Checks if this cell is revealed.
     *
     * @return true if the cell is revealed else false
     */
    public boolean isRevealed() {
        return state == CellState.REVEALED;
    }

    /**
     * Checks if this cell is covered (not revealed).
     *
     * @return true if the cell is covered else false
     */
    public boolean isCovered() {
        return state == CellState.COVERED;
    }

    /**
     * Reveals this cell.
     */
    public void reveal() {
        this.state = CellState.REVEALED;
    }

}
