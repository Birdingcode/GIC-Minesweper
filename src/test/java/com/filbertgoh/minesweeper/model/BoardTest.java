package com.filbertgoh.minesweeper.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for the Board class.
 */
public class BoardTest {

    @Test
    public void testBoardInitialization() {
        int size = 4;
        int numMines = 5; // Assuming numMines is valid for a 4x4 board (max 35% = 5.6 -> 5)
        Board board = new Board(size, numMines);

        assertEquals(size, board.getSize());
        assertEquals(numMines, board.getTotalMines());
        assertEquals(0, board.getRevealedCount()); // Check initial revealedCount

        // Check that all cells are initialized properly
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell cell = board.getCell(row, col);
                assertNotNull(cell);
                assertEquals(row, cell.getRow());
                assertEquals(col, cell.getCol());
                assertFalse(cell.hasMine());
                assertEquals(0, cell.getAdjacentMines());
                assertEquals(CellState.COVERED, cell.getState());
            }
        }
    }

    @Test
    public void testIsValidPosition() {
        Board board = new Board(3, 2);

        assertTrue(board.isValidPosition(0, 0));
        assertTrue(board.isValidPosition(0, 2));
        assertTrue(board.isValidPosition(2, 0));
        assertTrue(board.isValidPosition(2, 2));

        assertFalse(board.isValidPosition(-1, 0));
        assertFalse(board.isValidPosition(0, -1));
        assertFalse(board.isValidPosition(3, 0));
        assertFalse(board.isValidPosition(0, 3));
    }

    @Test
    public void testGetCell() {
        Board board = new Board(3, 2);

        Cell cell = board.getCell(1, 2);
        assertNotNull(cell);
        assertEquals(1, cell.getRow());
        assertEquals(2, cell.getCol());

        // Test out of bounds
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCell(-1, 0);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCell(0, 3);
        });
    }

    @Test
    public void testGetAdjacentCells() {
        Board board = new Board(3, 1); // 1 mine, doesn't affect adjacency count

        // Middle cell should have 8 adjacent cells
        List<Cell> adjacentCells = board.getAdjacentCells(1, 1);
        assertEquals(8, adjacentCells.size());

        // Corner cell should have 3 adjacent cells
        adjacentCells = board.getAdjacentCells(0, 0);
        assertEquals(3, adjacentCells.size());

        // Edge cell should have 5 adjacent cells
        adjacentCells = board.getAdjacentCells(0, 1);
        assertEquals(5, adjacentCells.size());
    }

    @Test
    public void testCalculateAdjacentMines() {
        Board board = new Board(3, 0);

        board.getCell(0, 0).setMine(true);
        board.getCell(1, 1).setMine(true);

        board.calculateAdjacentMines();

        // x | 2 | 1
        // 2 | x | 1
        // 1 | 1 | 1

        assertEquals(0, board.getCell(0, 0).getAdjacentMines()); // Mine cell
        assertEquals(2, board.getCell(0, 1).getAdjacentMines()); // Adjacent to (0,0) and (1,1)
        assertEquals(1, board.getCell(0, 2).getAdjacentMines()); // Adjacent to (1,1)
        assertEquals(2, board.getCell(1, 0).getAdjacentMines()); // Adjacent to (0,0) and (1,1)
        assertEquals(0, board.getCell(1, 1).getAdjacentMines()); // Mine cell
        assertEquals(1, board.getCell(1, 2).getAdjacentMines()); // Adjacent to (0,0) diagonally and (1,1)
        assertEquals(1, board.getCell(2, 0).getAdjacentMines()); // Adjacent to (1,1)
        assertEquals(1, board.getCell(2, 1).getAdjacentMines()); // Adjacent to (1,1) and (0,0) diagonally
        assertEquals(1, board.getCell(2, 2).getAdjacentMines()); // Adjacent to (1,1)
    }

    @Test
    public void testRevealCell_NoMine() {
        Board board = new Board(3, 0);
        board.getCell(0, 0).setMine(true); // A mine elsewhere
        board.calculateAdjacentMines(); // Cell (1,1) will have 1 adjacent mine

        boolean hitMine = board.revealCell(1, 1);

        assertFalse(hitMine);
        assertTrue(board.getCell(1, 1).isRevealed());
        assertEquals(1, board.getRevealedCount());
    }

    @Test
    public void testRevealCell_HitMine() {
        Board board = new Board(3, 1); // One mine
        board.getCell(0, 0).setMine(true);
        board.calculateAdjacentMines();

        boolean hitMine = board.revealCell(0, 0);

        assertTrue(hitMine);
        assertTrue(board.getCell(0, 0).isRevealed());
        assertEquals(1, board.getRevealedCount());
    }

    @Test
    public void testRevealCell_AlreadyRevealed() {
        Board board = new Board(3, 1);
        board.getCell(0,0).setMine(true); // mine
        board.calculateAdjacentMines();

        board.revealCell(1, 1); // Reveal a non-mine cell
        assertTrue(board.getCell(1,1).isRevealed());
        assertEquals(1, board.getRevealedCount());

        boolean hitMineResult = board.revealCell(1, 1); // Try to reveal it again
        assertFalse(hitMineResult); // Should not indicate a mine was hit
        assertTrue(board.getCell(1,1).isRevealed()); // Still revealed
        assertEquals(1, board.getRevealedCount()); // Revealed count should not change
    }


    @Test
    public void testRevealCellCascade() {
        Board board = new Board(3, 0);
        // Place a mine such that (2,2) will have 0 adjacent mines
        board.getCell(0, 0).setMine(true);
        board.calculateAdjacentMines();

        // Cell (2,2) should have 0 adjacent mines because (0,0) is too far.
        assertEquals(0, board.getCell(2,2).getAdjacentMines());
        assertEquals(1, board.getCell(1,1).getAdjacentMines());

        board.revealCell(2, 2); // This should start a cascade

        assertFalse(board.getCell(0, 0).isRevealed()); // Mine
        assertTrue(board.getCell(0, 1).isRevealed());
        assertTrue(board.getCell(0, 2).isRevealed());
        assertTrue(board.getCell(1, 0).isRevealed());
        assertTrue(board.getCell(1, 1).isRevealed());
        assertTrue(board.getCell(1, 2).isRevealed());
        assertTrue(board.getCell(2, 0).isRevealed());
        assertTrue(board.getCell(2, 1).isRevealed());
        assertTrue(board.getCell(2, 2).isRevealed());
        assertEquals(8, board.getRevealedCount()); // All 8 non-mine cells
    }


    @Test
    public void testIsGameWon() {
        int boardSize = 3;
        int numMines = 2;
        Board board = new Board(boardSize, numMines);

        board.getCell(0, 0).setMine(true);
        board.getCell(2, 2).setMine(true);
        board.calculateAdjacentMines();

        assertFalse(board.isGameWon());

        // Reveal all non-mine cells
        // Total cells = 9. Mines = 2. Non-mines = 7.
        int nonMineCellsToReveal = (boardSize * boardSize) - numMines;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (!board.getCell(row, col).hasMine()) {
                    board.revealCell(row, col);
                }
            }
        }
        assertEquals(nonMineCellsToReveal, board.getRevealedCount());
        assertTrue(board.isGameWon());
    }

    @Test
    public void testIsGameWon_NotAllRevealed() {
        Board board = new Board(3, 1);
        board.getCell(0,0).setMine(true);

        board.revealCell(1,1); // Reveal one non-mine cell
        assertFalse(board.isGameWon());
    }
}
