package com.example.csgs.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageQuery<T> {
    private int curPage;
    private int totalPage;
    private int totalSize;
    private List<T> dataList;

    public PageQuery(int curPage, int totalPage, int totalSize, List<T> dataList) {
        this.curPage = curPage;
        this.totalPage = totalPage;
        this.totalSize = totalSize;
        this.dataList = dataList;
    }
}
