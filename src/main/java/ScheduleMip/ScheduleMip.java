package ScheduleMip;

import Entity.Arc;
import Entity.Equip;
import Entity.Star;
import Entity.Task;
import ScheduleMip.Data.*;
import ScheduleMip.PrecessData.DataFilter;
import ScheduleMip.PrecessData.PreProcess;
import ScheduleMip.PrecessData.ProcessConflict;
import ScheduleMip.Solve.Solver;
import ScheduleMip.Tool.TimeTool;
import ilog.concert.IloException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleMip {
//    DrawChart drawChart;
    EntityData entityData;
    HashData hashData;
    IndexData indexData;
    ConflictData conflictData;
    ResultData resultData;

    int arcNum;
    int starNum;

    private void processData(List<Star> stars, List<Equip> equips, List<Arc> arcs, List<Integer> arcIds, Task validTask) throws ParseException {
        arcNum = arcs.size();
        starNum = stars.size();
        resultData = new ResultData(arcIds, starNum, arcNum);

        DataFilter dataFilter = new DataFilter(stars, equips, arcs ,validTask);
        arcs = dataFilter.arcFilter();
        stars = dataFilter.starFilter();
        equips = dataFilter.equipFilter();

        PreProcess preProcessMip = new PreProcess(stars, equips, arcs, validTask);
        this.entityData = preProcessMip.createEntity();
        this.indexData = preProcessMip.createIndex();
        this.hashData = preProcessMip.createHash();

        ProcessConflict processConflict = new ProcessConflict(hashData, entityData);
        this.conflictData = processConflict.createConflict();
    }

    public ResultData solve() throws IloException {
        Solver solver = new Solver(entityData, hashData, indexData, conflictData, resultData);
//        solver.setMaxIter(100);
        resultData = solver.executeModel("compute", "Default", 0.00);
        return resultData;
//        drawChart = solver.getDrawChart();
    }


    public ArrayList<Float> schedule(List<Star> stars, List<Equip> equips, List<Arc> arcs, List<Integer> arcIds, Task validTask) throws ParseException, IloException {
        TimeTool timeTool = new TimeTool();
        timeTool.addName("总计");
        timeTool.addName("预处理");
        timeTool.addStartTime("总计");
        timeTool.addStartTime("预处理");
        this.processData(stars, equips, arcs, arcIds, validTask);
//        main.testLoadData();
        timeTool.addEndTime("预处理");
        timeTool.printTime("预处理");
        resultData = this.solve();
        timeTool.addEndTime("总计");
        float timeAll = timeTool.printTime("总计");
        resultData.setTimeCost(timeAll);
//        main.drawChart.draw();
        System.out.println("Done!");
        return resultData.getResultList();
    }
}
