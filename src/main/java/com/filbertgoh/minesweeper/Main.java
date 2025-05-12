package com.filbertgoh.minesweeper;

import com.filbertgoh.minesweeper.controller.GameController;
import com.filbertgoh.minesweeper.service.BoardGenerator;
import com.filbertgoh.minesweeper.service.BoardPrinter;
import com.filbertgoh.minesweeper.service.InputValidator;
import com.filbertgoh.minesweeper.view.GameView;

public class Main {
    public static void main(String[] args) {
        BoardGenerator boardGenerator = new BoardGenerator();
        BoardPrinter boardPrinter = new BoardPrinter();
        InputValidator inputValidator = new InputValidator();

        GameController gameController = new GameController(boardGenerator);

        GameView gameView = new GameView(gameController, boardPrinter, inputValidator);

        gameView.startGame();
    }
}