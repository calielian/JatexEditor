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
        Config.iniciar();

        String tema = Config.acessarConfiguracoes(Config.TEMA);

        if (tema == "claro") FlatLightLaf.setup();
        else FlatDarkLaf.setup();

        iniciarFrame();
    }

    public static void iniciarFrame() {
        editor = new Editor();

        mainFrame = new JFrame("JatexEditor - Novo arquivo");
        mainFrame.setMinimumSize(new Dimension(1000, 550));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setIconImage(new ImageIcon(Arquivo.class.getResource("/assets/main.png")).getImage());

        mainFrame.add(editor.getPanel(), BorderLayout.CENTER);

        iniciarMenuBar();

        mainFrame.setJMenuBar(menuBar);

        mainFrame.setVisible(true);
    }

    public static void iniciarMenuBar() {
        menuBar = new JMenuBar();

        JMenu arquivosMenu = new JMenu("Aquivo");
        JMenu configMenu = new JMenu("Configurações");

        JMenuItem novoArquivo = new JMenuItem("Novo");
        JMenuItem abrirArquivo = new JMenuItem("Abrir");
        JMenuItem salvar = new JMenuItem("Salvar");
        JMenuItem salvarComo = new JMenuItem("Salvar como");

        JMenuItem fonte = new JMenuItem("Alterar tamanho da fonte");
        JMenuItem tema = new JMenuItem("Alterar tema padrão");

        novoArquivo.addActionListener(e -> Arquivo.criarNovoArquivo());
        abrirArquivo.addActionListener(e -> Arquivo.abrirArquivo());
        salvar.addActionListener(e -> Arquivo.salvarArquivo());
        salvarComo.addActionListener(e -> Arquivo.salvarArquivoComo());

        fonte.addActionListener(e -> Config.alterarFonte());
        tema.addActionListener(e -> Config.alterarTema());

        arquivosMenu.add(novoArquivo);
        arquivosMenu.add(abrirArquivo);
        arquivosMenu.add(salvar);
        arquivosMenu.add(salvarComo);

        configMenu.add(fonte);
        configMenu.add(tema);
        
        menuBar.add(arquivosMenu);
        menuBar.add(configMenu);
    }

    public static void definirNomeArquivoTitulo(String nome, boolean modificado) {
        if (modificado) mainFrame.setTitle("JatexEditor - [%s]*".formatted(nome));
        else mainFrame.setTitle("JatexEditor - %s".formatted(nome));
    }

}