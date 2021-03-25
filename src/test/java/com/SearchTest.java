package com;

import com.utils.GenUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class SearchTest {
    @Test
    public void searchTest() throws Exception{
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("index1");
        SearchClient searchClient = SearchProtocol.searchClient(keywords);
        ArrayList<byte[]> sendByts = SearchProtocol.searchServer(searchClient,"TSets");
        ArrayList<String> reciveStrs = SearchProtocol.searchClient(keywords.get(0),sendByts);
        for(String fileName : reciveStrs){
            System.out.println(fileName);
        }
    }
    @Test
    public void searchTest2() throws Exception{
        MasterKey mk = EDBSetupKeyUtil.getKey();
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("number1");
        keywords.add("number2");
        System.out.println("The first phase start...");
        SearchClient searchClient = SearchProtocol.searchClient(keywords);

        System.out.println("The second phase start...");
        long start = System.currentTimeMillis();
        ArrayList<byte[]> result = SearchProtocol.searchServer(searchClient,"TSets");
        long end = System.currentTimeMillis();
        System.out.println("The total time: " + (end - start) + " ms");

        System.out.println("The third phase start...");
        ArrayList<String> results = SearchProtocol.searchClient(keywords.get(0),result);
        for(String fileName : results){
            System.out.println(fileName);
        }
    }
    @Test
    public void searchTest3() throws Exception{
        File file = GenUtil.getFile("text","testSet.txt");
        List<String> lines = GenUtil.read(new FileInputStream(file));
        Map<String,Integer> keyCount = new HashMap<>();
        for(int i = 0;i < 3;i++){
            Random random = new Random();
            int index = random.nextInt(lines.size());
            String str = lines.get(index);
            keyCount.put(str.split(" ")[0],Integer.parseInt(str.split(" ")[1]));
        }
        ArrayList<Map.Entry<String,Integer>> list = new ArrayList<>(keyCount.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        ArrayList<String> keywords = new ArrayList<>();
        for(String str : keyCount.keySet()){
            keywords.add(str);
        }
        int count = keyCount.get(keywords.get(0));
       // MasterKey mk = EDBSetupKeyUtil.getKey();

        System.out.println("The first phase start...");
        SearchClient searchClient = SearchProtocol.searchClient(keywords,count);

        System.out.println("The second phase start...");
        long start = System.currentTimeMillis();
        ArrayList<byte[]> result = SearchProtocol.searchServer(searchClient,"TSets");
        long end = System.currentTimeMillis();
        if(result == null){
            System.out.println("Warning: The result is empty");
            return;
        }
        System.out.println("The total time: " + (end - start) + " ms");

        System.out.println("The third phase start...");
        ArrayList<String> results = SearchProtocol.searchClient(keywords.get(0),result);
        for(String fileName : results){
            System.out.println(fileName);
        }
    }
    @Test
    public void searchTest4() throws Exception{
        File file = GenUtil.getFile("text","testSet.txt");
        List<String> lines = GenUtil.read(new FileInputStream(file));

        long sumTime = 0;
        for(int i = 0;i < 100;i++){
            Map<String,Integer> keyCount = new HashMap<>();
            for(int j = 0;j < 100;j++){
                Random random = new Random();
                int index = random.nextInt(lines.size());
                String str = lines.get(index);
                keyCount.put(str.split(" ")[0],Integer.parseInt(str.split(" ")[1]));
            }
            ArrayList<Map.Entry<String,Integer>> list = new ArrayList<>();
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            ArrayList<String> keywords = new ArrayList<>();
            for(String str : keyCount.keySet()){
                keywords.add(str);
            }

            int count = keyCount.get(keywords.get(0));
            SearchProtocol searchProtocol = new SearchProtocol();
            System.out.println("The first phase start...");
            SearchClient searchClient = searchProtocol.searchClient(keywords,count);

            System.out.println("The second phase start...");
            long start = System.currentTimeMillis();
            ArrayList<byte[]> result = SearchProtocol.searchServer(searchClient,"TSets");
            long end = System.currentTimeMillis();
            System.out.println("The total time: " + (end - start) + " ms");
            sumTime = sumTime + (end - start);
            if(result == null){
                continue;
            }

            System.out.println("The third phase start...");
            ArrayList<String> results = SearchProtocol.searchClient(keywords.get(0),result);
            for(String fileName : results){
                System.out.println(fileName);
            }
        }
        System.out.println("100 times test need " + sumTime + " ms");
    }
    @Test
    public void searchTest5() throws Exception{
        File file = GenUtil.getFile("text","testSet.txt");
        List<String> lines = GenUtil.read(new FileInputStream(file));
        ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add("TSets");
        for(String tableName : tableNames){
            long sumTime = 0;
            for(int i = 0;i < 100;i++){
                Map<String,Integer> keyCount = new HashMap<>();
                for(int j = 0;j < 3;j++){
                    Random random = new Random();
                    int index = random.nextInt(lines.size());
                    String str = lines.get(index);
                    keyCount.put(str.split(" ")[0],Integer.parseInt(str.split(" ")[1]));
                }
                ArrayList<Map.Entry<String,Integer>> list = new ArrayList<>();
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                });
                ArrayList<String> keywords = new ArrayList<>();
                for(String str : keyCount.keySet()){
                    keywords.add(str);
                }

                int count = keyCount.get(keywords.get(0));
                System.out.println("The first phase start...");
                SearchClient searchClient = SearchProtocol.searchClient(keywords,count);

                System.out.println("The second phase start...");
                long start = System.currentTimeMillis();
                ArrayList<byte[]> result = SearchProtocol.searchServer(searchClient,"TSets");
                long end = System.currentTimeMillis();
                sumTime = sumTime + (end - start);
                if(result == null){
                    continue;
                }
            }
            System.out.println(tableName + " 100 times test need " + sumTime + " ms");
        }
    }

}
