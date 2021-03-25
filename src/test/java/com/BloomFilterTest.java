package com;

import bloomfilter.CanGenerateHashFrom;
import bloomfilter.mutable.BloomFilter;
import com.concurrent.Task;
import com.utils.GenUtil;
import com.utils.SerialUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

/** 布隆过滤器测试类 */
public class BloomFilterTest {
    @Test
    public void t1(){
        long expectedElements = 1000000;
        double falsePositiveRate = 0.1;
        BloomFilter<byte[]> bloomFilter = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$
        );
        byte[] element = new byte[100];
        bloomFilter.add(element);
        System.out.println(bloomFilter.mightContain(element));
        bloomFilter.dispose();
    }
    @Test
    public void serializeTest() throws Exception{
        long expectedElements = 1000000;
        double falsePositiveRate = 0.01;
        BloomFilter<byte[]> bloomFilter = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$
        );
        for(int i = 0;i < 10000;i++){
            String str = "index" + i;
            byte[] element = str.getBytes("utf-8");
            bloomFilter.add(element);
        }
        File file = GenUtil.getFile("testBloomFilter","xset_bf_10000.bf");
        SerialUtil.serialize(bloomFilter,file.getAbsolutePath());
        bloomFilter.dispose();
    }
    @Test
    public void serializeTest2() throws Exception{
        long expectedElements = 30;
        double falsePositiveRate = 0.1;
        BloomFilter<byte[]> bloomFilter = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$
        );
        for(int i = 0;i < 30;i++){
            String str = "index" + i;
            byte[] element = str.getBytes("utf-8");
            bloomFilter.add(element);
        }
        File file = GenUtil.getFile("testBloomFilter","xset_bf_30.bf");
        SerialUtil.serialize(bloomFilter,file.getAbsolutePath());
        bloomFilter.dispose();
    }
    @Test
    public void serializeTest3() throws Exception{
        File file = GenUtil.getFile("testBloomFilter","test.bf");
        long expectedElement = 1000000;
        double falsePositveRate = 0.01;
        BloomFilter<byte[]> bloomFilter = BloomFilter.apply(
                expectedElement,
                falsePositveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$
        );
        bloomFilter.add("index1".getBytes("utf-8"));
        System.out.println(bloomFilter.mightContain(("index1").getBytes("utf-8")));
        SerialUtil.serialize(bloomFilter,file.getAbsolutePath());
    }
    @Test
    public void deSerializeTest() throws Exception{
        File file = GenUtil.getFile("testBloomFilter","xset_bf_10000.bf");
        BloomFilter<byte[]> bloomFilter = SerialUtil.deSerialize(file.getAbsolutePath());
        for(int i = 0;i < 100;i++){
            System.out.println("index" + i + " " + bloomFilter.mightContain(("index" + i).getBytes("utf-8")));
        }
        for(int i = 10000 - 500;i < 10000;i++){
            System.out.println("index" + i + " " + bloomFilter.mightContain(("index" + i).getBytes("utf-8")));
        }
    }
    @Test
    public void deSerializeTest2() throws Exception{
        File file = GenUtil.getFile("testBloomFilter","xset_bf_10000.bf");
        BloomFilter<byte[]> bloomFilter = SerialUtil.deSerialize(file.getAbsolutePath());
        String str = "123871248364818723968573209213904682190473264092384095732892370948709852378647832498235023402380937982378463872612938741871207018798328470891273";
        byte[] byts = str.getBytes("utf-8");
        System.out.println(bloomFilter.mightContain(byts));
    }
    @Test
    public void deSerializeTest3() throws Exception{
        File file = GenUtil.getFile("testBloomFilter","test.bf");
        BloomFilter<byte[]> bloomFilter = SerialUtil.deSerialize(file.getAbsolutePath());
        System.out.println(bloomFilter.mightContain("index1".getBytes("utf-8")));
        System.out.println(bloomFilter.mightContain("index2".getBytes("utf-8")));
        bloomFilter.add("index2".getBytes("utf-8"));
        System.out.println(bloomFilter.mightContain("index2".getBytes("utf-8")));
        SerialUtil.serialize(bloomFilter,file.getAbsolutePath());
    }
    @Test
    public void deserializeTest4() throws Exception {
        File file = GenUtil.getFile("testBloomFilter","test.bf");
        BloomFilter<byte[]> bloomFilter = SerialUtil.deSerialize(file.getAbsolutePath());
        System.out.println(bloomFilter.mightContain("index1".getBytes("utf-8")));
        System.out.println(bloomFilter.mightContain("index2".getBytes("utf-8")));
    }
    @Test
    public void insertItemTest() throws Exception{
        File file = GenUtil.getFile("testBloomFilter","xset_bf_10000.bf");
        BloomFilter<byte[]> bloomFilter = SerialUtil.deSerialize(file.getAbsolutePath());

        String keyword = "key1";
        ArrayList<String> fileName = new ArrayList<>();
        for(int i = 0;i < 100;i++){
            fileName.add("index" + i);
        }
        Task task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        for(int i = 0;i < task.getXSets().size();i++){
            bloomFilter.add(task.getXSets().get(i).getBytes("utf-8"));
        }

        keyword = "key2";
        fileName = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            fileName.add("index" + i);
        }
        task = EDBSetupSplMen.EDBSetup(keyword,fileName);
        for(int i = 0;i < task.getXSets().size();i++){
            bloomFilter.add(task.getXSets().get(i).getBytes("utf-8"));
        }

        SerialUtil.serialize(bloomFilter,file.getAbsolutePath());
    }
}
