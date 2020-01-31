package search;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Getter
public class MapValidator {
    private List<String> linesList;
    private Integer size;
    private int[][] state;
    private List<Integer> sequence;
    private int zeroXInitState;
    private int zeroYInitState;


     public MapValidator() {
        this.linesList = new ArrayList<>();
    }

    public void read(String fileName){

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
            System.err.println("Reading file error, filename: " + fileName);
            System.exit(1);
        }
        validate();
    }

    private void validate(){
        delComment();
        setMapSize();
        setMapList();
        checkNumberSequence();
        setZeroPos();
    }

    // TODO доработать методы check resolve
    // TODO привести к единообразию координаты нуля
    private void setZeroPos() {
         int[] pos = getZeroPos(this.state);
         zeroXInitState = pos[0];
         zeroYInitState = pos[1];
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
            System.err.println("ERROR: Invalid value size: " + linesList.get(0));
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
            for (int j = 0; j < size; j++) {
                if (sequence.contains(state[i][j]) || state[i][j] > size * size - 1 || state[i][j] < 0){
                    throw new IllegalArgumentException(String.valueOf(state[i][j]));
                }
                sequence.add(state[i][j]);
            }
        }
    }



    // ------------------------


//    public void checkResolve(int[][] goalState) {
//
//         int invGoal = calculateInv(goalState);
//         int invCur = calculateInv(this.state);
//
//        System.out.println("invGoal = " + invGoal);
//        System.out.println("invCur = " + invCur);
//
////        if (inv % 2 != 0){
////            System.out.println("inv = " + inv);
////            throw new PuzzleIsUnsolvableException();
////        }
////
////        System.out.println("inv = " + inv);
//    }
//
//    private int calculateInv(int[][] state){
//
//        List<Integer> sequence = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (sequence.contains(state[i][j]) || state[i][j] > size * size - 1 || state[i][j] < 0){
//                    throw new IllegalArgumentException(String.valueOf(state[i][j]));
//                }
//                sequence.add(state[i][j]);
//            }
//        }
//
//        int inv = 0;
//        int size = state.length;
//        for (int i=0; i<size * size; ++i)
//            if (sequence.get(i) != 0)
//                for (int j=0; j<i; ++j)
//                    if (sequence.get(j) > sequence.get(i))
//                        ++inv;
//        for (int i=0; i<size *size; ++i) {
//            if (sequence.get(i) == 0)
//                inv += 1 + i / size;
//        }
//
//        return inv;
//
//    }

// -----------------------------

    public void checkResolve(int[][] goalState) {

        int currStateOffsets = numberOfOffsets(state);
        int goalStateOffsets = numberOfOffsets(goalState);
        System.out.println("state Offsets = " + currStateOffsets);
        System.out.println("goal Offsets = " + goalStateOffsets);

        if (state.length % 2 == 0)
        {
            int[] currZeroPos = getZeroPos(state);
            int[] goalZeroPos = getZeroPos(goalState);
            int stateSize = state.length;
            currStateOffsets += stateSize * stateSize - (currZeroPos[0] + currZeroPos[1] * stateSize);
            goalStateOffsets += stateSize * stateSize - (goalZeroPos[0] + goalZeroPos[1] * stateSize);
        }
        if (currStateOffsets % 2 != goalStateOffsets % 2)
            throw new PuzzleIsUnsolvableException();
    }

    int[] getZeroPos(int[][] state) {
        int[] zero = new int[2];
        for (int y = 0; y < state.length; y++) {
            for (int x = 0; x < state.length; x++) {
                if (state[y][x] == 0)
                {
                    zero[0] = x;
                    zero[1] = y;
                }
            }
        }
        return zero;
    }

    int numberOfOffsets(int[][] state)
    {
        int[] sequence = stateToSequence(state);
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

    int[] stateToSequence(int[][] state)
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





}
