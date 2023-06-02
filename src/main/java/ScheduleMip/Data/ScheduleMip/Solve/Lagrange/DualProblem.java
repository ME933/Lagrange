package ScheduleMip.Data.ScheduleMip.Solve.Lagrange;//package ScheduleMip.Solve.Lagrange;
//
//import Data.ComData;
//
//import java.util.HashMap;
//
//import static java.lang.Math.pow;
//import static java.lang.Math.sqrt;
//
//public class DualProblem {
//    double[] subGradients;
//    double[] m;
//    double[] v;
//    ComData comData;
//    int[] downTimes;
//    int starSize;
//    final double epsilon = 10E-8;
//
//    public DualProblem(ComData comData){
//        this.starSize = comData.getStarNum();
//        this.downTimes = new int[starSize];
////        Arrays.fill(downTimes, 0);
//        this.comData = comData;
//        subGradients = new double[starSize];
//        m = new double[starSize];
//        v = new double[starSize];
//    }
//
//    //计算次梯度
//    public void computeSubGradients(HashMap<Integer, Double> variable, double[] y, int task){
//        for (int i = 0; i < starSize; i++) {
//            for(int j = 0; j < comData.getStarArc(i).size(); j++){
//                int arcIndex = comData.getStarArc(i).get(j);
//                subGradients[i] += variable.get(arcIndex);
//            }
//            subGradients[i] -= y[i] * task;
//        }
//    }
//
//    public boolean verifySolution(){
//        for (Double subGradient : subGradients) {
//            if (-subGradient < 0) {
//                return false;
//            }
//        }
//        System.out.println("该解符合原问题约束。");
//        return true;
//    }
//
//    //更新步长
//    public double stepSize(Double LB, Double UB){
//        int m = subGradients.length;
//        double norm2 = 0;
//        for (double subGradient : subGradients) {
//            norm2 += subGradient * subGradient;
//        }
////        return 2 * (UB - LB) / norm2; // 返回步长
//        return (UB - LB) / norm2; // 返回步长
//    }
//
//    //更新lambda
//    public double[] updateLambda(double[] lambda, double stepSize){
//        double change;
//        for (int i = 0; i < lambda.length; i++) {
////            if(subGradients[i] < 0){
////                lambda[i] = Math.max(0, lambda[i] - stepSize * subGradients[i]); // 更新拉格朗日乘子（非负）
////                downTimes[i] = 0;
////            }else if(subGradients[i] > 0){
////                lambda[i] = Math.max(0, lambda[i] - stepSize * subGradients[i]); // 更新拉格朗日乘子（非负）
////                downTimes[i] = 0;
////            }else {
////                downTimes[i] ++;
////            }
////            lambda[i] = Math.max(0, lambda[i] - stepSize * subGradients[i]); // 更新拉格朗日乘子（非负）
//            change = m[i] / (sqrt(v[i]) + epsilon);
////            change = subGradients[i];
//            if(change > 0){
//                change = change * 0.1;
//            }
////            change = pow(E, -change) * change;
//            lambda[i] = Math.max(0.000001, lambda[i] - stepSize * change);
//        }
//        return lambda;
//    }
//
//    public void adam(int iteration){
//        double beta1 = 0.9;
//        double beta2 = 0.999;
//        for (int i = 0; i < starSize; i++) {
//            m[i] = beta1 * m[i] + (1 - beta1) * subGradients[i];
//            v[i] = beta2 * v[i] + (1 - beta2) * pow(subGradients[i], 2);
//            m[i] = m[i] / (1 - pow(beta1, iteration));
//            v[i] = v[i] / (1 - pow(beta2, iteration));
//        }
//    }
//}
