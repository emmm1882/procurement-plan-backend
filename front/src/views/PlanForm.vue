<template>
  <div class="plan-form">
    <el-card>
      <el-page-header @back="$router.back()" content="新增/修改计划" style="margin-bottom: 16px;" />
      <el-form :model="form" :rules="rules" ref="form" label-width="100px" class="form-block">
        <div class="form-title">基本信息</div>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="所属年度" prop="year">
              <el-select v-model="form.year" placeholder="请选择">
                <el-option v-for="item in yearOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="所属公司">
              <el-input v-model="form.company" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="编制部门">
              <el-input v-model="form.deptName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="编制人">
              <el-input v-model="form.userName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="编制时间">
              <el-date-picker v-model="form.createTime" type="date" placeholder="选择日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="附件">
              <el-upload
                  :action="uploadUrl"
                  :file-list="form.attachments"
                  :on-success="handleUploadSuccess"
                  :on-remove="handleRemove"
                  :limit="5"
                  :auto-upload="true"
                  :show-file-list="true"
                  :before-upload="beforeUpload"
              >
                <el-button size="small" type="primary">上传文件</el-button>
                <div slot="tip" class="el-upload__tip">文件不超过5个，且单个文件不超过100M</div>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="form-title">计划明细</div>
        <el-table :data="form.details" border style="margin-bottom: 8px;">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="itemName" label="采购名称">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemName" placeholder="请输入" />
            </template>
          </el-table-column>
          <el-table-column prop="itemType" label="采购类别">
            <template slot-scope="scope">
              <el-select v-model="scope.row.itemType" placeholder="请选择">
                <el-option v-for="item in itemTypeOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="purchaseMethod" label="采购方式">
            <template slot-scope="scope">
              <el-select v-model="scope.row.purchaseMethod" placeholder="请选择">
                <el-option v-for="item in purchaseMethodOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="estimate" label="拟采购估价">
            <template slot-scope="scope">
              <el-input v-model="scope.row.estimate" placeholder="请输入" type="number" />
            </template>
          </el-table-column>
          <el-table-column prop="planDate" label="计划采购时间">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.planDate" type="date" placeholder="选择日期" />
            </template>
          </el-table-column>
          <el-table-column prop="fundSource" label="资金来源">
            <template slot-scope="scope">
              <el-select v-model="scope.row.fundSource" placeholder="请选择">
                <el-option v-for="item in fundSourceOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="text" @click="removeDetail(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" icon="el-icon-plus" @click="addDetail">新增</el-button>
        <el-button @click="showImport = true">明细导入</el-button>
        <el-button @click="exportDetail">明细导出</el-button>
        <div style="margin-top: 24px;">
          <el-button type="primary" @click="submit('save')">保存</el-button>
          <el-button type="success" @click="submit('effect')">生效</el-button>
        </div>
      </el-form>
    </el-card>
    <import-dialog v-if="showImport" @close="showImport = false" @imported="onImportDetail" />
  </div>
</template>

<script>
import { addPlan, updatePlan, getPlanDetail, exportDetail } from '@/api/plan'
import ImportDialog from './ImportDialog.vue'
import moment from 'moment'

export default {
  name: 'PlanForm',
  components: { ImportDialog },
  data() {
    return {
      form: {
        year: '',
        company: '测试人员单位',
        deptName: '测试一部',
        userName: '一部人员001',
        createTime: '',
        attachments: [],
        details: []
      },
      rules: {
        year: [{ required: true, message: '请选择年度', trigger: 'change' }]
      },
      yearOptions: ['2024年/年度', '2024年/上半年', '2024年/下半年', '2025年/年度', '2025年/上半年', '2025年/下半年'],
      itemTypeOptions: ['B01 材料设备', 'B02 服务', 'B03 工程'],
      purchaseMethodOptions: ['竞价采购', '公开招标', '单一来源'],
      fundSourceOptions: ['自筹', '拨款', '其他'],
      showImport: false,
      uploadUrl: '/api/plan/uploadAttachment'
    }
  },
  created() {
    if (this.$route.query.mode === 'edit' && this.$route.query.id) {
      getPlanDetail(this.$route.query.id).then(res => {
        this.form = res.data
      })
    }
  },
  methods: {
    addDetail() {
      this.form.details.push({
        itemName: '',
        itemType: '',
        purchaseMethod: '',
        estimate: '',
        planDate: '',
        fundSource: '',
        remark: ''
      })
    },
    removeDetail(index) {
      this.form.details.splice(index, 1)
    },
    handleUploadSuccess(res, file, fileList) {
      this.form.attachments = fileList
    },
    handleRemove(file, fileList) {
      this.form.attachments = fileList
    },
    beforeUpload(file) {
      const isLt100M = file.size / 1024 / 1024 < 100
      if (!isLt100M) {
        this.$message.error('单个文件不能超过100M')
      }
      return isLt100M
    },
    submit(type) {
      this.$refs.form.validate(valid => {
        if (!valid) return
        let createTime = this.form.createTime
        if (createTime) {
          if (typeof createTime !== 'string') {
            createTime = moment(createTime).format('YYYY-MM-DD HH:mm:ss')
          } else if (/^\d{4}-\d{2}-\d{2}$/.test(createTime)) {
            createTime = createTime + ' 00:00:00'
          }
        }
        const api = this.$route.query.mode === 'edit' ? updatePlan : addPlan
        api({ ...this.form, createTime, status: type === 'effect' ? '已生效' : '已保存' }).then(() => {
          this.$message.success(type === 'effect' ? '生效成功' : '保存成功')
          this.$router.push({ name: 'PlanList' })
        })
      })
    },
    exportDetail() {
      exportDetail({ planId: this.$route.query.id }).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '明细导出.xlsx'
        link.click()
      })
    },
    onImportDetail(list) {
      this.form.details = list
      this.showImport = false
    }
  }
}
</script>

<style scoped>
.plan-form {
  padding: 24px;
}
.form-title {
  font-weight: bold;
  margin: 16px 0 8px 0;
}
</style>