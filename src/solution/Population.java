package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population
{
    private static int POP_SIZE;            //Population size
    private static double Px;                  //Crossover propability
    private static double Pm;                  //Mutation propability
    private List<Individual> individuals;   //Individuals of population
    private static Random random;           //Random object used to draw a individuals or propability randomly

    /**
     * Constructor for create a population of 100 individuals with standard propability values
     */
    Population()
    {
        POP_SIZE = 100;
        Px = 0.07;
        Pm = 0.01;
        individuals = new ArrayList<>(POP_SIZE);
        random = new Random();
    }

    /**
     * Constructor for create a population of @_popSize individuals with propability values passed by args (_Px, _Pm)
     * @param _popSize size of population
     * @param _Px Crossover propability
     * @param _Pm Mutation propability
     */
    Population(int _popSize, double _Px, double _Pm)
    {
        POP_SIZE = _popSize;
        Px = _Px;
        Pm = _Pm;
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

    /**
     * POP_SIZE setter
     * @param _popSize new size of the population
     */
    public static void setPopSize(int _popSize)
    {
        POP_SIZE = _popSize;
    }

    /**
     * POP_SIZE getter
     * @return size of the population
     */
    public static int getPopSize()
    {
        return POP_SIZE;
    }

    /**
     * Px setter
     * @param _Px new crossover propability
     */
    public static void setPx(double _Px)
    {
        Px = _Px;
    }

    /**
     * Px getter
     * @return crossover propability
     */
    public static double getPx()
    {
        return Px;
    }

    /**
     * Pm setter
     * @param _Pm new mutation propability
     */
    public static void setPm(double _Pm)
    {
        Pm = _Pm;
    }

    /**
     * Px getter
     * @return mutation propability
     */
    public static double getPm()
    {
        return Pm;
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
        Population newPopulation = new Population(POP_SIZE, Px, Pm);
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

    /**
     * Crossovers the individuals of population based on cross propability
     */
    public void crossover()
    {
        Individual ind1 = null;
        Individual ind2 = null;

        List<Individual> crossoveredPop = new ArrayList<>();
        crossoveredPop.addAll(this.getIndividuals());
        this.getIndividuals().clear();


        for(int i = crossoveredPop.size() - 1; i >= 0 ; i--)
        {
            System.out.println(this.getIndividuals().size() + " | " + crossoveredPop.size());

            boolean isCrossover = random.nextDouble() < Px;
            if(isCrossover)
            {
                ind1 = crossoveredPop.get(i);
                ind2 = crossoveredPop.get(random.nextInt(crossoveredPop.size()));
                crossoveredPop.remove(ind1);
                //Individual child = ind1.crossCX(ind2);
                Individual child = ind1.crossOX(ind2);
                this.getIndividuals().add(child);
            }
            else
            {
                this.getIndividuals().add(crossoveredPop.get(i));
            }
        }

//        for(int i = crossoveredPop.size() - 1; i >= 0 ; i -= 2)
//        {
//            System.out.println(this.getIndividuals().size() + " | " + crossoveredPop.size());
//            ind1 = crossoveredPop.get(i);
//            ind2 = crossoveredPop.get(random.nextInt(crossoveredPop.size()));
//
//            boolean isCrossover = random.nextDouble() < Px;
//            if(isCrossover)
//            {
//                crossoveredPop.remove(ind1);
//                crossoveredPop.remove(ind2);
//                List<Individual> children = ind1.crossPMX(ind2);
//                this.getIndividuals().addAll(children);
//            }
//            else
//            {
//                this.getIndividuals().add(ind1);
//                this.getIndividuals().add(ind2);
//            }
//        }

    }

    public void mutation()
    {
        for (Individual ind : individuals)
        {
            double check = random.nextDouble();
            boolean isMutation = check < Pm;
            //System.out.println(check + " " + isMutation);
            //if (isMutation) ind.swapMutate(0.45);
            if (isMutation) ind.inversionMutate();
        }
    }

    /**
     * Calculates average assignment cost of whole population
     * @return average assignment cost of whole population
     */
    public double getAverageAssignmentCost()
    {
        double assignmentCost = 0.0;
        for (Individual ind : this.individuals)
        {
            assignmentCost += ind.getAssignmentCost();
        }
        return assignmentCost / (1.0 * this.individuals.size());
    }

    /**
     * Finds the best Individual in population (Individual with the least assignment cost)
     * @return Individual with the least assignment cost
     */
    public Individual getBestIndividual()
    {
        Individual theBestInd = this.individuals.get(0);
        for(int i = 1; i < this.individuals.size(); i++)
        {
            if(individuals.get(i).getAssignmentCost() < theBestInd.getAssignmentCost())
                theBestInd = individuals.get(i);
        }
        return theBestInd;
    }

    /**
     * Finds the worst Individual in population (Individual with the most expensive assignment cost)
     * @return Individual with the most expensive assignment cost
     */
    public Individual getWorstIndividual()
    {
        Individual theWorstInd = this.individuals.get(0);
        for(int i = 1; i<this.individuals.size(); i++)
        {
            if(individuals.get(i).getAssignmentCost() > theWorstInd.getAssignmentCost())
                theWorstInd = individuals.get(i);
        }
        return theWorstInd;
    }

    /**
     * Prints a population - Individual by Individual
     */
    public void printPopulation()
    {
        for(Individual elem2 : individuals)
        {
            elem2.printChromosome();
            System.out.println();
        }

    }
}
