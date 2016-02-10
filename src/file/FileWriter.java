/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius
 */
public class FileWriter {

    private String filename;
    private Writer out;

    public FileWriter(String filename) {
        this.filename = filename;
        this.out = null;
    }

    public boolean openFile() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.filename), "UTF-8"));
            return true;
        } catch (Exception ex) {
            Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean closeFile() {
        try {
            out.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void writeLine(String aString) {
        if (this.out == null) {
            this.openFile();
        }
        
        try {
            out.write(aString);
            System.out.println(aString);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(FileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
