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
import addOfficer from "../views/subviews/addOfficer";
import OfficerAddUser from "../views/subviews/OfficerAddUser";
import statistics from "../views/subviews/statistics";
import officerManage from "../views/subviews/officerManage";
import Cookies from "js-cookie";
import setPasswordProtect from "../views/subviews/setPasswordProtect";
import Search from "../views/subviews/search";

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
        path: "userInfoShow/:uid",
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
          title: buildTitle("新建日志")
        }
      },
      {
        path: "addOfficer",
        name: "addOfficer",
        component: addOfficer,
        meta: {
          title: buildTitle("添加网格员")
        }
      },
      {
        path: "officerManage",
        name: "officerManage",
        component: officerManage,
        meta: {
          title: buildTitle("网格员管理")
        }
      },
      {
        path: "officerAddUser",
        name: "officerAddUser",
        component: OfficerAddUser,
        meta: {
          title: buildTitle("添加用户")
        }
      },
      {
        path: "statistics",
        name: "statistics",
        component: statistics,
        meta: {
          title: buildTitle("数据视图")
        }
      },
      {
        path: "/dashboard/setPasswordProtect",
        name: "/dashboard/setPasswordProtect",
        component: setPasswordProtect,
        meta: {
          title: buildTitle("设置密保")
        }
      },
      {
        path: "/dashboard/search",
        name: "/dashboard/search",
        component: Search,
        meta: {
          title: buildTitle("全文检索")
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
  console.log(`${from.path} -> ${to.path}`);
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  if (to.path === "/sign" && Cookies.get("csgs_token") !== undefined) {
    Vue.prototype.$message.warning("您已经登录啦！");
  }
  if (to.path !== "/sign" && Cookies.get("csgs_token") === undefined) {
    next({
      path: "/sign"
    });
  }
  next();
});

export default router;
