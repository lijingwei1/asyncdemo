package com.async.demo.controller;

import com.async.demo.service.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
public class TestController {
    @Autowired
    private TestServiceImpl service;

    @GetMapping("/test")
    public String test() throws Exception{
        Future<String> f = service.asyncInvokeReturnFuture(1111);
        /*for(int i = 0; i < 100; i++){
            System.out.println("0000000000000000000000000000000000000000000000000000");
            Future<String> f = service.asyncInvokeReturnFuture(i);
            System.out.println("++++++++++++++++" + f.get());
        }*/
        return "sss";
    }
    @GetMapping("/test2")
    public void test2() throws Exception{
        service.function1(100);
        service.function2(100);
    }
    @GetMapping("/test1")
    public void test1() throws Exception{
        for(int i = 0; i < 100; i++){
            System.out.println("0000000000000000000000000000000000000000000000000000");
            service.function1(i);
            service.function2(i);
        }
    }
}
