<template>
  <div class="plan-detail">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>采购计划详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>
      
      <el-descriptions class="margin-top" title="基本信息" :column="2" border>
        <el-descriptions-item label="计划名称">{{ planInfo.planName }}</el-descriptions-item>
        <el-descriptions-item label="所属年度">{{ planInfo.year }}</el-descriptions-item>
        <el-descriptions-item label="所属公司">{{ planInfo.company }}</el-descriptions-item>
        <el-descriptions-item label="编制部门">{{ planInfo.dept }}</el-descriptions-item>
        <el-descriptions-item label="编制人">{{ planInfo.creator }}</el-descriptions-item>
        <el-descriptions-item label="编制时间">{{ planInfo.createTime ? planInfo.createTime.substr(0, 10) : '' }}</el-descriptions-item>
        <el-descriptions-item label="附件">
          <div v-if="parsedAttachmentList.length">
            <div v-for="file in parsedAttachmentList" :key="file.filename">
              <el-link :href="`/api/attachment/download/${file.filename}`" target="_blank" type="primary">{{ file.originName }}</el-link>
            </div>
          </div>
          <div v-else>无</div>
        </el-descriptions-item>
      </el-descriptions>

      <div class="attachment-section">
        <h3>附件列表</h3>
        <el-table :data="parsedAttachmentList" border style="width: 100%">
          <el-table-column prop="originName" label="文件名称"></el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleDownload(scope.row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="detail-section">
        <h3>计划明细</h3>
        <el-table :data="detailList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="50" />
          <el-table-column prop="itemName" label="采购名称" />
          <el-table-column prop="category" label="采购类别" />
          <el-table-column prop="method" label="采购方式" />
          <el-table-column prop="estimate" label="拟采购估价">
            <template slot-scope="scope">{{ scope.row.estimate }} 元</template>
          </el-table-column>
          <el-table-column prop="planTime" label="计划采购时间">
            <template slot-scope="scope">{{ scope.row.planTime ? (scope.row.planTime.substr(0, 10)) : '' }}</template>
          </el-table-column>
          <el-table-column prop="fundSource" label="资金来源" />
          <el-table-column prop="remark" label="备注" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getPlanDetail, getPlanDetailList } from '@/api/plan'

export default {
  name: 'PlanDetail',
  data() {
    return {
      planInfo: {},
      attachmentList: [],
      detailList: []
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    getDetail() {
      const id = this.$route.params.id
      getPlanDetail(id).then(response => {
        this.planInfo = response.data
        if (response.data && response.data.attachment) {
          this.attachmentList = response.data.attachment.split(',').filter(f => f).map(f => ({ fileName: f }))
        } else {
          this.attachmentList = []
        }
      })
      getPlanDetailList(id).then(response => {
        this.detailList = response.data
      })
    },
    getStatusType(status) {
      const statusMap = {
        'SAVED': 'info',
        'APPROVING': 'warning',
        'REJECTED': 'danger',
        'EFFECTIVE': 'success'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'SAVED': '已保存',
        'APPROVING': '审批中',
        'REJECTED': '审批退回',
        'EFFECTIVE': '已生效'
      }
      return statusMap[status]
    },
    getCategoryText(category) {
      const categoryMap = {
        'GOODS': '货物',
        'SERVICE': '服务',
        'PROJECT': '工程'
      }
      return categoryMap[category]
    },
    getMethodText(method) {
      const methodMap = {
        'OPEN': '公开招标',
        'INVITE': '邀请招标',
        'NEGOTIATION': '竞争性谈判',
        'INQUIRY': '询价'
      }
      return methodMap[method]
    },
    getFundSourceText(source) {
      const sourceMap = {
        'FISCAL': '财政资金',
        'SELF': '自筹资金',
        'OTHER': '其他'
      }
      return sourceMap[source]
    },
    handleDownload(file) {
      window.open(`/api/attachment/download/${file.filename}`);
    },
    goBack() {
      this.$router.push('/plan/list')
    }
  },
  computed: {
    parsedAttachmentList() {
      if (!this.planInfo.attachment) return [];
      return this.planInfo.attachment.split(',').map(item => {
        const [originName, filename] = item.split(':');
        return { originName, filename };
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.plan-detail {
  padding: 20px;
  
  .box-card {
    margin-bottom: 20px;
  }
  
  .attachment-section,
  .detail-section {
    margin-top: 20px;
    
    h3 {
      margin-bottom: 20px;
      font-weight: normal;
      color: #303133;
    }
  }
}
</style> 