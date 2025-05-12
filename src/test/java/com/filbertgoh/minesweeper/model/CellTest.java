package com.filbertgoh.minesweeper.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Cell class.
 */
public class CellTest {

    @Test
    public void testCellInitialization() {
        Cell cell = new Cell(1, 2);

        assertEquals(1, cell.getRow());
        assertEquals(2, cell.getCol());
        assertFalse(cell.hasMine());
        assertEquals(0, cell.getAdjacentMines());
        assertEquals(CellState.COVERED, cell.getState());
        assertTrue(cell.isCovered());
        assertFalse(cell.isRevealed());
    }

    @Test
    public void testSetMine() {
        Cell cell = new Cell(0, 0);

        assertFalse(cell.hasMine());

        cell.setMine(true);
        assertTrue(cell.hasMine());

        cell.setMine(false);
        assertFalse(cell.hasMine());
    }

    @Test
    public void testSetAdjacentMines() {
        Cell cell = new Cell(0, 0);

        assertEquals(0, cell.getAdjacentMines());

        cell.setAdjacentMines(3);
        assertEquals(3, cell.getAdjacentMines());
    }

    @Test
    public void testReveal() {
        Cell cell = new Cell(0, 0);

        assertTrue(cell.isCovered());
        assertFalse(cell.isRevealed());

        cell.reveal();

        assertFalse(cell.isCovered());
        assertTrue(cell.isRevealed());
        assertEquals(CellState.REVEALED, cell.getState());
    }


    @Test
    public void testSetState() {
        Cell cell = new Cell(0, 0);

        assertEquals(CellState.COVERED, cell.getState());

        cell.setState(CellState.REVEALED);
        assertEquals(CellState.REVEALED, cell.getState());

        cell.setState(CellState.COVERED);
        assertEquals(CellState.COVERED, cell.getState());
    }
}
