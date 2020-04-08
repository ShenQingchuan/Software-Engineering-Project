import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

Vue.use(VueRouter);

function buildTitle(name) {
  return `${name} - 社会综合治理系统`;
}

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      title: buildTitle("总览台")
    }
  },
  {
    path: "/sign",
    name: "Sign",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/Sign.vue"),
    meta: {
      title: buildTitle("登录|注册")
    }
  }
];

const router = new VueRouter({
  routes
});

// 重写标签页标题：
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  next();
});

export default router;
