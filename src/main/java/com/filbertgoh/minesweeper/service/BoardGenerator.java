package com.filbertgoh.minesweeper.service;

import com.filbertgoh.minesweeper.model.Board;
import com.filbertgoh.minesweeper.model.Cell;
import com.filbertgoh.minesweeper.util.GameConfig;

import java.util.Random;

/**
 * Service for generating and initializing the game board.
 */
public class BoardGenerator {
    private final Random random;

    /**
     * Creates a new board generator.
     */
    public BoardGenerator() {
        this.random = new Random();
    }

    /**
     * Creates a new board generator with a specified random seed for testing.
     *
     * @param seed The random seed
     */
    public BoardGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Validates the input parameters for board creation.
     *
     * @param size     The board size
     * @param numMines The number of mines
     * @throws IllegalArgumentException if the parameters are invalid
     */
    public void validateBoardParameters(int size, int numMines) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be positive");
        }

        int maxMines = (int) (size * size * GameConfig.MAX_MINE_DENSITY_RATIO);
        if (numMines <= 0 || numMines > maxMines) {
            throw new IllegalArgumentException("Number of mines must be between 1 and " + maxMines);
        }
    }

    /**
     * Generates a new board with randomly placed mines.
     *
     * @param size     The board size
     * @param numMines The number of mines
     * @return The generated board
     */
    public Board generateBoard(int size, int numMines) {
        validateBoardParameters(size, numMines);
        Board board = new Board(size, numMines);
        placeMines(board, numMines, -1, -1);
        board.calculateAdjacentMines();
        return board;
    }

    /**
     * Generates a new board with randomly placed mines, excluding the first clicked position.
     *
     * @param size         The board size
     * @param numMines     The number of mines
     * @param excludeRow   The row to exclude from mine placement
     * @param excludeCol   The column to exclude from mine placement
     * @return The generated board
     */
    public Board generateBoard(int size, int numMines, int excludeRow, int excludeCol) {
        validateBoardParameters(size, numMines);
        Board board = new Board(size, numMines);
        placeMines(board, numMines, excludeRow, excludeCol);
        board.calculateAdjacentMines();
        return board;
    }

    /**
     * Places mines randomly on the board.
     *
     * @param board      The board to place mines on
     * @param numMines   The number of mines to place
     * @param excludeRow The row to exclude from mine placement
     * @param excludeCol The column to exclude from mine placement
     */
    private void placeMines(Board board, int numMines, int excludeRow, int excludeCol) {
        int size = board.getSize();
        int minesPlaced = 0;

        while (minesPlaced < numMines) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if ((row == excludeRow && col == excludeCol) || board.getCell(row, col).hasMine()) {
                continue;
            }

            board.getCell(row, col).setMine(true);
            minesPlaced++;
        }
    }
}
