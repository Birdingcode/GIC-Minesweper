package com.filbertgoh.minesweeper.service;

import com.filbertgoh.minesweeper.util.GameConfig;

/**
 * Service for validating user input.
 */
public class InputValidator {

    /**
     * Validates the board size input.
     *
     * @param sizeStr The size input string
     * @return The parsed board size
     * @throws IllegalArgumentException if the input is invalid
     */
    public int validateBoardSize(String sizeStr) {
        try {
            int size = Integer.parseInt(sizeStr.trim());
            if (size < 2) {
                throw new IllegalArgumentException("Board size must be positive and or greater than 1");
            }
            return size;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid board size: must be a positive integer");
        }
    }

    /**
     * Validates the number of mines input.
     *
     * @param minesStr The mines input string
     * @param boardSize The board size
     * @return The parsed number of mines
     * @throws IllegalArgumentException if the input is invalid
     */
    public int validateMineCount(String minesStr, int boardSize) {
        try {
            int mines = Integer.parseInt(minesStr.trim());
            int maxMines = (int) (boardSize * boardSize * GameConfig.MAX_MINE_DENSITY_RATIO);

            if (mines <= 0 || mines > maxMines) {
                throw new IllegalArgumentException("Number of mines must be between 1 and " + maxMines);
            }

            return mines;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid mine count: must be a positive integer");
        }
    }

    /**
     * Validates and parses a cell coordinate input.
     *
     * @param input The cell coordinate input (e.g., "A1")
     * @param boardSize The board size
     * @return An array containing [row, col] indices
     * @throws IllegalArgumentException if the input is invalid
     */
    public int[] validateCellCoordinate(String input, int boardSize) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Cell coordinate cannot be empty");
        }

        input = input.trim().toUpperCase();
        if (input.length() < 2) {
            throw new IllegalArgumentException("Invalid cell coordinate format. Example: A1");
        }

        char rowChar = input.charAt(0);
        String colStr = input.substring(1);

        // Parse row (letter)
        int row = rowChar - 'A';
        if (row < 0 || row >= boardSize) {
            throw new IllegalArgumentException("Row must be between A and " + (char) ('A' + boardSize - 1));
        }

        // Parse column (number)
        try {
            int col = Integer.parseInt(colStr) - 1;
            if (col < 0 || col >= boardSize) {
                throw new IllegalArgumentException("Column must be between 1 and " + boardSize);
            }

            return new int[] {row, col};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid column: must be a number between 1 and " + boardSize);
        }
    }
}
