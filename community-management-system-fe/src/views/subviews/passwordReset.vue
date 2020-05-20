<template>
  <div class="flex-box flex-col jy-center subpage-password-reset">
    <!-- 步骤条 -->
    <el-steps
      :active="step"
      align-center
      class="steps-tip"
      finish-status="success"
      simple
    >
      <el-step class="steps-tip-item" title="安全验证"></el-step>
      <el-step class="steps-tip-item" title="修改密码"></el-step>
      <el-step class="steps-tip-item" title="修改成功"></el-step>
    </el-steps>

    <el-select
      v-show="step === 0"
      class="verify-type-select"
      placeholder="请选择验证方式"
      v-model="verifyType"
    >
      <el-option label="密保验证" value="密保验证"></el-option>
      <el-option
        v-if="userInfo.telPhone && userInfo.telPhone !== ''"
        label="手机验证"
        value="手机验证"
      ></el-option>
    </el-select>

    <!-- 各步骤对应操作 -->
    <secret-verify
      @forward="stepForward"
      v-if="verifyType === '密保验证' && step === 0"
    ></secret-verify>
    <!-- 手机验证表单组件 -->
    <phone-code-verify
      @forward="stepForward"
      v-if="verifyType === '手机验证' && step === 0"
    />
    <!-- 最后显示成功 -->
    <div class="finished-view tb-gap flex-box flex-col" v-show="step === 2">
      <i
        class="el-icon-finished"
        style="padding: 10px; border-radius: 100%; border: 5px solid darkgreen; color: darkgreen; font-size: 50px; font-weight: bold"
      ></i>
      <b class="tb-gap" style="color: darkgreen; margin-left: 20px;"
        >密码修改成功 ！</b
      >
    </div>

    <!-- 修改密码 -->
    <final-reset-password @forward="stepForward" v-show="step === 1" />
  </div>
</template>

<script>
import PhoneCodeVerify from "../../components/phoneCodeVerify";
import finalResetPassword from "../../components/finalResetPassword";
import SecretVerify from "../../components/secretVerify";
import { mapState } from "vuex";

export default {
  name: "passwordReset",
  components: { SecretVerify, finalResetPassword, PhoneCodeVerify },
  computed: {
    ...mapState(["userInfo"])
  },
  data() {
    return {
      verifyType: "",
      step: 0
    };
  },
  methods: {
    stepForward() {
      this.step++;
    }
  }
};
</script>

<style lang="scss" scoped>
.subpage-password-reset {
  padding: 20px;
  width: 80%;
  height: 500px;
  margin: 0 auto;

  .verify-type-select {
    margin: 20px;
    width: 40%;
  }

  .steps-tip {
    max-width: 80%;
    width: 65%;

    &-item {
      width: max-content;
    }
  }
}
</style>
