<template>
  <el-dialog title="明细导入" :visible.sync="visible" width="600px" @close="handleClose">
    <div style="display: flex; justify-content: space-between; align-items: flex-start;">
      <div style="flex:1; text-align:center;">
        <div style="font-size:40px; color:#67c23a; margin-bottom:8px;">
          <i class="el-icon-upload"></i>
        </div>
        <div>上传填写好的模板</div>
        <el-upload
            :action="importUrl"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="handleSuccess"
            :on-error="handleError"
            :data="{}"
        >
          <el-button type="primary" style="margin-top:8px;">上传文件</el-button>
        </el-upload>
        <div style="font-size:12px; color:#999; margin-top:4px;">支持xls、xlsx，文件大小请控制在10MB内</div>
      </div>
      <div style="flex:1; text-align:center;">
        <div style="font-size:40px; color:#409EFF; margin-bottom:8px;">
          <i class="el-icon-download"></i>
        </div>
        <div>下载模板</div>
        <el-button type="success" style="margin-top:8px;" @click="downloadTemplate">下载模板</el-button>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleConfirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { exportPlanDetails } from '@/api/plan'

export default {
  name: 'ImportDialog',
  props: {
    value: { type: Boolean, default: true },
    importUrl: { type: String, required: true }
  },
  data() {
    return {
      visible: true
    }
  },
  watch: {
    value(val) {
      this.visible = val
    }
  },
  methods: {
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isExcel) {
        this.$message.error('只能上传Excel文件')
      }
      if (!isLt10M) {
        this.$message.error('文件不能超过10MB')
      }
      return isExcel && isLt10M
    },
    handleSuccess(response) {
      if (response && response.code === 0) {
        this.$message.success('导入成功')
        this.$emit('imported')
        this.handleClose()
      } else {
        this.$message.error(response.msg || '导入失败')
      }
    },
    handleError() {
      this.$message.error('导入失败')
    },
    downloadTemplate() {
      exportPlanDetails('template').then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '明细模板.xlsx'
        link.click()
      })
    },
    handleClose() {
      this.visible = false
      this.$emit('close')
    },
    handleConfirm() {
      // Implementation of handleConfirm method
    }
  }
}
</script> 