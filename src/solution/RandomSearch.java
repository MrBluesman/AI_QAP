package solution;

import java.io.FileNotFoundException;

public class RandomSearch
{
    private static final int SEARCH_SIZE = 10000;

    RandomSearch()
    {}

    public static void main(String[] args) throws FileNotFoundException
    {
        QAP data = new QAP("had9.dat");

        Individual best = new Individual(QAP.getN());
        int counter = 0;
        while (counter < SEARCH_SIZE)
        {
            Individual randInd = new Individual(QAP.getN());
            if(best.getAssignmentCost() > randInd.getAssignmentCost())
            {
                System.out.println(best.getAssignmentCost() + " -> " + randInd.getAssignmentCost());
                best = randInd;
            }
            counter++;
        }

        System.out.println();
        System.out.println("Best found: " + best.getAssignmentCost());
        System.out.print("Bests chromosome: ");
        best.printChromosome();
    }
}
