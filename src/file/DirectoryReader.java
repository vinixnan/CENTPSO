/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.File;

/**
 *
 * @author Vinicius
 */
public class DirectoryReader {

    protected String dirpath;

    public DirectoryReader(String dirpath) {
        this.dirpath = dirpath;
    }
    
    public String getDirpath() {
        return dirpath;
    }

    public void setDirpath(String dirpath) {
        this.dirpath = dirpath;
    }

    
    
    public void run() {
        File folder = new File(this.dirpath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                this.processFile(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                this.processDirectory(listOfFiles[i].getName());
            }
        }
    }
    
    protected void processFile(String strLine)
    {
        System.out.println("File "+strLine);
    }
    
    protected void processDirectory(String strLine)
    {
        System.out.println("Directory "+strLine);
    } 
}
