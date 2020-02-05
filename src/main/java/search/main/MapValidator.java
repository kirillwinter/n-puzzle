package search.main;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Slf4j
public class MapValidator {
    private List<String> linesList;
    private Integer size;
    private int[][] state;
    private List<Integer> numberSequence;
    private int zeroXInitState;
    private int zeroYInitState;


    public MapValidator() {
        this.linesList = new ArrayList<>();
    }

    public void read(String fileName) {
        File file = new File(fileName);
        try (FileReader fr = new FileReader(file)) {
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            while (line != null) {
                linesList.add(line);
                log.debug(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            log.error("Reading file error, filename: " + fileName);
            System.exit(1);
        }
        validate();
    }

    private void validate() {
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


    private void delComment() {

        List<String> linesListClear = new ArrayList<>();

        for (String line : this.linesList) {
            line = line.trim();
            if (!line.startsWith("#")) {
                int indexSharp = line.indexOf('#');
                if (indexSharp == -1) {
                    linesListClear.add(line);
                } else {
                    linesListClear.add(line.substring(0, indexSharp));
                }
            }
        }
        this.linesList = linesListClear;
    }

    private void setMapSize() {
        try {
            size = Integer.parseInt(linesList.get(0));
        } catch (Exception e) {
            log.error("ERROR: Invalid value size: " + linesList.get(0));
            System.exit(1);
        }
        linesList.remove(0);
    }

    private void setMapList() {

        state = new int[size][size];
        int i = 0;
        try {
            for (String line : linesList) {
                state[i] = Arrays.stream(line.split(" +|\t+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                i++;
            }
        } catch (Exception e) {
            log.error("ERROR:  Incorrect format ");
            System.exit(1);
        }

    }

    private void checkNumberSequence() {

        numberSequence = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (numberSequence.contains(state[i][j]) || state[i][j] > size * size - 1 || state[i][j] < 0) {
                    log.error("ERROR:  Illegal argument " + state[i][j]);
                    System.exit(1);
                }
                numberSequence.add(state[i][j]);
            }
        }
    }


    public void checkResolve(int[][] goalState) {

        int currStateOffsets = numberOfOffsets(state);
        int goalStateOffsets = numberOfOffsets(goalState);

        if (state.length % 2 == 0) {
            int[] currZeroPos = getZeroPos(state);
            int[] goalZeroPos = getZeroPos(goalState);
            int stateSize = state.length;
            currStateOffsets += stateSize * stateSize - (currZeroPos[0] + currZeroPos[1] * stateSize);
            goalStateOffsets += stateSize * stateSize - (goalZeroPos[0] + goalZeroPos[1] * stateSize);
        }
        log.debug("state Offsets = " + currStateOffsets);
        log.debug("goal Offsets = " + goalStateOffsets);
        if (currStateOffsets % 2 != goalStateOffsets % 2) {
            log.error("ERROR: Puzzle is Unsolvable");
            System.exit(1);
        }
    }

    int[] getZeroPos(int[][] state) {
        int[] zero = new int[2];
        for (int y = 0; y < state.length; y++) {
            for (int x = 0; x < state.length; x++) {
                if (state[y][x] == 0) {
                    zero[0] = x;
                    zero[1] = y;
                }
            }
        }
        return zero;
    }

    int numberOfOffsets(int[][] state) {
        int[] sequence = stateToSequence(state);
        int offsets = 0;
        for (int i = 0; i < sequence.length - 1; i++) {
            if (sequence[i] != 0) {
                for (int j = i + 1; j < sequence.length; j++) {
                    if (sequence[j] != 0 && sequence[j] < sequence[i]) {
                        offsets++;
                    }
                }
            }
        }
        return offsets;
    }

    int[] stateToSequence(int[][] state) {
        int[] sequence = new int[state.length * state.length];
        for (int y = 0; y < state.length; y++) {
            System.arraycopy(state[y], 0, sequence, y * state.length, state.length);
        }
        return sequence;
    }
}
