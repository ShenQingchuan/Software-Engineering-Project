package com.example.csgs.service.Impl;

import com.example.csgs.bean.*;
import com.example.csgs.dao.*;
import com.example.csgs.entity.*;
import com.example.csgs.service.AdminAllService;
import com.example.csgs.utils.CalculatePageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AdminAllServiceImpl implements AdminAllService {
    @Autowired
    UserDao userDao;
    @Autowired
    ProfileDao profileDao;
    @Autowired
    CommunityInfoDao communityInfoDao;
    @Autowired
    DistrictDao districtDao;
    @Autowired
    GridDao gridDao;

    /**
     * 获取用户资料接口
     * 场景：系统管理员添加网格员时，需要查看该居民用户的基本资料信息
     */
    @Override
    public UserProfile getGridMaterial(String userID) {
        Optional<UserEntity> byIdUser = userDao.findOneByUserID(userID);
        return (UserProfile) byIdUser.<Object>map(UserEntity::getUserProfile).orElse(null);
    }

    /**
     * 场景：系统管理员新增网格员，首先要获取区域数据信息，然后在其中选择区域分配网格员
     * 重点：这里我们返回的区域是，还没有被划分的区域，如果某一区域已经被分配，那么不返回该区域信息
     */

    @Override
    public AreaList getAreaList(String userID) {
        Optional<UserEntity> userEntity = userDao.findOneByUserID(userID);

        //在添加网格员之前，如果这位用户已经是网格员了，就不能再进行一下操作
        //所以我们通过判断用户userType的方法，事先进行一个身份信息判断
        if (userEntity.isPresent() && userEntity.get().getUserType() != 1) {
            DistrictEntity districtEntity = userEntity.get().getUserProfile().getCommunityInfoEntity().getDistrictID();
            return findCommunityArray(districtEntity);
        }
        return null;
    }

    private AreaList findCommunityArray(DistrictEntity districtEntity) {
        List<String> communityNameList = new ArrayList<>();
        List<CommunityInfoEntity> communityInfoList = communityInfoDao.findByDistrictName_toCommunity(districtEntity.getDistrictName());

        for (CommunityInfoEntity infoEntity : communityInfoList) {
            if (infoEntity.getGridEntity() == null) {
                communityNameList.add(infoEntity.getCommunityName());
            }
        }
        String[] districtNameArray = communityNameList.toArray(new String[0]);
        return new AreaList(districtEntity.getDistrictName(), districtNameArray);
    }

    /**
     * 场景：系统管理员在修改网格员管理区域之前，首先要获取所有的区域数据信息，然后在其中选择区域分配网格员
     * 重点：这里我们返回的区域是，还没有被划分的区域，如果某一区域已经被分配，那么不返回该区域信息
     */
    @Override
    public List<AreaList> getAllAreaList() {
        Iterable<DistrictEntity> districtEntities = districtDao.findAll();
        List<AreaList> areaLists = new ArrayList<>();
        for (DistrictEntity districtEntity : districtEntities) {
            AreaList areaList = findCommunityArray(districtEntity);
            areaLists.add(areaList);
        }
        return areaLists;
    }

    /**
     * 新增网格员接口
     * 同时系统管理员为网格员分配管理区域
     */
    @Override
    public boolean addGrid(CreateGridInfo createGridInfo) {
        String userID = createGridInfo.getUserID();
        Optional<UserEntity> userEntity = userDao.findOneByUserID(userID);

        //在添加网格员之前，如果这位用户已经是网格员了，就不能再进行一下操作
        //所以我们通过判断用户userType的方法，事先进行一个身份信息判断
        if (userEntity.isPresent() && userEntity.get().getUserType() != 1) {
            userEntity.get().setUserType(1);
            userDao.save(userEntity.get());

            GridEntity gridEntity = new GridEntity();
            gridEntity.setUserEntity(userEntity.get());
            gridEntity.setDistrictName(createGridInfo.getAreaList().getDistrictName());
            gridDao.save(gridEntity);

            String[] communityArray = createGridInfo.getAreaList().getCommunityArray();
            modifyGridIdOfCommunity(communityArray,gridEntity);
            return true;
        }
        return false;
    }

    /**
     * 返回当前系统管理员添加的网格员数据列表
     */
    @Override
    public PageQuery<GridPersonalInfo> getAllGrids(String page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, pageSize, Sort.by("id").ascending());
        Iterable<GridEntity> gridEntityIterable = gridDao.findAll(pageable);
        List<GridPersonalInfo> gridPersonalInfoList = new ArrayList<>();
        int size = 0;

        for (GridEntity gridEntity : gridEntityIterable) {
            String districtName = gridEntity.getDistrictName();
            String[] communityArray = communityInfoDao.findByDistrictID_DistrictName(districtName);
            AreaList areaList = new AreaList(districtName, communityArray);

            UserEntity userEntity = gridEntity.getUserEntity();
            Long id = userEntity.getId();
            String telPhone = userEntity.getUserProfile().getTelPhone();
            String userName = userEntity.getUserProfile().getUserName();
            GridPersonalInfo gridPersonalInfo = new GridPersonalInfo(id, userName, telPhone, areaList);

            gridPersonalInfoList.add(gridPersonalInfo);
            size++;
        }
        return CalculatePageUtils.getPageInfo(size, pageSize, pageable, gridPersonalInfoList);
    }

    /**
     * 场景：在系统管理员获取到网格员数据信息列表之后，可以进行两项操作：
     * 选择修改任意网格员管理区域、是否删除该网格员（user表中任然存在，只是grid表中不存在了）
     * 功能：该接口进行管理区域修改
     * id是网网格员的id
     */
    @Override
    public boolean modifyGrid(AreaList areaList,Long id) {
        Optional<GridEntity> gridEntity = gridDao.findById(id);
        String[] communityArray = areaList.getCommunityArray();

        if (gridEntity.isPresent()) {
            //将该网格员之前所负责管理的区域置空（释放区域管理权）
            List<CommunityInfoEntity> communityOldList = communityInfoDao.findByGridEntity(gridEntity.get());
            for (CommunityInfoEntity infoEntity : communityOldList) {
                infoEntity.setGridEntity(null);
                communityInfoDao.save(infoEntity);
            }

            modifyGridIdOfCommunity(communityArray,gridEntity.get());
            return true;
        }
        return false;
    }

    /**
     * 功能：删除某一网格员（user表中任然存在，只是grid表中不存在了）
     */
    @Override
    public boolean deleteOneGrid(Long id) {
        Optional<GridEntity> gridEntity = gridDao.findById(id);
        if (gridEntity.isPresent()) {
            String districtName = gridEntity.get().getDistrictName();
            List<CommunityInfoEntity> communityInfoList = communityInfoDao.findByDistrictName_toCommunity(districtName);
            for (CommunityInfoEntity infoEntity : communityInfoList) {
                infoEntity.setGridEntity(null);
            }

            gridEntity.get().getUserEntity().setUserType(0);
            userDao.save(gridEntity.get().getUserEntity());
            gridEntity.get().setUserEntity(null);
            gridDao.save(gridEntity.get());
            gridDao.delete(gridEntity.get());
            return true;
        }
        return false;
    }

    /**
     * 功能：分配网格员管理区域(使用在管理区域修改、新增网格员功能接口中)
     * 注意点：现实中可能出现这种情况，在不同区之间，可能存在着相同的小区名，
     *         所以我们在分配管理区域时，事先一定要确定district，然后再进行比对、分配。
     */
    public void modifyGridIdOfCommunity(String[] communityArray,GridEntity gridEntity){
        List<String> list = Arrays.asList(communityArray);
        //在将字符串数组转化成List集合时，Arrays.asList()返回的是Arrays的内部类ArrayList，而不是java.util.ArrayList，
        //所以在使用remove、add等方法是会产生异常“UnsupportedOperationException”，因此我们要进行如下操作。
        List<String> communityList = new ArrayList<>(list);
        List<CommunityInfoEntity> communityInfoAllList = communityInfoDao.findByDistrictName_toCommunity(gridEntity.getDistrictName());
        for (CommunityInfoEntity infoEntity : communityInfoAllList) {
                /*对于communityList中已经匹配的community，下一次我们不需要再次匹配，所以在每次匹配成功之后，
                将其从communityList中移除，这样可以降低代码的时间复杂度。*/
            if (!communityList.isEmpty()) {
                for (String community : communityList) {
                    // 从该网格员所位于的district中，遍历所有的community与最新分配的community进行比较，
                    // 若communityList中存在，则重新设置gridID值
                    if (infoEntity.getCommunityName().equals(community)) {
                        infoEntity.setGridEntity(gridEntity);
                        communityInfoDao.save(infoEntity);
                        communityList.remove(community);
                        break;
                    }
                }
            }else {
                break;
            }
        }
    }

}
