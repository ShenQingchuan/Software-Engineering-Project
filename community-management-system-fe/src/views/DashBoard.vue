<template>
  <div class="page-home">
    <el-container>
      <el-aside width="200px">
        <common-aside />
      </el-aside>
      <el-container>
        <el-header class="common-header-wrap">
          <common-header />
        </el-header>
        <el-main>
          <div class="main-view">
            <router-view v-if="loadSubView"></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src
import CommonHeader from "@/components/common-header";
import CommonAside from "@/components/common-aside";
import jwt from "jsonwebtoken";
import Cookies from "js-cookie";
import { mapState } from "vuex";

export default {
  name: "Home",
  components: { CommonHeader, CommonAside },
  data() {
    return {
      loadSubView: false
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  watch: {
    async $route(newValue) {
      if (newValue === "/dashboard") {
        if (this.userInfo.userType < 2) {
          // 是居民 or 网格员
          await this.$router.push("/dashboard/residentOverview");
        } else if (this.userInfo.userType === 2) {
          await this.$router.push("/dashboard/statistics");
        }
      }
    }
  },
  async mounted() {
    try {
      console.log("> Dashboard View Mounted. ");
      const jwtData = jwt.decode(Cookies.get("csgs_token"));
      if (jwtData) {
        console.log(jwtData);
        const { id, userType } = jwtData;
        this.$store.commit("setUserInfo", {
          info: Object.assign({}, this.userInfo, {
            id,
            userType,
            sfzId: localStorage.getItem("csgs_sfzId")
          })
        });
        const res = await this.$axios.get(`/profile/getProfile/${id}`);
        // const res = await this.$axios.get(`/admin/getAllAreaList`);
        console.log(res);
        if (res.data.resultCode === "200") {
          // 成功获取到用户资料
          const fullInfo = Object.assign({}, this.userInfo, res.data.data);
          this.$store.commit("setUserInfo", { info: fullInfo });
          this.loadSubView = true;
        }

        if (this.$route.path === "/dashboard") {
          if (userType < 2) {
            // 是居民 or 网格员
            await this.$router.push("/dashboard/residentOverview");
          } else if (userType === 2) {
            await this.$router.push("/dashboard/statistics");
          }
        }
      }
    } catch (err) {
      console.error(err);
      this.$message.error(String(err));
    }
  }
};
</script>

<style lang="scss">
.common-header-wrap {
  height: 66px !important;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.15);
}
.main-view {
  padding: 20px;
}
</style>
