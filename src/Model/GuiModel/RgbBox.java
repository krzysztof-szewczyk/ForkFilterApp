package Model.GuiModel;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class RgbBox {
    private IntegerProperty integerProperty = new SimpleIntegerProperty(0);

    public int getIntegerProperty() {
        return integerProperty.get();
    }

    public IntegerProperty integerPropertyProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(int integerProperty) {
        this.integerProperty.set(integerProperty);
    }
}
