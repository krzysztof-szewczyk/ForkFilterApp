package Controller;

import Model.Filters.FiltersImpl.CustomMatrixFilter;
import Model.Filters.FiltersImpl.NegativeFilter;
import Model.Filters.FiltersImpl.CustomFilter;
import Model.Filters.FiltersImpl.SepiaFilter;
import Model.Filters.FilterInterface.Filter;
import Model.Filters.FilterManager;
import Model.Properties.MyProperties;
import Model.Validators.IntegerValidator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
    private TextField thresholdTxt;

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
    private Pane originalImagePane;

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

    @FXML
    private Slider contrastSlider;

    @FXML
    private Label contrastLabel;

    private List filterValueList = new ArrayList<TextField>();

    private MyProperties myProperty = new MyProperties();

    private ForkJoinPool forkJoinPool = new ForkJoinPool();

    private ColorAdjust ca = new ColorAdjust();

    private ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }

    public void setForkJoinPool() {
        this.forkJoinPool = new ForkJoinPool(Integer.parseInt(parallelismLevelTxt.getText()));
    }

    @FXML
    public void initialize(){
        // Init image loading
        printNewImage(new Image("file:..\\..\\resources\\eif.JPG"));
        // Tooltips
        newFilterSizeTxt.setTooltip(new Tooltip("Set 3, 5 or 7"));
        redSlider.setTooltip(new Tooltip("Set value to add"));
        greenSlider.setTooltip(new Tooltip("Set value to add"));
        blueSlider.setTooltip(new Tooltip("Set value to add"));
        parallelismLevelTxt.setTooltip(new Tooltip("Set pool. Default the number of processors"));
        thresholdTxt.setTooltip(new Tooltip("Only under this threshold tasks are processed sequentially"));
        filterChoiceBox.setTooltip(new Tooltip("Select filter"));
        //Init
        loadBtn.setText("Load");
        newFilterSizeTxt.setText("3");
        filterChoiceBox.getItems().setAll("Custom Pixel", "Custom Matrix", "Negative","Sepia");
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
        thresholdTxt.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(),
                        100,
                        new IntegerValidator("-?^([1-9]|[1-9][0-9]*)?")));
        // Set brightness and contrast
        contrastSlider.valueProperty().bindBidirectional(ca.contrastProperty());
        filteredImage.setEffect(ca);
        contrastLabel.textProperty().bindBidirectional(contrastSlider.valueProperty(), new NumberStringConverter());

        /*
         *  Binding sliders to Doubleproperties binded to IntegerProperty binded to RgbLabels
         */
        // Red binding
        redSlider.valueProperty().bindBidirectional(myProperty.stringToDoubleProperty());
        myProperty.stringToDoubleProperty().bindBidirectional(myProperty.doubleToIntProperty());
        redLabel.textProperty().bind(myProperty.doubleToIntProperty().asString());
        // Green binding
        greenSlider.valueProperty().bindBidirectional(myProperty.stringToDoubleGreenProperty());
        myProperty.stringToDoubleGreenProperty().bindBidirectional(myProperty.doubleToIntGreenProperty());
        greenLabel.textProperty().bind(myProperty.doubleToIntGreenProperty().asString());
        // Blue binding
        blueSlider.valueProperty().bindBidirectional(myProperty.stringToDoubleBlueProperty());
        myProperty.stringToDoubleBlueProperty().bindBidirectional(myProperty.doubleToIntBlueProperty());
        blueLabel.textProperty().bind(myProperty.doubleToIntBlueProperty().asString());
        // Align images to each other
        filteredImage.fitWidthProperty().bindBidirectional(originalImage.fitWidthProperty());
        filteredImage.fitHeightProperty().bindBidirectional(originalImage.fitHeightProperty());
        // Fit ImageViews to parent panes
        originalImage.fitWidthProperty().bind(originalImagePane.widthProperty());
        originalImage.fitHeightProperty().bind(originalImagePane.heightProperty());
        // Init matrix values
        changeFilterMatrix();
    }

    @FXML
    private void setFilteredAsOriginal(){
        printNewImage(filteredImage.getImage());
    }

    @FXML
    private void resetSliders(){
        redSlider.setValue(0);
        greenSlider.setValue(0);
        blueSlider.setValue(0);
    }

    @FXML
    private void resetContrast(){
        contrastSlider.setValue(0.0);
    }

    @FXML
    private void changeFilterMatrix(){
        boolean notNum = false;
        String str = newFilterSizeTxt.getText();
        if(str.equals("") || str.isEmpty()){
            notNum = true;
        }

        if(!notNum){
            // clear old fields
            filterValueList.clear();

            final int n = Integer.parseInt(newFilterSizeTxt.getText());
            final int txtSize = 25;
            final int margin = 2;

            matrixGridPane.getChildren().clear();

            matrixGridPane.setPrefSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);
            matrixGridPane.setMinSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);
            matrixGridPane.setMaxSize(n*(txtSize+margin)+margin, n*(txtSize+margin)+margin);

            matrixGridPane.getColumnConstraints().clear();
            matrixGridPane.getRowConstraints().clear();
            int index = 0;
            for(int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    final TextField tf = new TextField();
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
        if(filterChoiceBox.getSelectionModel().getSelectedItem().equals("Custom Pixel")){
            RGBBox.setDisable(false);
            matrixGridPane.setDisable(true);
            newFilterSizeTxt.setDisable(true);
        }else if(filterChoiceBox.getSelectionModel().getSelectedItem().equals("Custom Matrix")){
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
    private void openFileChooser(){
        final FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter(".jpg", "*.jpg"),
                new FileChooser.ExtensionFilter(".png", "*.png")
        );

        final File startDirectory = new File(System.getProperty("user.home") + "/Desktop");

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
            final Image image = new Image(new FileInputStream(loadTxt.getText()));
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
    private void filter(){

        // counter of recursive actions (tasks)
        FilterManager.i = 0;

        ForkJoinPool fjp = getForkJoinPool();
        int pool = Integer.parseInt(parallelismLevelTxt.getText());
        if(fjp.getParallelism() != pool){
            fjp = new ForkJoinPool(pool);
        }


        final BufferedImage bi = SwingFXUtils.fromFXImage(originalImage.getImage(),null);
        final String filterName = filterChoiceBox.getSelectionModel().getSelectedItem();
        Filter filter = null;

        switch (filterName){
            case "Negative":
                filter = new NegativeFilter();
                break;
            case "Sepia":
                filter = new SepiaFilter();
                break;
            case "Custom Matrix":
                final int size = Integer.parseInt(newFilterSizeTxt.getText());
                final int[][] filterValues = new int[size][size];
                int index = 0;
                for(int i = 0; i<size; i++){
                    for(int j = 0; j<size; j++){
                        filterValues[i][j] = Integer.parseInt(((TextField) filterValueList.get(index)).getText());
                        index++;
                    }
                }
                filter = new CustomMatrixFilter(filterValues, originalImage.getImage().getPixelReader());
                break;
            case "Custom Pixel":
                final int[] addRgb = {(int)redSlider.getValue(), (int)greenSlider.getValue(), (int)blueSlider.getValue()};
                filter = new CustomFilter(addRgb);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Select correct filter");
        }

        if(filter != null){

            final long beginT = System.nanoTime();
            final FilterManager fm = new FilterManager(bi, filter, 0, (int)originalImage.getImage().getWidth(), 0, (int)originalImage.getImage().getHeight(), Integer.parseInt(thresholdTxt.getText()));
            fjp.invoke(fm);
            filteredImage.setImage(SwingFXUtils.toFXImage(bi, null));
            tasksLabel.setText("Tasks: " + FilterManager.i);
            final long endT = System.nanoTime();
            processingTimeLabel.setText("Processing time: "+(endT-beginT)/1000000+"ms");
        }
    }
}
