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
        <el-radio v-model="logType" label="调查走访"></el-radio>
        <el-radio v-model="logType" label="巡逻治安"></el-radio>
        <el-radio v-model="logType" label="基础设施养护"></el-radio>
        <el-radio v-model="logType" label="阶段工作总结"></el-radio>
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
export default {
  name: "editAnnouncement",
  mounted() {
    const path = this.$route.path.split("/").slice(-1)[0];
    console.log(path);
    this.editor.type = path === "addLog" ? "新建" : "编辑";
  },
  data() {
    return {
      title: "",
      logType: "",
      logDate: "",
      logTime: "",
      editor: {
        type: "",
        source: "",
        render: ""
      }
    };
  },
  methods: {
    submitLog() {
      this.$message.warning("暂未调试接口");
      // TODO: 连调测试上传日志  /grid/releaseAnnouncement/{id}
      // https://easydoc.xyz/p/43159074/MAhLR20e
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
