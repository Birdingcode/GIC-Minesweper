package com.filbertgoh.minesweeper.service;

import com.filbertgoh.minesweeper.model.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BoardGenerator class.
 */
public class BoardGeneratorTest {

    @Test
    public void testValidateBoardParameters() {
        BoardGenerator generator = new BoardGenerator();

        // 5*5 = 25. 25 * 0.35 = 8.75. So max 8 mines.
        assertDoesNotThrow(() -> generator.validateBoardParameters(5, 5));
        assertDoesNotThrow(() -> generator.validateBoardParameters(5, 8));


        // Invalid size
        assertThrows(IllegalArgumentException.class, () -> generator.validateBoardParameters(0, 5));
        assertThrows(IllegalArgumentException.class, () -> generator.validateBoardParameters(1, 5));

        // Invalid number of mines
        assertThrows(IllegalArgumentException.class, () -> generator.validateBoardParameters(5, 0));
        assertThrows(IllegalArgumentException.class, () -> generator.validateBoardParameters(5, -1));

        // Too many mines (more than 35% of total squares)
        // For a 5x5 board, max mines should be 8 (floor of 25 * 0.35 = 8.75)
        assertThrows(IllegalArgumentException.class, () -> generator.validateBoardParameters(5, 9));
    }

    @Test
    public void testGenerateBoard() {
        BoardGenerator generator = new BoardGenerator(123L); // Fixed seed

        int size = 5;
        int numMines = 5;

        Board board = generator.generateBoard(size, numMines);

        assertEquals(size, board.getSize());
        assertEquals(numMines, board.getTotalMines());

        // Count the number of mines
        int mineCount = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board.getCell(row, col).hasMine()) {
                    mineCount++;
                }
            }
        }
        assertEquals(numMines, mineCount);
    }

    @Test
    public void testGenerateBoardWithExcludedPosition() {
        BoardGenerator generator = new BoardGenerator(123L); // Fixed seed

        int size = 5;
        int numMines = 5;
        int excludeRow = 2;
        int excludeCol = 2;

        Board board = generator.generateBoard(size, numMines, excludeRow, excludeCol);

        assertEquals(size, board.getSize());
        assertEquals(numMines, board.getTotalMines());

        // Check that the excluded position doesn't have a mine
        assertFalse(board.getCell(excludeRow, excludeCol).hasMine());

        int mineCount = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board.getCell(row, col).hasMine()) {
                    mineCount++;
                }
            }
        }
        assertEquals(numMines, mineCount);
    }
}
