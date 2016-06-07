package gui;

import instances.Grid;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by mouton on 28/04/16.
 */
public class ColorButtonHandler implements EventHandler<ActionEvent> {

    GridDrawer drawer;

    public ColorButtonHandler(GridDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void handle(ActionEvent actionEvent) {


        Dialog<List<Integer>> dialog = new Dialog<>();
        dialog.setTitle("Choose color");

        // Set the button types.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        Label redValue = new Label("Red : ");
        Label greenValue = new Label("Green : ");
        Label blueValue = new Label("Blue : ");

        Color color = drawer.currentColor;
        int red = (int)(color.getRed() * 255);
        int blue = (int)(color.getBlue() * 255);
        int green = (int)(color.getGreen() * 255);

        TextField redTf = new TextField(""+red);
        redTf.setMinWidth(50);
        redTf.setMaxWidth(50);
        redTf.textProperty().addListener(getListener(redTf));

        TextField greenTf = new TextField(""+green);
        greenTf.setMinWidth(50);
        greenTf.setMaxWidth(50);
        greenTf.textProperty().addListener(getListener(greenTf));

        TextField blueTf = new TextField(""+blue);
        blueTf.setMinWidth(50);
        blueTf.setMaxWidth(50);
        blueTf.textProperty().addListener(getListener(blueTf));


        ToggleGroup group = new ToggleGroup();
        RadioButton rbRed = new RadioButton("Red");
        RadioButton rbGreen = new RadioButton("Green");
        RadioButton rbBlue = new RadioButton("Blue");
        RadioButton rbBlack = new RadioButton("Black");

        rbBlack.setSelected(true);
        rbBlack.setToggleGroup(group);
        rbBlack.setOnAction(getToggleColorHandler(redTf, greenTf, blueTf, Color.BLACK));

        rbRed.setToggleGroup(group);
        rbRed.setOnAction(getToggleColorHandler(redTf, greenTf, blueTf, Color.RED));
        rbGreen.setToggleGroup(group);
        rbGreen.setOnAction(getToggleColorHandler(redTf, greenTf, blueTf, Color.color(0,1,0)));
        rbBlue.setToggleGroup(group);
        rbBlue.setOnAction(getToggleColorHandler(redTf, greenTf, blueTf, Color.BLUE));

        grid.add(rbBlack, 0, 1);
        grid.add(redValue, 1, 0);
        grid.add(redTf, 2, 0);
        grid.add(rbRed, 1, 1);
        grid.add(greenValue, 3, 0);
        grid.add(greenTf, 4, 0);
        grid.add(rbGreen, 3, 1);
        grid.add(blueValue, 5, 0);
        grid.add(blueTf, 6, 0);
        grid.add(rbBlue, 5, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(Integer.valueOf(redTf.getText()));
                list.add(Integer.valueOf(greenTf.getText()));
                list.add(Integer.valueOf(blueTf.getText()));
                return list;
            }
            return null;
        });

        Optional<List<Integer>> result = dialog.showAndWait();

        result.ifPresent(exportParameters -> {
            double redR = exportParameters.get(0)/255D;
            double greenR = exportParameters.get(1)/255D;
            double blueR = exportParameters.get(2)/255D;
            this.drawer.currentColor = Color.color(redR, greenR, blueR);
        });

    }

    private static ChangeListener<String> getListener(TextField tf){
        return new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 3 || !newValue.matches("\\d*")) {
                    tf.setText(oldValue);
                }
            }
        };
    }

    private static EventHandler<ActionEvent> getToggleColorHandler(TextField redTf, TextField greenTf,
                                                                   TextField blueTf, Color color){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                redTf.setText(""+(int)(color.getRed()*255));
                greenTf.setText(""+(int)(color.getGreen()*255));
                blueTf.setText(""+(int)(color.getBlue()*255));
            }
        };
    }
}
