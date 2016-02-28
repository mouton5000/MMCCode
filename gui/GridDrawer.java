package gui;

import algorithms.*;
import instances.Grid;
import instances.IndexOutOfBoundGridException;
import instances.UnmergeableGridException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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

/**
 * Created by mouton on 23/02/16.
 */
public class GridDrawer extends Application{

    public static final int SIZEX = 10;
    public static final int SIZEY = 10;
    public static final int LENGTH = 30;

    public static final int MARGIN = 50;


    private static final int ANIMATION_DURATION = 100;


    public Grid grid;
    public Grid currentGrid;

    public Shape[][] circles;
    public Rectangle downRect;
    public Rectangle rightRect;

    public Button mainButton;
    public Button lclButton;
    public Button greedButton;
    public Button neighborizationGreedyButton;

    public Pane root;

    public GridMousePressedHandler gmph;
    public GridMouseMovedHandler gmmh;

    @Override
    public void start(Stage stage) throws Exception {

        grid = new Grid(SIZEX, SIZEY);
        currentGrid = grid;
        circles = new Shape[SIZEX][SIZEY];
        downRect = new Rectangle(MARGIN, MAXDOWN(), MAXRIGHT() - MARGIN, 0);
        rightRect = new Rectangle(MAXRIGHT(), MARGIN, 0, MAXDOWN() - MARGIN);

        VBox box = new VBox(MARGIN);

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

        gmph = new GridMousePressedHandler(this,root);
        gmmh = new GridMouseMovedHandler(this, root);


        box.getChildren().add(root);

        HBox hbox = new HBox(15);

        mainButton = new Button();
        mainButton.setText("Start");
        MainButtonHandler btnHandler = new MainButtonHandler(this);
        mainButton.setOnAction(btnHandler);

        lclButton = new Button();
        lclButton.setText("LCL");
        AlgorithmButtonHandler lclHandler = new AlgorithmButtonHandler(new LCLApproximationAlgorithm(grid), this);
        lclButton.setOnAction(lclHandler);

        greedButton = new Button();
        greedButton.setText("Greedy");
        AlgorithmButtonHandler greedHandler = new AlgorithmButtonHandler(new GreedyAlgorithm(grid), this);
        greedButton.setOnAction(greedHandler);

        neighborizationGreedyButton = new Button();
        neighborizationGreedyButton.setText("Neighborization");
        AlgorithmButtonHandler neighborizationHandler = new AlgorithmButtonHandler(new NeighborizationAlgorithm2(grid), this);
        neighborizationGreedyButton.setOnAction(neighborizationHandler);

        hbox.getChildren().addAll(mainButton, lclButton, greedButton, neighborizationGreedyButton);
        hbox.setAlignment(Pos.BASELINE_CENTER);

        box.getChildren().add(hbox);
        box.setAlignment(Pos.TOP_CENTER);


        Scene scene = new Scene(box, 2*MARGIN + SIZEY * LENGTH, 3*MARGIN + SIZEX * LENGTH);
        scene.setOnMousePressed(gmph);
        scene.setOnMouseMoved(gmmh);

        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();


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

    public void reinit(){
        mainButton.setText("Start");
        for(int line = 0; line < GridDrawer.SIZEX; line++){
            for(int column = 0; column < GridDrawer.SIZEY; column++){
                Shape circle = circles[line][column];
                circles[line][column] = null;
                if(circle != null)
                    root.getChildren().remove(circle);
                if(grid.hasPoint(line,column)) {
                    circle = GridDrawer.getCircleShape(line, column);
                    circles[line][column] = circle;
                    root.getChildren().add(circle);
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

    public static Shape getCircleShape(int line, int column) {
        Circle circle = new Circle(MARGIN + column * LENGTH + LENGTH / 2, MARGIN + line * LENGTH + LENGTH / 2, LENGTH / 4);
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
