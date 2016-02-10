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
public class Graph {

    protected ArrayList<Node> nodes;
    

    public Graph() {
        this.nodes = new ArrayList<>();

    }

    public Node getNodeAt(int pos) {
        
        return this.nodes.get(pos);
    }
    
    public void addNode(Node node)
    {
        this.nodes.add(node);
    }

    public void generateProblem(int qtdnodes) {
        this.nodes.add(new Node(1, 0));
        for (int i = 1; i < qtdnodes; i++) {
            this.nodes.add(new Node(i + 1));
        }
    }
    
    public ArrayList<Node> getNodesValues()
    {
        ArrayList<Node> nodes=new  ArrayList<>();
        nodes.addAll(this.nodes);
        return nodes;
    }

    public boolean isInitialNode(Node node)
    {
        if(this.nodes.size() > 0)
            return (this.nodes.get(0)==node);
        return false;
    }
    
     public int getQtdNodes()
    {
        return this.nodes.size();
    }
}
