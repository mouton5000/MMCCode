package gui;

import instances.Grid;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by mouton on 28/04/16.
 */
public class ExportButtonHandler implements EventHandler<ActionEvent> {

    GridDrawer drawer;
    FileChooser fileChooser;

    public ExportButtonHandler(GridDrawer drawer) {
        this.drawer = drawer;
        this.fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tex files", "*.tex"));
    }

    @Override
    public void handle(ActionEvent actionEvent) {


            Dialog<List<Object>> dialog = new Dialog<>();
            dialog.setTitle("Export grid?");

            // Set the button types.
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);


            TextField exportFile = new TextField("");
            exportFile.setMinWidth(400);
            exportFile.setMaxWidth(400);

            Button browse = new Button();
            browse.setText("Browse");
            browse.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    fileChooser.setTitle("Choose destination.");
                    File selectedFile = fileChooser.showOpenDialog(null);
                    if (selectedFile != null) {
                        if(!selectedFile.toPath().toString().matches(".*\\.tex"))
                            selectedFile = new File(selectedFile.getPath()+".tex");
                        exportFile.setText(selectedFile.getAbsolutePath());
                    }
                }
            });


            ToggleGroup group = new ToggleGroup();
            RadioButton rbOne = new RadioButton("Ones");
            RadioButton rbDot = new RadioButton("Dots");

            rbOne.setSelected(true);
            rbOne.setToggleGroup(group);
            rbDot.setToggleGroup(group);

            grid.add(exportFile, 1, 0);
            grid.add(browse, 0, 0);
            grid.add(rbOne, 0, 1);
            grid.add(rbDot, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(exportFile.getText());
                    list.add(rbOne.isSelected());
                    return list;
                }
                return null;
            });

            Optional<List<Object>> result = dialog.showAndWait();

            result.ifPresent(exportParameters -> {
                String exportFileName = (String)exportParameters.get(0);
                Boolean putOnes = (Boolean)exportParameters.get(1);
                export(exportFileName, putOnes, drawer.grid);
            });

    }

    private void export(String exportFileName, Boolean putOnes, Grid grid) {
        FileManager fm = new FileManager();
        fm.openErase(exportFileName);


        fm.writeln("\\begin{figure}");
        fm.writeln("    \\centering");
        fm.writeln();
        fm.writeln("    \\begin{tikzpicture}");
        fm.writeln("        \\coordinate (O) at (0,0);");
        fm.writeln("        \\prgrid{O}{"+grid.getSizex()+"}{"+grid.getSizey()+"}");

        fm.writeln();


        for(int line = 0; line < grid.getSizex(); line++){
            boolean atLeast1 = false;
            for(int column = 0; column < grid.getSizey(); column++){
                if(!grid.hasPoint(line, column))
                    continue;
                atLeast1 = true;
                if(putOnes)
                    fm.writeln("        \\prone{O}{"+(line+1)+"}{"+(column+1)+"};");
                else
                    fm.writeln("        \\prbul{O}{"+(line+1)+"}{"+(column+1)+"};");
            }
            if(atLeast1)
               fm.writeln();
        }
        fm.writeln();
        fm.writeln("    \\end{tikzpicture}");
        fm.writeln("    \\caption{}");
        fm.writeln("    \\label{fig:}");
        fm.writeln("\\end{figure}");

        fm.closeWrite();
    }
}
