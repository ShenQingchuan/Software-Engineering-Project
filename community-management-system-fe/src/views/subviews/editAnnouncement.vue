<template>
  <div
    class="subpage-add-announcement"
    v-bp-default="[
      userInfo.id,
      `访问了${editor.type}公告页面`,
      '页面访问',
      $route.path
    ]"
  >
    <h3>{{ editor.type }}公告</h3>

    <!--其他属性信息-->
    <div class="meta-form font-bold tb-gap flex-box flex-col jy-start">
      <div class="meta-form-item tb-gap flex-box jy-start">
        <label>标题：</label>
        <el-input
          @blur="titleBlurHandler"
          placeholder="请输入标题"
          v-model="title"
        ></el-input>
      </div>
    </div>

    <!--编辑器-->
    <mavon-editor
      :ishljs="true"
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
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
import { send } from "../../utils/burringPoint";

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
    titleBlurHandler(e) {
      send([
        this.userInfo.id,
        `编辑日志标题中... 当前编辑值: ${e.target.value}`,
        "文本编辑",
        this.$route.path
      ])
        .then(res => console.log(res))
        .catch(err => console.error(err));
    },
    async submitAnnouncement() {
      if (this.editor.source.length === 0 || this.title.length === 0) {
        this.$message.error("不能提交空内容！");
        return;
      }

      try {
        const res = await this.$axios.post(
          `/grid/releaseAnnouncement/${this.userInfo.id}`,
          {
            titleName: this.title,
            content: this.editor.render
          }
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("新建公告成功！");
          await this.$router.push("/dashboard/announcementManage");
        }
      } catch (err) {
        this.$message.error(String(err));
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
