package search;

import java.util.HashMap;

public class GoalNodeCreator {

    public static int[][] createFirstZeroGoalNode(int size, HashMap<Integer, Coordinate> coordinates){
        int[][] state = initState(size);
        int value = 0;

        Coordinate coordinate;

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {

                coordinate = new Coordinate();
                coordinate.setyPos(i);
                coordinate.setxPos(j);
                coordinates.put(value, coordinate);
                state[i][j] = value++;
            }
        }
        return state;
    }



    public static int[][] createLastZeroGoalNode(int size, HashMap<Integer, Coordinate> coordinates){
        int[][] state = initState(size);
        int value = 1;

        Coordinate coordinate;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (value == size * size)
                    value = 0;
                coordinate = new Coordinate();
                coordinate.setyPos(i);
                coordinate.setxPos(j);
                coordinates.put(value, coordinate);
                state[i][j] = value++;
            }
        }
//        state[size - 1][size - 1] = 0;
        return state;
    }


    public static int[][] createSnakeGoalNode(int size, HashMap<Integer, Coordinate> coordinates){

        int[][] state = initState(size);

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

    private static int[][] initState(int size){
        int[][] state;
        state = new int[size][];
        for (int i = 0; i < size; i++) {
            state[i] = new int[size];
        }
        return state;
    }


}
