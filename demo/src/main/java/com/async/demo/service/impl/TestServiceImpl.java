package com.async.demo.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Future;

@Service
public class TestServiceImpl {
    /**
     * 带有参数的异步调用，异步方法可以传入参数
     * @param i
     */
    @Async
    public void function1(int i){
        System.out.println("f1:++++" + i + Thread.currentThread().getName() + " " + UUID.randomUUID());
        try{
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 带参数的异步调用 异步方法可以传入参数
     * 对于返回值是void，异常会被AsyncUncaughtExceptionHandler处理掉
     * @param i
     */
    @Async
    public void function2(int i){
        /*int d = 1/0;*/
        System.out.println("f2:++++" + i + Thread.currentThread().getName() + " " + UUID.randomUUID());
        try{
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 异常调用返回Future
     * 对于返回值是Future，不会被AsyncUncaughtExceptionHandler处理，需要我们在方法中捕获异常并处理
     * 或者在调用方在调用Futrue.get时捕获异常进行处理
     * @param i
     * @return
     */
    @Async
    public Future<String> asyncInvokeReturnFuture(int i){
        System.out.println("function:++++" + Thread.currentThread().getName() + " " + UUID.randomUUID());
        System.out.println("asyncInvokeReturnFuture, parementer={}" + i);
        Future<String> future;
        try {
            Thread.sleep(1000 * 1);
            future = new AsyncResult<String>("success:" + i);
        } catch (InterruptedException e) {
            future = new AsyncResult<String>("error");
        }
        return future;
    }
}
