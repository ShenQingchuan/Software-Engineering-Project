<template>
  <el-tabs v-loading="loadingChart" v-model="activeName">
    <el-tab-pane label="各片区总体" name="first">
      <div class="all-district flex-box flex-col jy-center">
        <v-chart
          v-if="allDistrictCharShow"
          class="main-chart"
          :options="allDistrictChartOptions"
        ></v-chart>
        <div class="tb-gap sort-options flex-box jy-center">
          <el-button
            @click="
              sortDataSourceBy(allDistrictChartOptions.dataset.source, '住房')
            "
            type="primary"
            plain
            >按照住房数排序</el-button
          >
          <el-button
            @click="
              sortDataSourceBy(allDistrictChartOptions.dataset.source, '车位')
            "
            type="warning"
            plain
            >按照车位数排序</el-button
          >
          <el-button
            @click="
              sortDataSourceBy(allDistrictChartOptions.dataset.source, '人口')
            "
            type="success"
            plain
            >按照人口数排序</el-button
          >
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="单片区" name="second">
      <div class="subpage-statistics flex-box flex-col jy-center">
        <div class="tb-gap flex-box jy-center">
          <label class="lr-gap"><b>请选择片区：</b></label>
          <el-select
            v-model="selectedDistrict"
            @change="getChartDataAndSetChartOptions"
          >
            <el-option
              v-for="e in allDistrict"
              :key="e.id"
              :value="e.districtName"
              :label="e.districtName"
            ></el-option>
          </el-select>
        </div>
        <v-chart
          v-if="selectedDistrict !== ''"
          class="main-chart"
          autoresize
          :options="singleChartOptions"
        ></v-chart>
        <div class="tb-gap sort-options flex-box jy-center">
          <el-button
            @click="sortDataSourceBy(singleChartOptions.dataset.source, '住房')"
            type="primary"
            plain
            >按照住房数排序</el-button
          >
          <el-button
            @click="sortDataSourceBy(singleChartOptions.dataset.source, '车位')"
            type="warning"
            plain
            >按照车位数排序</el-button
          >
          <el-button
            @click="sortDataSourceBy(singleChartOptions.dataset.source, '人口')"
            type="success"
            plain
            >按照人口数排序</el-button
          >
        </div>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
// import chartForAllMock from "@/mock/chartsForAll";
// import chartDataMock from "@/mock/charts";
// import allDistrictNameMock from "@/mock/getAllDistrict";

export default {
  name: "statistics",
  data() {
    return {
      allDistrict: [],
      selectedDistrict: "",

      // Tab
      activeName: "first",

      loadingChart: false,
      allDistrictCharShow: false,
      allDistrictChartOptions: {
        tooltip: {
          renderMode: "richText"
        },
        dataset: {
          source: []
        },
        xAxis: {
          type: "category",
          axisLabel: {
            internal: 0
          }
        },
        yAxis: {},
        series: [{ type: "bar" }, { type: "bar" }, { type: "bar" }]
      },
      singleChartOptions: {
        grid: {
          x: 50,
          y: 50,
          x2: 50,
          y2: 60
        },
        barGap: "0%",
        legend: {},
        tooltip: {},
        dataset: {
          source: []
        },
        xAxis: {
          type: "category",
          axisLabel: {
            internal: 0
          }
        },
        yAxis: {},
        series: [{ type: "bar" }, { type: "bar" }, { type: "bar" }]
      }
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  async mounted() {
    try {
      const res = await this.$axios.get("/leader/getAllDistrictName");
      resErrorHandler(this, res);
      if (res.data.resultCode === "200") {
        this.allDistrict = res.data.data;
      }

      this.loadingChart = true;
      const rphRes = await this.$axios.get("/leader/getDistrictRPHList");
      console.log(rphRes);
      resErrorHandler(this, rphRes);
      if (rphRes.data.resultCode === "200") {
        this.allDistrictChartOptions.dataset.source = rphRes.data.data.map(
          e => ({
            片区: e.district,
            住房: e.numHouses,
            车位: e.numParkingSpaces,
            人口: e.numResidents
          })
        );
        this.loadingChart = false;
        this.allDistrictCharShow = true;
      }
    } catch (err) {
      this.$message.error(String(err));
    }
  },
  methods: {
    sortDataSourceBy(arr, key) {
      arr.sort((a, b) => a[key] - b[key]);
    },
    async getChartDataAndSetChartOptions(e) {
      let selectedDistrictID;
      this.allDistrict.forEach(item => {
        if (item.districtName === e) {
          selectedDistrictID = item.id;
        }
      });
      this.loadingChart = true;

      try {
        const res = await this.$axios.get(
          `/leader/getCommunityRPHList/${selectedDistrictID}`
        );
        resErrorHandler(this, res);
        if (res.data.resultCode === "200") {
          this.singleChartOptions.dataset.source = res.data.data.map(e => ({
            小区名称: e.community,
            住房: e.numHouses,
            车位: e.numParkingSpaces,
            人口: e.numResidents
          }));
          this.loadingChart = false;
        }
      } catch (err) {
        this.$message.error(String(err));
      }
    }
  }
};
</script>

<style lang="scss">
/**
 * 默认尺寸为 600px×400px，如果想让图表响应尺寸变化，可以像下面这样
 * 把尺寸设为百分比值（同时请记得为容器设置尺寸）。
 */
.echarts {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss" scoped>
.subpage-statistics {
  width: 80%;
  margin: 0 auto;
}
.all-district {
  width: 100%;
}
.main-chart {
  width: 100%;
}
</style>
