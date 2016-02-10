/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Vinicius
 */
public class Particle implements Comparable{

    private int idparticle;
    private ArrayList<VehicleRoute> currentSolution;//one solution (vector) for each car 
    private ArrayList<VehicleRoute> bestPersonalSolution;
    private double[] velocity;//onde [current/personalbest][qtdvehicles]
     private double uBound;
    private double lBound;

    public Particle(int idparticle) {
        this.bestPersonalSolution = new ArrayList<VehicleRoute>();
        this.currentSolution = new ArrayList<VehicleRoute>();
        this.idparticle = idparticle;
        this.velocity=new double[this.getRealSize()];
        this.lBound = -4;
        this.uBound = 4;
    }

    public int getIdparticle() {
        return idparticle;
    }
    
    
    
    public void generateRandomVelocity() {
        Random rdx = new Random();
        this.velocity=new double[this.getRealSize()];
        for(int i=0; i < velocity.length; i++)
        {
            this.velocity[i] = rdx.nextInt(this.getRealSize());
        }
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        for(int i=0; i < velocity.length; i++)
        {
            if(velocity[i] < lBound)
                lBound=velocity[i];
            else if(velocity[i] > uBound)
                uBound=velocity[i];
        }
        this.velocity = velocity;
    }

    
   
    
    
    

    public void setIdparticle(int idparticle) {
        this.idparticle = idparticle;
    }

    public Particle clonar() {
        Particle part = new Particle(idparticle);
        for (VehicleRoute vrt : this.currentSolution) {
            part.addItemSolution(vrt.clonar());
        }
        for (VehicleRoute vrt : this.bestPersonalSolution) {
            part.getBestPersonalSolution().add(vrt.clonar());
        }
        double[] cpy;
        cpy=Arrays.copyOf(this.velocity, this.velocity.length);
        part.setVelocity(cpy);
        return part;
    }

    public void destruir() {
        this.idparticle = 0;
        for (VehicleRoute vr : this.bestPersonalSolution) {
            vr.destruir();
        }
        for (VehicleRoute vr : this.currentSolution) {
            vr.destruir();
        }
    }

    //metodo criado para simplificar acesso a Nos
    private Node getAt(int pos, ArrayList<VehicleRoute> solution) {
        Node aux = null;
        int i = 0;
        while (i < solution.size() && solution.get(i).getSolutionPath().size() <= pos) {
            pos -= solution.get(i).getSolutionPath().size();
            i++;
        }
        if (i < solution.size()) {
            aux = solution.get(i).getSolutionPath().get(pos);
        }
        return aux;
    }
    
    private int getByIdNode(int idnode, ArrayList<VehicleRoute> solution) {
        int pos=-1;
        for(int i=0; i < solution.size(); i++)
        {
            VehicleRoute vr=solution.get(i);
            for(int j=0; j < vr.getSolutionPath().size(); j++)
            {
                pos++;
                Node n=vr.getSolutionPath().get(j);
                if(n.getIdclient()==idnode)
                    return pos;
            }
        }
        return pos;
    }

    public int getByIdNodeCurrentSolution(int idnode) {
        return this.getByIdNode(idnode, currentSolution);
    }

    public int getByIdNodePersonalBestSolution(int idnode) {
        return this.getByIdNode(idnode, bestPersonalSolution);
    }
    
    public Node getAtCurrentSolution(int pos) {
        return this.getAt(pos, currentSolution);
    }

    public Node getAtPersonalBestSolution(int pos) {
        return this.getAt(pos, bestPersonalSolution);
    }

    public void swapNodeLocation(Node node1, Node node2) {
        int[] posnode1 = this.locateNode(node1);
        int[] posnode2 = this.locateNode(node2);
        if (posnode1[0] != -1 && posnode1[1] != -1 && posnode2[0] != -1 && posnode2[1] != -1) {
            this.currentSolution.get(posnode1[0]).setNodeinPos(posnode1[1], node2);
            this.currentSolution.get(posnode2[0]).setNodeinPos(posnode2[1], node1);

        } else {
            //System.out.println("\nPROBLEMAS---------------------------------\n");
        }
    }

    public void updateBestPersonal() {
        
        if (this.fitnessCurrentSolution() < this.fitnessPersonalBestSolution() || this.fitnessPersonalBestSolution() == 0) {
            //System.out.println("\nUpdating Best Personal\n");
            this.bestPersonalSolution.clear();
            for (VehicleRoute vrt : this.currentSolution) {
                this.bestPersonalSolution.add(vrt.clonar());
            }

        }
    }

