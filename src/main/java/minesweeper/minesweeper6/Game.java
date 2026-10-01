package minesweeper.minesweeper6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game
{
    //dimensions for different difficulties
    private static final int HARDDIMX = 16;
    private static final int HARDDIMY = 30;
    private static final int NORMALDIM = 16;
    private static final int EASYDIM = 9;

    private boolean running;
    private int chosenDimX;
    private int chosenDimY;
    private int clearTiles;
    private int mineAmount;

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
                mineAmount = 99;
                break;
            case "Normal":
                chosenDimX = NORMALDIM;
                chosenDimY = NORMALDIM;
                mineAmount = 40;
                break;
            default:
                chosenDimX = EASYDIM;
                chosenDimY = EASYDIM;
                mineAmount = 10;
                break;
        }

        /*use 2d array to hold playing field, first two indexes are coordinates [y coord][x coord], element at [y][x][0]
         is what is actually there. For the real grid, 2 options: -1's are mines, 0+ is the number of mines adjacent.
         For the player grid, 0's are clear, 1's are flags, and 2's are unknown.*/
        gridReal = new int[chosenDimY][chosenDimX];
        gridPlayer = new int[chosenDimY][chosenDimX];

        //set the game progress to true
        running = true;
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
     * Fills the grids randomly with mines and empty spaces. -1's are mines, 0+ is the number of mines adjacent,
     * player's view is completely filled with unknowns (2's, no flags). A 3x3 group of tiles centered on (y, x) will
     * be left empty of mines.
     * @param xB The x coord to leave blank
     * @param yB The y coord to leave blank
     */
    public void populateGrid(int yB, int xB)
    {
        //start by filling the player grid with unknowns (2's)
        for (int[] row : gridPlayer)
        {
            Arrays.fill(row, 2);
        }

        //then begin filling the real grid
        //create a list of all possible positions inside the grid and populate it
        ArrayList<ArrayList<Integer>> emptySpots = new ArrayList<ArrayList<Integer>>();

        //loop through all the rows
        for (int y = 0; y < chosenDimY; y++)
        {
            //add a new arraylist to hold the elements in the row
            emptySpots.add(new ArrayList<Integer>());
            emptySpots.get(y).add(y);   //first element in each row holds the y coordinate

            //then loop the columns
            for (int x = 0; x < chosenDimX; x++)
            {
                //and if the position is not in the 3x3 area centered on (yB, xB)
                if ((y > yB + 1 || y < yB - 1) ||
                        (x > xB + 1 || x < xB - 1))
                {
                    //then add in the position
                    emptySpots.get(y).add(x);
                }
            }
        }

        //then use a random number generator to pick out positions from inside the list
        Random rand = new Random();
        int yInd;
        int xInd;

        //loop through all the mines
        for (int i = 0; i < mineAmount; i++)
        {
            //generate a random coord from the list
            yInd = rand.nextInt(1, emptySpots.size());
            //System.out.println("yInd: " + yInd);
            xInd = rand.nextInt(1, emptySpots.get(yInd).size());

            //add in a mine at that spot
            gridReal[emptySpots.get(yInd).get(0)][emptySpots.get(yInd).get(xInd)] = -1;

            //update the list of empty spots
            emptySpots.get(yInd).remove(xInd);

            //if that row is now empty, delete the row
            if (emptySpots.get(yInd).isEmpty())
            {
                emptySpots.remove(yInd);
            }
        }

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
