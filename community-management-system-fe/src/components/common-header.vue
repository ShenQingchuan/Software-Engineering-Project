<template>
  <div class="common-header">
    <!--用户操作下拉菜单-->
    <el-dropdown class="user-dropdown">
      <span class="user-dropdown-btn">
        {{ userInfo.userName }}
        <i class="el-icon-arrow-down el-icon--right" />
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <div @click="$router.push('/dashboard/passwordReset')">
            修改密码
          </div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <!--其他操作按钮-->
    <ul class="actions">
      <li @click="logout"><i class="el-icon-switch-button"></i></li>
    </ul>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import logout from "@/mixins/logout";
import { send } from "../utils/burringPoint";
import { mapState } from "vuex";

export default {
  name: "common-header",
  mixins: [logout],
  data() {
    return {
      notificationCount: 5,
      username: "Tom"
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  methods: {
    logout() {
      console.log("正在退出登录...");
      send([this.userInfo.id, "退出登录了", "行为操作", "/logout"])
        .then(res => console.log(res))
        .catch(err => console.error(err))
        .then(() => {
          Cookies.remove("csgs_token");
          Cookies.remove("JSESSIONID");
          this.$router.push("/sign");
          this.$nextTick(() => this.$message.success("退出登录成功!"));
        });
    }
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
