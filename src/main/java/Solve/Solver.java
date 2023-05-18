package Solve;

import Tool.DrawChart;
import Data.ComData;
import Tool.TimeTool;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Solver {
    IloCplex cplex;
    ComData comData;

    //决策变量
    IloIntVar[] x;
    IloIntVar[] y;
    //约束
    IloRange[] resYX;
    IloRange[] resCon;

    int arcNum;
    int starNum;
    int conNum;
    int equNum;
    //任务要求圈次数
    int task;


    int maxIter; // 最大迭代次数
    double epsilon; // 终止条件阈值
    double stepSizeInit; // 初始步长因子

    DrawChart drawChart;


    public Solver(ComData inComData){
        this.comData = inComData;
        arcNum = this.comData.getArcNum();
        starNum = this.comData.getStarNum();
        conNum = this.comData.getConNum();
        task = this.comData.getTask();
        equNum = this.comData.getEquNum();
        this.initParams();
    }

    public void executeModel(String modelMode,String solveMode,double MIPGap) throws IloException {
        cplex = new IloCplex();
        this.iniSolver(modelMode);
        if(solveMode.equals("Default")){
            this.defaultSolve(MIPGap);
        } else if (solveMode.equals("Lagrange")) {
            this.Lagrange();
        }
        else if (solveMode.equals("Compare")) {
            this.defaultSolve(MIPGap);
            this.Lagrange();
        }
    }

    private void iniSolver(String mode) throws IloException {
        //从文件中读取模型
        if(mode.equals("load")){
            System.out.println("加载model.lp文件。");
            cplex.importModel("File/model.lp");
        } else if (mode.equals("compute") || mode.equals("save")) {
            cplex.setParam(IloCplex.Param.Preprocessing.Presolve, false);
            //创建决策变量
            x = cplex.intVarArray(arcNum, 0, 1);
            y = cplex.intVarArray(starNum, 0, 1);

            //命名决策变量
            for (int i = 0; i < arcNum; i++) {
                x[i].setName("x_" + i);
            }
            for (int i = 0; i < starNum; i++) {
                y[i].setName("y_" + i);
            }

            //创建约束
            resYX = new IloRange[arcNum];
            resCon = new IloRange[conNum];

            //添加目标
            this.addObj();
            //添加约束
            this.addResCon();
            this.addResStar();
            if(mode.equals("save")){
                cplex.exportModel("File/model.lp");
                System.out.println("初始化模型成功，存入model.lp文件。");
            }
        }else {
            System.out.println("未知model指令。");
        }
    }

    //添加模型目标
    private void addObj() throws IloException {
        int[] yCost = new int[starNum];
        Arrays.fill(yCost, 1);

        //添加模型目标
        cplex.addMaximize(cplex.scalProd(yCost,y));
    }

    //添加冲突约束
    private void addResCon() throws IloException {
        ArrayList<ArrayList<Integer>> conArcSet = comData.getConArcSet();
        for (int i = 0; i < conArcSet.size(); i++){
            ArrayList<Integer> conArc = conArcSet.get(i);
            IloLinearNumExpr lhs = cplex.linearNumExpr();
                for (int arcIndex: conArc) {
                    lhs.addTerm(1, x[arcIndex]);
                }
            resCon[i] = cplex.addLe(lhs,1);
            resCon[i].setName("resCon_" + i);
        }
    }

    //添加卫星约束
    private void addResStar() throws IloException {
        //遍历卫星
        for (int i = 0; i < starNum; i++) {
            //创建线性表达式
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            //int[] starVar = new int[arcNum];
            //遍历弧段
            for (int j = 0; j < arcNum; j++) {
                //假如该弧段属于该卫星，取1，否则0
                if(comData.getArcStar(j) == i){
                    //starVar[j] = 1;
                    lhs.addTerm(1, x[j]);
                }
//                else {
//                    starVar[j] = 0;
//                }
            }
            //∑_(i ∈ it_j) x_i - k_j * y_j >= 0
            resYX[i] = cplex.addGe(cplex.sum(lhs,cplex.prod(-task,y[i])),0);
            resYX[i].setName("resY_" + i);
        }
    }

    public void defaultSolve(double MIPGap) throws IloException {
        if(comData.MIPStartMode.equals("MIPStart")){
            IloIntVar[] startVar = new IloIntVar[arcNum+starNum];
            double[] startVal = new double[arcNum+starNum];
            //加入x的值
            for (int i = 0; i < arcNum; i++) {
                startVar[i] = x[i];
                startVal[i] = comData.getXMIPStartList()[i];
            }
            for (int i = 0; i < starNum; i++) {
                startVar[arcNum+i] = y[i];
                startVal[arcNum+i] = comData.getYMIPStartList()[i];
            }
            cplex.addMIPStart(startVar,startVal);
            cplex.setParam(IloCplex.Param.Emphasis.MIP, 1);
        }
        cplex.setParam (IloCplex.Param.MIP.Tolerances.MIPGap,MIPGap);
        cplex.solve();
        HashMap<Integer, ArrayList<Integer>> virStar = comData.getVirStar();
        int countY1 = 0;
        int countY2 = 0;
        for (int yIndex: virStar.keySet()) {
            ArrayList<Integer> yList = virStar.get(yIndex);
            int count = 0;
            for (int virY: yList) {
//                System.out.println(cplex.getValue(y[virY]));
                int yValue = (int) cplex.getValue(y[virY]);
                count += yValue;
                if(count >= 1){
                    countY1 += 1;
                    break;
                }
            }
        }
        for (int yIndex: virStar.keySet()) {
            ArrayList<Integer> yList = virStar.get(yIndex);
            int count = 0;
            for (int virY: yList) {
//                System.out.println(cplex.getValue(y[virY]));
                int yValue = (int) cplex.getValue(y[virY]);
                count += yValue;
                if(count >= 2){
                    countY2 += 1;
                    break;
                }
            }
        }
        int countX = 0;
        for (int i = 0; i < arcNum; i++){
            int xValue = (int) cplex.getValue(x[i]);
            countX += xValue;
        }
        System.out.println("覆盖卫星数：" + countY1);
        System.out.println("完成卫星数：" + countY2);
        System.out.println("目标函数值：" + cplex.getObjValue());
        System.out.println("选用弧段数：" + countX);
    }

    public void Lagrange() throws IloException {
        drawChart = new DrawChart();
        TimeTool timeTool = new TimeTool();
        timeTool.addName(new String[]{"建立子问题", "求解松弛问题", "更新拉格朗日乘子","修复解", "求解"});
        timeTool.addStartTime("求解");
        // 定义拉格朗日乘子初始值（随机生成）
        double[] lambda = new double[starNum];
        for (int i = 0; i < starNum; i++) {
//            lambda[i] = 0.001;
            lambda[i] = Math.random() * 0.001;
        }
        // 定义当前迭代次数和最优目标值
        int iter = 1; // 当前迭代次数
        double bestLB = 0; // 最优下界
        double bestUB = Double.POSITIVE_INFINITY; // 最优上界

        // 开始循环求解拉格朗日子问题
        SubProblem[] subProblems = new SubProblem[equNum];
        //建立子问题
        timeTool.addStartTime("建立子问题");
        for (int i = 0; i < equNum; i++) {
            SubProblem subCplex = new SubProblem(i, comData);
            subProblems[i] = subCplex;
        }
        timeTool.addEndTime("建立子问题");
        while (iter < maxIter + 1  && bestUB - bestLB > epsilon) {
//        while (iter < maxIter) {
            double LB;
            double UB = 0;
            StarSubProblem starSubProblem = new StarSubProblem(task,lambda);
            HashMap<Integer, Double> xMap = new HashMap<>();
            for (int i = 0; i < equNum; i++) {
                //建立子问题松弛目标
                timeTool.addStartTime("建立子问题");
                subProblems[i].creatObj(lambda);
                timeTool.addEndTime("建立子问题");
                timeTool.addStartTime("求解松弛问题");
                if (subProblems[i].solve()){
                    //获取当前上界（松弛目标函数值）
                    UB += subProblems[i].getObjective();
                    //获取当前解
                    xMap = subProblems[i].getVariable(xMap);
//                    subProblems[i].saveModel();
                }
                timeTool.addEndTime("求解松弛问题");
            }
            //计算下界
            LB = starSubProblem.getObjValue();
            UB += starSubProblem.getRelaxValue();
//            timeTool.addStartTime("更新拉格朗日乘子");
            //更新拉格朗日乘子（使用次梯度算法）
            DualProblem dualProblem = new DualProblem(comData);
            //计算次梯度
            dualProblem.computeSubGradients(xMap, starSubProblem.getRelaxYValue(), task);
//            timeTool.addEndTime("更新拉格朗日乘子");
            double transLB = 0;
            //若满足卫星弧段约束，则判断后更新下界
            timeTool.addStartTime("修复解");
            int arcNum = 0;
            if(dualProblem.verifySolution()){
                if (LB > bestLB) {
                    bestLB = LB; // 更新最优下界
                    transLB = LB;
                }
            }
            //若不满足卫星弧段约束，则进行LocalSearch，得到修复解
            else {
                Solve.Solution solution = new Solve.Solution();
                transLB = solution.getObjectiveValue(xMap,comData.getMapStarArc(),task);
                arcNum = solution.getArcNum(xMap);
//                double[] transY = solution.getObjectiveValueVector(xMap, comData.getMapStarArc(),task);
//                //计算次梯度
//                dualProblem.computeSubGradients(xMap, transY, task);
                if (transLB > bestLB) {
                    bestLB = transLB; // 更新最优下界
                }
            }
            timeTool.addEndTime("修复解");
            drawChart.addData(transLB,UB);
            System.out.println("Iteration " + iter + ": LB=" + LB + ", fixedLB=" + transLB + ", UB=" + UB + ", bestLB=" + bestLB + ", bestUB=" + bestUB + ", selectedArc=" + arcNum);
            if (UB < bestUB) {
                bestUB = UB; // 更新最优上界
            }
            timeTool.addStartTime("更新拉格朗日乘子");
            //更新拉格朗日乘子（使用次梯度算法）
            double stepSize = dualProblem.stepSize(bestLB, UB);
            dualProblem.adam(iter);
            lambda = dualProblem.updateLambda(lambda,stepSize);
            timeTool.addEndTime("更新拉格朗日乘子");
            iter++; // 更新迭代次数
        }
        timeTool.addEndTime("求解");
        // 输出最终结果
        System.out.println("Final result: bestLB=" + bestLB + ", bestUB=" + bestUB);
        System.out.println("求解过程总用时: " + timeTool.getMillisecondTime("求解") + "ms; 平均每代求解用时" + timeTool.getMillisecondTime("求解") / maxIter + "ms.");
        System.out.println();
        timeTool.printAllTime();
        HashMap<Integer, Double> xMap = new HashMap<>();
        Solve.Solution solution = new Solution();
        for (SubProblem subProblem:subProblems) {
            xMap = subProblem.getVariable(xMap);
        }
        if (solution.verifySolution(comData, xMap)){
            System.out.println("松弛解满足原问题约束,目标函数值:"+solution.getObjectiveValue(xMap,comData.getMapStarArc(),task));
            System.out.println("选用弧段数量:"+solution.getArcNum(xMap));
        }else {
            System.out.println("松弛解不满足原问题约束。");
            HashMap<Integer,Double> transX = solution.transSolution(comData, xMap);
            System.out.println(solution.getObjectiveValue(transX,comData.getMapStarArc(),task));
        }
        // 关闭cplex对象
        cplex.end();
        drawChart.draw();
    }

    public DrawChart getDrawChart() {
        return drawChart;
    }

    public void initParams(){
        this.maxIter = 250; // 最大迭代次数
        this.epsilon = 0.0001 * starNum; // 终止条件阈值
        this.stepSizeInit = -1.0 / Math.sqrt(2 * arcNum); // 初始步长因子
    }

    public void setEpsilon(double epsilon){
        this.epsilon = epsilon * starNum; // 终止条件阈值
    }

    public void setMaxIter(int maxIter){
        this.maxIter = maxIter; // 最大迭代次数
    }
}
