/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.omg.CORBA.INTERNAL;

/**
 *
 * @author Vinicius
 */
public class DISCRETEPSO extends PSOBASE {

    private final ArrayList<int[][]> velocity;
    

    public DISCRETEPSO(VRP problem, int qtdParticles, int numberMaxIterations) {
        super(problem, qtdParticles, numberMaxIterations);
        this.velocity = new ArrayList<>();
        this.w1=0.2;
    }

    private void initVelocity() {
        Random rdx = new Random();
        for (int i = 0; i < this.allparticles.size(); i++) {
            int[][] change = new int[this.qtdnodes][2];
            for(int j=0; j < this.qtdnodes; j++)
            {
                change[j][0] = rdx.nextInt(this.qtdnodes);
                change[j][1] = rdx.nextInt(this.qtdnodes);
            }
            this.velocity.add(change);
        }
    }
    
    private int[][] coeficientTimesPos(double coeficient, int[][] pos)
    {
        if(coeficient < 0)
            coeficient=0;
        else if(coeficient > 1)
            coeficient=coeficient-1;
        int qtdarray=(int) Math.floor(this.qtdnodes*coeficient);//ver
        if(qtdarray > pos.length)
            qtdarray=pos.length;
        int[][] ret=new int[qtdarray][2];
        System.arraycopy(pos, 0, ret, 0, qtdarray);
        return ret;
    }
    
    private int[][] positionPlusPosition(int[][] first, int[][] second)
    {
        int[][] resp=new int[first.length+second.length][2];
        int i;
        for(i=0; i < first.length && i <resp.length; i++)
        {
            resp[i]=first[i];
        }
        for(int j=0; j < second.length && i <resp.length; j++ )
        {
            resp[i]=second[j];
            i++;
        }
        return resp;
    }
    
   
    
    private void recalcVelocity() {
        //obtem a velocidade apenas no for dividindo em 2 vetores
        //usa o coefieciente para calcular o percentual do array que vai ser assim para cada gerando 2 novos arrays
        //a adicao com velocidade anterior ja vai estar pronta devido a usarmos vetor de velocidade completo? ´´e bom usar inercia, usar fator de contricao e outro fator
        Random rdx=new Random();
        for (int i = 0; i < this.velocity.size(); i++) {
            int[][] atualspeed = this.velocity.get(i);
            int[][] personalbestspeed=new int[this.qtdnodes][2];
            int[][] globalbestspeed=new int[this.qtdnodes][2];
            Particle part=this.allparticles.get(i);
            double coeficientC1=this.getC1()*rdx.nextDouble();
            double coeficientC2=this.getC2()*rdx.nextDouble();
            double coeficientW=this.w1*rdx.nextDouble();
            for(int pospart=0; pospart < this.qtdnodes; pospart++)
            {
                Node ax=part.getAtCurrentSolution(pospart);
                int posBest=this.bestSolution.getByIdNodeCurrentSolution(ax.getIdclient());
                int poslocalbest=part.getByIdNodePersonalBestSolution(pospart);
                personalbestspeed[pospart][0] = pospart;
                personalbestspeed[pospart][1] = poslocalbest;
                globalbestspeed[pospart][0] = pospart;
                globalbestspeed[pospart][1] = posBest;
            }
            atualspeed=this.coeficientTimesPos(coeficientW, atualspeed);
            personalbestspeed=this.coeficientTimesPos(coeficientC1, personalbestspeed);
            globalbestspeed=this.coeficientTimesPos(coeficientC2, globalbestspeed);
            int[][] change=this.positionPlusPosition(atualspeed, this.positionPlusPosition(personalbestspeed, globalbestspeed));
            this.velocity.set(i, change);        
        }
    }
    
    private int[][] convertArray(int[][] input)
    {
        int[][] ret=new int[2][input.length];
        for(int i=0; i < input.length; i++)
        {
            ret[0][i]=input[i][0];
            ret[1][i]=input[i][1];
        }
        return ret;
    }
    
    private void updateBest()
    {
        for(Particle p : this.allparticles)
        {
            if(this.bestSolution==null)
                this.bestSolution=p.clonar();
            else if(p.fitnessCurrentSolution() < this.bestSolution.fitnessCurrentSolution())
            {
                this.bestSolution.destruir();
                this.bestSolution=p.clonar();
            }
        }
    }
   
    @Override
    public String run() {
        this.generateInitialSolution();
        this.initVelocity();
        this.updateBest();
        for (this.iteration = 0; this.iteration < this.numberMaxIterations; this.iteration++) {
            outputSimple("Iteration: " + iteration);
            for(int i=0; i < this.allparticles.size(); i++)
            {
                Particle part=this.allparticles.get(i);
                this.vnsSwap(part, this.convertArray(this.velocity.get(i)));
            }
            this.updateBest();
            this.drawParticle(this.bestSolution);
            this.recalcVelocity();
        }
        if(this.bestSolution!=null)
             return this.bestSolution.toStringReduced();
        else
            return "";
    }

}
