package com.alex.game;

import java.util.Arrays;


public class Game {

    private int[][] matrix;
    // ai
    private int firstPersonSign = 1;
    // human
    private int secondPersonSign = 2;

    public Game(int rows, int cols) {
        // TODO: 20/05/01 add validation
        this.matrix = new int[rows][cols];
        Zobrist zobrist = new Zobrist();

    }

    // human make move
    public void setValue(int x, int y) {
        if (isOutOfBounds(x, y)) {
            throw new IllegalArgumentException("index out of bounds");
        }

        int currElement = matrix[x][y];
        if (isTaken(currElement)) {
            throw new IllegalArgumentException("element already chosen");
        }

        matrix[x][y] = secondPersonSign;
    }

    private boolean isTaken(int currElement) {
        return currElement == firstPersonSign || currElement == 2;
    }

    // 0 -> tie 1 -> first player 2-> second player
    public int isGameEnded(int[][] matrix) {
        if (checkHorizontal(firstPersonSign, 5, matrix) || checkVertical(firstPersonSign, 5, matrix)) {
            return 1;
        }

        if (checkHorizontal(secondPersonSign, 5, matrix) || checkVertical(secondPersonSign, 5, matrix)) {
            return 2;
        }

        if(evaluateDiagonal(firstPersonSign, 5, matrix) == 1){
            return 1;
        }

        if(evaluateDiagonal(secondPersonSign, 5, matrix) == 1){
            return 2;
        }

        // tie
        return 0;
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
                    boolean isPreviousElementTheOpenonet = !isOutOfBounds(j - max, i) && matrix[j - max][i] != 0 && matrix[j - max][i] != playerToCheck;
//                    int previousElement = matrix[j - max][i];
                    boolean isNextElementTheOponent = !isOutOfBounds(j + 1, i) && matrix[j + 1][i] != 0 && matrix[j + 1][i] != playerToCheck;
//                    int nextElement = matrix[j+1][i];

                    if (isPreviousElementTheOpenonet ^ isNextElementTheOponent) {
                        combinationsWithOpponentBlocking++;
                    } else if (!isPreviousElementTheOpenonet) {
                        combinationsWithoutBlockingOpponent++;
                    }
                }
            }
        }
        if (combinationsWithoutBlockingOpponent >= 1) {
            return combinationsWithoutBlockingOpponent * 10;
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
            for (int j = 0; j < matrix.length - max + 1; j++) {
                int element = matrix[i][j];
                if (element == playerToCheck) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == max) {
                    boolean isPreviousElementTheOpenonet = !isOutOfBounds(i, j - max) && matrix[i][j - max] != 0 && matrix[i][j - max] != playerToCheck;
//                    int previousElement = matrix[i][j - max];
                    boolean isNextElementTheOponent = !isOutOfBounds(i, j + 1) && matrix[i][j + 1] != 0 && matrix[i][j + 1] != playerToCheck;
//                    int nextElement = matrix[i][j+1];

                    if (isPreviousElementTheOpenonet ^ isNextElementTheOponent) {
                        combinationsWithBlockingOpponent++;
                    } else if (!isPreviousElementTheOpenonet) {
                        combinationsWithoutBlockingOpponent++;
                    }
                }
            }
        }
        if (combinationsWithoutBlockingOpponent >= 1) {
            return combinationsWithoutBlockingOpponent * 10;
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
    public void nextTurn() {
        int bestEvaluation = Integer.MIN_VALUE;
        int bestMoveX = -1;
        int bestMoveY = -1;
        // traverse all cells and check each one minimax score and pick the best one

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {

                    // make the move
                    matrix[i][j] = firstPersonSign;

                    int[][] copyOfNode = Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
                    // get score for current move
                    int moveScore = minimax(copyOfNode, 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    // undo the move
                    matrix[i][j] = 0;

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
        matrix[bestMoveX][bestMoveY] = firstPersonSign;
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
                        boolean isPreviousElementTheOpponents = !isOutOfBounds(i + 1, j - 1) && matrix[i + 1][j - 1] != 0 && matrix[i + 1][j - 1] != playerToCheck;
//                        int previousElement = matrix[i + 1][j - 1];
                        boolean isNextElementTheOpponent = !isOutOfBounds(i - max, j + max) && matrix[i - max][j + max] != 0 && matrix[i - max][j + max] != playerToCheck;
//                        int nextElement = matrix[i - max][j + max];

                        if (isPreviousElementTheOpponents ^ isNextElementTheOpponent) {
                            combinationWithBlockingOpponent++;
                        } else if (!isPreviousElementTheOpponents) {
                            combinationWithoutBlockingOpponent++;
                        }
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

        if(combinationWithoutBlockingOpponent >= 1){
            return combinationWithoutBlockingOpponent*10;
        }
        if(combinationWithBlockingOpponent >= 1){
            return combinationWithBlockingOpponent;
        }
        // there are either no combinations or there are already blocked
        return 0;
    }

    private int evaluateBoard(int[][] matrix) {

        // evaluate vertical for human
        int verticalSecond5 = evaluateVertical(secondPersonSign, 5, matrix);
        int verticalSecond4 = evaluateVertical(secondPersonSign, 4, matrix);

        // evaluate horizontal for human
        int horizontalSecond5 = evaluateHorizontal(secondPersonSign, 5, matrix);
        int horizontalSecond4 = evaluateHorizontal(secondPersonSign, 4, matrix);

        // evaluate diagonal for human
        int evaluateDiagonal5 = evaluateDiagonal(secondPersonSign, 5, matrix);
        int evaluateDiagonal4 = evaluateDiagonal(secondPersonSign, 4, matrix);

        // evaluate if ai is able to win in the next move
        int hplus = evaluateHorizontal(firstPersonSign, 5, matrix);
        int vplus = evaluateVertical(firstPersonSign, 5, matrix);
        int dplus = evaluateDiagonal(firstPersonSign, 5, matrix);

        // if ai cna win it should always take the move
        if(hplus > 0 || vplus > 0 || dplus > 0){
            return 1;
        }

        // figure out best blocking decision
        int negative = (horizontalSecond4 + horizontalSecond5 + verticalSecond4 + verticalSecond5 + evaluateDiagonal4 + evaluateDiagonal5) * -1;

        // if a blocking position is not needed ai should take the best move
        // based on itself wining
        if(negative == 0){
            return evaluateHorizontal(firstPersonSign, 4, matrix) +
            hplus +
            evaluateVertical(firstPersonSign, 4, matrix) +
            vplus +
            evaluateDiagonal(firstPersonSign, 4, matrix) +
            dplus;
        }

        // return the best blocking position
        return negative;

    }

    private int minimax(int[][] node, int depth, boolean isMax, int alpha, int beta) {
        int boardScore = evaluateBoard(node);

        if (depth == 0 || !isMovesLeft(node)) {
            return boardScore;
        }
        // maximizing player
        if (isMax) {
            int maxEvaluation = Integer.MIN_VALUE;
            for (int i = 0; i < node.length; i++) {
                for (int j = 0; j < node[i].length; j++) {
                    if (node[i][j] == 0) {
                        // make possible move
                        node[i][j] = firstPersonSign;

                        // calculate current evaluation
                        int evaluation = minimax(node, depth - 1, false, alpha, beta);

                        // get best evaluation
                        maxEvaluation = Integer.max(evaluation, maxEvaluation);

                        alpha = Integer.max(alpha, maxEvaluation);

                        // undo move
                        node[i][j] = 0;

                        // check for alpha beta pruning
                        if (beta <= alpha) {
                            break;
                        }

                    }
                }
            }
            return maxEvaluation;
        } else {
            // minimizing player
            int minEvaluation = Integer.MAX_VALUE;

            for (int i = 0; i < node.length; i++) {
                for (int j = 0; j < node[i].length; j++) {

                    if (node[i][j] == 0) {
                        // make possible move
                        node[i][j] = secondPersonSign;

                        // evaluate move
                        int evaluation = minimax(node, depth - 1, true, alpha, beta);

                        // get best move
                        minEvaluation = Integer.min(evaluation, minEvaluation);

                        beta = Integer.min(beta, minEvaluation);

                        // undo move
                        node[i][j] = 0;

                        // check for alpha beta pruning
                        if (beta <= alpha) {
                            break;
                        }
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
    }

    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.printf("%d\t", element);
            }
            System.out.println();
        }
    }
}