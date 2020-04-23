<template>
  <div class="page-announcement-manage">
    <div class="flex-box announcement-actions">
      <el-form class="announcement-manage-form" inline>
        <el-form-item label="公告时间：">
          <el-date-picker
            end-placeholder="结束日期"
            range-separator="至"
            start-placeholder="开始日期"
            type="daterange"
            v-model="queryForm.dateRange"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="公告类型：">
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
        <el-button icon="el-icon-plus" plain type="success">新增日志</el-button>
      </el-form>
    </div>
    <div class="flex-box announcement-table-container">
      <el-table :data="announcementData">
        <el-table-column fixed label="公告标题" prop="title" width="300">
          <template slot-scope="scope">
            <b>{{ scope.row.title }}</b>
          </template>
        </el-table-column>
        <el-table-column fixed label="公告内容" prop="content" width="500">
          <template slot-scope="scope">
            {{ scope.row.content.slice(0, 40) }} ...
          </template>
        </el-table-column>
        <el-table-column fixed label="创建者" prop="creatorName" width="200">
        </el-table-column>
        <el-table-column
          fixed
          label="公告创建时间"
          prop="createTime"
          width="200"
        >
          <template slot-scope="scope">
            {{
              $moment(new Date(scope.row.createTime * 1000))
                .locale("zh-cn")
                .format("lll")
            }}
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button @click="handleEdit(scope.row)" size="small" type="text"
              >编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="flex-box pagination">
      共 {{ announcementCount }} 条记录
      <el-pagination :total="totalPage" layout="prev, pager, next">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import announcementMock from "@/mock/announcement";

export default {
  name: "announcementManage",
  data() {
    return {
      logOptions: [
        {
          value: "system",
          label: "系统公告"
        },
        {
          value: "resident",
          label: "居民通知"
        },
        {
          value: "manager",
          label: "管理任务"
        }
      ],
      queryForm: {
        dateRange: "",
        logType: ""
      },
      announcementCount: 61,
      totalPage: 70,
      announcementData: announcementMock
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
.page-announcement-manage {
  margin-top: 20px;

  .announcement-table-container {
    width: 90%;
    margin: 20px auto;
  }

  .pagination {
    width: 65%;
    margin: 0 auto;
    font-size: 14px;
    font-weight: bold;
  }
}
</style>
