<template>
  <el-tabs
    @tab-click="tabChangeHandler"
    v-bp-default="[userInfo.id, '访问了数据统计图页', '页面访问', $route.path]"
    v-loading="loadingChart"
    v-model="activeName"
  >
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
            >按照住房数排序
          </el-button>
          <el-button
            @click="
              sortDataSourceBy(allDistrictChartOptions.dataset.source, '车位')
            "
            type="warning"
            plain
            >按照车位数排序
          </el-button>
          <el-button
            @click="
              sortDataSourceBy(allDistrictChartOptions.dataset.source, '人口')
            "
            type="success"
            plain
            >按照人口数排序
          </el-button>
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
            >按照住房数排序
          </el-button>
          <el-button
            @click="sortDataSourceBy(singleChartOptions.dataset.source, '车位')"
            type="warning"
            plain
            >按照车位数排序
          </el-button>
          <el-button
            @click="sortDataSourceBy(singleChartOptions.dataset.source, '人口')"
            type="success"
            plain
            >按照人口数排序
          </el-button>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="用户日志分析" name="third">
      <div class="flex-box flex-col">
        <div class="user-log-type" ref="userLogTypeChart"></div>
        <div class="user-log-time" ref="userLogTimeChart"></div>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import { mapState } from "vuex";
import resErrorHandler from "../../utils/resErrorHandler";
import { send } from "../../utils/burringPoint";
import Echart from "echarts";
import {
  userLogTimeOptionsMaker,
  userLogTypePieOptions,
  singleChartOptions
} from "../../utils/chartOptions";
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
      singleChartOptions,

      userLogTypeChartInstance: false,
      userLogTypeChartOptions: { ...userLogTypePieOptions },
      userLogTimeChartOptions: {}
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  beforeDestroy() {
    this.userLogTypeChartInstance.dispose();
    this.userLogTimeChartInstance.dispose();
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
        this.allDistrictCharShow = true;
      }

      const userLogTypeRes = await this.$axios.get(`/log/AggregateQuery/type`);
      console.log(userLogTypeRes);
      resErrorHandler(this, userLogTypeRes);
      if (rphRes.data.resultCode === "200") {
        const typeData = userLogTypeRes.data.data;
        this.userLogTypeChartOptions.series[0].data = typeData.map(e => ({
          name: e.type,
          value: e.count
        }));
        this.userLogTypeChartInstance = Echart.init(
          this.$refs.userLogTypeChart
        );
        this.userLogTypeChartInstance.setOption(this.userLogTypeChartOptions);
      }

      const timeLogRes = await this.$axios.get(`/log/AggregateQuery/time`);
      console.log(timeLogRes);
      resErrorHandler(this, timeLogRes);
      if (rphRes.data.resultCode === "200") {
        const typeData = timeLogRes.data.data;
        this.userLogTimeChartOptions = userLogTimeOptionsMaker(
          typeData.map(e => e.createTime),
          typeData.map(e => e.count)
        );
        this.userLogTimeChartInstance = Echart.init(
          this.$refs.userLogTimeChart
        );
        this.userLogTimeChartInstance.setOption(this.userLogTimeChartOptions);
      }

      this.loadingChart = false;
    } catch (err) {
      this.$message.error(String(err));
    }
  },
  methods: {
    tabChangeHandler(tab) {
      send([
        this.userInfo.id,
        `切换到了Tab页面: ${tab.label}`,
        "页面访问",
        this.$route.path
      ])
        .then(res => console.log(res))
        .catch(err => console.error(err));

      if (tab.name === "third") {
        this.userlogChartShow = true;
      }
    },
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

.user-log-type {
  width: 300px;
  height: 300px;
}

.user-log-time {
  margin-top: 10px;
  width: 400px;
  height: 300px;
}

.main-chart {
  width: 100%;
}
</style>
