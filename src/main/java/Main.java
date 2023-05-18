
import Data.ComData;
import Data.OriData;
import Data.PreData;
import Solve.Solver;
import Tool.DrawChart;
import Tool.TimeTool;
import ilog.concert.IloException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    ComData comData;
    DrawChart drawChart;

    public void loadData() throws ParseException {
        OriData oriData;
        PreData preData;
        PreProcess preProcess;
        oriData = new OriData();
        preProcess = new PreProcess(oriData,"load");
        preData = preProcess.getPreData(1);
        this.comData = new ComData(preData,1,"");
    }

    public void testLoadData() throws ParseException, IloException {
        int starNum = 10000;
        int equNum = 20;
        int arcNum = 400000;
        int arcSize = 7;
        int[] equCap = new int[equNum];
        for (int i = 0; i < equNum; i++) {
            equCap[i] = 1;
        }
        OriData oriData;
        PreData preData;
        PreProcess preProcess;
        oriData = new OriData(starNum, equNum, arcNum, arcSize, equCap);
        preProcess = new PreProcess(oriData,"compute");
        preData = preProcess.getPreData(1);
        this.comData = new ComData(preData,1,"default");
    }

    public void solve() throws IloException {
        Solver solver = new Solver(comData);
        solver.setMaxIter(100);
        solver.executeModel("compute","Default",0.00);
        drawChart = solver.getDrawChart();
    }


    public static void main(String[] args) throws ParseException, IloException {
        Main main = new Main();
        TimeTool timeTool = new TimeTool();
        timeTool.addName("总计");
        timeTool.addName("预处理");
        timeTool.addStartTime("总计");
        timeTool.addStartTime("预处理");
        main.loadData();
//        main.testLoadData();
        timeTool.addEndTime("预处理");
        timeTool.printTime("预处理");
        main.solve();
        timeTool.addEndTime("总计");
        timeTool.printTime("总计");
//        main.drawChart.draw();
        System.out.println("Done!");
    }
}