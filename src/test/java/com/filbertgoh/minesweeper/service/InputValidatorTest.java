package com.filbertgoh.minesweeper.service;

import com.filbertgoh.minesweeper.util.GameConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the InputValidator class.
 */
public class InputValidatorTest {

    @Test
    public void testValidateBoardSize() {
        InputValidator validator = new InputValidator();

        // Valid input
        assertEquals(5, validator.validateBoardSize("5"));
        assertEquals(10, validator.validateBoardSize(" 10 "));
        assertEquals(2, validator.validateBoardSize("2"));

        // Invalid input
        assertThrows(IllegalArgumentException.class, () -> validator.validateBoardSize("1")); // Less than 2
        assertThrows(IllegalArgumentException.class, () -> validator.validateBoardSize("0"));
        assertThrows(IllegalArgumentException.class, () -> validator.validateBoardSize("-1"));
        assertThrows(IllegalArgumentException.class, () -> validator.validateBoardSize("abc"));
        assertThrows(IllegalArgumentException.class, () -> validator.validateBoardSize(""));
    }

    @Test
    public void testValidateMineCount() {
        InputValidator validator = new InputValidator();
        int boardSize = 5;
        // Max mines for 5x5 with 0.35 ratio is floor(25 * 0.35) = floor(8.75) = 8
        int calculatedMaxMines = (int) (boardSize * boardSize * GameConfig.MAX_MINE_DENSITY_RATIO);


        // Valid input
        assertEquals(1, validator.validateMineCount("1", boardSize));
        assertEquals(5, validator.validateMineCount(" 5 ", boardSize));
        assertEquals(calculatedMaxMines, validator.validateMineCount(String.valueOf(calculatedMaxMines), boardSize));

        // Invalid input
        assertThrows(IllegalArgumentException.class, () -> validator.validateMineCount("0", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateMineCount("-1", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateMineCount(String.valueOf(calculatedMaxMines + 1), boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateMineCount("abc", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateMineCount("", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateMineCount("!", boardSize));
    }

    @Test
    public void testValidateCellCoordinate() {
        InputValidator validator = new InputValidator();
        int boardSize = 5; // A-E, 1-5

        // Valid input
        assertArrayEquals(new int[]{0, 0}, validator.validateCellCoordinate("A1", boardSize));
        assertArrayEquals(new int[]{4, 4}, validator.validateCellCoordinate("E5", boardSize));
        assertArrayEquals(new int[]{2, 3}, validator.validateCellCoordinate(" C4 ", boardSize));
        assertArrayEquals(new int[]{0, 0}, validator.validateCellCoordinate("a1", boardSize));

        // Invalid input
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("!", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("A", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("1", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("F1", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("A6", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("A0", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("AA", boardSize));
        assertThrows(IllegalArgumentException.class, () -> validator.validateCellCoordinate("A 1", boardSize));
    }
}
