/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxel.ati.tp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author maximo
 */
public class Window extends JFrame {

        private static final long serialVersionUID = 1L;

        private Panel panel = new Panel(this);


    private GraphicsConfiguration config;

        public Window() {

        config = getGraphicsConfiguration();
                setTitle("TPS Analisis y Tratamiento de Imagenes");
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setBounds(1, 1, 900, 800);
                Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
                setLocation(size.width / 3 - getWidth() / 3, size.height / 3
                                - getHeight() / 2);
                setResizable(false);
                this.setMinimumSize(new Dimension(600, 600));
                panel.setBackground(Color.BLACK);

        panel.initKeyBindings();
                add(panel);


        }



        public Panel getPanel() {
                return panel;
        }

        public void enableTools() {
   
        }

    public GraphicsConfiguration getConfig() {
        return config;
    }
}