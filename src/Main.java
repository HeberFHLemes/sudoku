import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import br.com.sudoku.model.*;

import static br.com.sudoku.util.Board.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {

    // Tabuleiro, board receberá valor quando jogo for iniciado.
    private static GameBoard board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {

        // Uso do args para montar as posições iniciais e valores esperados
        final var positions = Stream.of(args)
                .collect(toMap(
                        key -> key.split(";")[0],
                        value -> value.split(";")[1])
                );

        try (Scanner scanner = new Scanner(System.in)){
            int option;
            while (true){
                
                System.out.println("Selecione uma das opções a seguir");
                System.out.println("[1] - Iniciar um novo Jogo");
                System.out.println("[2] - Colocar um novo número");
                System.out.println("[3] - Remover um número");
                System.out.println("[4] - Visualizar jogo atual");
                System.out.println("[5] - Verificar status do jogo");
                System.out.println("[6] - limpar jogo");
                System.out.println("[7] - Finalizar jogo");
                System.out.println("[0] - Sair");


                option = getOption(scanner);
                switch (option){
                    case 1 -> startGame(positions);
                    case 2 -> inputNumber(scanner);
                    case 3 -> removeNumber(scanner);
                    case 4 -> showCurrentGame();
                    case 5 -> showGameStatus();
                    case 6 -> clearGame(scanner);
                    case 7 -> finishGame();
                    case 0 -> System.exit(0);
                    default -> System.out.println("Opção inválida, selecione uma das opções do menu");
                }
            }
        }
    }

    // Iniciar novo jogo.
    private static void startGame(Map<String, String> positions) {

        // Se o tabuleiro/board não for nulo, informar que o jogo já foi iniciado
        if (nonNull(board)){
            System.out.println("O jogo já foi iniciado. ");
            return;
        }

        // Montar o tabuleiro
        List<List<Square>> squares = new ArrayList<>();

        for (int i = 0; i < BOARD_LIMIT; i++){

            // Adiciona uma linha
            squares.add(new ArrayList<>());

            // Formar os quadrados de cada linha com os args passados no main
            for (int j = 0; j < BOARD_LIMIT; j++){

                // Valor recebido do Map positions referente a valor esperado (expected) e booleano fixo (fixed)
                var positionConfig = positions.get("%s,%s".formatted(i, j));

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
        // Novo tabuleiro para este jogo instanciado
        board = new GameBoard(squares);

        System.out.println("O jogo está pronto para começar. ");

    }

    // Permitir inserção de um número em uma coordenada, se já não tiver outro número.
    private static void inputNumber(Scanner scanner) {

        // Verificar se o jogo ainda não foi iniciado
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado. ");
            return;
        }

        // Perguntar pela linha
        System.out.println("Informe a linha (0-8) onde será inserido o valor desejado: ");
        var row = getValidNumber(scanner, 0, 8);

        // Perguntar pela coluna
        System.out.println("Informe a coluna (0-8) onde será inserido o valor desejado: ");
        var column = getValidNumber(scanner, 0, 8);

        // Perguntar pelo valor a ser inserido
        System.out.printf("Informe o valor a ser inserido em [%s, %s]\n", row, column);
        var value = getValidNumber(scanner, 1, 9);

        // Alterar o valor ou não, caso o valor seja fixo
        if (!board.changeValue(row, column, value)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", row, column);
        }

    }

    // Remover um valor em uma coordenada específica, se não for fixo.
    private static void removeNumber(Scanner scanner) {

        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado. ");
            return;
        }

        // Perguntar pela linha
        System.out.println("Informe a linha (0-8) onde será inserido o valor desejado: ");
        var row = getValidNumber(scanner, 0, 8);

        // Perguntar pela coluna
        System.out.println("Informe a coluna (0-8) onde será inserido o valor desejado: ");
        var column = getValidNumber(scanner, 0, 8);System.out.println("Informe a coluna (0-8) onde será inserido o valor desejado: ");

        // Remover ou não um valor, com a condição de ser fixo ou não.
        if (!board.clearValue(row, column)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", row, column);
        }
    }

    // Informar situação atual do jogo.
    private static void showCurrentGame() {

        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado. ");
            return;
        }

        // Matriz relativa ao tabuleiro
        var squares = new Object[81];

        // Começando do primeiro elemento
        var squarePosition = 0;

        // Loop pela matriz, mostrando o valor ou espaço em cada quadrado.
        for (var row: board.getSquares()) {
            for (var square : row){
                squares[squarePosition ++] = " " + ((isNull(square.getActual())) ? " " : square.getActual());
            }
        }

        // Imprimir resultado
        System.out.println("Seu jogo se encontra da seguinte forma: ");
        System.out.printf((BOARD_TEMPLATE) + "\n", squares);
    }

    // Informar status do jogo (enums de GameStatusEnum).
    private static void showGameStatus() {

        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado. ");
            return;
        }

        // Retorna o status do jogo atual, entre os enums de GameStatusEnum
        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getStatus());

        // Informa se contém erros ou não
        if(board.hasErrors()){
            System.out.println("O jogo contém erros");
        } else {
            System.out.println("O jogo não contém erros");
        }
    }

    // Limpar valores inseridos pelo usuário, preservando os fixos.
    private static void clearGame(Scanner scanner) {

        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado. ");
            return;
        }

        // Mensagem de confirmação da escolha de limpar o jogo
        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu progresso?");
        System.out.println("[1] - SIM]");
        System.out.println("[0] - NÃO]");

        int option;
        option = getOption(scanner);

        // Uso de reset para limpar os valores inseridos pelo usuário.
        if (option == 1) board.reset();
    }

    // Se completo e sem erros, finalizar, se não, informar impossibilidade de conclusão do jogo.
    private static void finishGame() {

        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado. ");
            return;
        }

        if (board.gameFinished()){
            System.out.println("Parabéns! Jogo concluído com sucesso!");
            showCurrentGame();
            board = null;
            System.out.println("Obrigado por jogar!");
            return;
        } else if (board.hasErrors()){
            System.out.println("Existem erros no jogo, verifique novamente e ajuste o tabuleiro para concluir. ");
            return;
        }
        System.out.println("Ainda há espaço(s) em branco... Complete o tabuleiro e tente novamente. ");
    }

    // Utilizado em menus interativos para escolher as opções com um número.
    private static int getOption(Scanner scanner){

        int option;

        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e){
            option = -1;
        }

        return option;
    }

    // Utilizado para perguntar por valor inteiro válido, entre min e max.
    private static int getValidNumber(Scanner scanner, final int min, final int max){

        var current = scanner.nextInt();

        while (current < min || current > max){
            System.out.printf("Informe um número entre %s e %s.\n", min, max);
            current = scanner.nextInt();
        }

        return current;
    }
}
