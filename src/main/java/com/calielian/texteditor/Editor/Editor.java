package com.calielian.texteditor.Editor;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Editor {

    private JScrollPane editorPanel;

    public Editor() {

        JPanel panel = new JPanel();

        panel.setBackground(Color.RED);

        panel.setLayout(new BoxLayout(panel, 0));

        JTextArea texto = new JTextArea();

        texto.setLineWrap(true);
        
        panel.add(texto);

        this.editorPanel = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public JScrollPane getPanel() {
        return this.editorPanel;
    }
}
