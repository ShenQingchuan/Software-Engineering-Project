<template>
  <div class="subpage-officer-add-user add-user-card">
    <el-card class="box-card">
      <div slot="header" class="inner-box flex-box jy-between">
        <span><b>添加用户</b></span>
        <el-button @click="submitOfficerAddUser" type="text"
          >确认提交</el-button
        >
      </div>
      <el-form
        ref="form"
        class="add-user-form"
        :model="form"
        label-width="100px"
      >
        <el-form-item label="身份证号">
          <el-input
            placeholder="请输入用户 ID"
            v-model="form.userID"
          ></el-input>
        </el-form-item>
        <el-form-item label="所属片区">
          <div class="flex-box jy-start">
            <b>{{ OfficerGetDistrictName.districtName }}</b>
          </div>
        </el-form-item>
        <el-form-item label="所属小区">
          <el-select
            class="select-restrict"
            v-model="form.community"
            placeholder="请选择活动区域"
          >
            <el-option
              v-for="(e, i) in OfficerGetDistrictName.communityArray"
              :key="i"
              :label="e"
              :value="e"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapState } from "vuex";
import md5 from "md5";
import resErrorHandler from "../../utils/resErrorHandler";
// import OfficerGetDistrictNameMock from "../../mock/OfficerGetDistrictName";

export default {
  name: "OfficerAddUser",
  async mounted() {
    const res = await this.$axios.get(
      `/grid/getManageAreaList/${this.userInfo.id}`
    );
    resErrorHandler(this, res);
    console.log(res);
    if (res.data.resultCode === "200") {
      this.$message.success("获取到当前网格员管理信息！");
      this.OfficerGetDistrictName = res.data.data;
    }
  },
  computed: {
    ...mapState(["userInfo"])
  },
  data() {
    return {
      form: {
        userID: "",
        community: ""
      },
      OfficerGetDistrictName: {}
    };
  },
  methods: {
    async submitOfficerAddUser() {
      const { userID, community } = this.form;
      if (!/\d{18}/.test(userID)) {
        this.$message.error("身份证输入格式出错！");
        return;
      }

      try {
        const res = await this.$axios.post(`/grid/addResidentUser`, {
          userID,
          district: this.OfficerGetDistrictName.districtName,
          community,
          password: md5(userID.slice(-6))
        });
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("添加用户成功！");
          await this.$router.push("/dashboard/userManage");
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.add-user-card {
  width: 60%;
  margin: 0 auto;

  .inner-box {
    b {
      font-size: 26px;
    }
  }

  .add-user-form {
    .select-restrict {
      width: 100%;
    }
  }
}
</style>
