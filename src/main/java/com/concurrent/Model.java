package com.concurrent;
/** 模型实现 */
public interface Model {
    /**  消费者线程 */
    Runnable newRunnableConsumer();
    /** 生产者线程 */
    Runnable newRunnableProducer();
    /** 原料线程 */
    Runnable newRunnableMaterial();
}
