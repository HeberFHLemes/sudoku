package br.com.sudoku.service;

import br.com.sudoku.model.GameBoard;
import br.com.sudoku.model.GameStatusEnum;
import br.com.sudoku.model.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardService {
    private final static int BOARD_LIMIT = 9;

    private final GameBoard board;

    public BoardService(final Map<String, String> gameConfig) {
        this.board = new GameBoard(initBoard(gameConfig));
    }

    // Métodos semelhantes aos em GameBoard.java
    public List<List<Square>> getSquares(){
        return board.getSquares();
    }

    public void reset(){
        board.reset();
    }

    public boolean hasErrors(){
        return board.hasErrors();
    }

    public GameStatusEnum getStatus(){
        return board.getStatus();
    }

    public boolean gameFinished(){
        return board.gameFinished();
    }

    // Semelhantemente à startGame() de Main.java, utilizado para iniciar o tabuleiro com os parâmetros
    private List<List<Square>> initBoard(Map<String, String> gameConfig) {

        // Montar o tabuleiro
        List<List<Square>> squares = new ArrayList<>();

        for (int i = 0; i < BOARD_LIMIT; i++) {

            // Adiciona uma linha
            squares.add(new ArrayList<>());

            // Formar os quadrados de cada linha com os args passados no main
            for (int j = 0; j < BOARD_LIMIT; j++) {

                // Valor recebido do Map positions referente a valor esperado (expected) e booleano fixo (fixed)
                var positionConfig = gameConfig.get("%s,%s".formatted(i, j));

                // Valor esperado será o número antes da vírgula, como o 0 em "0,true".
                var expected = Integer.parseInt(positionConfig.split(",")[0]);

                // Booleano fixed será a palavra após a vírgula, como o true em "0,true".
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);

                // Instanciação do quadrado
                var current = new Square(expected, fixed);

                // Adicionar o quadrado à matriz
                squares.get(i).add(current);
            }
        }
        return squares;
    }
}
