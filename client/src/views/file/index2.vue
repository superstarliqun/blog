<template>
  <div class="file-upload-container">
    <el-card class="box-card" header="文件上传">
      <el-form ref="form" :model="form" label-width="82px">
        <el-form-item label="分片大小：">
          <el-select v-model="value" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <el-divider />
      <el-upload class="upload-demo" drag :action="uploadUrl" :headers="uploadHeaders" :data="uploadExtraData"
                 :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" :on-success="handleSuccess"
                 :on-error="handleError" :on-progress="handleProgress" :file-list="fileList" multiple :limit="fileLimit"
                 :on-exceed="handleExceed"
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <div slot="tip" class="el-upload__tip">
          <p>
            支持格式：**JPG/PNG/PDF** 等，大小不超过 **10MB**。
          </p>
          <p>
            可一次性上传多个文件 (最多 {{ fileLimit }} 个)。
          </p>
        </div>
      </el-upload>

      <div class="control-panel">
        <el-date-picker v-model="param.uploadDate" type="date" placeholder="选择文件日期" size="mini"
                        value-format="yyyy-MM-dd"
        />
        <el-input v-model="firstValue" placeholder="输入文件编号 (纯数字)" size="mini" style="width: 200px; margin-right: 10px;"
                  @input="firstValue = firstValue.toString().replace(/^(0+)|[^\d]+/g, '')"
        />
        <el-button type="primary" size="mini" plain @click="submitFiles">
          <i class="el-icon-upload2" /> 确认上传
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
// 导入新的 Stomp 客户端
import { Client } from '@stomp/stompjs'
export default {
  data() {
    return {
      // 您的原始数据
      stompClient: null,
      value: null,
      firstValue: null,
      param: {
        uploadDate: null // 用于记录上传的日期信息
      },
      form: {},
      options: [
        { value: 'option1', label: '05 MB' },
        { value: 'option2', label: '10 MB' },
        { value: 'option3', label: '15 MB' },
        { value: 'option4', label: '20 MB' }
      ],
      // 文件上传相关配置
      uploadUrl: 'http://localhost:8585/file/upload/direct', // **请替换为您的Java后端文件上传接口地址**
      fileLimit: 5, // 限制最大上传文件数量
      fileList: [], // 已上传文件列表，用于展示
      uploadHeaders: {
        // 'Authorization': 'Bearer ' + localStorage.getItem('token') // 如果需要身份验证
      },
      uploadExtraData: {
        // extra_param: 'value' // 上传时携带的额外参数，例如业务ID等
      }
    }
  },
  created() {
    // 页面创建时可以执行一些初始化操作，例如获取已上传文件列表
    // this.getUploadedFiles()
  },
  beforeDestroy() {
    // 组件销毁前断开连接
    if (this.stompClient) {
      this.stompClient.deactivate()
    }
  },
  methods: {
    // ---------------------- 文件上传核心方法 ----------------------

    /**
     * @description 文件上传成功时的钩子
     * @param response 后端返回的响应数据
     * @param file 当前上传的文件信息
     * @param fileList 当前的文件列表
     */
    handleSuccess(response, file, fileList) {
      // 上传成功后，通常会拿到后端返回的文件路径/ID等信息
      console.log('文件上传成功:', response)
      this.$message.success(`文件 ${file.name} 上传成功！`)
      this.connectWebSocket('123123')
      // 可在此处处理业务逻辑，例如将文件ID保存到 this.fileList
      // file.backendId = response.data.id
    },

    /**
     * @description 文件上传失败时的钩子
     */
    handleError(err, file, fileList) {
      console.error('文件上传失败:', err)
      this.$message.error(`文件 ${file.name} 上传失败，请重试。`)
    },

    /**
     * @description 文件上传时的钩子（显示进度条）
     */
    handleProgress(event, file, fileList) {
      // event.percent 就是当前上传进度
      // Element UI 会自动显示进度条，这里可以用于额外的逻辑
      console.log(`文件 ${file.name} 上传进度：${event.percent}%`)
    },

    /**
     * @description 删除文件时的钩子
     */
    handleRemove(file, fileList) {
      console.log('文件被移除:', file, fileList)
      // 如果文件已上传到服务器，您需要在此处调用一个API来删除服务器上的文件
      // this.deleteFileFromServer(file.backendId)
    },

    /**
     * @description 点击已上传的文件时的钩子（通常用于下载或预览）
     */
    handlePreview(file) {
      console.log('预览/下载文件:', file)
      // window.open(file.url) // file.url是文件的下载地址
    },

    /**
     * @description 移除文件之前的钩子，可用于二次确认
     */
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },

    /**
     * @description 文件数量超出限制时的钩子
     */
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 ${this.fileLimit} 个文件，本次选择了 ${files.length} 个，共选择了 ${files.length + fileList.length} 个`)
    },

    // ---------------------- 自定义业务方法 ----------------------

    /**
     * @description 模拟的业务数据获取方法 (保留您的原始逻辑)
     */
    getQuery() {
      console.log('当前的日期选择值：', this.param.uploadDate)
      console.log('当前的文件编号：', this.firstValue)
      this.$message.info('这里可以执行获取/查询业务逻辑')
    },

    /**
     * @description 确认所有文件上传并提交业务数据
     */
    submitFiles() {
      // 1. 检查是否有文件正在上传
      // 2. 检查必须字段是否填写 (如 this.param.uploadDate, this.firstValue)

      // 注意： el-upload 会在选择文件后**自动**开始上传（如果没有设置 `auto-upload` 为 false）
      // 这里的 '确认上传' 按钮通常用于：
      //   A) 如果 `auto-upload` 为 false，则调用 this.$refs.upload.submit() 触发上传。
      //   B) 收集所有已上传成功的文件ID和表单数据 (this.param)，一起提交给后端业务接口。
      this.$message.success('业务数据和文件信息已提交！')
    },
    connectWebSocket(taskId) {
      // 1. 创建新的 Client 实例
      const client = new Client({
        // 2. 配置 brokerURL：后端 ws/progress 接口
        // 这里的 URL 必须是完整的 ws 或 wss 地址
        brokerURL: 'ws://localhost:8585/ws/progress',

        // 4. 连接成功回调
        onConnect: () => {
          console.log('WebSocket Connection Successful!')

          // 订阅主题：这里使用 subscribe 方法
          client.subscribe(`/topic/upload/${taskId}`, (message) => {
            const body = JSON.parse(message.body)

            // ... 你的进度处理逻辑（与之前相同）...
            if (body.percent >= 90) {
              this.customProgress = body.percent
            }
            if (body.percent === 100 || body.percent === -1) {
              client.deactivate() // 完成后断开连接
            }
          })
        },

        // 5. 连接断开或错误回调
        onStompError: (frame) => {
          console.error('Broker reported error: ' + frame.headers['message'])
        }

        // 6. 心跳配置 (可选，默认每 10s 发送，每 10s 接收)
        // heartbeatIncoming: 10000,
        // heartbeatOutgoing: 10000,

        // 7. 自动重连配置 (可选，默认为 500ms)
        // reconnectDelay: 5000,
      })

      // 8. 激活连接
      client.activate()
      this.stompClient = client // 保存客户端实例
    }

  }
}
</script>

<style scoped>
.file-upload-container {
  padding: 20px;
}

.box-card {
  max-width: 800px;
  margin: 0 auto;
}

.upload-demo {
  margin-bottom: 20px;
}

.el-upload__tip p {
  line-height: 1.5;
  color: #909399;
  font-size: 13px;
}

.control-panel {
  margin-top: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  /* 元素间的间距 */
}
</style>
