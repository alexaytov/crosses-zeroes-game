package com.alex;

import java.util.Optional;

public class Game {

    private final int[][] matrix;
    private int firstPersonSign = 1;
    private int secondPersonSign = 2;
    private int playerTurn;

    public Game(int length, int width) {
        // TODO: 20/05/01 add validation
        this.matrix = new int[length][width];
        this.playerTurn = 0;
    }

    public void setValue(int x, int y) {
        if (isOutOfBounds(x, y)) {
            throw new IllegalArgumentException("index out of bounds");
        }

        int currElement = matrix[x][y];
        if (isTaken(currElement)) {
            throw new IllegalArgumentException("element already chosen");
        }
        // turn
        if (playerTurn % 2 == 0) {
            matrix[x][y] = firstPersonSign;
        } else {
            matrix[x][y] = secondPersonSign;
        }
        playerTurn++;
    }

    private boolean isTaken(int currElement) {
        return currElement == firstPersonSign || currElement == 2;
    }

    public Optional<String> isGameEnded() {
        int counter = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == firstPersonSign) {
                    counter++;
                    if (counter >= 5) {
                        return Optional.of("first person won");
                    }
                    if (!checkSurroundingElements(i, j, firstPersonSign)) {
                        counter = 0;
                    }
                } else if (matrix[i][j] == secondPersonSign) {
                    counter++;
                    if (counter >= 5) {
                        return Optional.of("second person won");
                    }
                    if (!checkSurroundingElements(i, j, secondPersonSign)) {
                        counter = 0;
                    }
                } else {
                    counter = 0;
                }
            }
        }
        int resultFromCheckDiagonal = checkDiagonal();
        if (resultFromCheckDiagonal == firstPersonSign) {
            return Optional.of("first person wins");
        } else if (resultFromCheckDiagonal == secondPersonSign) {
            return Optional.of("second person wins");
        }
        return Optional.empty();
    }

    // TODO: 20/05/02 remove chekcing of boxes closer than 4 elemtns to the borders
    private int checkDiagonal() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    int currentSign = matrix[i][j];
                    for (int k = 1; k < 5; k++) {
                        if (!isOutOfBounds(i + k, j + k) && matrix[i + k][j + k] == currentSign) {
                            if (k == 4) {
                                return currentSign;
                            }
                        } else if (!isOutOfBounds(i + k, j - k) && matrix[i + k][j - k] == currentSign) {
                            if (k == 4) {
                                return currentSign;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return 0;
    }

    // return false if there are not matches to the current player sign
    private boolean checkSurroundingElements(int x, int y, int currentPlayerSign) {

        if (isOutOfBounds(x, y)) {
            return false;
        }

        //upper element
        if (!isOutOfBounds(x, y - 1) && matrix[x][y - 1] == currentPlayerSign) {
            return true;
        }

        // upper left element
        if (!isOutOfBounds(x - 1, y - 1) && matrix[x - 1][y - 1] == currentPlayerSign) {
            return true;
        }

        //upper right element
        if (!isOutOfBounds(x + 1, y - 1) && matrix[x + 1][y - 1] == currentPlayerSign) {
            return true;
        }

        // left
        if (!isOutOfBounds(x - 1, y) && matrix[x - 1][y] == currentPlayerSign) {
            return true;
        }

        //right
        if (!isOutOfBounds(x + 1, y) && matrix[x + 1][y] == currentPlayerSign) {
            return true;
        }

        //down left
        if (!isOutOfBounds(x - 1, y + 1) && matrix[x - 1][y + 1] == currentPlayerSign) {
            return true;
        }

        //down
        if (!isOutOfBounds(x, y + 1) && matrix[x][y + 1] == currentPlayerSign) {
            return true;
        }

        //down right
        if (!isOutOfBounds(x + 1, y + 1) && matrix[x + 1][y + 1] == currentPlayerSign) {
            return true;
        }

        return false;
    }

    private boolean isOutOfBounds(int x, int y) {
        return x >= matrix.length || y >= matrix[0].length || x < 0 || y < 0;
    }

    public int[][] getMatrix() {
        return matrix;
    }
}
