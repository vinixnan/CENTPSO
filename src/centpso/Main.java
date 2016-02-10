/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centpso;

import file.DirectoryReader;
import file.VRPDirectoryReader;
import file.VRPFileReader;
import graphtest.Chart;
import org.jfree.ui.RefineryUtilities;
import src.CENTPSO;
import src.CNTPSO;
import src.DISCRETEPSO;
import src.Node;
import src.PSOBASE;
import src.VRP;

/**
 *
 * @author Vinicius
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DirectoryReader app;
        if (args.length == 5 && args[0].equalsIgnoreCase("d")) {
            app = new VRPDirectoryReader(Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), args[1]);
            app.run();
        } 
        else if (args.length >= 5 && args[0].equalsIgnoreCase("f")) 
        {
            PSOBASE pso;
            VRPFileReader fr = new VRPFileReader(args[1]);
            fr.readFile();
            if (fr.getReadtype() == 4) 
            {
                Chart demo;
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
                    Node n = new Node(i + 1, demand[i]);
                    n.setX_axis(coords[i][0]);
                    n.setY_axis(coords[i][1]);
                    problem.addNode(n);
                }
                switch (Integer.parseInt(args[2])) 
                {
                    case 1:
                        pso = new DISCRETEPSO(problem, Integer.valueOf(args[3]), Integer.valueOf(args[4]));
                        demo = new Chart("DISCRETEPSO "+args[1], "DISCRETEPSO");
                        break;
                    case 2:
                        pso = new CENTPSO(problem, Integer.valueOf(args[3]), Integer.valueOf(args[4]));
                        if(args.length==6)
                            ((CENTPSO)pso).setQtdIterationVNS(Integer.valueOf(args[5]));
                        demo = new Chart("CENTPSO "+args[1], "CENTPSO");
                        break;
                    case 3:
                        pso = new CNTPSO(problem, Integer.valueOf(args[3]), Integer.valueOf(args[4]));
                        if(args.length==6)
                            ((CNTPSO)pso).setQtdIterationVNS(Integer.valueOf(args[5]));
                        demo = new Chart("CNTPSO "+args[1], "CNTPSO");
                        break;
                    default:
                        pso = new CENTPSO(problem, Integer.valueOf(args[3]), Integer.valueOf(args[4]));
                        if(args.length==6)
                            ((CENTPSO)pso).setQtdIterationVNS(Integer.valueOf(args[5]));
                        demo = new Chart("CENTPSO "+args[1], "CENTPSO");
                        break;
                }
               
                pso.setSimpleOutput(true);
                //pso.setChart(demo);
                pso.run();
            }
        } 
        else 
        {
            app = new VRPDirectoryReader(80, 10, 5, "D:/A/");
            app.run();
        }
    }

}
