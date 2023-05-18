package DataTransferTest;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.text.ParseException;
import java.util.Arrays;

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

    public Model() throws IloException, ParseException {
        cplex = new IloCplex();
        data = new Data(10,20,60,30);
        this.cliStarNum = data.getCliStarNum();
        this.arcNum = data.getArcNum();
        this.conStarNum = data.getConStarNum();
        this.conConNum = data.conStarConf.size();
        this.cliConNum = data.cliStarConf.size();
        this.weiConNum = data.conf.size();
    }

    private void createModel() throws IloException {
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

    private void addObj() throws IloException {
        int[] yCost = new int[cliConNum];
        Arrays.fill(yCost, 1);

        //添加模型目标
        cplex.addMaximize(cplex.scalProd(yCost,y));
    }

    private void addResTime(){

    }

    private void addResConCon(){

    }

    private void addResCliCon(){

    }

    private void addResWeiCon(){

    }
}
