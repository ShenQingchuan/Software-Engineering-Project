<template>
  <div class="subpage-statistics flex-box flex-col jy-center">
    <v-chart :options="chartOptions"></v-chart>
    <div class="tb-gap sort-options flex-box jy-center">
      <el-button @click="sortDataSourceBy('住房')" type="primary" plain
        >按照住房数排序</el-button
      >
      <el-button @click="sortDataSourceBy('人口')" type="success" plain
        >按照人口数排序</el-button
      >
      <el-button @click="sortDataSourceBy('车位')" type="warning" plain
        >按照车位数排序</el-button
      >
    </div>
  </div>
</template>

<script>
import chartDataMock from "@/mock/charts";

export default {
  name: "statistics",
  data() {
    return {
      chartOptions: {
        legend: {},
        tooltip: {},
        dataset: {
          source: chartDataMock
        },
        xAxis: {
          type: "category",
          axisLabel: {
            internal: 0,
            rotate: -15
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
    }
  }
};
</script>

<style lang="scss" scoped>
.subpage-statistics {
  width: 70%;
  margin: 0 auto;
}
</style>
