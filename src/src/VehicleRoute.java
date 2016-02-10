/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius
 */
public class VehicleRoute{
    private Vehicle vehicle;//vehicle used in this solution
    private ArrayList<Node> solutionPath;
    private Node depot;
    
    
    private double cost;

    public VehicleRoute(Node depot, Vehicle veihicle) {
        this.vehicle = veihicle;
        this.depot=depot;
        this.solutionPath=new ArrayList<>();
        this.cost=0;
    }
    
    public void destruir()
    {
        this.cost=0;
        this.vehicle=null;
        this.solutionPath=new ArrayList<>();
    }
    
    public VehicleRoute clonar() {
       VehicleRoute vr=new VehicleRoute(this.depot, vehicle);
       vr.setCost(cost);
       for(Node n: this.solutionPath)
       {
           vr.addItemSolution(n);
       }
       return vr;
    }

    

    public VehicleRoute(Vehicle veihicle, ArrayList<Node> solutionPath) {
        this.vehicle = veihicle;
        this.solutionPath = solutionPath;
    }

    public void addItemSolution(Node node)
    {
        this.solutionPath.add(node);
        //this.veihicle.decrementCapacity(node.getDemand());
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    public void setNodeinPos(int pos, Node node)
    {
        this.solutionPath.set(pos, node);
    }
    
    public int getNodePosition(Node node)
    {
        return this.solutionPath.indexOf(node);
    }

    public boolean isNodeVisited(Node node)
    {
        return this.solutionPath.contains(node);
    }

    public ArrayList<Node> getSolutionPath() {
        return solutionPath;
    }

    public void setSolutionPath(ArrayList<Node> solutionPath) {
        this.solutionPath = solutionPath;
    }
    
    public Node getLastElement()
    {
        if(this.solutionPath.size() > 0)
            return this.solutionPath.get(this.solutionPath.size()-1);
        return null;
    }
    
    public void removeLastElement()
    {
        Node aux=this.getLastElement();
        if(aux!=null)
            this.solutionPath.remove(aux);
    }

    public double getCost() {
        cost=this.calcCost();
        cost+=this.depot.calcDistanceTo(this.solutionPath.get(0));
        cost+=this.getLastElement().calcDistanceTo(this.depot);
        return cost;
    }
    
     public double getCostWDep() {
        return this.calcCost();
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

   
    
    private double Fpj(double q, int i)
    {
        double distance=0;
        if(i < this.solutionPath.size()-1)
        {
            Node origin=this.solutionPath.get(i);
            Node destiny=this.solutionPath.get(i+1);
            double origindestiny=origin.calcDistanceTo(destiny);
            if(q > destiny.getDemand())
            {
                q-=destiny.getDemand();
                distance+=origindestiny;
            }
            else
            {
                double depottodestiny=this.depot.calcDistanceTo(destiny);
                double origintodepot=origin.calcDistanceTo(this.depot);
                double destinydepot=destiny.calcDistanceTo(this.depot);
                double distanceBacking=depottodestiny+origintodepot;//origin->depot->destiny
                double distanceGoing=depottodestiny+destinydepot+origindestiny;//origin->destiny->depot->destiny
                if(distanceBacking < distanceGoing)
                {
                    distance+=distanceBacking;
                }
                else
                {
                    distance+=distanceGoing;
                }
                q=vehicle.getFullcapacity()+q-destiny.getDemand();
            }
            distance+=this.Fpj(q, i+1);
        }
        return distance;
    }
    
    private double Fj(double q, int i)
    {
        double distance=0;
        Node origin=this.solutionPath.get(i);
        q-=origin.getDemand();
        distance+=this.Fpj(q,i);
        return distance;
    }
    
    public double calcCost()
    {
        return this.Fj(vehicle.getFullcapacity(), 0);
    }
    
    public double[] calcCostValidate(boolean  withDepot)
    {
        double distance=0;
        double demand=0;
        for(int i=0; i < this.solutionPath.size()-1; i++)
        {
            Node origin=this.solutionPath.get(i);
            Node destiny=this.solutionPath.get(i+1);
            distance+=origin.calcDistanceTo(destiny);
            demand+=origin.getDemand();
        }
        if(this.solutionPath.size() > 0)
        {
           Node node=this.solutionPath.get(this.solutionPath.size()-1);
           demand+= node.getDemand();
           if(withDepot)
           {
                distance+=this.depot.calcDistanceTo(this.solutionPath.get(0));
                distance+=this.getLastElement().calcDistanceTo(this.depot);
           }
        }
        double[] ret=new double[2];
        ret[0]=distance;
        ret[1]=demand;
        return ret;   
    }
    
    @Override
    public String toString() {
        this.getCost();
        String path="";
        String virg="";
        for(Node node:solutionPath)
        {
            path+=virg+node.toString();
            virg=", ";
        }
        double aux[]=this.calcCostValidate(true);
        return "\n\tSolution{" + "veihicle=" + vehicle.getIdvehicle() + ", solutionPath=[" + path + "]} "+"Distance: "+cost+" Demand: "+aux[1]+" Comum Distance "+aux[0];
    }
    
    
}
