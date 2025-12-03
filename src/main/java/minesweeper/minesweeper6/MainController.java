package minesweeper.minesweeper6;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class MainController
{
    @FXML
    private HBox CenterPane;

    @FXML
    private Label InternalIssueNoti;

    @FXML
    private Label UserActionNoti; //unused

    @FXML
    private Label CurDiff;

    @FXML
    private Button DifficultyEasy;

    @FXML
    private Button DifficultyHard;

    @FXML
    private Button DifficultyNormal;

    @FXML
    private Button Start;

    @FXML
    private GridPane MainGrid;

    private Game game;
    private boolean firstClick;
    private String difficulty;
    private int cleared;

    /// GRID COLORS ///
    private static final String WHITE = "#DCDCDC";    //actually closer to a very light grey
    private static final String GRAY = "#969696;";
    private static final String RED = "#C23434";
    private static final String GREEN = "#26a317";
    private static final String BLUE = "#0030BE";
    private static final String BLACK = "#000000";

    /**
     * On game launch, set up the window
     */
    @FXML
    void initialize()
    {
        //start with the game being empty
        game = null;
        firstClick = true;
        cleared = 0;
        InternalIssueNoti.setText("");
        UserActionNoti.setText("");
        MainGrid.getChildren().clear();
    }

    /**
     * Set the difficulty to hard
     * @param event Not used
     */
    @FXML
    void onDifficultyHardClicked(MouseEvent event)
    {
        CurDiff.setText("Hard");
        difficulty = "Hard";
    }

    /**
     * Set the difficulty to normal
     * @param event Not used
     */
    @FXML
    void onDifficultyNormalClicked(MouseEvent event)
    {
        CurDiff.setText("Normal");
        difficulty = "Normal";
    }

    /**
     * Set the difficulty to easy
     * @param event Not used
     */
    @FXML
    void onDifficultyEasyClicked(MouseEvent event)
    {
        CurDiff.setText("Easy");
        difficulty = "Easy";
    }

    /**
     * checks if a difficulty has been selected, and start (or restarts) a new game.
     * @param event Not used
     */
    @FXML
    void onStartClicked(MouseEvent event)
    {
        //make sure a difficulty was selected before making a new game
        if (difficulty != null)
        {
            //if a game is already in progress
            if (game != null && game.isRunning() == true)
            {
                //ask the user for confirmation before restarting
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Restart?");
                alert.setContentText("A game is currently in progress. Are you sure you want to restart?");

                //check to see what the player chose
                Optional<ButtonType> result = alert.showAndWait();

                //if the user confirmed, then restart the game
                if (result.get() == ButtonType.OK)
                {
                    game = new Game(difficulty);
                    firstClick = true;
                    cleared = 0;
                    loadPlayerGrid(firstClick);

                    //then rename the label to restart
                    Start.setText("Restart");
                }

                //otherwise do nothing if they cancelled
            }else //if no game is in progress, skip confirmation checks and immediately create game
            {
                game = new Game(difficulty);
                firstClick = true;
                cleared = 0;

                //make the grid size scale with the window
                MainGrid.prefWidthProperty().bind(CenterPane.widthProperty().multiply(0.75));
                MainGrid.prefHeightProperty().bind(CenterPane.heightProperty().multiply(0.6));

                loadPlayerGrid(firstClick);

                //then rename the label to restart
                Start.setText("Restart");
            }

            //remove difficulty notification warning
            InternalIssueNoti.setText("");
        }else //if no difficulty was chosen, do nothing and tell user to set difficulty first
        {
            InternalIssueNoti.setText("Choose a difficulty first.");
        }
    }

    /**
     * Clears the grid and loads the player's view of the grid
     * @param firstClick Checks if the grid has been populated yet
     */
    private void loadPlayerGrid(boolean firstClick)
    {
        //remove any prior buttons already there
        MainGrid.getChildren().clear();

        //reset grid boundaries
        MainGrid.getColumnConstraints().remove(0, MainGrid.getColumnConstraints().size());
        MainGrid.getRowConstraints().remove(0, MainGrid.getRowConstraints().size());

        String label;
        String color;
        String labelColor;

        //begin to fill the grid with buttons with labels matching the player view of the grid
        for (int y = 0; y < game.getChosenDimY(); y++)
        {
            for (int x = 0; x < game.getChosenDimX(); x++)
            {
                //set the label and the color
                label = "";
                labelColor = RED;
                color = "-fx-base: " + GRAY; //default to unknown color

                //if player has not clicked yet, skip checks for other element types. Entire grid will be unknown
                if (firstClick == false)
                {
                    if (game.getPlayerElem(y, x) == 1)  //if the spot has been flagged by the player
                    {
                        label = "F";
                        labelColor = BLACK;
                        color = "-fx-base: " + RED;
                    } else if (game.getPlayerElem(y, x) == 0)   //if the spot has been cleared by the player
                    {
                        //update the label...
                        if (game.getRealElem(y, x) == 0)
                        {
                            label = "";
                        } else
                        {
                            label = "" + game.getRealElem(y, x);
                        }

                        //and the label color...
                        if (label.equals("1"))
                        {
                            labelColor = BLUE;
                        } else if (label.equals("2"))
                        {
                            labelColor = GREEN;
                        }   //if 3 or more mines adjacent, text is red

                        color = "-fx-base: " + WHITE;
                    }
                }

                //create a new button
                Button button = new Button(label);
                button.setStyle(color);
                button.setTextFill(Color.web(labelColor));
                
                //size the button to a portion of the window
                button.setMinSize(1, 1);
                button.prefWidthProperty().bind(MainGrid.widthProperty().multiply(game.getChosenDimX()));
                button.prefHeightProperty().bind(MainGrid.heightProperty().multiply(game.getChosenDimY()));
                MainGrid.add(button, x, y);     //coords are flipped for this, expects (x, y) not (y, x)
                button.setOnMouseClicked(new GridButtonClickedHandler(y, x, button));
            }
        }
    }

    /**
     * Clears the grid and loads the real view of the grid
     */
    private void loadRealGrid()
    {
        //remove any prior buttons already there
        MainGrid.getChildren().clear();

        //reset grid boundaries
        MainGrid.getColumnConstraints().remove(0, MainGrid.getColumnConstraints().size());
        MainGrid.getRowConstraints().remove(0, MainGrid.getRowConstraints().size());

        String label;
        String labelColor;
        String color;
        //Image img = new Image("naval-mine.jpg");
        //ImageView imgView;

        //begin to fill the grid with buttons with labels matching the real view of the grid
        for (int y = 0; y < game.getChosenDimY(); y++)
        {
            for (int x = 0; x < game.getChosenDimX(); x++)
            {
                //set the label and the color
                if (game.getRealElem(y, x) == -1)    //if the spot had a mine
                {
                    label = "M";
                    labelColor = BLACK;
                    color = "-fx-base: " + RED;

                    //imgView = new ImageView(img);

                } else  //if the spot is clear
                {
                    //set the label
                    label = "" + game.getRealElem(y, x);

                    if (label.equals("0"))
                    {
                        label = "";
                    }

                    labelColor = RED;

                    //and the label color...
                    if (label.equals("1"))
                    {
                        labelColor = BLUE;
                    } else if (label.equals("2"))
                    {
                        labelColor = GREEN;
                    }   //if 3 or more mines adjacent, text is red

                    //imgView = null;
                    color = "-fx-base: " + WHITE;
                }


                //create a new button
                Button button = new Button(label);
                button.setStyle(color);
                button.setTextFill(Color.web(labelColor));

                //size the button to a portion of the window
                button.setMinSize(1, 1);
                button.prefWidthProperty().bind(MainGrid.widthProperty().multiply(game.getChosenDimX()));
                button.prefHeightProperty().bind(MainGrid.heightProperty().multiply(game.getChosenDimY()));
                MainGrid.add(button, x, y);     //coords are flipped for this, expects (x, y) not (y, x)
            }
        }
    }

    /**
     * the event handler for the grid map button clicks
     */
    private class GridButtonClickedHandler implements EventHandler
    {
        private int row;
        private int column;
        private Button button;

        //constructor
        public GridButtonClickedHandler(int y, int x, Button button)
        {
            this.column = y;
            this.row = x;
            this.button = button;
        }

        /**
         * Updates the player grid view when buttons are pressed. Use left click to check, and right click to flag.
         *
         * @param event used to determine which mouse button was used to click
         */
        @Override
        public void handle(Event event)
        {
            //cast event into a MouseEvent to be able to determine which mouse button was clicked
            MouseEvent mEvent = (MouseEvent)event;

            //first make sure game is still in progress
            if (game.isRunning() == true)
            {
                //if right-clicked...
                if (mEvent.getButton() == MouseButton.SECONDARY && firstClick == false)
                {
                    if (game.getPlayerElem(column, row) == 2)   //if there wasn't a flag already, add it
                    {
                        game.changePlayerElem(column, row, 1);

                        //update the label and color
                        button.setText("F");
                        button.setStyle("-fx-base: " + RED);
                        button.setTextFill(Color.web(BLACK));
                    }else if (game.getPlayerElem(column, row) == 1)  //if there already was a flag, remove it
                    {
                        game.changePlayerElem(column, row, 2);

                        //update the label and color
                        button.setText("");
                        button.setStyle("-fx-base: " + GRAY);
                    }
                    //if spot was already cleared, do nothing
                } else if (mEvent.getButton() == MouseButton.PRIMARY && game.getPlayerElem(column, row) != 1)   //if left-clicked and no flag present...
                {
                    //if it is the first click of the game, populate the grid making sure to leave the clicked tile blank
                    if (firstClick == true)
                    {
                        game.populateGrid(column, row);
                        firstClick = false;
                    }

                    //make sure no flag or already cleared, otherwise do nothing (assuming is misclick)
                    if (game.getPlayerElem(column, row) != 1 && game.getPlayerElem(column, row) != 0 )
                    {
                        //if the spot has no mine, try to clear it
                        if (game.getRealElem(column, row) != -1)
                        {
                            //if it was a blank spot, check for spread
                            if (game.getRealElem(column, row) == 0)
                            {
                                revealSpread(column, row);
                                loadPlayerGrid(firstClick);
                            } else   //otherwise only have to change the element clicked on
                            {
                                cleared++;
                                game.changePlayerElem(column, row, 0);

                                //update the label and color
                                if (game.getRealElem(column, row) == 0)
                                {
                                    button.setText("");
                                } else
                                {
                                    button.setText("" + game.getRealElem(column, row));
                                }

                                if (button.getText().equals("1"))
                                {
                                    button.setTextFill(Color.web(BLUE));
                                } else if (button.getText().equals("2"))
                                {
                                    button.setTextFill(Color.web(GREEN));
                                } else
                                {
                                    button.setTextFill(Color.web(RED));
                                }

                                button.setStyle(WHITE);
                            }

                            //then check if the player has won
                            if (game.checkVictory(cleared) == true)
                            {
                                victoryNotification();
                                loadRealGrid();
                                game.end();
                            }
                        } else   //otherwise if the spot has a mine
                        {
                            //end the game and show the player the real board
                            gameOverNotification();
                            loadRealGrid();
                            game.end();
                        }
                    }
                }
            }
        }
    }

    /**
     * Given a tile coordinate, checks to see if it is clear and if so, reveal all neighboring clear tiles to the player.
     * ONLY UPDATES THE GAME MAP, NOT THE VISUAL GRID. MUST RELOAD GRID AFTER TO SHOW CHANGES
     * @param y The y coordinate of the tile to be checked
     * @param x The x coordinate of the tile to be checked
     */
    private void revealSpread(int y, int x)
    {
        //if a clear tile has been found, check his direct (no diagonal) neighbors to see if non mines. If so, reveal them to the player as well
        if (x < game.getChosenDimX() && x >= 0 &&
            y < game.getChosenDimY() && y >= 0 &&
            game.getPlayerElem(y, x) == 2)
        {
            //if it was also another blank, continue the spread
            if (game.getRealElem(y, x) == 0)
            {
                cleared++;
                game.changePlayerElem(y, x, 0);

                revealSpread(y, x - 1);
                revealSpread(y + 1, x - 1);
                revealSpread(y + 1, x);
                revealSpread(y + 1, x + 1);
                revealSpread(y, x + 1);
                revealSpread(y - 1, x + 1);
                revealSpread(y - 1, x);
                revealSpread(y - 1, x - 1);
            } else if (game.getPlayerElem(y, x) == 2)    //otherwise stop at that tile
            {
                cleared++;
                game.changePlayerElem(y, x, 0);
            }
        }
    }

    /**
     * creates an alert notifying user of loss
     */
    private void gameOverNotification()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Game over!");
        alert.setHeaderText("Game over!");
        alert.setContentText("You hit a mine!");
        alert.show();
    }

    /**
     * creates an alert notifying user of victory
     */
    private void victoryNotification()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victory!");
        alert.setHeaderText("Victory!");
        alert.setContentText("All mines have been cleared!");
        alert.show();
    }

    /**
     * When the "Fullscreen" option is selected in the menu>exit dropdown, toggle borderless windowed
     * @param event Not used
     */
    @FXML
    void onMenuFullscreenClicked(ActionEvent event)
    {
        //get the current stage
        Stage stage = (Stage)CurDiff.getScene().getWindow();
        //stage.setFullScreenExitHint("");

        //switch between fullscreen and windowed
        if (stage.isMaximized())
        {
            stage.setMaximized(false);
            stage.setHeight(300);
            stage.setWidth(700);
        }else
        {
            stage.setMaximized(true);
        }
    }

    /**
     * When the "Exit" option is selected in the menu>exit dropdown, close the program
     * @param event Not used
     */
    @FXML
    void onMenuExitClicked(ActionEvent event)
    {
        Platform.exit();
    }

    /**
     * When the "About" option is selected in the menu>help dropdown, show an "About" pop up
     * @param event Not used
     */
    @FXML
    void onAboutClicked(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the program");
        alert.setHeaderText("About the program");
        alert.setContentText("Author: Weikai Chen\n" +
                "Email: weikai.c@gmail.com\n" +
                "This is a recreation of the classic game Minesweeper. To clear a tile, " +
                "use left click. To flag a tile, right click. For more detailed instructions, " +
                "check the readme");
        alert.show();
    }
}