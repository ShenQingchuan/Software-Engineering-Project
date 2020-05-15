<template>
  <div class="subpage-user-info">
    <!--头顶标签-->
    <div class="user-info-header">修改信息</div>
    <div
      class="flex-col flex-col user-info-go-back"
      @click="() => $router.push('/dashboard/userInfoShow')"
    >
      <i class="el-icon-arrow-left"></i>
    </div>
    <el-form
      v-if="updateForm.id"
      :model="updateForm"
      class="user-info-update-form"
    >
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">姓名：</span></template>
        <el-input
          class="form-inputer"
          v-model="updateForm.userName"
          placeholder="请输入姓名"
        ></el-input>
      </el-form-item>
      <el-form-item
        v-if="showGridInputer"
        class="form-item"
        label-width="100px"
      >
        <template v-slot:label
          ><span class="form-label">所属网格：</span></template
        >
        <div class="flex-box">
          <el-input
            class="form-inputer"
            v-model="updateForm.ofGrid.districtName"
            placeholder="请输入小区名称"
          ></el-input>
          <el-input
            class="form-inputer lr-gap"
            v-model="updateForm.ofGrid.communityName"
            placeholder="请输入片区名称"
          ></el-input>
        </div>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label
          ><span class="form-label">身份证号：</span></template
        >
        <el-input class="form-inputer" v-model="updateForm.sfzId"></el-input>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">性别：</span></template>
        <el-select
          class="form-selector"
          v-model="updateForm.sex"
          placeholder="请选择性别"
        >
          <el-option label="男" :value="1"> </el-option>
          <el-option label="女" :value="0"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">民族：</span></template>
        <el-select
          class="form-selector"
          v-model="updateForm.nation"
          placeholder="请选择民族"
        >
          <el-option
            v-for="item in ChinaNations"
            :key="item.id"
            :label="item.name"
            :value="item.name"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">血型：</span></template>
        <el-select
          class="form-selector"
          v-model="updateForm.bloodType"
          placeholder="请选择血型"
          @change="turnToRareBloodType"
          v-show="!showRareBloodTypeInputer"
        >
          <el-option value="A"> </el-option>
          <el-option value="B"> </el-option>
          <el-option value="O"> </el-option>
          <el-option value="AB"> </el-option>
          <el-option label="其他稀有血型" value="其他稀有血型"> </el-option>
        </el-select>
        <el-input
          v-show="showRareBloodTypeInputer"
          class="form-inputer"
          v-model="updateForm.bloodType"
          placeholder="请输入您的稀有血型名称"
        ></el-input>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label
          ><span class="form-label">政治面貌：</span></template
        >
        <el-select
          class="form-selector"
          v-model="updateForm.politicCountenance"
          placeholder="请选择政治面貌"
        >
          <el-option label="中共党员" value="中共党员"> </el-option>
          <el-option label="其他党派人士" value="其他党派人士"> </el-option>
          <el-option label="无党派人士" value="无党派人士"> </el-option>
          <el-option label="共青团员" value="共青团员"> </el-option>
          <el-option label="群众" value="群众"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">职业：</span></template>
        <el-input
          class="form-inputer"
          v-model="updateForm.occupation"
        ></el-input>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label
          ><span class="form-label">文化程度：</span></template
        >
        <el-input
          class="form-inputer"
          v-model="updateForm.degreeOfEducation"
        ></el-input>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label
          ><span class="form-label">联系手机：</span></template
        >
        <el-input class="form-inputer" v-model="updateForm.telPhone"></el-input>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">邮箱：</span></template>
        <el-input class="form-inputer" v-model="updateForm.email"></el-input>
      </el-form-item>
    </el-form>

    <!--动作按钮-->
    <div class="info-actions">
      <el-button type="warning" plain @click="submitUpdateInfo"
        >确认修改</el-button
      >
    </div>
  </div>
</template>

<script>
import ChinaNations from "@/assets/data/china-nations";
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
// import userInfoMock from "../../mock/userInfoShow";
// import gridSelectorMock from "../../mock/gridSelector";

export default {
  name: "userInfoUpdate",
  data() {
    return {
      ChinaNations,
      showRareBloodTypeInputer: false,

      updateForm: {},
      showGridInputer: false
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  mounted() {
    this.updateForm = this.userInfo;
    this.showGridInputer = true;
  },
  methods: {
    turnToRareBloodType(val) {
      this.showRareBloodTypeInputer = val === "其他稀有血型";
      this.updateForm.bloodType = "";
    },
    async submitUpdateInfo() {
      const {
        userName,
        avatarUrl,
        sex,
        telPhone,
        ofGrid,
        nation,
        degreeOfEducation,
        bloodType,
        occupation,
        email,
        politicCountenance
      } = this.updateForm;
      try {
        const res = await this.$axios.post(
          `/profile/modify/${this.userInfo.id}`,
          {
            userName,
            avatarUrl,
            sex,
            telPhone,
            ofGrid,
            nation,
            degreeOfEducation,
            bloodType,
            occupation,
            email,
            politicCountenance
          }
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.$message.success("修改资料成功");
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.subpage-user-info {
  .user-info-go-back {
    top: -20px;
    position: absolute;
    width: 40px;
    height: 40px;
    border-radius: 25px;
    background: #2c3e50;
    cursor: pointer;
    transition: all 0.4s ease;

    &:hover {
      box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
    }

    i {
      z-index: 9;
      color: white;
      line-height: 40px;
      font-weight: bold;
    }
  }

  .user-info-update-form {
    margin-top: 30px;

    .form-item {
      text-align: left;

      .form-label {
        font-weight: bold;
      }
      .form-selector {
        margin-right: 10px;
      }
    }
  }
}
</style>
