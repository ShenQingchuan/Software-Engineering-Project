<template>
    <div
            class="page-log-manage"
            v-bp-default="[userInfo.id, '访问了日志管理页面', '页面访问', $route.path]"
    >
        <div class="flex-box log-actions">
            <el-button
                    @click="
          () => {
            $router.push('./addLog');
          }
        "
                    icon="el-icon-plus"
                    plain
                    type="success"
        >新增日志</el-button
      >
    </div>
    <div class="flex-box log-table-container">
      <el-table class="log-table" :data="logData">
        <el-table-column fixed label="日志标题" prop="titleName">
          <template slot-scope="scope">
            <b>{{ scope.row.titleName }}</b>
          </template>
        </el-table-column>
        <el-table-column fixed label="日志类型" prop="typeName" />
        <el-table-column fixed label="日志创建时间" prop="createTime" />
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button @click="handleEdit(scope.row)" size="small" type="text"
              >查看内容
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="flex-box pagination">
      共 {{ totalSize }} 条记录
      <el-pagination :total="totalSize" layout="prev, pager, next">
      </el-pagination>
    </div>

    <el-dialog
      title="提示"
      :visible.sync="showContentDialog.isShow"
      width="30%"
      center
    >
      <span slot="title"
        ><b>日志：{{ showContentDialog.title }}</b></span
      >
      <p v-html="showContentDialog.content"></p>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeShowContentDialog"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
// import logMock from "@/mock/logs";
import resErrorHandler from "@/utils/resErrorHandler";

import { mapState } from "vuex";

export default {
  name: "logManage",
  computed: {
    ...mapState(["userInfo"])
  },
  async mounted() {
    try {
      this.loadingTable = true;
      const res = await this.$axios.get(
        `/grid/getJournalList/${this.userInfo.id}?page=1`
      );
      resErrorHandler(this, res);
      if (res.data.data && res.data.data.dataList.length > 0) {
        this.logData = res.data.data.dataList;
        this.totalSize = res.data.data.totalSize;
      }
      this.loadingTable = false;
    } catch (err) {
      this.$message.error(String(err));
    }
  },
  data() {
    return {
      loadingTable: false,
      queryForm: {
        dateRange: "",
        logType: ""
      },
      totalSize: 0,
      logData: [],
      showContentDialog: {
        isShow: false,
        title: "",
        content: ""
      }
    };
  },
  methods: {
    async handleEdit(row) {
      try {
        const log = await this.$axios.get(`/grid/getJournalContent/${row.id}`);
        resErrorHandler(this, log);
        if (log.data.resultCode === "200") {
          this.$message.success("获取日志详情成功！");
          this.showContentDialog.title = row.titleName;
          this.showContentDialog.content = log.data.data;
          this.showContentDialog.isShow = true;
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    },
    closeShowContentDialog() {
      this.showContentDialog = {
        isShow: false,
        title: "",
        content: ""
      };
    }
  }
};
</script>

<style lang="scss" scoped>
.page-log-manage {
  margin-top: 20px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
  padding: 20px;

  .log-table-container {
    width: 90%;
    margin: 20px auto;

    .log-table {
      width: 100%;
    }
  }
}
</style>
