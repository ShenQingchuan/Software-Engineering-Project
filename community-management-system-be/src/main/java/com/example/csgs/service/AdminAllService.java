package com.example.csgs.service;

import com.example.csgs.bean.AreaList;
import com.example.csgs.bean.CreateGridInfo;
import com.example.csgs.bean.GridPersonalInfo;
import com.example.csgs.bean.PageQuery;

import java.util.List;

public interface AdminAllService {

    AreaList getAreaList(String userID);

    List<AreaList> getAllAreaList();

    boolean addGrid(CreateGridInfo createGridInfo);

    PageQuery<GridPersonalInfo> getAllGrids(String page);

    boolean deleteOneGrid(Long id);

    boolean modifyGrid(AreaList areaList, Long id);
}
