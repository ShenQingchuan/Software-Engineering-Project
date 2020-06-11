<template>
  <div class="subpage-user-info">
    <!--头顶标签-->
    <div class="user-info-header">修改信息</div>
    <div
      class="flex-col flex-col user-info-go-back"
      @click="() => $router.push(`/dashboard/userInfoShow/${userInfo.id}`)"
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
          @blur="blurHandler($event, '姓名')"
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
            @blur="blurHandler($event, '小区名称', 'districtName')"
            class="form-inputer"
            v-model="updateForm.ofGrid.districtName"
            placeholder="请输入小区名称"
          ></el-input>
          <el-input
            @blur="blurHandler($event, '片区名称', 'communityName')"
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
        <el-input
          @blur="blurHandler($event, '身份证号', 'sfzId')"
          class="form-inputer"
          v-model="updateForm.sfzId"
        ></el-input>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">性别：</span></template>
        <el-select
          @blur="blurHandler($event, '性别', 'sex')"
          class="form-selector"
          v-model="updateForm.sex"
          placeholder="请选择性别"
        >
          <el-option label="男" :value="1"></el-option>
          <el-option label="女" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">民族：</span></template>
        <el-select
          @blur="blurHandler($event, '民族', 'nation')"
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
          @blur="blurHandler($event, '血型', 'bloodType')"
          class="form-selector"
          v-model="updateForm.bloodType"
          placeholder="请选择血型"
          @change="turnToRareBloodType"
          v-show="!showRareBloodTypeInputer"
        >
          <el-option value="A"></el-option>
          <el-option value="B"></el-option>
          <el-option value="O"></el-option>
          <el-option value="AB"></el-option>
          <el-option label="其他稀有血型" value="其他稀有血型"></el-option>
        </el-select>
        <el-input
          @blur="blurHandler($event, '稀有血型', 'bloodType')"
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
          @blur="blurHandler($event, '政治面貌', 'politicCountenance')"
          class="form-selector"
          v-model="updateForm.politicCountenance"
          placeholder="请选择政治面貌"
        >
          <el-option label="中共党员" value="中共党员"></el-option>
          <el-option label="其他党派人士" value="其他党派人士"></el-option>
          <el-option label="无党派人士" value="无党派人士"></el-option>
          <el-option label="共青团员" value="共青团员"></el-option>
          <el-option label="群众" value="群众"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">职业：</span></template>
        <el-input
          @blur="blurHandler($event, '职业', 'occupation')"
          class="form-inputer"
          v-model="updateForm.occupation"
        ></el-input>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label
          ><span class="form-label">文化程度：</span></template
        >
        <el-select
          @blur="blurHandler($event, '文化程度', 'degreeOfEducation')"
          class="form-inputer"
          v-model="updateForm.degreeOfEducation"
        >
          <el-option value="小学" />
          <el-option value="初中" />
          <el-option value="高中" />
          <el-option value="中专" />
          <el-option value="大专" />
          <el-option value="本科" />
          <el-option value="硕士" />
          <el-option value="博士" />
          <el-option value="博士后" />
        </el-select>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label
          ><span class="form-label">联系手机：</span></template
        >
        <div class="flex-box jy-between">
          <el-input
            @blur="blurHandler($event, '联系手机号', 'telPhone')"
            class="form-inputer"
            v-model="updateForm.telPhone"
          ></el-input>
          <el-button class="lr-gap" type="primary" @click="changeTelPhone"
            >更换手机
          </el-button>
        </div>
      </el-form-item>
      <el-form-item class="form-item" label-width="100px">
        <template v-slot:label><span class="form-label">邮箱：</span></template>
        <el-input
          @blur="blurHandler($event, '邮箱', 'email')"
          class="form-inputer"
          v-model="updateForm.email"
        ></el-input>
      </el-form-item>
    </el-form>

    <!--动作按钮-->
    <div class="info-actions">
      <el-button type="warning" plain @click="submitUpdateInfo"
        >确认修改
      </el-button>
    </div>
  </div>
</template>

<script>
import ChinaNations from "@/assets/data/china-nations";
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
import AV from "@/utils/LeanCloudMessage";
import { send } from "../../utils/burringPoint";
// import userInfoMock from "../../mock/userInfoShow";
// import gridSelectorMock from "../../mock/gridSelector";

export default {
  name: "userInfoUpdate",
  data() {
    return {
      ChinaNations,
      showRareBloodTypeInputer: false,

      updateForm: {},
      changeTelPhoneVerifyCode: "",
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
    blurHandler(evt, label, key) {
      send([
        this.userInfo.id,
        `编辑${label}中... 当前编辑值: ${this.updateForm[key]}`,
        "文本编辑",
        this.$route.path
      ])
        .then(res => console.log(res))
        .catch(err => console.error(err));
    },
    turnToRareBloodType(val) {
      if (val === "其他稀有血型") {
        this.showRareBloodTypeInputer = true;
        this.updateForm.bloodType = "";
      }
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
    },
    async changeTelPhone() {
      const h = this.$createElement;
      const self = this;
      AV.Cloud.requestSmsCode({
        mobilePhoneNumber: this.updateForm.telPhone,
        name: "社区治理信息助手",
        op: "更换手机",
        ttl: 10 // 验证码有效时间为 10 分钟
      })
        .then(res => {
          console.log(res);
          this.$message.success("短信发送成功！");
        })
        .catch(err => {
          this.$message.error("短信发送失败！" + String(err));
        });

      this.$confirm(
        h(
          "div",
          {
            class: "el-input"
          },
          [
            h(
              "div",
              {
                class: "tb-gap"
              },
              "手机验证码已发送！"
            ),
            h("input", {
              class: "el-input__inner",
              domProps: {
                placeholder: "请输入您接收到的验证码"
              },
              on: {
                input(e) {
                  self.changeTelPhoneVerifyCode = e.target.value;
                  self.$forceUpdate();
                },
                blur(evt) {
                  send([
                    self.userInfo.id,
                    `编辑短信验证码中... 当前编辑值: ${evt.target.value}`,
                    "文本编辑",
                    self.$route.path
                  ])
                    .then(res => console.log(res))
                    .catch(err => console.error(err));
                }
              }
            })
          ]
        ),
        "更换手机验证",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
          dangerouslyUseHTMLString: true
        }
      )
        .then(() => {
          AV.Cloud.verifySmsCode(
            this.changeTelPhoneVerifyCode,
            this.updateForm.telPhone
          )
            .then(res => {
              console.log(res);
              this.$axios
                .post(`/profile/modify/${this.userInfo.id}`, {
                  telPhone: this.updateForm.telPhone
                })
                .then(res => {
                  resErrorHandler(this, res);
                  if (res.data.resultCode === "200") {
                    this.$message.success("更换手机成功！");
                  }
                })
                .catch(err => {
                  this.$message.error(
                    "向服务器提交更换手机请求失败！" + String(err)
                  );
                });
            })
            .catch(err => {
              this.$message.error("更换失败！" + String(err));
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消更换手机..."
          });
        });
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
