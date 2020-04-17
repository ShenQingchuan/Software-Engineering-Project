<template>
  <div class="flex-box flex-col jy-center subpage-resident-overview">
    <div class="announcement-show"></div>
    <div class="flex-box jy-center overview-card-group">
      <div class="flex-box overview-card">
        <el-card shadow="hover">
          <svg
            t="1587101150215"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="1927"
            id="mx_n_1587101150216"
            width="50"
            height="50"
          >
            <path
              d="M433.567135 601.28173a138.378378 138.378378 0 1 1 184.569081 0A221.433081 221.433081 0 0 1 747.243243 802.594595a27.675676 27.675676 0 0 1-55.351351 0 166.054054 166.054054 0 1 0-332.108108 0 27.675676 27.675676 0 0 1-55.351352 0 221.433081 221.433081 0 0 1 129.134703-201.312865zM525.837838 581.189189a83.027027 83.027027 0 1 0 0-166.054054 83.027027 83.027027 0 0 0 0 166.054054z"
              fill="#1296db"
              p-id="1928"
              data-spm-anchor-id="a313x.7781069.0.i3"
              class="selected"
            ></path>
            <path
              d="M488.143568 38.745946L23.385946 449.120865a27.675676 27.675676 0 0 0 18.293622 48.432432h137.824864V968.648649a55.351351 55.351351 0 0 0 55.351352 55.351351H819.2a55.351351 55.351351 0 0 0 55.351351-55.351351V497.497946h107.381622a27.675676 27.675676 0 0 0 18.930162-47.878919L562.563459 39.825297a55.351351 55.351351 0 0 0-74.447567-1.051675z m331.028756 403.400649V968.648649H234.883459V442.146595H114.854054L524.758486 80.259459l387.044325 361.887136H819.2z"
              fill="#4D4A49"
              p-id="1929"
            ></path>
          </svg>
          <br />
          本小区居民数量： <span class="population-count">193</span>
        </el-card>
      </div>
      <div class="overview-card">
        <el-card shadow="hover">
          <svg
            t="1587101236970"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="2163"
            data-spm-anchor-id="a313x.7781069.0.i6"
            width="50"
            height="50"
          >
            <path
              d="M949.333333 917.333333H917.333333V117.333333A74.88 74.88 0 0 0 842.666667 42.666667h-661.333334A74.88 74.88 0 0 0 106.666667 117.333333V917.333333H74.666667a32 32 0 0 0 0 64h128a32 32 0 0 0 0-64H170.666667V117.333333a10.666667 10.666667 0 0 1 10.666666-10.666666h661.333334a10.666667 10.666667 0 0 1 10.666666 10.666666V917.333333h-32a32 32 0 0 0 0 64h128a32 32 0 0 0 0-64z"
              fill="#333333"
              p-id="2164"
            ></path>
            <path
              d="M538.666667 245.333333h-181.333334v490.666667a32 32 0 0 0 64 0v-170.666667h117.333334a160 160 0 0 0 0-320z m0 256h-117.333334v-192h117.333334a96 96 0 0 1 0 192z"
              fill="#1296db"
              p-id="2165"
              data-spm-anchor-id="a313x.7781069.0.i7"
              class="selected"
            ></path>
          </svg>
          <br />
          本小区停车位数量：<span class="car-count">100</span>
        </el-card>
      </div>
    </div>
    <div class="actions-group">
      <el-button type="primary" plain>我的车辆信息</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "residentOverview"
};
</script>

<style lang="scss" scoped>
.subpage-resident-overview {
  margin-top: 20px;
  width: 90%;

  .announcement-show {
    width: 100%;
  }
  .overview-card-group {
    margin: 20px;
    width: 100%;
    font-size: 20px;
    font-weight: bold;

    .overview-card {
      svg {
        margin-bottom: 20px;
      }
      margin: 20px;
      .population-count,
      .car-count {
        color: chocolate;
      }
    }
  }
}
</style>
