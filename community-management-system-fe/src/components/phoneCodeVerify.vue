<template>
  <div class="comp-phone-code-verify">
    <el-form
      :model="form"
      :rules="rules"
      label-width="100px"
      ref="phoneCodeVerifyForm"
      v-loading="phoneCodeVerifyLoading"
    >
      <el-form-item label="手机号：" prop="phone">
        <div class="flex-box">
          <el-input
            placeholder="请输入手机号"
            disabled
            :value="userInfo.telPhone"
          ></el-input>
          <el-button
            @click="sendVerifyCode"
            class="send-code-btn"
            plain
            type="primary"
          >
            {{ !gapTime ? "发送验证码" : `等待 ${gapTime} 秒后重新发送` }}
          </el-button>
        </div>
      </el-form-item>
      <el-form-item label="验证码：" prop="code">
        <el-input placeholder="请输入验证码" v-model="form.code"></el-input>
      </el-form-item>

      <el-button @click="finishVerifyStep" plain type="primary"
        >下一步
      </el-button>
    </el-form>
  </div>
</template>

<script>
import { mapState } from "vuex";
import AV from "@/utils/LeanCloudMessage";

const codeRegex = /(\d){6}/;

export default {
  name: "phoneCodeVerify",
  computed: {
    ...mapState(["userInfo"])
  },
  data() {
    const validateCode = (rule, value, callback) => {
      if (this.codeFail(value)) {
        callback(new Error("验证码格式不正确！"));
      } else {
        callback();
      }
    };

    return {
      form: {
        code: ""
      },
      rules: {
        code: [{ validator: validateCode, trigger: "blur" }]
      },
      gapTime: 0,

      phoneCodeVerifyLoading: false
    };
  },
  methods: {
    codeFail(value) {
      return value === "" || !codeRegex.test(value);
    },
    finishVerifyStep() {
      this.phoneCodeVerifyLoading = true;
      this.$refs.phoneCodeVerifyForm.validate(async valid => {
        if (!valid) {
          this.phoneCodeVerifyLoading = false;
          this.$message.error("表单出错！");
          return false;
        } else {
          try {
            const res = await AV.Cloud.verifySmsCode(
              this.form.code,
              this.userInfo.telPhone
            );
            console.log(res);
            this.phoneCodeVerifyLoading = false;
            this.$message.success("验证码验证成功！");
            this.$emit("forward", true);
          } catch (err) {
            this.$message.error(String(err));
          }
        }
      });
    },
    async sendVerifyCode() {
      try {
        const res = await AV.Cloud.requestSmsCode({
          mobilePhoneNumber: this.userInfo.telPhone,
          name: "社区治理信息助手",
          op: "修改密保",
          ttl: 10 // 验证码有效时间为 10 分钟
        });
        console.log(res);
        this.$message.success("短信发送成功！");
      } catch (err) {
        console.error(err);
        this.$message.error("短信发送失败！");
      }

      this.gapTime = 60;
      setInterval(() => {
        this.gapTime--;
      }, 1000);
    }
  }
};
</script>

<style lang="scss" scoped>
.comp-phone-code-verify {
  width: 50%;
  margin: 20px;

  .send-code-btn {
    margin-left: 20px;
  }
}
</style>
