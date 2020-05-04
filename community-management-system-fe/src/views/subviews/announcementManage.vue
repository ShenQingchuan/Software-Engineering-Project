<template>
  <div class="page-announcement-manage">
    <div class="flex-box announcement-actions">
      <el-button
        @click="
          () => {
            $router.push('./addAnnouncement');
          }
        "
        icon="el-icon-plus"
        plain
        type="success"
        >新增公告</el-button
      >
    </div>
    <div class="flex-box announcement-table-container">
      <el-table :data="announcementData">
        <el-table-column fixed label="公告标题" prop="title" width="300">
          <template slot-scope="scope">
            <b>{{ scope.row.title }}</b>
          </template>
        </el-table-column>
        <el-table-column fixed label="公告内容" prop="content" width="600">
          <template slot-scope="scope">
            {{ scope.row.content.slice(0, 40) }} ...
          </template>
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
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button @click="handleDelete(scope.row)" size="small" type="text"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="flex-box pagination">
      共 {{ announcementCount }} 条记录
      <el-pagination :total="announcementCount" layout="prev, pager, next">
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
      announcementData: announcementMock
    };
  },
  methods: {
    handleDelete(row) {
      // TODO: 实现删除日志
      this.$message.warning("TODO: 实现删除日志");
      this.announcementData.splice(row, 1);
    }
  }
};
</script>

<style lang="scss" scoped>
.page-announcement-manage {
  margin-top: 20px;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
  padding: 20px;

  .announcement-table-container {
    width: 90%;
    margin: 20px auto;
  }
}
</style>
