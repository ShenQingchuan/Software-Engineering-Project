<template>
  <div class="subpage-password-protect">
    <el-form ref="form" :model="pswdPro" label-width="100px">
      <el-form-item label="密保问题一：">
        <el-input v-model="pswdPro.questionOne"></el-input>
      </el-form-item>
      <el-form-item label="问题一答案：">
        <el-input v-model="pswdPro.answerOne"></el-input>
      </el-form-item>
      <el-form-item label="密保问题二：">
        <el-input v-model="pswdPro.questionTwo"></el-input>
      </el-form-item>
      <el-form-item label="问题二答案：">
        <el-input v-model="pswdPro.answerTwo"></el-input>
      </el-form-item>
    </el-form>

    <div class="info-actions flex-box jy-center">
      <el-button
        @click="submitPasswordProtect"
        class="lr-gap"
        size="medium"
        type="success"
        plain
        >提交密保
      </el-button>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "setPasswordProtect",
  computed: {
    ...mapState(["userInfo"])
  },
  data() {
    return {
      pswdPro: {
        questionOne: "",
        answerOne: "",
        questionTwo: "",
        answerTwo: ""
      }
    };
  },
  methods: {
    async submitPasswordProtect() {
      for (const key in this.pswdPro) {
        if (this.pswdPro[key].length === 0) {
          this.$message.error("您有字段没有填写！");
          return;
        }
      }
      try {
        const res = await this.$axios.put(
          `/pwdPro/setUpPwdPro/${this.userInfo.id}`,
          this.pswdPro
        );
        if (res.data.resultCode === "200") {
          this.$message.success("设置密保成功！");
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.subpage-password-protect {
  padding: 20px;
  font-weight: bold;
}
</style>
