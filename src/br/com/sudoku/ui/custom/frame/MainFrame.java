package br.com.sudoku.ui.custom.frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

public class MainFrame extends JFrame {

    public MainFrame(final Dimension dimension, final JPanel mainPanel){

        super("Sudoku");
        this.setSize(dimension); // Dimension passado como parâmetro.
        this.setPreferredSize(dimension); // Dimension passado como parâmetro.
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Se fechar, encerra a aplicação.
        this.setVisible(true);
        this.setLocationRelativeTo(null); // Centralizar na tela.
        this.setResizable(false); // Não permitir resize.
        this.add(mainPanel); // JPanel passado como parâmetro.

    }
}
