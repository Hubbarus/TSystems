package com.tsystems.javaschool.tasks.pyramid;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        try {
            Collections.sort(inputNumbers);
        } catch (OutOfMemoryError e) {
            throw new CannotBuildPyramidException();
        }  catch (NullPointerException e) {
            throw new CannotBuildPyramidException();
        }

        boolean isPossible;
        int[][] result;

        int count = 0;
        int rows = 1;
        int cols = 1;

        while(count < inputNumbers.size()){
            count = count + rows;
            rows++;
            cols += 2;
        }
        rows = rows - 1;
        cols = cols - 2;

        isPossible = count == inputNumbers.size();

        if (isPossible) {
            result = new int[rows][cols];

            for (int[] row : result) {
                Arrays.fill(row, 0);
            }

            int center = (cols / 2);
            count = 1;
            int arrIdx = 0;

            for (int i = 0, offset = 0; i < rows; i++, offset++, count++) {
                int start = center - offset;
                for (int j = 0; j < count * 2; j +=2, arrIdx++) {
                    result[i][start + j] = inputNumbers.get(arrIdx);
                }
            }
        } else throw new CannotBuildPyramidException();

        return result;
    }


}
