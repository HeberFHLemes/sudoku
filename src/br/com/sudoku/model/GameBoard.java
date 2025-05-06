package br.com.sudoku.model;

import br.com.sudoku.util.Board;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/** Define as ações a serem realizadas no template Board. */
public class GameBoard {

    // Tabuleiro: 'matriz' de quadrados
    private final List<List<Square>> squares;

    public GameBoard(final List<List<Square>> squares){
        this.squares = squares;
    }

    public List<List<Square>> getSquares(){
        return squares;
    }

    // Retorna o status atual do jogo, utilizando-se do enum GameStatusEnum.
    public GameStatusEnum getStatus(){

        // Caso todos os quadrados sejam não-fixos e nulos, retornar que o jogo não foi iniciado
        if (squares.stream()
                .flatMap(Collection::stream)
                .noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return GameStatusEnum.NOT_STARTED;
        }

        /*
         Se não, retorna o status incompleto ou completo, baseado na condição de pelo menos um quadrado atender
         à condição de ter o atual número no quadrado como nulo.
        */
        return squares.stream()
                .flatMap(Collection::stream)
                .anyMatch(square -> isNull(square.getActual()))
                ? GameStatusEnum.INCOMPLETE
                : GameStatusEnum.COMPLETE;
    }

    public boolean hasErrors(){
        if (getStatus() == GameStatusEnum.NOT_STARTED) return false; // Se o jogo não começou ainda, retorna falso

        // Retorna true se algum quadrado contém o actual, não-nulo, diferente do expected
        return squares.stream()
                .flatMap(Collection::stream)
                .anyMatch(square -> nonNull(square.getActual()) && !square.getActual().equals(square.getExpected()));

    }

    // Adicionar valor em uma posição do tabuleiro, como em uma matriz
    public boolean changeValue(final int row, final int column, final Integer value){

        // 'row' -> Lista horizontal na qual será buscado o elemento no índice referente à 'column'
        var square = squares.get(row).get(column);

        // Se o quadrado for considerado como fixo, retorna false, pois não deve ser alterado.
        if (square.isFixed()) {
            return false;
        }

        // Atualiza o valor com o parâmetro
        square.setActual(value);

        // Informa, com true, que o valor foi alterado
        return true;
    }

    // 'Limpa' o valor de um quadrado, se não for fixo
    public boolean clearValue(int row, int column){
        var square = squares.get(row).get(column);
        if (square.isFixed()) {
            return false;
        }
        square.clearSquare();
        return true;
    }

    // 'Limpa' todos os valores
    public void reset(){
        squares.forEach(row -> row.forEach(Square::clearSquare));
    }

    public boolean gameFinished(){
        return (!hasErrors() && getStatus().equals(GameStatusEnum.COMPLETE));
    }
}
