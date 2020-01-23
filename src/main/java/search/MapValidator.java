package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MapValidator {
    private List<String> linesList;
    private Integer size;
    private int[][] state;
    private List<Integer> sequence;


     MapValidator() {
        this.linesList = new ArrayList<>();
    }

    void read(String fileName){

        try {
            File file = new File(fileName);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();

            while (line != null) {
                linesList.add(line);
                System.out.println(line);
                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        validate();
    }

        private void validate(){
        delComment();
        setMapSize();
        setMapList();
        checkNumberSequence();
        checkResolve(createGoalNode(state.length, new HashMap<>()));
    }



    private void delComment(){

        List<String> linesList = new ArrayList<String>();

        for (String line: this.linesList) {
            line = line.trim();
            if (!line.startsWith("#")){
                int indexSharp = line.indexOf("#");
                if (indexSharp == -1){
                    linesList.add(line);
                } else {
                    linesList.add(line.substring(0 ,indexSharp));
                }
            }
        }

        this.linesList = linesList;

    }

    private void setMapSize() {
        try {
            size = Integer.parseInt(linesList.get(0));
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
            linesList.remove(0);
    }

    private void setMapList() {

         state = new int[size][size];
         int i = 0;
         for (String line : linesList) {
             state[i] = Arrays.stream(line.split(" +|\t+"))
                     .mapToInt(Integer::parseInt)
                     .toArray();
             i++;
         }
        System.out.println(size);
    }

    private void checkNumberSequence() {

        sequence = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int ii = 0; ii < size; ii++) {

                if (sequence.contains(state[i][ii]) || state[i][ii] > size * size - 1 || state[i][ii] < 0){
                    throw new IllegalArgumentException(String.valueOf(state[i][ii]));
                }
                sequence.add(state[i][ii]);
            }
        }
    }

    private void checkResolve(int[][] goalState) {

        int currStateOffsets = numberOfOffsets(state);
        int goalStateOffsets = numberOfOffsets(goalState);
        System.out.println("state Offsets = " + currStateOffsets);
        System.out.println("goal Offsets = " + goalStateOffsets);

        if (state.length % 2 == 0)
        {
            int[] currZerroPos = getZerroPos(state);
            int[] goalZerroPos = getZerroPos(goalState);
            int size = state.length;
            currStateOffsets += size * size - (currZerroPos[0] + currZerroPos[1] * size);
            currStateOffsets += size * size - (goalZerroPos[0] + goalZerroPos[1] * size);
            throw new PuzzleIsUnsolvableException();
        }
        if (currStateOffsets % 2 != goalStateOffsets % 2)
            throw new PuzzleIsUnsolvableException();
    }

    int[] getZerroPos(int[][] state) {
        int[] zerro = new int[2];
        for (int y = 0; y < state.length; y++) {
            for (int x = 0; x < state.length; x++) {
                if (state[y][x] == 0)
                {
                    zerro[0] = x;
                    zerro[1] = y;
                }
            }
        }
        return zerro;
    }

    int numberOfOffsets(int[][] state)
    {
        int[] sequence = sateToSequence(state);
        int offsets = 0;
        for (int i = 0; i < sequence.length - 1; i++)
        {
            if (sequence[i] != 0)
            {
                for (int j = i + 1; j < sequence.length; j++)
                {
                    if (sequence[j] != 0 && sequence[j] < sequence[i])
                    {
                        offsets++;
                    }
                }
            }
        }
        return offsets;
    }

    int[] sateToSequence(int[][] state)
    {
        int[] sequence = new int[state.length * state.length];
        for (int y = 0; y < state.length; y++)
        {
            for (int x = 0; x < state.length; x++)
            {
                sequence[x + y * state.length] = state[y][x];
            }
        }
        return sequence;
    }

    int[][] getState() {
        return state;
    }

    public Integer getSize() {
        return size;
    }


    int[][] createGoalNode(int size, HashMap<Integer, Coordinate> coordinates){
        int[][] state;
        state = new int[size][];
        for (int i = 0; i < size; i++) {
            state[i] = new int[size];
        }

//        где side - текущая сторона (0 - вверх, 1 - право, 2 - ...)
//        sizeX - размер массива по горизонтали
//        CorrectX - переменная, которая отвечает за автоматическое декриментирование
//        Count - переменная, которая отвечает за текущую цифру внутри массива
//        Summ - произведение ширины на высоту, нужно для устранения ошибки (см. Далее)
//        Mas - название двумерного массива
//        index - собственно позиция внутри массива


        int sizeX = size;
        int sizeY = size;

        int summ = sizeX * sizeY;
        int correctY = 0;
        int correctX = 0;
        int value = 1;
        Coordinate coordinate;
        while( sizeY > 0 )
        {
            for ( int side = 0; side < 4; side++ )
            {
                for (int index = 0; index < (Math.max(sizeX, sizeY)); index++ )
                {

                    if (value == summ)
                        value = 0;

                    if ( side == 0 && index < sizeX - correctX && value <= summ){
                        coordinate = new  Coordinate();
                        state[side + correctY][index + correctX] = value;
                        coordinate.setyPos(side + correctY);
                        coordinate.setxPos(index + correctX);
                        coordinates.put(value, coordinate);
                        value++;

                    }

                    else if ( side == 1 && index < sizeY - correctY && index != 0 && value <= summ ){
                        coordinate = new  Coordinate();
                        state[index + correctY][sizeX - 1] = value;
                        coordinate.setyPos(index + correctY);
                        coordinate.setxPos(sizeX - 1);
                        coordinates.put(value, coordinate);
                        value++;
                    }

                    else if ( side == 2 && index < sizeX - correctX && index != 0 && value <= summ ){
                        coordinate = new  Coordinate();
                        state[sizeY - 1][sizeX - (index + 1)] = value;
                        coordinate.setyPos(sizeY - 1);
                        coordinate.setxPos(sizeX - (index + 1));
                        coordinates.put(value, coordinate);
                        value++;
                    }

                    else if ( side == 3 && index < sizeY - ( correctY + 1 ) && index != 0 && value <= summ ){
                        coordinate = new  Coordinate();
                        state[sizeY - (index + 1)][correctY] = value;
                        coordinate.setyPos(sizeY - (index + 1));
                        coordinate.setxPos(correctY);
                        coordinates.put(value, coordinate);
                        value++;
                    }
                }
            }
            sizeY--;
            sizeX--;
            correctY += 1;
            correctX += 1;
        }
        return state;
    }

}
