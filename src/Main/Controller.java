package Main;

import Main.Algorithms.Algorithm;
import Main.Algorithms.BreadthFirst;
import Main.Algorithms.DepthFirst;

import Main.Algorithms.Dijkstra;

import Main.Animation.BounceIn;

import Main.GraphRelated.Cell;
import Main.GraphRelated.CellState;
import Main.MazeGenerator.MazeGenerator;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import Main.Configurations.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ComboBox<String> algoOptions;
    @FXML
    private AnchorPane gridContainer;
    @FXML
    private GridPane platform;
    @FXML
    private JFXButton sourceButton, wallButton, unvisitedButton, targetButton, weightButton, startButton, stopButton, clearButton, clearPathButton, pauseButton, mazeButton;

    public static BorderPane[][] BorderGrid = new BorderPane[Constants.ROW][Constants.COL];
    public static Cell[][] CellGrid = new Cell[Constants.ROW][Constants.COL];

    private int[][] currentST = new int[2][2]; // location of source and target, 0 for source, 1 for target

    private int selectedAlgo;
    private CellState currentState;
    private boolean applyColor;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algoOptions.getItems().addAll("Breadth First Search", "Depth First Search", "Dijkstra Algorithm");
        algoOptions.setOnAction(e -> {
            selectedAlgo = algoOptions.getSelectionModel().getSelectedIndex();
            switch (selectedAlgo) {
                case 0: case 1:
                    clearWeight();
                    weightButton.setDisable(true);
                    break;
                case 2:
                    weightButton.setDisable(false);
                    break;
            }
            startButton.setDisable(false);
        });

        gridInit();

        for (int x = 0; x < Constants.ROW; x++) {
            for (int y = 0; y < Constants.COL; y++) {
                CellGrid[x][y] = new Cell(x, y);
                constructCell(x, y);
            }
        }

        clearButton.setAlignment(Pos.CENTER_LEFT);
        clearPathButton.setAlignment(Pos.CENTER_LEFT);
        startButton.setAlignment(Pos.CENTER_LEFT);
        stopButton.setAlignment(Pos.CENTER_LEFT);
        pauseButton.setAlignment(Pos.CENTER_LEFT);
        wallButton.setAlignment(Pos.CENTER_LEFT);
        weightButton.setAlignment(Pos.CENTER_LEFT);
        mazeButton.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < 2; i++) currentST[i][0] = -1;

        selectedAlgo = -1; //initially no algorithm is selected.
        currentState = null;
        applyColor = false;

        ChangeListener<Number> changeListener = (observable, oldVal, newVal) -> gridUpdate();

        gridContainer.heightProperty().addListener(changeListener);
        gridContainer.widthProperty().addListener(changeListener);
    }

    public void gridUpdate() {
        double containerWidth = gridContainer.getWidth();
        double containerHeight = gridContainer.getHeight();

        double gridCellWidth = containerWidth / Constants.COL;
        double gridCellHeight = containerHeight / Constants.ROW;

        double gridCellSize = Math.min(gridCellWidth, gridCellHeight);

        double gridWidth = gridCellSize * Constants.COL;
        double gridHeight = gridCellSize * Constants.ROW;

        double marginTopBottom = (containerHeight - gridHeight) / 2;
        double marginLeftRight = (containerWidth - gridWidth) / 2;

//        AnchorPane.setRightAnchor(platform, anchorLeftRight);
//        AnchorPane.setLeftAnchor(platform, anchorLeftRight);
//        AnchorPane.setTopAnchor(platform, anchorTopBottom);
//        AnchorPane.setBottomAnchor(platform, anchorTopBottom);

        platform.setPadding(new Insets(marginTopBottom, marginLeftRight, marginTopBottom, marginLeftRight));
    }

    public void gridInit() {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100.0 / Constants.ROW);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.0 / Constants.COL);
        for (int i = 0; i < Constants.ROW; i++)
            platform.getRowConstraints().add(rowConstraints);
        for (int i = 0; i < Constants.COL; i++)
            platform.getColumnConstraints().add(columnConstraints);
    }

    private void constructCell(int x, int y) {
        BorderPane tmpPane = new BorderPane();
        tmpPane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.UNVISITED + ";");
        platform.add(tmpPane, y, x);
        BorderGrid[x][y] = tmpPane;
        //Event related

        //Handle event when design graph
        tmpPane.setOnMouseEntered(e -> {
            if (currentState == CellState.WALL) {
                if (CellGrid[x][y].state != CellState.SOURCE && CellGrid[x][y].state != CellState.TARGET && applyColor) {
                    tmpPane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.WALL + ";");
                    CellGrid[x][y].state = CellState.WALL;
                }
            }

            if (currentState == CellState.WEIGHT) {
                if (CellGrid[x][y].state != CellState.SOURCE && CellGrid[x][y].state != CellState.TARGET && applyColor) {
                    tmpPane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.WEIGHT + ";");
                    CellGrid[x][y].state = CellState.WEIGHT;
                    CellGrid[x][y].weighted = true;
                    CellGrid[x][y].count = Constants.WEIGHT_COUNT;
                }
            }

            if (currentState == CellState.UNVISITED) {
                if (CellGrid[x][y].state != CellState.SOURCE && CellGrid[x][y].state != CellState.TARGET && applyColor) {
                    tmpPane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.UNVISITED + ";");
                    CellGrid[x][y].state = CellState.UNVISITED;
                    CellGrid[x][y].weighted = false;
                    CellGrid[x][y].count = 0;
                }
            }
        });

        tmpPane.setOnMouseClicked(e -> {
            if (currentState == CellState.SOURCE) {
                if (CellGrid[x][y].state == CellState.SOURCE) {
                    unvisitCell(0);
                } else if (CellGrid[x][y].state == CellState.UNVISITED) {
                    unvisitCell(0);
                    CellGrid[x][y].state = CellState.SOURCE;
                    currentST[0][0] = x;
                    currentST[0][1] = y;
                    paintBlock(x, y, Constants.BORDER, Constants.SOURCE);
                }
            } else if (currentState == CellState.TARGET) {
//                System.out.println("Current state: Target");
                if (CellGrid[x][y].state == CellState.TARGET) {
                    unvisitCell(1);
                } else if (CellGrid[x][y].state == CellState.UNVISITED) {
                    unvisitCell(1);
                    CellGrid[x][y].state = CellState.TARGET;
                    currentST[1][0] = x;
                    currentST[1][1] = y;
                    paintBlock(x, y, Constants.BORDER, Constants.TARGET);
                }
            }

            if (currentState == CellState.WALL || currentState == CellState.WEIGHT || currentState == CellState.UNVISITED) {
                applyColor = !(applyColor);

                if (applyColor && CellGrid[x][y].state != CellState.SOURCE && CellGrid[x][y].state != CellState.TARGET) {
                    if (currentState == CellState.WALL) {
                        paintBlock(x, y, Constants.BORDER, Constants.WALL);
                        CellGrid[x][y].state = CellState.WALL;
                    } else if (currentState == CellState.WEIGHT) {
                        paintBlock(x, y, Constants.BORDER, Constants.WEIGHT);
                        CellGrid[x][y].state = CellState.WEIGHT;
                        CellGrid[x][y].weighted = true;
                    } else {
                        paintBlock(x, y, Constants.BORDER, Constants.UNVISITED);
                        CellGrid[x][y].state = CellState.UNVISITED;
                    }
                }
            }


        });

        CellGrid[x][y].state = CellState.UNVISITED; //default
    }

    // reverse source node or target node to unvisited node
    private void unvisitCell(int row) {
        if (currentST[row][0] != -1) {
            paintBlock(currentST[row][0], currentST[row][1], Constants.BORDER, Constants.UNVISITED);
            CellGrid[currentST[row][0]][currentST[row][1]].state = CellState.UNVISITED;
            CellGrid[currentST[row][0]][currentST[row][1]].weight = Constants.UNVISITED_WEIGHT;

            CellGrid[currentST[row][0]][currentST[row][1]].count = Constants.COUNT;

        }
    }

    public synchronized static void paintBlock(int x, int y, String border, String background) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (background == Constants.SOURCE || background == Constants.TARGET)
                    BorderGrid[x][y].setStyle("-fx-border-color: " + border + "; -fx-background-image: url('" + background + "')" + ";" +
                            "-fx-background-position: center center; " +
                            "-fx-background-size: cover");

                else
                    BorderGrid[x][y].setStyle("-fx-border-color: " + border + "; -fx-background-color: " + background + ";");

                new BounceIn(BorderGrid[x][y], 1, true).play();
            }
        });
    }


    private void clearGrid() {
        for (int x = 0; x < Constants.ROW; x++) {
            for (int y = 0; y < Constants.COL; y++) {

                CellGrid[x][y].setParent(-1, -1); // Set parent to null
                CellGrid[x][y].distance = Integer.MAX_VALUE;
                // Remove Everything except the walls
                if (CellGrid[x][y].state != CellState.WALL && CellGrid[x][y].state != CellState.WEIGHT) {
                    paintBlock(x, y, Constants.BORDER, Constants.UNVISITED);
                    CellGrid[x][y].state = CellState.UNVISITED;
                    CellGrid[x][y].weight = Constants.UNVISITED_WEIGHT;
                } else if (CellGrid[x][y].state == CellState.WALL) {
                    CellGrid[x][y].weight = Constants.WALL_WEIGHT;
                } else if (CellGrid[x][y].state == CellState.WEIGHT) {
                    CellGrid[x][y].weight = Constants.WEIGHT_WEIGHT;
                    CellGrid[x][y].count = Constants.WEIGHT_COUNT;
                }
                if (CellGrid[x][y].weighted)
                {
                    CellGrid[x][y].count = Constants.WEIGHT_COUNT;
                    paintBlock(CellGrid[x][y].x, CellGrid[x][y].y, Constants.BORDER, Constants.WEIGHT);
                }

            }
        }
        //resetCount();
        // repaint the source and target point
        paintBlock(currentST[0][0], currentST[0][1], Constants.BORDER, Constants.SOURCE);
        paintBlock(currentST[1][0], currentST[1][1], Constants.BORDER, Constants.TARGET);

        CellGrid[currentST[0][0]][currentST[0][1]].state = CellState.SOURCE;
        CellGrid[currentST[1][0]][currentST[1][1]].state = CellState.TARGET;
    }


    public void toggleButton(boolean logic) {
        stopButton.setDisable(logic);
        pauseButton.setDisable(logic);
        sourceButton.setDisable(!logic);
        weightButton.setDisable(!logic);
        targetButton.setDisable(!logic);
        wallButton.setDisable(!logic);
        unvisitedButton.setDisable(!logic);
        startButton.setDisable(!logic);
        clearPathButton.setDisable(!logic);
        clearButton.setDisable(!logic);
        mazeButton.setDisable(!logic);
    }

    // Events
    @FXML
    public void unvisitedBtnEvent(ActionEvent actionEvent) {
        currentState = CellState.UNVISITED;
        applyColor = false;
    }

    @FXML
    public void wallBtnEvent(ActionEvent actionEvent) {
        currentState = CellState.WALL;
        applyColor = false;
    }

    @FXML
    public void sourceBtnEvent(ActionEvent actionEvent) {
        currentState = CellState.SOURCE;
    }

    @FXML
    public void targetBtnEvent(ActionEvent actionEvent) {
        currentState = CellState.TARGET;
    }

    @FXML

    public void weightBtnEvent(ActionEvent actionEvent) {
        currentState = CellState.WEIGHT;
        applyColor = false;
    }


    @FXML
    public void startBtnEvent(ActionEvent actionEvent) {

        if (Constants.currentThread == null && currentST[0][0] != -1 && currentST[1][0] != -1 && selectedAlgo != -1) {
            // Disable all button, disable draw mode
            applyColor = false;
            toggleButton(false);
            currentState = null;
            Algorithm algorithm = null;
            clearGrid();

            switch (selectedAlgo) {
                case 0:
                    clearWeight();
                    algorithm = new BreadthFirst();
                    break;
                case 1:
                    clearWeight();
                    algorithm = new DepthFirst();
                    break;
                case 2:
                    algorithm = new Dijkstra();
                    weightButton.setDisable(false);
                    break;
            }

            Constants.currentThread = algorithm;
            algorithm.initialize(CellGrid[currentST[0][0]][currentST[0][1]], CellGrid[currentST[1][0]][currentST[1][1]]);
            algorithm.start();

        }
    }

    public void clearWeight() {
        for (int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                if(CellGrid[i][j].weighted)
                {
                    CellGrid[i][j].count = 0;
                    CellGrid[i][j].weighted = false;
                    CellGrid[i][j].distance = Integer.MAX_VALUE;
                    paintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                }

            }
        }
    }
    @FXML
    public void clearBtnEvent(ActionEvent actionEvent) {
        clearWeight();
        for (int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                CellGrid[i][j].state = CellState.UNVISITED;
                paintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
            }
        }
        for (int i = 0; i < 2; i++) currentST[i][0] = -1;
    }

    @FXML
    public void clearPathBtnEvent(ActionEvent actionEvent) {
        clearGrid();
        for (int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                if (CellGrid[i][j].state != CellState.WALL && !CellGrid[i][j].weighted && CellGrid[i][j].state != CellState.SOURCE && CellGrid[i][j].state != CellState.TARGET) {
                    CellGrid[i][j].state = CellState.UNVISITED;
                    paintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                    CellGrid[i][j].distance = Integer.MAX_VALUE;
                }
            }
        }
    }

    @FXML
    public void pauseBtnEvent(ActionEvent actionEvent) {
        Constants.isPause = !(Constants.isPause);
    }

    @FXML
    public void stopBtnEvent(ActionEvent actionEvent) {
        try {
            if (Constants.currentThread != null) {
                Constants.currentThread.killThread();
                System.out.println("Thread killed///");
            }
            applyColor = false;
            Constants.isPause = false;
            toggleButton(true);
        } catch (Exception e) {
            System.out.println("Error when stopping");
        }
    }

    @FXML
    public void mazeBtnEvent(ActionEvent actionEvent) {
        currentST[0][0]=-1;
        currentST[1][0]=-1;
        MazeGenerator mazeGenerator = new MazeGenerator();
        mazeGenerator.start();

        System.out.println("Thread drawing maze kill...");
        mazeGenerator.interrupt();
    }
}
