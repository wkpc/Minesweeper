package minesweeper.minesweeper6;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MainController {
    @FXML
    private Label InternalIssueNoti;

    @FXML
    private Label UserActionNoti;

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
    private String difficulty;
    private int cleared;

    @FXML
    void initialize()
    {
        //start with the game being empty
        game = null;
        cleared = 0;
        InternalIssueNoti.setText("");
        MainGrid.getChildren().clear();
    }

    @FXML
    void onDifficultyHardClicked(MouseEvent event)
    {
        CurDiff.setText("Hard");
        difficulty = "Hard";
    }

    @FXML
    void onDifficultyNormalClicked(MouseEvent event)
    {
        CurDiff.setText("Normal");
        difficulty = "Normal";
    }

    @FXML
    void onDifficultyEasyClicked(MouseEvent event)
    {
        CurDiff.setText("Easy");
        difficulty = "Easy";
    }

    @FXML
    void onStartClicked(MouseEvent event)
    {
        //make sure a difficulty was selected before making a new game
        if (difficulty != null)
        {
            game = new Game(difficulty);
            cleared = 0;
            loadGrid();

            //then rename the label to restart
            Start.setText("Restart");
        }else //if no difficulty was chosen, do nothing and tell user to set difficulty first
        {
            InternalIssueNoti.setText("Choose a difficulty first.");
        }
    }

    /**
     * clears the grid and loads the grid again
     */
    private void loadGrid()
    {
        //remove any prior buttons already there
        MainGrid.getChildren().clear();

        //reset grid boundaries
        MainGrid.getColumnConstraints().remove(0, MainGrid.getColumnConstraints().size());
        MainGrid.getRowConstraints().remove(0, MainGrid.getRowConstraints().size());

        //begin to fill the grid with buttons with labels matching the player view of the grid
        for (int x = 0; x < game.getChosenDim(); x++)
        {
            for (int y = 0; y < game.getChosenDim(); y++)
            {
                //set the label and the color
                String label = "";
                String color = "-fx-base: #969696;"; //default to unknown color

                if (game.getPlayerElem(x, y) == 1)  //if the spot has been flagged by the player
                {
                    label = "F";
                    color = "-fx-base: #C23434;";
                } else if (game.getPlayerElem(x, y) == 0)   //if the spot has been cleared by the player
                {
                    //update the label and color
                    if (game.getRealElem(x, y) == 0)
                    {
                        label = "";
                    }else
                    {
                        label = "" + game.getRealElem(x, y);
                    }
                    color = "-fx-base: #DCDCDC;";
                }

                //create a new button
                Button button = new Button(label);
                button.setStyle(color);
                button.setMinSize(30, 30);
                MainGrid.add(button, x, y);
                button.setOnMouseClicked(new GridButtonClickedHandler(x, y, button));
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
        public GridButtonClickedHandler(int x, int y, Button button)
        {
            this.column = x;
            this.row = y;
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
                if (mEvent.getButton() == MouseButton.SECONDARY)
                {
                    if (game.getPlayerElem(column, row) == 2)   //if there wasn't a flag already, add it
                    {
                        game.changePlayerElem(column, row, 1);

                        //update the label and color
                        button.setText("F");
                        button.setStyle("-fx-base: #C23434;");
                    }else if (game.getPlayerElem(column, row) == 1)  //if there already was a flag, remove it
                    {
                        game.changePlayerElem(column, row, 2);

                        //update the label and color
                        button.setText("");
                        button.setStyle("-fx-base: #969696;");
                    }
                    //if spot was already cleared, do nothing
                } else if (mEvent.getButton() == MouseButton.PRIMARY && game.getPlayerElem(column, row) != 1)   //if left-clicked and no flag present...
                {
                    //if the spot has no mine, clear it
                    if (game.getRealElem(column, row) != -1)
                    {
                        game.changePlayerElem(column, row, 0);
                        cleared++;

                        //update the label and color
                        if (game.getRealElem(column, row) == 0)
                        {
                            button.setText("");
                        }else
                        {
                            button.setText("" + game.getRealElem(column, row));
                        }
                        button.setStyle("-fx-base: #DCDCDC;");
                        System.out.println("cleared so far: " + cleared);

                        //then check if the player has won
                        if (game.checkVictory(cleared) == true)
                        {
                            victoryNotification();
                        }
                    }else   //otherwise if the spot has a mine
                    {
                        //end the game
                        gameOverNotification();
                        game.end();
                    }
                }
            }
        }
    }

    private void gameOverNotification()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Game over!");
        alert.setHeaderText("Game over!");
        alert.setContentText("You hit a mine!");
        alert.show();
    }

    private void victoryNotification()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victory!");
        alert.setHeaderText("Victory!");
        alert.setContentText("All mines have been cleared!");
        alert.show();
    }
}