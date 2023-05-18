package DevCap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomTaskType {
    HashMap<Integer,ArrayList<TaskType>> taskTypeMap;
    String[][] taskTypeSet1;
    String[][] taskTypeSet2;
    String[][] taskTypeSet3;

    public RandomTaskType(){
        taskTypeMap = new HashMap<>();
//        this.taskTypeSet = new String[][]{{"L","RC","20"}, {"L","SS","20"}, {"S","RC","20"}, {"S","TE","20"}, {"S","SU","20"}, {"S","SS","20"}, {"C","RC","20"}, {"C","TE","20"}, {"C","SU","20"}, {"C","SS","20"}, {"S","DT","20"}};
        this.taskTypeSet1 = new String[][]{{"L","SS","20"}, {"S","SS","20"}, {"C","SS","20"}};
        this.taskTypeSet2 = new String[][]{{"S","RC","20"}, {"S","TE","20"}, {"S","SU","20"}, {"S","SS","20"}};
        this.taskTypeSet3 = new String[][]{{"S","DT","20"}};
    }

    public HashMap<Integer,ArrayList<TaskType>> generateRandomTask(int starNum, int seed){
        Random rand = new Random(seed);
        for (int i = 0; i < starNum; i++) {
            double taskRand = rand.nextDouble();
            ArrayList<TaskType> taskList = new ArrayList<>();
            if(taskRand < 0.45){
                randTask(rand, taskList, taskTypeSet1);
            } else if (taskRand < 0.9) {
                randTask(rand, taskList, taskTypeSet2);
            } else {
                randTask(rand, taskList, taskTypeSet3);
            }
            taskTypeMap.put(i, taskList);
        }
        return taskTypeMap;
    }

    private void randTask(Random rand, ArrayList<TaskType> taskList, String[][] taskTypeSet) {
        int taskIndex1 = rand.nextInt(taskTypeSet.length);
        int taskIndex2 = rand.nextInt(taskTypeSet.length);
//        int taskIndex3 = rand.nextInt(taskTypeSet.length);
        int minutes = rand.nextInt(2) + 3;
        TaskType taskType1 = new TaskType(taskTypeSet[taskIndex1], minutes * 60);
        TaskType taskType2 = new TaskType(taskTypeSet[taskIndex2], minutes * 60);
//        TaskType taskType3 = new TaskType(taskTypeSet[taskIndex3], minutes * 60);
        taskList.add(taskType1);
        taskList.add(taskType2);
//        taskList.add(taskType2);
    }
}
