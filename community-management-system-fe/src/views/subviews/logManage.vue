<template>
  <div class="page-log-manage">
    <div class="flex-box log-actions">
      <el-form class="log-manage-form" inline>
        <el-form-item label="日志发布时间：">
          <el-date-picker
            end-placeholder="结束日期"
            range-separator="至"
            start-placeholder="开始日期"
            type="daterange"
            v-model="queryForm.dateRange"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="日志类型：">
          <el-select placeholder="请选择" v-model="queryForm.logType">
            <el-option
              :key="item.value"
              :label="item.label"
              :value="item.value"
              v-for="item in logOptions"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-button icon="el-icon-search" plain type="primary"
          >执行查询
        </el-button>
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
      </el-form>
    </div>
    <div class="flex-box log-table-container">
      <el-table class="log-table" :data="logData">
        <el-table-column fixed label="日志内容" prop="content">
          <template slot-scope="scope">
            <b>{{ scope.row.content.slice(0, 40) }} ...</b>
          </template>
        </el-table-column>
        <el-table-column fixed label="创建者" prop="creatorName">
        </el-table-column>
        <el-table-column fixed label="日志创建时间" prop="createTime">
          <template slot-scope="scope">
            {{
              $moment(new Date(scope.row.createTime * 1000))
                .locale("zh-cn")
                .format("lll")
            }}
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button @click="handleEdit(scope.row)" size="small" type="text"
              >编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="flex-box pagination">
      共 {{ logCount }} 条记录
      <el-pagination :total="totalPage" layout="prev, pager, next">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import logMock from "@/mock/logs";

export default {
  name: "logManage",
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
      logCount: 61,
      totalPage: 70,
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

  .log-table-container {
    width: 90%;
    margin: 20px auto;

    .log-table {
      width: 100%;
    }
  }
}
</style>
