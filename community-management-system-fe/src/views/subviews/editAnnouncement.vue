<template>
  <div class="subpage-add-announcement">
    <h3>{{ editor.type }}公告</h3>

    <!--其他属性信息-->
    <div class="meta-form font-bold tb-gap flex-box flex-col jy-start">
      <div class="meta-form-item tb-gap flex-box jy-start">
        <label>标题：</label>
        <el-input v-model="title" placeholder="请输入标题"></el-input>
      </div>
      <div class="meta-form-item tb-gap flex-box jy-start">
        <el-checkbox v-model="fixedTop">是否置顶</el-checkbox>
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
      <el-button @click="submitAnnouncement" type="primary" plain
        >保存提交</el-button
      >
    </div>
  </div>
</template>

<script>
export default {
  name: "editAnnouncement",
  mounted() {
    this.editor.type =
      this.$route.path.split("/").slice(-1)[0] === "addAnnouncement"
        ? "新建"
        : "编辑";
  },
  data() {
    return {
      title: "",
      fixedTop: "",
      editor: {
        type: "",
        source: "",
        render: ""
      }
    };
  },
  methods: {
    submitAnnouncement() {
      this.$message.warning("暂未调试接口");
      // TODO: 连调测试上传公告
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
