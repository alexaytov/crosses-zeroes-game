package com.alex.controllers;

import com.alex.game.Game;

public class test {
    public static void main(String[] args) {
        int[][] matrix = new int[15][15];


        Game game = new Game(15, 15);

        // horizontal testing
//        matrix[2][1] = 1;
//        matrix[2][2] = 2;
//        matrix[2][3] = 2;
//        matrix[2][4] = 2;
//        matrix[2][5] = 2;
//        matrix[2][6] = 1;

//        matrix[5][3] = 1;
//        matrix[5][4] = 2;
//        matrix[5][5] = 2;
//        matrix[5][6] = 2;
//        matrix[5][7] = 2;
//
//
//        matrix[7][3] = 1;
//        matrix[7][4] = 2;
//        matrix[7][5] = 2;
//        matrix[7][6] = 2;
//        matrix[7][7] = 2;


        // vertical testing

//        matrix[1][2] = 1;
//        matrix[2][2] = 2;
//        matrix[3][2] = 2;
//        matrix[4][2] = 2;
//        matrix[5][2] = 2;
//        matrix[6][2] = 1;


//        matrix[6][5] = 1;
//        matrix[7][5] = 2;
//        matrix[8][5] = 2;
//        matrix[9][5] = 2;
//        matrix[10][5] = 2;
//        matrix[11][5] = 1;
////
//        matrix[9][10] = 1;
//        matrix[10][10] = 2;
//        matrix[11][10] = 2;
//        matrix[12][10] = 2;
//        matrix[13][10] = 2;
////
//        matrix[10][11] = 1;
//        matrix[11][11] = 2;
//        matrix[12][11] = 2;
//        matrix[13][11] = 2;
//        matrix[14][11] = 2;

        // testing primary diagonal

//        matrix[1][2] = 1;
//        matrix[2][3] = 2;
//        matrix[3][4] = 2;
//        matrix[4][5] = 2;
//        matrix[5][6] = 2;
//        matrix[6][7] = 1;

//        matrix[9][9] = 1;
//        matrix[10][10] = 2;
//        matrix[11][11] = 2;
//        matrix[12][12] = 2;
//        matrix[13][13] = 2;
//        matrix[14][14] = 1;


//        matrix[6][2] = 1;
//        matrix[7][3] = 2;
//        matrix[8][4] = 2;
//        matrix[9][5] = 2;
//        matrix[10][6] = 2;

        // testing secondary diagonal

//        matrix[11][9] = 1;
//        matrix[10][10] = 2;
//        matrix[9][11] = 2;
//        matrix[8][12] = 2;
//        matrix[7][13] = 2;
//        matrix[6][14] = 1;
//
//
//        matrix[14][0] = 2;
//        matrix[13][1] = 2;
//        matrix[12][2] = 2;
//        matrix[11][3] = 2;
//        matrix[10][4] = 1;
////
////
//        matrix[5][6] = 2;
//        matrix[4][7] = 2;
//        matrix[3][8] = 2;
//        matrix[2][9] = 2;
//        matrix[6][5] = 1;


        printMatrix(matrix);

        System.out.println("Game evaluated to");

        System.out.println(game.evaluateDiagonal(2, 4, matrix));

//        System.out.println("Game evaluated to");
//        System.out.println(game.evaluateDiagonal(1, 4, matrix));

    }

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%d\t", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
