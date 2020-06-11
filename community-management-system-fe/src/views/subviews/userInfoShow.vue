<template>
    <div
            class="flex-box flex-col bb-full subpage-user-info"
            v-bp-default="[
      userInfo.id,
      '访问了用户资料展示页',
      '页面访问',
      $route.path
    ]"
    >
        <!--头顶标签-->
        <div class="user-info-header">个人信息</div>

        <!--主要居住信息-->
        <div class="flex-box as-start basic-info">
            <div class="flex-box flex-col avatar-box">
                <el-upload :http-request="uploadAvatar" class="upload-demo" action="">
                    <img
                            v-if="userInfo.avatarUrl"
                            :src="userInfo.avatarUrl"
            alt=""
            class="photo"
          />
          <div v-else>
            <svg
                    t="1589353294807"
                    class="icon"
                    viewBox="0 0 1024 1024"
                    version="1.1"
                    xmlns="http://www.w3.org/2000/svg"
                    p-id="1606"
                    width="120"
                    height="120"
            >
                <path
                        d="M642.56 709.12C638.72 669.44 640 640 640 602.88c17.92-10.24 81.92-71.68 87.04-122.88 14.08-1.28 37.12-15.36 43.52-71.68 3.84-29.44-10.24-47.36-19.2-52.48 23.04-70.4 33.28-290.56-128-312.32C606.72 15.36 564.48 0 509.44 0 289.28 3.84 224 168.96 272.64 357.12c-8.96 5.12-23.04 21.76-19.2 52.48 6.4 56.32 29.44 70.4 43.52 71.68 5.12 52.48 69.12 113.92 87.04 122.88 0 37.12 1.28 65.28-2.56 106.24C337.92 828.16 52.48 766.72 38.4 1024l947.2 0C977.92 762.88 686.08 828.16 642.56 709.12z"
                        p-id="1607"
                ></path>
            </svg>
          </div>
                    <el-button
                            class="update-avatar-btn"
                            plain
                            type="primary"
                            v-bp-click="[userInfo.id, '准备修改头像', '用户行为', $route.path]"
                    >修改头像
                    </el-button>
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
          <b>身份证号：</b> {{ userInfo.sfzId }}
        </div>
        <div class="info-item flex-box">
          <b>性别：</b> {{ userInfo.sex ? "男" : "女" }}
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
            <b>政治面貌：</b> {{ userInfo.politicCountenance }}
          </div>
        </div>
        <div class="meta-line flex-box jy-between">
          <div class="info-item flex-box">
            <b>联系手机：</b> {{ userInfo.telPhone }}
          </div>
          <div class="info-item flex-box">
            <b>邮箱：</b> {{ userInfo.email }}
          </div>
        </div>
      </div>
    </div>

    <!--动作按钮-->
    <div class="info-actions flex-box">
      <el-button
        class="lr-gap"
        type="warning"
        plain
        @click="() => $router.push('/dashboard/userInfoUpdate')"
        >修改资料
      </el-button>
      <el-button
        type="primary"
        plain
        @click="() => $router.push('/dashboard/setPasswordProtect')"
        >设置密保
      </el-button>
    </div>
  </div>
</template>

<script>
// import userInfoMock from "../../mock/userInfoShow";
import { mapState } from "vuex";
import { aliStore } from "../../utils/alioss";

export default {
  name: "userInfoShow",
  computed: {
    ...mapState(["userInfo"]),
    ofGrid() {
      return `${this.userInfo.ofGrid.districtName} / ${this.userInfo.ofGrid.communityName}`;
    }
  },
  methods: {
    async uploadAvatar({ file }) {
      // <2> 获取文件名后缀（.jpg）
      let suffix = file.name.substr(file.name.lastIndexOf("."));

      // <3> 拼接一个随机名（157302468994737672324903.jpg）
      const random = file.uid + (Math.random() * 1000000).toFixed(0) + suffix;

      // <4> 拼接处存到阿里oss的完整文件名
      let fileName = "csgs/avatar/" + random;

      // <5> 存入到阿里云oss上
      // eslint-disable-next-line no-unused-vars
      try {
        const { res, url } = await aliStore.put(fileName, file);
        if (res.status === 200) this.$message.success("上传成功");
        const newInfo = Object.assign({}, this.userInfo, { avatarUrl: url });
        this.$store.commit("setUserInfo", { info: newInfo });

        // 提交用户资料修改
        const modifiedRes = await this.$axios.post(
          `/profile/modify/${this.userInfo.id}`,
          {
            avatarUrl: url
          }
        );
        if (modifiedRes.data.resultCode === "200") {
          if (res.status === 200) this.$message.success("修改资料成功");
        }
      } catch (err) {
        this.$message.error(String(err));
      }
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
      padding-top: 30px;

      img {
          box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
          max-width: 100px;
          width: 100%;
          margin-top: 10px;
          border-radius: 20px;
      }

      .update-avatar-btn {
          margin-top: 4px;
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
