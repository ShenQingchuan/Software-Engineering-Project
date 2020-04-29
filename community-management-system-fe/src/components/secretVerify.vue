<template>
  <div class="comp-secret-verify">
    <el-form :model="form">
      <el-form-item :label="`问题一：${question.q1}`" prop="answer1">
        <el-input
          placeholder="请填写问题一的答案"
          v-model="form.answer1"
        ></el-input>
      </el-form-item>
      <el-form-item :label="`问题二：${question.q2}`" prop="answer2">
        <el-input
          placeholder="请填写问题二的答案"
          v-model="form.answer2"
        ></el-input>
      </el-form-item>

      <el-button @click="finishSecretVerify" plain type="primary"
        >下一步
      </el-button>
    </el-form>
  </div>
</template>

<script>
import SecretVerifyMock from "@/mock/getSecretQuestion";

export default {
  name: "secretVerify",
  data() {
    return {
      question: SecretVerifyMock,
      form: {
        answer1: "",
        answer2: ""
      }
    };
  },
  methods: {
    async requestForSecret() {},
    async finishSecretVerify() {
      this.$emit("forward", true);
    }
  }
};
</script>

<style lang="scss" scoped>
.comp-secret-verify {
  width: 50%;
}
</style>
