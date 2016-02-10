/*
	mod
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Vinicius
 */
public class CENTPSO extends PSOBASE {
    
    
    protected int qtdIterationVNS;
    protected final ArrayList<Swarm> swarms;
    //protected double[][] velocity;//onde [particula][solucao][no]
    protected int atualSwarmSize;
    protected int minSwarmSize;
    protected int maxSwarmSize;
    protected int incrementSwarmSize;
    protected int qtdSwarm;
    
    
    public CENTPSO(VRP problem, int qtdParticles, int numberMaxIterations) {
        super(problem, qtdParticles, numberMaxIterations);
        this.swarms = new ArrayList<>();
        
        this.qtdIterationVNS = 5;
        this.minSwarmSize = 3;
        this.maxSwarmSize = this.qtdParticles;
        this.incrementSwarmSize = 3;
        this.atualSwarmSize = this.minSwarmSize;
        this.qtdSwarm=this.qtdParticles/4;
    }
    
    public int getQtdIterationVNS() {
        return qtdIterationVNS;
    }
    
    public void setQtdIterationVNS(int qtdIterationVNS) {
        this.qtdIterationVNS = qtdIterationVNS;
    }
    
    public double getC1min() {
        return c1min;
    }
    
    public void setC1min(double c1min) {
        this.c1min = c1min;
    }
    
    public double getC2min() {
        return c2min;
    }
    
    public void setC2min(double c2min) {
        this.c2min = c2min;
    }
    
  
    
    public double L1(double uBound, double lBound) {
        return (uBound - lBound) * (w1 - (w1 / numberMaxIterations) * iteration) + lBound;
    }
    
    public double L2(double uBound, double lBound) {
        return (uBound - lBound) * (w2 - (w2 / (2 * numberMaxIterations)) * iteration) + lBound;
    }
    
    public int[][] Opt(ArrayList allowedArcs, int qtd) {
        qtd = qtd * 2;// * 2 por conta de um arco conter 2 nos, 
        int[][] resp = new int[2][qtd];//0 é o que será removido e 1 o que será adicionado
        int max = qtd - 1;
        for (int i = 0; i < max; i++) {
            resp[0][i++] = this.getWithNoRepeat(resp, allowedArcs, 2);
            resp[0][i] = resp[0][i - 1] + 1;
        }
        for (int i = 0; i < max; i++) {
            resp[1][i++] = this.getWithNoRepeat(resp, allowedArcs, 1);
            resp[1][i] = this.getWithNoRepeat(resp, allowedArcs, 1);
        }
        return resp;
    }
    
    public int[][] Opt2(ArrayList allowedArcs) {
        //retorna POSICOES, E nao nos reais
        outputComplex("\t2-OPT");
        int[][] resp = this.Opt(allowedArcs, 2);

        //outputSimple(Arrays.toString(resp[0]));
        //  outputSimple(Arrays.toString(resp[1]));
        return resp;
    }
    
    public int[][] Opt3(ArrayList allowedArcs) {
        outputComplex("\t3-OPT");
        int[][] resp = this.Opt(allowedArcs, 3);
        return resp;
    }
    
    private Particle vns(Particle part, ArrayList allowedArcs) {
        Particle bestsearch = part.clonar();
        outputComplex("\tSTARTING VARIABLE NEIGHBORHOOD SEARCH");
        for (int j = 0; j < this.qtdIterationVNS; j++) {
            outputComplex("\n\tLOCAL SEARCH " + j);
            if (allowedArcs.size() >= 8) {//4 for 2 arcs (X,X), (X,X) and 4 to swap arcs (X,X), (X,X)
                int[][] respOpt2 = this.Opt2(allowedArcs);
                Particle partOpt2 = part.clonar();
                this.vnsSwap(partOpt2, respOpt2);
                if (partOpt2.fitnessCurrentSolution() < bestsearch.fitnessCurrentSolution()) {
                    outputComplex("\t\t\tThe last swap made it better");
                    bestsearch.destruir();
                    bestsearch = partOpt2;
                }
            }
            if (allowedArcs.size() >= 12) { //6 for 3 arcs (X,X), (X,X), (X,X) and 6 to swap arcs (X,X), (X,X), (X,X)
                int[][] respOpt3 = this.Opt3(allowedArcs);
                Particle partOpt3 = part.clonar();
                this.vnsSwap(partOpt3, respOpt3);
                if (partOpt3.fitnessCurrentSolution() < bestsearch.fitnessCurrentSolution()) {
                    outputComplex("\t\t\tThe last swap made it better");
                    bestsearch.destruir();
                    bestsearch = partOpt3;
                }
            }
        }
        outputComplex("\tEND VNS--------------------------\n");
        if (bestsearch != null) {
            outputComplex("NOVO " + bestsearch.toString());
        }
        
        return bestsearch;
    }
    
    private void pathRelinking(Particle part, Node node1, Node node2) {
        if (node1 != node2) {
            outputComplex("\t\t\tSwapping nodes " + node1.toString() + " with " + node2.toString() + " from particle " + part.getIdparticle() + " via Path Relinkink");
            part.swapNodeLocation(node1, node2);
        }
    }
    
