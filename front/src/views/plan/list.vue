<template>
  <div class="plan-list">
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="计划名称">
          <el-input v-model="queryParams.name" placeholder="请输入计划名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="计划状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="不限" value=""></el-option>
            <el-option label="已保存" value="SAVED"></el-option>
            <el-option label="审批中" value="APPROVING"></el-option>
            <el-option label="审批退回" value="REJECTED"></el-option>
            <el-option label="已生效" value="EFFECTIVE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
        <el-form-item style="float:right; margin-left:auto;">
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table
      v-loading="loading"
      :data="planList"
      border
      style="width: 100%;">
      <el-table-column prop="year" label="所属年度" align="center" />
      <el-table-column prop="dept" label="编制部门" align="center" />
      <el-table-column prop="creator" label="编制人" align="center" />
      <el-table-column prop="createTime" label="编制时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime ? scope.row.createTime.substr(0, 10) : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="text" @click="handleView(scope.row)">查看</el-button>
          <el-button v-if="scope.row.status === 'SAVED' || scope.row.status === '已保存' || scope.row.status === 'REJECTED' || scope.row.status === '审批退回'" type="text" @click="handleEdit(scope.row)">修改</el-button>
          <el-button v-if="scope.row.status === 'SAVED' || scope.row.status === '已保存'" type="text" style="color:#f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getPlanList, deletePlan } from '@/api/plan'

export default {
  name: 'PlanList',
  data() {
    return {
      loading: false,
      planList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        status: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const userId = localStorage.getItem('userId') || ''
      getPlanList({ ...this.queryParams, createUserId: userId }).then(response => {
        this.planList = response.data.list
        this.total = response.data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    handleAdd() {
      this.$router.push('/plan/add')
    },
    handleEdit(row) {
      this.$router.push(`/plan/edit/${row.id}`)
    },
    handleView(row) {
      this.$router.push(`/plan/detail/${row.id}`)
    },
    handleDelete(row) {
      this.$confirm('确认删除该计划吗？', '提示', {
        type: 'warning'
      }).then(() => {
        deletePlan(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    },
    getStatusType(status) {
      const statusMap = {
        'SAVED': 'info',
        '已保存': 'info',
        'APPROVING': 'warning',
        '审批中': 'warning',
        'REJECTED': 'danger',
        '审批退回': 'danger',
        'EFFECTIVE': 'success',
        '已生效': 'success'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        'SAVED': '已保存',
        '已保存': '已保存',
        'APPROVING': '审批中',
        '审批中': '审批中',
        'REJECTED': '审批退回',
        '审批退回': '审批退回',
        'EFFECTIVE': '已生效',
        '已生效': '已生效'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.plan-list {
  padding: 20px;
  
  .search-bar {
    margin-bottom: 20px;
    padding: 20px;
    background: #fff;
    border-radius: 4px;
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.el-table th, .el-table td {
  text-align: center !important;
  vertical-align: middle !important;
  white-space: nowrap;
}

.el-table {
  width: 100%;
}
</style> 