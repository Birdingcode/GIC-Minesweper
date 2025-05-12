package com.filbertgoh.minesweeper.service;

import com.filbertgoh.minesweeper.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BoardPrinter class.
 */
public class BoardPrinterTest {

    @Test
    public void testPrintBoard() {
        Board board = new Board(2, 0);
        BoardPrinter printer = new BoardPrinter();

        String expectedInitial = """
                  1 2\s
                A _ _\s
                B _ _\s
                """;
        assertEquals(expectedInitial, printer.printBoard(board));

        // Reveal a cell with 1 adjacent mine
        board.getCell(0, 0).setAdjacentMines(1);
        board.getCell(0, 0).reveal();

        String expectedRevealNumber = """
                  1 2\s
                A 1 _\s
                B _ _\s
                """;
        assertEquals(expectedRevealNumber, printer.printBoard(board));

        // Reveal a cell with a mine
        board.getCell(1, 1).setMine(true);
        board.getCell(1, 1).reveal();

        String expectedRevealMine = """
                  1 2\s
                A 1 _\s
                B _ *\s
                """;
        assertEquals(expectedRevealMine, printer.printBoard(board));

        // Reveal a cell with 0 adjacent mines
        board.getCell(1,0).setAdjacentMines(0);
        board.getCell(1,0).reveal();

        String expectedRevealZero = """
                  1 2\s
                A 1 _\s
                B 0 *\s
                """;
        assertEquals(expectedRevealZero, printer.printBoard(board));
    }
}
