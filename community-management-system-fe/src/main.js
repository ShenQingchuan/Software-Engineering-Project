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

Vue.use(mavonEditor);

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
