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
        // cria um objeto de uma janela para que possa escolher aonde salvar o arquivo
        JFileChooser escolher = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        // pergunta o nome completo o arquivo
        String nome = JOptionPane.showInputDialog("Digite o nome e extensão do arquivo:");

        // se for fechado, cancelar operação retornando
        if (nome == null) return;

        boolean sobrescrever = false;

        // configura a janela para selecionar somente diretórios
        escolher.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // cria a lista de opções
        Object[] opcoes = {"Renomear", "Sobrescrever"};

        // abre a janela e pega a opção selecionada
        if (escolher.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            // caso seja selecioado um diretório:
            // cria uma label, que leva a um loop:
            reinicio: while (true) {
                try {
                    // pega o caminho do diretório
                    String caminho = escolher.getSelectedFile().getAbsolutePath();
                    
                    // cria um objeto do arquivo naquele diretório
                    file = new File(caminho + "/" + nome);

                    // tenta criar um arquivo sendo executado caso não for possível criar o arquivo
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
                            // renomeia o arquivo
                            nome = JOptionPane.showInputDialog("Digite o novo nome do arquivo: ");
                            continue reinicio; // retorna ao label
                        } else {
                            // sobrescreve o arquivo
                            sobrescrever = true;
                            if (editor.texto.getText().length() > 0) Files.writeString(Path.of(caminho + "/" + nome), editor.texto.getText());
                            else Files.writeString(Path.of(caminho + "/" + nome), "");

                            Main.definirNomeArquivoTitulo(nome, false);
                            editor.atualizarTexto(Files.readAllBytes(file.toPath()));

                            break;
                        }
                    }

                    // já adiciona o texto digitado pelo usuário, caso tenha escrito algo, ao arquivo criado
                    if (editor.texto.getText().length() > 0) Files.writeString(Path.of(caminho + "/" + nome), editor.texto.getText());

                    Main.definirNomeArquivoTitulo(nome, false);
                    editor.atualizarTexto(Files.readAllBytes(file.toPath()));

                    break; // encerra o loop
                } catch (IOException e) {
                    // caso não for possível criar/sobrescrever o arquivo
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
            file = escolher.getSelectedFile();

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
        if (file != null) {
            // se o arquivo não for nulo (foi criado)
            try {
                // tenta escrever o conteúdo digitado nele
                Files.writeString(Path.of(file.getAbsolutePath()), editor.texto.getText());
                editor.atualizarTexto(Files.readAllBytes(file.toPath()));
                Main.definirNomeArquivoTitulo(file.getName(), false);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Não foi possível salvar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            // senão: manda criar um novo
            criarNovoArquivo();
        }
    }

    public static void salvarArquivoComo() {
        JFileChooser escolher;
        
        // "semelhante igual" o método de criar arquivo, com 1 diferença:
    
        // define se vai abrir a janela já no diretório parente do arquivo, ou não
        if (file != null) {
            escolher = new JFileChooser(file.getParentFile());
        } else {
            escolher = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        }

        String nome = JOptionPane.showInputDialog("Digite o nome do arquivo:");

        if (nome == null) return;

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
                    if (!sobrescrever) JOptionPane.showMessageDialog(null, "Não foi possível salvar o arquivo, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
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
