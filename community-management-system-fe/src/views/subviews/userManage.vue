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
          v-model="userManageQueryForm.userId"
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
    <el-button icon="el-icon-search" plain type="primary">查询</el-button>
    <el-button
      @click="() => $router.push('/dashboard/officerAddUser')"
      icon="el-icon-plus"
      plain
      type="success"
      >添加用户</el-button
    >
    <el-button icon="el-icon-tickets" plain type="primary"
      >导出用户数据表
    </el-button>
    <el-table class="user-manage-data-table" :data="userManageTableData" border>
      <el-table-column fixed prop="districtName" label="归属地区" width="200">
      </el-table-column>
      <el-table-column prop="communityName" label="归属小区" width="200">
      </el-table-column>
      <el-table-column prop="userID" label="用户ID" width="200">
      </el-table-column>
      <el-table-column prop="userName" label="姓名" width="200">
      </el-table-column>
      <el-table-column prop="telPhone" label="手机" width="200">
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small"
            >查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="user-manage-pagination flex-box pagination">
      共 {{ totalSize }} 条记录
      <el-pagination :total="totalSize" layout="prev, pager, next">
      </el-pagination>
    </div>
  </div>
</template>

<script>
// import userManageTableDataMock from "@/mock/userManageTableData";
import { mapState } from "vuex";

export default {
  name: "userManage",
  async mounted() {
    const res = await this.$axios.get(
      // /{id}
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
        userId: "",
        userName: ""
      },
      userRecordCount: 65,
      totalSize: 70,
      userManageTableData: []
    };
  },
  methods: {
    handleClick(row) {
      console.log(row);
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
</style>
