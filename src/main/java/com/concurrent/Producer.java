package com.concurrent;
/** 生产者接口 */
public interface Producer {
    void produce() throws InterruptedException;
}
