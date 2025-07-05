package com.calielian.jatexeditor;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.calielian.jatexeditor.Editor.Arquivo;
import com.calielian.jatexeditor.Editor.Editor;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class Main {
    public static JFrame mainFrame;
    public static JMenuBar menuBar;
    public static Editor editor;
    public static void main(String[] args) {
        // inicia as configurações -> verifica o arquivo de configuração e inicia elas
        Config.iniciar();

        // definição do tema
        if (Config.acessarConfiguracoes(Config.TEMA).equals("claro")) FlatLightLaf.setup();
        else FlatDarkLaf.setup();

        // inicia a GUI do JatexEdiitor
        iniciarFrame();
    }

    public static void iniciarFrame() {
        editor = new Editor();

        // iniciando e configurando a frame principal do app
        mainFrame = new JFrame("JatexEditor - Novo arquivo");
        mainFrame.setMinimumSize(new Dimension(1000, 550));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setIconImage(new ImageIcon(Arquivo.class.getResource("/assets/main.png")).getImage());

        // adicionando o painel do editor ao frame principal
        mainFrame.add(editor.getPanel(), BorderLayout.CENTER);

        // inicia a menu bar
        iniciarMenuBar();

        // define para usar a menu bar iniciada
        mainFrame.setJMenuBar(menuBar);

        // deixa o frame visível, quando carregada
        mainFrame.setVisible(true);
    }

    public static void iniciarMenuBar() {
        menuBar = new JMenuBar();

        // criando os menus da menu bar
        JMenu arquivosMenu = new JMenu("Aquivo");
        JMenu configMenu = new JMenu("Configurações");

        // criando os itens do menu da menu bar "Arquivo"
        JMenuItem novoArquivo = new JMenuItem("Novo");
        JMenuItem abrirArquivo = new JMenuItem("Abrir");
        JMenuItem salvar = new JMenuItem("Salvar");
        JMenuItem salvarComo = new JMenuItem("Salvar como");

        // criando os itens do menu da menu bar "Configurações"
        JMenuItem fonte = new JMenuItem("Alterar fonte");
        JMenuItem tema = new JMenuItem("Alterar tema padrão");

        // adiciona as ações ao clicar nos itens dos menus da menu bar
        novoArquivo.addActionListener(e -> Arquivo.criarNovoArquivo());
        abrirArquivo.addActionListener(e -> Arquivo.abrirArquivo());
        salvar.addActionListener(e -> Arquivo.salvarArquivo());
        salvarComo.addActionListener(e -> Arquivo.salvarArquivoComo());
        fonte.addActionListener(e -> Config.alterarFonte());
        tema.addActionListener(e -> Config.alterarTema());

        // adicionando cada item em seu menu
        arquivosMenu.add(novoArquivo);
        arquivosMenu.add(abrirArquivo);
        arquivosMenu.add(salvar);
        arquivosMenu.add(salvarComo);
        configMenu.add(fonte);
        configMenu.add(tema);
        
        // adicionando os menus a própria menu bar
        menuBar.add(arquivosMenu);
        menuBar.add(configMenu);
    }

    public static void definirNomeArquivoTitulo(String nome, boolean modificado) {
        // define o título da frame principal para quando o arquivo estiver modificado ou não
        if (modificado) mainFrame.setTitle("JatexEditor - [%s]*".formatted(nome)); // para caso estiver modificado
        else mainFrame.setTitle("JatexEditor - %s".formatted(nome)); // para caso não estiver modificado
    }

}