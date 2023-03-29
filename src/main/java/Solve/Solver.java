package Solve;

import Data.ComData;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

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
    //任务要求圈次数
    int task;


    int maxIter; // 最大迭代次数
    double epsilon; // 终止条件阈值
    double stepSizeInit; // 初始步长因子


    public Solver(ComData inComData){
        this.comData = inComData;
        arcNum = this.comData.getArcNum();
        starNum = this.comData.getStarNum();
        conNum = this.comData.getConNum();
        task = this.comData.getTask();
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
        } else if (mode.equals("compute")) {
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
            cplex.exportModel("File/model.lp");
            System.out.println("初始化模型成功，存入model.lp文件。");
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
        for (int i = 0; i < conNum; i++) {
            int[] conPair = comData.getConArc().get(i);
            resCon[i] = cplex.addLe(cplex.sum(x[conPair[0]], x[conPair[1]]),1);
            resCon[i].setName("resCon_" + i);
        }
    }

    //添加目标约束
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
        System.out.println("目标函数值：" + cplex.getObjValue());
    }

    public void Lagrange() throws IloException {
//        cplex.solve();
//        System.out.println("原问题目标函数值：" + cplex.getObjValue());
        System.out.println("共有冲突对:"+comData.getConArc().size());
//        System.out.println("跨目标冲突对:"+comData.getCrossNum());
//        int crossNum = comData.getCrossNum();
        // 定义拉格朗日乘子初始值（随机生成）
        double[] lambda = new double[starNum];
        for (int i = 0; i < starNum; i++) {
            lambda[i] = 0;
            //System.out.println("lambda[" + i + "]=" + lambda[i]);
        }
        // 定义当前迭代次数和最优目标值
        int iter = 0; // 当前迭代次数
        double bestLB = 0; // 最优下界
        double bestUB = Double.POSITIVE_INFINITY; // 最优上界

        // 开始循环求解拉格朗日子问题
        SubProblem[] subProblems = new SubProblem[starNum];
        //建立子问题
//        for (int i = 0; i < starNum; i++) {
//            SubProblem subCplex = new SubProblem(i, task, comData.subProblemConPair(i), comData.getStarArc(i));
//            subProblems[i] = subCplex;
//        }
        int ubNonImproveCnt = 0;
        while (iter < maxIter + 1 && bestUB - bestLB > epsilon) {
//        while (iter < maxIter) {
            double LB = 0;
            double UB = 0;
            HashMap<Integer, Double> xMap = new HashMap<>();
            for (int i = 0; i < starNum; i++) {
                //建立子问题松弛目标
//                subProblems[i].creatObj(comData.subProblemConRelax(i),lambda);
                if (subProblems[i].solve()){
                    //获取当前上界（目标函数值）
                    UB += subProblems[i].getObjective();
                    //计算当前下界（原问题目标函数值）
                    LB += subProblems[i].getResult();
//                    System.out.println("y"+i+":"+subProblems[i].getResult());
                    //获取当前解
                    xMap = subProblems[i].getVariable(xMap);
//                    subProblems[i].saveModel();
                }
            }
            UB += Arrays.stream(lambda).sum();
            if (iter % 10 == 0){
                System.out.println("Iteration " + iter + ": LB=" + LB + ", UB=" + UB + ", bestLB=" + bestLB + ", bestUB=" + bestUB);
            }
            //更新拉格朗日乘子（使用次梯度算法）
            DualProblem dualProblem = new DualProblem();
            //计算次梯度
            dualProblem.computeSubGradients(xMap, comData.getConArc(), comData.getCrossCon(), comData.getCrossNum());
            if(dualProblem.verifySolution()){
                if (LB > bestLB) {
                    bestLB = LB; // 更新最优下界
                }
            }else {
                Solve.Solve.Solution solution = new Solve.Solve.Solution();
                //对解进行变换
                HashMap<Integer,Double> transX = solution.transSolution(comData.getConArc(),comData.getEachArcConNum(),xMap);
                int transLB = solution.getObjectiveValue(transX,comData.getMapStarArc(),task);
                if (transLB > bestLB) {
                    bestLB = transLB; // 更新最优下界
                }
            }
            if (UB < bestUB) {
                bestUB = UB; // 更新最优上界
            }
            double stepSize = dualProblem.stepSize(bestLB, UB);
            lambda = dualProblem.updateLambda(lambda,stepSize);
//            System.out.println("lambda平均值："+ Arrays.stream(lambda).average().getAsDouble());
            //找到可行解
            iter++; // 更新迭代次数
        }
        // 输出最终结果
        System.out.println("Final result: bestLB=" + bestLB + ", bestUB=" + bestUB);
        HashMap<Integer, Double> xMap = new HashMap<>();
        Solve.Solve.Solution solution = new Solution();
        for (SubProblem subProblem:subProblems) {
            xMap = subProblem.getVariable(xMap);
        }
        if (solution.verifySolution(comData.getConArc(),xMap)){
            System.out.println("松弛解满足原问题约束。");
        }else {
            System.out.println("松弛解不满足原问题约束。");
            HashMap<Integer,Double> transX = solution.transSolution(comData.getConArc(),comData.getEachArcConNum(),xMap);
            System.out.println(solution.getObjectiveValue(transX,comData.getMapStarArc(),task));
        }

        // 关闭cplex对象
        cplex.end();
    }

    public void initParams(){
        this.maxIter = 100; // 最大迭代次数
        this.epsilon = 0.001 * starNum; // 终止条件阈值
        this.stepSizeInit = -1.0 / Math.sqrt(2 * arcNum); // 初始步长因子
    }

    public void setLagrangeParams(int maxIter,double epsilon){
        this.maxIter = maxIter; // 最大迭代次数
        this.epsilon = epsilon; // 终止条件阈值
    }
}
