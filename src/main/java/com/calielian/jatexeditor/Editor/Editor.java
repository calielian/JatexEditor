package com.calielian.jatexeditor.Editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.calielian.jatexeditor.Main;
import com.calielian.jatexeditor.Config;

public class Editor {
    public JTextArea texto;

    private JScrollPane editorPanel;

    private byte[] textoAtual;

    public Editor() {

        JPanel panel = new JPanel();

        // configura o painel interno do editor
        panel.setBackground(Color.RED);
        panel.setLayout(new BoxLayout(panel, 0));

        // inicializa as variáveis
        texto = new JTextArea();
        textoAtual = new byte[0];

        // configura a aréa onde terá o texto do arquivo
        texto.setLineWrap(true);
        texto.setFont(Config.pegarFonteDefinida());

        // ações pra quando o texto for alterado
        texto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // caso for adicionado texto
                atualizarTitulo();
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
                // caso for removido texto
                atualizarTitulo();
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        
            // função para atualizar o título
            private void atualizarTitulo() {
                if (Arquivo.getFile() != null) {
                    // se o arquivo de edição não for nulo (há arquivo existente aberto sendo editado)

                    // verifica se houve alteração e modifica o título de acordo com essa verificação
                    if (verificarAlteração()) Main.definirNomeArquivoTitulo(Arquivo.getFile().getName(), true);
                    else Main.definirNomeArquivoTitulo(Arquivo.getFile().getName(), false);
                } else {
                    // senão:

                    // verifica se houve alteração e modifica o título de acordo com essa verificação
                    if (verificarAlteração()) Main.definirNomeArquivoTitulo("Novo arquivo", true);
                    else Main.definirNomeArquivoTitulo("Novo arquivo", false);
                }
            }
        });

        // cria uma ação para salvar o arquivo
        Action salvar = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Arquivo.salvarArquivo();
            }
        };

        // cria um atalho para quando for precionado "Ctrl S"
        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);

        // adiciona o atalho e ação a área de texto
        texto.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlS, "salvar");
        texto.getActionMap().put("salvar", salvar);

        // adiciona a área de texto ao painel interno
        panel.add(texto);

        // adiciona o painel interno dentro de um painel scrolável
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
