package gui;

import instances.Grid;
import instances.GridT;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by mouton on 28/04/16.
 */
public class NewSaveLoadButtonHandler implements EventHandler<ActionEvent> {

    GridDrawer drawer;
    FileChooser fileChooser;

    public NewSaveLoadButtonHandler(GridDrawer drawer) {
        this.drawer = drawer;
        this.fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("GridFiles", "*.grid"));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(drawer.newButton) || actionEvent.getSource().equals(drawer.paramsButton)) {
            Dialog<List<Integer>> dialog = new Dialog<>();
            dialog.setTitle("How many lines and columns?");

            // Set the button types.
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField sizeX = new TextField();
            sizeX.setText("" + drawer.grid.getSizex());
            TextField sizeY = new TextField();
            sizeY.setText("" + drawer.grid.getSizey());
            TextField length = new TextField();
            length.setText("" + drawer.LENGTH);

            sizeX.textProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        sizeX.setText(oldValue);
                    }
                }
            });

            sizeY.textProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        sizeY.setText(oldValue);
                    }
                }
            });

            length.textProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        length.setText(oldValue);
                    }
                }
            });

            grid.add(new Label("Lignes:"), 0, 0);
            grid.add(sizeX, 1, 0);
            grid.add(new Label("Colonnes:"), 0, 1);
            grid.add(sizeY, 1, 1);
            grid.add(new Label("Largeur pixels:"), 0, 2);
            grid.add(length, 1, 2);


            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(Integer.valueOf(sizeX.getText()));
                    list.add(Integer.valueOf(sizeY.getText()));
                    list.add(Integer.valueOf(length.getText()));
                    return list;
                }
                return null;
            });

            Optional<List<Integer>> result = dialog.showAndWait();

            result.ifPresent(linesColumns -> {
                GridT<Color> g = drawer.grid;
                drawer.grid = new GridT<Color>(linesColumns.get(0), linesColumns.get(1));
                GridDrawer.LENGTH = linesColumns.get(2);
                if(actionEvent.getSource().equals(drawer.paramsButton)){
                    for(int line = 0; line < Math.min(g.getSizex(), drawer.grid.getSizex()); line++)
                        for(int column = 0; column < Math.min(g.getSizey(), drawer.grid.getSizey()); column++){
                            Color c = g.getPoint(line, column);
                            if(c != null)
                                drawer.grid.addPoint(line, column, c);
                        }
                }
                drawer.reinitDraw();
            });
        }
        else if(actionEvent.getSource().equals(drawer.saveButton)) {
            fileChooser.setTitle("Choose destination.");
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                if(!selectedFile.toPath().toString().matches(".*\\.grid"))
                    selectedFile = new File(selectedFile.getPath()+".grid");
                save(selectedFile);
            }
        }
        else if(actionEvent.getSource().equals(drawer.loadButton)) {
            fileChooser.setTitle("Choose destination.");
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                if (!selectedFile.toPath().toString().matches(".*\\.grid"))
                    return;
                load(selectedFile);
            }
        }
    }

    public void save(File f){
        GridT<Color> g = drawer.grid;
        Integer length = GridDrawer.LENGTH;

        FileManager fm = new FileManager();
        fm.openErase(f.getPath());

        fm.writeln(g.getSizex()+" "+g.getSizey()+" "+length);
        fm.writeln();
        for(int line = 0; line < g.getSizex(); line++) {
            for (int column = 0; column < g.getSizey(); column++){
                Color color = g.getPoint(line, column);
                int red, green, blue;
                if(color == null){
                    red = -1;
                    green = -1;
                    blue = -1;
                }
                else{
                    red = (int)(color.getRed()*255);
                    green = (int)(color.getGreen()*255);
                    blue = (int)(color.getBlue()*255);
                }
                fm.write(red+" "+green+" "+blue+" ");
            }
            fm.writeln();
            fm.flush();
        }
        fm.closeWrite();
    }

    public void load(File f){
        FileManager fm = new FileManager();
        fm.openRead(f.getPath());

        String fileLine = fm.readLine();
        String[] size = fileLine.split("\\s+");
        int sizeX = Integer.valueOf(size[0]);
        int sizeY = Integer.valueOf(size[1]);
        int length = Integer.valueOf(size[2]);

        fm.readLine();

        GridT<Color> g = new GridT<Color>(sizeX, sizeY);
        for(int line = 0; line < g.getSizex(); line++) {
            fileLine = fm.readLine();
            fileLine = fileLine.trim();
            String[] points = fileLine.split("\\s+");
            for (int column = 0; column < g.getSizey(); column++){
                if(points[3 * column].equals("-1"))
                    continue;
                double red = Integer.valueOf(points[3 * column])/255D;
                double green = Integer.valueOf(points[3 * column + 1])/255D;
                double blue = Integer.valueOf(points[3 * column + 2])/255D;

                g.addPoint(line, column, Color.color(red, green, blue));

            }
        }

        drawer.grid = g;
        GridDrawer.LENGTH = length;

        drawer.reinitDraw();

        fm.closeRead();

    }
}
