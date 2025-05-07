package br.com.sudoku.ui.custom.input;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.List;

import static java.util.Objects.isNull;

/** Permitir apenas alguns caracteres como input, neste caso, números de 1 a 9. */
public class NumberTextLimit extends PlainDocument {

    private final List<String> NUMBERS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Override
    public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {

        // Verificar se a String é nula ou não está entre os inputs permitidos (NUMBERS).
        if (isNull(str) || (!NUMBERS.contains(str))) return;

        // Verificar tamanho do input.
        if (getLength() + str.length() <= 1){
            super.insertString(offs, str, a);
        }
    }
}
