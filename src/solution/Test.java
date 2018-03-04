package solution;

import java.io.FileNotFoundException;
import java.util.List;

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
        Population p = new Population(2, 0.7, 0.1);
        p.initialize();
        //p.getTournamentGroup(10);
        p.selection(2);
//        p.printPopulation();
        System.out.println();
        //p.crossover();
        //p.mutation();
        //p.printPopulation();
//        System.out.println();
//        p.getIndividuals().get(0).printChromosome();
//        System.out.println();
//        p.getIndividuals().get(1).printChromosome();
//        System.out.println();
//        System.out.println();

        p.getIndividuals().get(0).printChromosome();
        System.out.println();
        System.out.println();
        p.getIndividuals().get(0).inversionMutate();
        System.out.println();
        p.getIndividuals().get(0).printChromosome();

        //List<Individual> ch = p.getIndividuals().get(0).crossPMX(p.getIndividuals().get(1));
//        Individual ch = p.getIndividuals().get(0).crossOX(p.getIndividuals().get(1));

//        ch.get(0).printChromosome();
//        System.out.println();
//        ch.get(1).printChromosome();

//        ch.printChromosome();
//        System.out.println();

//        p.getIndividuals().get(0).printChromosome();
//        System.out.println();
//        p.getIndividuals().get(1).printChromosome();
//        System.out.println();
    }
}
