<template>
  <div id="app" v-bp-default="[userInfo.id, '登录了页面', '页面访问', '/']">
    <router-view/>
  </div>
</template>

<script>
  import {mapState} from "vuex";

  export default {
    computed: {
      ...mapState(["userInfo"])
    }
  };
</script>

<style lang="scss">
  * {
    margin: 0;
    padding: 0;
  }

  a,
  a:link,
  a:visited {
  color: #2c3e50;
  text-decoration: none;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  font-weight: normal;
}
</style>
