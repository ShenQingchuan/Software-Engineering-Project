<template>
  <div class="comp-change-password">
    <el-form :model="form" :rules="rules" label-width="130px">
      <el-form-item label="新密码：" prop="newPassword">
        <el-input v-model="form.newPassword"></el-input>
      </el-form-item>
      <el-form-item label="再次确认新密码：" prop="reNewPassword">
        <el-input v-model="form.reNewPassword"></el-input>
      </el-form-item>

      <el-button @click="confirmChangePassword" plain type="primary"
        >下一步
      </el-button>
    </el-form>
  </div>
</template>

<script>
import { mapState } from "vuex";
import resErrorHandler from "../utils/resErrorHandler";

export default {
  name: "finalResetPassword",
  data() {
    const validateRepassowrd = (rule, value, callback) => {
      if (value !== this.form.newPassword) {
        callback(new Error("两次密码输入不一致！"));
      } else {
        callback();
      }
    };

    return {
      form: {
        newPassword: "",
        reNewPassword: ""
      },
      rules: {
        reNewPassword: [{ validator: validateRepassowrd, trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  methods: {
    async confirmChangePassword() {
      if (this.form.newPassword !== this.form.reNewPassword) {
        this.$message.error("两次密码不一致");
        return;
      }

      const res = await this.$axios.put(`/pwdPro/modifyPwd/${this.user.id}`, {
        newPassword: this.form.newPassword
      });
      resErrorHandler(this, res);
      if (res.data.resultCode === "200") {
        this.$message.success("修改密码成功");
        this.$emit("forward", true);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.comp-change-password {
  padding: 20px;
  width: 40%;
}
</style>
