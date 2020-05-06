<template>
  <div
    v-loading="loadingChart"
    class="subpage-statistics flex-box flex-col jy-center"
  >
    <div class="tb-gap flex-box jy-center">
      <label class="lr-gap"><b>请选择片区：</b></label>
      <el-select
        v-model="selectedDistrict"
        @change="getChartDataAndSetChartOptions"
      >
        <el-option
          v-for="(e, i) in allDistrict"
          :key="i"
          :value="e"
          :label="e"
        ></el-option>
      </el-select>
    </div>

    <v-chart
      v-if="selectedDistrict !== ''"
      class="main-chart"
      :options="chartOptions"
    ></v-chart>
    <div class="tb-gap sort-options flex-box jy-center">
      <el-button @click="sortDataSourceBy('住房')" type="primary" plain
        >按照住房数排序</el-button
      >
      <el-button @click="sortDataSourceBy('车位')" type="warning" plain
        >按照车位数排序</el-button
      >
      <el-button @click="sortDataSourceBy('人口')" type="success" plain
        >按照人口数排序</el-button
      >
    </div>
  </div>
</template>

<script>
import chartDataMock from "@/mock/charts";
import allDistrictMock from "@/mock/getAllDistrict";

export default {
  name: "statistics",
  data() {
    return {
      allDistrict: allDistrictMock,
      selectedDistrict: "",

      loadingChart: false,
      chartOptions: {
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
  methods: {
    sortDataSourceBy(key) {
      this.chartOptions.dataset.source.sort((a, b) => a[key] - b[key]);
    },
    getChartDataAndSetChartOptions() {
      this.loadingChart = true;
      setTimeout(() => {
        this.chartOptions.dataset.source = chartDataMock; // TODO: 这一句后面真实请求时删掉，其他不变
        this.loadingChart = false;
      }, 1000);

      // TODO: 此处是每当 select 组件选择的值变化时，就请求 chart 数据
      // https://easydoc.xyz/p/43159074/MAhLR20e
    }
  }
};
</script>

<style>
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

  .main-chart {
    width: 100%;
  }
}
</style>
