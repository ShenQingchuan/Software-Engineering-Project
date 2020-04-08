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
  Aside,
  Message,
  MessageBox,
  Notification,
  Loading
} from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

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
  .use(Footer)
  .use(Form)
  .use(FormItem)
  .use(Input)
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
