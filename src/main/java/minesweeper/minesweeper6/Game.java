package minesweeper.minesweeper6;

import java.util.Random;

public class Game
{
    private static boolean running;
    private static final int HARDDIM = 24;
    private static final int NORMALDIM = 12;
    private static final int EASYDIM = 6;
    private static int chosenDim;
    private static int[][] gridReal;
    private static int[][] gridPlayer;

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

        /*use 2d array to hold playing field, first two indexes are coordinates [x coord][y coord], element at [x][y][0]
         is what is actually there. For the real grid, 2 options: -1's are mines, 0+ is the number of mines adjacent.
         For the player grid, 0's are clear, 1's are flags, and 2's are unknown.*/
        gridReal = new int[chosenDim][chosenDim];
        gridPlayer = new int[chosenDim][chosenDim];

        //call a helper function to populate the map, and set the game progress to true
        populateGrid();
        printGrid();
        running = true;
    }

    /**
     * Fills the grids randomly with mines and empty spaces. -1's are mines, 0+ is the number of mines adjacent,
     * player's view is completely filled with unknowns (2's, no flags)
     */
    private void populateGrid()
    {
        //use a random number generator
        Random rand = new Random();

        //loop through all the rows
        for (int x = 0; x < chosenDim; x++)
        {
            //then the columns
            for (int y = 0; y < chosenDim; y++)
            {
                //then add in a mine or an empty and cover it up for the player
                gridReal[x][y] = rand.nextInt(-1, 1);
                gridPlayer[x][y] = 2;
            }
        }

        printGrid();

        //once all the mines have been added, must go back through the real grid and update all the clear spots to
        //reflect the number of mines adjacent
        //loop through all the rows
        for (int x = 0; x < chosenDim; x++)
        {
            //then the columns
            for (int y = 0; y < chosenDim; y++)
            {
                //then if a clear spot is found...
                if (gridReal[x][y] != -1)
                {
                    //check in a circle around it, clockwise from top left
                    int checkX = x - 1;
                    int checkY = y - 1;
                    int mineCount = 0;

                    //check row by row
                    for (int cY = checkY; cY < checkY + 3; cY++)
                    {
                        for (int cX = checkX; cX < checkX + 3; cX++)
                        {
                            //if that spot exists (i.e. not out of bounds)
                            if (cX >= 0 && cX < chosenDim && cY >= 0 && cY < chosenDim)
                            {
                                //check if there's a mine and add it to the count
                                if (gridReal[cX][cY] == -1)
                                {
                                    mineCount++;
                                }
                            }
                        }
                    }

                    //finally update the element at that spot to show how many adjacent mines
                    gridReal[x][y] = mineCount;
                }
            }
        }
    }

    /**
     * check to see if the game is ongoing
     * @return true if the game is still running, false if not
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * return the dimensions of the grid
     * @return chosenDim
     */
    public int getChosenDim()
    {
        return chosenDim;
    }

    /**
     * returns the real element in the grid at position (x, y)
     *
     * @param x The x coord of the element
     * @param y The y coord of the element
     * @return The element at (x, y) in the grid
     */
    public int getRealElem(int x, int y)
    {
        return gridReal[x][y];
    }

    /**
     * returns the element seen by the player in the grid at position (x, y)
     *
     * @param x The x coord of the element
     * @param y The y coord of the element
     * @return The element at (x, y) in the grid
     */
    public int getPlayerElem(int x, int y)
    {
        return gridPlayer[x][y];
    }

    public void changePlayerElem(int x, int y, int e)
    {
        gridPlayer[x][y] = e;
    }

    /**
     * helper function for debugging, print the contents of the grid (player and real) to the console
     */
    private void printGrid()
    {
        System.out.print("Real: \n");
        //loop through all the columns
        for (int x = 0; x < chosenDim; x++)
        {
            //then the rows
            for (int y = 0; y < chosenDim; y++)
            {
                //and print the real element there
                System.out.print(gridReal[x][y] + ", ");
            }

            System.out.print("\n");
        }

        //now do the same thing for the player's view
        System.out.print("Player view: \n");
        //loop through all the rows
        for (int x = 0; x < chosenDim; x++)
        {
            //then the rows
            for (int y = 0; y < chosenDim; y++)
            {
                //and print the fake element there
                System.out.print(gridPlayer[x][y] + ", ");
            }

            System.out.print("\n");
        }
    }
}
