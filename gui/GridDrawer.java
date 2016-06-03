package gui;

import algorithms.*;
import instances.Grid;
import instances.GridT;
import instances.IndexOutOfBoundGridException;
import instances.UnmergeableGridException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Couple;

import java.io.File;

/**
 * Created by mouton on 23/02/16.
 */
public class GridDrawer extends Application{

    public static int SIZEX = 10;
    public static int SIZEY = 10;
    public static int LENGTH = 30;

    public static final int MARGIN = 30;


    private static final int ANIMATION_DURATION = 100;


    private Stage stage;

    public GridT<Color> grid;
    public GridT<Color> currentGrid;

    public Shape[][] circles;
    public Rectangle downRect;
    public Rectangle rightRect;

    public Color currentColor;

    public CheckMenuItem modeButton;
    public MenuItem exactButton;
    public MenuItem lclButton;
    public MenuItem greedButton;
    public MenuItem neighborizationGreedyButton;

    public MenuItem newButton;
    public MenuItem saveButton;
    public MenuItem loadButton;
    public MenuItem exportButton;

    public MenuItem flipHButton;
    public MenuItem flipVButton;
    public MenuItem simplifyButton;
    public MenuItem eraseGridButton;
    public MenuItem colorButton;


    public Pane root;

    public GridMousePressedHandler gmph;
    public GridMouseMovedHandler gmmh;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        grid = new GridT<Color>(SIZEX, SIZEY);
        currentGrid = grid;
        currentColor = Color.BLACK;

        reinitDraw();

