<template>
  <div class="subpage-add-announcement">
    <h3>{{ editor.type }}日志</h3>

    <!--其他属性信息-->
    <div class="meta-form font-bold tb-gap flex-box flex-col jy-start">
      <div class="meta-form-item tb-gap flex-box jy-start">
        <label>标题：</label>
        <el-input v-model="title" placeholder="请输入标题"></el-input>
      </div>
      <div class="meta-form-item tb-gap flex-box jy-start">
        <label>日志类型：</label>
        <el-select v-model="logType">
          <el-option
            v-for="(t, i) in logTypeOptions"
            :key="i"
            :label="t"
            :value="t"
          ></el-option>
        </el-select>
      </div>
    </div>

    <!--编辑器-->
    <mavon-editor
      class="editor tb-gap"
      v-model="editor.source"
      @change="
        (source, render) => {
          editor.source = source;
          editor.render = render;
        }
      "
    ></mavon-editor>

    <div class="actions-container tb-gap flex-box jy-center">
      <el-button @click="submitLog" type="primary" plain>保存提交</el-button>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";

export default {
  name: "editAnnouncement",
  async mounted() {
    const path = this.$route.path.split("/").slice(-1)[0];
    console.log(path);
    this.editor.type = path === "addLog" ? "新建" : "编辑";

    const res = await this.$axios.get(`/grid/getJournalTypeName`);
    resErrorHandler(this, res);
    if (res.data.resultCode === "200") {
      this.logTypeOptions = res.data.data;
    }
  },
  data() {
    return {
      logTypeOptions: [],

      title: "",
      logType: "",
      editor: {
        type: "",
        source: "",
        render: ""
      }
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  methods: {
    async submitLog() {
      if (
        this.title === "" ||
        this.logType === "" ||
        this.editor.source === ""
      ) {
        this.$message.error("不允许提交空内容！");
        return;
      }
      if (this.editor.type === "新建") {
        try {
          const res = await this.$axios.post(
            `/grid/releaseJournal/${this.userInfo.id}`,
            {
              titleName: this.title,
              type: this.logType,
              content: this.editor.render
            }
          );
          resErrorHandler(this, res);
          if (res.data.resultCode === "200") {
            this.$message.success("新建日志成功！");
            await this.$router.push("/dashboard/logManage");
          }
        } catch (err) {
          this.$message.error(String(err));
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.subpage-add-announcement {
  width: 70%;
  margin: 0 auto;

  .meta-form {
    &-item {
      width: 70%;
      label {
        width: 100px;
        min-width: fit-content;
      }
      .scope-selector {
        flex: 1;
      }
    }
  }

  .editor {
  }
}
</style>
