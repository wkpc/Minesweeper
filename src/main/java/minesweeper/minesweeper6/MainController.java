package minesweeper.minesweeper6;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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

    @FXML
    void initialize()
    {
        //start with the game being empty
        game = null;
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
                    color = "-fx-base: #DCDCDC;";
                }

                //create a new button
                Button button = new Button(label);
                button.setStyle(color);
                button.setMinSize(30, 30);
                MainGrid.add(button, x, y);
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
            //cast even into a MouseEvent to be able to determine which mouse button was clicked
            MouseEvent mEvent = (MouseEvent)event;
        }
    }
}