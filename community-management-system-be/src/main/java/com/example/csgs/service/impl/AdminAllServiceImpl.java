package com.example.csgs.service.impl;

import com.example.csgs.bean.AreaList;
import com.example.csgs.bean.CreateGridInfo;
import com.example.csgs.bean.GridPersonalInfo;
import com.example.csgs.bean.PageQuery;
import com.example.csgs.entity.*;
import com.example.csgs.mapper.*;
import com.example.csgs.service.AdminAllService;
import com.example.csgs.utils.CalculatePageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Log4j
public class AdminAllServiceImpl implements AdminAllService {
    final UserMapper userMapper;
    final ProfileMapper profileMapper;
    final CommunityInfoMapper communityInfoMapper;
    final DistrictMapper districtMapper;
    final GridMapper gridMapper;

    public AdminAllServiceImpl(UserMapper userMapper, ProfileMapper profileMapper,
                               CommunityInfoMapper communityInfoMapper, DistrictMapper districtMapper, GridMapper gridMapper) {
        this.userMapper = userMapper;
        this.profileMapper = profileMapper;
        this.communityInfoMapper = communityInfoMapper;
        this.districtMapper = districtMapper;
        this.gridMapper = gridMapper;
    }

    /**
     * 场景：系统管理员新增网格员，首先要获取区域数据信息，然后在其中选择区域分配网格员
     * 重点：这里我们返回的区域是，还没有被划分的区域，如果某一区域已经被分配，那么不返回该区域信息
     * @param userID 身份证号
     * @return 某一区下的区域信息
     */
    @Override
    public AreaList getAreaList(String userID) {
        UserEntity userEntity = userMapper.findOneByUserID(userID);
        //在添加网格员之前，如果这位用户已经是网格员了，就不能再进行一下操作
        //所以我们通过判断用户userType的方法，事先进行一个身份信息判断
        if (userEntity != null && userEntity.getUserType() != 1) {
            DistrictEntity districtEntity = profileMapper.findDistrictById(userEntity.getId());
            List<String> communityNameList = communityInfoMapper.findCommunityByDistrictId(districtEntity.getId());
            String[] districtNameArray = communityNameList.toArray(new String[0]);
            return new AreaList(districtEntity.getDistrictName(), districtNameArray);
        }
        return null;
    }

    /**
     * 场景：系统管理员在修改网格员管理区域之前，首先要获取所有未被分配管理的区域数据信息，然后在其中选择区域分配给网格员
     * @return 所有区的AreaList信息
     */
    @Override
    public List<AreaList> getAllAreaList() {
        List<DistrictEntity> allDistrict = districtMapper.findAllDistrict();
        List<AreaList> areaLists = new ArrayList<>();
        for (DistrictEntity districtEntity : allDistrict) {
            List<String> communityNameList = communityInfoMapper.findCommunityByDistrictId(districtEntity.getId());
            String[] districtNameArray = communityNameList.toArray(new String[0]);
            AreaList areaList = new AreaList(districtEntity.getDistrictName(), districtNameArray);
            areaLists.add(areaList);
        }
        return areaLists;
    }

    /**
     * 新增网格员接口
     * 同时系统管理员为网格员分配管理区域
     * @param createGridInfo 含有区域信息和用户身份证号
     * @return 布尔类型
     */
    @Override
    public boolean addGrid(CreateGridInfo createGridInfo) {
        String userID = createGridInfo.getUserID();
        UserEntity userEntity = userMapper.findOneByUserID(userID);
        //在添加网格员之前，如果这位用户已经是网格员了，就不能再进行一下操作
        //所以我们通过判断用户userType的方法，事先进行一个身份信息判断
        if (userEntity != null && userEntity.getUserType() != 1) {
            //修改userType
            userMapper.setGrid(userID);
            //在Grid表中加入新的网格员
            gridMapper.addOneGrid(userEntity.getId(), createGridInfo.getAreaList().getDistrictName());
            //为这名网格员分配管理区域
            String[] communityArray = createGridInfo.getAreaList().getCommunityArray();
            GridEntity gridEntity = gridMapper.findByDistrictNameId(createGridInfo.getAreaList().getDistrictName(),userEntity.getId());

            modifyGridIdOfCommunity(communityArray, gridEntity);
            return true;
        }
        return false;
    }

