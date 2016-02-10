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
public class Swarm {
    
    protected  ArrayList<Particle> particles;
    protected Particle bestParticle;
    
    protected int qtdnodes;
    protected int qtdParticles;

    public Swarm(ArrayList<Particle> particles, int qtdnodes) {
        this.particles=particles;
        this.qtdParticles=this.particles.size();
        this.bestParticle=null;
        this.qtdnodes=qtdnodes;
       
    }
    
    
    
    public void updateBestSolution() {
        double bestfitness;
        if (bestParticle == null) {
            bestfitness = Double.MAX_VALUE;
        } else {
            bestfitness = bestParticle.fitnessCurrentSolution();
        }
        for (Particle part : this.particles) {
            if (part.fitnessCurrentSolution() < bestfitness) {
                if (bestParticle != null) {
                    bestParticle.destruir();
                }
                bestParticle = part.clonar();
                bestfitness = part.fitnessCurrentSolution();
            }
        }
       // System.out.println(bestParticle.toString());
    }
    
    
    
    public void destruir()
    {
        this.particles.clear();
        this.bestParticle.destruir();
    }
    
    @Override
    public String toString() {
        String str = "Swarm with "+this.qtdParticles;
        for (Particle part : this.particles) {
            str += part.toString() + "\n\n";
        }
        return str;
    }
    
    protected void calcSpeed(double c1, double  c2) {
        Random rdx = new Random();
        for (int i = 0; i < this.particles.size(); i++) {
            Particle part = this.particles.get(i);
            Particle localbest=this.bestParticle;
            double[] velocity=part.getVelocity();
            for (int k = 0; k < velocity.length; k++) {
                Node node  = part.getAtCurrentSolution(k);
                Node pbest = part.getAtPersonalBestSolution(k);
                Node lbest = localbest.getAtCurrentSolution(k);
                velocity[k] = velocity[k] + this.fx(c1,c2)*(node.getIdclient() + c1 * rdx.nextDouble() * (pbest.getIdclient()-node.getIdclient()) + c2 * rdx.nextDouble() * (lbest.getIdclient()-node.getIdclient()));
            }
            part.setVelocity(velocity);
        }
    }

    private double fx(double c1, double  c2)
    {
        double ret=1;
        double c=c1+c2;
        if(c > 4)
        {
            ret=2/Math.abs(2-c-Math.sqrt(Math.pow(c, 2)-4*c));
        }
        return ret;
    }
    
    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void setParticles(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    public Particle getBestParticle() {
        return bestParticle;
    }

    public void setBestParticle(Particle bestParticle) {
        this.bestParticle = bestParticle;
    }

  
    
    private double average(double[][] velocitySolution) {
        double avg = 0;
        for(int j=0; j < velocitySolution.length; j++)
        {
            for (int i = 0; i < velocitySolution[j].length; i++) {
                avg += velocitySolution[j][i];
            }
        }
        avg = avg / (velocitySolution.length * velocitySolution[0].length);
        return avg;
    }
    
    public double average() {
        double[][] velocities=new double[this.particles.size()][qtdnodes];
        for(int i=0; i < this.particles.size(); i++)
        {
            Particle part=this.getParticles().get(i);
            velocities[i]=part.getVelocity();
        }
        return this.average(velocities);
    }
}
