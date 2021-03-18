package com.concurrent;

/** 消费者抽象类 */
abstract class AbstractConsumer implements Consumer,Runnable{
    @Override
    public void run(){
        while(true){
            try{
                consume();
            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}
