<template>
  <div
    class="subpage-search"
    v-bp-default="[userInfo.id, '访问了全文搜索页', '页面访问', $route.path]"
  >
    <h3 class="title">
      全文搜索
    </h3>
    <div class="tb-gap">
      <el-radio-group v-model="indexName" size="mini">
        <el-radio label="journal" border>查询日志</el-radio>
        <el-radio label="announcement" border>查询公告</el-radio>
      </el-radio-group>
    </div>
    <el-input placeholder="请输入内容" v-model="keyword" class="tb-gap">
      <el-button slot="append" icon="el-icon-search" @click="submitSearch"
        >搜索
      </el-button>
    </el-input>

    <div class="flex-box flex-col tb-gap w-full">
      <el-card
        class="tb-gap w-full"
        v-for="record in resultData"
        :key="record.id"
      >
        <div class="text-align-left flex-box jy-between" slot="header">
          <span><b v-html="record.title_name"></b></span>
          <span
            ><b>{{ indexName === "journal" ? "日志" : "公告" }}时间：</b
            >{{ new Date(record.create_time).toLocaleString() }}</span
          >
        </div>
        <p class="content text-align-left" v-html="record.content"></p>
      </el-card>
    </div>
  </div>
</template>

<script>
import resErrorHandler from "../../utils/resErrorHandler";
import { mapState } from "vuex";

export default {
  name: "search",
  data() {
    return {
      indexName: "journal",
      keyword: "",
      page: 1,
      resultData: []
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  watch: {
    indexName() {
      this.resultData = [];
      this.keyword = "";
    }
  },
  methods: {
    async submitSearch() {
      try {
        const res = await this.$axios.get(
          `/lucene/${this.indexName}?keyword=${this.keyword}&page=${this.page}`
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("查询成功！");
          this.resultData = res.data.data;
        }
      } catch (err) {
        this.$message.error(String(err));
        this.resultData = [];
      }
    }
  }
};
</script>

<style scoped>
.subpage-search {
  width: 80%;
  margin: 0 auto;
}

.title {
  width: 100%;
  text-align: center;
}
.content {
  padding: 4px 20px;
}
</style>
