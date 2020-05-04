package com.alex.game;

import java.util.*;

public class Zobrist {

    private RandomNumber randomNumber;
    private Map<Integer, PositionInformation> resultMap;


    public Zobrist(){
        randomNumber = new RandomNumber(15);
        resultMap = new HashMap<>();
    }



    public void addEntry(int[][] matrix, PositionInformation positionInformation){

        int result = calculateResult(matrix);

        resultMap.put(result, positionInformation);
    }

    public Optional<PositionInformation> getEntry(int[][] matrix){
        int result = calculateResult(matrix);

        return Optional.ofNullable(resultMap.get(result));
    }

    private int calculateResult(int[][] matrix){
        List<Integer> allRandomNumbers = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int element = matrix[i][j];

                if(element == 1){
                    int rand = randomNumber.getAi(i, j);

                    allRandomNumbers.add(rand);
                }

                if(element == 2){
                    int rand = randomNumber.getHuman(i, j);

                    allRandomNumbers.add(rand);
                }
            }
        }

        int result = allRandomNumbers.get(0);

        for (int i = 1; i < allRandomNumbers.size(); i++) {
            result = result ^ allRandomNumbers.get(i);
        }

        return result;
    }



}
