package ru.shanalotte.objects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class ObjectArrayHolder {

    private int[] nums = {1,2,3};
    private Integer[] oNums = {1,2,3};
    private int[][] matrix = {{1,2,3}, {4,5,6}};


  @Override
  public String toString() {
    return "ObjectArrayHolder{" +
            "nums=" + Arrays.toString(nums) +
            ", oNums=" + Arrays.toString(oNums) +
            ", matrix=" + Arrays.deepToString(matrix) +
            '}';
  }
}
