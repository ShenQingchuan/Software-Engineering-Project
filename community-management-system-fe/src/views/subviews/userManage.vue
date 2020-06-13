<template>
  <div class="subpage-user-manage">
    <el-form inline class="user-manage-query-form">
      <el-form-item label="归属小区: ">
        <el-input
          v-model="userManageQueryForm.community"
          placeholder="请输入归属小区"
        ></el-input>
      </el-form-item>
      <el-form-item label="用户ID: ">
        <el-input
          v-model="userManageQueryForm.userID"
          placeholder="请输入用户ID"
        ></el-input>
      </el-form-item>
      <el-form-item label="姓名: ">
        <el-input
          v-model="userManageQueryForm.userName"
          placeholder="请输入姓名"
        ></el-input>
      </el-form-item>
    </el-form>
    <el-button @click="queryUser" icon="el-icon-search" plain type="primary"
      >查询</el-button
    >
    <el-button @click="goAddUserTab" icon="el-icon-plus" plain type="success"
      >添加用户
    </el-button>
    <el-table
      class="user-manage-data-table"
      width="1150"
      :data="userManageTableData"
      border
    >
      <el-table-column fixed prop="districtName" label="归属地区" width="200">
      </el-table-column>
      <el-table-column prop="communityName" label="归属小区" width="200">
      </el-table-column>
      <el-table-column prop="userID" label="用户身份证号" width="309">
      </el-table-column>
      <el-table-column prop="userName" label="姓名" width="200">
      </el-table-column>
      <el-table-column prop="telPhone" label="手机" width="200">
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="100">
        <template slot-scope="scope">
          <el-button
            @click="checkUserProfile(scope.row)"
            type="text"
            size="small"
            >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div class="user-manage-pagination flex-box pagination">
      共 {{ totalSize }} 条记录
      <el-pagination :total="totalSize" layout="prev, pager, next">
      </el-pagination>
    </div>

    <el-dialog
      v-if="showUserInfo.profile.userName !== ''"
      :visible.sync="showUserInfo.isShow"
      width="30%"
    >
      <b slot="title">{{ `查看用户资料：${showUserInfo.profile.userName}` }}</b>
      <div class="show-info flex-box flex-col jy-start">
        <div class="show-info-line flex-box tb-gap">
          <label><b>姓名：</b></label> {{ showUserInfo.profile["userName"] }}
        </div>
        <div class="show-info-line flex-box tb-gap">
          <label><b>所属网格：</b></label>
          {{ showUserInfo.profile.ofGrid.districtName }} /
          {{ showUserInfo.profile.ofGrid.communityName }}
        </div>
        <div class="show-info-line flex-box tb-gap">
          <label><b>民族：</b></label> {{ showUserInfo.profile["nation"] }}
        </div>
        <div class="show-info-line flex-box tb-gap">
          <label><b>手机：</b></label> {{ showUserInfo.profile["telPhone"] }}
        </div>
        <div class="show-info-line flex-box tb-gap">
          <label><b>邮箱：</b></label> {{ showUserInfo.profile["email"] }}
        </div>
        <div class="show-info-line flex-box tb-gap">
          <label><b>血型：</b></label> {{ showUserInfo.profile["bloodType"] }}
        </div>
        <div class="show-info-line flex-box tb-gap">
          <label><b>学历水平：</b></label>
          {{ showUserInfo.profile["degreeOfEducation"] }}
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="showUserInfo.isShow = false"
          >关 闭</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
// import userManageTableDataMock from "@/mock/userManageTableData";
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
import { send } from "../../utils/burringPoint";

export default {
  name: "userManage",
  async mounted() {
    const res = await this.$axios.get(
      `/query/allUserOfGrid/${this.userInfo.id}?page=1`
    );
    console.log(res);
    this.userManageTableData = res.data.data.dataList;
    this.totalSize = res.data.data.totalSize;
  },
  computed: {
    ...mapState(["userInfo"])
  },
  data() {
    return {
      activeTab: "stuff",

      userManageQueryForm: {
        community: "",
        userID: "",
        userName: ""
      },
      userRecordCount: 65,
      totalSize: 70,
      userManageTableData: [],

      showUserInfo: {
        isShow: false,
        profile: {
          ofGrid: {}
        }
      }
    };
  },
  methods: {
    send,
    async queryUser() {
      await send([this.userInfo.id, "查询用户", "AJAX请求", this.$route.path]);
      try {
        const res = await this.$axios.post(
          `/query/multipleConditions/${this.userInfo.id}?page=1`,
          this.userManageQueryForm
        );
        resErrorHandler(this, res);
        if (
          res.data.resultCode === "200" &&
          res.data.data &&
          res.data.data.dataList.length > 0
        ) {
          this.$message.success("成功查询到该用户！");
          this.totalSize = res.data.data.totalSize;
          this.userManageTableData = res.data.data.dataList;
        } else {
          this.$nextTick(() => {
            this.$message.warning("没有查询到该用户！");
          });
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    },
    async checkUserProfile(row) {
      try {
        const res = await this.$axios.get(`/profile/getProfile/${row.id}`);
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("成功获取用户资料！");
          this.showUserInfo.profile = res.data.data;
          this.showUserInfo.isShow = true;
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    },
    async goAddUserTab() {
      await this.$router.push("/dashboard/officerAddUser");
      await send([this.userId, "准备添加用户", "页面切换", this.$route.path]);
    }
  }
};
</script>

<style lang="scss" scoped>
.user-manage-data-table {
  margin: 20px auto;
  width: 80%;
}
.user-manage-pagination {
  width: 80%;
  margin: 0 auto;
}
.show-info {
  width: 100%;
  font-size: 18px;
  &-line {
    width: 100%;
  }
}
</style>
