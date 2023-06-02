package ScheduleMip.Data.ScheduleMip.Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TimeTool {
    HashMap<String, ArrayList<Long>> startTime;
    HashMap<String, ArrayList<Long>> endTime;

    public TimeTool() {
        startTime = new HashMap<>();
        endTime = new HashMap<>();
    }

    /**
     * @description: 将数组中的Name添加到Map中
     * @date: 2023/4/11 1:47
     */
    public void addName(String[] nameList) {
        ArrayList<String> nameArray = new ArrayList<>(Arrays.asList(nameList));
        for (String name : nameArray) {
            startTime.put(name, new ArrayList<>());
            endTime.put(name, new ArrayList<>());
        }
    }

    /**
     * @description: 将Name添加到Map中
     * @date: 2023/4/11 1:47
     */
    public void addName(String name) {
        startTime.put(name, new ArrayList<>());
        endTime.put(name, new ArrayList<>());

    }

    /**
     * @param name 待添加开始时间的Name
     * @description: 添加开始时间
     * @date: 2023/4/11 1:15
     */
    public void addStartTime(String name) {
        //只有startTime和endTime数量一致时才执行
        if (startTime.get(name).size() == endTime.get(name).size()) {
            startTime.get(name).add(System.currentTimeMillis());
        } else {
            throw new IllegalArgumentException("开始时间非法！");
        }
    }

    /**
     * @param name 待添加结束时间的Name
     * @description: 添加结束时间
     * @date: 2023/4/11 1:16
     */
    public void addEndTime(String name) {
        //只有startTime比endTime数量大1时才执行
        if (startTime.get(name).size() == endTime.get(name).size() + 1) {
            endTime.get(name).add(System.currentTimeMillis());
        } else {
            throw new IllegalArgumentException("结束时间非法！");
        }
    }

    /**
     * @param name 待统计时间的Name
     * @return double
     * @description: 计算指定Name的总计运行时间（单位：毫秒）
     * @date: 2023/4/11 1:20
     */
    private long getTime(String name) {
        long countTime = 0;
        ArrayList<Long> startTimeList = startTime.get(name);
        ArrayList<Long> endTimeList = endTime.get(name);
        for (int i = 0; i < startTimeList.size(); i++) {
            countTime += endTimeList.get(i) - startTimeList.get(i);
        }
        return countTime;
    }

    /**
     * @param name 待统计时间的Name
     * @return double
     * @description: 返回指定Name的总计运行时间（单位：毫秒）
     * @date: 2023/4/11 1:20
     */
    public float getMillisecondTime(String name) {
        //只有startTime和endTime数量一致时才执行
        if (startTime.get(name).size() == endTime.get(name).size()) {
            return getTime(name);
        } else {
            throw new IllegalArgumentException("开始时间与结束时间长度不一致！");
        }
    }

    /**
     * @param name 待统计时间的Name
     * @return double
     * @description: 返回指定Name的总计运行时间（单位：秒）
     * @date: 2023/4/11 1:24
     */
    public float getSecondTime(String name) {
        //只有startTime和endTime数量一致时才执行
        if (startTime.get(name).size() == endTime.get(name).size()) {
            return ((float) getTime(name)) / 100;
        } else {
            throw new IllegalArgumentException("开始时间与结束时间长度不一致！");
        }
    }

    public void printAllTime() {
        for (String name : startTime.keySet()) {
            if (startTime.get(name).size() == endTime.get(name).size()) {
                System.out.println(name + "用时: " + getMillisecondTime(name) + "ms.");
            } else {
                throw new IllegalArgumentException("开始时间与结束时间长度不一致！");
            }
        }
    }

    public float printTime(String name) {
        if (startTime.get(name).size() == endTime.get(name).size()) {
            System.out.println(name + "用时: " + getMillisecondTime(name) + "ms.");
            return getMillisecondTime(name);
        } else {
            throw new IllegalArgumentException("开始时间与结束时间长度不一致！");
        }
    }
}
