package com.concurrent;
/** 消费者接口 */
public interface Consumer {
    void consume() throws InterruptedException;
}
