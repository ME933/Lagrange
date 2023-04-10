package Solve;

import Data.ComData;

import java.util.ArrayList;
import java.util.HashMap;

public class DualProblem {
    double[] subGradients;
    ComData comData;

    public DualProblem(ComData comData){
        this.comData = comData;
    }

    //计算次梯度
    public void computeSubGradients(HashMap<Integer, Double> variable, double[] y, int task){
        subGradients = new double[comData.getStarNum()];
        for (int i = 0; i < comData.getStarNum(); i++) {
            for(int j = 0; j < comData.getStarArc(i).size(); j++){
                int arcIndex = comData.getStarArc(i).get(j);
                subGradients[i] += variable.get(arcIndex);
            }
            subGradients[i] -= y[i] * task;
        }
    }

    public boolean verifySolution(){
        for (Double subGradient : subGradients) {
            if (-subGradient < 0) {
                return false;
            }
        }
        System.out.println("该解符合原问题约束。");
        return true;
    }

    //更新步长
    public double stepSize(Double LB, Double UB){
        int m = subGradients.length;
        double norm2 = 0;
        for (double subGradient : subGradients) {
            norm2 += subGradient * subGradient;
        }
        return 2 * (UB - LB) / norm2; // 返回步长
    }

    //更新lambda
    public double[] updateLambda(double[] lambda, double stepSize){
        for (int i = 0; i < lambda.length; i++) {
            if(subGradients[i] < 0){
                lambda[i] = Math.max(0, lambda[i] - stepSize * subGradients[i]); // 更新拉格朗日乘子（非负）
            }
        }
        return lambda;
    }
}
