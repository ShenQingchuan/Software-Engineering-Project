package com.example.csgs.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery<T> {
    private long curPage;
    private long totalPage;
    private long totalSize;
    private List<T> dataList;
}
