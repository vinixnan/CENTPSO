/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import file.FileWriter;
import graphtest.Chart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Vinicius
 */
public abstract class PSOBASE {

    protected final ArrayList<Particle> allparticles;
    protected VRP problem;
    protected double w1;
    protected double w2;
    protected final int qtdParticles;
    protected int qtdvehicles;
    protected int qtdnodes;
    protected int numberMaxIterations;
    protected int iteration;
    protected double c1;
    protected double c2;
    protected double c1min;
    protected double c2min;
    protected boolean simpleOutput;
    protected Chart chart;
    protected FileWriter outputfile;
    protected Particle bestSolution;

    public PSOBASE(VRP problem, int qtdParticles, int numberMaxIterations) {
        this.problem = problem;
        this.qtdvehicles = this.problem.getQtdVehicles();
        this.qtdParticles = qtdParticles;
        this.qtdnodes = this.problem.getQtdNodes();
        this.numberMaxIterations = numberMaxIterations;
        this.allparticles = new ArrayList<>();
        this.c1min = 2;
        this.c2min = 2;
        this.c1 = 0.9;
        this.c2 = 0.8;
        this.iteration = 0;
        this.w1 = 0.8;
        this.w2 = 0.9;
        //this.outputfile = new FileWriter("./" + this.qtdParticles + "_" + this.qtdnodes + "_" + this.qtdvehicles + "_.txt");
        this.simpleOutput = true;
        this.chart = null;
        this.bestSolution=null;
    }

    public boolean isSimpleOutput() {
        return simpleOutput;
    }

    public void setSimpleOutput(boolean simpleOutput) {
        this.simpleOutput = simpleOutput;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    protected void outputComplex(String str) {
        if (!this.simpleOutput) {
           // this.outputfile.writeLine(str);
        }
    }

    protected void outputSimple(String str) {
      //  this.outputfile.writeLine(str + "\n");
    }

    public int getQtdvehicles() {
        return qtdvehicles;
    }

    public void setQtdvehicles(int qtdvehicles) {
        this.qtdvehicles = qtdvehicles;
    }

    public int getQtdnodes() {
        return qtdnodes;
    }

    public void setQtdnodes(int qtdnodes) {
        this.qtdnodes = qtdnodes;
    }

    public void setC1(double c1) {
        this.c1 = c1;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }

    public void generateInitialSolution() {
        for (int i = 0; i < qtdParticles; i++) {
            Particle part = new Particle(i + 1);
            VehicleRoute sol;
            //ADD VEHICLES TO ROUTE
            for (int j = 0; j < qtdvehicles; j++) {
                sol = new VehicleRoute(this.problem.getDepot(), this.problem.getVehicleAt(j));
                //sol.addItemSolution(this.problem.getNodeAt(0)); //add 0 position
                part.addItemSolution(sol);
            }
            //SEPARATED TO GIVE ALMOST THE SAME SIZE TO EVERY ROUTE
            int idveic = 0;
            for (int k = 0; k < qtdnodes; k++) {
                sol = part.getCurrentSolution().get(idveic);
                Node node = part.getRandomNodeFrom(this.problem.getNodesValues());//getNodesValues IS A COPY
                if (node != null) {
                    sol.addItemSolution(node);
                }
                sol.calcCost();
                idveic++;
                if (idveic == this.qtdvehicles) {
                    idveic = 0;
                }
            }
            //ADD THE SAME BEGINNING TO THE ENDING
            /*
             for (int j = 0; j < qtdvehicles; j++) {
             sol=part.getCurrentSolution().get(j);
             sol.addItemSolution(this.problem.getNodeAt(0));
             }
             */
            part.generateRandomVelocity();
            part.updateBestPersonal();

            this.allparticles.add(part);
        }
    }

    public double getW1() {
        return w1;
    }

    public void setW1(double w1) {
        this.w1 = w1;
    }

    public double getW2() {
        return w2;
    }

    public void setW2(double w2) {
        this.w2 = w2;
    }

    public void drawParticle(Particle part) {

        if (this.chart != null) {
            chart.clear();
            chart.addRoots(this.qtdvehicles, this.problem.getDepot().getX_axis(), this.problem.getDepot().getX_axis());
            for (int i = 0; i < this.problem.getNodesValues().size(); i++) {
                Node n=this.problem.getNodesValues().get(i);
                chart.addValue(i + 1, n.getX_axis(), n.getY_axis());
            }
            for (int veiccod = 0; veiccod < part.getCurrentSolution().size(); veiccod++) {
                VehicleRoute vr = part.getCurrentSolution().get(veiccod);
                String depot = "Root" + String.valueOf(veiccod + 1);
                chart.addEdge(veiccod, depot, String.valueOf(vr.getSolutionPath().get(0).getIdclient()));
                for (int j = 1; j < vr.getSolutionPath().size() - 1; j++) {
                    chart.addEdge(veiccod, vr.getSolutionPath().get(j).getIdclient(), vr.getSolutionPath().get(j + 1).getIdclient());
                }
                chart.addEdge(veiccod, String.valueOf(vr.getSolutionPath().get(vr.getSolutionPath().size() - 1).getIdclient()), depot);
            }
            chart.pack();
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        }

    }

    protected int getWithNoRepeat(int[][] vet, ArrayList allowedArcs, int limit) {
        Random rdx = new Random();
        int ret = rdx.nextInt(this.qtdnodes - limit);
        int[] array0 = Arrays.copyOf(vet[0], vet[0].length);
        int[] array1 = Arrays.copyOf(vet[1], vet[1].length);
        Arrays.sort(array0);
        Arrays.sort(array1);
        while (Arrays.binarySearch(array0, ret) >= 0 || Arrays.binarySearch(array1, ret) >= 0 || !allowedArcs.contains(ret)) {
            ret = rdx.nextInt(this.qtdnodes - limit);
        }
        return ret;
    }

     
    protected double getC1() {
        return c1min + ((c1 - c1min) / this.numberMaxIterations) * iteration;
    }
    
    
    protected double getC2() {
        return c2min + ((c2 - c2min) / this.numberMaxIterations) * iteration;
    }

    protected void vnsSwap(Particle part, int[][] resp) {

        for (int i = 0; i < resp[0].length; i++) {
            int pos1 = resp[0][i];
            int pos2 = resp[1][i];
            Node node1 = part.getAtCurrentSolution(pos1);
            Node node2 = part.getAtCurrentSolution(pos2);
            outputComplex("\t\t\tSwapping nodes " + node1.toString() + " with " + node2.toString() + " from particle " + part.getIdparticle() + " via VNS");
            part.swapNodeLocation(node1, node2);
        }
    }

    public abstract String run();

    public Particle getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Particle bestSolution) {
        this.bestSolution = bestSolution;
    }
     
    
    
}
