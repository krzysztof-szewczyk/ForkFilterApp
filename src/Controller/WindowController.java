package Controller;

import Model.Filter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;


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
    private ImageView filteredImage;

    @FXML
    private Label sizeLabel;

    @FXML
    private TextField thresholdLabel;

    @FXML
    private Label processingTimeLabel;


    @FXML
    public void initialize(){

        loadBtn.setText("Load");
        newFilterSizeTxt.setText("3");
        filterChoiceBox.getItems().setAll("Custom","Filter1","Filter2","Filter3");
        filterChoiceBox.getSelectionModel().selectFirst();
        filterChoiceBox.setOnAction(this::toggleMatrixEnable);
        sizeLabel.setText("Size: " + (int) originalImage.getImage().getWidth() + "x" + (int) originalImage.getImage().getHeight() + "px");
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

    @FXML
    public void openFileChooser(){
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\Ozii\\Desktop"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File selectedFile = fc.showOpenDialog(null);

        if(selectedFile != null) {
            originalImage.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    public void filter(){
        BufferedImage bi = SwingFXUtils.fromFXImage(originalImage.getImage(),null);
        Filter task = new Filter(bi, 0, (int)originalImage.getImage().getWidth(), 0, (int)originalImage.getImage().getHeight(), Integer.parseInt(thresholdLabel.getText()));
        long beginT = System.nanoTime();
        task.invoke();
        long endT = System.nanoTime();
        processingTimeLabel.setText("Processing time: "+(endT-beginT)/1000000+"ms");
        filteredImage.setImage(SwingFXUtils.toFXImage(bi, null));
    }
}
