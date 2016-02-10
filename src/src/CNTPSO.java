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
public class CNTPSO extends CENTPSO {

   

    public CNTPSO(VRP problem, int qtdParticles, int numberMaxIterations) {
        super(problem, qtdParticles, numberMaxIterations);
        this.maxSwarmSize = 1;
        this.atualSwarmSize = this.qtdParticles;
    }

    
     protected void calcSpeed() {
        for (int i = 0; i < this.swarms.size(); i++) {
            Swarm s = this.swarms.get(i);
            s.calcSpeed(this.getC1(), this.getC2());
        }
    }
    
    
    @Override
    public String run() {

        this.prepareSwarm();
        //this.drawParticle(bestParticle);
        //this.drawParticle(bestParticle);//descobrir super best

        this.calcSpeed();
        for (this.iteration = 0; this.iteration < this.numberMaxIterations; this.iteration++) {
            //System.out.println("Iteration: " + iteration + " Using SWARM SIZE " + this.atualSwarmSize + "\n");
            Swarm s = this.swarms.get(0);
            this.processSwarm(s);
            s.updateBestSolution();
           // System.out.println("END PROCESS SWARM--------------------------\n");
            this.drawParticle(this.swarms.get(0).getBestParticle());
        }
        //System.out.println("\nFINISHED");
        
        this.bestSolution=this.swarms.get(0).getBestParticle();
        return this.swarms.get(0).getBestParticle().toStringReduced();
        
    }
}
