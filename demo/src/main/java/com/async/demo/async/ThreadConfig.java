package com.async.demo.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ComponentScan("com.async.demo.service.impl")
@EnableAsync
public class ThreadConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        /**
         * SimpleAsyncTaskExecutor:不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程
         * SyncTaskExecutor：这个类没有实现异步调用，只是一个同步操作，只适用于不需要多线程的地方
         * ConcurrentTaskExecutor：Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类
         * SimpleThreadPoolTaskExecutor:是Quartz的SimpleThreadPool的类。线程池同时被quartz和非quartz使用，才需要使用此类
         * ThreadPoolTaskExecutor：最长使用，推荐。其本质是对java.util.concurrent.ThreadPoolExecutor的包装
         */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(5);
        //最大线程数
        executor.setMaxPoolSize(8);
        //设置队列容量
        executor.setQueueCapacity(15);
        //设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        //设置默认线程名称
        executor.setThreadNamePrefix("ljw-");
        //设置拒绝策略
        /**
         * 1. new ThreadPoolExecutor.AbortPolicy():抛出异常java.util.concurrent.RejectedExecutionException
         * 2. new ThreadPoolExecutor.CallerRunsPolicy():用于被拒绝任务的处理程序，它直接再execute方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
         * 3. new ThreadPoolExecutor.DiscardOldestPolicy():如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）
         * 4. new ThreadPoolExecutor.DiscardPolicy():用于被拒绝任务的处理程序，默认情况下它将丢弃被拒绝的任务
         * java.util.concurrent.ThreadPoolExecutor.AbortPolicy 这个是默认使用的拒绝策略,如果有要执行的任务队列已满,且还有任务提交,则直接抛出异常信息
         * java.util.concurrent.ThreadPoolExecutor.DiscardPolicy 这个是忽略策略,如果有要执行的任务队列已满,且还有任务提交,则直接忽略掉这个任务,即不抛出异常也不做任何处理.
         * java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy 忽略最早提交的任务.如果有要执行的任务队列已满,此时若还有任务提交且线程池还没有停止,则把队列中最早提交的任务抛弃掉,然后把当前任务加入队列中.
         * java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy 这个是来着不拒策略.如果有要执行的任务队列已满,此时若还有任务提交且线程池还没有停止,则直接运行任务的run方法.
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        AsyncUncaughtExceptionHandler handler = new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
                System.out.println("未处理：" + objects[0] + "");
            }
        };
        return handler;
    }
}
/*public class ThreadConfig {
    // 执行需要依赖的线程池
    @Bean
    public Executor getExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(11);
        executor.setQueueCapacity(30);
        //处理异常
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler(){
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("-----------------------------");
            }
        });
        executor.initialize();
        return executor;
    }
}*/
