package com.alex.game;

import com.alex.utils.JavaFXUtils;

import java.util.Arrays;


public class Game {

    private boolean isXFirst;
    private int[][] matrix;
    private int xPersonSign;
    private int oPersonSign;
    private boolean ai;
    private int moveCounter;
    private boolean isXAISign;

    public Game(int rows, int cols, boolean ai, boolean isXFirst, boolean isXAISign) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Invalid rows or cols");
        }
        this.isXAISign = isXAISign;
        this.xPersonSign = 1;
        this.oPersonSign = 2;
        this.moveCounter = 0;
        this.isXFirst = isXFirst;
        this.ai = ai;
        this.matrix = new int[rows][cols];
        aiMakeMoveIfFirst();
    }

    private void aiMakeMoveIfFirst() {
        if (ai) {
            if (isXAISign && isXFirst) {
                nextTurn();
            } else if (!isXAISign && !isXFirst) {
                nextTurn();
            }
        }
    }

    // human make move
    public int setValue(int x, int y) {
        if (isOutOfBounds(x, y)) {
            throw new IllegalArgumentException("index out of bounds");
        }

        if (isTaken(matrix[x][y])) {
            throw new IllegalArgumentException("element already chosen");
        }
        if (ai) {
            moveCounter++;
            if (isXAISign) {
                matrix[x][y] = oPersonSign;
            } else {
                matrix[x][y] = xPersonSign;
            }
            // ai makes turn
            if (isGameEnded(matrix) != -1) {
                return isGameEnded(matrix);
            }
            nextTurn();
            if (isGameEnded(matrix) != -1) {
                return isGameEnded(matrix);
            }
        } else {
            if (isGameEnded(matrix) != -1) {
                return isGameEnded(matrix);
            }
            if (isTaken(matrix[x][y])) {
                JavaFXUtils.popUp("Element is already taken");
            } else {
                moveCounter++;
                if (isXFirst) {
                    if (moveCounter % 2 == 0) {
                        matrix[x][y] = xPersonSign;
                    } else {
                        matrix[x][y] = oPersonSign;
                    }
                } else {
                    if (moveCounter % 2 == 0) {
                        matrix[x][y] = oPersonSign;
                    } else {
                        matrix[x][y] = xPersonSign;
                    }
                }
            }
        }
        return -1;
    }

    private boolean isTaken(int currElement) {
        return currElement == xPersonSign || currElement == oPersonSign;
    }

    // 0 -> tie 1 -> X wins 2-> O wins
    public int isGameEnded(int[][] matrix) {
        if (checkHorizontal(xPersonSign, 5, matrix) || checkVertical(xPersonSign, 5, matrix) || (evaluateDiagonal(xPersonSign, 5, matrix) > 0)) {
            return 1;
        }

        if (checkHorizontal(oPersonSign, 5, matrix) || checkVertical(oPersonSign, 5, matrix) || evaluateDiagonal(oPersonSign, 5, matrix) > 0) {
            return 2;
        }

        // tie
        if (!isMovesLeft(matrix)) {
            return 0;
        }

        return -1;
    }

    // evaluate vertical combinations
    private int evaluateVertical(int playerToCheck, int max, int[][] matrix) {
        int combinationsWithOpponentBlocking = 0;
        int combinationsWithoutBlockingOpponent = 0;
        for (int i = 0; i < matrix.length - max + 1; i++) {
            int counter = 0;
            for (int j = 0; j < matrix.length; j++) {
                int element = matrix[j][i];
                if (element == playerToCheck) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == max) {
                    boolean isPreviousElementTheOpponentOrWall = isOutOfBounds(j - max, i) || (matrix[j - max][i] != 0 && matrix[j - max][i] != playerToCheck);
//                    int previousElement = matrix[j - max][i];
                    boolean isNextElementTheOpponentOrWall = isOutOfBounds(j + 1, i) || (matrix[j + 1][i] != 0 && matrix[j + 1][i] != playerToCheck);
//                    int nextElement = matrix[j+1][i];

                    if (isPreviousElementTheOpponentOrWall ^ isNextElementTheOpponentOrWall) {
                        combinationsWithOpponentBlocking++;
                    } else if (!isPreviousElementTheOpponentOrWall) {
                        combinationsWithoutBlockingOpponent++;
                    }
                    counter = 0;
                }
            }
        }
        if (combinationsWithoutBlockingOpponent >= 1) {
            return combinationsWithoutBlockingOpponent * 100;
        }
        if (combinationsWithOpponentBlocking >= 1) {
            return combinationsWithOpponentBlocking;
        }
        // either there are no combinations or current ones are being blocked
        return 0;
    }

    // evaluate horizontal combinations
    private int evaluateHorizontal(int playerToCheck, int max, int[][] matrix) {
        int combinationsWithBlockingOpponent = 0;
        int combinationsWithoutBlockingOpponent = 0;
        for (int i = 0; i < matrix.length; i++) {
            int counter = 0;
            for (int j = 0; j < matrix.length; j++) {
                int element = matrix[i][j];
                if (element == playerToCheck) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == max) {
                    boolean isPreviousElementTheOpponentOrWall = isOutOfBounds(i, j - max) || (matrix[i][j - max] != 0 && matrix[i][j - max] != playerToCheck);
//                    int previousElement = matrix[i][j - max];
                    boolean isNextElementTheOpponentOrWall = isOutOfBounds(i, j + 1) || (matrix[i][j + 1] != 0 && matrix[i][j + 1] != playerToCheck);
//                    int nextElement = matrix[i][j+1];

                    if (isPreviousElementTheOpponentOrWall ^ isNextElementTheOpponentOrWall) {
                        combinationsWithBlockingOpponent++;
                    } else if (!isPreviousElementTheOpponentOrWall) {
                        combinationsWithoutBlockingOpponent++;
                    }
                    counter = 0;
                }
            }
        }
        if (combinationsWithoutBlockingOpponent >= 1) {
            return combinationsWithoutBlockingOpponent * 100;
        }
        if (combinationsWithBlockingOpponent >= 1) {
            return combinationsWithBlockingOpponent;
        }
        // either there are no combinations or present combinations are blocked
        return 0;
    }

    // check if there is a vertical winner
    private boolean checkVertical(int playerToCheck, int max, int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            int counter = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (!isOutOfBounds(i, j) && matrix[j][i] == playerToCheck) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter >= max) {
                    return true;
                }
            }
        }
        return false;
    }

    // check if there is horizontal winner
    private boolean checkHorizontal(int playerToCheck, int max, int[][] matrix) {
        for (int[] row : matrix) {
            int counter = 0;
            for (int element : row) {
                if (element == playerToCheck) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == max) {
                    return true;
                }
            }
        }
        return false;
    }

    // make next move
    private void nextTurn() {
        int bestEvaluation = Integer.MIN_VALUE;
        int bestMoveX = -1;
        int bestMoveY = -1;
        int[][] scoreMatrix = new int[15][15];
        // traverse all cells and check each one minimax score and pick the best one

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {

                    // make the move
                    matrix[i][j] = isXAISign ? xPersonSign : oPersonSign;

                    int[][] copyOfNode = Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
                    // get score for current move
                    int moveScore = minimax(copyOfNode, 1, !isXAISign, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    // undo the move
                    matrix[i][j] = 0;
                    scoreMatrix[i][j] = moveScore;
                    // if current move value is more than the best value update it
                    if (moveScore > bestEvaluation) {
                        bestEvaluation = moveScore;
                        bestMoveX = i;
                        bestMoveY = j;
                    }
                }
            }
        }
        // make the best move
        if (isXAISign) {
            matrix[bestMoveX][bestMoveY] = xPersonSign;

        } else {
            matrix[bestMoveX][bestMoveY] = oPersonSign;
        }
        System.out.println("print score matrix");

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%d\t", scoreMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    // evaluates the primary and secondary diagonal for combinations
    public int evaluateDiagonal(int playerToCheck, int max, int[][] matrix) {
        // counter for number of combinations found where the opponent is not
        // blocking in either direction
        int combinationWithoutBlockingOpponent = 0;
        // counter for number of combinations if the opponent is blocking in either direction
        int combinationWithBlockingOpponent = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == playerToCheck) {
                    // primary diagonal check
                    int count = 1;
                    for (int k = 1; k < max; k++) {
                        if (!isOutOfBounds(i - k, j + k) && playerToCheck == matrix[i - k][j + k]) {
                            count++;
                        } else {
                            count = 0;
                            break;
                        }
                    }
                    if (count == max) {
                        boolean isPreviousElementTheOpponents = isOutOfBounds(i + 1, j - 1) || (matrix[i + 1][j - 1] != 0 && matrix[i + 1][j - 1] != playerToCheck);
//                        int previousElement = matrix[i + 1][j - 1];
                        boolean isNextElementTheOpponent = isOutOfBounds(i - max, j + max) || (matrix[i - max][j + max] != 0 && matrix[i - max][j + max] != playerToCheck);
//                        int nextElement = matrix[i - max][j + max];

                        if (isPreviousElementTheOpponents ^ isNextElementTheOpponent) {
                            combinationWithBlockingOpponent++;
                        } else if (!isPreviousElementTheOpponents) {
                            combinationWithoutBlockingOpponent++;
                        }
                        count = 0;
                    }
                    // check secondary diagonal
                    count = 1;
                    for (int k = 1; k < max; k++) {
                        if (!isOutOfBounds(i + k, j + k) && playerToCheck == matrix[i + k][j + k]) {
                            count++;
                        } else {
                            count = 0;
                            break;
                        }
                    }
                    if (count == max) {

                        boolean isPreviousElementOpponent = !isOutOfBounds(i - 1, j - 1) && matrix[i - 1][j - 1] != 0 && matrix[i - 1][j - 1] != playerToCheck;
//                        int previousElement = matrix[i - 1][j - 1];
                        boolean isNextElementOpponent = !isOutOfBounds(i + max, j + max) && matrix[i + max][j + max] != 0 && matrix[i + max][j + max] != playerToCheck;
//                        int nextElement = matrix[i + max][j + max];

                        if (isPreviousElementOpponent ^ isNextElementOpponent) {
                            combinationWithBlockingOpponent++;
                        } else if (!isPreviousElementOpponent) {
                            combinationWithoutBlockingOpponent++;
                        }
                    }
                }
            }
        }

        if (combinationWithoutBlockingOpponent >= 1) {
            return combinationWithoutBlockingOpponent * 100;
        }
        if (combinationWithBlockingOpponent >= 1) {
            return combinationWithBlockingOpponent;
        }
        // there are either no combinations or there are already blocked
        return 0;
    }

    public int evaluateBoard(int[][] matrix) {
        int aiSign;
        int personSign;
        if (isXAISign) {
            aiSign = xPersonSign;
            personSign = oPersonSign;
        } else {
            aiSign = oPersonSign;
            personSign = xPersonSign;
        }


        // evaluate if ai is able to win in the next move
        int hplus = evaluateHorizontal(aiSign, 5, matrix);
        int vplus = evaluateVertical(aiSign, 5, matrix);
        int dplus = evaluateDiagonal(aiSign, 5, matrix);

        // if ai cna win it should always take the move
        if (hplus > 0 || vplus > 0 || dplus > 0) {
            return 1;
        }

        // evaluate vertical for human
        int verticalSecond5 = evaluateVertical(personSign, 5, matrix);
        int verticalSecond4 = evaluateVertical(personSign, 4, matrix);

        // evaluate horizontal for human
        int horizontalSecond5 = evaluateHorizontal(personSign, 5, matrix);
        int horizontalSecond4 = evaluateHorizontal(personSign, 4, matrix);

        // evaluate diagonal for human
        int evaluateDiagonal5 = evaluateDiagonal(personSign, 5, matrix);
        int evaluateDiagonal4 = evaluateDiagonal(personSign, 4, matrix);

        if (evaluateDiagonal5 > 0 || horizontalSecond5 > 0 || verticalSecond5 > 0) {
            return (evaluateDiagonal5 + horizontalSecond5 + verticalSecond5) * -1;
        }

        if (evaluateDiagonal4 > 0 || horizontalSecond4 > 0 || verticalSecond4 > 0) {
            return (evaluateDiagonal4 + horizontalSecond4 + verticalSecond4) * -1;
        }

        // if a blocking position is not needed ai should take the best move
        // based on itself wining
        return evaluateHorizontal(aiSign, 4, matrix) +
                evaluateVertical(aiSign, 4, matrix) +
                evaluateDiagonal(aiSign, 4, matrix);

        // return the best blocking position
    }

    private int minimax(int[][] node, int depth, boolean isMax, int alpha, int beta) {
        int boardScore = evaluateBoard(node);

        if (depth == 0 || !isMovesLeft(node) || boardScore == 1) {
            return boardScore;
        }
        // maximizing player
        if (isMax) {
            int maxEvaluation = 0;
            for (int i = 0; i < node.length; i++) {
                for (int j = 0; j < node[i].length; j++) {
                    if (node[i][j] == 0) {
                        // make possible move
                        node[i][j] = xPersonSign;

                        // calculate current evaluation
                        int evaluation = minimax(node, depth - 1, false, alpha, beta);

                        // get best evaluation
//                        maxEvaluation = Integer.max(evaluation, maxEvaluation);
                        maxEvaluation += evaluation;
                        alpha = Integer.max(alpha, maxEvaluation);

                        // undo move
                        node[i][j] = 0;

                        // check for alpha beta pruning
//                        if (beta <= alpha) {
//                            break;
//                        }

                    }
                }
            }
            return maxEvaluation;
        } else {
            // minimizing player
            int minEvaluation = 0;

            for (int i = 0; i < node.length; i++) {
                for (int j = 0; j < node[i].length; j++) {

                    if (node[i][j] == 0) {
                        // make possible move
                        node[i][j] = oPersonSign;

                        // evaluate move
                        int evaluation = minimax(node, depth - 1, true, alpha, beta);

                        // get best move
//                        minEvaluation = Integer.min(evaluation, minEvaluation);

                        minEvaluation += evaluation;

                        beta = Integer.min(beta, minEvaluation);

                        // undo move
                        node[i][j] = 0;

                        // check for alpha beta pruning
//                        if (beta <= alpha) {
//                            break;
//                        }
                    }
                }
            }
            return minEvaluation;
        }
    }

    // checks if there are any available moves
    private boolean isMovesLeft(int[][] matrix) {
        for (int[] row : matrix) {
            for (int elem : row) {
                if (elem == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOutOfBounds(int x, int y) {
        return x >= matrix.length || y >= matrix[0].length || x < 0 || y < 0;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void reset() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = 0;
            }
        }
        moveCounter = 0;
        aiMakeMoveIfFirst();
    }
}