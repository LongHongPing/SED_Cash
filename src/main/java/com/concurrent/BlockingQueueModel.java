package com.concurrent;

import com.EDBSetupSplMen;
import com.db.MysqlDb;
import com.db.RedisDb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/** 消费者-生产者模型 */
public class BlockingQueueModel implements Model{
    private static int materialBegin = 4000;
    private BlockingQueue<TaskMaterial> queueMaterial = null;
    private BlockingQueue<Task> queue = null;

    public BlockingQueueModel(int cap){
        this.queue = new LinkedBlockingQueue<>(cap);
        this.queueMaterial = new LinkedBlockingQueue<>(100);
    }

    @Override
    public Runnable newRunnableConsumer(){
        return new ConsumerImpl();
    }
    @Override
    public Runnable newRunnableProducer(){
        return new ProducerImpl();
    }
    @Override
    public Runnable newRunnableMaterial(){
        return new MaterialImpl();
    }

    private class ConsumerImpl extends AbstractConsumer implements Consumer,Runnable{
        @Override
        public void consume() throws InterruptedException{
            Task task = queue.take();
            MysqlDb mysqlUtil = new MysqlDb();
            mysqlUtil.saveTask(task);
        }
    }
    private class ProducerImpl extends AbstractProducer implements Producer,Runnable{
        @Override
        public void produce() throws InterruptedException{
            TaskMaterial taskMaterial = queueMaterial.take();
            try{
                Task task = EDBSetupSplMen.EDBSetup(taskMaterial.getKeyword(),taskMaterial.getFileNames());
                queue.put(task);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private class MaterialImpl extends AbstractMaterial implements Material,Runnable{
        @Override
        public void material() throws InterruptedException{
            Map<String, Collection<String>> map = new HashMap<>();
            synchronized(this){
                map = RedisDb.getRecords(materialBegin,materialBegin + 50);
                materialBegin += 50;
                for(String keyword : map.keySet()){
                    if(map.get(keyword).size() > 200){
                        System.out.println(keyword + "includes in" + map.get(keyword) + "files.");
                        continue;
                    }
                    TaskMaterial taskMaterial = new TaskMaterial(keyword,map.get(keyword));
                    queueMaterial.put(taskMaterial);
                }
            }
            if(materialBegin % 1000 == 0){
                System.out.println(Thread.currentThread().getName() + "already running " + materialBegin);
            }
        }
    }

    public static void main(String[] args){
        Model model = new BlockingQueueModel(50);
        Runnable runnable = model.newRunnableMaterial();
        new Thread(runnable,"Material" + 1).start();

        for(int i = 0;i < 1;i++){
            new Thread(model.newRunnableConsumer(),"Consumer" + i).start();
        }
        for(int i = 0;i < 5;i++){
            new Thread(model.newRunnableProducer(),"Producer" + i).start();
        }
    }
}
