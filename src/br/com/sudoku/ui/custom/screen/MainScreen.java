package br.com.sudoku.ui.custom.screen;

import br.com.sudoku.model.Square;
import br.com.sudoku.service.*;
import br.com.sudoku.ui.custom.button.*;
import br.com.sudoku.ui.custom.input.NumberText;
import br.com.sudoku.ui.custom.frame.MainFrame;
import br.com.sudoku.ui.custom.panel.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.sudoku.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainScreen {

    private final static Dimension dimension = new Dimension(600, 600);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton finishGameButton;
    private JButton checkGameStatusButton;
    private JButton resetButton;

    public MainScreen(final Map<String, String> gameConfig){

        this.boardService = new BoardService(gameConfig);

        this.notifierService = new NotifierService();
    }

    public void buildMainScreen(){

        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);

        // Loop para montar com os respectivos quadrados (de 3 em 3)
        for (int row = 0; row < 9; row += 3) {
            var endRow = row + 2;
            for (int column = 0; column < 9; column += 3) {
                var endCol = column + 2;
                var squares = getSquaresFromSector(boardService.getSquares(), row, endRow, column, endCol);
                JPanel sector = generateSection(squares);
                mainPanel.add(sector);
            }
        }

        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);

        mainFrame.repaint();
        mainFrame.revalidate();
    }

    private List<Square> getSquaresFromSector(final List<List<Square>> squares,
                                            final int initRow, final int endRow,
                                            final int initCol, final int endCol){

        // Loop para percorrer o sector formado em generateSection()
        List<Square> squareSector = new ArrayList<>();
        for (int row = initRow; row <= endRow; row++) {
            for (int column = initCol; column <= endCol; column++) {
                squareSector.add(squares.get(row).get(column));
            }
        }
        return squareSector;
    }

    private JPanel generateSection(final List<Square> squares){

        List<NumberText> fields = new ArrayList<>(squares.stream().map(NumberText::new).toList());

        fields.forEach(text -> notifierService.subscribe(CLEAR_SPACE, text));

        return new SudokuSector(fields);

    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameFinished()){
                showMessageDialog(null, "Parabéns você concluiu o jogo! ");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                var message = "Seu jogo tem alguma inconsistência, ajuste e tente novamente. ";
                showMessageDialog(null, message);
            }
        });

        mainPanel.add(finishGameButton);
    }

    /** Botão para permitir verificação da situação atual do jogo (Se está completo e sem erros ou não) */
    private void addCheckGameStatusButton(JPanel mainPanel) {

        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NOT_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            showMessageDialog(null, message);
        });

        mainPanel.add(checkGameStatusButton);
    }

    /** Botão para permitir o usuário a recomeçar o jogo (limpa os valores inseridos) */
    private void addResetButton(JPanel mainPanel) {

        resetButton = new ResetButton(e -> {
            var dialogResult = showConfirmDialog(
                    null,
                    "Deseja realmente reiniciar o jogo?",
                    "Reiniciar jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if (dialogResult == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });

        mainPanel.add(resetButton);
    }

}
