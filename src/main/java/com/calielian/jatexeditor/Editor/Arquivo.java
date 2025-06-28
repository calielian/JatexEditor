package com.calielian.jatexeditor.Editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import com.calielian.jatexeditor.Main;

public class Arquivo {
    private static File file = null;
    private static Editor editor = Main.editor;

    public static void criarNovoArquivo() {
        JFileChooser escolher = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        String nome = JOptionPane.showInputDialog("Digite o nome do arquivo:");

        boolean sobrescrever = false;

        escolher.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Object[] opcoes = {"Renomear", "Sobrescrever"};

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
                            sobrescrever = true;
                            if (editor.texto.getText().length() > 0) Files.writeString(Path.of(caminho + "/" + nome), editor.texto.getText());
                            else Files.writeString(Path.of(caminho + "/" + nome), "");

                            Main.definirNomeArquivoTitulo(nome, false);
                            editor.atualizarTexto(Files.readAllBytes(file.toPath()));

                            break;
                        }
                    }

                    if (editor.texto.getText().length() > 0) Files.writeString(Path.of(caminho + "/" + nome), editor.texto.getText());

                    Main.definirNomeArquivoTitulo(nome, false);
                    editor.atualizarTexto(Files.readAllBytes(file.toPath()));

                    break;
                } catch (IOException e) {
                    if (!sobrescrever) JOptionPane.showMessageDialog(null, "Não foi possível criar o arquivo, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    else JOptionPane.showMessageDialog(null, "Não foi possível sobrescrever o arquivo, verifique se possui a permissão de escrita sobre o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public static void abrirArquivo() {
        JFileChooser escolher = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        escolher.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (escolher.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = new File(escolher.getSelectedFile().getAbsolutePath());

            try {
                editor.definirTextoEditor(new String(Files.readAllBytes(Path.of(file.getAbsolutePath()))));
                editor.atualizarTexto(Files.readAllBytes(Path.of(file.getAbsolutePath())));
                Main.definirNomeArquivoTitulo(file.getName(), false);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Não foi possível abrir o arquivo, verifique se possui permissão para acessar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            return;
        }
    }

    public static void salvarArquivo() {
        try {
            Files.writeString(Path.of(file.getAbsolutePath()), editor.texto.getText());
            editor.atualizarTexto(Files.readAllBytes(file.toPath()));
            Main.definirNomeArquivoTitulo(file.getName(), false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível salvar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void salvarArquivoComo() {
        JFileChooser escolher = new JFileChooser(FileSystemView.getFileSystemView().getParentDirectory(file.getParentFile()));

        String nome = JOptionPane.showInputDialog("Digite o nome do arquivo:");

        boolean sobrescrever = false;

        escolher.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Object[] opcoes = {"Renomear", "Sobrescrever"};

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
                            sobrescrever = true;
                            if (editor.texto.getText().length() > 0) Files.writeString(Path.of(caminho + "/" + nome), editor.texto.getText());
                            else Files.writeString(Path.of(caminho + "/" + nome), "");

                            Main.definirNomeArquivoTitulo(nome, false);
                            editor.atualizarTexto(Files.readAllBytes(file.toPath()));

                            break;
                        }
                    }

                    if (editor.texto.getText().length() > 0) Files.writeString(Path.of(caminho + "/" + nome), editor.texto.getText());

                    Main.definirNomeArquivoTitulo(nome, false);
                    editor.atualizarTexto(Files.readAllBytes(file.toPath()));

                    break;
                } catch (IOException e) {
                    if (!sobrescrever) JOptionPane.showMessageDialog(null, "Não foi possível criar o arquivo, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                    else JOptionPane.showMessageDialog(null, "Não foi possível sobrescrever o arquivo, verifique se possui a permissão de escrita sobre o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    break;
                }
            }
        }

    }

    public static File getFile() {
        return file;
    }

}
