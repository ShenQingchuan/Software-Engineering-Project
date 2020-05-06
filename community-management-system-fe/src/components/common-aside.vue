<template>
  <div class="common-aside">
    <!--logo与网站名称-->
    <div class="logo">
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
      <div class="title">社会综合<br />治理系统</div>
    </div>

    <br />
    <!--侧边栏菜单-->
    <div class="menu">
      <el-menu :default-openeds="['1']">
        <el-submenu index="1">
          <template slot="title"
            ><i class="el-icon-s-operation" />常用操作
          </template>
          <!--居民用户操作组-->
          <el-menu-item-group>
            <el-menu-item
              @click="() => $router.push('/dashboard/residentOverview')"
              index="1-1"
            >
              <i class="el-icon-guide" />
              <a>信息纵览</a>
            </el-menu-item>
            <el-menu-item
              @click="() => $router.push('/dashboard/userInfoShow/' + uid)"
              index="1-2"
            >
              <i class="el-icon-set-up" />
              <a>信息更新</a>
            </el-menu-item>
          </el-menu-item-group>

          <!--网格员操作组-->
          <el-menu-item-group>
            <el-menu-item
              @click="() => $router.push('/dashboard/userManage')"
              index="2-1"
            >
              <i class="el-icon-user" />
              <a>用户管理</a>
            </el-menu-item>
            <el-menu-item
              @click="() => $router.push('/dashboard/announcementManage')"
              index="2-2"
            >
              <i class="el-icon-chat-line-round" />
              公告管理
            </el-menu-item>
            <el-menu-item
              @click="() => $router.push('/dashboard/logManage')"
              index="2-3"
            >
              <i class="el-icon-notebook-1" />
              日志管理
            </el-menu-item>
          </el-menu-item-group>

          <!--领导操作组-->
          <el-menu-item-group>
            <template slot="title">领导功能组：</template>
            <el-menu-item
              @click="() => $router.push('/dashboard/statistics')"
              index="3-1"
            >
              <i class="el-icon-data-analysis" />
              <a>数据视图</a>
            </el-menu-item>
          </el-menu-item-group>

          <!--  系统管理员操作组-->
          <el-menu-item-group>
            <template slot="title">系统管理员功能组：</template>
            <el-menu-item
              @click="() => $router.push('/dashboard/addOfficer')"
              index="4-1"
            >
              <i class="el-icon-medal" />
              <a>添加网格员</a>
            </el-menu-item>
            <el-menu-item
              @click="() => $router.push('/dashboard/officerManage')"
              index="4-1"
            >
              <i class="el-icon-connection" />
              <a>网格员管理</a>
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
      </el-menu>
    </div>
  </div>
</template>

<script>
export default {
  name: "common-aside",
  mounted() {
    this.uid = localStorage.getItem("uid");
  },
  data() {
    return {
      uid: ""
    };
  }
};
</script>

<style lang="scss" scoped>
.common-aside {
  padding: 10px 0;

  .logo {
    display: flex;
    justify-content: center;
    align-items: center;

    .title {
      font-weight: bold;
      margin-left: 16px;
    }

    svg {
      width: 30px;
      height: 30px;
    }
  }

  .menu {
    width: 100%;
    text-align: left;
  }
}
</style>
