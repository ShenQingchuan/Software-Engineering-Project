<template>
  <div
    class="subpage-officer-manage"
    v-bp-default="[
      userInfo.id,
      '访问了网格员管理页面',
      '页面访问',
      $route.path
    ]"
  >
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
          &nbsp;
          <el-popconfirm
            title="确定删除网格员？"
            @onConfirm="handleDeleteOfficer(scope)"
          >
            <el-button size="small" type="text" slot="reference"
              >删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div class="flex-box pagination">
      共 {{ totalSize }} 条记录
      <el-pagination :total="totalSize" layout="prev, pager, next">
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
            @click="
              () => {
                showEditCard = false;
              }
            "
            style="float: right; padding: 3px 0; margin-left: 10px;"
            type="text"
            >取消</el-button
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
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
// import officerManageTableDataMock from "@/mock/officerManageTableData";
// import getOptionsWhileEditingOfficerScopeMock from "@/mock/getOptionsWhileEditingOfficerScope";

export default {
  name: "officerManage",
  data() {
    return {
      totalSize: 0,
      officerList: [],

      showEditCard: false,
      gotOptions: {},
      editing: {}
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  async mounted() {
    try {
      const res = await this.$axios.get(`/admin/getGrids?page=1`);
      resErrorHandler(this, res);
      if (res.data.resultCode === "200") {
        this.officerList = res.data.data.dataList;
        this.totalSize = res.data.data.totalSize;
        this.$message.success("获取网格员资料成功！");
      }
    } catch (err) {
      this.$message.error(String(err));
    }
  },
  methods: {
    // eslint-disable-next-line no-unused-vars
    async handleEditOfficer(scope) {
      try {
        this.showEditCard = true;
        this.editing = scope.row;
        this.gotOptions.communityArray = [
          ...this.editing.areaList.communityArray
        ];

        const res = await this.$axios.get(
          `/admin/getAreaList?userID=${scope.row.userID}`
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("获取该用户对应区域成功");
          if (res.data.data.communityArray.length === 0) {
            setTimeout(() => {
              this.$message.warning("已经没有可以分配的小区了！");
            }, 0);
            return;
          }
          this.gotOptions.communityArray = [
            ...this.gotOptions.communityArray,
            ...res.data.data.communityArray
          ];
          this.$forceUpdate();
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    },
    // eslint-disable-next-line no-unused-vars
    async handleDeleteOfficer(scope) {
      try {
        console.log(`删除 用户id: ${scope.row.id}`);
        const res = await this.$axios.delete(
          `/admin/deleteOneGrid/${scope.row.id}`
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("删除网格员成功！");
          this.officerList.splice(this.officerList.indexOf(scope.row), 1);
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    },
    async submitEdit() {
      try {
        const res = await this.$axios.put(
          `/admin/modifyAreaOfGrid/${this.editing.id}`,
          {
            communityArray: this.editing.areaList.communityArray,
            districtName: this.editing.areaList.districtName
          }
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("更改管理区域范围成功！");
          this.showEditCard = false; // 请求成功结束后隐藏卡片
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
