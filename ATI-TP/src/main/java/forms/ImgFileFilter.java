/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forms;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author maximo
 */
public class ImgFileFilter extends FileFilter{

    @Override
    public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".bmp")||f.getName().toLowerCase().endsWith(".png")||f.getName().toLowerCase().endsWith(".gif")||f.getName().toLowerCase().endsWith(".jpeg")||f.getName().toLowerCase().endsWith(".jpg")||f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "Imagen soportada."; //To change body of generated methods, choose Tools | Templates.
    }
    
}
