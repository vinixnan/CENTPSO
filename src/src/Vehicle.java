/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package src;

/**
 *
 * @author Vinicius
 */
public class Vehicle {
    private int idvehicle;
    private double atualcapacity;
    private double fullcapacity;

    public Vehicle(int idvehicle, double capacity) {
        this.idvehicle = idvehicle;
        this.fullcapacity = capacity;
        this.atualcapacity = capacity;
    }

    public void decrementCapacity(double decrement)
    {
        this.atualcapacity-=decrement;
    }
    
    public int getIdvehicle() {
        return idvehicle;
    }

    public void setIdvehicle(int idvehicle) {
        this.idvehicle = idvehicle;
    }

    public double getAtualcapacity() {
        return atualcapacity;
    }

    public void setAtualcapacity(double atualcapacity) {
        this.atualcapacity = atualcapacity;
    }

    public double getFullcapacity() {
        return fullcapacity;
    }

    public void setFullcapacity(double fullcapacity) {
        this.fullcapacity = fullcapacity;
    }
    
    
}
