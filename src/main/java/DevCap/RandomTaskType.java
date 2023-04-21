package DevCap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomTaskType {
    HashMap<Integer,ArrayList<TaskType>> taskTypeMap;
    String[][] taskTypeSet;

    public RandomTaskType(){
        taskTypeMap = new HashMap<>();
        this.taskTypeSet = new String[][]{{"L","RC","20"}, {"L","SS","20"}, {"S","RC","20"}, {"S","TE","20"}, {"S","SU","20"}, {"S","SS","20"}, {"C","RC","20"}, {"C","TE","20"}, {"C","SU","20"}, {"C","SS","20"}, {"S","DT","20"}};
    }

    public HashMap<Integer,ArrayList<TaskType>> generateRandomTask(int starNum, int seed){
        Random rand = new Random(seed);
        for (int i = 0; i < starNum; i++) {
            int taskNum = rand.nextInt(3) + 1;  // 生成1到3之间的整数
            ArrayList<TaskType> taskList = new ArrayList<>();
            for (int j = 0; j < taskNum; j++) {
                int taskIndex = rand.nextInt(taskTypeSet.length);
                int minutes = rand.nextInt(2) + 3;
                TaskType taskType = new TaskType(taskTypeSet[taskIndex], minutes * 60);
                taskList.add(taskType);
            }
            taskTypeMap.put(i, taskList);
        }
        return taskTypeMap;
    }
}
