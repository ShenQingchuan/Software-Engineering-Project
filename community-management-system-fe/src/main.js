import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import {
  Button,
  Badge,
  Menu,
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
  Message,
  MessageBox,
  Notification,
  Loading
} from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import "./assets/styles/common.scss"; // 公共样式

Vue.config.productionTip = false;
Vue.use(Button)
  .use(Badge)
  .use(Menu)
  .use(Submenu)
  .use(MenuItem)
  .use(MenuItemGroup)
  .use(DropdownMenu)
  .use(DropdownItem)
  .use(Dropdown)
  .use(Icon)
  .use(Aside)
  .use(Container)
  .use(Header)
  .use(Main)
  .use(Card)
  .use(Footer)
  .use(Form)
  .use(FormItem)
  .use(Input)
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

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
