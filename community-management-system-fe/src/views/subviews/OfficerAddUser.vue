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
          <el-input placeholder="请输入用户 ID" v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="所属片区">
          <div class="flex-box jy-start">
            <b>{{ OfficerGetDistrictName.districtName }}</b>
          </div>
        </el-form-item>
        <el-form-item label="所属小区">
          <el-select
            class="select-restrict"
            v-model="form.restrict"
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
import OfficerGetDistrictNameMock from "../../mock/OfficerGetDistrictName";

export default {
  name: "OfficerAddUser",
  data() {
    return {
      form: {
        userId: "",
        restrict: "",
        community: ""
      },
      OfficerGetDistrictName: OfficerGetDistrictNameMock
    };
  },
  methods: {
    submitOfficerAddUser() {}
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
