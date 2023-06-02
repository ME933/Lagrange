package ScheduleMip.Solve;

import ScheduleMip.Data.ConflictData;
import ScheduleMip.Data.EntityData;
import ScheduleMip.Data.HashData;
import ScheduleMip.Data.ResultData;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;

public class Model {
    IloCplex cplex;
    //决策变量
    IloIntVar[] x;
    IloIntVar[] y;
    //约束
    IloRange[] resYX;
    IloRange[] resCon;

    int arcNum;
    int starNum;
    int equNum;

    int equConNum;
    int starConNum;
    int task;

    EntityData entityData;
    HashData hashData;
    ConflictData conflictData;
    ResultData resultData;

    public Model(EntityData entityData, ConflictData conflictData, HashData hashData){
        this.entityData = entityData;
        this.hashData = hashData;
        this.conflictData = conflictData;
        this.arcNum = entityData.getArcNum();
        this.starNum = entityData.getStarNum();
        this.equNum = entityData.getEquNum();
        this.equConNum = conflictData.getEquConfNum();
        this.starConNum = conflictData.getStarConfNum();
        this.task = entityData.getCycleCount();
    }

    public void iniSolver(String mode) throws IloException {
        //从文件中读取模型
        if (mode.equals("load")) {
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
            resCon = new IloRange[equConNum];

            //添加目标
            this.addObj();
            //添加约束
            this.addResCon();
            this.addResStar();
            if (mode.equals("save")) {
                cplex.exportModel("model.lp");
                System.out.println("初始化模型成功，存入model.lp文件。");
            }
        } else {
            System.out.println("未知model指令。");
        }
    }

    //添加模型目标
    private void addObj() throws IloException {
        int[] yCost = new int[starNum];
        Arrays.fill(yCost, 1);

        //添加模型目标
        cplex.addMaximize(cplex.scalProd(yCost, y));
    }

    //添加冲突约束
    private void addResCon() throws IloException {
        ArrayList<ArrayList<Integer>> conArcSet = conflictData.getEquConf();
        for (int conIndex = 0; conIndex < conArcSet.size(); conIndex++) {
            ArrayList<Integer> conArc = conArcSet.get(conIndex);
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            for (int arcIndex : conArc) {
                lhs.addTerm(1, x[arcIndex]);
            }
            int equIndex = hashData.getEquByArc(conArc.get(0));
            int cap = entityData.getEquCap(equIndex);
            resCon[conIndex] = cplex.addLe(lhs, cap);
            resCon[conIndex].setName("resCon_" + conIndex);
        }
    }

    //添加卫星约束
    private void addResStar() throws IloException {
        //遍历卫星
        for (int starIndex = 0; starIndex < starNum; starIndex++) {
            //创建线性表达式
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            //遍历弧段
            ArrayList<Integer> arcList = hashData.getArcsByStar(starIndex);
            for (int arcIndex : arcList) {
                lhs.addTerm(1, x[arcIndex]);
            }
            //∑_(i ∈ it_j) x_i - k_j * y_j >= 0
            resYX[starIndex] = cplex.addGe(cplex.sum(lhs, cplex.prod(-task, y[starIndex])), 0);
            resYX[starIndex].setName("resY_" + starIndex);
        }
    }


    public ResultData getResult() throws IloException {
        int countComplete = 0;
        for (int starIndex = 0; starIndex < starNum; starIndex++) {
            int yValue = (int) cplex.getValue(y[starIndex]);
            countComplete += yValue;
        }
        int countCover = 0;
        for (int starIndex = 0; starIndex < starNum; starIndex++) {
            ArrayList<Integer> arcList = hashData.getArcsByStar(starIndex);
            for (int arcIndex : arcList) {
                int xValue = (int) cplex.getValue(x[arcIndex]);
                if (xValue == 1) {
                    countCover += 1;
                    break;
                }
            }
        }
        int countArc = 0;
        for (int arcIndex = 0; arcIndex < arcNum; arcIndex++) {
            int xValue = (int) cplex.getValue(x[arcIndex]);
            if (xValue == 1) {
                countArc += xValue;
                resultData.addArcResult(entityData.getArcID(arcIndex));
            }
        }
        resultData.setCountComplete(countComplete);
        resultData.setCountCover(countCover);
        resultData.setCountArc(countArc);
        return resultData;
    }

    public void mipSolve(double MIPGap) throws IloException {
        cplex.setParam(IloCplex.Param.MIP.Tolerances.MIPGap, MIPGap);
        cplex.solve();
    }
}
