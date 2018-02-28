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
     * Constructor for create an one Individual of whole population (potencial solution)
     * Based on Individual params passed by Individual object
     * @param _individual base of new Individual, new individual has a properies of this object
     */
    Individual(Individual _individual)
    {
        chromosomeSize = _individual.getChromosomeSize();
        chromosome = _individual.getChromosome();
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

    /**
     * Prints a chromosome gene by gene
     */
    public void printChromosome()
    {
        for(Integer elem : chromosome)
        {
            System.out.print("[" + elem + "] ");
        }
    }


    /**
     * Calculates a assigment cost of chromosome (RATE FUNCTION | EVALUATOR)
     * @return assignment cost of chromosome (proposal solution)
     */
    public int getAssignmentCost()
    {
        int assignmentCost = 0;

        for(int i = 0; i < QAP.getN(); i++)
        {
            for(int j = i + 1; j < QAP.getN(); j++)
            {
                //distance matrix * flows depended on individuals chromosome
                assignmentCost += QAP.getDistancesMatrix()[i][j] * QAP.getFlowsMatrix()[chromosome.get(i)][chromosome.get(j)];
            }
        }

        return assignmentCost;
    }

    /**
     * PMX crossover of 2 parents - begets 2 childrens
     * @param _ind second individual to cross with this
     * @return list of childrens (2 childrens - new Individuals)
     */
    public List<Individual> crossPMX(Individual _ind)
    {
        //Creating a ArrayList of children based on this Individual (first parent) and second parent passed by _ind
        Individual child1 = new Individual(this);
        Individual child2 = new Individual(_ind);
        List<Individual> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        //Randomized 2 cross points
        Random rand = new Random();
        int middlePoint = chromosomeSize / 2;
        int xPoint1 = rand.nextInt(middlePoint - 1) + 1;
        int xPoint2 = rand.nextInt(chromosomeSize - middlePoint) + middlePoint;

        List<Integer> relationList = new ArrayList<>();
        List<Integer> ch1ReplaceList = new ArrayList<>();
        List<Integer> ch2ReplaceList = new ArrayList<>();

        for(int i = xPoint1; i <= xPoint2; i++)
        {
            swapGenes(child1, child2, i);

            //making a relationlist of genes to repair chromosome after swap
            if(!child1.chromosome.get(i).equals(child2.chromosome.get(i)))
            {
                if(!relationList.contains(child1.chromosome.get(i)))
                {
                    //Unknown pair relation -> add both children genes
                    if(!relationList.contains(child2.chromosome.get(i)))
                    {
                        relationList.add(child1.chromosome.get(i));
                        relationList.add(child2.chromosome.get(i));
                    }
                    else
                    {
                        //Only Child2s gene was found in relation - update relation
                        relationList.set(relationList.indexOf(child2.chromosome.get(i)), child1.chromosome.get(i));
                    }
                }
                else if(relationList.contains(child2.chromosome.get(i)))
                {
                    //BOTH CONTAIN -> SCALLING RELATION
                    int index1 = relationList.indexOf(child1.chromosome.get(i));
                    int index2 = relationList.indexOf(child2.chromosome.get(i));

                    //match pair
                    if(index2 % 2 == 0)
                    {
                        relationList.set(index1, relationList.get(index2 + 1));
                        relationList.remove(index2);
                        relationList.remove(index2);
                    }
                    else
                    {
                        relationList.set(index1, relationList.get(index2 - 1));
                        relationList.remove(index2 - 1);
                        relationList.remove(index2 - 1);
                    }
                }
                else
                {
                    //Only Child1s gene was found in relation - update relation
                    relationList.set(relationList.indexOf(child1.chromosome.get(i)), child2.chromosome.get(i));

                }
            }
        }

        //split relation list to separated repair lists for both children
        for(int i=0; i<relationList.size(); i+=2)
        {
            ch1ReplaceList.add(relationList.get(i));
            ch2ReplaceList.add(relationList.get(i+1));
        }

        //repair based on replace/repair lists
        repairPMXchildrens(0, xPoint1, child1, child2, ch1ReplaceList, ch2ReplaceList);
        repairPMXchildrens(xPoint2 + 1, child1.chromosomeSize, child1, child2, ch1ReplaceList, ch2ReplaceList);

        return children;
    }

    /**
     * Swap genes beetwen 2 Individuals at selected position
     * @param _ind1 Individual 1
     * @param _ind2 Individual 2
     * @param _position swap position
     */
    private void swapGenes(Individual _ind1, Individual _ind2, int _position)
    {
        Integer temp = _ind1.chromosome.get(_position);
        _ind1.chromosome.set(_position, _ind2.chromosome.get(_position));
        _ind2.chromosome.set(_position, temp);
    }

    /**
     * Repairs childrens of PMX crossover, is based on repair/replace lists
     * @param _startPoint start index of subchromosome which needs repair
     * @param _endPoint end index of subchromosome which needs repair
     * @param _ch1 first children (no. 1)
     * @param _ch2 second children (no. 2)
     * @param _ch1ReplaceList replace/repair list for firs children (no. 1)
     * @param _ch2ReplaceList replace/repair list for firs children (no. 1)
     */
    private void repairPMXchildrens(int _startPoint, int _endPoint, Individual _ch1, Individual _ch2, List<Integer> _ch1ReplaceList, List<Integer> _ch2ReplaceList)
    {
        for(int i=_startPoint; i < _endPoint; i++)
        {
            if(_ch1ReplaceList.contains(_ch1.chromosome.get(i)))
            {
                _ch1.chromosome.set(i, _ch2ReplaceList.get(_ch1ReplaceList.indexOf(_ch1.chromosome.get(i))));
            }

            if(_ch2ReplaceList.contains(_ch2.chromosome.get(i)))
            {
                _ch2.chromosome.set(i, _ch1ReplaceList.get(_ch2ReplaceList.indexOf(_ch2.chromosome.get(i))));
            }
        }
    }
}
