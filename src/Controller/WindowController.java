package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;



public class WindowController {


    @FXML
    private Button loadBtn;

    @FXML
    private TextField newFilterSizeTxt;

    @FXML
    private GridPane matrixGridPane;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    @FXML
    private ImageView originalImage;

    @FXML
    private Label sizeLabel;

    @FXML
    public void initialize(){

        loadBtn.setText("Load");
        newFilterSizeTxt.setText("3");
        filterChoiceBox.getItems().setAll("Custom","Filter1","Filter2","Filter3");
        filterChoiceBox.getSelectionModel().selectFirst();
        filterChoiceBox.setOnAction(this::toggleMatrixEnable);
        sizeLabel.setText("Size: " + (int) originalImage.getImage().getWidth() + "x" + (int) originalImage.getImage().getHeight() + "px");
        System.out.println(originalImage.getImage().getHeight());
        System.out.println(originalImage.getImage().getWidth());

    }

    @FXML
    public void changeFilterMatrix(){
        boolean notNum = false;
        String str = newFilterSizeTxt.getText();
        if(str.equals("") || str.isEmpty()){
            notNum = true;
        }else{
            for(char c : str.toCharArray()){
                if(c < '1' || c > '5'){
                    notNum = true;
                }
            }
        }

        if(!notNum){
            int n = Integer.parseInt(newFilterSizeTxt.getText());
            int txtSize = 25;
            int margin = 2;

            matrixGridPane.getChildren().clear();

            matrixGridPane.setPrefSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);
            matrixGridPane.setMinSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);
            matrixGridPane.setMaxSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);

            matrixGridPane.getColumnConstraints().clear();
            matrixGridPane.getRowConstraints().clear();

            for(int i = 0 ; i<n ; i++) {
                for (int j = 0; j < n; j++) {
                    TextField tf = new TextField();
                    tf.setText("3");
                    tf.setPrefSize(txtSize, txtSize);
                    tf.setMaxSize(txtSize, txtSize);
                    tf.setMinSize(txtSize, txtSize);
                    tf.setFont(Font.font(10));
                    tf.setAlignment(Pos.CENTER);
                    tf.setStyle("-fx-background-color: #6699cc;");
                    GridPane.setConstraints(tf, i, j);
                    GridPane.setMargin(tf, new Insets(2, 2, 2, 2));
                    matrixGridPane.getChildren().add(tf);
                }
            }
        }
    }

    @FXML
    public void toggleMatrixEnable(ActionEvent actionEvent){
        if(filterChoiceBox.getSelectionModel().getSelectedIndex() != 0){
            matrixGridPane.setDisable(true);
            newFilterSizeTxt.setDisable(true);
        }
        else {
            matrixGridPane.setDisable(false);
            newFilterSizeTxt.setDisable(false);
        }
    }
}
