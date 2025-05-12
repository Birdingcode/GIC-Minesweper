package com.filbertgoh.minesweeper.controller;

import com.filbertgoh.minesweeper.model.Board;
import com.filbertgoh.minesweeper.model.Cell;
import com.filbertgoh.minesweeper.service.BoardGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the GameController class.
 */
public class GameControllerTest {

    @Mock
    private BoardGenerator mockBoardGenerator;

    @Mock
    private Board mockBoard;

    @Mock
    private Cell mockCell;

    @InjectMocks
    private GameController gameController;

    private final int defaultSize = 5;
    private final int defaultNumMines = 3;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        // Default behavior for board generation during initializeGame
        when(mockBoardGenerator.generateBoard(defaultSize, defaultNumMines)).thenReturn(mockBoard);
        // Default behavior for board interactions
        when(mockBoard.getSize()).thenReturn(defaultSize);
        when(mockBoard.getTotalMines()).thenReturn(defaultNumMines);
        when(mockBoard.getCell(anyInt(), anyInt())).thenReturn(mockCell);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void testInitializeGame() {
        gameController.initializeGame(defaultSize, defaultNumMines);

        verify(mockBoardGenerator, times(1)).generateBoard(defaultSize, defaultNumMines);
        assertEquals(mockBoard, gameController.getBoard());
    }

    @Test
    public void testMakeMoveFirstMove_HitsMine_ShouldRegenerateBoard() {
        gameController.initializeGame(defaultSize, defaultNumMines);

        int row = 2;
        int col = 3;

        // Setup: First click on the initial mockBoard IS a mine
        when(mockBoard.getCell(row, col)).thenReturn(mockCell);
        when(mockCell.hasMine()).thenReturn(true);

        // boardGenerator returns a new mock board instance
        Board regeneratedMockBoard = mock(Board.class);
        when(mockBoardGenerator.generateBoard(defaultSize, defaultNumMines, row, col)).thenReturn(regeneratedMockBoard);
        // Assume the regenerated board doesn't hit a mine on reveal
        when(regeneratedMockBoard.revealCell(row,col)).thenReturn(false);


        boolean hitMineResult = gameController.makeMove(row, col);

        // BoardGenerator called to regenerate the board
        verify(mockBoardGenerator, times(1)).generateBoard(defaultSize, defaultNumMines, row, col);
        // Game controller uses the regenerated board
        assertEquals(regeneratedMockBoard, gameController.getBoard());
        // revealCell was called on the regenerated board
        verify(regeneratedMockBoard, times(1)).revealCell(row, col);
        // revealCell NOT called on initial mockBoard as it was replaced
        verify(mockBoard, never()).revealCell(row,col);


        assertFalse(hitMineResult);
    }

    @Test
    public void testMakeMoveFirstMove_SafeClick_ShouldNotRegenerateBoard() {
        gameController.initializeGame(defaultSize, defaultNumMines);

        int row = 2;
        int col = 3;

        // Setup: First click on the initial mockBoard is NOT a mine
        when(mockBoard.getCell(row, col)).thenReturn(mockCell);
        when(mockCell.hasMine()).thenReturn(false);
        when(mockBoard.revealCell(row, col)).thenReturn(false);

        boolean hitMineResult = gameController.makeMove(row, col);

        // BoardGenerator NOT called to regenerate
        verify(mockBoardGenerator, never()).generateBoard(defaultSize, defaultNumMines, row, col);
        // Game controller uses initial board
        assertEquals(mockBoard, gameController.getBoard());
        // revealCell called on initial board
        verify(mockBoard, times(1)).revealCell(row, col);

        assertFalse(hitMineResult);
    }


    @Test
    public void testMakeMoveSubsequentMove_ShouldNotRegenerate() {
        gameController.initializeGame(defaultSize, defaultNumMines);

        int firstRow = 0, firstCol = 0;
        int secondRow = 1, secondCol = 1;

        when(mockBoard.getCell(firstRow, firstCol)).thenReturn(mockCell);
        when(mockCell.hasMine()).thenReturn(false);
        when(mockBoard.revealCell(firstRow, firstCol)).thenReturn(false);
        gameController.makeMove(firstRow, firstCol);

        // Reset interaction count for generateBoard to ensure only count for second move
        clearInvocations(mockBoardGenerator);

        // Setup for second move
        Cell mockCell2 = mock(Cell.class); // Potentially different cell for second move
        when(mockBoard.getCell(secondRow, secondCol)).thenReturn(mockCell2);
        when(mockBoard.revealCell(secondRow, secondCol)).thenReturn(false); // Assume safe

        // Make the second move
        boolean hitMineResult = gameController.makeMove(secondRow, secondCol);

        // BoardGenerator NOT called for regeneration
        verify(mockBoardGenerator, never()).generateBoard(anyInt(), anyInt());
        verify(mockBoardGenerator, never()).generateBoard(anyInt(), anyInt(), anyInt(), anyInt());

        // revealCell called for second move on the board
        verify(mockBoard, times(1)).revealCell(secondRow, secondCol);
        assertFalse(hitMineResult);
    }

    @Test
    public void testMakeMove_HitsMineOnSubsequentMove() {
        gameController.initializeGame(defaultSize, defaultNumMines);

        when(mockBoard.getCell(0, 0)).thenReturn(mockCell);
        when(mockCell.hasMine()).thenReturn(false);
        when(mockBoard.revealCell(0,0)).thenReturn(false);
        gameController.makeMove(0, 0);


        // Setup for second move that hits a mine
        int mineRow = 1, mineCol = 1;
        Cell mineCell = mock(Cell.class);
        when(mockBoard.getCell(mineRow, mineCol)).thenReturn(mineCell);
        when(mockBoard.revealCell(mineRow, mineCol)).thenReturn(true); // hits mine

        boolean hitMineResult = gameController.makeMove(mineRow, mineCol);

        assertTrue(hitMineResult);
        // revealCell was called
        verify(mockBoard, times(1)).revealCell(mineRow, mineCol);
    }

    @Test
    public void testIsGameWon() {
        gameController.initializeGame(defaultSize, defaultNumMines);

        when(mockBoard.isGameWon()).thenReturn(true);
        assertTrue(gameController.isGameWon());
        verify(mockBoard, times(1)).isGameWon();

        when(mockBoard.isGameWon()).thenReturn(false);
        assertFalse(gameController.isGameWon());
        verify(mockBoard, times(2)).isGameWon();
    }
}
