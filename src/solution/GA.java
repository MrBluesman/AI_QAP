package solution;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class GA
{
    private static final int POP_SIZE = 100;      //Population size
    private static final int GEN_LENGTH = 100;    //Numbers of generations of population
    private static final double PX = 0.7;        //Crossover propability
    private static final double PM = 0.1;        //Mutation propability
    private static final int T_SIZE = 10;         //Tournament size
    private static int TEST_NUMBERS = 10;         //Numbers of test

    private static Individual bestInd;
    private static double[] averageIndOfPopulation = new double[GEN_LENGTH];  //AVF
    private static double[] bestIndOfPopulation = new double[GEN_LENGTH];     //MAX
    private static double[] worstIndOfPopulation = new double[GEN_LENGTH];    //MIN

    public GA()
    {
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        QAP data = new QAP("had20.dat");

        double average = 0;
        double deviation = 0;
        double deviationOfPopulation = 0;

        //printing
        System.out.println("Distances matrix:");
        data.printMatrix(QAP.getDistancesMatrix());
        System.out.println();
        System.out.println("Flows matrix:");
        data.printMatrix(QAP.getFlowsMatrix());

        PrintWriter outputFile = new PrintWriter("results.csv");

        bestInd = new Individual(QAP.getN());

        Population population;
        int actualGeneration;

        for (int i = 0; i < TEST_NUMBERS; i++)
        {
            //Inicialization
            population = new Population(POP_SIZE, PX, PM);
            population.initialize();
            actualGeneration = 0;

            while (actualGeneration < GEN_LENGTH)
            {
                actualGeneration++;
                population = population.selection(T_SIZE);
                population.crossover();
                population.mutation();

                average = population.getAverageAssignmentCost();
                averageIndOfPopulation[actualGeneration - 1] += population.getAverageAssignmentCost();
                bestIndOfPopulation[actualGeneration - 1] += population.getBestIndividual().getAssignmentCost();
                worstIndOfPopulation[actualGeneration - 1] += population.getWorstIndividual().getAssignmentCost();
                //System.out.println(i + ": " + actualGeneration);

                //Replace the best solution if we found better
                if(population.getBestIndividual().getAssignmentCost() < bestInd.getAssignmentCost())
                {
                    bestInd = population.getBestIndividual();
                }
            }


            //Deviation - odchylenie standardowe
            for (int d = 0; d < population.getIndividuals().size(); d++)
            {
                deviation += Math.pow(average - population.getIndividuals().get(d).getAssignmentCost(), 2);
            }
            deviation /= population.getIndividuals().size();
            deviationOfPopulation += Math.sqrt(deviation);

        }
        for (int i = 0; i < GEN_LENGTH; i++)
        {
            averageIndOfPopulation[i] /=  TEST_NUMBERS;
            bestIndOfPopulation[i]    /=  TEST_NUMBERS;
            worstIndOfPopulation[i]   /=  TEST_NUMBERS;

            outputFile.print(i + 1);
            //outputFile.print("\t");
            outputFile.print("," + worstIndOfPopulation[i]);
            //outputFile.print("\t");
            outputFile.print("," + averageIndOfPopulation[i]);
            //outputFile.print("\t");
            outputFile.print(", " + bestIndOfPopulation[i]);
            outputFile.println();
        }

        System.out.println("\nAverage of worst Individuals: " + worstIndOfPopulation[GEN_LENGTH - 1]);
        System.out.println("Average of medium Individuals: " + averageIndOfPopulation[GEN_LENGTH - 1]);
        System.out.println("Average of best Individuals: " + bestIndOfPopulation[GEN_LENGTH - 1]);
        System.out.println("The best found Individual: " + bestInd.getAssignmentCost());
        System.out.println("Daviation: " + deviationOfPopulation / TEST_NUMBERS);
        System.out.println();
        bestInd.printChromosome();

        outputFile.close();
    }
}
