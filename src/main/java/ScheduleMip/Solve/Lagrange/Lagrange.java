package ScheduleMip.Solve.Lagrange;//package ScheduleMip.Solve.Lagrange;
//import ScheduleMip.Data.*;
//import ScheduleMip.Tool.DrawChart;
//import ScheduleMip.Tool.TimeTool;
//import ilog.concert.IloException;
//
//import java.util.HashMap;
//
//public class Lagrange {
//    DrawChart drawChart;
//
//    int arcNum;
//    int starNum;
//    int equNum;
//
//    int equConNum;
//    int starConNum;
//    int task;
//
//    EntityData entityData;
//    HashData hashData;
//    ConflictData conflictData;
//    ResultData resultData;
//
//    int maxIter; // 最大迭代次数
//    double epsilon; // 终止条件阈值
//    double stepSizeInit; // 初始步长因子
//
//    public Lagrange(EntityData entityData, ConflictData conflictData, HashData hashData){
//        this.entityData = entityData;
//        this.hashData = hashData;
//        this.conflictData = conflictData;
//        this.arcNum = entityData.getArcNum();
//        this.starNum = entityData.getStarNum();
//        this.equNum = entityData.getEquNum();
//        this.equConNum = conflictData.getEquConfNum();
//        this.starConNum = conflictData.getStarConfNum();
//        this.task = entityData.getCycleCount();
//    }
//
//    public void Lagrange() throws IloException {
//        drawChart = new DrawChart();
//        TimeTool timeTool = new TimeTool();
//        timeTool.addName(new String[]{"建立子问题", "求解松弛问题", "更新拉格朗日乘子","修复解", "求解"});
//        timeTool.addStartTime("求解");
//        // 定义拉格朗日乘子初始值（随机生成）
//        double[] lambda = new double[starNum];
//        for (int i = 0; i < starNum; i++) {
////            lambda[i] = 0.001;
//            lambda[i] = Math.random() * 0.001;
//        }
//        // 定义当前迭代次数和最优目标值
//        int iter = 1; // 当前迭代次数
//        double bestLB = 0; // 最优下界
//        double bestUB = Double.POSITIVE_INFINITY; // 最优上界
//
//        // 开始循环求解拉格朗日子问题
//        SubProblem[] subProblems = new SubProblem[equNum];
//        //建立子问题
//        timeTool.addStartTime("建立子问题");
//        for (int i = 0; i < equNum; i++) {
//            SubProblem subCplex = new SubProblem(i, comData);
//            subProblems[i] = subCplex;
//        }
//        timeTool.addEndTime("建立子问题");
//        while (iter < maxIter + 1  && bestUB - bestLB > epsilon) {
////        while (iter < maxIter) {
//            double LB;
//            double UB = 0;
//            StarSubProblem starSubProblem = new StarSubProblem(task,lambda);
//            HashMap<Integer, Double> xMap = new HashMap<>();
//            for (int i = 0; i < equNum; i++) {
//                //建立子问题松弛目标
//                timeTool.addStartTime("建立子问题");
//                subProblems[i].creatObj(lambda);
//                timeTool.addEndTime("建立子问题");
//                timeTool.addStartTime("求解松弛问题");
//                if (subProblems[i].solve()){
//                    //获取当前上界（松弛目标函数值）
//                    UB += subProblems[i].getObjective();
//                    //获取当前解
//                    xMap = subProblems[i].getVariable(xMap);
////                    subProblems[i].saveModel();
//                }
//                timeTool.addEndTime("求解松弛问题");
//            }
//            //计算下界
//            LB = starSubProblem.getObjValue();
//            UB += starSubProblem.getRelaxValue();
////            timeTool.addStartTime("更新拉格朗日乘子");
//            //更新拉格朗日乘子（使用次梯度算法）
//            DualProblem dualProblem = new DualProblem(comData);
//            //计算次梯度
//            dualProblem.computeSubGradients(xMap, starSubProblem.getRelaxYValue(), task);
////            timeTool.addEndTime("更新拉格朗日乘子");
//            double transLB = 0;
//            //若满足卫星弧段约束，则判断后更新下界
//            timeTool.addStartTime("修复解");
//            int arcNum = 0;
//            if(dualProblem.verifySolution()){
//                if (LB > bestLB) {
//                    bestLB = LB; // 更新最优下界
//                    transLB = LB;
//                }
//            }
//            //若不满足卫星弧段约束，则进行LocalSearch，得到修复解
//            else {
//                Solution solution = new Solution(entityData, hashData, conflictData);
//                transLB = solution.getObjectiveValue(xMap,comData.getMapStarArc(),task);
//                arcNum = solution.getArcNum(xMap);
////                double[] transY = solution.getObjectiveValueVector(xMap, comData.getMapStarArc(),task);
////                //计算次梯度
////                dualProblem.computeSubGradients(xMap, transY, task);
//                if (transLB > bestLB) {
//                    bestLB = transLB; // 更新最优下界
//                }
//            }
//            timeTool.addEndTime("修复解");
//            drawChart.addData(transLB,UB);
//            System.out.println("Iteration " + iter + ": LB=" + LB + ", fixedLB=" + transLB + ", UB=" + UB + ", bestLB=" + bestLB + ", bestUB=" + bestUB + ", selectedArc=" + arcNum);
//            if (UB < bestUB) {
//                bestUB = UB; // 更新最优上界
//            }
//            timeTool.addStartTime("更新拉格朗日乘子");
//            //更新拉格朗日乘子（使用次梯度算法）
//            double stepSize = dualProblem.stepSize(bestLB, UB);
//            dualProblem.adam(iter);
//            lambda = dualProblem.updateLambda(lambda,stepSize);
//            timeTool.addEndTime("更新拉格朗日乘子");
//            iter++; // 更新迭代次数
//        }
//        timeTool.addEndTime("求解");
//        // 输出最终结果
//        System.out.println("Final result: bestLB=" + bestLB + ", bestUB=" + bestUB);
//        System.out.println("求解过程总用时: " + timeTool.getMillisecondTime("求解") + "ms; 平均每代求解用时" + timeTool.getMillisecondTime("求解") / maxIter + "ms.");
//        System.out.println();
//        timeTool.printAllTime();
//        HashMap<Integer, Double> xMap = new HashMap<>();
//        Solution solution = new Solution(entityData, hashData, conflictData);
//        for (SubProblem subProblem:subProblems) {
//            xMap = subProblem.getVariable(xMap);
//        }
//        if (solution.verifySolution(xMap)){
//            System.out.println("松弛解满足原问题约束,目标函数值:"+solution.getObjectiveValue(xMap,comData.getMapStarArc(),task));
//            System.out.println("选用弧段数量:"+solution.getArcNum(xMap));
//        }else {
//            System.out.println("松弛解不满足原问题约束。");
//            HashMap<Integer,Double> transX = solution.transSolution(comData, xMap);
//            System.out.println(solution.getObjectiveValue(transX,comData.getMapStarArc(),task));
//        }
//        // 关闭cplex对象
//        drawChart.draw();
//    }
//
//    public DrawChart getDrawChart() {
//        return drawChart;
//    }
//
//    public void initParams(){
//        this.maxIter = 250; // 最大迭代次数
//        this.epsilon = 0.0001 * starNum; // 终止条件阈值
//        this.stepSizeInit = -1.0 / Math.sqrt(2 * arcNum); // 初始步长因子
//    }
//
//    public void setEpsilon(double epsilon){
//        this.epsilon = epsilon * starNum; // 终止条件阈值
//    }
//
//    public void setMaxIter(int maxIter){
//        this.maxIter = maxIter; // 最大迭代次数
//    }
//}
