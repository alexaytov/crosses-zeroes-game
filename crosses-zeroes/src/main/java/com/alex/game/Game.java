package com.alex.game;

import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class Game {

    private int[][] matrix;
    private int firstPersonSign = 1;
    private int secondPersonSign = 2;
    private int GLOBAL_COUNTER = 0;
    private Zobrist zobrist;

    public Game(int rows, int cols) {
        // TODO: 20/05/01 add validation
        this.matrix = new int[rows][cols];
        zobrist = new Zobrist();

    }

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
    @SuppressWarnings("Duplicates")
    public int isGameEnded(int[][] matrix) {


        if (checkHorizontal(firstPersonSign, 5, matrix) || checkVertical(firstPersonSign, 5, matrix)) {
            return 1;
        }

        if (checkHorizontal(secondPersonSign, 5, matrix) || checkVertical(secondPersonSign, 5, matrix)) {
            return 2;
        }

        // diagonal check
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int currentElement = matrix[i][j];
                if (currentElement != 0) {
                    // primary diagonal check
                    int count = 0;
                    for (int k = 1; k <= 4; k++) {
                        if (isOutOfBounds(i - k, j + k) || currentElement != matrix[i - k][j + k]) {
                            count = 0;
                            break;
                        } else {
                            count++;
                        }
                    }
                    if (count >= 4) {
                        return currentElement;
                    }
                    // check secondary diagonal
                    count = 0;
                    for (int k = 1; k <= 4; k++) {
                        if (isOutOfBounds(i + k, j + k) || currentElement != matrix[i + k][j + k]) {
                            break;
                        } else {
                            count++;
                        }
                    }
                    if (count >= 4) {
                        return currentElement;
                    }
                }
            }
        }
        return 0;
    }

    @SuppressWarnings("Duplicates")
    // 0 -> false 1 -> true 2 -> true withe other player nearby
    public int evaluateVertical(int playerToCheck, int max, int[][] matrix) {
        int withOpponentsCounter = 0;
        int alone = 0;
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
                        withOpponentsCounter++;
                    } else if (!isPreviousElementTheOpenonet) {
                        alone++;
                    }
                }
            }
        }

        if (alone >= 1) {
            return alone * 10;
        }
        if (withOpponentsCounter >= 1) {
            return withOpponentsCounter;
        }

        return 0;
    }

    // 0 -> no 1-> yes 2-> yes with other player nearby 3 -> more than once yes with one nearby 4 -> more than one without opponents
    public int evaluateHorizontal(int playerToCheck, int max, int[][] matrix) {
        int withOpponentsCounter = 0;
        int alone = 0;
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
                        withOpponentsCounter++;
                    } else if (!isPreviousElementTheOpenonet) {
                        alone++;
                    }


                }
            }
        }
        if (alone >= 1) {
            return alone * 10;
        }
        if (withOpponentsCounter >= 1) {
            return withOpponentsCounter;
        }

        return 0;
    }

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

    @SuppressWarnings("Duplicates")
    private boolean checkHorizontal(int playerToCheck, int max, int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            int counter = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == playerToCheck) {
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

    private boolean isAvailableFields() {
        for (int[] row : matrix) {
            for (int element : row) {
                if (element == 0) {
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
        int[][] scoreMatrix = new int[15][15];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {

                    // make the move
                    matrix[i][j] = firstPersonSign;
                    int[][] copyOfNode = Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
                    int moveScore = minimax(copyOfNode, 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    scoreMatrix[i][j] = moveScore;
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

        System.out.println("This is the score matrix");
        printMatrix(scoreMatrix);
        System.out.println();

        System.out.println(" Best move is " + bestMoveX + bestMoveY);

        matrix[bestMoveX][bestMoveY] = firstPersonSign;
    }

    // 0-> no 1-> yes 2-> yes with nearby
    public int evaluateDiagonal(int playerToCheck, int max, int[][] matrix) {
        int alone = 0;
        int withOpponentsCounter = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
//                int currentElement = matrix[i][j];
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
                            withOpponentsCounter++;
                        } else if (!isPreviousElementTheOpponents) {
                            alone++;
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
                            withOpponentsCounter++;
                        } else if (!isPreviousElementOpponent) {
                            alone++;
                        }
                    }
                }
            }
        }

        if(alone >= 1){
            return alone*10;
        }
        if(withOpponentsCounter >= 1){
            return withOpponentsCounter;
        }
        return 0;
    }

    private int evaluateBoard(int[][] matrix) {

        // evaluate vertical
        int verticalSecond5 = evaluateVertical(secondPersonSign, 5, matrix);
        int verticalSecond4 = evaluateVertical(secondPersonSign, 4, matrix);

//        if(verticalSecond5 != 0){
//            return verticalSecond5 * -1;
//        }
//
//        if(verticalSecond4 != 0){
//            return verticalSecond4 * -1;
//        }

        // evaluate horizontal
        int horizontalSecond5 = evaluateHorizontal(secondPersonSign, 5, matrix);
        int horizontalSecond4 = evaluateHorizontal(secondPersonSign, 4, matrix);

//        if(horizontalSecond5 != 0){
//            return horizontalSecond5 * -1;
//        }
//
//        if(horizontalSecond4 != 0){
//            return horizontalSecond4 * -1;
//        }

        int evaluateDiagonal5 = evaluateDiagonal(secondPersonSign, 5, matrix);
        int evaluateDiagonal4 = evaluateDiagonal(secondPersonSign, 4, matrix);

//        if(evaluateDiagonal5 != 0){
//            return evaluateDiagonal5 * -1;
//        }
//
//        if(evaluateDiagonal4 != 0){
//            return evaluateDiagonal4 * -1;
//        }

        int hplus = evaluateHorizontal(firstPersonSign, 5, matrix);
        int vplus = evaluateVertical(firstPersonSign, 5, matrix);
        int dplus = evaluateDiagonal(firstPersonSign, 5, matrix);

        if(hplus > 0 || vplus > 0 || dplus > 0){
            return 1;
        }

        int negative = (horizontalSecond4 + horizontalSecond5 + verticalSecond4 + verticalSecond5 + evaluateDiagonal4 + evaluateDiagonal5) * -1;

        if(negative == 0){
            return evaluateHorizontal(firstPersonSign, 4, matrix) +
            hplus +
            evaluateVertical(firstPersonSign, 4, matrix) +
            vplus +
            evaluateDiagonal(firstPersonSign, 4, matrix) +
            dplus;
        }

        return negative;

    }

    private int minimax(int[][] node, int depth, boolean isMax, int alpha, int beta) {
//        if(depth == 0 || !isMovesLeft(node)){
//            return evaluateBoard(node);
//        }

        int boardScore = evaluateBoard(node);
//        if(boardScore != 0 || !isMovesLeft(node)){
//            return boardScore;
//        }
        if (depth == 0 || !isMovesLeft(node)) {
            return boardScore;
        }
        // copy array


        if (isMax) {
            int maxEvaluation = Integer.MIN_VALUE;
            for (int i = 0; i < node.length; i++) {
                for (int j = 0; j < node[i].length; j++) {

                    // make move
                    if (node[i][j] == 0) {
                        node[i][j] = firstPersonSign;

                        int evaluation = minimax(node, depth - 1, false, alpha, beta);

                        maxEvaluation = Integer.max(evaluation, maxEvaluation);

                        alpha = Integer.max(alpha, maxEvaluation);

                        zobrist.addEntry(node, new PositionInformation(evaluation, alpha, beta));

                        node[i][j] = 0;
                        if (beta <= alpha) {
                            break;
                        }

                    }
                }
            }
            return maxEvaluation;
        } else {
            int minEvaluation = Integer.MAX_VALUE;

            for (int i = 0; i < node.length; i++) {
                for (int j = 0; j < node[i].length; j++) {

                    if (node[i][j] == 0) {
                        // make move
                        node[i][j] = secondPersonSign;

                        int evaluation = minimax(node, depth - 1, true, alpha, beta);

                        minEvaluation = Integer.min(evaluation, minEvaluation);

                        beta = Integer.min(beta, minEvaluation);

                        zobrist.addEntry(matrix, new PositionInformation(evaluation, alpha, beta));
                        node[i][j] = 0;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return minEvaluation;
        }
    }

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

    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%d\t", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
