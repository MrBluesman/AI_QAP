package solution;

import java.io.FileNotFoundException;

public class Test
{
    public static void main(String[] args) throws FileNotFoundException {
        //GA tescik = new GA(4);
        QAP tescik = new QAP("had9.dat");
        System.out.println("Distances matrix:");
        tescik.printMatrix(tescik.getDistancesMatrix());
        System.out.println();
        System.out.println("Flows matrix:");
        tescik.printMatrix(tescik.getFlowsMatrix());
//        Individual n = new Individual(12);
//        n.printChromosome();
        System.out.println();
        //System.out.println("Populations:");
        Population p = new Population(100, 0.7, 0.01);
        p.initialize();
        //p.getTournamentGroup(10);
        p.selection(2);
        p.printPopulation();
        System.out.println();
        p.crossover();
        p.printPopulation();
//        System.out.println();
//        p.getIndividuals().get(0).printChromosome();
//        System.out.println();
//        p.getIndividuals().get(1).printChromosome();
//        System.out.println();
//        System.out.println();
//
//        p.getIndividuals().get(0).crossPMX(p.getIndividuals().get(1));
//
//        p.getIndividuals().get(0).printChromosome();
//        System.out.println();
//        p.getIndividuals().get(1).printChromosome();
//        System.out.println();
    }
}
