package com.example.csgs.service;

import com.example.csgs.entity.AreaList;
import com.example.csgs.entity.CreateGridInfo;
import com.example.csgs.entity.GridPersonalInfo;
import com.example.csgs.entity.PageQuery;

import java.util.List;

public interface AdminAllService {

    /**
     * 场景：系统管理员新增网格员，首先要获取区域数据信息，然后在其中选择区域分配网格员
     * 重点：这里我们返回的区域是，还没有被划分的区域，如果某一区域已经被分配，那么不返回该区域信息
     * @param userID 身份证号
     * @return 某一区下的区域信息
     */
    AreaList getAreaList(String userID);

    /**
     * 场景：系统管理员在修改网格员管理区域之前，首先要获取所有未被分配管理的区域数据信息，然后在其中选择区域分配给网格员
     * @return 所有区的AreaList信息
     */
    List<AreaList> getAllAreaList();

    /**
     * 新增网格员接口
     * 同时系统管理员为网格员分配管理区域
     * @param createGridInfo 含有区域信息和用户身份证号
     * @return 布尔类型
     */
    boolean addGrid(CreateGridInfo createGridInfo);

    /**
     * 返回当前系统管理员添加的网格员数据列表
     * @param page 当前请求页数
     * @return 当前页的GridPersonalInfo集合信息和分页信息
     */
    PageQuery<GridPersonalInfo> getAllGrids(String page);

    /**
     * 功能：删除某一网格员（user表中任然存在，只是grid表中不存在了）
     * @param id grid表中的id
     * @return 布尔类型
     */
    boolean deleteOneGrid(Long id);

    /**
     * 场景：在系统管理员获取到网格员数据信息列表之后，选择修改任意网格员管理区域
     * @param areaList 区域信息
     * @param id grid表中id
     * @return 布尔类型
     */
    boolean modifyGrid(AreaList areaList, Long id);
}
