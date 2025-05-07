package br.com.sudoku.ui.custom.input;

import br.com.sudoku.model.Square;
import br.com.sudoku.service.EventEnum;
import br.com.sudoku.service.EventListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

import static br.com.sudoku.service.EventEnum.CLEAR_SPACE;
import static java.awt.Font.PLAIN;

public class NumberText extends JTextField implements EventListener {

    private final Square square;

    public NumberText(final Square square) {

        this.square = square;

        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", PLAIN, 20)); // Fonte do texto
        this.setHorizontalAlignment(CENTER); // Texto alinhado horizontalmente ao centro.
        this.setDocument(new NumberTextLimit()); // Classe NumberTextLimit criada.
        this.setEnabled(!square.isFixed()); // Permitir ou não edição dependendo da condição do square ser fixo.

        if (square.isFixed()) this.setText(square.getActual().toString()); // Já preencher com respectivo texto, caso for fixo.

        this.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeSquare();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeSquare();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeSquare();
            }

            /** Mudar o valor do quadrado, se o getText() estiver vazio, apenas chamar clearSquare(), se não, atualiza o valor. */
            private void changeSquare(){
                if (getText().isEmpty()){
                    square.clearSquare();
                    return;
                }
                square.setActual(Integer.parseInt(getText()));
            }
        });
    }

    @Override
    public void update(final EventEnum eventType) {
        if (eventType.equals(CLEAR_SPACE) && this.isEnabled()) this.setText("");
    }
}
