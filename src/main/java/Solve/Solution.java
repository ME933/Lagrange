package Solve;

import java.util.ArrayList;
import java.util.HashMap;

public class Solution {

    public boolean verifySolution(ArrayList<int[]> conArc, HashMap<Integer,Double> xMap){
        for (int[] conPair:conArc) {
            int xi = xMap.get(conPair[0]).intValue();
            int xj = xMap.get(conPair[1]).intValue();
            if (xi + xj > 1){
                return false;
            }
        }
        return true;
    }

    public HashMap<Integer,Double> transSolution(ArrayList<int[]> conArc, HashMap<Integer,Integer> eachArcConNum, HashMap<Integer,Double> xMap){
        HashMap<Integer,Double> transformedX = (HashMap<Integer, Double>) xMap.clone();
        for (int[] conPair:conArc) {
            int i = conPair[0];
            int j = conPair[1];
            int xi = transformedX.get(i).intValue();
            int xj = transformedX.get(j).intValue();
            //释放冲突弧段
            if (xi + xj > 1){
                int iCon = eachArcConNum.get(i);
                int jCon = eachArcConNum.get(j);
                if(iCon > jCon){
                    transformedX.put(i,0.0);
                }else {
                    transformedX.put(j,0.0);
                }
            }
        }
        if(verifySolution(conArc,transformedX)){
//            System.out.println("成功变换出可行解。");
            return transformedX;
        }
        else {
            System.out.println("未成功变换出可行解。");
            return null;
        }
    }

    public int getObjectiveValue(HashMap<Integer,Double> xMap, HashMap<Integer,ArrayList<Integer>> mapStarArc, int task){
        int value = 0;
        for (ArrayList<Integer> xList:mapStarArc.values()) {
            int temp = 0;
            for (int xIndex:xList) {
                temp += xMap.get(xIndex);
                if(temp >= task){
                    value++;
                    break;
                }
            }
        }
        return value;
    }
}
