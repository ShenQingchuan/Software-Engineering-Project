import Vue from "vue";
import VueRouter from "vue-router";
import DashBoard from "../views/DashBoard.vue";
import userManage from "../views/subviews/userManage";
import residentOverview from "../views/subviews/residentOverview";
import userInfoShow from "../views/subviews/userInfoShow";
import userInfoUpdate from "../views/subviews/userInfoUpdate";
import announcementManage from "../views/subviews/announcementManage";
import passwordReset from "../views/subviews/passwordReset";
import editAnnouncement from "../views/subviews/editAnnouncement";
import logManage from "../views/subviews/logManage";
import editLog from "../views/subviews/editLog";
import officerManage from "../views/subviews/officerManage";

Vue.use(VueRouter);

function buildTitle(name) {
  return `${name} - 社会综合治理系统`;
}

const routes = [
  {
    path: "/",
    redirect: "/dashboard"
  },
  {
    path: "/dashboard",
    name: "DashBoard",
    component: DashBoard,
    meta: {
      title: buildTitle("总览台")
    },
    children: [
      {
        path: "userManage",
        name: "userManage",
        component: userManage,
        meta: {
          title: buildTitle("用户管理")
        }
      },
      {
        path: "residentOverview",
        name: "residentOverview",
        component: residentOverview,
        meta: {
          title: buildTitle("居民用户总览页")
        }
      },
      {
        path: "userInfoShow",
        name: "userInfoShow",
        component: userInfoShow,
        meta: {
          title: buildTitle("我的个人信息")
        }
      },
      {
        path: "userInfoUpdate",
        name: "userInfoUpdate",
        component: userInfoUpdate,
        meta: {
          title: buildTitle("修改个人信息")
        }
      },
      {
        path: "announcementManage",
        name: "announcementManage",
        component: announcementManage,
        meta: {
          title: buildTitle("管理公告")
        }
      },
      {
        path: "logManage",
        name: "logManage",
        component: logManage,
        meta: {
          title: buildTitle("管理日志")
        }
      },

      {
        path: "passwordReset",
        name: "passwordReset",
        component: passwordReset,
        meta: {
          title: buildTitle("修改密码")
        }
      },
      {
        path: "addAnnouncement",
        name: "addAnnouncement",
        component: editAnnouncement,
        meta: {
          title: buildTitle("新建公告")
        }
      },
      {
        path: "addLog",
        name: "addLog",
        component: editLog,
        meta: {
          title: buildTitle("新建公告")
        }
      },
      {
        path: "officerManage",
        name: "officerManage",
        component: officerManage,
        meta: {
          title: buildTitle("网格员管理")
        }
      }
    ]
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
  if (to.path !== "/sign" && window.localStorage.logined !== "1") {
    next({ path: "/sign" });
    Vue.prototype.$message.warning("检测到您还未登录,请登录后操作！");
  }
  next();
});

export default router;
