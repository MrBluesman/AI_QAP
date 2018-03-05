package solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Population
{
    private static int POP_SIZE;            //Population size
    private static double Px;                  //Crossover probability
    private static double Pm;                  //Mutation probability
    private List<Individual> individuals;   //Individuals of population
    private static Random random;           //Random object used to draw a individuals or probability randomly

    /**
     * Constructor for create a population of 100 individuals with standard propability values
     */
    Population()
    {
        POP_SIZE = 100;
        Px = 0.7;
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
     * @param _Px new crossover probability
     */
    public static void setPx(double _Px)
    {
        Px = _Px;
    }

    /**
     * Px getter
     * @return crossover probability
     */
    public static double getPx()
    {
        return Px;
    }

    /**
     * Pm setter
     * @param _Pm new mutation probability
     */
    public static void setPm(double _Pm)
    {
        Pm = _Pm;
    }

    /**
     * Px getter
     * @return mutation probability
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
     * Using a tournament selection
     * @param _tSize size of the tournament group
     * @return population with selected Individuals by _tSized tournament
     */
    public Population selection(int _tSize)
    {
        Population newPopulation = new Population(POP_SIZE, Px, Pm);

        int newPopSize = 0;
        while(newPopSize < POP_SIZE)
        {
            //tournament
            ArrayList<Individual> tournamentGroup = this.getTournamentGroup(_tSize);
            Individual bestIndividual = tournamentGroup.get(0);
            for (int i = 1; i < _tSize; i++)
            {
                if (tournamentGroup.get(i).getAssignmentCost() < bestIndividual.getAssignmentCost())
                {
                    bestIndividual = tournamentGroup.get(i);
                }
            }
            newPopulation.getIndividuals().add(new Individual(bestIndividual));

            newPopSize++;
        }

        return newPopulation;
    }

    /**
     * Selects a Individuals from this population and saves them in the new population for next generation
     * Using a roulette wheel
     * @return population with selected Individuals by roulette wheel
     */
    public Population selection()
    {
        Population newPopulation = new Population(POP_SIZE, Px, Pm);

        //creating an list with probabilities to roll
        double popOverallAssignmentCost = this.getOverallAssignmentCost();
        double[] rouletteProb = new double[POP_SIZE];
        int theWorstAssignmentcost = this.getWorstIndividual().getAssignmentCost();
        Double indProd = 0.0;
        for(int i = 0; i < POP_SIZE; i++)
        {
            //indProd += (this.getIndividuals().get(i).getAssignmentCost() / popOverallAssignmentCost);
            indProd += (theWorstAssignmentcost - this.getIndividuals().get(i).getAssignmentCost() + 1 / (popOverallAssignmentCost + 1));
            rouletteProb[i] = indProd;
        }

        //(Wartość najgorszego osobnika-Wartość danego osobnika+1)/(suma wartości wszystkich osobników+1)

        int newPopSize = 0;
        while(newPopSize < POP_SIZE)
        {
            //roulette wheel
            double randomFitness = random.nextDouble() * rouletteProb[rouletteProb.length - 1];
            int index = Arrays.binarySearch(rouletteProb, randomFitness);
            index = Math.abs(index) - 1;
            newPopulation.getIndividuals().add(this.getIndividuals().get(index));

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
     * Crossovers the individuals of population based on cross probability
     * @param _method crossover method which will be used to beget children to new population
     */
    public void crossover(String _method)
    {
        Individual ind1;
        Individual ind2;

        List<Individual> crossoveredPop = new ArrayList<>(this.getIndividuals());
        this.getIndividuals().clear();

        switch(_method)
        {
            case "PMX":
            {
                for(int i = crossoveredPop.size() - 1; i >= 0 ; i -= 2)
                {
                    //System.out.println(this.getIndividuals().size() + " | " + crossoveredPop.size());
                    ind1 = crossoveredPop.get(i);
                    ind2 = crossoveredPop.get(random.nextInt(crossoveredPop.size()));

                    boolean isCrossover = random.nextDouble() < Px;
                    if(isCrossover)
                    {
                        crossoveredPop.remove(ind1);
                        crossoveredPop.remove(ind2);
                        List<Individual> children = ind1.crossPMX(ind2);
                        this.getIndividuals().addAll(children);
                    }
                    else
                    {
                        this.getIndividuals().add(ind1);
                        this.getIndividuals().add(ind2);
                    }
                }
                break;
            }

            default:
            {
                for(int i = crossoveredPop.size() - 1; i >= 0 ; i--)
                {
                   // System.out.println(this.getIndividuals().size() + " | " + crossoveredPop.size());
                    boolean isCrossover = random.nextDouble() < Px;
                    if(isCrossover)
                    {
                        ind1 = crossoveredPop.get(i);
                        ind2 = crossoveredPop.get(random.nextInt(crossoveredPop.size()));
                        crossoveredPop.remove(ind1);
                        //Individual child = ind1.crossCX(ind2);
                        Individual child = _method.equals("CX") ? ind1.crossCX(ind2) : ind1.crossOX(ind2);
                        this.getIndividuals().add(child);
                    }
                    else
                    {
                        this.getIndividuals().add(crossoveredPop.get(i));
                    }
                }
            }
        }
    }

    public void mutation(String _method)
    {
        for (Individual ind : individuals)
        {
            double check = random.nextDouble();
            boolean isMutation = check < Pm;
            //System.out.println(check + " " + isMutation);
            //if (isMutation) ind.swapMutate(0.45);
            if (isMutation)
            {
                if(_method.equals("swap")) ind.swapMutate(0.45);
                else ind.inversionMutate();
            }
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
     * Calculates overall assignment cost of whole population
     * @return overal assignment cost of whole population
     */
    public double getOverallAssignmentCost()
    {
        double assignmentCost = 0.0;
        for (Individual ind : this.individuals)
        {
            assignmentCost += ind.getAssignmentCost();
        }
        return assignmentCost;
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
