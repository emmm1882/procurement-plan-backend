<template>
  <div class="plan-add">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>新增采购计划</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>
      
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="所属年度" prop="year">
          <el-row :gutter="10">
            <el-col :span="8">
              <el-select v-model="yearValue" placeholder="请选择年份">
                <el-option v-for="item in yearList" :key="item" :label="item+'年'" :value="item" />
              </el-select>
            </el-col>
            <el-col :span="10">
              <el-select v-model="periodValue" placeholder="请选择时间段">
                <el-option v-for="item in periodList" :key="item" :label="item" :value="item" />
              </el-select>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="所属公司">
          <el-input v-model="form.company" placeholder="自动带入/可编辑" />
        </el-form-item>
        <el-form-item label="编制部门">
          <el-input v-model="form.dept" placeholder="自动带入/可编辑" />
        </el-form-item>
        <el-form-item label="编制人">
          <el-input v-model="form.creator" placeholder="自动带入/可编辑" />
        </el-form-item>
        <el-form-item label="编制时间">
          <el-date-picker v-model="form.createTime" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd" placeholder="自动带入" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            class="upload-demo"
            action="/api/attachment/upload"
            :on-success="handleUploadSuccess"
            :file-list="fileList"
            :limit="5"
            :on-exceed="handleExceed"
            :before-upload="beforeUpload">
            <el-button size="small" type="primary">上传文件</el-button>
            <div slot="tip" class="el-upload__tip">文件不超过5个，且单个文件不超过100M</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="计划明细">
          <div class="detail-toolbar">
            <el-button type="primary" size="small" @click="handleAddDetail">新增</el-button>
            <el-button type="success" size="small" @click="showImportDialog = true">明细导入</el-button>
            <el-button type="warning" size="small" @click="handleExport">明细导出</el-button>
          </div>
          <el-table :data="form.details" border style="width: 100%">
            <el-table-column type="index" label="序号" width="50" />
            <el-table-column label="采购名称" prop="itemName" width="200">
              <template slot-scope="scope">
                <el-input v-model="scope.row.itemName" placeholder="请输入采购名称" style="width:100%" />
              </template>
            </el-table-column>
            <el-table-column label="采购类别" prop="category" width="140">
              <template slot-scope="scope">
                <el-select v-model="scope.row.category" placeholder="请选择" style="width:100%">
                  <el-option label="B01 材料设备" value="B01 材料设备" />
                  <el-option label="B02 服务" value="B02 服务" />
                  <el-option label="B03 工程" value="B03 工程" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="采购方式" prop="method" width="140">
              <template slot-scope="scope">
                <el-select v-model="scope.row.method" placeholder="请选择" style="width:100%">
                  <el-option label="竞价采购" value="竞价采购" />
                  <el-option label="公开招标" value="公开招标" />
                  <el-option label="单一来源" value="单一来源" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="拟采购估价" prop="estimate" width="150">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.estimate" :min="0" style="width:150px; text-align:right;" />
                <span>元</span>
              </template>
            </el-table-column>
            <el-table-column label="计划采购时间" prop="planTime" width="150">
              <template slot-scope="scope">
                <el-date-picker v-model="scope.row.planTime" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
              </template>
            </el-table-column>
            <el-table-column label="资金来源" prop="fundSource" width="120">
              <template slot-scope="scope">
                <el-select v-model="scope.row.fundSource" placeholder="请选择" style="width:100%">
                  <el-option label="自筹" value="自筹" />
                  <el-option label="拨款" value="拨款" />
                  <el-option label="其他" value="其他" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="备注" prop="remark" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark" placeholder="请输入备注" style="width:100%" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="handleDeleteDetail(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button type="success" @click="submitForm('effect')">生效</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <import-dialog
      v-if="showImportDialog"
      :value="showImportDialog"
      :import-url="`/api/excel/import/${form.id}`"
      @close="showImportDialog = false"
      @imported="onImportDetail"
    />
  </div>
</template>

<script>
import { createPlan, addPlan, importPlanDetails, exportPlanDetails } from '@/api/plan'
import ImportDialog from './ImportDialog.vue'
import moment from 'moment'

export default {
  name: 'PlanAdd',
  components: { ImportDialog },
  data() {
    return {
      form: {
        planName: '',
        year: '',
        company: '',
        dept: '',
        creator: '',
        createTime: new Date().toISOString().substr(0, 10),
        attachment: '',
        details: []
      },
      rules: {
        planName: [
          { required: true, message: '请输入计划名称', trigger: 'blur' }
        ],
        year: [
          { required: true, message: '请选择所属年度', trigger: 'change' }
        ]
      },
      yearList: this.generateYearList(),
      periodList: ['年度', '上半年', '下半年', '一季度'],
      yearValue: '',
      periodValue: '',
      fileList: [],
      uploadUrl: process.env.VUE_APP_BASE_API + '/procurement-plans/attachments',
      uploadHeaders: {
        // 如果需要认证token
        // Authorization: 'Bearer ' + getToken()
      },
      showImportDialog: false
    }
  },
  watch: {
    yearValue(val) { this.updateYearField(); },
    periodValue(val) { this.updateYearField(); },
  },
  methods: {
    generateYearList() {
      const now = new Date();
      const currentYear = now.getFullYear();
      return [currentYear, currentYear + 1, currentYear + 2];
    },
    updateYearField() {
      if (this.yearValue && this.periodValue) {
        this.form.year = `${this.yearValue}年/${this.periodValue}`;
      } else {
        this.form.year = '';
      }
    },
    handleAddDetail() {
      this.form.details.push({
        name: '',
        category: '',
        method: '',
        estimatedAmount: 0,
        plannedTime: '',
        fundSource: '',
        remark: ''
      })
    },
    handleDeleteDetail(index) {
      this.form.details.splice(index, 1)
    },
    onImportDetail() {
      // 导入成功后刷新明细
      this.getDetail()
    },
    handleExport() {
      const id = this.form.id
      if (!id) {
        this.$message.error('请先保存主表后再导出明细')
        return
      }
      exportPlanDetails(id).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '计划明细导出.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
      })
    },
    handleUploadSuccess(response, file, fileList) {
      // response: {originName, filename}
      this.form.attachment = fileList
        .map(f => {
          const r = f.response || {};
          return r.originName && r.filename ? `${r.originName}:${r.filename}` : '';
        })
        .filter(Boolean)
        .join(',');
      this.$message.success('上传成功')
    },
    handleUploadError() {
      this.$message.error('上传失败')
    },
    handleExceed(files, fileList) {
      this.$message.warning('最多只能上传5个文件')
    },
    beforeUpload(file) {
      const isLt100M = file.size / 1024 / 1024 < 100
      if (!isLt100M) {
        this.$message.error('单个文件不能超过100M')
      }
      return isLt100M
    },
    submitForm(type) {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (!this.form.details || this.form.details.length === 0) {
          this.$message.error('请至少填写一条计划明细')
          return
        }
        // 直接拼接' 00:00:00'，不再用moment
        let createTime = this.form.createTime
        if (createTime && /^\d{4}-\d{2}-\d{2}$/.test(createTime)) {
          createTime = createTime + ' 00:00:00'
        }
        const details = this.form.details.map(d => ({
          itemName: d.itemName || d.name || '',
          category: d.category || '',
          method: d.method || '',
          estimate: d.estimate || d.estimatedAmount || 0,
          planTime: d.planTime && /^\d{4}-\d{2}-\d{2}$/.test(d.planTime) ? d.planTime + ' 00:00:00' : d.planTime || d.plannedTime || '',
          fundSource: d.fundSource || '',
          remark: d.remark || ''
        }))
        const status = type === 'effect' ? '已生效' : '已保存'
        addPlan({ ...this.form, createTime, details, status }).then(() => {
          this.$message.success(type === 'effect' ? '生效成功' : '保存成功')
          this.$router.push({ name: 'PlanList' })
        })
      })
    },
    goBack() {
      this.$router.push('/plan/list')
    }
  },
  mounted() {
    // 编辑时自动拆分
    if (this.form.year) {
      const match = this.form.year.match(/(\d{4})年\/(.+)/);
      if (match) {
        this.yearValue = match[1];
        this.periodValue = match[2];
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.plan-add {
  padding: 20px;
}
.box-card {
  margin-bottom: 20px;
}
.detail-toolbar {
  margin-bottom: 20px;
}
.el-input__inner {
  text-align: center;
}
</style>