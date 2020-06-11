export const userLogTypePieOptions = {
  title: {
    left: "center",
    text: "前端操作日志类型饼图："
  },
  tooltip: {
    trigger: "item",
    formatter: "{a}<br/>{b}：{c}次 ({d}%)",
    position: function(pos, params, dom, rect, size) {
      // 鼠标在左侧时 tooltip 显示到右侧，鼠标在右侧时 tooltip 显示到左侧。
      let obj = { top: 60 };
      obj[["left", "right"][+(pos[0] < size.viewSize[0] / 2)]] = 5;
      return obj;
    }
  },
  series: [
    {
      name: "日志类型",
      type: "pie",
      radius: ["40%", "80%"],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: "center"
      },
      labelLine: {
        show: false
      },
      data: []
      // { value: 335, name: '零钱通' },
      // { value: 110, name: '零钱+' },
      // { value: 24, name: '零钱' }
    }
  ]
};

export function userLogTimeOptionsMaker(timeList, valueList) {
  return {
    title: {
      left: "center",
      text: "前端操作日志数量，按日期聚合："
    },
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: timeList
    },
    yAxis: {
      type: "value"
    },
    series: [
      {
        data: valueList,
        type: "line",
        areaStyle: {}
      }
    ]
  };
}

export const singleChartOptions = {
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
};
