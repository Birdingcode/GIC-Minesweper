package com.filbertgoh.minesweeper.service;

import com.filbertgoh.minesweeper.model.Board;
import com.filbertgoh.minesweeper.model.Cell;
import com.filbertgoh.minesweeper.model.CellState;

/**
 * Service for printing the board to the console.
 */
public class BoardPrinter {

    /**
     * Prints the current state of the board to the console.
     *
     * @param board The board to print
     */
    public String printBoard(Board board) {
        StringBuilder output = new StringBuilder();
        int size = board.getSize();

        // Print column headers
        output.append("  ");
        for (int col = 1; col <= size; col++) {
            output.append(col).append(" ");
        }
        output.append("\n");

        // Print rows
        for (int row = 0; row < size; row++) {
            // Print row header (A, B, C, ...)
            output.append((char) ('A' + row)).append(" ");

            // Print cells
            for (int col = 0; col < size; col++) {
                output.append(getCellDisplay(board.getCell(row, col))).append(" ");
            }
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * Determines the display character for a cell based on its state.
     *
     * @param cell The cell to get the display for
     * @return The character to display for the cell
     */
    private String getCellDisplay(Cell cell) {
        if (cell.getState() == CellState.COVERED) {
            return "_";
        } else if (cell.isRevealed()) {
            if (cell.hasMine()) {
                return "*";
            } else {
                return String.valueOf(cell.getAdjacentMines());
            }
        }

        return "?"; // Unexpected state
    }
}
