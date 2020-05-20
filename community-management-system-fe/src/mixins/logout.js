import Cookies from "js-cookie";

export default {
  methods: {
    logout() {
      console.log("退出登录");
      Cookies.remove("csgs_token");
      Cookies.remove("JSESSIONID");
      this.$router.push("/sign");
      this.$message.success("退出登录成功!");
    }
  }
};
