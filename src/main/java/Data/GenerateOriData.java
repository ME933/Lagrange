package Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class GenerateOriData {
    //原始卫星数据
    ArrayList<String> oriDataStar;
    //原始设备数据
    ArrayList<String[]> oriDataEqu;
    //原始弧段数据
    ArrayList<String[]> oriDataArcs;


    public GenerateOriData(int starNum, int equNum, int arcNum, int timeWinSizeMin, int[] equCap) {
        // 创建一个Random对象
        Random random = new Random();

        // 创建三个ArrayList对象
        oriDataStar = new ArrayList<>();
        oriDataEqu = new ArrayList<>();
        oriDataArcs = new ArrayList<>();

        // 定义时间窗口的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 生成卫星列表和装备列表
        for (int i = 0; i < starNum; i++) {
            oriDataStar.add(String.valueOf(i));
        }

        for (int i = 0; i < equNum; i++) {
            oriDataEqu.add(new String[]{String.valueOf(i), String.valueOf(equCap[i])});
        }

        // 生成时间窗口列表
        for (int i = 0; i < arcNum; i++) {
            // 随机选择一个卫星ID
            String starId = oriDataStar.get(random.nextInt(starNum));

            // 随机选择一个装备ID
            String equId = oriDataEqu.get(random.nextInt(equNum))[0];

            // 随机生成一个时间窗口开始时间，范围为2023年3月13日到2023年3月14日之间的任意时刻
            LocalDateTime startDateTime = LocalDateTime.of(2023, 3, 13, random.nextInt(24), random.nextInt(60), random.nextInt(60));

            // 时间窗口结束时间为开始时间加上一小时
            LocalDateTime endDateTime = startDateTime.plusMinutes(timeWinSizeMin);

            // 随机选择一个上升下降信息，为“Rise”或“Fall”中的任意一个
            String riseOrFall = random.nextBoolean() ? "Rise" : "Fall";

            // 创建一个String数组，存储时间窗口信息，包括时间窗口ID、卫星ID、装备ID、开始时间、结束时间、上升下降信息
            String[] arcInfo = new String[6];

            arcInfo[0] = String.valueOf(i); // 时间窗口ID
            arcInfo[1] = starId;         // 卫星ID
            arcInfo[2] = equId;         // 装备ID
            arcInfo[3] = formatter.format(startDateTime);   // 开始时间
            arcInfo[4] = formatter.format(endDateTime);     // 结束时间
            arcInfo[5] = riseOrFall;    // 上升下降信息
            // 将String数组添加到oriDataArcs列表中

            oriDataArcs.add(arcInfo);

        }

        // 打印三个列表的内容
        System.out.println("oriDataStar: " + oriDataStar);
        System.out.println("oriDataEqu: " + oriDataEqu);
        // 循环输出oriDataArcs中的内容
        for (String[] arc : oriDataArcs) {
            System.out.println(java.util.Arrays.toString(arc));
        }
    }

    public ArrayList<String> getOriDataStar() {
        return oriDataStar;
    }

    public ArrayList<String[]> getOriDataEqu() {
        return oriDataEqu;
    }

    public ArrayList<String[]> getOriDataArcs() {
        return oriDataArcs;
    }
}
