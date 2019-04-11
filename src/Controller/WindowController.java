package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class WindowController {


    @FXML
    private Button loadBtn;

    @FXML
    private GridPane matrixGridPane;

    @FXML
    public void initialize(){

        loadBtn.setText("Load");

    }
}
