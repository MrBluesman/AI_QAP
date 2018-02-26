package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual
{
    private int chromosomeSize;
    private List<Integer> chromosome;

    /**
     * Constructor for create an one Individual of whole population (potencial solution)
     * Individual has own chromosome
     * @param _chromosomeSize size of individual chromosome
     */
    Individual(int _chromosomeSize)
    {
        chromosomeSize = _chromosomeSize;
        List<Integer> arrayToRoll = new ArrayList<>(chromosomeSize);
        for(int i = 0; i < chromosomeSize; i++)
        {
            arrayToRoll.add(i);
        }
        //Rolling filled chromosome
        chromosome = rollArrayList(arrayToRoll);
    }

    /**
     * Rolls a arrayList passed by _arrayList
     * @param _arrayList ArrayList to roll
     * @return rolled ArrayList
     */
    private List<Integer> rollArrayList(List<Integer> _arrayList)
    {
        List<Integer> rolledArrayListToReturn = new ArrayList<>(chromosomeSize);
        Random randIndex = new Random();

        int fillingArrayListSize = chromosomeSize;
        for(int i = 0; i < chromosomeSize; i++)
        {
            int indexToMove = randIndex.nextInt(fillingArrayListSize);
            rolledArrayListToReturn.add(_arrayList.get(indexToMove));
            _arrayList.remove(indexToMove);
            fillingArrayListSize--;
        }

        return rolledArrayListToReturn;
    }

    /**
     *  chromosomeSize setter
     * @param _chromosomeSize new size of the Individuals chromosome
     */
    public void setChromosomeSize(int _chromosomeSize)
    {
        chromosomeSize = _chromosomeSize;
    }

    /**
     * chromosomeSize getter
     * @return chromosomeSize
     */
    public int getChromosomeSize()
    {
        return chromosomeSize;
    }

    /**
     * chromosome setter
     * @param _chromosome new Individuals chromosome
     */
    public void setChromosome(List<Integer> _chromosome)
    {
        chromosome = _chromosome;
    }

    /**
     * chromosome getter
     * @return chromosome
     */
    public List<Integer> getChromosome()
    {
        return chromosome;
    }

    //----------------------------------------------------------------------------------------------------------

    public void printChromosome()
    {
        for(Integer elem : chromosome)
        {
            System.out.print("[" + elem + "] ");
        }
    }

    //DO ZROBIENIA

    /**
     * Calculates a assigment cost of chromosome
     * @return assignment cost of chromosome (proposal solution)
     */
    public int getAssignmentCost()
    {
        int assignmentCost = 0;
        return assignmentCost;
    }
}
