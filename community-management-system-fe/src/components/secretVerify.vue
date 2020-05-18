<template>
  <div class="comp-secret-verify">
    <el-form v-loading="loadingQuestion" :model="form">
      <el-form-item :label="`问题一：${question[0]}`" prop="answer1">
        <el-input
          placeholder="请填写问题一的答案"
          v-model="form.answerOne"
        ></el-input>
      </el-form-item>
      <el-form-item :label="`问题二：${question[1]}`" prop="answer2">
        <el-input
          placeholder="请填写问题二的答案"
          v-model="form.answerTwo"
        ></el-input>
      </el-form-item>

      <el-button @click="finishSecretVerify" plain type="primary"
        >下一步
      </el-button>
    </el-form>
  </div>
</template>

<script>
import resErrorHandler from "../utils/resErrorHandler";
import { mapState } from "vuex";
// import SecretVerifyMock from "@/mock/getSecretQuestion";

export default {
  name: "secretVerify",
  async mounted() {
    this.loadingQuestion = true;
    const res = await this.$axios.get(
      `/pwdPro/returnPwdProQue/${this.userInfo.id}`
    );
    resErrorHandler(this, res);
    if (res.data.resultCode === "200") {
      this.question = res.data.data;
      this.$message.success("获取密保问题成功");
      this.loadingQuestion = false;
    }
  },
  data() {
    return {
      loadingQuestion: false,
      question: [],
      form: {
        answerOne: "",
        answerTwo: ""
      }
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  methods: {
    async finishSecretVerify() {
      try {
        const { answerOne, answerTwo } = this.form;
        const res = await this.$axios.post(
          `/pwdPro/comparePwdProAns/${this.userInfo.id}`,
          {
            answerOne,
            answerTwo
          }
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("密保验证成功！");
          this.$emit("forward", true);
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.comp-secret-verify {
  width: 50%;
}
</style>
