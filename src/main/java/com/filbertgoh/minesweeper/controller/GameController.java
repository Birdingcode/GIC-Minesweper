package com.filbertgoh.minesweeper.controller;

import com.filbertgoh.minesweeper.model.Board;
import com.filbertgoh.minesweeper.service.BoardGenerator;

/**
 * Controller that manages the game logic and state.
 */
public class GameController {
    private Board board;
    private final BoardGenerator boardGenerator;
    private boolean firstMove;

    /**
     * Creates a new game controller.
     *
     * @param boardGenerator The board generator service
     */
    public GameController(BoardGenerator boardGenerator) {
        this.boardGenerator = boardGenerator;
        this.firstMove = true;
    }

    /**
     * Initializes a new game with the specified parameters.
     *
     * @param size     The board size
     * @param numMines The number of mines
     */
    public void initializeGame(int size, int numMines) {
        this.board = boardGenerator.generateBoard(size, numMines);
        this.firstMove = true;
    }

    /**
     * Gets the current game board.
     *
     * @return The game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Makes a move by revealing the cell at the specified position.
     *
     * @param row The row position
     * @param col The column position
     * @return true if the move hit a mine else false
     */
    public boolean makeMove(int row, int col) {
        // For the first move and hits mine, regenerate the board to ensure the first click is safe
        if (firstMove) {
            if (board.getCell(row,col).hasMine()){
                int size = board.getSize();
                int numMines = board.getTotalMines();
                this.board = boardGenerator.generateBoard(size, numMines, row, col);
            }

            firstMove = false;
        }

        return board.revealCell(row, col);
    }

    /**
     * Checks if the game is won.
     *
     * @return true if the game is won else false
     */
    public boolean isGameWon() {
        return board.isGameWon();
    }
}
