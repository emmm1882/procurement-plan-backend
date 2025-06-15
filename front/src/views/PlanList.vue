<template>
  <div class="plan-list">
    <el-card>
      <div class="search-bar" style="margin-bottom: 16px; display: flex; align-items: center;">
        <el-input
            v-model="searchForm.planName"
            placeholder="请输入计划名称"
            style="width: 220px; margin-right: 16px;"
            clearable
        />
        <el-select
            v-model="searchForm.status"
            placeholder="状态"
            style="width: 160px; margin-right: 16px;"
            clearable
        >
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="fetchList">查询</el-button>
        <el-button type="primary" style="margin-left:auto;" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="list" border style="width: 100%;">
        <el-table-column type="selection" align="center" />
        <el-table-column prop="year" label="所属年度" align="center" />
        <el-table-column prop="deptName" label="编制部门" align="center" />
        <el-table-column prop="userName" label="编制人" align="center" />
        <el-table-column prop="createDate" label="编制时间" align="center">
          <template slot-scope="scope">
            {{ scope.row.createDate ? scope.row.createDate.substr(0, 10) : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.status)">
              {{ statusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button v-if="scope.row.status === '已保存'" type="text" @click="handleEdit(scope.row)">修改</el-button>
            <el-button v-if="scope.row.status === '已保存'" type="text" style="color:#f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          style="margin-top: 16px; text-align: right;"
          background
          layout="prev, pager, next, jumper"
          :total="total"
          :page-size="pageSize"
          :current-page.sync="pageNum"
          @current-change="fetchList"
      />
    </el-card>
  </div>
</template>

<script>
import { fetchPlanList, deletePlan } from '@/api/plan'

export default {
  name: 'PlanList',
  data() {
    return {
      searchForm: {
        planName: '',
        status: ''
      },
      statusOptions: [
        { label: '不限', value: '' },
        { label: '已保存', value: '已保存' },
        { label: '审批中', value: '审批中' },
        { label: '审批退回', value: '审批退回' },
        { label: '已生效', value: '已生效' }
      ],
      list: [],
      total: 0,
      pageNum: 1,
      pageSize: 10
    }
  },
  created() {
    this.fetchList()
  },
  methods: {
    fetchList() {
      const userId = localStorage.getItem('userId') || ''
      fetchPlanList({
        ...this.searchForm,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        createUserId: userId
      }).then(res => {
        this.list = res.data.records || []
        this.total = res.data.total || 0
      })
    },
    statusLabel(val) {
      const map = {
        '已保存': '已保存',
        '审批中': '审批中',
        '审批退回': '审批退回',
        '已生效': '已生效'
      }
      return map[val] || val
    },
    statusTagType(val) {
      if (val === '已保存') return 'info'
      if (val === '审批中') return 'warning'
      if (val === '审批退回') return 'danger'
      if (val === '已生效') return 'success'
      return ''
    },
    handleAdd() {
      this.$router.push({ name: 'PlanForm', query: { mode: 'add' } })
    },
    handleEdit(row) {
      this.$router.push({ name: 'PlanForm', query: { mode: 'edit', id: row.id } })
    },
    handleView(row) {
      this.$router.push({ name: 'PlanDetail', query: { id: row.id } })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该采购计划吗？', '提示', { type: 'warning' }).then(() => {
        deletePlan(row.id).then(() => {
          this.$message.success('删除成功')
          this.fetchList()
        })
      })
    }
  }
}
</script>

<style scoped>
.plan-list {
  padding: 24px;
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