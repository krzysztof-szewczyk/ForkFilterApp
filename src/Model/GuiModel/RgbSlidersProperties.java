package Model.GuiModel;

import javafx.beans.property.*;

public class RgbSlidersProperties {
    private IntegerProperty ip = new SimpleIntegerProperty();
    private DoubleProperty dp = new SimpleDoubleProperty();



    public IntegerProperty ipProperty() {
        return ip;
    }

    public DoubleProperty dpProperty() {
        return dp;
    }
}
