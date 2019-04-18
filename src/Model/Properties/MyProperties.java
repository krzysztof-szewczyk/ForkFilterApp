package Model.Properties;

import javafx.beans.property.*;

final public class MyProperties {

    // RED
    final private IntegerProperty doubleToInt = new SimpleIntegerProperty();
    final private DoubleProperty stringToDouble = new SimpleDoubleProperty();
    final public IntegerProperty doubleToIntProperty() {
        return doubleToInt;
    }
    final public DoubleProperty stringToDoubleProperty() {
        return stringToDouble;
    }

    // GREEN
    final private IntegerProperty doubleToIntGreen = new SimpleIntegerProperty();
    final private DoubleProperty stringToDoubleGreen = new SimpleDoubleProperty();
    final public IntegerProperty doubleToIntGreenProperty() {
        return doubleToIntGreen;
    }
    final public DoubleProperty stringToDoubleGreenProperty() {
        return stringToDoubleGreen;
    }

    // BLUE
    final private IntegerProperty doubleToIntBlue = new SimpleIntegerProperty();
    final private DoubleProperty stringToDoubleBlue = new SimpleDoubleProperty();
    final public IntegerProperty doubleToIntBlueProperty() {
        return doubleToIntBlue;
    }
    final public DoubleProperty stringToDoubleBlueProperty() {
        return stringToDoubleBlue;
    }

    // TASKS
    final private IntegerProperty thresholdToTasks = new SimpleIntegerProperty();
    public IntegerProperty thresholdToTasksProperty() {
        return thresholdToTasks;
    }
    final private IntegerProperty doubleToIntTasks = new SimpleIntegerProperty();

    public int getDoubleToIntTasks() {
        return doubleToIntTasks.get();
    }

    public IntegerProperty doubleToIntTasksProperty() {
        return doubleToIntTasks;
    }

    public void setDoubleToIntTasks(int doubleToIntTasks) {
        this.doubleToIntTasks.set(doubleToIntTasks);
    }
}
