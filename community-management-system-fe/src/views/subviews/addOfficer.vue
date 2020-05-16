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
          {{ searchUserResult.userProfile.userName }}
        </div>
        <div class="info-item flex-box">
          <label>性别：</label>
          {{ searchUserResult.userProfile.sex ? "男" : "女" }}
        </div>
        <div class="info-item flex-box">
          <label>联系方式：</label>
          {{ searchUserResult.userProfile.telphone }}
        </div>
        <div class="info-item flex-box">
          <label>民族：</label> {{ searchUserResult.userProfile.nation }}
        </div>
        <div class="info-item flex-box">
          <label>文化程度：</label>
          {{ searchUserResult.userProfile.degreeOfEducation }}
        </div>
        <div class="info-item flex-box">
          <label>政治面貌：</label>
          {{ searchUserResult.userProfile.politicCountenance }}
        </div>
      </div>
      <div class="tb-gap commit-action flex-box jy-center">
        <el-button @click="getOptionsList" type="success" plain
          >确认设置 他/她 为网格员</el-button
        >
      </div>
    </el-card>

    <div class="tb-gap" v-show="showOptionsList">
      <h3 class="tb-gap">选择网格管理范围：</h3>
      <el-checkbox-group v-model="selectCommunity">
        <el-checkbox
          v-for="(e, i) in communityOptionsList.communityArray"
          :key="i"
          :label="e"
        ></el-checkbox>
      </el-checkbox-group>
    </div>

    <div class="tb-gap">
      <el-button @click="submitUserQuery" type="primary" plain
        >确认赋予网格员权限</el-button
      >
    </div>
  </div>
</template>

<script>
import officerManageSearchResultMock from "../../mock/officerManageSearchResult";
import resErrorHandler from "../../utils/resErrorHandler";

export default {
  name: "officerManage",
  data() {
    return {
      uid: "",
      selectCommunity: [],
      searchUserResult: officerManageSearchResultMock.userEntity,

      showOptionsList: false,
      communityOptionsList: officerManageSearchResultMock.options
    };
  },
  methods: {
    async submitUserQuery() {
      // 先查询某用户基本资料 /admin/getGridProfile ，先不展示可选的小区列表，要等管理员确认无误点击按钮后再请求 /getAreaList
      const userRes = await this.$axios.get(
        `/admin/getGridProfile?userID=${this.uid}`
      );
      resErrorHandler(this, userRes);
      if (userRes.data.resultCode === "200") {
        this.$message.success("成功查询到该用户资料！");
        console.log(userRes.data);
      }
    },
    async getOptionsList() {
      // TODO: 获取 可选的小区列表
      const res = await this.$axios.get(
        `/admin/getAreaList?userID=${this.uid}`
      );
      resErrorHandler(this, res);
      if (res.data.resultCode === "200") {
        this.$message.success("获取该用户对应区域成功");
        console.log(res);
      }

      this.showOptionsList = true;
    }
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
        min-width: max-content;
        margin: 0 5px;
        background-color: rgba(0, 0, 0, 0.08);
        padding: 5px 10px;
        border-radius: 5px;
      }
    }
  }
}
</style>
