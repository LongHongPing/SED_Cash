package com.concurrent;
/** 原料抽象类 */
abstract class AbstractMaterial implements Material,Runnable{
    @Override
    public void run(){
        while(true){
            try{
                material();
            }catch(InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}
