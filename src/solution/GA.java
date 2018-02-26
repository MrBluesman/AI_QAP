package solution;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class GA
{
    private static int N;
    private static int[][] distancesMatrix; //BetweenLocations
    private static int[][] flowsMatrix; //BetweenFacilities

    /**
     * Constructor for create GA class with QAP data random determined random N
     */
    GA()
    {
        Random randN = new Random();
        N = randN.nextInt(9) + 1;
        N = (N % 2) == 0 ? N : N + 1;

        distancesMatrix = new int[N][N];
        flowsMatrix = new int[N][N];
        fillMatrixWithRandom(distancesMatrix);
        fillMatrixWithRandom(flowsMatrix);
    }

    /**
     *  Constructor for create GA class with QAP data random determined by N passed by user
     * @param _N size of the problem
     */
    GA(int _N)
    {
        N = (_N % 2) == 0 ? _N : _N + 1;
        N = _N < 0 ? -N : N;
        distancesMatrix = new int[N][N];
        flowsMatrix = new int[N][N];
        fillMatrixWithRandom(distancesMatrix);
        fillMatrixWithRandom(flowsMatrix);
    }

    /**
     * Fills a matrix with random values between 0 and 10
     * @param _matrix matrix to fill with random values
     */
    private void fillMatrixWithRandom(int[][] _matrix)
    {
        Random randDistAndFlow = new Random();
        for(int i = 0; i < N; i++)
        {
            for(int j = i + 1; j < N; j++)
            {
                _matrix[i][j] = randDistAndFlow.nextInt(10);
            }
        }

        for(int i = N - 1; i >= 0; i--)
        {
            for(int j = N - 1 - (N - i); j >= 0; j--)
            {
                _matrix[i][j] = _matrix[j][i];
            }
        }
    }

    /**
     * Constructor for create GA class with QAP data from file
     * @param _fileName path to the file with problem parameters
     */
    GA(String _fileName) throws FileNotFoundException
    {
        File qapData = new File("../" + _fileName);
        //System.out.println(qapData.canRead());
        Scanner readQapData = new Scanner(qapData);

        N = readQapData.nextInt();
        distancesMatrix = new int[N][N];
        flowsMatrix = new int[N][N];

        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
                if(readQapData.hasNextInt())
                {
                    distancesMatrix[i][j] = readQapData.nextInt();
                }
            }
        }

        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
                if(readQapData.hasNextInt())
                {
                    flowsMatrix[i][j] = readQapData.nextInt();
                }
            }
        }
    }

    /**
     * N setter
     * @param _N new size of the problem to assign
     */
    public void setN(int _N)
    {
        N = _N;
    }


    /**
     * N getter
     * @return N size of the problem
     */
    public int getN()
    {
        return N;
    }

    /**
     * distancesMatrix setter
     * @param _distanceMatrix new matrix of distances between locations to assign
     */
    public void setDistancesMatrix(int[][] _distanceMatrix)
    {
        distancesMatrix = _distanceMatrix;
    }

    /**
     * distancesMatrix getter
     * @return matrix of distances between locations
     */
    public int[][] getDistancesMatrix()
    {
        return distancesMatrix;
    }

    /**
     * flowsMatrix setter
     * @param _flowsMatrix new matrix of flows between facilities to assign
     */
    public void setFlowsMatrix(int[][] _flowsMatrix)
    {
        flowsMatrix = _flowsMatrix;
    }

    /**
     * flowsMatrix getter
     * @return matrix of flows between facilities
     */
    public int[][] getFlowsMatrix()
    {
        return flowsMatrix;
    }

    //----------------------------------------------------------------------------------------------------------

    /**
     * Prints selected matrix
     * @param _matrix matrix to print
     */
    public void printMatrix(int[][] _matrix)
    {
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
                System.out.print(_matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

}
