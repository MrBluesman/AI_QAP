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
     * CX crossover of 2 parents - begets 1 child
     * @param _ind second Individual to cross with this Individual
     * @return child of 2 parents crossover - new Individual
     */
    public Individual crossCX(Individual _ind)
    {
        Individual child = new Individual(QAP.getN());
        child.getChromosome().clear();
        List<Integer> positionsToCopy = new ArrayList<>();
        int cycleStart = this.getChromosome().get(0);
        int relationStart = cycleStart;
        int cycleEnd = _ind.getChromosome().get(0);
        int relationEnd = cycleEnd;
        positionsToCopy.add(this.getChromosome().indexOf(relationStart));

        while((relationStart != relationEnd) && (cycleStart != cycleEnd))
        {
            relationStart = cycleEnd;
            relationEnd = _ind.getChromosome().get(this.getChromosome().indexOf(relationStart));
            cycleEnd = relationEnd;
            System.out.println(relationStart + " -> " + relationEnd);
            positionsToCopy.add(this.getChromosome().indexOf(relationStart));
        }

        for(int i = 0; i < this.getChromosomeSize(); i++)
        {
            child.getChromosome().add(positionsToCopy.contains(i) ? this.getChromosome().get(i) : _ind.getChromosome().get(i));

        }
        return child;
    }

    /**
     * OX crossover of 2 parents - begets 1 child
     * @param _ind second Individual to cross with this Individual
     * @return child of 2 parents crossover - new Individual
     */
    public Individual crossOX(Individual _ind)
    {
        Individual child = new Individual(QAP.getN());
        child.getChromosome().clear();

        //Randomized 2 cross points
        Random rand = new Random();
        int middlePoint = chromosomeSize / 2;
        int xPoint1 = rand.nextInt(middlePoint - 1) + 1;
        int xPoint2 = rand.nextInt(chromosomeSize - middlePoint) + middlePoint;

        //List of used and unused elements to repair if it's necessary
        List<Integer> usedGenes = new ArrayList<>();
        List<Integer> unusedGenes = new ArrayList<>(this.getChromosome());

        //Copy genes restricted by xPoints
        List<Integer> genesToCopy = new ArrayList<>();
        for(int i = xPoint1; i < xPoint2; i++)
        {
            genesToCopy.add(this.getChromosome().get(i));
        }

        for(int i = 0; i < this.getChromosomeSize(); i++)
        {
            Integer copiedGene = null;
            if(genesToCopy.contains(_ind.getChromosome().get(i)) || genesToCopy.contains(this.getChromosome().get(i)))
            {
                copiedGene = usedGenes.contains(this.getChromosome().get(i)) ? unusedGenes.get(0) : this.getChromosome().get(i);
            }
            else
            {
                copiedGene = usedGenes.contains(_ind.getChromosome().get(i)) ? unusedGenes.get(0) : _ind.getChromosome().get(i);
            }

            //setting a chromosome and updating usedGenes and unusedGenes
            child.getChromosome().add(copiedGene);
            usedGenes.add(copiedGene);
            unusedGenes.remove(copiedGene);
        }

        return child;
    }

    /**
     * PMX crossover of 2 parents - begets 2 children
     * @param _ind second Individual to cross with this Individual
     * @return list of children (2 children - new Individuals)
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
        repairPMXchildren(0, xPoint1, child1, child2, ch1ReplaceList, ch2ReplaceList);
        repairPMXchildren(xPoint2 + 1, child1.chromosomeSize, child1, child2, ch1ReplaceList, ch2ReplaceList);

        return children;
    }

    /**
     * Swap genes beetween 2 Individuals at selected position
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
     * Swap genes in 1 Individual - position1 with position2
     * @param _ind Individual which genes will be swapped
     * @param _position1 position of first gene to swap with second
     * @param _position2 position of second gene to swap with first
     */
    private void swapGenes(Individual _ind, int _position1, int _position2)
    {
        Integer temp = _ind.chromosome.get(_position1);
        _ind.chromosome.set(_position1, _ind.chromosome.get(_position2));
        _ind.chromosome.set(_position2, temp);
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
    private void repairPMXchildren(int _startPoint, int _endPoint, Individual _ch1, Individual _ch2, List<Integer> _ch1ReplaceList, List<Integer> _ch2ReplaceList)
    {
        for(int i = _startPoint; i < _endPoint; i++)
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

    /**
     * Mutetes by swap genes in Individuals chromosome
     * @param _percOfGenesToM Percentage of genes will be swapped
     */
    public void swapMutate(double _percOfGenesToM)
    {
        int genesToMutate = (int) (this.chromosomeSize * _percOfGenesToM/2) * 2;
        Random rand = new Random();

        for(int i = 0; i < genesToMutate; i += 2)
        {
            int firstPosToSwap = rand.nextInt(this.chromosomeSize);  //first position to swap
            int secondPosToSwap = rand.nextInt(this.chromosomeSize); //second position to swap
            swapGenes(this, firstPosToSwap, secondPosToSwap);
        }
    }
}
