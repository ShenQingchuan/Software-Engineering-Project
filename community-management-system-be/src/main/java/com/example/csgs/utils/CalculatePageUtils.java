package com.example.csgs.utils;

import com.example.csgs.entity.PageQuery;
import com.github.pagehelper.Page;

import java.util.List;

public class CalculatePageUtils {
    public static<T> PageQuery<T> getPageInfo(int page, long pageSize, Page<T> pageable, List<T> dataList){
        long totalPage;
        if (pageable.getTotal() != 0) {
            if (pageable.getTotal() % pageSize == 0) {
                totalPage = pageable.getTotal() / pageSize;
            } else {
                totalPage = pageable.getTotal() / pageSize + 1;
            }
            if (page > totalPage) {
                return null;
            }
        } else {
            return null;
        }
        return new PageQuery<T>(pageable.getPageNum(), totalPage, pageable.getTotal(), dataList);
    }
}
