import axios from "axios";
import Cookies from "js-cookie";

const http = axios.create();
http.defaults.baseURL = "http://112.126.85.20:9090/log/storage/";
http.defaults.withCredentials = true;
http.interceptors.request.use(config => {
  // 设置统一的请求头
  if (Cookies.get("csgs_token")) {
    config.headers.csgs_token = Cookies.get("csgs_token");
    config.headers.Authorization = Cookies.get("JSESSIONID");
  }
  return config;
});

export async function send(config) {
  const [uid, event, type, page] = config;
  const create_time = new Date();
  const localeTimeString = create_time.toLocaleTimeString();
  try {
    const res = await http.post(`${uid}`, {
      uid,
      event,
      type,
      page,
      create_time: `${create_time.getFullYear()}年${create_time.getMonth() +
        1}月${create_time.getDate()}日 ${localeTimeString.slice(
        2,
        localeTimeString.lastIndexOf(":")
      )}`
    });
    if (res.data.resultCode === "200") {
      console.log(`埋点记录 [${type}] - ${create_time} - 日志事件：${event}`);
    } else {
      await Promise.reject("AJAX 请求失败！");
    }
  } catch (err) {
    console.error("上传埋点日志出错... " + String(err));
  }
}
