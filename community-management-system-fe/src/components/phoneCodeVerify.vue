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
          <el-input placeholder="请输入手机号" v-model="form.phone"></el-input>
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
const phoneRegex = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/;
const codeRegex = /(\d){6}/;

import AV from "leancloud-storage";

AV.init({
  appId: "2ljhc8aJt8UgjvbjHc2u8qrL-gzGzoHsz",
  appKey: "nGckV9BcpYFTReNi4BPBemAQ",
  serverURL: "https://2ljhc8aj.lc-cn-n1-shared.com"
});

export default {
  name: "phoneCodeVerify",
  data() {
    const validatePhone = (rule, value, callback) => {
      if (this.phoneFail(value)) {
        callback(new Error("手机号码格式不正确！"));
      } else {
        callback();
      }
    };
    const validateCode = (rule, value, callback) => {
      if (this.codeFail(value)) {
        callback(new Error("验证码格式不正确！"));
      } else {
        callback();
      }
    };

    return {
      form: {
        phone: "",
        code: ""
      },
      rules: {
        phone: [{ validator: validatePhone, trigger: "blur" }],
        code: [{ validator: validateCode, trigger: "blur" }]
      },
      gapTime: 0,

      phoneCodeVerifyLoading: false
    };
  },
  methods: {
    phoneFail(value) {
      return value === "" || !phoneRegex.test(value);
    },
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
          // 模拟 异步请求
          try {
            const res = await AV.Cloud.verifySmsCode(
              this.form.code,
              this.form.phone
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
      if (this.phoneFail(this.form.phone)) {
        this.$message.warning("请检查您的手机号填写！");
        return;
      }

      const phone = this.form.phone;
      try {
        const res = await AV.Cloud.requestSmsCode({
          mobilePhoneNumber: phone,
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
