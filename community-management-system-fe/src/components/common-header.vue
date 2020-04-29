<template>
  <div class="common-header">
    <!--用户操作下拉菜单-->
    <el-dropdown class="user-dropdown">
      <span class="user-dropdown-btn">
        {{ username }}
        <i class="el-icon-arrow-down el-icon--right" />
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <div @click="$router.push('/dashboard/passwordReset')">
            修改密码
          </div>
        </el-dropdown-item>
        <el-dropdown-item>退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <!--其他操作按钮-->
    <ul class="actions">
      <li>
        <el-badge
          :hidden="notificationCount === 0"
          :max="99"
          :value="notificationCount"
          class="badge"
        >
          <i class="el-icon-bell"></i>
        </el-badge>
      </li>
      <li><i class="el-icon-setting"></i></li>
      <li><i class="el-icon-switch-button"></i></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "common-header",
  data() {
    return {
      notificationCount: 5,
      username: "Tom"
    };
  }
};
</script>

<style lang="scss" scoped>
.common-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;

  .user-dropdown {
    font-weight: bold;

    .user-dropdown-btn {
      cursor: pointer;
    }
  }

  .actions {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
    list-style: none;

    li {
      padding: 5px;

      .badge {
        margin-right: 4px;
      }

      i,
      .badge i {
        font-size: 16px;
        font-weight: bold;
        margin: 0 5px;
        cursor: pointer;
      }
    }
  }
}
</style>
