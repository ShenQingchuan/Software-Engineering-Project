<template>
  <div class="page-log-manage">
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
            <el-button @click="() => {}" size="small" type="text"
              >查看
            </el-button>
            <el-button @click="handleEdit(scope.row)" size="small" type="text"
              >编辑
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
  </div>
</template>

<script>
import logMock from "@/mock/logs";
import { mapState } from "vuex";

export default {
  name: "logManage",
  computed: {
    ...mapState(["userInfo"])
  },
  async mounted() {
    try {
      const res = await this.$axios.get(
        `/grid/getJournalList/${this.userInfo.id}?page=1`
      );
      this.logData = res.data.data.dataList;
      this.totalSize = res.data.data.totalSize;
    } catch (err) {
      this.$message.error(String(err));
    }
  },
  data() {
    return {
      logOptions: [
        {
          value: "survey",
          label: "调查走访"
        },
        {
          value: "patrol",
          label: "巡逻治安"
        },
        {
          value: "logistical",
          label: "基础设施养护"
        },
        {
          value: "summary",
          label: "阶段工作总结"
        }
      ],
      queryForm: {
        dateRange: "",
        logType: ""
      },
      totalSize: 0,
      logData: logMock
    };
  },
  methods: {
    handleEdit(row) {
      console.log(row);
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
