/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package file;

import file.FileReader;

/**
 *
 * @author Vinicius
 */
public class VRPFileReader extends FileReader
{
    private String name;
    private String comment;
    private String type;
    private int dimension;
    private String weight_type;
    private double capacity;
    private int qtdVehicle;
    private double[][] coords; //coords[capacity][2]
    private double[] demands;//demands[capacity]
    private double[] depotSection; //depotSection[2]
    
    private int readtype;
    private int counter;
    
    public VRPFileReader(String filename) {
        super(filename);
        this.readtype=0;
        this.depotSection=new double[2];
    }
    
    @Override
    protected void processData(String strLine)
    {
        strLine=strLine.trim();
        if(this.readtype==0)
        {
            if(strLine.contains("NAME : "))
            {
                this.name=strLine.replace("NAME : ", "").trim();
            }
            else if(strLine.contains("COMMENT : "))
            {
                this.comment=strLine.replace("COMMENT : ", "").trim();
                int pos=strLine.indexOf("truck");
                if(pos >=0)
                {
                    strLine=strLine.substring(pos);
                    int pos2=strLine.indexOf(",");
                    if(pos2 >=0)
                    {
                        strLine=strLine.substring(0, pos2);
                        strLine=strLine.replace("truck", "");
                        strLine=strLine.replace("s", "");
                        strLine=strLine.replace(":", "").trim();
                        this.qtdVehicle=Integer.parseInt(strLine);
                    }
                   
                }
                 
            }
            else if(strLine.contains("EDGE_WEIGHT_TYPE : "))
            {
                this.weight_type=strLine.replace("EDGE_WEIGHT_TYPE : ", "").trim();
            }
            else if(strLine.contains("TYPE : "))
            {
                this.type=strLine.replace("TYPE : ", "").trim();
            }
          
            else if(strLine.contains("DIMENSION : "))
            {
                String aux=strLine.replace("DIMENSION : ", "").trim();
                this.dimension=Integer.parseInt(aux);
            }
            else if(strLine.contains("CAPACITY : "))
            {
                String aux=strLine.replace("CAPACITY : ", "").trim();
                this.capacity=Integer.parseInt(aux);
            }
            else if(strLine.equalsIgnoreCase("NODE_COORD_SECTION"))
            {
                this.readtype++;
                this.coords=new double[dimension][2];
                this.demands=new double[dimension];
                this.counter=0;
            }
        }
        else if(this.readtype==1)
        {
            if(!strLine.equalsIgnoreCase("DEMAND_SECTION"))
            {
                String[] coord=strLine.split(" ");
                int x=Integer.parseInt(coord[1]);
                int y=Integer.parseInt(coord[2]);
                this.coords[this.counter]=new double[2];
                this.coords[this.counter][0]=x;
                this.coords[this.counter][1]=y;
                this.counter++;
            }
            else
            {
                this.readtype++;
                this.counter=0;
            }
        }
        else if(this.readtype==2)
        {
            if(!strLine.equalsIgnoreCase("DEPOT_SECTION"))
            {
                String[] weights=strLine.split(" ");
                this.demands[this.counter]=Double.parseDouble(weights[1]);
                this.counter++;
            }
            else
            {
                this.readtype++;
                this.counter=0;
            }
        }
        else if(this.readtype==3)
        {
            if(!strLine.equalsIgnoreCase("EOF"))
            {
                this.depotSection[counter++]=Double.parseDouble(strLine);
            }
            else
            {
                this.readtype++;
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getReadtype() {
        return readtype;
    }

    public void setReadtype(int readtype) {
        this.readtype = readtype;
    }

    
    
    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public String getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(String weight_type) {
        this.weight_type = weight_type;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double[][] getCoords() {
        return coords;
    }

    public void setCoords(double[][] coords) {
        this.coords = coords;
    }

    public double[] getDemands() {
        return demands;
    }

    public void setDemands(double[] demands) {
        this.demands = demands;
    }

    public double[] getDepotSection() {
        return depotSection;
    }

    public void setDepotSection(double[] depotSection) {
        this.depotSection = depotSection;
    }

    public int getQtdVehicle() {
        return qtdVehicle;
    }

    public void setQtdVehicle(int qtdVehicle) {
        this.qtdVehicle = qtdVehicle;
    }
    
    
}
