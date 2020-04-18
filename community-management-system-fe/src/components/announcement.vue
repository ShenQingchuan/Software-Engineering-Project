<template>
  <div class="flex-box flex-col comp-announcement">
    <div class="announcement-header">
      社区最新公告 <br />
      <a href="">查看更多</a>
    </div>

    <div
      v-for="(a, i) in announcement"
      :key="i"
      class="flex-box jy-start as-start announcement-item"
    >
      <div v-if="a.coverUrl && a.coverUrl.length !== 0" class="cover-img">
        <img :src="a.coverUrl" alt="" />
      </div>
      <div class="content-box jy-start  flex-box flex-col">
        <div class="title">{{ a.title }}</div>
        <div class="content-slice">{{ a.content.slice(0, 140) }} ...</div>
        <div class="meta">
          创建者：{{ a.creatorName }} ｜ 时间：{{
            moment(new Date(a.createTime)).calendar()
          }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import announcementMock from "../mock/announcement";
import moment from "moment";

export default {
  name: "announcement",
  data() {
    return {
      moment,
      announcement: announcementMock
    };
  }
};
</script>

<style lang="scss" scoped>
.comp-announcement {
  width: 100%;
  box-sizing: border-box;

  .announcement-header {
    font-weight: bold;
    font-size: 20px;
    margin: 5px 0;

    a,
    a:visited,
    a:link {
      position: relative;
      font-weight: normal;
      font-size: 14px;
      border-bottom: 0 solid #2c3e50;
    }
    a::after {
      position: absolute;
      z-index: 1;
      left: 0;
      top: 20px;
      content: "";
      width: 0;
      height: 3px;
      background-color: #2c3e506e;
      border-radius: 25%;
      transition: width 0.4s ease;
    }
    a:hover {
      &::after {
        width: 100%;
      }
    }
  }

  .announcement-item {
    border: 1px solid rgba(0, 0, 0, 0.09);
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
    box-sizing: border-box;
    width: 80%;
    padding: 20px;
    margin: 5px 0;

    .cover-img {
      img {
        width: 100px;
      }
    }
    .content-box {
      margin-left: 20px;
      align-items: start;
      .title {
        font-size: 16px;
        font-weight: bold;
      }
      .content-slice {
        text-align: left;
        font-size: 14px;
        color: dimgray;
      }
      .meta {
        margin-top: 10px;
        font-size: 12px;
        color: grey;
      }
    }
  }
}
</style>
