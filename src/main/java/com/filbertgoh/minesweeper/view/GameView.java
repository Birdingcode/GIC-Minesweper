package com.filbertgoh.minesweeper.view;

import com.filbertgoh.minesweeper.controller.GameController;
import com.filbertgoh.minesweeper.model.Board;
import com.filbertgoh.minesweeper.service.BoardPrinter;
import com.filbertgoh.minesweeper.service.InputValidator;

import java.util.Scanner;

/**
 * Handles the console view and user interaction for the Minesweeper game.
 */
public class GameView {
    private final GameController gameController;
    private final BoardPrinter boardPrinter;
    private final InputValidator inputValidator;
    private final Scanner scanner;

    /**
     * Creates a new console game view.
     *
     * @param gameController The game controller
     * @param boardPrinter   The board printer service
     * @param inputValidator The input validator service
     */
    public GameView(GameController gameController, BoardPrinter boardPrinter, InputValidator inputValidator) {
        this.gameController = gameController;
        this.boardPrinter = boardPrinter;
        this.inputValidator = inputValidator;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game and handles user interaction.
     */
    public void startGame() {
        boolean playAgain = true;

        while (playAgain) {
            setupGame();
            playGame();
            playAgain = askToPlayAgain();
        }
    }

    /**
     * Sets up a new game by getting board parameters from the user.
     */
    private void setupGame() {
        System.out.println("Welcome to Minesweeper!\n");

        int size = getBoardSize();

        int mines = getNumberOfMines(size);

        gameController.initializeGame(size, mines);

        System.out.println();
        displayBoard();
    }

    /**
     * Gets the board size from the user.
     *
     * @return The validated board size
     */
    private int getBoardSize() {
        int size = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Enter the size of the grid (e.g. 4 for a 4x4 grid): ");
            String input = scanner.nextLine();

            try {
                size = inputValidator.validateBoardSize(input);
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return size;
    }

    /**
     * Gets the number of mines from the user.
     *
     * @param size The board size
     * @return The validated number of mines
     */
    private int getNumberOfMines(int size) {
        int mines = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Enter the number of mines to place on the grid (maximum is 35% of the total squares): ");
            String input = scanner.nextLine();

            try {
                mines = inputValidator.validateMineCount(input, size);
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return mines;
    }

    /**
     * Handles the main game loop.
     */
    private void playGame() {
        boolean gameOver = false;

        while (!gameOver) {
            int[] coordinates = getUserMove();
            int row = coordinates[0];
            int col = coordinates[1];

            boolean hitMine = gameController.makeMove(row, col);

            if (hitMine) {
                System.out.println("Oh no, you detonated a mine! Game over.");
                gameOver = true;
            } else {
                int adjacentMines = gameController.getBoard().getCell(row, col).getAdjacentMines();
                System.out.println("This square contains " + adjacentMines + " adjacent mines. ");
                System.out.println("\nHere is your updated minefield:");
                displayBoard();

                // Check if the game is won
                if (gameController.isGameWon()) {
                    System.out.println("Congratulations, you have won the game!");
                    gameOver = true;
                }
            }
        }
    }

    /**
     * Gets a move from the user.
     *
     * @return An array containing [row, col] coordinates
     */
    private int[] getUserMove() {
        int[] coordinates = null;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Select a square to reveal (e.g. A1): ");
            String input = scanner.nextLine();

            try {
                coordinates = inputValidator.validateCellCoordinate(input, gameController.getBoard().getSize());
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return coordinates;
    }

    /**
     * Displays the current state of the board.
     */
    private void displayBoard() {
        Board board = gameController.getBoard();
        String boardDisplay = boardPrinter.printBoard(board);
        System.out.println(boardDisplay);
    }

    /**
     * Asks the user if they want to play again.
     *
     * @return true to play again
     */
    private boolean askToPlayAgain() {
        System.out.println("Press any key to play again...");
        scanner.nextLine();
        return true;
    }
}
