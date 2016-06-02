package gui;

import instances.Grid;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
        if(actionEvent.getSource().equals(drawer.newButton)) {
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
                drawer.grid = new Grid(linesColumns.get(0), linesColumns.get(1));
                GridDrawer.LENGTH = linesColumns.get(2);
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
        Grid g = drawer.grid;
        Integer length = GridDrawer.LENGTH;

        FileManager fm = new FileManager();
        fm.openErase(f.getPath());

        fm.writeln(g.getSizex()+" "+g.getSizey()+" "+length);
        fm.writeln();
        for(int line = 0; line < g.getSizex(); line++) {
            for (int column = 0; column < g.getSizey(); column++){
                int toWrite = g.hasPoint(line, column)?1:0;
                fm.write(toWrite+" ");
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

        Grid g = new Grid(sizeX, sizeY);
        for(int line = 0; line < g.getSizex(); line++) {
            fileLine = fm.readLine();
            String[] points = fileLine.split("\\s+");
            for (int column = 0; column < g.getSizey(); column++){
                if(points[column].equals("1"))
                    g.addPoint(line, column);
            }
        }

        drawer.grid = g;
        GridDrawer.LENGTH = length;

        drawer.reinitDraw();

        fm.closeRead();

    }
}
