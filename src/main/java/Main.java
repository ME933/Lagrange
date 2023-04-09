
import Data.ComData;
import Data.OriData;
import Data.PreData;
import Solve.Solver;
import ilog.concert.IloException;

import java.text.ParseException;

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
        int desNum = 10000;
        int equNum = 14;
        int arcNum = 200000;
        int arcSize = 7;
        int[] equCap = new int[equNum];
        for (int i = 0; i < equNum; i++) {
            equCap[i] = 1;
        }
        OriData oriData;
        PreData preData;
        PreProcess preProcess;
        oriData = new OriData(desNum, equNum, arcNum, arcSize, equCap);
        preProcess = new PreProcess(oriData,"compute");
        preData = preProcess.getPreData();
        this.comData = new ComData(preData,1,"default");
        this.solve();
    }

    public void solve() throws IloException {
        Solver solver = new Solver(comData);
        solver.executeModel("compute","Lagrange",0.05);
    }

    public static void main(String[] args) throws ParseException, IloException {
        Main main = new Main();
        main.loadData();
//        main.testLoadData();
        main.solve();
        System.out.println("Done!");
    }
}