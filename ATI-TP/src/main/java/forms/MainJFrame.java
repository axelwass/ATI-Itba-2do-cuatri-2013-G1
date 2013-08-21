/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import com.maxel.ati.tp.EasyImage;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author maximo
 */
public class MainJFrame extends javax.swing.JFrame {

    EasyImage img1 = null;
    EasyImage img2 = null;

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        LoadImg1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        Show = new javax.swing.JMenuItem();
        NegativeImg1 = new javax.swing.JMenuItem();
        Normalize = new javax.swing.JMenuItem();
        LoadImg2 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        Negative2 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        Suma = new javax.swing.JMenuItem();
        Resta12 = new javax.swing.JMenuItem();
        Resta21 = new javax.swing.JMenuItem();
        Save1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        jMenu1.setText("jMenu1");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu2.setText("File");

        LoadImg1.setText("Load img 1...");
        LoadImg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadImg1ActionPerformed(evt);
            }
        });
        jMenu2.add(LoadImg1);

        jMenu4.setText("Edit img 1");

        Show.setText("Show");
        Show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowActionPerformed(evt);
            }
        });
        jMenu4.add(Show);

        NegativeImg1.setText("Negative");
        NegativeImg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NegativeImg1ActionPerformed(evt);
            }
        });
        jMenu4.add(NegativeImg1);

        Normalize.setText("Normalize");
        Normalize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NormalizeActionPerformed(evt);
            }
        });
        jMenu4.add(Normalize);

        jMenu2.add(jMenu4);

        LoadImg2.setText("Load img 2...");
        LoadImg2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadImg2ActionPerformed(evt);
            }
        });
        jMenu2.add(LoadImg2);

        jMenu5.setText("Edit img 2");

        Negative2.setText("Negative");
        Negative2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Negative2ActionPerformed(evt);
            }
        });
        jMenu5.add(Negative2);

        jMenu2.add(jMenu5);

        jMenu6.setText("Edit img 1 and 2");

        Suma.setText("Suma");
        Suma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SumaActionPerformed(evt);
            }
        });
        jMenu6.add(Suma);

        Resta12.setText("Resta 1 - 2");
        Resta12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Resta12ActionPerformed(evt);
            }
        });
        jMenu6.add(Resta12);

        Resta21.setText("Resta 2 - 1");
        jMenu6.add(Resta21);

        jMenu2.add(jMenu6);

        Save1.setText("Save img 1");
        Save1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Save1ActionPerformed(evt);
            }
        });
        jMenu2.add(Save1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Edit");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 633, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 424, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LoadImg1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadImg1ActionPerformed
        // TODO add your handling code here:
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new ImgFileFilter());
        
        
//In response to a button click:
        fc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(fc.getSelectedFile());
                    img1 = new EasyImage(img);
                } catch (IOException ex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        fc.showOpenDialog(LoadImg1);

    }//GEN-LAST:event_LoadImg1ActionPerformed

    private void LoadImg2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadImg2ActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new ImgFileFilter());
//In response to a button click:
        fc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(fc.getSelectedFile());
                    img2 = new EasyImage(img);
                } catch (IOException ex) {
                    Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        fc.showOpenDialog(LoadImg2);

    }//GEN-LAST:event_LoadImg2ActionPerformed

    private void NegativeImg1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NegativeImg1ActionPerformed
        img1.applyNegative();
        img1.updateImg();
        displayImage(img1.getImage());
    }//GEN-LAST:event_NegativeImg1ActionPerformed

    private void Negative2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Negative2ActionPerformed
        img2.applyNegative();
        img2.updateImg();
        displayImage(img2.getImage());
    }//GEN-LAST:event_Negative2ActionPerformed

    private void SumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SumaActionPerformed
        img1.add(img2);
        displayImage(img1.getImage());
    }//GEN-LAST:event_SumaActionPerformed

    private void Resta12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Resta12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Resta12ActionPerformed

    private void Save1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Save1ActionPerformed
        String fileName,type;
        fileName = JOptionPane.showInputDialog("File name: ");
        System.out.println("Se guarda el archivo: " + fileName);
        String[] aux = fileName.split("\\.");
//        String t="";
//        for(String a:aux){
//            t=a;
//        }
//        System.out.println("Se guarda el archivo: " + t);
//        
        type = aux[aux.length>0?aux.length-1:aux.length];
//        type = "png";
        try {
            // retrieve image
            BufferedImage bi = img1.getImage();
            File outputfile = new File("./" + fileName);
            ImageIO.write(bi, type, outputfile);
        } catch (IOException e) {
        }
    }//GEN-LAST:event_Save1ActionPerformed

    private void NormalizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NormalizeActionPerformed
        img1.normalize();
        displayImage(img1.getImage());
    }//GEN-LAST:event_NormalizeActionPerformed

    private void ShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowActionPerformed
        displayImage(img1.getImage());
    }//GEN-LAST:event_ShowActionPerformed

    public void displayImage(BufferedImage img) {
        JFrame frame = new JFrame();
        JLabel lblimage = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(lblimage, BorderLayout.CENTER);
        frame.setSize(img.getWidth(), img.getHeight());
        frame.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainJFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem LoadImg1;
    private javax.swing.JMenuItem LoadImg2;
    private javax.swing.JMenuItem Negative2;
    private javax.swing.JMenuItem NegativeImg1;
    private javax.swing.JMenuItem Normalize;
    private javax.swing.JMenuItem Resta12;
    private javax.swing.JMenuItem Resta21;
    private javax.swing.JMenuItem Save1;
    private javax.swing.JMenuItem Show;
    private javax.swing.JMenuItem Suma;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    // End of variables declaration//GEN-END:variables
}
