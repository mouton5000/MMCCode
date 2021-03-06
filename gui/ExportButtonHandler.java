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
import javafx.scene.paint.Paint;
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

            Label sizeLb = new Label("Size of box (cm) : ");

            TextField sizeTf = new TextField("0.5");
            sizeTf.setMinWidth(50);
            sizeTf.setMaxWidth(50);
            sizeTf.textProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*(\\.\\d*)?")) {
                        sizeTf.setText(oldValue);
                    }
                }
            });

            grid.add(exportFile, 1, 0);
            grid.add(browse, 0, 0);
            grid.add(rbOne, 0, 1);
            grid.add(rbDot, 1, 1);
            grid.add(sizeLb, 0, 2);
            grid.add(sizeTf, 1, 2);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(exportFile.getText());
                    list.add(rbOne.isSelected());
                    String size = sizeTf.getText();
                    if(size.length() == 0)
                        list.add(0.5);
                    else
                        list.add(Double.valueOf(sizeTf.getText()));
                    return list;
                }
                return null;
            });

            Optional<List<Object>> result = dialog.showAndWait();

            result.ifPresent(exportParameters -> {
                String exportFileName = (String)exportParameters.get(0);
                Boolean putOnes = (Boolean)exportParameters.get(1);
                Double length = (Double)exportParameters.get(2);
                export(exportFileName, putOnes, length, drawer.grid);
            });

    }

    private void export(String exportFileName, Boolean putOnes, double length, GridT<Color> grid) {
        FileManager fm = new FileManager();
        fm.openErase(exportFileName);


        fm.writeln("\\renewcommand{\\gridsize}{"+length+"}");

        fm.writeln("\\begin{figure}");
        fm.writeln("    \\centering");
        fm.writeln();
        fm.writeln("    \\begin{tikzpicture}");
        fm.writeln("        \\coordinate (O) at (0,0);");
        fm.writeln("        \\prgrid{O}{"+grid.getSizex()+"}{"+grid.getSizey()+"}");

        fm.writeln();


        Color color;
        for(int line = 0; line < grid.getSizex(); line++){
            boolean atLeast1 = false;
            for(int column = 0; column < grid.getSizey(); column++){
                if((color = grid.getPoint(line, column)) == null)
                    continue;
                atLeast1 = true;
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();
                String colorString = "{"+red+","+green+","+blue+"}";
                fm.writeln("\\definecolor{tempcolor}{rgb}" + colorString);
                String paramsString = "{O}{"+(line+1)+"}{"+(column+1)+"}{tempcolor};";
                String commandString = putOnes?"\\pronec":"\\prbulc";
                fm.writeln("        "+commandString+paramsString);
            }
            if(atLeast1)
               fm.writeln();
        }

        for(int line = 5; line-1 < grid.getSizex(); line += 5)
            fm.writeln("\\draw ($(O)+(0,"+(length/2 + (line-1) * length)+")$) node[anchor=east] {$"+line+"$};");
        for(int column= 5; column-1 < grid.getSizey(); column += 5)
            fm.writeln("\\draw ($(O)+("+(length/2 + (column-1) * length)+",-0.3)$) node {$"+column+"$};");

        fm.writeln();
        fm.writeln("    \\end{tikzpicture}");
        fm.writeln("    \\caption{}");
        fm.writeln("    \\label{fig:}");
        fm.writeln("\\end{figure}");

        fm.closeWrite();
    }
}
