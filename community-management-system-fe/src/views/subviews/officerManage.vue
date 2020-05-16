<template>
  <div class="subpage-officer-manage">
    <el-table :data="officerList">
      <el-table-column fixed label="姓名" prop="userName"></el-table-column>
      <el-table-column fixed label="联系方式" prop="telPhone"></el-table-column>
      <el-table-column fixed label="管理区域">
        <template slot-scope="scope">
          <b
            >{{ scope.row.areaList.districtName }} -
            {{ scope.row.areaList.communityArray.join("/") }}</b
          >
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button @click="handleEditOfficer(scope)" size="small" type="text"
            >修改管理区域
          </el-button>
          <el-button
            @click="handleDeleteOfficer(scope)"
            size="small"
            type="text"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="flex-box pagination">
      共 {{ officerCount }} 条记录
      <el-pagination :total="officerCount" layout="prev, pager, next">
      </el-pagination>
    </div>

    <div class="tb-gap text-align-left" v-if="showEditCard">
      <el-card class="box-card">
        <div slot="header">
          <span
            ><b
              >修改 <span>{{ editing.userName }}</span> 的管理区域</b
            ></span
          >
          <el-button
            @click="submitEdit"
            style="float: right; padding: 3px 0"
            type="text"
            >确认提交</el-button
          >
        </div>
        <div class="flex-box flex-col jy-start">
          <h3 class="tb-gap text-align-left width-100-percent">
            管理区域：{{ editing.areaList.districtName }}
          </h3>
          <el-checkbox-group v-model="editing.areaList.communityArray">
            <el-checkbox
              v-for="(e, i) in gotOptions.communityArray"
              :key="i"
              :label="e"
            ></el-checkbox>
          </el-checkbox-group>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import officerManageTableDataMock from "@/mock/officerManageTableData";
import getOptionsWhileEditingOfficerScopeMock from "@/mock/getOptionsWhileEditingOfficerScope";

export default {
  name: "officerManage",
  data() {
    return {
      officerCount: 88,
      officerList: officerManageTableDataMock,

      showEditCard: false,
      gotOptions: {},
      editing: {}
    };
  },
  methods: {
    // eslint-disable-next-line no-unused-vars
    handleEditOfficer(scope) {
      this.showEditCard = true;
      this.editing = scope.row;
      this.editing.areaList.communityArray = []; // 清空原管理区域数组

      // TODO: 根据 this.editing.userID（正要编辑的那个网格员的身份证号）去调用 /admin/getAreaList
      // https://easydoc.xyz/doc/43159074/MAhLR20e/b7E6okvI
      // 下面是这里暂时假装获得了数据：（选取我们界面上的第一个做演示）
      this.gotOptions = getOptionsWhileEditingOfficerScopeMock;
    },
    // eslint-disable-next-line no-unused-vars
    handleDeleteOfficer(scope) {
      // TODO: 删除网格员  https://easydoc.xyz/doc/43159074/MAhLR20e/gZNThCm9
    },
    submitEdit() {
      // TODO: 修改管理区域  https://easydoc.xyz/doc/43159074/MAhLR20e/LQWiAz7Y
      this.showEditCard = false; // 请求成功结束后隐藏卡片
    }
  }
};
</script>

<style lang="scss" scoped>
.subpage-officer-manage {
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
  padding: 20px;
  width: 85%;
  margin: 0 auto;

  .box-card {
    width: 50%;
    b {
      span {
        color: cadetblue;
      }
    }
  }
}
</style>
