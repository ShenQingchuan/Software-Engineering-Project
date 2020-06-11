<template>
  <div
    class="subpage-officer-manage tb-gap flex flex-col"
    v-bp-default="[
      userInfo.id,
      '访问了添加网格员页面',
      '页面访问',
      $route.path
    ]"
  >
    <div class="search-form flex-box">
      <label>要搜索的用户：</label>
      <el-input v-model="userID" placeholder="请输入用户 ID" />
      <el-button
        @click="submitUserQuery"
        class="query-submit"
        plain
        type="primary"
        >搜索
      </el-button>
    </div>

    <el-card
      v-if="searchUserResult.userName !== undefined"
      class="info-card text-align-left tb-gap flex-wrap"
      shadow="hover"
    >
      <h3>个人信息：</h3>
      <div class="info-list flex-box jy-start flex-wrap">
        <div class="info-item flex-box">
          <label>姓名：</label>
          {{ searchUserResult.userName }}
        </div>
        <div class="info-item flex-box">
          <label>所属网格：</label>
          {{ searchUserResult.ofGrid.districtName }}-{{
            searchUserResult.ofGrid.communityName
          }}
        </div>
        <div class="info-item flex-box">
          <label>性别：</label>
          {{ searchUserResult.sex ? "男" : "女" }}
        </div>
        <div class="info-item flex-box">
          <label>联系方式：</label>
          {{ searchUserResult.telPhone }}
        </div>
        <div class="info-item flex-box">
          <label>民族：</label> {{ searchUserResult.nation }}
        </div>
        <div class="info-item flex-box">
          <label>文化程度：</label>
          {{ searchUserResult.degreeOfEducation }}
        </div>
        <div class="info-item flex-box">
          <label>政治面貌：</label>
          {{ searchUserResult.politicCountenance }}
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

    <div v-if="step === 2" class="tb-gap">
      <el-button @click="submitAddOfficer" type="primary" plain
        >确认赋予网格员权限</el-button
      >
    </div>
  </div>
</template>

<script>
import resErrorHandler from "../../utils/resErrorHandler";
import { mapState } from "vuex";
// import officerManageSearchResultMock from "../../mock/officerManageSearchResult";

export default {
  name: "officerManage",
  data() {
    return {
      userID: "",
      selectCommunity: [],
      searchUserResult: {},
      step: 0,

      showOptionsList: false,
      communityOptionsList: {}
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  methods: {
    async submitUserQuery() {
      if (!/\d{18}/.test(this.userID)) {
        this.$message.error("身份证输入格式出错！");
        return;
      }
      // 先查询某用户基本资料 /admin/getGridProfile ，先不展示可选的小区列表，要等管理员确认无误点击按钮后再请求 /getAreaList
      const userRes = await this.$axios.get(
        `/admin/getGridProfile?userID=${this.userID}`
      );
      resErrorHandler(this, userRes);
      if (userRes.data.resultCode === "200") {
        this.$message.success("成功查询到该用户资料！");
        this.searchUserResult = userRes.data.data;
        this.step = 1;
      }
    },
    async getOptionsList() {
      const res = await this.$axios.get(
        `/admin/getAreaList?userID=${this.userID}`
      );
      resErrorHandler(this, res);
      if (res.data.resultCode === "200") {
        this.$message.success("获取该用户对应区域成功");
        console.log(res.data);
        if (res.data.data.communityArray.length === 0) {
          setTimeout(() => {
            this.$message.warning("已经没有可以分配的小区了！");
          }, 0);
          return;
        }
      }

      this.showOptionsList = true;
      this.communityOptionsList = res.data.data;
      this.step++;
    },
    async submitAddOfficer() {
      try {
        const { userID, selectCommunity } = this;
        const res = await this.$axios.post(`/admin/addGrid`, {
          userID,
          areaList: {
            districtName: this.communityOptionsList.districtName,
            communityArray: selectCommunity
          }
        });
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("添加网格员成功！");
          await this.$router.push("/dashboard/officerManage");
        }
      } catch (err) {
        this.$message.error(String(err));
      }
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
        margin: 5px;
        background-color: rgba(0, 0, 0, 0.08);
        padding: 5px 10px;
        border-radius: 5px;
      }
    }
  }
}
</style>
