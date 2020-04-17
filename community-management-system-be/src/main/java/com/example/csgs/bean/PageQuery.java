package com.example.csgs.bean;

import lombok.Data;

import java.util.List;

@Data
public class PageQuery {
    private int curPage;
    private int totalPage;
    private int totalSize;
    private List<User> user;

    public PageQuery(int curPage, int totalPage, int totalSize, List<User> user) {
        this.curPage = curPage;
        this.totalPage = totalPage;
        this.totalSize = totalSize;
        this.user = user;
    }
}
