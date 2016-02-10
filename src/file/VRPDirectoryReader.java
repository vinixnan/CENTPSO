/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.CENTPSO;
import src.CNTPSO;
import src.DISCRETEPSO;
import src.Node;
import src.VRP;

/**
 *
 * @author Vinicius
 */
public class VRPDirectoryReader extends DirectoryReader {

    private int qtdParticles;
    private int qtdIteration;
    private int qtdVNSIteration;
    private FileWriter output;
    private FileWriter outputlog;

    public VRPDirectoryReader(int qtdParticles, int qtdIteration, int qtdVNSIteration, String dirpath) {
        super(dirpath);
        this.qtdParticles = qtdParticles;
        this.qtdIteration = qtdIteration;
        this.qtdVNSIteration = qtdVNSIteration;
        Date date = new Date(); 
        String name=dirpath+date.getTime()+"_outputFile";
        this.output=new FileWriter(name+".csv");
        this.outputlog=new FileWriter(name+".txt");
    }
   
    
    

    @Override
    protected void processFile(String strLine) {
        if (strLine.contains(".vrp")) {
            //demo.clear();
            System.out.println(this.dirpath + strLine);
            VRPFileReader fr = new VRPFileReader(this.dirpath + strLine);
            fr.readFile();
            if (fr.getReadtype() == 4) {
//                String resp="Executing file "+strLine+"\n";
                String resp=strLine+",";
                //this.output.writeLine(resp);
                this.outputlog.writeLine(strLine);
                resp+=this.execute(fr);
                resp+="\n";
                this.output.writeLine(resp);
                
            } else {
                System.err.println("Problems to read file: " + this.dirpath + strLine);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(VRPDirectoryReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String execute(VRPFileReader fr) {
        double[] posroot = fr.getDepotSection();
        double[] demand = fr.getDemands();
        double[][] coords = fr.getCoords();
        Node root = new Node(0, 0);
        root.setX_axis(posroot[0]);
        root.setY_axis(posroot[1]);
        VRP problem = new VRP();
        problem.setDepot(root);
        problem.addVehicle(fr.getQtdVehicle(), (int) fr.getCapacity());

        for (int i = 0; i < coords.length; i++) {
            // demo.addValue(i + 1, coords[i][0], coords[i][1]);
            Node n = new Node(i + 1, demand[i]);
            n.setX_axis(coords[i][0]);
            n.setY_axis(coords[i][1]);
            problem.addNode(n);
        }
        String resp="";
        
        this.outputlog.writeLine("\nDiscrete PSO Result;\n");
        DISCRETEPSO dpso=new DISCRETEPSO(problem, qtdParticles, qtdIteration);
        resp+=dpso.run();
        this.outputlog.writeLine(dpso.getBestSolution().toString());
        
        
        CENTPSO pso = new CENTPSO(problem, this.qtdParticles, this.qtdIteration);
        pso.setSimpleOutput(true);
        pso.setQtdIterationVNS(this.qtdVNSIteration);
        
        this.outputlog.writeLine("\nCENTPSO Result;\n");
        resp+=pso.run();
        this.outputlog.writeLine(pso.getBestSolution().toString());
        
        
        this.outputlog.writeLine("\nCNTPSO Result;\n");
        CNTPSO cnt=new CNTPSO(problem, this.qtdParticles, this.qtdIteration);
        cnt.setSimpleOutput(true);
        cnt.setQtdIterationVNS(qtdVNSIteration);
        resp+=cnt.run();
        this.outputlog.writeLine(cnt.getBestSolution().toString());
            
        return resp;
    }
}
