export default function resErrorHandler(instance, res) {
  console.log(res);
  if (res.data.resultCode !== "200") {
    instance.$message.error(res.data.msg);
  }
}
