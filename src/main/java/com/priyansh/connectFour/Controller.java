package com.priyansh.connectFour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private final static int Columns = 7;
    private final static int Rows = 6;
    private final static int CircleDiameter = 80;
    private final static String DiscColor1 = "#24303E";
    private final static String DiscColor2 = "#4CAA88";
    private boolean isAllowedToInsert =true;
    private static String Player1 = "Player One";
    private static String Player2 = "Player Two";

    private boolean isPlayerOneTurn = true;
    private Disc[][] insertedDiscArr = new Disc[Rows][Columns]; //for structural changes for dev
    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertedDiskPane;
    @FXML
    public Label playerNameLabel;
    @FXML
    public Button setNamesButton;
    @FXML
    public TextField playerOneName;
    @FXML
    public TextField playerTwoName;

    public void Playground() {
        Shape rectangleWithHoles =GridStructure();
        rootGridPane.add(rectangleWithHoles,0,1);
        List<Rectangle> rectangleList = clickableColumn();
        for (Rectangle rectangle: rectangleList) {
            rootGridPane.add(rectangle, 0, 1);
        }
    }
    private Shape GridStructure(){
        Shape rectangleWithHoles = new Rectangle((Columns+1) * CircleDiameter, (Rows+1) * CircleDiameter);
        for(int row=0;row<Rows;row++) {
            for (int col = 0; col < Columns; col++) {


                Circle circle = new Circle();
                circle.setRadius((float)CircleDiameter / 2);
                circle.setCenterX((float)CircleDiameter / 2);
                circle.setCenterY((float)CircleDiameter / 2);
                circle.setSmooth(true);

                circle.setTranslateX(col*((float)CircleDiameter+5) + (float)CircleDiameter/4);
                circle.setTranslateY(row*((float)CircleDiameter+5) + (float)CircleDiameter/4);
                rectangleWithHoles = Shape.subtract(rectangleWithHoles,circle);
            }
        }

        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }
    private List<Rectangle> clickableColumn(){
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int col = 0; col < Columns; col++) {
            Rectangle rectangle = new Rectangle(CircleDiameter, (Rows + 1) * CircleDiameter);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(CircleDiameter+5)+CircleDiameter / 4);

            rectangle.setOnMouseEntered(actionEvent -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(actionEvent -> rectangle.setFill(Color.TRANSPARENT));
            final int column = col;
            rectangle.setOnMouseClicked(actionEvent ->{
                if(isAllowedToInsert) {
                    isAllowedToInsert =false;
                    insertDisc(new Disc(isPlayerOneTurn), column);
                }
            });
            rectangleList.add(rectangle);
        }
        return rectangleList;
    }
    private  void insertDisc(Disc disc,int column){

        int row = Rows -1;
        while (row>=0){
            if(getDiscPresent(row,column)==null){
                break;}

            row--;
        }
        if(row<0)
            return;
        insertedDiscArr[row][column] = disc;//Structural changes for dev
        insertedDiskPane.getChildren().add(disc);
        disc.setTranslateX(column*(CircleDiameter+5)+(float)CircleDiameter / 4);

        int currentRow= row;
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row*(CircleDiameter+5)+(float)CircleDiameter/4);
        translateTransition.setOnFinished(actionEvent ->{
            isAllowedToInsert =true;
            if(gameEnded((currentRow),column)){
                gameOver();
            }
            isPlayerOneTurn= !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn?Player1:Player2);
        });
        translateTransition.play();
    }


    private boolean gameEnded(int row,int column) {

        // Vertical Points.
        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3) //range of row is then 1 to 5
                .mapToObj(r -> new Point2D(r, column))//0,3 1,3 2,3 4,3 5,3->Point2D x,y
                .collect(Collectors.toList());
        //Horizontal Points
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3) //range of row is then 1 to 5
                .mapToObj(c -> new Point2D(row, c))//0,3 1,3 2,3 4,3 5,3->Point2D x,y
                .collect(Collectors.toList());
        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Point = IntStream
                .rangeClosed(0, 6).mapToObj(i -> startPoint1
                        .add(i, -i)).collect(Collectors.toList());
        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Point = IntStream
                .rangeClosed(0, 6).mapToObj(i -> startPoint2
                        .add(i, i)).collect(Collectors.toList());
        return checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
                || checkCombinations(diagonal1Point) || checkCombinations(diagonal2Point);
    }


    private boolean checkCombinations(List<Point2D> points) {
        int chain = 0;

        for (Point2D point : points) {

            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscPresent(rowIndexForArray,columnIndexForArray);

            if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            }else {
                chain = 0;
            }
        }
        return false;
    }
    private Disc getDiscPresent(int row,int column){
        if(row>=Rows|| row<0 || column>=Columns || column<0){
            return null;}
        return insertedDiscArr[row][column];
    }

    private void gameOver() {
        String winner = isPlayerOneTurn ? Player1: Player2;
        System.out.println("Winner is:"+ winner);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ConnectFour");
        alert.setHeaderText("The Winner is "+ winner);
        alert.setContentText("Want to Play again?");

        ButtonType yesbtn = new ButtonType("Yes");
        ButtonType nobtn = new ButtonType("No");
        alert.getButtonTypes().setAll(yesbtn,nobtn);

        Platform.runLater(()->{
            Optional<ButtonType> btnClicked=alert.showAndWait();
            if(btnClicked.isPresent() && btnClicked.get() ==yesbtn){
                resetGame();
            }else {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void resetGame() {
        insertedDiskPane.getChildren().clear();
        for (int row = 0; row < insertedDiscArr.length; row++) {
            for (int col = 0; col < insertedDiscArr[row].length; col++) {
                insertedDiscArr[row][col] = null;
            }
        }
        isPlayerOneTurn = true;
        playerNameLabel.setText(Player1);
        Playground();
    }

    private static  class Disc extends Circle{
        private final boolean isPlayerOneMove;
        public Disc(boolean isPlayerOneMove){
            this.isPlayerOneMove =isPlayerOneMove;
            setRadius((float)CircleDiameter / 2);
            setFill(isPlayerOneMove ?Color.valueOf(DiscColor1):Color.valueOf(DiscColor2));
            setCenterX((float)CircleDiameter / 2);
            setCenterY((float)CircleDiameter / 2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setNamesButton.setOnAction(actionEvent -> {
            String input1 = playerOneName.getText();
            String input2 = playerTwoName.getText();

            Player1 = input1 + "`s";
            Player2= input2 + "`s";

            if (input1.isEmpty())
                Player1 = "Player One`s";

            if (input2.isEmpty())
                Player2 = "Player Two`s";

            //isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn? Player1 : Player2);
        });
    }
}