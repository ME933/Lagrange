package ScheduleMip.Data;//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.example.schedulespringboot.common.ScheduleMip.ScheduleMip;
//import com.example.schedulespringboot.entity.Arc;
//import com.example.schedulespringboot.entity.Equip;
//import com.example.schedulespringboot.entity.Result;
//import com.example.schedulespringboot.entity.Task;
//import com.example.schedulespringboot.service.*;
//import ilog.concert.IloException;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class Schedule {
//    private final TaskService taskService;
//    private final TaskTargetService taskTargetService;
//    private final EquipService equipService;
//    private final ArcService arcService;
//    private final ResultService resultService;
//
//    public Schedule(TaskService taskService, TaskTargetService taskTargetService, EquipService equipService, ArcService arcService,
//                    ResultService resultService) {
//        this.taskService = taskService;
//        this.taskTargetService = taskTargetService;
//        this.equipService = equipService;
//        this.arcService = arcService;
//        this.resultService = resultService;
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    @Transactional
//    public void doTask() throws ParseException, IloException {
//        long inProgressTasksNum = taskService.countInProgressTasks();
//        long pendingTasksNum = taskService.countPendingTasks();
//
//        boolean newTaskReady = inProgressTasksNum == 0 && pendingTasksNum > 0;
//
//        System.out.println("InProgress: " + inProgressTasksNum + "; Pending: " + pendingTasksNum + "; NewTaskReady: " + newTaskReady);
//
//        if (!newTaskReady) return;
//
//        Task validTask = taskService.findValidTask();  // 没有待进行任务时返回结果为 null
//        if (validTask == null) return;
//        Integer taskId = validTask.getTaskId();
//
////        Integer taskId = 497;
//        List<Integer> equipIds = Arrays.asList(1311, 1312, 1313, 1314, 1318, 1319);
//
//
//        // 数据准备
//        List<Integer> targets = taskTargetService.findTargetsByTaskIdAndEquipIds(taskId, equipIds);
//        List<Equip> equips = equipService.list();
//        List<Equip> newEquips = new ArrayList<>();
//        for(Equip equip: equips){
//            if(equipIds.contains(equip.getEquipId())){
//                newEquips.add(equip);
//            }
//        }
//        List<Arc> arcs = arcService.findArcsByTaskId(taskId, equipIds);
//
//
//        // 开始任务
//        LambdaUpdateWrapper<Task> taskStartWrapper = new LambdaUpdateWrapper<>();
//        taskStartWrapper.eq(Task::getTaskId, taskId).set(Task::getStatus, 0);
//        taskService.update(taskStartWrapper);
//
//
        // 跑代码……
//        List<Integer> arcIds = new ArrayList<>();
//        Float completeRatio = 0.0F;
//        Float coverRatio = 0.0F;
//        ArrayList<Float> ratio;
////        ratio = scheduleByCost.schedule(targets, equips, arcs, arcIds, validTask);
//        ScheduleMip scheduleMip = new ScheduleMip();
//        ratio = scheduleMip.schedule(targets, newEquips, arcs, arcIds, validTask);
//        completeRatio = ratio.get(0);
//        coverRatio = ratio.get(1);
//        System.out.println("完成率：" + completeRatio + "覆盖率：" + coverRatio);
//
//
//        // 清除已有结果
//        LambdaQueryWrapper<Result> resultRemoveWrapper = new LambdaQueryWrapper<>();
//        resultRemoveWrapper.eq(Result::getTaskId, taskId);
//        resultService.remove(resultRemoveWrapper);
//
//
//        // 写结果
//        List<Result> results = new ArrayList<>();
//        for (Integer arcId : arcIds) {
//            Result result = new Result();
//            result.setTaskId(taskId);
//            result.setArcId(arcId);
//            results.add(result);
//        }
//        resultService.saveBatch(results);
//
//
//        // 写任务评价
//        Float utilizationRatio = 92.22F;
//        Float balanceRatio = 91.11F;
//        LambdaUpdateWrapper<Task> taskFinishWrapper = new LambdaUpdateWrapper<>();
//        taskFinishWrapper.eq(Task::getTaskId, taskId)
//                .set(Task::getStatus, 1)
//                .set(Task::getCompleteRatio, completeRatio)
//                .set(Task::getCoverRatio, coverRatio)
//                .set(Task::getUtilizationRatio, utilizationRatio)
//                .set(Task::getBalanceRatio, balanceRatio);
//        taskService.update(taskFinishWrapper);
//    }
//}
