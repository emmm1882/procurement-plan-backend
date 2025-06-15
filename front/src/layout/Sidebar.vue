<template>
  <el-menu :default-active="activeMenu" class="sidebar-menu" background-color="#f7f9fa" text-color="#333" active-text-color="#2256d3" unique-opened>
    <el-submenu index="plan">
      <template slot="title"><i class="el-icon-document"></i>采购计划管理</template>
      <el-menu-item index="plan-year">年度计划编制</el-menu-item>
      <el-menu-item index="plan-month">月度计划列表</el-menu-item>
      <el-menu-item index="plan-group">集团年度计划汇总</el-menu-item>
      <el-menu-item index="plan-company">公司年度计划汇总</el-menu-item>
      <el-menu-item index="plan-dept">部门年度计划汇总</el-menu-item>
    </el-submenu>
    <el-submenu index="apply">
      <template slot="title"><i class="el-icon-folder"></i>采购申请管理</template>
      <el-menu-item index="apply-project">项目立项</el-menu-item>
      <el-menu-item index="apply-collect">集采需求</el-menu-item>
    </el-submenu>
    <el-submenu index="solution">
      <template slot="title"><i class="el-icon-suitcase"></i>采购方案管理</template>
      <el-menu-item index="solution-main">采购方案</el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: 'Sidebar',
  data() {
    return {
      activeMenu: 'plan-year'
    }
  }
}
</script>

<style scoped>
.sidebar-menu {
  width: 220px;
  min-height: 100vh;
  border-right: 1px solid #e6e6e6;
  background: #f7f9fa;
  padding-top: 8px;
}
.el-menu-item.is-active {
  background: #e6f0ff !important;
  color: #2256d3 !important;
}
</style> 