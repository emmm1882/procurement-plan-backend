<template>
  <div id="app">
    <Header />
    <div class="main-layout">
      <Sidebar />
      <div class="main-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import Header from './layout/Header.vue'
import Sidebar from './layout/Sidebar.vue'

export default {
  name: 'App',
  components: { Header, Sidebar }
}
</script>

<style>
#app {
  height: 100vh;
  background: #f5f7fa;
}
.main-layout {
  display: flex;
  height: calc(100vh - 56px); /* Header高度 */
}
.main-content {
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  overflow: auto;
}
</style>

<style>
.sidebar-menu {
  position: fixed;
  top: 60px;
  left: 0;
  width: 220px;
  min-height: calc(100vh - 56px);
  border-right: 1px solid #e6e6e6;
  background: #f7f9fa;
  padding-top: 8px;
  z-index: 100;
}
.main-content {
  flex: 1;
  padding: 24px;
  background: #f5f7fa;
  overflow: auto;
}
</style>