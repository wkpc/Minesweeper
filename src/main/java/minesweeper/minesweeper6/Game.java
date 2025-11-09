package minesweeper.minesweeper6;

import java.util.Random;

public class Game
{
    private static boolean running;
    private static final int HARDDIM = 24;
    private static final int NORMALDIM = 12;
    private static final int EASYDIM = 6;
    private static int chosenDim;
    private static int[][][] grid;

    //constructor
    public Game(String difficulty)
    {
        //create a grid with the correct dimensions according to difficulty
        switch (difficulty)
        {
            case "Hard":
                chosenDim = HARDDIM;
                break;
            case "Normal":
                chosenDim = NORMALDIM;
                break;
            default:
                chosenDim = EASYDIM;
                break;
        }

        /*use 3d array to hold playing field, first two indexes are coordinates [y coord][x coord],last index represents
        the piece at that spot. Element at [y][x][0] is what is actually there (0's are empty spots, 1's are mines),
        element at [y][x][1] is what the player sees (0's are unknown, 1's are flags)*/
        grid = new int[chosenDim][chosenDim][2];

        //call a helper function to populate the map, and set the game progress to true
        populateGrid();
        printGrid();
        running = true;
    }

    /**
     * Fills the grid randomly with mines and empty spaces. 0's are empty, 1's are mines, player's view is
     * completely filled with unknowns (no flags)
     */
    private void populateGrid()
    {
        //use a random number generator
        Random rand = new Random();

        //loop through all the rows
        for (int y = 0; y < chosenDim; y++)
        {
            //then the columns
            for (int x = 0; x < chosenDim; x++)
            {
                //then add in a mine or an empty and cover it up for the player
                grid[y][x][0] = rand.nextInt(2);
                grid[y][x][1] = 0;
            }
        }
    }

    //checks to see if the game is still ongoing
    public boolean isRunning()
    {
        return running;
    }

    /**
     * helper function for debugging, print the contents of the grid (player and real) to the console
     */
    private void printGrid()
    {
        System.out.println("Real: \n");
        //loop through all the rows
        for (int y = 0; y < chosenDim; y++)
        {
            //then the columns
            for (int x = 0; x < chosenDim; x++)
            {
                //and print the real element there
                System.out.print(grid[y][x][0] + ", ");
            }

            System.out.print("\n");
        }

        //now do the same thing for the player's view
        System.out.println("Player view: \n");
        //loop through all the rows
        for (int y = 0; y < chosenDim; y++)
        {
            //then the columns
            for (int x = 0; x < chosenDim; x++)
            {
                //and print the fake element there
                System.out.print(grid[y][x][1] + ", ");
            }

            System.out.print("\n");
        }
    }
}
