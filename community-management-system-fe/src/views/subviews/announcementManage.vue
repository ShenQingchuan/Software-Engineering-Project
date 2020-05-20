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
    <div v-loading="loadingTable" class="flex-box announcement-table-container">
      <el-table :data="announcementData">
        <el-table-column fixed label="公告标题" prop="title" width="200">
          <template slot-scope="scope">
            <b>{{ scope.row.titleName }}</b>
          </template>
        </el-table-column>
        <el-table-column fixed label="公告内容" prop="content" width="400">
          <template slot-scope="scope">
            {{ delHtmlTag(scope.row.content).slice(0, 40) }} ...
          </template>
        </el-table-column>
        <el-table-column
          fixed
          label="公告创建时间"
          prop="createTime"
          width="200"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button @click="handleDelete(scope.row)" size="small" type="text"
              >删除
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
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
// import announcementMock from "@/mock/announcement";

export default {
  name: "announcementManage",
  computed: {
    ...mapState(["userInfo"])
  },
  data() {
    return {
      loadingTable: false,
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
      totalPage: 0,
      totalSize: 0,
      announcementData: []
    };
  },
  methods: {
    async handleDelete(row) {
      this.$message.warning("实现删除日志"); // TODO: 实现删除日志
      this.announcementData.splice(row, 1);
    },
    delHtmlTag(str) {
      return str.replace(/<[^>]+>/g, "");
    }
  },
  async mounted() {
    this.loadingTable = true;
    const res = await this.$axios.get(
      `/grid/getAnnouncementList/${this.userInfo.id}?page=1`
    );
    console.log(res);
    resErrorHandler(this, res);
    if (
      res.data.resultCode === "200" &&
      res.data.data &&
      res.data.data.dataList.length > 0
    ) {
      this.announcementData = res.data.data.dataList;
      this.totalSize = res.data.data.totalSize;
      this.totalPage = res.data.data.totalPage;
    }
    this.loadingTable = false;
  }
};
</script>

<style lang="scss" scoped>
.page-announcement-manage {
  width: 75%;
  margin-top: 20px;
  margin-left: auto;
  margin-right: auto;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
  padding: 20px;

  .announcement-table-container {
    width: 100%;
    margin: 20px auto;
  }
}
</style>
