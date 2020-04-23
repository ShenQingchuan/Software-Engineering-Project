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
      <el-button icon="el-icon-plus" plain type="success">添加用户</el-button>
      <el-button icon="el-icon-tickets" plain type="primary"
      >导出用户数据表
      </el-button
      >
      <el-table
              class="user-manage-data-table"
              :data="userManageTableData"
              border
              style="width: 100%"
      >
          <el-table-column fixed prop="area" label="归属地区" width="200">
          </el-table-column>
          <el-table-column prop="community" label="归属小区" width="200">
          </el-table-column>
      <el-table-column prop="userId" label="用户ID" width="200">
      </el-table-column>
      <el-table-column prop="userName" label="姓名" width="200">
      </el-table-column>
      <el-table-column prop="phone" label="手机" width="200"> </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small"
            >查看
          </el-button>
          <el-button type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import userManageTableData from "@/mock/userManageTableData";

export default {
  name: "userManage",
  data() {
    return {
      activeTab: "stuff",

      userManageQueryForm: {
        community: "",
        userId: "",
        userName: ""
      },
      userManageTableData
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
  margin-top: 20px;
}
</style>
