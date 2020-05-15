const oss = require("ali-oss");

export const aliStore = new oss({
  accessKeyId: "LTAILomQSvnSX2JA",
  accessKeySecret: "IvvtTBHhu7x13rxiZ4hrIhmUWvOwvT",
  bucket: "rpzoss",
  region: "oss-cn-chengdu"
});

export const OSS_URL = "http://rpzoss.oss-cn-chengdu.aliyuncs.com/";
