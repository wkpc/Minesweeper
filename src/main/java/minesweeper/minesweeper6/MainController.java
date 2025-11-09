package minesweeper.minesweeper6;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
    private Font x3;

    @FXML
    private Color x4;

    private Game game;
    private String difficulty;

    @FXML
    void initialize()
    {
        //start with the game being empty
        game = null;
        InternalIssueNoti.setText("");
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

    }
}