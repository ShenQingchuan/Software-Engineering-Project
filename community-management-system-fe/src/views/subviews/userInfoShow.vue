<template>
  <div class="flex-box flex-col bb-full subpage-user-info">
    <!--头顶标签-->
    <div class="user-info-header">个人信息</div>

    <!--主要居住信息-->
    <div class="flex-box as-start basic-info">
      <div class="flex-box flex-col avatar-box">
        <img :src="userInfo.photoUrl" alt="" class="photo" />
        <el-upload class="upload-demo" action="">
          <el-button class="update-avatar-btn" type="primary" plain
            >修改头像</el-button
          >
          <div slot="tip" class="el-upload__tip">
            只能上传 JPEG/PNG 文件，且不超过500kb
          </div>
        </el-upload>
      </div>
      <div class="flex-col flex-box as-start info-list">
        <div class="info-item flex-box"><b>所属网格区域：</b> {{ ofGrid }}</div>
        <div class="info-item flex-box">
          <b>姓名：</b> {{ userInfo.userName }}
        </div>
        <div class="info-item flex-box">
          <b>身份证号：</b> {{ userInfo.userId }}
        </div>
        <div class="info-item flex-box">
          <b>性别：</b> {{ userInfo.sex ? "男" : "女" }}
        </div>
        <div class="info-item flex-box">
          <b>曾用名：</b> {{ userInfo.usedName }}
        </div>
      </div>
    </div>
    <!--其他详细信息-->
    <div class="flex-box flex-col meta-info">
      <div class="info-list">
        <div class="meta-line flex-box jy-between">
          <div class="info-item flex-box">
            <b>民族：</b> {{ userInfo.nation }}
          </div>
          <div class="info-item flex-box">
            <b>血型：</b> {{ userInfo.bloodType }}型
          </div>
        </div>
        <div class="meta-line flex-box jy-between">
          <div class="info-item flex-box">
            <b>职业：</b> {{ userInfo.occupation }}
          </div>
          <div class="info-item flex-box">
            <b>政治面貌：</b> {{ userInfo.politics }}
          </div>
        </div>
        <div class="meta-line flex-box jy-between">
          <div class="info-item flex-box">
            <b>联系手机：</b> {{ userInfo.phone }}
          </div>
          <div class="info-item flex-box">
            <b>邮箱：</b> {{ userInfo.email }}
          </div>
        </div>
      </div>
    </div>

    <!--动作按钮-->
    <div class="info-actions">
      <el-button
        type="warning"
        plain
        @click="() => $router.push('/dashboard/userInfoUpdate')"
        >修改资料</el-button
      >
    </div>
  </div>
</template>

<script>
import userInfoMock from "../../mock/userInfoShow";

export default {
  name: "userInfoShow",
  mounted() {
    // eslint-disable-next-line no-unused-vars
    const uid = this.$route.params.uid;
    // TODO： 请求用户的资料（用户自己看 / 网格员查看） /profile/getProfile/{id}
    // https://easydoc.xyz/p/43159074/MAhLR20e
  },
  data() {
    return {
      userInfo: userInfoMock
    };
  },
  computed: {
    ofGrid() {
      return `${this.userInfo.grid.area} / ${this.userInfo.grid.community}`;
    }
  }
};
</script>

<style lang="scss">
.subpage-user-info {
  width: 60%;
  min-width: 890px;
  max-width: 890px;
  margin: 20px auto;
  position: relative;
  padding: 20px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(0, 0, 0, 0.1);

  .user-info-header {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    top: -20px;
    cursor: default;
    padding: 5px 20px;
    width: fit-content;
    font-size: 18px;
    font-weight: bold;
    border-radius: 15px;
    background-color: #ffebab;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
  }

  .basic-info,
  .meta-info {
    width: 70%;
    padding: 10px 0 10px 0;

    .avatar-box {
      img {
        box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
        max-width: 150px;
        width: 100%;
        margin-top: 20px;
        border-radius: 20px;
      }
      .update-avatar-btn {
        margin-top: 40px;
      }
    }
    .info-list {
      margin-left: 20px;
      width: 100%;

      .info-item,
      .meta-line .info-item {
        b {
          width: 150px;
        }
        cursor: default;
        border-radius: 10px;
        box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
        &:hover {
          box-shadow: none;
          color: #e48b19;
        }
        text-align: left;
        width: max-content;
        margin: 10px 0;
        padding: 10px;
        font-size: 16px;
        color: #2c3e50;
        transition: all 0.4s ease;
      }
    }
  }

  .info-actions {
    margin-top: 20px;
  }
}
</style>
