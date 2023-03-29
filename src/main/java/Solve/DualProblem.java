package Solve;

import java.util.ArrayList;
import java.util.HashMap;

public class DualProblem {
    Double[] subGradients;

    //计算次梯度
    public void computeSubGradients(HashMap<Integer, Double> variable , ArrayList<int[]> conArc, ArrayList<Integer> crossCon ,int crossNum){
        subGradients = new Double[crossNum];
        for (int i = 0; i < crossNum; i++) {
            int xi = conArc.get(crossCon.get(i))[0];
            int xj = conArc.get(crossCon.get(i))[1];
            subGradients[i] = variable.get(xi) + variable.get(xj) - 1;
        }
    }

    public boolean verifySolution(){
        int max = 2;
        for (Double subGradient : subGradients) {
            if (subGradient > max) {
                max = subGradient.intValue();
                if (max < 1) {
                    System.out.println("该解符合原问题约束。");
                    return true;
                }
            }
        }
        return false;
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
            lambda[i] = Math.max(0, lambda[i] + stepSize * subGradients[i]); // 更新拉格朗日乘子（非负）
        }
        return lambda;
    }
}
