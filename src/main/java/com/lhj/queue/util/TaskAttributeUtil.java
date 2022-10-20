package com.lhj.queue.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.lhj.queue.AbstractTask;
import com.lhj.queue.TaskAttribute;
import com.lhj.queue.YxxCons;

/**
 * @author hongjian.liu
 * @version 1.0.0
 * @ClassName TaskAttributeUtil
 * @Description 任务的一些属性设置。
 * @date 2017-03-21
 */
public class TaskAttributeUtil {

    /**
     * setAttribute。
     * <ul>
     * <li>计算任务所在槽位</li>
     * <li>记录任务的加入时间，应该几点执行</li>
     * <li>任务Id和槽位的映射记录到taskSlotMapping中</li>
     * </ul>
     *
     * @param secondsLater    以当前时间点为基准，多少秒以后执行
     * @param task
     * @param taskSlotMapping
     * @return 返回所在槽位索引
     */
    public static int setAttribute(int secondsLater, AbstractTask task, Map<String, TaskAttribute> taskSlotMapping) {
        TaskAttribute taskAttribute = new TaskAttribute();
        Calendar calendar = Calendar.getInstance();
        //把当前时间的分钟和秒加起来 比如 2分57秒= 2*60+57=177      下次执行时间secondsLater=5秒
        int currentSecond = 0;
        if (YxxCons.WheelCount_3600 == 60) {
            currentSecond = calendar.get(Calendar.SECOND);
        } else if (YxxCons.WheelCount_3600 == 3600) {
            currentSecond = calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
        }

        int soltIndex = (currentSecond + secondsLater) % YxxCons.WheelCount_3600;

        task.setCycleNum(secondsLater / YxxCons.WheelCount_3600);

        calendar.add(Calendar.SECOND, 1);
        taskAttribute.setExecuteTime(calendar.getTime());/** 任务应该什么时候执行 */
        taskAttribute.setSoltIndex(soltIndex);/** 第几个槽位 */
        taskAttribute.setJoinTime(new Date());/** 任务加入槽位的时间 */
        taskSlotMapping.put(task.getId(), taskAttribute);

        return soltIndex;
    }

}
