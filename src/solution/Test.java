package solution;

import java.io.FileNotFoundException;

public class Test
{
    public static void main(String[] args) throws FileNotFoundException
    {
//        GA tescik = new GA();
        GA tescik = new GA("had12.dat");
        System.out.println("Distances matrix:");
        tescik.printMatrix(tescik.getDistancesMatrix());
        System.out.println();
        System.out.println("Flows matrix:");
        tescik.printMatrix(tescik.getFlowsMatrix());
    }
}
