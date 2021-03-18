package com.concurrent;

import java.util.Collection;

/** 原料单位 */
public class TaskMaterial {
    private String keyword;
    private Collection<String> fileNames;

    public TaskMaterial(String keyword,Collection<String> fileNames){
        this.keyword = keyword;
        this.fileNames = fileNames;
    }

    public String getKeyword(){
        return keyword;
    }
    public Collection<String> getFileNames(){
        return fileNames;
    }

}
