package com.calielian.jatexeditor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.calielian.jatexeditor.Editor.Arquivo;

public class Config {
    public static final int FONTE = 0;
    public static final int TAMANHO_FONTE = 1;
    public static final int TEMA = 2;

    private static final String CONF_FONTE = "FONTE=";
    private static final String CONF_TAM_FONTE = "TAM_FONTE=";
    private static final String CONF_TEMA = "TEMA=";

    private static List<String> config;
    private static List<String> fontesDisponiveis;

    private static Font fontePadrao;
    private static Font fonteDefinida;

    private static final Path PATH_ARQUIVO_CONF = Path.of("config.jtex");

    private static final List<String> CONFIG_PADRAO = List.of("FONTE=jetbrains", "TAM_FONTE=14", "TEMA=claro");

    public static String acessarConfiguracoes(int tipo) {
        try {
            // lê o arquivo de configurações
            config = Files.readAllLines(PATH_ARQUIVO_CONF);

            // retorna as configurações solicitadas
            if (tipo == FONTE) return config.get(FONTE).split("=")[1];
            else if (tipo == TAMANHO_FONTE) return config.get(TAMANHO_FONTE).split("=")[1];
            else if (tipo == TEMA) return config.get(TEMA).split("=")[1];

        } catch (IOException e) {
            // caso tenha dado erro na leitura do aquivo
            JOptionPane.showMessageDialog(null, "Não foi possível acessar o arquivo de configurações.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return "";
    }

    public static void alterarFonte() {
        JFrame frame = new JFrame("Configurações da fonte");

        // configurando o frame
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(new Dimension(430, 70));
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon(Main.class.getResource("/assets/main.png")).getImage());

        // criando e configurando o painel de configurações da fonte
        JPanel painelConfig = new JPanel();
        painelConfig.setLayout(new FlowLayout(FlowLayout.LEFT));

        // criando as opções de fonte a partir das fontes do sistema operacional do usuário e criando a caixa de seleção a partir das opções
        String[] opcoes = pegarFontes().toArray(new String[0]);
        JComboBox<String> fontes = new JComboBox<>(opcoes);

        //  configurando a caixa de seleções
        fontes.setEditable(false);
        fontes.setSelectedItem(pegarFonteDefinida().getFamily());

        // adicionando as ações que serão tomadas após o valor mudar
        fontes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selecionado = (String) fontes.getSelectedItem(); // pega a fonte selecionada

                // para caso for selecionado a fonte padrão
                if (selecionado.equals("JetBrains Mono Medium")) selecionado = "jetbrains";

                // remove a fonte antiga do arquivo de configurações e adiciona a nova
                config.remove(FONTE);
                config.add(FONTE, CONF_FONTE + selecionado);

                try {
                    // tenta escrever no arquivo de configurações
                    Files.write(PATH_ARQUIVO_CONF, config);
                } catch (IOException error) {
                    // caso não seja possível
                    JOptionPane.showMessageDialog(null, "Não foi possível salvar a fonte no arquivo de configuração, verifique se o arquivo existe ou altere manualmente o arquivo pra família selecionada\n%s".formatted((String) fontes.getSelectedItem()), "Erro", JOptionPane.ERROR_MESSAGE);
                    error.printStackTrace();
                }
            }
        });

        // cria a caixa de opções do tamanho da fonte, sendo:
        /*
         * valor padrão: o tamanho da fonte no arquivo de configurações
         * valor mínimo: 10
         * valor máximo: 42
         * passo (vai pulando): de 2 em 2 (somente números pares)
         */
        JSpinner tamanhoFonte = new JSpinner(new SpinnerNumberModel(Integer.parseInt(acessarConfiguracoes(TAMANHO_FONTE)), 10, 42, 2));

        // adicionando as ações para quando o valor for alterado
        tamanhoFonte.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                // remove o tamanho antigo e adiciona o tamanho novo no arquivo de configurações
                config.remove(TAMANHO_FONTE);
                config.add(TAMANHO_FONTE, CONF_TAM_FONTE + String.valueOf(tamanhoFonte.getValue()));

                try {
                    // tenta escrever as alterações no arquivo de configurações
                    Files.write(PATH_ARQUIVO_CONF, config);
                } catch (IOException e) {
                    // pra caso não for possível
                    JOptionPane.showMessageDialog(null, "Não foi possível salvar o tamanho da fonte no arquivo de configuração, verifique se o arquivo existe ou altere manualmente o arquivo para o tamanho selecionado\n%d".formatted(tamanhoFonte.getValue()), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // adiciona a caixa de seleções de fonte e tamanho da fonte ao painel
        painelConfig.add(fontes);
        painelConfig.add(tamanhoFonte);

        // adiciona as ações que serão tomadas quando certa ação foi feita na janela de cofigurações da fonte
        frame.addWindowListener(new WindowListener() {
            // para quando a janela estiver fechando:
            @Override
            public void windowClosing(WindowEvent e) {
                // cria uma variável com dois valores possíveis:
                // se a fonte selecionada for igual a fonte padrão do aplicativo: valor = "jetbrains"
                // senão: valor = fonte arquivada nas configurações
                String fonte = (acessarConfiguracoes(FONTE).equals("JetBrains Mono Medium")) ? "jetbrains" : acessarConfiguracoes(FONTE);

                // alterando a fonte definida para:
                // se fonte NÃO for igual a "jetbrains": fonte arquivada com tamanho arquivado
                // senão: fonte padrão do aplicativo com tamanho arquivado
                fonteDefinida = (!fonte.equals("jetbrains")) ? (new Font(acessarConfiguracoes(FONTE), Font.PLAIN, Integer.parseInt(acessarConfiguracoes(TAMANHO_FONTE)))) : (new Font("JetBrains Mono Medium", Font.PLAIN, Integer.parseInt(acessarConfiguracoes(TAMANHO_FONTE))));

                // definindo a fonte no editor de texto
                Main.editor.texto.setFont(fonteDefinida);

                // fecha a janela de configurações da fonte
                frame.dispose();
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowOpened(WindowEvent e) {
            }
        });

        // adiciona o painel de configurações a frame de configurações e a deixa visível
        frame.add(painelConfig);
        frame.setVisible(true);
    }

    public static void alterarTema() {
        // cria as opções e a variável que terá o tema selecionado
        Object[] opcoes = {"Claro", "Escuro"};
        String temaNovo;

        // cria uma variável que terá o valor dependendo da opção selecionada pelo usuário, ou negativa se fechada sem escolher
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

        // caso tenha sido escolhido nada, retornar sem fazer nada
        if (escolha < 0) return;

        // define o novo tema com base na escolha do usuário
        if (escolha == JOptionPane.YES_OPTION) temaNovo = "claro";
        else temaNovo = "escuro"; 

        try {
            // retira o tema antigo e insere o tema novo nas configurações
            config.remove(TEMA);
            config.add(TEMA, CONF_TEMA + temaNovo);

            // tenta escrever no arquivo de configurações
            Files.write(PATH_ARQUIVO_CONF, config);

            // aviso para reiniciar o aplicativo para que tudo seja alterado
            JOptionPane.showMessageDialog(null, "Reinicie o aplicativo para as alterações serem aplicadas.", "Configurações alteradas", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            // caso não for possível
            JOptionPane.showMessageDialog(null, "Não foi possível alterar ou acessar o arquivo de configurações.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static void mostrarErro() {
        // para economizar tempo e linhas
        JOptionPane.showMessageDialog(null, "O arquivo de configuração é inválido! Verifique se segue esse modelo:\n\nFONTE=familia (familia da fonte, deve estar no sistema)\nTAM_FONTE=numero_decimal ou numero_inteiro\nTEMA=palavra (valores permitidos para tema: \"claro\", \"escuro\", sem aspas, em minúsculo)", "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void iniciar() {
        Font fonteCustom = null;

        // tenta criar a fonte padrão do aplicativo, fechando-o caso não seja possível
        try {
            fonteCustom = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/assets/jetbrains.ttf"));
        } catch (FontFormatException | IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível criar a fonte padrão utilzada no aplicativo.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        // registra a fonte no ambiente gráfico do sistema operacional do usuário para que possa ser usada globalmente no app
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fonteCustom);

        // cria a fonte padrão do aplicativo derivando da fonte padrão original
        fontePadrao = fonteCustom.deriveFont(14f);

        // define a fonte definida como a fonte padrão, para caso não seja possível carregar outras fontes
        fonteDefinida = fontePadrao;

        // pega todas as fontes disponíveis no sistema operacional do usuário
        fontesDisponiveis = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

        try {
            if (!Files.exists(PATH_ARQUIVO_CONF)) {
                // caso o arquivo de configurações NÃO existir:
                // criar arquivo na mesma pasta de onde está o arquivo de execução do aplicativo, escrever as configurações padrão nele e inicializar config
                Files.createFile(PATH_ARQUIVO_CONF);
                Files.write(PATH_ARQUIVO_CONF, CONFIG_PADRAO);
                config = CONFIG_PADRAO;
            } else {
                // senão:
                // ler o arquivo
                config = Files.readAllLines(PATH_ARQUIVO_CONF);

                // pega cada configuração do arquivo
                String[] configFonte = config.get(FONTE).split("=");
                String[] configTamFonte = config.get(TAMANHO_FONTE).split("=");
                String[] configTema = config.get(TEMA).split("=");

                if (config.get(TAMANHO_FONTE).contains("=") && configTamFonte[0].equals("TAM_FONTE")) {
                // caso a configuração de tamanho for válida:
                    try {
                        // tenta converter o valor (verifica se foi inserido um valor float ou inteiro)
                        Float.parseFloat(configTamFonte[1]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        mostrarErro();
                        System.exit(1);
                    }
                } else  {
                    mostrarErro();
                    System.exit(1);
                }

                if (config.get(FONTE).contains("=") && configFonte[0].equals("FONTE")) {
                    // caso a configuração fde fonte for válida:
                    // verifica se está a fonte padrão no arquivo de configuração
                    if (configFonte[1].equals("jetbrains")) fonteDefinida = fonteCustom.deriveFont(Float.parseFloat(configTamFonte[1]));
                    else if (!fontesDisponiveis.contains(configFonte[1])) {
                        // caso a fonte não esteja, então é colocado a fonte padrão
                        JOptionPane.showMessageDialog(null, "A fonte inserida não é válida.");

                        fonteDefinida = fontePadrao;
                    } else {
                        // caso a fonte for válida e não for a fonte padrão, usar a fonte de fato inserida
                        fonteDefinida = new Font(configFonte[1], Font.PLAIN, Integer.parseInt(configTamFonte[1]));
                    }
                }

                // verifica se o tema inserido é válido ou não
                if (config.get(TEMA).contains("=") && configTema[0].equals("TEMA")) {
                    if (!(configTema[1].equals("claro") || configTema[1].equals("escuro"))) {mostrarErro(); System.exit(1);}
                } else {
                    mostrarErro();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            // caso não for possível ler o arquivo de configurações
            JOptionPane.showMessageDialog(null, "Não foi possível criar ou ler o arquivo de configurações.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static List<String> pegarFontes() {
        return fontesDisponiveis;
    }

    public static Font pegarFonteDefinida() {
        return fonteDefinida;
    }

}
