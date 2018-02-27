package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population
{
    private static int POP_SIZE;
    private List<Individual> individuals;
    private static Random random;

    /**
     * Constructor for create a population of 100 individuals
     */
    Population()
    {
        POP_SIZE = 100;
        individuals = new ArrayList<>(POP_SIZE);
        random = new Random();
    }

    /**
     * Constructor for create a population of @_popSize individuals
     * @param _popSize size of population
     */
    Population(int _popSize)
    {
        POP_SIZE = _popSize;
        individuals = new ArrayList<>(POP_SIZE);
        random = new Random();
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
            //new Individual with static N value from GA class
            individuals.add(new Individual(QAP.getN()));
        }
    }

    /**
     * Selects a Individuals from this population and saves them in the new population for next generation
     * @param _tSize size of the tournament group
     * @return population with selected Individuals by _tSized tournament
     */
    public Population selection(int _tSize)
    {
        //Population newPopulation = new Population(popSize, Px, Pm);
        Population newPopulation = new Population(POP_SIZE);
        int newPopSize = 0;
        while(newPopSize < POP_SIZE)
        {
            ArrayList<Individual> tournamentGroup = this.getTournamentGroup(_tSize);
            Individual bestIndividual = tournamentGroup.get(0);
            for (int i = 1; i < _tSize; i++)
            {
                if (tournamentGroup.get(i).getAssignmentCost() < bestIndividual.getAssignmentCost())
                {
                    bestIndividual = tournamentGroup.get(i);
                }
            }
            newPopulation.individuals.add(new Individual(bestIndividual));
            //System.out.println(bestIndividual.getAssignmentCost());
            newPopSize++;
        }

        return newPopulation;
    }

    /**
     * Draws randomly a _tSized group of unique individuals which will take part in the tournament
     * @param _tSize size of the tournament group
     * @return group of individuals which will take part in the tournament
     */
    private ArrayList<Individual> getTournamentGroup(int _tSize)
    {
        ArrayList<Individual> tournamentGroup = new ArrayList<>();

        //drawing randomly a _tSized group of unique individuals
        int tGroupSize = 0;
        int randInd;
        while(tGroupSize < _tSize)
        {
            randInd = random.nextInt(POP_SIZE);
            if(!tournamentGroup.contains(individuals.get(randInd)))
            {
                tournamentGroup.add(individuals.get(randInd));
                //System.out.println("ID: " + randInd + " | Assignment cost: " + individuals.get(randInd).getAssignmentCost());
                tGroupSize++;
            }
        }
        return tournamentGroup;
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
