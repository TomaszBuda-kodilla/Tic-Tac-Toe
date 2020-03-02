package com.kodilla.project.tictaktoe;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.scene.paint.Color.*;

public class TIC extends Application {

    private boolean playable = true;
    private boolean turnX = true;
    private Tile[][] board = new Tile[3][3];
    private List<Combo> combos = new ArrayList<>();
    private boolean turn;
    private Pane root = new Pane();
    Players playerOne = new Players("Tomek",0);
    Players playerTwo = new Players("Terminator",0);

    private Parent createContent() {
        root.setPrefSize(600, 620);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ToolBar toolBar = new ToolBar();
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(20 + (i * 200));
                Button newButton = new Button("New");
                newButton.setOnAction(actionEvent -> {

                });
                Button restartButton = new Button("Restart");
                Button exitButton = new Button("Exit");
                exitButton.setOnAction(actionEvent -> {
                    try {
                        Platform.exit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                toolBar.getItems().add(newButton);
                toolBar.getItems().add(restartButton);
                toolBar.getItems().add(exitButton);
                toolBar.setPrefSize(600,20);
                root.getChildren().addAll(toolBar,tile);
                board[j][i] = tile;
            }
        }

        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return root;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private void checkState() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                if (turnX){
                    playerOne.setScore();
                }
                else{
                    playerTwo.setScore();
                }
                break;
            }
        }
    }

    private void playWinAnimation(Combo combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[0].getCenterX());
        line.setEndY(combo.tiles[0].getCenterY());
        line.setStrokeWidth(6);
        line.setFill(YELLOW);
        line.setStroke(YELLOW);
        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play();
    }

    private class Combo {
        private Tile[] tiles;

        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;
            else {
                return tiles[0].getValue().equals(tiles[1].getValue())
                        && tiles[0].getValue().equals(tiles[2].getValue());
            }
        }
    }

        private class Tile extends StackPane {
            private Text text = new Text();

            public Tile() {
                Rectangle border = new Rectangle(200, 200);
                border.setFill(BLACK);
                border.setStroke(WHITE);

                text.setFont(Font.font(150));

                setAlignment(Pos.CENTER);
                getChildren().addAll(border, text);

                setOnMouseClicked(event -> {
                    if (!playable)
                        return;

                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (!turnX)
                            return;

                        drawX();
                        turnX = false;
                        checkState();
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        Random random = new Random();
                        if (turnX) return;

                            board[random.nextInt(3)][random.nextInt(3)].drawO();
                            turnX = true;
                            checkState();
                    }
                });
            }


                public double getCenterX () {
                    return getTranslateX() + 100;
                }

                public double getCenterY () {
                    return getTranslateY() + 100;
                }

                public String getValue () {
                    return text.getText();
                }

                private void drawX () {
                    text.setFill(RED);
                    text.setText("X");
                }

                private void drawO () {
                    text.setFill(GREEN);
                    text.setText("O");
                }
            }



        public static void main(String[] args) {
            launch(args);
        }
    }