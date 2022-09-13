package cn.stormbirds.iothub.iotgateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ Description cn.stormbirds.iothub.iotgateway
 * @ Author StormBirds
 * @ Email xbaojun@gmail.com
 * @ Date 2022/9/14 2:16
 */
@Configuration
public class DynamicScheduledPoolConfig {
    private static int poolSize = 5;//核心线程
    private static int awaitTermination = 60;
    private static String threadNamePrefix = "iot-item-task-job-";
    private static volatile ThreadPoolTaskScheduler taskScheduler;
    @Bean
    public static Executor getMySchedulerPool(){
        if(taskScheduler == null){
            synchronized (ThreadPoolTaskScheduler.class){
                if(taskScheduler == null){
                    taskScheduler = new ThreadPoolTaskScheduler();
                    taskScheduler.setThreadNamePrefix(threadNamePrefix);
                    taskScheduler.setPoolSize(poolSize);
                    taskScheduler.setAwaitTerminationSeconds(awaitTermination);//演示挂壁的时间为60s
                    /**设置为false,关闭线程池中的任务时,直接执行shutdownNow() 延时关闭 开启*/
                    taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
                    taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());//线程池对拒绝任务（无线程可用的）的处理策略
                    taskScheduler.initialize();//初始化
                }
            }
        }
        return taskScheduler;
    }

}
