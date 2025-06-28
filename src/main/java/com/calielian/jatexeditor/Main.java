package com.calielian.jatexeditor;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.calielian.jatexeditor.Editor.Arquivo;
import com.calielian.jatexeditor.Editor.Editor;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class Main {

    public static JFrame mainFrame;
    public static JMenuBar menuBar;
    public static Editor editor;
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        iniciarFrame();
    }

    public static void iniciarFrame() {
        editor = new Editor();
        CMD cmd = new CMD();

        mainFrame = new JFrame("Text Editor");
        mainFrame.setMinimumSize(new Dimension(1000, 550));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setIconImage(new ImageIcon(Arquivo.class.getResource("/assets/main.png")).getImage());

        mainFrame.add(editor.getPanel(), BorderLayout.CENTER);
        mainFrame.add(cmd.getPanel(), BorderLayout.EAST);

        iniciarMenuBar();

        mainFrame.setJMenuBar(menuBar);

        mainFrame.setVisible(true);
    }

    public static void iniciarMenuBar() {
        menuBar = new JMenuBar();

        JMenu arquivosMenu = new JMenu("File");

        JMenuItem novoArquivo = new JMenuItem("New");
        JMenuItem abrirArquivo = new JMenuItem("Open");

        novoArquivo.addActionListener(e -> Arquivo.criarNovoArquivo());

        arquivosMenu.add(novoArquivo);
        arquivosMenu.add(abrirArquivo);
        
        menuBar.add(arquivosMenu);
    }
}