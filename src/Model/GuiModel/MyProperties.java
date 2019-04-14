package Model.GuiModel;

import javafx.beans.property.*;

public class MyProperties {
    private IntegerProperty doubleToInt = new SimpleIntegerProperty();
    private DoubleProperty stringToDouble = new SimpleDoubleProperty();

    public IntegerProperty doubleToIntProperty() {
        return doubleToInt;
    }

    public DoubleProperty stringToDoubleProperty() {
        return stringToDouble;
    }
}
