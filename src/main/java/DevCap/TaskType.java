package DevCap;

public class TaskType {
    String[] taskTypeString;
    long seconds;

    public TaskType(String[] starTypeString, int seconds){
        this.taskTypeString = starTypeString;

        this.seconds = seconds;
    }

    public String[] getTtarTypeString() {
        return taskTypeString;
    }

    public long getSeconds() {
        return seconds;
    }
}
