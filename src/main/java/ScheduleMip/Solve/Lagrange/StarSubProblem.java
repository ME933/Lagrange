package ScheduleMip.Solve.Lagrange;//package ScheduleMip.Solve.Lagrange;
//
//public class StarSubProblem {
//    int task;
//    double[] lambda;
//
//    public StarSubProblem(int task, double[] lambda){
//        this.lambda = lambda;
//        this.task = task;
//    }
//
//    //返回原目标函数值
//    public double getObjValue(){
//        double yValue = 0;
//        for (int i = 0; i < lambda.length; i++) {
//            if(1 - task * lambda[i] >= 0){
//                yValue++;
//            }
//        }
//        return yValue;
//    }
//
//    //返回松弛目标函数值
//    public double getRelaxValue(){
//        double yValue = 0;
//        for (int i = 0; i < lambda.length; i++) {
//            if(1 -  task * lambda[i] >= 0){
//                yValue += 1 - lambda[i] * task;
//            }
//        }
//        return yValue;
//    }
//
//    //返回松弛解
//    public double[] getYValue(){
//        double[] y = new double[lambda.length];
//        for (int i = 0; i < lambda.length; i++) {
//            if(1 - task * lambda[i] >= 0){
//                y[i] = 1;
//            }else {
//                y[i] = 0;
//            }
//        }
//        return y;
//    }
//
//    public double[] getRelaxYValue(){
//        double[] y = new double[lambda.length];
//        for (int i = 0; i < lambda.length; i++) {
//            if(1 - task * lambda[i] >= 0){
//                y[i] = 1- task * lambda[i];
//            }else {
//                y[i] = 0;
//            }
//        }
//        return y;
//    }
//}
