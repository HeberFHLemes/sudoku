package br.com.sudoku.model;

/** Quadrado relativo ao tabuleiro, representa um quadrado dos 9x9 */
public class Square {

    private Integer actual; // Número definido pelo usuário para este quadrado.
    private final int expected; // Número que o quadrado deve conter para completar corretamente o jogo.
    private final boolean fixed; // É fixo? Se sim, está automaticamente no tabuleiro assim que começa o jogo.

    public Square(final int expected, final boolean fixed){
        this.expected = expected;
        this.fixed = fixed;
        if (fixed){
            actual = expected; // Se for fixo, já define o atual como o número correto/esperado.
        }
    }

    // Retorna o número que está inserido neste quadrado
    public Integer getActual(){
        return actual;
    }

    // Define o número a ser inserido neste quadrado
    public void setActual(Integer actual) {
        if (fixed) return; // Se este quadrado for fixo, não permitir mudanças
        this.actual = actual;
    }

    // Retorna o número esperado/correto
    public int getExpected() {
        return expected;
    }

    // Verifica se este quadrado é fixo ou não
    public boolean isFixed() {
        return fixed;
    }

    // 'Limpa' o valor contido no quadrado
    public void clearSquare(){
        setActual(null);
    }
}
