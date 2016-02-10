/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;

/**
 *
 * @author Vinicius
 */
public class VRP extends Graph {

    protected ArrayList<Vehicle> vehicles;
    protected Node depot;

    public VRP() {
        this.vehicles = new ArrayList<>();
        this.depot=null;
    }

    public Node getDepot() {
        return depot;
    }

    public void setDepot(Node depot) {
        this.depot = depot;
    }

    public int getQtdVehicles()
    {
        return this.vehicles.size();
    }
    
    
    public void generateProblem(int qtdvehicles, int qtdnodes, int maxCarriage) {
        this.generateProblem(qtdnodes);
        this.addVehicle(qtdvehicles, maxCarriage);
    }
    
    public void addVehicle(int qtdvehicles, int maxCarriage)
    {
        for (int i = 0; i < qtdvehicles; i++) {
            this.vehicles.add(new Vehicle(i + 1, maxCarriage));
        }
    }
    
    public Vehicle getVehicleAt(int pos)
    {
        return this.vehicles.get(pos);
    }
    
    public void addVehicle(Vehicle vehicle)
    {
        this.vehicles.add(vehicle);
    }
    
    public int getAllowedRandomVehicle()
    {
        int idvehicle=0;
        double min=Double.MIN_VALUE;
        for(int i=1; i < this.vehicles.size(); i++)
        {
            if(this.vehicles.get(i).getAtualcapacity() > min)
            {
                min=this.vehicles.get(i).getAtualcapacity();
                idvehicle=i;
            }
        }
        return idvehicle;
    }
}