    /**
     * 返回当前系统管理员添加的网格员数据列表
     * @param page 当前请求页数
     * @return 当前页的GridPersonalInfo集合信息和分页信息
     */
    @Override
    public PageQuery<GridPersonalInfo> getAllGrids(String page) {
        int pageSize = 10;
        Page<GridPersonalInfo> pageAble = PageHelper.startPage(Integer.parseInt(page), pageSize);
        PageHelper.orderBy("id ASC");
        List<GridEntity> gridEntityList = gridMapper.findAll();
        List<GridPersonalInfo> gridPersonalInfoList = new ArrayList<>();

        for (GridEntity gridEntity : gridEntityList) {
            String districtName = gridEntity.getDistrictName();
            List<String> communityNameList = communityInfoMapper.findCommunityNameByGridId(gridEntity.getId());
            String[] districtNameArray = communityNameList.toArray(new String[0]);
            AreaList areaList = new AreaList(districtName, districtNameArray);

            UserProfile userProfile = profileMapper.findById(gridEntity.getUserId().getId());
            //此刻返回的id是grid表中网格员的id
            GridPersonalInfo gridPersonalInfo = new GridPersonalInfo(
                    gridEntity.getId(), userProfile.getUserName(), userProfile.getTelPhone(), areaList);
            gridPersonalInfoList.add(gridPersonalInfo);
        }
        return CalculatePageUtils.getPageInfo(Integer.parseInt(page), pageSize, pageAble, gridPersonalInfoList);
    }

    /**
     * 场景：在系统管理员获取到网格员数据信息列表之后，选择修改任意网格员管理区域
     * @param areaList 区域信息
     * @param id grid表中id
     * @return 布尔类型
     */
    @Override
    public boolean modifyGrid(AreaList areaList, Long id) {
        GridEntity gridEntity = gridMapper.findById(id);
        if (gridEntity != null) {
            String[] communityArray = areaList.getCommunityArray();
            List<Long> communityIdList = communityInfoMapper.findCommunityIdByGridId(gridEntity.getId());
            for (Long communityId : communityIdList) {
                //将该网格员之前所负责管理的区域置空（释放区域管理权）
                if (communityInfoMapper.setGridIdIsNull(communityId) > 0) {
                    modifyGridIdOfCommunity(communityArray, gridEntity);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 功能：删除某一网格员（user表中任然存在，只是grid表中不存在了）
     * @param id grid表中的id
     * @return 布尔类型
     */
    @Override
    public boolean deleteOneGrid(Long id) {
        GridEntity gridEntity = gridMapper.findById(id);
        if (gridEntity != null) {
            List<Long> communityIdList = communityInfoMapper.findCommunityIdByGridId(gridEntity.getId());
            for (Long communityId : communityIdList) {
                //将该网格员之前所负责管理的区域置空（释放区域管理权）
                communityInfoMapper.setGridIdIsNull(communityId);
            }
            userMapper.cancelGrid(gridEntity.getUserId().getUserID());
            gridMapper.deleteGrid(gridEntity.getId());
            return true;
        }
        return false;
    }

    /**
     * 功能：分配网格员管理区域(使用在管理区域修改、新增网格员功能接口中)
     * 注意点：现实中可能出现这种情况，在不同区之间，可能存在着相同的小区名，
     * 所以我们在分配管理区域时，事先一定要确定district，然后再进行比对、分配。
     * @param communityArray 某一区下的小区名（多个）
     * @param gridEntity 网格员实例
     */
    public void modifyGridIdOfCommunity(String[] communityArray, GridEntity gridEntity) {
        List<String> list = Arrays.asList(communityArray);
        //在将字符串数组转化成List集合时，Arrays.asList()返回的是Arrays的内部类ArrayList，而不是java.util.ArrayList，
        //所以在使用remove、add等方法是会产生异常“UnsupportedOperationException”，因此我们要进行如下操作。
        List<String> communityNameList = new ArrayList<>(list);
        List<CommunityInfoEntity> communityInfoAllList = communityInfoMapper.findCommunityByDistrictName(gridEntity.getDistrictName());
        for (CommunityInfoEntity communityEntity : communityInfoAllList) {
            /*对于communityList中已经匹配的community，下一次我们不需要再次匹配，所以在每次匹配成功之后，
            将其从communityList中移除，这样可以降低代码的时间复杂度。*/
            if (!communityNameList.isEmpty()) {
                for (String community : communityNameList) {
                    // 从该网格员所位于的district中，遍历所有的community与最新分配的community进行比较，
                    // 若communityNameList中存在，则重新设置gridID值
                    if (communityEntity.getCommunityName().equals(community)) {
                        communityInfoMapper.updateGridId(communityEntity.getId(), gridEntity.getId());
                        communityNameList.remove(community);
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

}
