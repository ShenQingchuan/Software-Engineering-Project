<template>
  <div class="page-sign">
    <div class="back-canvas"></div>
    <div class="sign">
      <!--登录页标题,Logo-->
      <svg
        class="logo"
        version="1.1"
        viewBox="0 0 1024 1024"
        x="1585996662776"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M672 512l137.504 117.184A64 64 0 0 1 832 677.888V928h-160V512zM256 160h288a64 64 0 0 1 64 64v704H192V224a64 64 0 0 1 64-64z"
          fill="#FFCB01"
        ></path>
        <path
          d="M608 256V96H192v832h416V256z m64 56.896l202.528 180A64 64 0 0 1 896 540.736V928h32a32 32 0 0 1 0 64H96a32 32 0 0 1 0-64h32V96a64 64 0 0 1 64-64h416a64 64 0 0 1 64 64v216.896zM672 928h160V540.736l-160-142.208V928zM288 288h224a32 32 0 0 1 0 64H288a32 32 0 0 1 0-64z m0 192h224a32 32 0 0 1 0 64H288a32 32 0 0 1 0-64z m0 192h224a32 32 0 0 1 0 64H288a32 32 0 0 1 0-64z"
          fill="#1C412F"
        ></path>
      </svg>
      <h2 class="title">社会综合治理系统</h2>
      <h4>Comprehensive social governance system</h4>

      <!--登录表单-->
      <div class="sign-in-form">
        <el-form :model="signInform" label-width="30px" ref="form">
          <el-form-item>
            <template v-slot:label>
              <div class="label">
                <el-icon class="icon el-icon-user"></el-icon>
              </div>
            </template>
            <el-input
              placeholder="请输入用户名"
              v-model="signInform.userId"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <template v-slot:label>
              <div class="label">
                <el-icon class="icon el-icon-key"></el-icon>
              </div>
            </template>
            <el-input
              placeholder="请输入密码"
              v-model="signInform.password"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>

      <!--其他帮助-->
      <div class="help">
        <a href="#">忘记密码</a>
        <a href="#">找回密码</a>
      </div>

      <!--登录按钮-->
      <el-button
        @click="signIn"
        class="sign-in-btn"
        plain
        round
        size="small"
        type="primary"
      >
        登录
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "page-sign",
  data() {
    return {
      signInform: {
        userId: "",
        password: ""
      }
    };
  },
  methods: {
    signIn() {
      // TODO: 登录功能
      this.$message.warning("暂未实现登录！");
    }
  }
};
</script>

<style lang="scss" scoped>
.page-sign {
  .back-canvas {
    width: 100%;
    height: 50%;
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    background: linear-gradient(#9dd3fa, #faffeb, white);
  }

  .sign {
    width: 600px;
    margin: 80px auto;
    padding: 20px 30px;
    background: #fff;
    border: 1px solid rgba(0, 0, 0, 0.15);
    box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-radius: 10px;

    .title {
      color: darkblue;
    }

    .logo {
      width: 60px;
      margin: 0 0 20px 0;
    }

    .sign-in-form {
      width: 54%;
      margin: 20px 0 0 0;

      .label {
        text-align: left;
        font-weight: 500;
        font-size: 16px;
      }

      .icon {
        margin: 0 5px;
        font-weight: bold;
      }
    }

    .help {
      width: 56%;
      margin: 0 0 10px 0;
      display: flex;
      justify-content: flex-end;
      align-items: center;
      font-size: 13px;
      color: #2c3e50;

      a,
      a:active,
      a:visited,
      a:link {
        cursor: pointer;
        margin: 0 10px;
        color: #2c3e50;
        text-decoration: none;
      }
    }

    .sign-in-btn {
      width: 55%;
      font-size: 17px;
    }
  }
}
</style>
