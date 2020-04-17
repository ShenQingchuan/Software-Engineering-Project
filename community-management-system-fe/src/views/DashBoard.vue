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
            <router-view></router-view>
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

export default {
  name: "Home",
  components: { CommonHeader, CommonAside }
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
