package com.example.csgs.utils;

import com.example.csgs.bean.PageQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CalculatePageUtils {
    public static<T> PageQuery<T> getPageInfo(int size, int pageSize, Pageable pageable, List<T> dataList){
        int totalPage = 0;
        if (size != 0) {
            if (size % pageSize == 0) {
                totalPage = size / pageSize;
            } else {
                totalPage = size / pageSize + 1;
            }
        } else {
            return null;
        }
        return new PageQuery<T>(pageable.getPageNumber() + 1, totalPage, size, dataList);
    }
}