    protected Particle moviment(Particle part, Particle bestParticle, int pos, double avg) {
        
        double l1 = this.L1(part.getuBound(), part.getlBound());
        double l2 = this.L2(part.getuBound(), part.getlBound());
        outputComplex("L1: " + l1 + ", L2: " + l2 + ", AVG: " + avg);
        ArrayList<Integer> allowedArcstoVNS = new ArrayList<>();
        for (int i = 0; i < part.getRealSize(); i++) {//i=1 && < reallsize -1 para remover deposito
            //desta forma a particula inteira segue a soluucao
            if (avg < l1) {
                allowedArcstoVNS.add(i);
            } else if (avg >= l1 && avg <= l2) {
                outputComplex("PR Following Personal Best");
                Node nodeParticle = part.getAtCurrentSolution(i);
                Node nodeParticlePersonalBest = part.getAtPersonalBestSolution(i);
                //usando essa metodologia seria apenas fazer copia da particula global
                pathRelinking(part, nodeParticle, nodeParticlePersonalBest);
            } else {
                outputComplex("PR Following Local Best");
                Node nodeParticle = part.getAtCurrentSolution(i);
                Node nodePersonalBest = bestParticle.getAtCurrentSolution(i);
                //usando essa metodologia seria apenas fazer copia da particula best local
                pathRelinking(part, nodeParticle, nodePersonalBest);
            }
        }
        if (allowedArcstoVNS.size() > 1)//  >1 para ter 1 troca ao menos, >=4 tem 1 2-opt, >=6 tem 1 3-opt
        {
            outputComplex("\tItens allowed to VNS " + allowedArcstoVNS.toString());
            part = vns(part, allowedArcstoVNS);
        }
        return part;
    }
    
    protected void prepareSwarm() {
        this.generateInitialSolution();
        for (int i = 0; i < this.qtdSwarm; i++) {
            int posinicial=i;
            int half=this.atualSwarmSize/2;
            if(posinicial-half >=0)
                posinicial=posinicial-half;
            int posfinal = posinicial+this.atualSwarmSize;
            if(posfinal >= this.qtdParticles)
                posfinal=this.qtdParticles-1;
            ArrayList<Particle> aux = new ArrayList<>(allparticles.subList(posinicial, posfinal));
            Swarm s = new Swarm(aux, qtdnodes);
            s.updateBestSolution();
            this.swarms.add(s);
        }
    }
    
    private void reallocSwarm() {
        for (int i = 0; i < this.swarms.size(); i++) {
            int posinicial=i;
            int half=this.atualSwarmSize/2;
            if(posinicial-half >=0)
                posinicial=posinicial-half;
            int posfinal = posinicial+this.atualSwarmSize;
            if(posfinal >= this.qtdParticles)
                posfinal=this.qtdParticles-1;
            ArrayList<Particle> aux = new ArrayList<>(allparticles.subList(posinicial, posfinal));
            Swarm s = this.swarms.get(i);
            s.setParticles(aux);
            s.updateBestSolution();
        }
    }
    
   
    
    protected void processSwarm(Swarm s) {
        ArrayList<Particle> parts = s.getParticles();
        double swarmaverage = s.average();
        for (int i = 0; i < parts.size(); i++) {
            outputSimple("Process Particle #" + String.valueOf(i + 1));
            Particle part = parts.get(i);
            outputComplex("Before process particle " + part.toString());
            part = this.moviment(part, s.bestParticle, i, swarmaverage);
            if (part != null) {
                parts.set(i, part);
                part.updateBestPersonal();
            }
            outputComplex("After process particle " + part.toString());
            // outputComplex(part.toString());
            outputSimple("END PROCESS PARTICLE--------------------------\n");
        }
        s.calcSpeed(this.getC1(), this.getC2());
    }
    
    @Override
    public String run() {
        this.prepareSwarm();
        for (this.iteration = 0; this.iteration < this.numberMaxIterations; this.iteration++) {
            outputSimple("Iteration: " + iteration + " Using SWARM SIZE " + this.atualSwarmSize + "\n");
            for (int i = 0; i < this.swarms.size(); i++) {
                Swarm s = this.swarms.get(i);
                outputSimple("Process Swarm #" + String.valueOf(i + 1));
                this.processSwarm(s);
                s.updateBestSolution();//
                outputSimple("END PROCESS SWARM--------------------------\n");
            }
            //take the global best
            this.bestSolution=this.swarms.get(0).getBestParticle().clonar();
            for (int i = 1; i < this.swarms.size(); i++) {
                Particle p=this.swarms.get(i).getBestParticle();
                if(p.fitnessCurrentSolution() < this.bestSolution.fitnessCurrentSolution())
                {
                    this.bestSolution=p.clonar();
                }
            }
            if (bestSolution != null) {
                this.drawParticle(bestSolution);
            }
            this.atualSwarmSize+=this.incrementSwarmSize;
            if (this.atualSwarmSize == this.maxSwarmSize + 1) {
                this.atualSwarmSize = this.minSwarmSize;//min
            }
            this.reallocSwarm();
        }
        
        outputSimple("FINISHED");
        
        //System.out.println(this.bestSolution.toString());
        return this.bestSolution.toStringReduced();
    }
}
