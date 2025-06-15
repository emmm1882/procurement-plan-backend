<template>
  <div class="plan-detail">
    <el-card>
      <el-page-header @back="$router.back()" content="查看计划" style="margin-bottom: 16px;" />
      <div class="form-title">基本信息</div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="计划名称">{{ detail.planName }}</el-descriptions-item>
        <el-descriptions-item label="所属年度">{{ detail.year }}</el-descriptions-item>
        <el-descriptions-item label="所属公司">{{ detail.company }}</el-descriptions-item>
        <el-descriptions-item label="编制部门">{{ detail.deptName }}</el-descriptions-item>
        <el-descriptions-item label="编制人">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="编制时间">{{ detail.createDate }}</el-descriptions-item>
        <el-descriptions-item label="附件">
          <div v-if="detail.attachments && detail.attachments.length">
            <div v-for="file in detail.attachments" :key="file.url">
              <el-link :href="file.url" target="_blank" type="primary">{{ file.name }}</el-link>
            </div>
          </div>
          <div v-else>无</div>
        </el-descriptions-item>
      </el-descriptions>
      <div class="form-title" style="margin-top:24px;">计划明细</div>
      <el-table :data="detail.details" border>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="itemName" label="采购名称" />
        <el-table-column prop="itemType" label="采购类别" />
        <el-table-column prop="purchaseMethod" label="采购方式" />
        <el-table-column prop="estimate" label="拟采购估价" />
        <el-table-column prop="planDate" label="计划采购时间" />
        <el-table-column prop="fundSource" label="资金来源" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getPlanDetail } from '@/api/plan'

export default {
  name: 'PlanDetail',
  data() {
    return {
      detail: {
        planName: '',
        year: '',
        company: '',
        deptName: '',
        userName: '',
        createDate: '',
        attachments: [],
        details: []
      }
    }
  },
  created() {
    const id = this.$route.query.id
    if (id) {
      getPlanDetail(id).then(res => {
        this.detail = res.data
      })
    }
  }
}
</script>

<style scoped>
.plan-detail {
  padding: 24px;
}
.form-title {
  font-weight: bold;
  margin: 16px 0 8px 0;
}
</style>