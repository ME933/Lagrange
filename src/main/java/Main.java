
import Data.ComData;
import Data.OriData;
import Data.PreData;
import Solve.Solver;
import Tool.TimeTool;
import ilog.concert.IloException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    ComData comData;

    public void loadData() throws ParseException {
        OriData oriData;
        PreData preData;
        PreProcess preProcess;
        oriData = new OriData();
        preProcess = new PreProcess(oriData,"load");
        preData = preProcess.getPreData();
        this.comData = new ComData(preData,2,"");
    }

    public void testLoadData() throws ParseException, IloException {
        int starNum = 5;
        int equNum = 2;
        int arcNum = 20;
        int arcSize = 240;
        int[] equCap = new int[equNum];
        for (int i = 0; i < equNum; i++) {
            equCap[i] = 1;
        }
        OriData oriData;
        PreData preData;
        PreProcess preProcess;
        oriData = new OriData(starNum, equNum, arcNum, arcSize, equCap);
        preProcess = new PreProcess(oriData,"compute");
        preData = preProcess.getPreData();
        this.comData = new ComData(preData,3,"default");
        this.solve();
    }

    public void solve() throws IloException {
        Solver solver = new Solver(comData);
        solver.executeModel("compute","Lagrange",0.05);
    }

    public static void main(String[] args) throws ParseException, IloException {
        Main main = new Main();
        TimeTool timeTool = new TimeTool();
        timeTool.addName("总计");
        timeTool.addName("预处理");
        timeTool.addStartTime("总计");
        timeTool.addStartTime("预处理");
        main.loadData();
        timeTool.addEndTime("预处理");
        timeTool.printTime("预处理");
//        main.testLoadData();
        main.solve();
        timeTool.addEndTime("总计");
        timeTool.printTime("总计");
        System.out.println("Done!");
    }
}