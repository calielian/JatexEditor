package com.calielian.jatexeditor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.calielian.jatexeditor.Editor.Arquivo;

public class Config {
    private static final List<String> CONFIG_PADRAO = List.of("TAM_FONTE=18", "TEMA=claro");

    public static final int TAMANHO_FONTE = 0;
    public static final int TEMA = 1;

    private static List<String> config;

    private static final String CONF_FONTE = "TAM_FONTE=";
    private static final String CONF_TEMA = "TEMA=";

    private static final Path PATH_ARQUIVO_CONF = Path.of("config.jtex");

    public static String acessarConfiguracoes(int tipo) {
        try {
            config = Files.readAllLines(PATH_ARQUIVO_CONF);

            if (tipo == TAMANHO_FONTE) return config.get(0).split("=")[1];
            else if (tipo == TEMA) return config.get(1).split("=")[1];

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível acessar o arquivo de configurações.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return "";
    }

    public static void alterarFonte() {
        reinicio: while (true) {
            try {
                String escolha = JOptionPane.showInputDialog("Digite o novo tamanho:");

                if (escolha == null) break;

                int tamanhoNovo = Integer.parseInt(escolha);

                config = Files.readAllLines(PATH_ARQUIVO_CONF);
                config.remove(TAMANHO_FONTE);
                config.add(TAMANHO_FONTE, CONF_FONTE + String.valueOf(tamanhoNovo));

                Files.write(PATH_ARQUIVO_CONF, config);

                JOptionPane.showMessageDialog(null, "Reinicie o aplicativo para as alterações serem aplicadas.", "Configurações alteradas", JOptionPane.    INFORMATION_MESSAGE);
                break;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Não foi possível alterar ou acessar o arquivo de configurações.", "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite somente valores números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
                continue reinicio;
            }
        }
    }

    public static void alterarTema() {
        Object[] opcoes = {"Claro", "Escuro"};
        String temaNovo;

        int escolha = JOptionPane.showOptionDialog(
            null,
            "Qual o tema que deseja?",
            "Alterar tema.",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            new ImageIcon(Arquivo.class.getResource("/assets/mainMini.png")),
            opcoes,
            opcoes[0]
        );

        if (escolha < 0) return;

        if (escolha == JOptionPane.YES_OPTION) temaNovo = "claro";
        else temaNovo = "escuro"; 

        try {
            config = Files.readAllLines(PATH_ARQUIVO_CONF);
            config.remove(TEMA);
            config.add(TEMA, CONF_TEMA + temaNovo);

            Files.write(PATH_ARQUIVO_CONF, config);

            JOptionPane.showMessageDialog(null, "Reinicie o aplicativo para as alterações serem aplicadas.", "Configurações alteradas", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível alterar ou acessar o arquivo de configurações.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static void mostrarErro() {
        JOptionPane.showMessageDialog(null, "O arquivo de configuração é inválido! Verifique se segue esse modelo:\n\nTAM_FONTE=numero_inteiro\nTEMA=palavra (valores permitidos para tema: \"claro\", \"escuro\", sem aspas, em minúsculo)", "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void iniciar() {
        try {
            if (!Files.exists(PATH_ARQUIVO_CONF)) {
                Files.createFile(PATH_ARQUIVO_CONF);
                Files.write(PATH_ARQUIVO_CONF, CONFIG_PADRAO);
            } else {
                config = Files.readAllLines(PATH_ARQUIVO_CONF);

                String[] configFonte = config.get(0).split("=");
                String[] configTema = config.get(1).split("=");

                if (config.get(TAMANHO_FONTE).contains("=") && configFonte[0].equals("TAM_FONTE")) {
                    try {
                        Integer.parseInt(configFonte[1]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        mostrarErro();
                        System.exit(1);
                    }
                } else mostrarErro();

                if (config.get(TEMA).contains("=") && configTema[0].equals("TEMA")) {
                    if (!(configTema[1].equals("claro") || configTema[1].equals("escuro"))) {mostrarErro(); System.exit(1);}
                } else {
                    mostrarErro();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível criar ou ler o arquivo de configurações.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

}
