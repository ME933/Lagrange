package Solve;

import Data.ComData;
import TimeLine.TimeLine;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SubProblem {
    IloCplex cplex;
    //决策变量
    IloIntVar[] x;
    //存储全局弧段和本地弧段的对应关系
    HashMap<Integer, Integer> arcMap;
    //存储相关arc
    ArrayList<Integer> arc;
    ComData comData;
    int equIndex;

    public SubProblem(int equIndex, ComData comData) throws IloException {
        int arcNum = comData.getEquArcNum(equIndex);
        this.equIndex = equIndex;
        this.comData = comData;
        this.arc = comData.getEquArcList(equIndex);
//        ArrayList<Integer> conList = comData.getEquConList(equIndex);
        cplex = new IloCplex();
        cplex.setOut(null);
        this.creatArcMap();
        //初始化时，建立模型约束，添加对应装备冲突约束
        x = cplex.intVarArray(arcNum, 0, 1);
        int conSize = comData.getConSetByEqu(equIndex).size();
        IloRange[] resCon = new IloRange[conSize];
        ArrayList<ArrayList<Integer>> conSet = comData.getConSetByEqu(equIndex);
        //命名决策变量
        for (int i = 0; i < arcNum; i++) {
            x[i].setName("x_" + getGlobalIndex(i));
        }
        //建立冲突约束
        for (int i = 0; i < conSize; i++) {
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            ArrayList<Integer> conList = conSet.get(i);
            for (int arcIndex:conList) {
                lhs.addTerm(1, x[getLocalIndex(arcIndex)]);
            }
            resCon[i] = cplex.addLe(lhs,1);
            resCon[i].setName("c_" + i);
        }
    }

    public void creatObj(double[] lambda) throws IloException {
        //将任务约束加入目标函数
        cplex.remove(cplex.getObjective());
        IloLinearNumExpr lhs = cplex.linearNumExpr();
        for (int arc:comData.getEquArcList(equIndex)) {
            lhs.addTerm(lambda[comData.getArcStar(arc)],x[getLocalIndex(arc)]);
        }
        cplex.addMaximize(lhs);
    }

    public boolean solve() throws IloException {
        return cplex.solve();
    }

    //返回结果，返回变量名-变量值的MAP，如x_1-0
    public HashMap<Integer, Double> getVariable(HashMap<Integer, Double> variable) throws IloException {
//            HashMap<String,IloIntVar> result = new HashMap<>();
        if (arc != null){
            for (int i = 0; i < x.length; i++) {
                variable.put(getGlobalIndex(i), cplex.getValue(x[i]));
            }
        }
        return variable;
    }

    //返回y的值
//    public double getResult() throws IloException {
//        return cplex.getValue(y);
//    }

    //返回目标函数值
    public double getObjective() throws IloException {
        return cplex.getObjValue();
    }

    private void creatArcMap(){
        arcMap = new HashMap<>();
        for (int i = 0; i < arc.size(); i++) {
            arcMap.put(arc.get(i),i);
        }
    }

    public void saveModel() throws IloException {
        cplex.exportModel("subModel"+equIndex+".lp");
    }

    private int getGlobalIndex(int localIndex){
        return arc.get(localIndex);
    }

    private int getLocalIndex(int globalIndex){
        return arcMap.get(globalIndex);
    }
}


