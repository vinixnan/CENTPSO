/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src;

import java.util.Random;

/**
 *
 * @author Vinicius
 */
public class Node {
    private int idclient;
    private double x_axis;
    private double y_axis;
    private double demand;
    

    public Node(int idclient) {
        this.idclient = idclient;
        this.generateData();
    }

    public Node(int idclient, double demand) {
        this.idclient = idclient;
        this.demand = demand;
    }

    public double getX_axis() {
        return x_axis;
    }

    public void setX_axis(double x_axis) {
        this.x_axis = x_axis;
    }

    public double getY_axis() {
        return y_axis;
    }

    public void setY_axis(double y_axis) {
        this.y_axis = y_axis;
    }
    
    
    
    private void generateData()
    {
        Random rdx=(new Random());
        this.demand=rdx.nextDouble()*100;
        this.x_axis=rdx.nextDouble()*100;
        this.y_axis=rdx.nextDouble()*100;
    }
    
    public int getIdclient() {
        return idclient;
    }

    public void setIdclient(int idclient) {
        this.idclient = idclient;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }
    
    public double calcDistanceTo(Node destiny)
    {
        double dist=Math.sqrt(Math.pow(destiny.getX_axis()-this.getX_axis(), 2)+Math.pow(destiny.getY_axis()-this.getY_axis(), 2));
        return dist;
    }
    
   
    
    @Override
    public String toString() {
        String aux=String.valueOf(idclient);
       // aux+="\n"+this.x_axis+", "+this.y_axis;
       // aux+="\n"+this.demand;
        return aux;
    }
}
