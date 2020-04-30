<template>
  <div class="subpage-officer-manage tb-gap flex flex-col">
    <div class="search-form flex-box">
      <label>要搜索的用户：</label>
      <el-input v-model="uid" placeholder="请输入用户 ID" />
      <el-button class="query-submit" plain type="primary">搜索</el-button>
    </div>

    <el-card class="info-card text-align-left tb-gap" shadow="hover">
      <h3>个人信息：</h3>
      <div class="info-list flex-box jy-start">
        <div class="info-item flex-box">
          <label>姓名：</label>
          {{ searchResult.userEntity.userProfile.userName }}
        </div>
        <div class="info-item flex-box">
          <label>性别：</label>
          {{ searchResult.userEntity.userProfile.sex ? "男" : "女" }}
        </div>
        <div class="info-item flex-box">
          <label>联系方式：</label>
          {{ searchResult.userEntity.userProfile.telphone }}
        </div>
        <div class="info-item flex-box">
          <label>民族：</label> {{ searchResult.userEntity.userProfile.nation }}
        </div>
        <div class="info-item flex-box">
          <label>文化程度：</label>
          {{ searchResult.userEntity.userProfile.degreeOfEducation }}
        </div>
        <div class="info-item flex-box">
          <label>政治面貌：</label>
          {{ searchResult.userEntity.userProfile.politicCountenance }}
        </div>
      </div>
    </el-card>

    <div class="tb-gap">
      <h3 class="tb-gap">选择网格管理范围：</h3>
      <el-checkbox-group v-model="selectCommunity">
        <el-checkbox
          v-for="(e, i) in searchResult.communityArray"
          :key="i"
          :label="e"
        ></el-checkbox>
      </el-checkbox-group>
    </div>
  </div>
</template>

<script>
import officerManageSearchResultMock from "../../mock/officerManageSearchResult";

export default {
  name: "officerManage",
  data() {
    return {
      uid: "",
      selectCommunity: [],
      searchResult: officerManageSearchResultMock
    };
  }
};
</script>

<style lang="scss" scoped>
.subpage-officer-manage {
  width: 70%;
  margin: 0 auto;

  .search-form {
    label {
      flex: 1;
      min-width: 200px;
      font-weight: bold;
    }
    .query-submit {
      margin-left: 20px;
      font-size: 16px;
    }
  }

  .info-card {
    padding-left: 20px;
    .info-list {
      padding: 10px;
      .info-item {
        label {
          font-weight: bold;
        }

        width: fit-content;
        margin: 0 5px;
        background-color: rgba(0, 0, 0, 0.08);
        padding: 5px 10px;
        border-radius: 5px;
      }
    }
  }
}
</style>
