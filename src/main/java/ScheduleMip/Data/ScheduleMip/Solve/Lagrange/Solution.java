package ScheduleMip.Data.ScheduleMip.Solve.Lagrange;//package ScheduleMip.Solve.Lagrange;
//
//import ScheduleMip.Data.ConflictData;
//import ScheduleMip.Data.EntityData;
//import ScheduleMip.Data.HashData;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.TreeMap;
//
//public class Solution {
//    EntityData entityData;
//    HashData hashData;
//    ConflictData conflictData;
//
//    public Solution(EntityData entityData, HashData hashData, ConflictData conflictData){
//        this.entityData = entityData;
//        this.hashData = hashData;
//        this.conflictData = conflictData;
//    }
//
//    /**
//     * @description: 验证解是否满足装备能力约束
//     * @return boolean
//     * @date: 2023/5/31 22:17
//     */
//    public boolean verifySolution(HashMap<Integer,Double> xMap) {
//        for (int equIndex = 0; equIndex < entityData.getEquNum(); equIndex++) {
//            ArrayList<ArrayList<Integer>> conSetList = conflictData.getConSetByEqu(equIndex);
//            int cap = entityData.getEquCap(equIndex);
//            for (ArrayList<Integer> conSet: conSetList) {
//                int selectedArc = 0;
//                //查找该装备能力
//                for (int arc : conSet) {
//                    selectedArc += xMap.get(arc).intValue();
//                    if (selectedArc > cap) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    public int getArcNum(HashMap<Integer,Double> xMap){
//        int res = 0;
//        for (Double x: xMap.values()) {
//            res += x.intValue();
//        }
//        return res;
//    }
//
//    public HashMap<Integer,Double> transSolution(HashMap<Integer,Double> xMap){
//        HashMap<Integer, Double> transformedX = new HashMap<>(xMap);
//        for (ArrayList<ArrayList<Integer>> conSetList : comData.getConArcSetByEqu().values()) {
//            for (ArrayList<Integer> conSet : conSetList) {
//                //计算选用弧段数，同时维护选用弧段及其对应冲突数的treeMap
//                int selectedArc = 0;
//                TreeMap<Integer, ArrayList<Integer>> conNumTree = new TreeMap<>();
//                for (int arc : conSet) {
//                    selectedArc += transformedX.get(arc).intValue();
//                    int conNum = comData.getConflictArcNum(arc);
//                    //维护选用弧段及其对应冲突数的treeMap
//                    if (!conNumTree.containsKey(conNum)) {
//                        ArrayList<Integer> arcList = new ArrayList<>();
//                        arcList.add(arc);
//                        conNumTree.put(conNum, arcList);
//                    } else {
//                        conNumTree.get(conNum).add(arc);
//                    }
//                }
//                //释放冲突数较少的冲突弧段
//                while (selectedArc > comData.getTask()) {
//                    //获取最大冲突数
//                    int maxConNum = conNumTree.lastKey();
//                    //获取最大冲突数对应弧段并更新treeMap
//                    int arcIndex = conNumTree.get(maxConNum).get(0);
//                    conNumTree.get(maxConNum).remove(arcIndex);
//                    //释放该弧段
//                    transformedX.put(arcIndex, 0.0);
//                }
//            }
//        }
//        if(verifySolution(comData,transformedX)){
////            System.out.println("成功变换出可行解。");
//            return transformedX;
//        }
//        else {
//            System.out.println("未成功变换出可行解。");
//            return null;
//        }
//    }
//
//    public int getObjectiveValue(HashMap<Integer,Double> xMap, HashMap<Integer,ArrayList<Integer>> mapStarArc, int task){
//        int value = 0;
//        for (ArrayList<Integer> xList:mapStarArc.values()) {
//            int temp = 0;
//            for (int xIndex:xList) {
//                temp += xMap.get(xIndex);
//                if(temp >= task){
//                    value++;
//                    break;
//                }
//            }
//        }
//        return value;
//    }
//
//    public double[] getObjectiveValueVector(HashMap<Integer,Double> xMap, HashMap<Integer,ArrayList<Integer>> mapStarArc, int task){
//        double[] y = new double[mapStarArc.size()];
//        for (int i = 0; i < mapStarArc.size(); i++) {
//            y[i] = 0;
//        }
//        for (int i = 0; i < mapStarArc.size(); i++) {
//            ArrayList<Integer> xList = mapStarArc.get(i);
//            int temp = 0;
//            for (int xIndex:xList) {
//                temp += xMap.get(xIndex);
//                if(temp >= task){
//                    y[i] = 1;
//                    break;
//                }
//            }
//        }
//        return y;
//    }
//}
