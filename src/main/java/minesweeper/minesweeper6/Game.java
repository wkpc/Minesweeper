package minesweeper.minesweeper6;

import java.util.Random;

public class Game
{
    //dimensions for different difficulties
    private static final int HARDDIMX = 16;
    private static final int HARDDIMY = 30;
    private static final int NORMALDIM = 16;
    private static final int EASYDIM = 9;

    private static final int MINECHANCE = 20;   //mine spawn chance(%)

    private boolean running;
    private int chosenDimX;
    private int chosenDimY;
    private int clearTiles;

    private int[][] gridReal;
    private int[][] gridPlayer;

    //constructor
    public Game(String difficulty)
    {
        clearTiles = 0;

        //create a grid with the correct dimensions according to difficulty
        switch (difficulty)
        {
            case "Hard":
                chosenDimX = HARDDIMX;
                chosenDimY = HARDDIMY;
                break;
            case "Normal":
                chosenDimX = NORMALDIM;
                chosenDimY = NORMALDIM;
                break;
            default:
                chosenDimX = EASYDIM;
                chosenDimY = EASYDIM;
                break;
        }

        /*use 2d array to hold playing field, first two indexes are coordinates [y coord][x coord], element at [y][x][0]
         is what is actually there. For the real grid, 2 options: -1's are mines, 0+ is the number of mines adjacent.
         For the player grid, 0's are clear, 1's are flags, and 2's are unknown.*/
        gridReal = new int[chosenDimY][chosenDimX];
        gridPlayer = new int[chosenDimY][chosenDimX];

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
        int mine;

        //loop through all the rows
        for (int y = 0; y < chosenDimY; y++)
        {
            //then the columns
            for (int x = 0; x < chosenDimX; x++)
            {
                //then add in a mine or an empty and cover it up for the player
                mine = rand.nextInt(1, 101);    //adjusting mine spawn chance

                if (mine <= MINECHANCE)
                {
                    gridReal[y][x] = -1;
                }else
                {
                    gridReal[y][x] = 0;
                }

                gridPlayer[y][x] = 2;
            }
        }

        printGrid();

        //once all the mines have been added, must go back through the real grid and update all the clear spots to
        //reflect the number of mines adjacent
        //loop through all the rows
        for (int y = 0; y < chosenDimY; y++)
        {
            //then the columns
            for (int x = 0; x < chosenDimX; x++)
            {
                //then if a clear spot is found...
                if (gridReal[y][x] != -1)
                {
                    clearTiles++;

                    //check in a circle around it, clockwise from top left
                    int checkY = y - 1;
                    int checkX = x - 1;
                    int mineCount = 0;

                    //check row by row
                    for (int cY = checkY; cY < checkY + 3; cY++)
                    {
                        for (int cX = checkX; cX < checkX + 3; cX++)
                        {
                            //if that spot exists (i.e. not out of bounds)
                            if (cX >= 0 && cX < chosenDimX && cY >= 0 && cY < chosenDimY)
                            {
                                //check if there's a mine and add it to the count
                                if (gridReal[cY][cX] == -1)
                                {
                                    mineCount++;
                                }
                            }
                        }
                    }

                    //finally update the element at that spot to show how many adjacent mines
                    gridReal[y][x] = mineCount;
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
    public int getChosenDimX()
    {
        return chosenDimX;
    }

    /**
     * return the dimensions of the grid
     * @return chosenDim
     */
    public int getChosenDimY()
    {
        return chosenDimY;
    }

    /**
     * returns the real element in the grid at position (x, y)
     *
     * @param x The x coord of the element
     * @param y The y coord of the element
     * @return The element at (x, y) in the grid
     */
    public int getRealElem(int y, int x)
    {
        return gridReal[y][x];
    }

    /**
     * returns the element seen by the player in the grid at position (x, y)
     *
     * @param x The x coord of the element
     * @param y The y coord of the element
     * @return The element at (x, y) in the grid
     */
    public int getPlayerElem(int y, int x)
    {
        return gridPlayer[y][x];
    }

    /**
     * Swaps out an element at (x, y) in the player's grid for a new element e
     * @param x The x coord of the element to be replaced
     * @param y The y coord of the element to be replaced
     * @param e The new element to replace with
     */
    public void changePlayerElem(int y, int x, int e)
    {
        gridPlayer[y][x] = e;
    }

    /**
     * starts the game
     */
    public void start()
    {
        running = true;
    }

    /**
     * ends the game
     */
    public void end()
    {
        running = false;
    }

    /**
     * Checks to see if the player won, by seeing if all the clear tiles have been found
     * @param cleared The number of tiles cleared by the player
     * @return true if the player won, false if not yet
     */
    public boolean checkVictory(int cleared)
    {
        if (cleared == clearTiles)
        {
            return true;
        }

        return false;
    }

    /**
     * helper function for debugging, print the contents of the grid (player and real) to the console
     */
    private void printGrid()
    {
        System.out.print("Real: \n");
        //loop through all the columns
        for (int y = 0; y < chosenDimY; y++)
        {
            //then the columns
            for (int x = 0; x < chosenDimX; x++)
            {
                //and print the real element there
                System.out.print(gridReal[y][x] + ", ");
            }

            System.out.print("\n");
        }

        //now do the same thing for the player's view
        System.out.print("Player view: \n");
        //loop through all the rows
        for (int y = 0; y < chosenDimY; y++)
        {
            //then the columns
            for (int x = 0; x < chosenDimX; x++)
            {
                //and print the fake element there
                System.out.print(gridPlayer[y][x] + ", ");
            }

            System.out.print("\n");
        }

        System.out.println("Clear Tiles: " + clearTiles);
    }
}
