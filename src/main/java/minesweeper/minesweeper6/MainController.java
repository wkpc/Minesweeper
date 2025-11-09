package minesweeper.minesweeper6;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Label curDiff;

    @FXML
    private Button difficultyEasy;

    @FXML
    private Button difficultyHard;

    @FXML
    private Button difficultyNormal;

    @FXML
    private Button start;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    void onDifficultyHardClicked(MouseEvent event)
    {
        curDiff.setText("Hard");
    }

    @FXML
    void onDifficultyNormalClicked(MouseEvent event)
    {
        curDiff.setText("Normal");
    }

    @FXML
    void onDifficultyEasyClicked(MouseEvent event)
    {
        curDiff.setText("Easy");
    }
}