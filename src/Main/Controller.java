package Main;

import Main.Algorithms.Algorithm;
import Main.Algorithms.BreadthFirst;
import Main.Algorithms.DepthFirst;
import Main.GraphRelated.Cell;
import Main.GraphRelated.CellState;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import Main.Configurations.Constants;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ComboBox<String> algoOptions;
    @FXML
    private GridPane platform;
    @FXML
    private JFXButton sourceButton, wallButton, unvisitedButton, targetButton, startButton, stopButton, clearButton, clearPathButton, pauseButton;

    public static BorderPane[][] BorderGrid = new BorderPane[Constants.ROW][Constants.COL];
    public static Cell[][] CellGrid = new Cell[Constants.ROW][Constants.COL];

    private int[][] currentST = new int[2][2]; // location of source and target, 0 for source, 1 for target

    private int selectedAlgo;
    private CellState currentState;
    private boolean applyColor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        algoOptions.getItems().addAll("Breadth First Search", "Depth First Search", "Dijkstra Algorithm");
        algoOptions.setOnAction(e -> selectedAlgo = algoOptions.getSelectionModel().getSelectedIndex());

        for (int x = 0; x < Constants.ROW; x++) {
            for (int y = 0; y < Constants.COL; y++) {
                CellGrid[x][y] = new Cell(x, y);
                constructCell(x, y);
            }
        }

        for (int i = 0; i < 2; i++) currentST[i][0] = -1;

        selectedAlgo = -1; //initially no algorithm is selected.
        currentState = null;
        applyColor = false;
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

            if (currentState == CellState.UNVISITED) {
                if (CellGrid[x][y].state != CellState.SOURCE && CellGrid[x][y].state != CellState.TARGET && applyColor) {
                    tmpPane.setStyle("-fx-border-color: " + Constants.BORDER + "; -fx-background-color: " + Constants.UNVISITED + ";");
                    CellGrid[x][y].state = CellState.UNVISITED;
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

            if (currentState == CellState.WALL || currentState == CellState.UNVISITED) {
                applyColor = !(applyColor);

                if (applyColor && CellGrid[x][y].state != CellState.SOURCE && CellGrid[x][y].state != CellState.TARGET) {
                    if (currentState == CellState.WALL) {
                        paintBlock(x, y, Constants.BORDER, Constants.WALL);
                        CellGrid[x][y].state = CellState.WALL;
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
        }
    }

    public synchronized static void paintBlock(int x, int y, String border, String background) {
        BorderGrid[x][y].setStyle("-fx-border-color: " + border + "; -fx-background-color: " + background + ";");
    }

    private void disableButton() {
    }

    private void redrawGrid() {
        for (int x = 0; x < Constants.ROW; x++) {
            for (int y = 0; y < Constants.COL; y++) {

                CellGrid[x][y].setParent(-1, -1); // Set parent to null

                // Remove Everything except the walls
                if (CellGrid[x][y].state != CellState.WALL) {
                    paintBlock(x, y, Constants.BORDER, Constants.UNVISITED);
                    CellGrid[x][y].state = CellState.UNVISITED;

                    CellGrid[x][y].weight = Constants.UNVISITED_WEIGHT;
                } else {
                    CellGrid[x][y].weight = Constants.WALL_WEIGHT;
                }
            }
        }

        // repaint the source and target point
        paintBlock(currentST[0][0], currentST[0][1], Constants.BORDER, Constants.SOURCE);
        paintBlock(currentST[1][0], currentST[1][1], Constants.BORDER, Constants.TARGET);

        CellGrid[currentST[0][0]][currentST[0][1]].state = CellState.SOURCE;
        CellGrid[currentST[1][0]][currentST[1][1]].state = CellState.TARGET;
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
    public void startBtnEvent(ActionEvent actionEvent) {
        // Disable all button, disable draw mode
        applyColor = false;
        disableButton();

        if (Constants.currentThread == null && currentST[0][0] != -1 && currentST[1][0] != -1 && selectedAlgo != -1) {
            Algorithm algorithm = null;
            redrawGrid();

            switch (selectedAlgo) {
                case 0:
                    algorithm = new BreadthFirst();
                    break;
                case 1:
                    algorithm = new DepthFirst();
                    break;
                case 2:
                    break;
            }

            Constants.currentThread = algorithm;
            algorithm.initialize(CellGrid[currentST[0][0]][currentST[0][1]], CellGrid[currentST[1][0]][currentST[1][1]]);
            algorithm.start();
            stopButton.setDisable(false);
            pauseButton.setDisable(false);


        }
    }

    @FXML
    public void clearBtnEvent(ActionEvent actionEvent) {
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
        for (int i = 0; i < Constants.ROW; i++) {
            for (int j = 0; j < Constants.COL; j++) {
                if (CellGrid[i][j].state != CellState.WALL && CellGrid[i][j].state != CellState.SOURCE && CellGrid[i][j].state != CellState.TARGET) {
                    CellGrid[i][j].state = CellState.UNVISITED;
                    paintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                }
            }
        }
    }

    @FXML
    public void pauseBtnEvent(ActionEvent actionEvent)  {

    }

    @FXML
    public void stopBtnEvent(ActionEvent actionEvent) {
        try {
            if (Constants.currentThread != null) Constants.currentThread.killThread();
            Constants.currentThread = null;

            applyColor = false;
            pauseButton.setDisable(true);
            sourceButton.setDisable(false);
            targetButton.setDisable(false);
            wallButton.setDisable(false);
            for (int i = 0; i < Constants.ROW; i++) {
                for (int j = 0; j < Constants.COL; j++) {
                    if (CellGrid[i][j].state != CellState.WALL && CellGrid[i][j].state != CellState.SOURCE && CellGrid[i][j].state != CellState.TARGET) {
                        CellGrid[i][j].state = CellState.UNVISITED;
                        paintBlock(i, j, Constants.BORDER, Constants.UNVISITED);
                    }
                }
            }

            stopButton.setDisable(true);
        } catch (Exception e) {
        }
    }

}
