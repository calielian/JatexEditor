package com.calielian.texteditor;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class CMD {
    private JPanel cmdPanel;

    public CMD() {
        this.cmdPanel = new JPanel();

        cmdPanel.setBackground(Color.BLACK);
        cmdPanel.setMinimumSize(new Dimension(200, 500));
        cmdPanel.setPreferredSize(new Dimension(200, 500));
    }

    public JPanel getPanel() {
        return this.cmdPanel;
    }
}
