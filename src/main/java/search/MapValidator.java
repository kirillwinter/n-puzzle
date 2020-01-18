package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        checkResolve();
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

    private void checkResolve() {

         //  TODO пока что работает только для обычного расположения( не улиткой)

        int inv = 0;
        for (int i=0; i<size * size; ++i)
            if (sequence.get(i) != 0)
                for (int j=0; j<i; ++j)
                    if (sequence.get(j) > sequence.get(i))
                        ++inv;
        for (int i=0; i<size *size; ++i)
            if (sequence.get(i) == 0)
                inv += 1 + i / size;

        if (inv % 2 != 0){
            System.out.println("inv = " + inv);
            throw new PuzzleIsUnsolvableException();
        }

        System.out.println("inv = " + inv);
    }

    int[][] getState() {
        return state;
    }

    public Integer getSize() {
        return size;
    }
}
