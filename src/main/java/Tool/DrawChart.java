package Tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DrawChart {
    Map<Integer, Double> data1;
    Map<Integer, Double> data2;

    public DrawChart(){
        data1 = new HashMap<>();
        data2 = new HashMap<>();
    }

    public void addData(double data1, double data2){
        this.data1.put(this.data1.size(),data1);
        this.data2.put(this.data2.size(),data2);
    }

    public void draw(){
        Map<Double, Double>[] dataSet = new Map[]{data1, data2};
        String[] types = new String[]{"fixedLB", "UB"};
        //调用绘图工具
        DrawingTools.drawLineChart("Tool", "拉格朗日收敛过程", "Iteration", "Bond", dataSet, types);

        Scanner in = new Scanner(System.in);
        in.hasNext();
    }
}
