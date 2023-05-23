package DataTransferTest;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Model {
    //决策变量
    IloIntVar[] x;
    IloIntVar[] y;

    //任务时长约束
    IloRange[] resTime;
    //管控星能力约束
    IloRange[] resConCon;
    //用户星单一连接约束
    IloRange[] resCliCon;
    //带宽约束
    IloRange[] resWeiCon;

    IloCplex cplex;
    Data data;

    int cliStarNum;
    int arcNum;
    int conStarNum;

    int conConNum;
    int cliConNum;
    int weiConNum;

    //用户星-完全冲突弧段集
    ArrayList<ArrayList<Integer>> cliStarConf;
    //管控星-完全冲突弧段集
    ArrayList<ArrayList<Integer>> conStarConf;
    //完全冲突弧段集
    ArrayList<ArrayList<Integer>> conf;

    //管控星-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapCliStarArc;
    //用户星-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapConStarArc;

    //弧段时间
    HashMap<Integer, Long> arcTime;

    //任务所需时间
    HashMap<Integer,Integer> jobTime;
    //用户星对应带宽
    HashMap<Integer,Double> starWeight;

    public Model(Data data) throws IloException, ParseException {
        cplex = new IloCplex();
        this.data = data;
        this.cliStarNum = data.getCliStarNum();
        this.arcNum = data.getArcNum();
        this.conStarNum = data.getConStarNum();
        this.conConNum = data.conStarConf.size();
        this.cliConNum = data.cliStarConf.size();
        this.weiConNum = data.conf.size();
        this.cliStarConf = data.cliStarConf;
        this.conStarConf = data.conStarConf;
        this.conf = data.conf;
        this.mapCliStarArc = data.mapCliStarArc;
        this.mapConStarArc = data.mapConStarArc;
        this.arcTime = data.arcTime;
        this.jobTime = data.jobTime;
        this.starWeight = data.starWeight;
    }

    public void createModel() throws IloException {
        cplex.setParam(IloCplex.Param.Preprocessing.Presolve, false);
        //创建决策变量
        x = cplex.intVarArray(arcNum, 0, 1);
        y = cplex.intVarArray(cliStarNum, 0, 1);

        //命名决策变量
        for (int i = 0; i < arcNum; i++) {
            x[i].setName("x_" + i);
        }
        for (int i = 0; i < cliStarNum; i++) {
            y[i].setName("y_" + i);
        }

        //创建约束
        resTime = new IloRange[cliStarNum];
        resConCon = new IloRange[conConNum];
        resCliCon = new IloRange[cliConNum];
        resWeiCon = new IloRange[weiConNum];

        //添加目标
        this.addObj();
        //添加约束
        this.addResTime();
        this.addResConCon();
        this.addResCliCon();
        this.addResWeiCon();
    }

    public void solve() throws IloException {
        cplex.solve();
    }

    private void addObj() throws IloException {
        int[] yCost = new int[cliStarNum];
        Arrays.fill(yCost, 1);

        //添加模型目标
        cplex.addMaximize(cplex.scalProd(yCost,y));
    }

    private void addResTime() throws IloException {
        for (int i = 0; i < cliStarNum; i++){
            ArrayList<Integer> arcList = mapCliStarArc.get(i);
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            for (int arcIndex: arcList) {
                long time = arcTime.get(arcIndex);
                lhs.addTerm(time, x[arcIndex]);
            }
            resTime[i] = cplex.addGe(cplex.sum(lhs,cplex.prod(-data.getCliStarTime(i),y[i])),0);
            resTime[i].setName("resTime_" + i);
        }
    }

    //每个管控星同时只能观测两个弧段观
    private void addResConCon() throws IloException {
        for (int i = 0; i < conStarConf.size(); i++) {
            ArrayList<Integer> conArc = conStarConf.get(i);
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            for (int arcIndex : conArc) {
                lhs.addTerm(1, x[arcIndex]);
            }
            resConCon[i] = cplex.addLe(lhs, 2);
            resConCon[i].setName("resConCon_" + i);
        }
    }

    //每个用户星同时只能被一个弧段观测
    private void addResCliCon() throws IloException {
        for (int i = 0; i < cliStarConf.size(); i++){
            ArrayList<Integer> conArc = cliStarConf.get(i);
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            for (int arcIndex: conArc) {
                lhs.addTerm(1, x[arcIndex]);
            }
            resCliCon[i] = cplex.addLe(lhs,1);
            resCliCon[i].setName("resCliCon_" + i);
        }
    }

    //同时观测弧段带宽不能超过10
    private void addResWeiCon() throws IloException {
        for (int i = 0; i < cliStarConf.size(); i++) {
            ArrayList<Integer> conArc = conf.get(i);
            IloLinearNumExpr lhs = cplex.linearNumExpr();
            for (int arcIndex : conArc) {
                lhs.addTerm(data.getArcWeight(arcIndex), x[arcIndex]);
            }
            resWeiCon[i] = cplex.addLe(lhs, 10);
            resWeiCon[i].setName("resConCon_" + i);
        }
    }


}
