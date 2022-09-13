package cn.stormbirds.iothub.iotgateway;

import cn.stormbirds.iothub.entity.IotItem;
import cn.stormbirds.iothub.service.IIotItemService;
import cn.stormbirds.iothub.service.IIotgatewayService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ Description cn.stormbirds.iothub.iotgateway
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/14 2:13
 */
@Component
@Slf4j
public class DynamicScheduledManager implements SchedulingConfigurer {

    private static List<IotItem> initCronList ;
    @Resource
    private IIotItemService iotItemService;
    @Resource
    private IIotgatewayService iotgatewayService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(DynamicScheduledPoolConfig.getMySchedulerPool());
        initCronList = iotItemService.list(Wrappers.<IotItem>lambdaQuery().eq(IotItem::getEnable,true));
        initCronList.forEach(iotItem -> taskRegistrar.addTriggerTask(myTask(iotItem),myTrigger(iotItem)));
    }

    private Runnable myTask(IotItem cornEntity) {
        return new Runnable() {
            @Override
            public void run() {
                doItemJob(cornEntity);
            }
        };
    }

    private void doItemJob(IotItem cornEntity) {
        if(iotgatewayService.getById(cornEntity.getGatewayId()).getEnable() )
    }


    /**
     * 根据每一个配置的cron表达式，返回执行周期
     * @param cornEntity
     * @return
     */
    private Trigger myTrigger(IotItem cornEntity){
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                newCronList = cornUtils.getAllCornsList();
                if (newCronList!=null && newCronList.size()>0){
                    for (MyTaskCornEntity cronEntityNew : newCronList) {
                        if (cronEntityNew.getCornId()==cornEntity.getCornId()){
                            if (!cronEntityNew.getCorn().equals(cornEntity.getCorn())
                                    || !cronEntityNew.getOffState().equals(cornEntity.getOffState())){
                                //如果修改，cron表达式修改 或者 关闭状态修改 则取最新的数据的cron表达式和关闭状态
                                cornEntity.setCorn(cronEntityNew.getCorn());
                                cornEntity.setOffState(cronEntityNew.getOffState());
                            }
                        }
                    }
                }else {
                    cornEntity.setOffState("off");//没查到，证明数据删了 手动设置停止
                }
                CronTrigger trigger = new CronTrigger(cornEntity.getCorn());
                return trigger.nextExecutionTime(triggerContext);
            }
        };
    }
}
