package ScheduleMip.Data.ScheduleMip.Solve;

import ScheduleMip.Data.ScheduleMip.Data.*;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import lombok.Getter;

@Getter
public class Solver {
    IloCplex cplex;
//    DrawChart drawChart;

    EntityData entityData;
    HashData hashData;
    IndexData indexData;
    ConflictData conflictData;
    ResultData resultData;

    int arcNum;
    int starNum;
    int equNum;

    int equConNum;
    int starConNum;
    int task;

    public Solver(EntityData entityData, HashData hashData, IndexData indexData, ConflictData conflictData, ResultData resultData) {
        this.entityData = entityData;
        this.hashData = hashData;
        this.indexData = indexData;
        this.conflictData = conflictData;
        this.resultData = resultData;
        this.initNum();
//        this.initParams();
    }

    private void initNum() {
        this.arcNum = entityData.getArcNum();
        this.starNum = entityData.getStarNum();
        this.equNum = entityData.getEquNum();
        this.equConNum = conflictData.getEquConfNum();
        this.starConNum = conflictData.getStarConfNum();
        this.task = entityData.getCycleCount();
    }

    public ResultData executeModel(String modelMode, String solveMode, double MIPGap) throws IloException {
        cplex = new IloCplex();
        assert modelMode.equals("Mip") || modelMode.equals("Lagrange");
        if (solveMode.equals("Mip")) {
            Model model = new Model(entityData, conflictData, hashData);
            model.iniSolver(modelMode);
            model.mipSolve(MIPGap);
            return model.getResult();
        } else if (solveMode.equals("Lagrange")) {
//            this.Lagrange();
        }
        return null;
    }
}
