package com.example.csgs.service;

import com.alibaba.fastjson.JSONObject;
import com.example.csgs.bean.*;
import com.example.csgs.entity.UserProfile;

import java.awt.print.Pageable;
import java.util.List;

public interface AdminAllService {

    AreaList getAreaList(String userID);

    List<AreaList> getAllAreaList();

    boolean addGrid(CreateGridInfo createGridInfo);

    PageQuery<GridPersonalInfo> getAllGrids(String page);

    boolean deleteOneGrid(Long id);

    boolean modifyGrid(AreaList areaList,Long id);

    UserProfile getGridMaterial(String userID);
}
