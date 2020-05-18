import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import moment from "moment";
import {
  Button,
  Badge,
  Radio,
  RadioGroup,
  Menu,
  Checkbox,
  CheckboxGroup,
  Submenu,
  MenuItem,
  MenuItemGroup,
  Container,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  Icon,
  Header,
  Main,
  Footer,
  Form,
  FormItem,
  Input,
  Tabs,
  TabPane,
  Tag,
  Table,
  TableColumn,
  Aside,
  Card,
  Select,
  Upload,
  Option,
  Steps,
  Step,
  TimePicker,
  DatePicker,
  Pagination,
  Message,
  MessageBox,
  Notification,
  Loading
} from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import "./assets/styles/common.scss";
// 公共样式

import mavonEditor from "mavon-editor";
import "mavon-editor/dist/css/index.css";

import ECharts from "vue-echarts"; // 在 webpack 环境下指向 components/ECharts.vue

// 手动引入 ECharts 各模块来减小打包体积
import "echarts/lib/chart/bar";
import "echarts/lib/component/tooltip";
import "echarts/lib/component/legend";
import axios from "axios";

// 注册组件后即可使用
Vue.component("v-chart", ECharts);
Vue.use(mavonEditor);

import Cookies from "js-cookie";

// 配置 axios
axios.defaults.baseURL = "http://112.126.85.20:9090/";
axios.defaults.withCredentials = true;
axios.interceptors.request.use(config => {
  // 设置统一的请求头
  if (Cookies.get("csgs_token")) {
    config.headers.csgs_token = Cookies.get("csgs_token");
    config.headers.Authorization = Cookies.get("JSESSIONID");
  }
  return config;
});

Vue.prototype.$axios = axios;

Vue.config.productionTip = false;
Vue.use(Button)
  .use(Badge)
  .use(Checkbox)
  .use(Menu)
  .use(Submenu)
  .use(MenuItem)
  .use(MenuItemGroup)
  .use(DropdownMenu)
  .use(TimePicker)
  .use(DatePicker)
  .use(DropdownItem)
  .use(Dropdown)
  .use(Icon)
  .use(Aside)
  .use(Container)
  .use(Header)
  .use(Main)
  .use(Card)
  .use(Footer)
  .use(Radio)
  .use(RadioGroup)
  .use(Form)
  .use(Upload)
  .use(Select)
  .use(Steps)
  .use(Step)
  .use(Option)
  .use(Pagination)
  .use(FormItem)
  .use(Input)
  .use(CheckboxGroup)
  .use(Table)
  .use(TableColumn)
  .use(Tag)
  .use(TabPane)
  .use(Tabs)
  .use(Loading.directive);

Vue.prototype.$loading = Loading.service;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;
Vue.prototype.$moment = moment;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
