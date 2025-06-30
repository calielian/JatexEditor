package com.calielian.jatexeditor.Editor;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.calielian.jatexeditor.Main;
import com.calielian.jatexeditor.Config;

public class Editor {

    private JScrollPane editorPanel;
    public JTextArea texto;
    private byte[] textoAtual;

    public Editor() {

        JPanel panel = new JPanel();

        panel.setBackground(Color.RED);

        panel.setLayout(new BoxLayout(panel, 0));

        texto = new JTextArea();
        textoAtual = new byte[0];

        texto.setLineWrap(true);

        texto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarTitulo();
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarTitulo();
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        
            private void atualizarTitulo() {
                if (Arquivo.getFile() != null) {
                    if (verificarAlteração()) Main.definirNomeArquivoTitulo(Arquivo.getFile().getName(), true);
                    else Main.definirNomeArquivoTitulo(Arquivo.getFile().getName(), false);
                } else {
                    if (verificarAlteração()) Main.definirNomeArquivoTitulo("Novo arquivo", true);
                    else Main.definirNomeArquivoTitulo("Novo arquivo", false);
                }
            }
        });

        texto.setFont(Config.pegarFonteDefinida());
        
        panel.add(texto);

        this.editorPanel = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public JScrollPane getPanel() {
        return this.editorPanel;
    }

    public void definirTextoEditor(String texto) {
        this.texto.setText(texto);
        this.textoAtual = texto.getBytes();
    }

    public void atualizarTexto(byte[] bytes) {
        this.textoAtual = bytes;
    }

    public boolean verificarAlteração() {
        return !Arrays.equals(this.textoAtual, texto.getText().getBytes(StandardCharsets.UTF_8));
    }
}
