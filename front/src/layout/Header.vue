<template>
  <div class="header-bar">
    <div class="logo-area">
      <svg class="logo-svg" width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect x="2" y="6" width="8" height="20" rx="2" fill="#fff"/>
        <rect x="12" y="2" width="8" height="24" rx="2" fill="#fff" fill-opacity="0.8"/>
        <rect x="22" y="10" width="8" height="16" rx="2" fill="#fff" fill-opacity="0.6"/>
      </svg>
      <span class="platform-name">宁波城投集团 | 数字招采平台</span>
    </div>
    <div class="nav-menu-wrap">
      <el-menu mode="horizontal" class="nav-menu" background-color="#2057b1" text-color="#fff" active-text-color="#ffd04b" :default-active="activeMenu">
        <el-menu-item index="home">首页</el-menu-item>
        <el-menu-item index="plan">采购管理</el-menu-item>
        <el-menu-item index="inquiry">询价大厅</el-menu-item>
        <el-menu-item index="support">业务支撑</el-menu-item>
        <el-menu-item index="analysis">数据分析</el-menu-item>
        <el-menu-item index="system">系统管理</el-menu-item>
      </el-menu>
    </div>
    <div class="user-area">
      <el-avatar icon="el-icon-user-solid" size="small" style="background:#fff;color:#2057b1;" />
      <span class="user-name">一部人员001</span>
      <i class="el-icon-arrow-down"></i>
      <el-button icon="el-icon-phone" type="text" circle style="color:#fff;" />
      <el-button icon="el-icon-bell" type="text" circle style="color:#fff;" />
      <el-button icon="el-icon-house" type="text" circle style="color:#fff;" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'Header',
  data() {
    return {
      activeMenu: 'plan'
    }
  }
}
</script>

<style scoped>
.header-bar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  z-index: 1000;
  display: flex;
  align-items: center;
  background: #2057b1;
  color: #fff;
  height: 60px;
  padding: 0 32px;
  box-shadow: 0 2px 8px 0 rgba(32,87,177,0.06);
}
.logo-area {
  display: flex;
  align-items: center;
  min-width: 320px;
  height: 60px;
}
.logo-svg {
  height: 32px;
  width: 32px;
  margin-right: 14px;
}
.platform-name {
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 1px;
}
.nav-menu-wrap {
  flex: 1;
  display: flex;
  justify-content: center;
  height: 60px;
  align-items: center;
}
.nav-menu {
  background: transparent;
  border-bottom: none;
  min-width: 600px;
  height: 60px;
  display: flex;
  align-items: center;
}
.nav-menu .el-menu-item {
  height: 60px !important;
  line-height: 60px !important;
  font-size: 16px;
  padding: 0 24px;
}
.nav-menu .el-menu-item.is-active {
  border-bottom: 3px solid #ffd04b;
  color: #fff;
  font-weight: bold;
  background: transparent !important;
}
.user-area {
  display: flex;
  align-items: center;
  gap: 16px;
  min-width: 220px;
  justify-content: flex-end;
  height: 60px;
}
.user-name {
  margin: 0 8px;
  font-weight: 500;
}
.el-icon-arrow-down {
  font-size: 16px;
  margin-right: 8px;
  color: #fff;
  cursor: pointer;
}
body, #app {
  margin: 0;
  padding: 0;
}
</style> 