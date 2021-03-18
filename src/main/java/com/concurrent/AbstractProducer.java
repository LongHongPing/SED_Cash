package com.concurrent;
/** 生产者抽象类 */
abstract class AbstractProducer implements Producer,Runnable{
    @Override
    public void run(){
        while(true){
            try{
                produce();
            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}
