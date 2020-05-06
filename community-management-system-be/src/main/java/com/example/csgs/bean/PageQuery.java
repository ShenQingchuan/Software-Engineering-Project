package com.example.csgs.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery<T> {
    private int curPage;
    private int totalPage;
    private int totalSize;
    private List<T> dataList;
}
