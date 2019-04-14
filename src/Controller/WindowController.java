package Controller;

import Model.FilterInterface.AbstractMultiPixelFilterModel.FilterImpl.CustomMatrixFilter;
import Model.FilterInterface.AbstractSinglePixelFilterModel.FilterImpl.NegativeFilter;
import Model.FilterInterface.AbstractSinglePixelFilterModel.FilterImpl.CustomFilter;
import Model.FilterInterface.AbstractSinglePixelFilterModel.FilterImpl.SepiaFilter;
import Model.FilterInterface.Filter;
import Model.GuiModel.MyProperties;
import Model.Validator.IntegerValidator;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

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
    private Label tasksLabel;

    @FXML
    private TextField parallelismLevelTxt;

    @FXML
    private TextField loadTxt;

    @FXML
    private HBox RGBBox;

    @FXML
    private Slider redSlider;

    @FXML
    private Label redLabel;

    @FXML
    private Slider greenSlider;

    @FXML
    private Label greenLabel;

    @FXML
    private Slider blueSlider;

    @FXML
    private Label blueLabel;

    private List filterValueList = new ArrayList<TextField>();

    private MyProperties redSlidersPropertiesObject = new MyProperties();

    private MyProperties greenSlidersPropertiesObject = new MyProperties();

    private MyProperties blueSlidersPropertiesObject = new MyProperties();

    private ForkJoinPool forkJoinPool = new ForkJoinPool();

    public ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }

    public void setForkJoinPool() {
        this.forkJoinPool = new ForkJoinPool(Integer.parseInt(parallelismLevelTxt.getText()));
    }

    @FXML
    public void initialize(){

        // Tooltips
        newFilterSizeTxt.setTooltip(new Tooltip("Set 3, 5 or 7"));
        redSlider.setTooltip(new Tooltip("Set value to add"));
        greenSlider.setTooltip(new Tooltip("Set value to add"));
        blueSlider.setTooltip(new Tooltip("Set value to add"));
        parallelismLevelTxt.setTooltip(new Tooltip("Set pool. Default the number of processors"));
        thresholdLabel.setTooltip(new Tooltip("Only under this threshold tasks are processed sequentially"));
        filterChoiceBox.setTooltip(new Tooltip("Select filter"));

        //Init
        loadBtn.setText("Load");
        newFilterSizeTxt.setText("3");
        filterChoiceBox.getItems().setAll("Custom","Negative","Sepia","Matrix");
        filterChoiceBox.getSelectionModel().selectFirst();
        filterChoiceBox.setOnAction(this::toggleMatrixEnable);
        sizeLabel.setText("Size: " + (int) originalImage.getImage().getWidth() + "x" + (int) originalImage.getImage().getHeight() + "px");
        // Set filter size text field validator
        newFilterSizeTxt.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 3, new IntegerValidator("-?([1|3|5|7])?")));
        // Set parallelism level text field validator
        parallelismLevelTxt.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(),
                        forkJoinPool.getParallelism(),
                        new IntegerValidator("-?^([1-9]|[1-2][0-9]{0,4}|3[0-9]{0,3}|3[0-1][0-9]{0,3}|32[0-6][0-9]{0,2}|327[0-5][0-9]|3276[0-7])?")));

        /**
         *  Binding sliders to Doubleproperties binded to IntegerProperty binded to RgbLabels
        */
        // Red binding
        redSlider.valueProperty().bindBidirectional(redSlidersPropertiesObject.stringToDoubleProperty());
        redSlidersPropertiesObject.stringToDoubleProperty().bindBidirectional(redSlidersPropertiesObject.doubleToIntProperty());
        redLabel.textProperty().bind(redSlidersPropertiesObject.doubleToIntProperty().asString());
        // Green binding
        greenSlider.valueProperty().bindBidirectional(greenSlidersPropertiesObject.stringToDoubleProperty());
        greenSlidersPropertiesObject.stringToDoubleProperty().bindBidirectional(greenSlidersPropertiesObject.doubleToIntProperty());
        greenLabel.textProperty().bind(greenSlidersPropertiesObject.doubleToIntProperty().asString());
        // Blue binding
        blueSlider.valueProperty().bindBidirectional(blueSlidersPropertiesObject.stringToDoubleProperty());
        blueSlidersPropertiesObject.stringToDoubleProperty().bindBidirectional(blueSlidersPropertiesObject.doubleToIntProperty());
        blueLabel.textProperty().bind(blueSlidersPropertiesObject.doubleToIntProperty().asString());
        // Align images to each other
        filteredImage.fitWidthProperty().bindBidirectional(originalImage.fitWidthProperty());

        changeFilterMatrix();
    }

    @FXML
    public void setFilteredAsOriginal(){
        printNewImage(filteredImage.getImage());
    }

    @FXML
    public void resetSliders(){
        redSlider.setValue(0);
        greenSlider.setValue(0);
        blueSlider.setValue(0);
    }

    @FXML
    public void changeFilterMatrix(){
        boolean notNum = false;
        String str = newFilterSizeTxt.getText();
        if(str.equals("") || str.isEmpty()){
            notNum = true;
        }

        if(!notNum){

            // clear old fields
            filterValueList.clear();

            int n = Integer.parseInt(newFilterSizeTxt.getText());
            int txtSize = 25;
            int margin = 2;

            matrixGridPane.getChildren().clear();

            matrixGridPane.setPrefSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);
            matrixGridPane.setMinSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);
            matrixGridPane.setMaxSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);

            matrixGridPane.getColumnConstraints().clear();
            matrixGridPane.getRowConstraints().clear();
            int index = 0;
            for(int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    TextField tf = new TextField();
                    tf.setPrefSize(txtSize, txtSize);
                    tf.setMaxSize(txtSize, txtSize);
                    tf.setMinSize(txtSize, txtSize);
                    tf.setFont(Font.font(10));
                    tf.setAlignment(Pos.CENTER);
                    tf.setStyle("-fx-background-color: #6699cc;");
                    GridPane.setConstraints(tf, i, j);
                    GridPane.setMargin(tf, new Insets(2, 2, 2, 2));
                    matrixGridPane.getChildren().add(tf);
                    tf.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 1, new IntegerValidator("-?((-[1-9])|[0-9])?")));
                    filterValueList.add(matrixGridPane.getChildren().get(index));
                    index++;
                }
            }
        }
    }

    @FXML
    private void toggleMatrixEnable(ActionEvent actionEvent){
        if(filterChoiceBox.getSelectionModel().getSelectedIndex() == 0){
            RGBBox.setDisable(false);
            matrixGridPane.setDisable(true);
            newFilterSizeTxt.setDisable(true);
        }else if(filterChoiceBox.getSelectionModel().getSelectedIndex() == 3){
            matrixGridPane.setDisable(false);
            newFilterSizeTxt.setDisable(false);
            RGBBox.setDisable(true);
        }
        else {
            matrixGridPane.setDisable(true);
            newFilterSizeTxt.setDisable(true);
            RGBBox.setDisable(true);
        }
    }

    @FXML
    public void openFileChooser(){
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter(".jpg", "*.jpg"),
                new FileChooser.ExtensionFilter(".png", "*.png")
        );

        File startDirectory = new File(System.getProperty("user.home") + "/Desktop");

        if(startDirectory.exists())
            fc.setInitialDirectory(startDirectory);

        File selectedFile= fc.showOpenDialog( null);

        if(selectedFile != null) {
            loadTxt.setText(selectedFile.getAbsolutePath());
            printNewImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    private void searchImageByPath(){
        try {
            Image image = new Image(new FileInputStream(loadTxt.getText()));
            printNewImage(image);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Could not find image");
        }
    }

    private  void printNewImage(Image image){
        originalImage.setImage(image);
        filteredImage.setImage(image);
        sizeLabel.setText("Size: " + (int) originalImage.getImage().getWidth() + "x" + (int) originalImage.getImage().getHeight() + "px");

    }

    @FXML
    public void filter(){

        // counter of recursive actions (tasks)
        FilterManager.i = 0;

        ForkJoinPool fjp = getForkJoinPool();
        int pool = Integer.parseInt(parallelismLevelTxt.getText());
        if(fjp.getParallelism() != pool){
            fjp = new ForkJoinPool(pool);
        }


        BufferedImage bi = SwingFXUtils.fromFXImage(originalImage.getImage(),null);
        String filterName = filterChoiceBox.getSelectionModel().getSelectedItem();
        Filter filter = null;

        switch (filterName){
            case "Negative":
                filter = new NegativeFilter();
                break;
            case "Sepia":
                filter = new SepiaFilter();
                break;
            case "Matrix":
                int size = Integer.parseInt(newFilterSizeTxt.getText());
                int[][] filterValues = new int[size][size];
                int index = 0;
                for(int i = 0; i<size; i++){
                    for(int j = 0; j<size; j++){
                        filterValues[i][j] = Integer.parseInt(((TextField) filterValueList.get(index)).getText());
                        index++;
                    }
                }
                filter = new CustomMatrixFilter(filterValues, originalImage.getImage().getPixelReader());
                break;
            case "Custom":
                int[] addRgb = {(int)redSlider.getValue(), (int)greenSlider.getValue(), (int)blueSlider.getValue()};
                filter = new CustomFilter(addRgb);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Select correct filter");
        }

        if(!filter.equals(null)){

            long beginT = System.nanoTime();
            FilterManager fm = new FilterManager(bi, filter, 0, (int)originalImage.getImage().getWidth(), 0, (int)originalImage.getImage().getHeight(), Integer.parseInt(thresholdLabel.getText()));
            fjp.invoke(fm);
            filteredImage.setImage(SwingFXUtils.toFXImage(bi, null));
            tasksLabel.setText("Tasks: " + FilterManager.i);
            long endT = System.nanoTime();
            processingTimeLabel.setText("Processing time: "+(endT-beginT)/1000000+"ms");
        }
    }
}
