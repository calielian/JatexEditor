package com.calielian.jatexeditor.Editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import com.calielian.jatexeditor.Main;

public class Arquivo {
    private static File file = null;
    private static Editor editor = Main.editor;

    public static File criarNovoArquivo() {
        JFileChooser escolher = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        String nome = JOptionPane.showInputDialog("Digite o nome do arquivo:");

        escolher.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Object[] opcoes = {"Renomear", "Sobrescrever"};

        
            // loop
        if (escolher.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            reinicio: while (true) {
                try {
                    String caminho = escolher.getSelectedFile().getAbsolutePath();
                    
                    file = new File(caminho + "/"+ nome);

                    while (!file.createNewFile()) {
                        int opcao = JOptionPane.showOptionDialog(
                            null,
                            "O arquivo já existe, deseja renomear ou sobrescrever o arquivo?",
                            "Arquivo já existente.",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            new ImageIcon(Arquivo.class.getResource("/assets/mainMini.png")),
                            opcoes,
                            opcoes[0]
                        );

                        if (opcao == JOptionPane.YES_OPTION) {
                            // renomear
                            nome = JOptionPane.showInputDialog("Digite o novo nome do arquivo: ");
                            continue reinicio;
                        } else {
                            // sobrescrever
                            if (editor.getEditorText().length() > 0) Files.write(Paths.get(caminho + "/" + nome), List.of(editor.getEditorText()));
                            else Files.write(Paths.get(caminho + "/" + nome), List.of(""));
                            break;
                        }
                    }

                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    continue reinicio;
                }
            }
        }

        return file;
    }

    public static File getFile() {
        return file;
    }

}