        stage.show();


    }

    public void reinitDraw(){

        SIZEX = grid.getSizex();
        SIZEY = grid.getSizey();

        circles = new Shape[SIZEX][SIZEY];
        downRect = new Rectangle(MARGIN, MAXDOWN(), MAXRIGHT() - MARGIN, 0);
        rightRect = new Rectangle(MAXRIGHT(), MARGIN, 0, MAXDOWN() - MARGIN);

        VBox box = new VBox();

        root = new Pane();

        for(int i = 0; i<=SIZEX ; i++){
            Line line = new Line(MARGIN,MARGIN+i*LENGTH,MARGIN+SIZEY*LENGTH,MARGIN+i*LENGTH);
            root.getChildren().add(line);
        }

        for(int i = 5; i<=SIZEX;i+=5){
            Text text = new Text(MARGIN - LENGTH/10, MARGIN - LENGTH/2 +  i * LENGTH, String.valueOf(i));
            text.setX( text.getX() -  text.getLayoutBounds().getWidth());
            text.setY( text.getY() + text.getLayoutBounds().getHeight()/2);
            root.getChildren().add(text);

            text = new Text(MAXRIGHT() + LENGTH/10, MARGIN - LENGTH/2 +  i * LENGTH, String.valueOf(i));
            text.setY( text.getY() + text.getLayoutBounds().getHeight()/2);
            root.getChildren().add(text);
        }

        for(int i = 0; i<=SIZEY ; i++){
            Line line = new Line(MARGIN+i*LENGTH,MARGIN,MARGIN+i*LENGTH,MARGIN+SIZEX*LENGTH);
            root.getChildren().add(line);
        }

        for(int i = 5; i<=SIZEY;i+=5){
            Text text = new Text(MARGIN - LENGTH/2 +  i * LENGTH, MARGIN - LENGTH/10, String.valueOf(i));
            text.setX( text.getX() -  text.getLayoutBounds().getWidth()/2);
            root.getChildren().add(text);


            text = new Text(MARGIN - LENGTH/2 +  i * LENGTH, MAXDOWN() + LENGTH/10, String.valueOf(i));
            text.setX( text.getX() -  text.getLayoutBounds().getWidth()/2);
            text.setY( text.getY() + 2*text.getLayoutBounds().getHeight()/3);
            root.getChildren().add(text);
        }

        root.getChildren().addAll(downRect, rightRect);

        gmph = new GridMousePressedHandler(this);
        gmmh = new GridMouseMovedHandler(this, root);

        MenuBar menuBar = new MenuBar();
        menuBar.setMinHeight(MARGIN);
        menuBar.setMaxHeight(MARGIN);


        Menu fileMenu = new Menu("File");
        Menu gridMenu = new Menu("Grid");
        Menu toolMenu = new Menu("Tools");

        menuBar.getMenus().addAll(fileMenu, gridMenu, toolMenu);



        modeButton = new CheckMenuItem();
        modeButton.setText("Edit mode");
        ModeButtonHandler btnHandler = new ModeButtonHandler(this);
        modeButton.setOnAction(btnHandler);


        exactButton = new MenuItem();
        exactButton.setText("Exact");
        AlgorithmButtonHandler exactHandler = new AlgorithmButtonHandler(new EnumerationAlgorithm(), this);
        exactButton.setOnAction(exactHandler);

        lclButton = new MenuItem();
        lclButton.setText("LCL");
        AlgorithmButtonHandler lclHandler = new AlgorithmButtonHandler(new LCLApproximationAlgorithm(), this);
        lclButton.setOnAction(lclHandler);

        greedButton = new MenuItem();
        greedButton.setText("Greedy");
        AlgorithmButtonHandler greedHandler = new AlgorithmButtonHandler(new GreedyAlgorithm(), this);
        greedButton.setOnAction(greedHandler);

        neighborizationGreedyButton = new MenuItem();
        neighborizationGreedyButton.setText("Neighborization");
        AlgorithmButtonHandler neighborizationHandler = new AlgorithmButtonHandler(new NeighborizationAlgorithm(), this);
        neighborizationGreedyButton.setOnAction(neighborizationHandler);

        toolMenu.getItems().addAll(modeButton, new SeparatorMenuItem(), exactButton, lclButton, greedButton, neighborizationGreedyButton);




        NewSaveLoadButtonHandler slh = new NewSaveLoadButtonHandler(this);

        newButton = new MenuItem();
        newButton.setText("New");
        newButton.setOnAction(slh);

        saveButton = new MenuItem();
        saveButton.setText("Save");
        saveButton.setOnAction(slh);

        loadButton = new MenuItem();
        loadButton.setText("Load");
        loadButton.setOnAction(slh);

        ExportButtonHandler eh = new ExportButtonHandler(this);

        exportButton = new MenuItem();
        exportButton.setText("Export");
        exportButton.setOnAction(eh);

        fileMenu.getItems().addAll(newButton, new SeparatorMenuItem(),
                saveButton, loadButton, new SeparatorMenuItem(),
                exportButton);

        FlipButtonHandler fliph = new FlipButtonHandler(this);

        flipHButton = new MenuItem();
        flipHButton.setText("Flip grid horizontally");
        flipHButton.setOnAction(fliph);

        flipVButton = new MenuItem();
        flipVButton.setText("Flip grid vertically");
        flipVButton.setOnAction(fliph);

        SimplifyButtonHandler simplh = new SimplifyButtonHandler(this);

        simplifyButton = new MenuItem();
        simplifyButton.setText("Simplify grid");
        simplifyButton.setOnAction(simplh);

        EraseGridButtonHandler eraseh = new EraseGridButtonHandler(this);

        eraseGridButton = new MenuItem();
        eraseGridButton.setText("Erase initial grid");
        eraseGridButton.setOnAction(eraseh);

        ColorButtonHandler colorh = new ColorButtonHandler(this);
        colorButton = new MenuItem();
        colorButton.setText("Choose color");
        colorButton.setOnAction(colorh);

        gridMenu.getItems().addAll(flipHButton, flipVButton, simplifyButton, eraseGridButton, new SeparatorMenuItem(),
                colorButton);


        box.getChildren().addAll(menuBar, root);
        box.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(box, 2*MARGIN + SIZEY * LENGTH, 5*MARGIN + SIZEX * LENGTH);
        root.setOnMousePressed(gmph);
        root.setOnMouseMoved(gmmh);


        stage.setTitle("Grid Drawer");
        stage.setScene(scene);
        stage.setWidth(2*MARGIN + SIZEY * LENGTH);
        stage.setHeight(5*MARGIN + SIZEX * LENGTH);

        reinit();
    }

    public void mergeLine(int line){
        try {
            Timeline timeline = this.mergeLineTransition(line);
            currentGrid = currentGrid.mergeLine(line - 1);
            if(timeline != null)
                timeline.play();

        }
        catch(IndexOutOfBoundGridException | UnmergeableGridException e){
        }

    }

    public Timeline mergeLineTransition(int line) {
        try {
            if (!currentGrid.canMergeLine(line - 1))
                return null;
        }
        catch(IndexOutOfBoundGridException e){
            return null;
        }
        ParallelTransition transitions = new ParallelTransition();
        Timeline timeline = new Timeline();
        for (int i = line; i < circles.length; i++) {
            for (int j = 0; j < circles[0].length; j++) {
                Shape circle = circles[i][j];
                if (circle != null) {
                    circles[i-1][j] = circle;
                    circles[i][j] = null;
                    double ty = circle.getTranslateY();
                    timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(circle.translateYProperty(), ty)
                            ),
                            new KeyFrame(new Duration(ANIMATION_DURATION),
                                    new KeyValue(circle.translateYProperty(), ty - GridDrawer.LENGTH)
                            )
                    );
                }
            }
        }
        double height = downRect.getHeight();
        double ty = downRect.getTranslateY();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(downRect.heightProperty(),height),
                        new KeyValue(downRect.translateYProperty(),ty)
                ),
                new KeyFrame(new Duration(ANIMATION_DURATION),
                        new KeyValue(downRect.heightProperty(),height+GridDrawer.LENGTH),
                        new KeyValue(downRect.translateYProperty(),ty-GridDrawer.LENGTH)
                )
        );

        return timeline;

    }

    public void mergeColumn(int column){
        try{
            Timeline timeline = this.mergeColumnTransition(column);
            currentGrid = currentGrid.mergeColumn(column - 1);
            if(timeline != null)
                timeline.play();
        }
        catch(IndexOutOfBoundGridException | UnmergeableGridException e){
        }
    }

    public Timeline mergeColumnTransition(int column){
        try {
            if (!currentGrid.canMergeColumn(column-1))
                return null;
        }
        catch(IndexOutOfBoundGridException e){
            return null;
        }
        Timeline timeline = new Timeline();
        for (int j = column; j < circles[0].length; j++) {
            for (int i = 0; i < circles.length; i++) {
                Shape circle = circles[i][j];
                if (circle != null) {
                    circles[i][j-1] = circle;
                    circles[i][j] = null;
                    double tx = circle.getTranslateX();
                    timeline.getKeyFrames().addAll(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(circle.translateXProperty(), tx)
                            ),
                            new KeyFrame(new Duration(ANIMATION_DURATION),
                                    new KeyValue(circle.translateXProperty(), tx - GridDrawer.LENGTH)
                            )
                    );
                }
            }
        }
        double width = rightRect.getWidth();
        double tx = rightRect.getTranslateX();

        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(rightRect.widthProperty(),width),
                        new KeyValue(rightRect.translateXProperty(),tx)
                ),
                new KeyFrame(new Duration(ANIMATION_DURATION),
                        new KeyValue(rightRect.widthProperty(),width+GridDrawer.LENGTH),
                        new KeyValue(rightRect.translateXProperty(),tx-GridDrawer.LENGTH)
                )
        );
        return timeline;

    }

    public void addPoint(int line, int column, Color color){
        Shape circle = GridDrawer.getCircleShape(line, column, color);
        circle.setOpacity(1.0);
        this.circles[line][column] = circle;
        root.getChildren().add(circle);
    }

    public void removePoint(int line, int column){
        Shape circle = this.circles[line][column];
        this.circles[line][column] = null;
        if(circle != null)
            root.getChildren().remove(circle);
    }

    public void reinit(){
        modeButton.setSelected(false);
        Color color;
        for(int line = 0; line < GridDrawer.SIZEX; line++){
            for(int column = 0; column < GridDrawer.SIZEY; column++){
                Shape circle = circles[line][column];
                circles[line][column] = null;
                if(circle != null)
                    root.getChildren().remove(circle);
                if((color = grid.getPoint(line, column)) != null) {
                    this.addPoint(line, column, color);
                }
            }
        }
        downRect.setTranslateY(0);
        downRect.setHeight(0);
        rightRect.setTranslateX(0);
        rightRect.setWidth(0);
        currentGrid = grid;
    }

    private static Integer MAXRIGHT(){
        return MARGIN + SIZEY * LENGTH;
    }

    private static Integer MAXDOWN(){
        return MARGIN + SIZEX * LENGTH;
    }


    public static Integer getColumn(int x){
        boolean outX = x <= MARGIN || x >= MAXRIGHT();
        if(outX)
            return null;
        return  (x - MARGIN) / LENGTH;
    }

    public static Integer getLine(int y){
        boolean outY = y <= MARGIN || y >= MAXDOWN();
        if(outY)
            return null;
        return  (y - MARGIN) / LENGTH;
    }

    public static Integer getAbscissa(int column){
        System.out.println(column * LENGTH + MARGIN);
        return column * LENGTH + MARGIN;
    }

    public static Integer getOrdinate(int line){
        return line * LENGTH + MARGIN;
    }

    public static Couple<Integer,Integer> getLineColumn(int x, int y){
        boolean outX = x <= MARGIN || x >= MAXRIGHT();
        boolean outY = y <= MARGIN || y >= MAXDOWN();

        if(outX && outY)
            return new Couple<Integer,Integer>(null,null);

        Integer column = null;
        Integer line = null;

        if(outY){
            if(x > MARGIN + LENGTH/2 && x < MAXRIGHT() - LENGTH/2)
                column = (int) ((x + LENGTH/2 - MARGIN) / LENGTH);
        }
        else if(outX){
            if(y > MARGIN + LENGTH/2 && y < MAXDOWN() - LENGTH/2)
                line = (int) ((y + LENGTH/2 - MARGIN) / LENGTH);
        }
        else {
            column = (int) ((x - MARGIN) / LENGTH);
            line = (int) ((y - MARGIN) / LENGTH);
        }
        return new Couple<Integer,Integer>(line, column);
    }

    public static Shape getLineRectShape(int line, boolean red){
        Rectangle rect = new Rectangle(MARGIN, MARGIN + line * LENGTH - LENGTH / 8, MAXRIGHT()-MARGIN, LENGTH/4);
        if(red)
            rect.setFill(Color.RED);
        return rect;
    }

    public static Shape getColumnRectShape(int column, boolean red){
        Rectangle rect = new Rectangle(MARGIN + column * LENGTH - LENGTH / 8, MARGIN, LENGTH/4 ,MAXDOWN()-MARGIN);
        if(red)
            rect.setFill(Color.RED);
        return rect;
    }

    public static Shape getCircleShape(int line, int column, Color color) {
        Circle circle = new Circle(MARGIN + column * LENGTH + LENGTH / 2, MARGIN + line * LENGTH + LENGTH / 2, LENGTH / 4);
        circle.setFill(color);
        return circle;
    }

    public static void main(String[] args){launch();}


    public static Shape getBorderLeftLineRectShape(int line) {
        Rectangle rect = new Rectangle(MARGIN-LENGTH/10, MARGIN + line * LENGTH, LENGTH/5, LENGTH);
        return rect;
    }

    public static Shape getBorderRightLineRectShape(int line) {
        Rectangle rect = new Rectangle(MAXRIGHT()-LENGTH/10, MARGIN + line * LENGTH, LENGTH/5, LENGTH);
        return rect;
    }

    public static Shape getFullLineRectShape(int line) {
        Rectangle rect = new Rectangle(MARGIN, MARGIN + line * LENGTH, MAXRIGHT()-MARGIN, LENGTH);
        return rect;
    }

    public static Shape getBorderTopColumnRectShape(int column) {
        Rectangle rect = new Rectangle(MARGIN + column * LENGTH, MARGIN-LENGTH/10, LENGTH, LENGTH/5);
        return rect;
    }

    public static Shape getBorderBottomColumnRectShape(int column) {
        Rectangle rect = new Rectangle(MARGIN + column * LENGTH, MAXDOWN()-LENGTH/10, LENGTH, LENGTH/5);
        return rect;
    }

    public static Shape getFullColumnRectShape(int column) {
        Rectangle rect = new Rectangle(MARGIN + column * LENGTH, MARGIN, LENGTH, MAXDOWN()-MARGIN);
        return rect;
    }
}
