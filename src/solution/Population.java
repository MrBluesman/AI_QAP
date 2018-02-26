package solution;

import java.util.ArrayList;
import java.util.List;

public class Population
{
    private static int POP_SIZE;
    private List<Individual> individuals;


    /**
     * Constructor for create a population of 100 individuals
     */
    Population()
    {
        POP_SIZE = 100;
        individuals = new ArrayList<>(POP_SIZE);
    }

    /**
     * Constructor for create a population of @_popSize individuals
     * @param _popSize size of population
     */
    Population(int _popSize)
    {
        POP_SIZE = _popSize;
        individuals = new ArrayList<>(POP_SIZE);
    }

    /**
     * individuals setter
     * @param _individuals new individuals to assign to population
     */
    public void setIndividuals(List<Individual> _individuals)
    {
        individuals = _individuals;
    }

    /**
     * individuals getter
     * @return population of individuals
     */
    public List<Individual> getIndividuals()
    {
        return individuals;
    }

    //-------------------------------------------------------------------------------------------

    /**
     * Initializes a population with Individuals generated randomly
     */
    public void initialize()
    {
        for(int i = 0; i < POP_SIZE; i++)
        {
            individuals.add(new Individual(12));
        }
    }

    public void printPopulation()
    {
        for(Individual elem2 : individuals)
        {
            elem2.printChromosome();
            System.out.println();
        }
    }
}