    private int[] locateNode(Node node) {
        int pos = -1;
        int i = 0;
        for (i = 0; i < this.currentSolution.size() && pos == -1; i++) {
            pos = this.currentSolution.get(i).getNodePosition(node);
        }
        int[] resp = new int[2];
        resp[0] = i - 1;
        resp[1] = pos;
        return resp;
    }

    public void addItemSolution(VehicleRoute sol) {
        this.currentSolution.add(sol);
    }

    public boolean isNodeVisited(Node node) {
        boolean resp = false;
        for (int i = 0; i < this.currentSolution.size() && resp == false; i++) {
            resp = this.currentSolution.get(i).isNodeVisited(node);
        }
        return resp;
    }

    public int getRealSize() {
        int soma = 0;
        for (int i = 0; i < this.currentSolution.size(); i++) {
            soma += this.currentSolution.get(i).getSolutionPath().size();
        }
        //soma-=2;//-2 é deposito
        return soma;
    }

    public ArrayList<VehicleRoute> getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(ArrayList<VehicleRoute> currentSolution) {
        this.currentSolution = currentSolution;
    }

    public Node getRandomNodeFrom(ArrayList<Node> elements) {
        elements = this.removeVisited(elements);
        if (elements.size() > 0) {
            Random rdx = new Random();
            int r = rdx.nextInt(elements.size());
            return elements.get(r);
        }
        return null;
    }

    private ArrayList<Node> removeVisited(ArrayList<Node> elements) {
        int i = 0;
        while (i < elements.size()) {
            Node node = elements.get(i);
            if (this.isNodeVisited(node)) {
                elements.remove(i);
            } else {
                i++;
            }
        }
        return elements;
    }

    public double fitnessCurrentSolution() {
        double fit = 0;
        for (VehicleRoute sol : this.currentSolution) {
            fit += sol.getCost();
        }
        return fit;
    }

    public double fitnessCurrentSolutionWDep() {
        double fit = 0;
        for (VehicleRoute sol : this.currentSolution) {
            fit += sol.getCostWDep();
        }
        return fit;
    }
    
    public double fitnessPersonalBestSolution() {
        double fit = 0;
        for (VehicleRoute sol : this.bestPersonalSolution) {
            fit += sol.getCost();
        }
        return fit;
    }

    public ArrayList<VehicleRoute> getBestPersonalSolution() {
        return bestPersonalSolution;
    }

    public void setBestPersonalSolution(ArrayList<VehicleRoute> bestPersonalSolution) {
        this.bestPersonalSolution = bestPersonalSolution;
    }

    @Override
    public String toString() {
        
        String aux = "Particle " + this.idparticle + " {" + "currentSolution=" + Arrays.toString(currentSolution.toArray()) + "\n}";
        aux += "\nFitness Personal: " + this.fitnessCurrentSolution();
        aux += "\nFitness Validation: " + this.calcCostValidate(true);
        aux += "\nFitness Personal Without Depot Distance: " + this.fitnessCurrentSolutionWDep();
        aux += "\nFitness Validation Without Depot Distance: " + this.calcCostValidate(false);
        aux += "\nFitness Best Personal: " + this.fitnessPersonalBestSolution();
        aux += "\nVelocity: " + Arrays.toString(this.velocity);
        aux+="\nUbound: "+this.uBound+", Lbound: "+this.lBound+"\n\n";
        return aux;
    }
    
     public String toStringReduced() {
        
        String aux =String.valueOf(this.fitnessCurrentSolutionWDep())+",";
        return aux;
    }

    public double getuBound() {
        return uBound;
    }

    public void setuBound(double uBound) {
        this.uBound = uBound;
    }

    public double getlBound() {
        return lBound;
    }

    public void setlBound(double lBound) {
        this.lBound = lBound;
    }
    
    public String calcCostValidate(boolean withDepot)
    {
        double demand=0;
        double distance=0;
        for(VehicleRoute vr : this.currentSolution)
        {
            double[] aux=vr.calcCostValidate(withDepot);
            distance+=aux[0];
            demand+=aux[1];
        }
        double mediadistance=distance/this.currentSolution.size();
        double mediademand=demand/this.currentSolution.size();
        return "Distance: "+distance+" Demand: "+demand+" Media Distance: "+mediadistance+" Media Demand "+mediademand;
    }

    @Override
    public int compareTo(Object o) {
        if(this.fitnessCurrentSolution() < ((Particle)o).fitnessCurrentSolution())
            return 0;
        else
            return 1;
    }
}
