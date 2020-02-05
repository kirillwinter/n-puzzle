package search.node;

import java.util.HashMap;

public final class GoalNodeCreator {



    public static Node createFirstZeroGoalNode(int size, HashMap<Integer, Coordinate> coordinates) {
        int[][] state = initState(size);
        int value = 0;
        int zeroX = Integer.MAX_VALUE;
        int zeroY = Integer.MAX_VALUE;

        Coordinate coordinate;

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {

                if (value == 0) {
                    zeroX = j;
                    zeroY = i;
                }

                coordinate = new Coordinate();
                coordinate.setYPos(i);
                coordinate.setXPos(j);
                coordinates.put(value, coordinate);
                state[i][j] = value++;
            }
        }
        return new Node(null, state, zeroX, zeroY);
    }


    public static Node createLastZeroGoalNode(int size, HashMap<Integer, Coordinate> coordinates) {
        int[][] state = initState(size);
        int zeroX = Integer.MAX_VALUE;
        int zeroY = Integer.MAX_VALUE;
        int value = 1;

        Coordinate coordinate;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (value == size * size) {
                    value = 0;
                    zeroX = j;
                    zeroY = i;
                }
                coordinate = new Coordinate();
                coordinate.setYPos(i);
                coordinate.setXPos(j);
                coordinates.put(value, coordinate);
                state[i][j] = value++;
            }
        }
        return new Node(null, state, zeroX, zeroY);
    }


    public static Node createSnakeGoalNode(int size, HashMap<Integer, Coordinate> coordinates) {

        int[][] state = initState(size);

//        где side - текущая сторона (0 - вверх, 1 - право, 2 - ...)
//        sizeX - размер массива по горизонтали
//        CorrectX - переменная, которая отвечает за автоматическое декриментирование
//        Count - переменная, которая отвечает за текущую цифру внутри массива
//        Summ - произведение ширины на высоту, нужно для устранения ошибки (см. Далее)
//        Mas - название двумерного массива
//        index - собственно позиция внутри массива

        int zeroX;
        int zeroY;

        int sizeX = size;
        int sizeY = size;

        int summ = sizeX * sizeY;
        int correctY = 0;
        int correctX = 0;
        int value = 1;
        Coordinate coordinate;
        while (sizeY > 0) {
            for (int side = 0; side < 4; side++) {
                for (int index = 0; index < (Math.max(sizeX, sizeY)); index++) {

                    if (value == summ)
                        value = 0;

                    if (side == 0 && index < sizeX - correctX && value <= summ) {
                        coordinate = new Coordinate();
                        state[side + correctY][index + correctX] = value;
                        coordinate.setYPos(side + correctY);
                        coordinate.setXPos(index + correctX);
                        coordinates.put(value, coordinate);
                        value++;
                    } else if (side == 1 && index < sizeY - correctY && index != 0 && value <= summ) {
                        coordinate = new Coordinate();
                        state[index + correctY][sizeX - 1] = value;
                        coordinate.setYPos(index + correctY);
                        coordinate.setXPos(sizeX - 1);
                        coordinates.put(value, coordinate);
                        value++;
                    } else if (side == 2 && index < sizeX - correctX && index != 0 && value <= summ) {
                        coordinate = new Coordinate();
                        state[sizeY - 1][sizeX - (index + 1)] = value;
                        coordinate.setYPos(sizeY - 1);
                        coordinate.setXPos(sizeX - (index + 1));
                        coordinates.put(value, coordinate);
                        value++;
                    } else if (side == 3 && index < sizeY - (correctY + 1) && index != 0 && value <= summ) {
                        coordinate = new Coordinate();
                        state[sizeY - (index + 1)][correctY] = value;
                        coordinate.setYPos(sizeY - (index + 1));
                        coordinate.setXPos(correctY);
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
        zeroX = coordinates.get(0).getXPos();
        zeroY = coordinates.get(0).getYPos();
        return new Node(null, state, zeroX, zeroY);
    }

    private static int[][] initState(int size) {
        int[][] state;
        state = new int[size][];
        for (int i = 0; i < size; i++) {
            state[i] = new int[size];
        }
        return state;
    }


}
