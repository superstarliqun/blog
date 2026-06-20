<template>
  <div class="upload-container">
    <!-- 顶部操作栏 -->
    <div class="top-bar">
      <h2 class="title">
        <i class="el-icon-cloud-upload" /> 多渠道文件上传系统
      </h2>
      <div class="operate-group">
        <el-button type="primary" icon="el-icon-setting" class="config-btn" @click="openConfigModal">
          上传配置管理
        </el-button>
        <el-button plain icon="el-icon-info" class="help-btn" @click="openHelpModal">
          使用帮助
        </el-button>
      </div>
    </div>

    <!-- 上传渠道选择 -->
    <div class="channel-selector">
      <el-radio-group v-model="selectedChannel" class="channel-group">
        <el-radio label="local" class="channel-item">
          <i class="el-icon-folder" />
          <span>本地存储</span>
        </el-radio>
        <el-radio label="ftp" class="channel-item">
          <i class="el-icon-server" />
          <span>FTP服务器</span>
        </el-radio>
        <el-radio label="minio" class="channel-item">
          <i class="el-icon-cloud" />
          <span>MinIO</span>
        </el-radio>
        <el-radio label="qiniu" class="channel-item">
          <i class="el-icon-globe" />
          <span>七牛云</span>
        </el-radio>
      </el-radio-group>
    </div>

    <!-- 分片上传核心区域 -->
    <div class="upload-core">
      <el-upload ref="uploadRef" :auto-upload="false" :file-list="fileList" :on-change="handleFileChange"
                 :on-remove="handleFileRemove" :before-upload="handleBeforeUpload" class="upload-area" drag multiple
      >
        <div class="upload-mask">
          <i class="el-icon-upload2 upload-icon" />
          <div class="upload-text">
            <p class="main-text">拖拽文件至此处上传</p>
            <p class="sub-text">
              支持分片上传、断点续传、秒传 | 单个文件最大 {{ maxFileSize }}GB
            </p>
          </div>
          <el-button type="text" class="browse-btn">浏览文件</el-button>
        </div>
      </el-upload>

      <!-- 文件列表与进度展示 -->
      <div v-if="fileList.length > 0" class="file-list-container">
        <el-table :data="fileList" border :header-cell-style="tableHeaderStyle">
          <el-table-column label="文件名" width="300">
            <template slot-scope="scope">
              <div class="file-name">
                <i :class="getfileIcon(scope.row.raw.type)" />
                <span>{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="大小" width="120">
            <template slot-scope="scope">{{
              formatFileSize(scope.row.size)
            }}</template>
          </el-table-column>
          <el-table-column label="状态" width="120">
            <template slot-scope="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="进度" width="200">
            <template slot-scope="scope">
              <el-progress v-if="scope.row.status === 'uploading'" :percentage="scope.row.progress" size="small"
                           status="success"
              />
              <span v-else-if="scope.row.status === 'success'">100%</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status === 'waiting'" icon="el-icon-play" size="mini"
                         @click="startUpload(scope.row)"
              />
              <el-button v-if="scope.row.status === 'uploading'" icon="el-icon-pause" size="mini"
                         @click="pauseUpload(scope.row)"
              />
              <el-button v-if="scope.row.status === 'paused'" icon="el-icon-play" size="mini"
                         @click="resumeUpload(scope.row)"
              />
              <el-button icon="el-icon-delete" size="mini" type="text" @click="handleFileRemove(scope.row)" />
              <el-button icon="el-icon-info" size="mini" type="text" @click="showFileHash(scope.row)" />
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 批量操作按钮 -->
      <div v-if="fileList.length > 0" class="batch-operate">
        <el-button type="primary" icon="el-icon-play" :disabled="isAllUploaded" @click="batchStartUpload">
          全部开始
        </el-button>
        <el-button plain icon="el-icon-pause" :disabled="!hasUploadingFile" @click="batchPauseUpload">
          全部暂停
        </el-button>
        <el-button plain icon="el-icon-delete" @click="batchRemoveFiles">
          清空列表
        </el-button>
        <el-select v-model="chunkSize" placeholder="分片大小" size="mini" class="chunk-size-select">
          <el-option label="1MB" value="1048576" />
          <el-option label="5MB" value="5242880" />
          <el-option label="10MB" value="10485760" />
          <el-option label="20MB" value="20971520" />
        </el-select>
      </div>
    </div>

    <!-- 配置管理弹窗 -->
    <el-dialog title="上传配置管理" :visible.sync="configModalVisible" width="60%" :close-on-click-modal="false">
      <el-tabs v-model="activeConfigTab" type="border-card">
        <el-tab-pane label="FTP配置" name="ftp">
          <el-form :model="config.ftp" :rules="configRules.ftp" label-width="140px">
            <el-form-item label="服务器地址" prop="host">
              <el-input v-model="config.ftp.host" placeholder="输入FTP服务器IP或域名" />
            </el-form-item>
            <el-form-item label="端口" prop="port">
              <el-input v-model="config.ftp.port" type="number" min="1" max="65535" />
            </el-form-item>
            <el-form-item label="用户名" prop="username">
              <el-input v-model="config.ftp.username" placeholder="输入登录用户名" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="config.ftp.password" type="password" />
            </el-form-item>
            <el-form-item label="远程路径" prop="remotePath">
              <el-input v-model="config.ftp.remotePath" placeholder="例如：/data/upload/" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="MinIO配置" name="minio">
          <el-form :model="config.minio" :rules="configRules.minio" label-width="140px">
            <el-form-item label="Endpoint" prop="endpoint">
              <el-input v-model="config.minio.endpoint" placeholder="MinIO服务地址" />
            </el-form-item>
            <el-form-item label="Access Key" prop="accessKey">
              <el-input v-model="config.minio.accessKey" />
            </el-form-item>
            <el-form-item label="Secret Key" prop="secretKey">
              <el-input v-model="config.minio.secretKey" type="password" />
            </el-form-item>
            <el-form-item label="Bucket名称" prop="bucket">
              <el-input v-model="config.minio.bucket" />
            </el-form-item>
            <el-form-item label="HTTPS协议">
              <el-switch v-model="config.minio.useHttps" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="七牛云配置" name="qiniu">
          <el-form :model="config.qiniu" :rules="configRules.qiniu" label-width="140px">
            <el-form-item label="Access Key" prop="accessKey">
              <el-input v-model="config.qiniu.accessKey" />
            </el-form-item>
            <el-form-item label="Secret Key" prop="secretKey">
              <el-input v-model="config.qiniu.secretKey" type="password" />
            </el-form-item>
            <el-form-item label="存储空间" prop="bucket">
              <el-input v-model="config.qiniu.bucket" />
            </el-form-item>
            <el-form-item label="存储区域" prop="region">
              <el-select v-model="config.qiniu.region">
                <el-option label="华东(z0)" value="z0" />
                <el-option label="华北(z1)" value="z1" />
                <el-option label="华南(z2)" value="z2" />
                <el-option label="北美(na0)" value="na0" />
                <el-option label="东南亚(as0)" value="as0" />
              </el-select>
            </el-form-item>
            <el-form-item label="自定义域名" prop="domain">
              <el-input v-model="config.qiniu.domain" placeholder="可选" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="上传设置" name="upload">
          <el-form :model="uploadSettings" label-width="140px">
            <el-form-item label="最大文件大小(GB)">
              <el-input v-model="uploadSettings.maxFileSize" type="number" min="1" max="100" />
            </el-form-item>
            <el-form-item label="默认分片大小">
              <el-select v-model="uploadSettings.defaultChunkSize">
                <el-option label="1MB" value="1048576" />
                <el-option label="5MB" value="5242880" />
                <el-option label="10MB" value="10485760" />
                <el-option label="20MB" value="20971520" />
              </el-select>
            </el-form-item>
            <el-form-item label="并发上传数">
              <el-input v-model="uploadSettings.concurrent" type="number" min="1" max="10" />
            </el-form-item>
            <el-form-item label="自动秒传检测">
              <el-switch v-model="uploadSettings.autoQuickUpload" />
            </el-form-item>
            <el-form-item label="断点续传保留时间(天)">
              <el-input v-model="uploadSettings.resumeExpire" type="number" min="1" max="30" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="configModalVisible = false">取消</el-button>
        <el-button type="primary" @click="saveConfig">保存配置</el-button>
      </div>
    </el-dialog>

    <!-- 文件哈希值弹窗 -->
    <el-dialog v-if="currentFile" title="文件哈希信息" :visible.sync="hashModalVisible" width="50%"
               :close-on-click-modal="false"
    >
      <div class="hash-info">
        <div class="hash-item">
          <label>文件名称：</label>
          <span>{{ currentFile.name }}</span>
        </div>
        <div class="hash-item">
          <label>文件大小：</label>
          <span>{{ formatFileSize(currentFile.size) }}</span>
        </div>
        <div class="hash-item">
          <label>MD5：</label>
          <el-tooltip content="点击复制" placement="top">
            <span class="hash-value" @click="copyToClipboard(currentFile.hash.md5)">{{ currentFile.hash.md5 }}</span>
          </el-tooltip>
        </div>
        <div class="hash-item">
          <label>SHA1：</label>
          <el-tooltip content="点击复制" placement="top">
            <span class="hash-value" @click="copyToClipboard(currentFile.hash.sha1)">{{ currentFile.hash.sha1 }}</span>
          </el-tooltip>
        </div>
        <div class="hash-item">
          <label>SHA256：</label>
          <el-tooltip content="点击复制" placement="top">
            <span class="hash-value" @click="copyToClipboard(currentFile.hash.sha256)">{{ currentFile.hash.sha256
            }}</span>
          </el-tooltip>
        </div>
        <div class="hash-item">
          <label>唯一标识：</label>
          <span>{{ currentFile.fileId }}</span>
        </div>
      </div>
    </el-dialog>

    <!-- 帮助弹窗 -->
    <el-dialog title="使用帮助" :visible.sync="helpModalVisible" width="50%">
      <div class="help-content">
        <h4>📌 核心功能</h4>
        <ul>
          <li>支持本地、FTP、MinIO、七牛云多渠道上传</li>
          <li>分片上传：大文件分割为小块上传，降低传输风险</li>
          <li>断点续传：网络中断后可恢复上传，无需重新开始</li>
          <li>秒传功能：通过文件哈希值检测，已上传文件直接完成</li>
          <li>文件哈希：支持MD5、SHA1、SHA256三种哈希值生成</li>
        </ul>
        <h4>📋 使用步骤</h4>
        <ol>
          <li>点击「上传配置管理」完善对应渠道的配置信息</li>
          <li>选择上传渠道（本地/FTP/MinIO/七牛云）</li>
          <li>拖拽文件或点击浏览选择需要上传的文件</li>
          <li>选择分片大小（默认5MB）</li>
          <li>点击开始上传，可暂停/恢复/取消上传任务</li>
        </ol>
        <h4>⚠️ 注意事项</h4>
        <ul>
          <li>请确保上传渠道配置信息正确，否则会导致上传失败</li>
          <li>
            分片大小越大，传输速度越快，但断点续传恢复时需要重新上传的内容越多
          </li>
          <li>秒传功能依赖文件哈希值检测，首次上传仍需完整传输</li>
          <li>断点续传信息默认保留7天，超过后需重新上传</li>
        </ul>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 引入哈希计算库（实际项目中需安装：npm install spark-md5 js-sha1 js-sha256）
import SparkMD5 from 'spark-md5'
import sha1 from 'js-sha1'
import sha256 from 'js-sha256'

export default {
  data() {
    return {
      // 基础配置
      selectedChannel: 'local',
      configModalVisible: false,
      helpModalVisible: false,
      hashModalVisible: false,
      activeConfigTab: 'ftp',
      currentFile: null,

      // 上传设置
      maxFileSize: 5, // 默认最大文件大小(GB)
      chunkSize: 5242880, // 默认分片大小(5MB)
      fileList: [],
      uploadSettings: {
        maxFileSize: 5,
        defaultChunkSize: 5242880,
        concurrent: 3,
        autoQuickUpload: true,
        resumeExpire: 7
      },

      // 渠道配置
      config: {
        ftp: {
          host: '',
          port: 21,
          username: '',
          password: '',
          remotePath: '/'
        },
        minio: {
          endpoint: '',
          accessKey: '',
          secretKey: '',
          bucket: '',
          useHttps: false
        },
        qiniu: {
          accessKey: '',
          secretKey: '',
          bucket: '',
          region: 'z0',
          domain: ''
        }
      },

      // 配置校验规则
      configRules: {
        ftp: {
          host: [
            { required: true, message: '请输入服务器地址', trigger: 'blur' }
          ],
          port: [{ required: true, message: '请输入端口', trigger: 'blur' }],
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' }
          ],
          remotePath: [
            { required: true, message: '请输入远程路径', trigger: 'blur' }
          ]
        },
        minio: {
          endpoint: [
            { required: true, message: '请输入Endpoint', trigger: 'blur' }
          ],
          accessKey: [
            { required: true, message: '请输入Access Key', trigger: 'blur' }
          ],
          secretKey: [
            { required: true, message: '请输入Secret Key', trigger: 'blur' }
          ],
          bucket: [
            { required: true, message: '请输入Bucket名称', trigger: 'blur' }
          ]
        },
        qiniu: {
          accessKey: [
            { required: true, message: '请输入Access Key', trigger: 'blur' }
          ],
          secretKey: [
            { required: true, message: '请输入Secret Key', trigger: 'blur' }
          ],
          bucket: [
            { required: true, message: '请输入存储空间名称', trigger: 'blur' }
          ],
          region: [
            { required: true, message: '请选择存储区域', trigger: 'change' }
          ]
        }
      },

      // 上传状态管理
      uploadTasks: new Map() // 存储上传任务：fileId -> task
    }
  },
  computed: {
    // 是否所有文件都已上传完成
    isAllUploaded() {
      return this.fileList.every((file) => file.status === 'success')
    },
    // 是否有正在上传的文件
    hasUploadingFile() {
      return this.fileList.some((file) => file.status === 'uploading')
    }
  },
  created() {
    // 从本地存储加载配置
    this.loadConfig()
    // 初始化分片大小
    this.chunkSize = this.uploadSettings.defaultChunkSize
    // 监听窗口关闭事件，保存上传进度
    window.addEventListener('beforeunload', this.saveUploadProgress)
  },
  beforeDestroy() {
    window.removeEventListener('beforeunload', this.saveUploadProgress)
  },
  methods: {
    // 加载配置
    loadConfig() {
      const savedConfig = localStorage.getItem('uploadConfig')
      const savedSettings = localStorage.getItem('uploadSettings')
      if (savedConfig) {
        this.config = JSON.parse(savedConfig)
      }
      if (savedSettings) {
        this.uploadSettings = JSON.parse(savedSettings)
        this.maxFileSize = this.uploadSettings.maxFileSize
      }
    },

    // 保存配置
    saveConfig() {
      // 校验当前标签页的配置
      const currentRules = this.configRules[this.activeConfigTab]
      if (currentRules) {
        // this.$refs[`configForm${this.activeConfigTab}`]?.validate((valid) => {
        //   if (!valid) return
        //   this.doSaveConfig()
        // })
      } else {
        this.doSaveConfig()
      }
    },

    doSaveConfig() {
      localStorage.setItem('uploadConfig', JSON.stringify(this.config))
      localStorage.setItem(
        'uploadSettings',
        JSON.stringify(this.uploadSettings)
      )
      this.maxFileSize = this.uploadSettings.maxFileSize
      this.chunkSize = this.uploadSettings.defaultChunkSize
      this.$message.success('配置保存成功！')
      this.configModalVisible = false
    },

    // 打开配置弹窗
    openConfigModal() {
      this.configModalVisible = true
    },

    // 打开帮助弹窗
    openHelpModal() {
      this.helpModalVisible = true
    },

    // 文件改变时触发（计算哈希值）
    async handleFileChange(file) {
      // 生成唯一文件ID
      file.fileId = this.generateFileId(file.raw)
      // 初始化文件状态
      file.status = 'waiting'
      file.progress = 0
      file.chunks = []
      file.chunkIndex = 0
      file.hash = null

      try {
        // 计算文件哈希值（MD5、SHA1、SHA256）
        this.$message.info(`正在计算文件哈希值：${file.name}`)
        const hashInfo = await this.calculateFileHash(file.raw)
        file.hash = hashInfo

        // 秒传检测
        if (this.uploadSettings.autoQuickUpload) {
          const isExisted = await this.checkFileExisted(hashInfo.md5)
          if (isExisted) {
            file.status = 'success'
            file.progress = 100
            this.$message.success(`文件 ${file.name} 已存在，秒传成功！`)
            return
          }
        }

        // 分割文件为分片
        this.splitFileIntoChunks(file)

        // 恢复上传进度（如果有）
        this.resumeUploadProgress(file)
      } catch (error) {
        this.$message.error(`文件 ${file.name} 处理失败：${error.message}`)
        file.status = 'error'
      }
    },

    // 生成唯一文件ID（基于文件名+大小+最后修改时间）
    generateFileId(file) {
      return `${file.name}-${file.size}-${file.lastModified}`
    },

    // 计算文件哈希值
    calculateFileHash(file) {
      return new Promise((resolve, reject) => {
        const fileReader = new FileReader()
        const spark = new SparkMD5.ArrayBuffer()
        let chunks = []
        const chunkSize = 2 * 1024 * 1024 // 2MB为单位计算哈希
        let currentChunk = 0

        fileReader.onload = (e) => {
          spark.append(e.target.result)
          currentChunk++

          if (currentChunk < chunks.length) {
            loadNextChunk()
          } else {
            const md5 = spark.end()
            const sha1Hash = sha1(file)
            const sha256Hash = sha256(file)
            resolve({ md5, sha1: sha1Hash, sha256: sha256Hash })
          }
        }

        fileReader.onerror = (error) => {
          reject(error)
        }

        const loadNextChunk = () => {
          const start = currentChunk * chunkSize
          const end = Math.min(start + chunkSize, file.size)
          fileReader.readAsArrayBuffer(file.slice(start, end))
        }

        // 初始化分片
        chunks = Array.from({ length: Math.ceil(file.size / chunkSize) })
        loadNextChunk()
      })
    },

    // 分割文件为分片
    splitFileIntoChunks(file) {
      const chunks = []
      const totalChunks = Math.ceil(file.size / this.chunkSize)

      for (let i = 0; i < totalChunks; i++) {
        const start = i * this.chunkSize
        const end = Math.min(start + this.chunkSize, file.size)
        const chunk = file.raw.slice(start, end)

        chunks.push({
          chunkIndex: i,
          chunkSize: chunk.size,
          start,
          end,
          blob: chunk,
          uploaded: false
        })
      }

      file.chunks = chunks
      file.totalChunks = totalChunks
    },

    // 检查文件是否已存在（秒传）
    checkFileExisted(md5) {
      // 实际项目中需要调用后端接口检查
      return new Promise((resolve) => {
        // 模拟接口延迟
        setTimeout(() => {
          resolve(false) // 默认为不存在，实际根据后端返回调整
        }, 500)
      })
    },

    // 保存上传进度
    saveUploadProgress() {
      const progressMap = {}
      this.fileList.forEach((file) => {
        if (file.status === 'uploading' || file.status === 'paused') {
          progressMap[file.fileId] = {
            progress: file.progress,
            chunkIndex: file.chunkIndex,
            chunks: file.chunks.map((chunk) => ({ ...chunk, blob: null })) // 不保存blob数据
          }
        }
      })
      localStorage.setItem('uploadProgress', JSON.stringify(progressMap))
    },

    // 恢复上传进度
    resumeUploadProgress(file) {
      const progressMap = JSON.parse(
        localStorage.getItem('uploadProgress') || '{}'
      )
      const savedProgress = progressMap[file.fileId]

      if (savedProgress) {
        file.progress = savedProgress.progress
        file.chunkIndex = savedProgress.chunkIndex
        file.chunks = savedProgress.chunks

        // 询问用户是否恢复上传
        this.$confirm(
          `检测到文件 ${file.name} 有未完成的上传任务，是否恢复？`,
          '恢复上传',
          {
            confirmButtonText: '恢复',
            cancelButtonText: '重新上传',
            type: 'info'
          }
        )
          .then(() => {
            file.status = 'paused'
            this.$message.info(`已恢复文件 ${file.name} 的上传进度`)
          })
          .catch(() => {
            // 重新上传，重置进度
            file.progress = 0
            file.chunkIndex = 0
            file.chunks.forEach((chunk) => (chunk.uploaded = false))
          })
      }
    },

    // 开始上传单个文件
    async startUpload(file) {
      if (file.status === 'uploading') return

      // 非本地上传需要检查配置
      if (this.selectedChannel !== 'local' && !this.checkChannelConfig()) {
        this.$message.warning('请先完善上传渠道配置！')
        this.configModalVisible = true
        this.activeConfigTab = this.selectedChannel
        return
      }

      file.status = 'uploading'

      try {
        // 并发上传分片
        const concurrent = this.uploadSettings.concurrent
        const promises = []

        while (file.chunkIndex < file.totalChunks) {
          if (promises.length >= concurrent) {
            await Promise.race(promises)
          }

          const chunk = file.chunks[file.chunkIndex]
          if (!chunk.uploaded) {
            const promise = this.uploadChunk(file, chunk)
            promises.push(promise)
            promise.then(() => {
              const index = promises.indexOf(promise)
              if (index !== -1) promises.splice(index, 1)
            })
          }

          file.chunkIndex++
        }

        // 等待所有分片上传完成
        await Promise.all(promises)

        // 合并分片（调用后端接口）
        await this.mergeChunks(file)

        file.status = 'success'
        file.progress = 100
        this.$message.success(`文件 ${file.name} 上传完成！`)
      } catch (error) {
        file.status = 'error'
        this.$message.error(`文件 ${file.name} 上传失败：${error.message}`)
      }
    },

    // 上传单个分片
    uploadChunk(file, chunk) {
      return new Promise((resolve, reject) => {
        const formData = new FormData()
        formData.append('fileId', file.fileId)
        formData.append('fileName', file.name)
        formData.append('chunkIndex', chunk.chunkIndex)
        formData.append('totalChunks', file.totalChunks)
        formData.append('fileHash', file.hash.md5)
        formData.append('chunk', chunk.blob)
        formData.append('channel', this.selectedChannel)

        // 添加渠道配置参数（实际项目中应通过后端接口传递，这里仅做演示）
        if (this.selectedChannel !== 'local') {
          formData.append(
            'config',
            JSON.stringify(this.config[this.selectedChannel])
          )
        }

        // 实际项目中替换为真实上传接口
        const uploadUrl = `/api/upload/chunk`

        this.$axios
          .post(uploadUrl, formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
            onUploadProgress: (progressEvent) => {
              // 计算单个分片上传进度，并更新总进度
              const chunkProgress = progressEvent.loaded / progressEvent.total
              const totalProgress =
                file.chunkIndex / file.totalChunks +
                chunkProgress / file.totalChunks
              file.progress = Math.round(totalProgress * 100)
            }
          })
          .then((response) => {
            if (response.data.code === 200) {
              chunk.uploaded = true
              resolve()
            } else {
              reject(new Error(response.data.msg))
            }
          })
          .catch((error) => {
            reject(error)
          })
      })
    },

    // 合并分片
    mergeChunks(file) {
      return new Promise((resolve, reject) => {
        // 实际项目中调用后端合并接口
        const mergeUrl = `/api/upload/merge`

        this.$axios
          .post(mergeUrl, {
            fileId: file.fileId,
            fileName: file.name,
            fileHash: file.hash.md5,
            totalChunks: file.totalChunks,
            channel: this.selectedChannel
          })
          .then((response) => {
            if (response.data.code === 200) {
              resolve()
            } else {
              reject(new Error(response.data.msg))
            }
          })
          .catch((error) => {
            reject(error)
          })
      })
    },

    // 暂停上传单个文件
    pauseUpload(file) {
      file.status = 'paused'
      // 实际项目中需要取消当前正在上传的分片请求
      const task = this.uploadTasks.get(file.fileId)
      if (task) {
        task.cancel()
      }
      this.saveUploadProgress()
      this.$message.info(`文件 ${file.name} 上传已暂停`)
    },

    // 恢复上传单个文件
    resumeUpload(file) {
      this.startUpload(file)
    },

    // 批量开始上传
    batchStartUpload() {
      this.fileList.forEach((file) => {
        if (
          file.status === 'waiting' ||
          file.status === 'paused' ||
          file.status === 'error'
        ) {
          this.startUpload(file)
        }
      })
    },

    // 批量暂停上传
    batchPauseUpload() {
      this.fileList.forEach((file) => {
        if (file.status === 'uploading') {
          this.pauseUpload(file)
        }
      })
    },

    // 移除单个文件
    handleFileRemove(file) {
      const index = this.fileList.findIndex(
        (item) => item.fileId === file.fileId
      )
      if (index !== -1) {
        // 取消上传任务
        if (file.status === 'uploading') {
          const task = this.uploadTasks.get(file.fileId)
          task?.cancel()
        }
        this.fileList.splice(index, 1)
        this.saveUploadProgress()
      }
    },

    // 批量移除文件
    batchRemoveFiles() {
      this.$confirm(
        '确定要清空所有文件吗？已上传的文件不会被删除。',
        '确认清空',
        {
          type: 'warning'
        }
      )
        .then(() => {
          // 取消所有上传任务
          this.uploadTasks.forEach((task) => task.cancel())
          this.fileList = []
          this.uploadTasks.clear()
          localStorage.removeItem('uploadProgress')
          this.$message.success('文件列表已清空')
        })
        .catch(() => { })
    },

    // 检查渠道配置是否完整
    checkChannelConfig() {
      const channel = this.selectedChannel
      const config = this.config[channel]
      const rules = this.configRules[channel]

      if (!rules) return true // 没有校验规则的渠道默认通过

      return Object.keys(rules).every((key) => {
        const value = config[key]
        return (
          value !== undefined &&
          value !== null &&
          value.toString().trim() !== ''
        )
      })
    },

    // 上传前校验
    handleBeforeUpload(file) {
      const fileSizeGB = file.size / 1024 / 1024 / 1024
      if (fileSizeGB > this.maxFileSize) {
        this.$message.error(`文件大小不能超过 ${this.maxFileSize}GB！`)
        return false
      }
      return true
    },

    // 显示文件哈希信息
    showFileHash(file) {
      if (!file.hash) {
        this.$message.warning('文件哈希值尚未计算完成！')
        return
      }
      this.currentFile = file
      this.hashModalVisible = true
    },

    // 复制到剪贴板
    copyToClipboard(text) {
      navigator.clipboard
        .writeText(text)
        .then(() => {
          this.$message.success('复制成功！')
        })
        .catch(() => {
          this.$message.error('复制失败，请手动复制')
        })
    },

    // 格式化文件大小
    formatFileSize(size) {
      if (size < 1024) return `${size}B`
      if (size < 1024 * 1024) return `${(size / 1024).toFixed(2)}KB`
      if (size < 1024 * 1024 * 1024) { return `${(size / 1024 / 1024).toFixed(2)}MB` }
      return `${(size / 1024 / 1024 / 1024).toFixed(2)}GB`
    },

    // 获取文件图标
    getfileIcon(fileType) {
      if (fileType.startsWith('image/')) return 'el-icon-picture'
      if (fileType.startsWith('video/')) return 'el-icon-video-play'
      if (fileType.startsWith('audio/')) return 'el-icon-music'
      if (fileType.includes('pdf')) return 'el-icon-file-pdf'
      if (fileType.includes('word')) return 'el-icon-file-word'
      if (fileType.includes('excel')) return 'el-icon-file-excel'
      if (fileType.includes('powerpoint')) return 'el-icon-file-powerpoint'
      if (fileType.includes('text')) return 'el-icon-file-text'
      return 'el-icon-file'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        waiting: '等待上传',
        uploading: '上传中',
        paused: '已暂停',
        success: '上传成功',
        error: '上传失败'
      }
      return statusMap[status] || '未知状态'
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        waiting: 'info',
        uploading: 'primary',
        paused: 'warning',
        success: 'success',
        error: 'danger'
      }
      return typeMap[status] || 'info'
    },

    // 表格头部样式
    tableHeaderStyle() {
      return {
        'background-color': '#f5f7fa',
        'font-weight': 'bold',
        color: '#1989fa'
      }
    }
  }
}
</script>

<style scoped lang="scss">
// 科技简约风格主色调：蓝色系、深色背景、低饱和度
$primary-color: #1989fa;
$secondary-color: #409eff;
$dark-bg: #f5f7fa;
$card-bg: #ffffff;
$border-color: #e8f4f8;
$text-color: #303133;
$light-text: #606266;

.upload-container {
  padding: 20px;
  background-color: $dark-bg;
  min-height: calc(100vh - 40px);
}

// 顶部操作栏
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  .title {
    display: flex;
    align-items: center;
    color: $primary-color;
    font-size: 20px;
    font-weight: 600;

    i {
      margin-right: 8px;
      font-size: 24px;
    }
  }

  .operate-group {
    display: flex;
    gap: 12px;

    .config-btn {
      background-color: $primary-color;
      border-color: $primary-color;

      &:hover {
        background-color: $secondary-color;
        border-color: $secondary-color;
      }
    }

    .help-btn {
      color: $primary-color;
      border-color: $primary-color;
    }
  }
}

// 渠道选择器
.channel-selector {
  margin-bottom: 24px;

  .channel-group {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
  }

  .channel-item {
    display: flex;
    align-items: center;
    padding: 8px 16px;
    border-radius: 6px;
    transition: all 0.3s;

    &:hover {
      background-color: rgba(25, 137, 250, 0.08);
    }

    i {
      font-size: 16px;
      margin-right: 8px;
      color: $primary-color;
    }

    &.is-checked {
      background-color: rgba(25, 137, 250, 0.1);
      border-color: $primary-color;

      span {
        color: $primary-color;
        font-weight: 500;
      }
    }
  }
}

// 上传核心区域
.upload-core {
  background-color: $card-bg;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  padding: 24px;
}

// 上传区域
.upload-area {
  margin-bottom: 24px;

  .upload-mask {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 200px;
    border: 2px dashed $border-color;
    border-radius: 8px;
    transition: all 0.3s;

    &:hover {
      border-color: $primary-color;
      background-color: rgba(25, 137, 250, 0.03);
    }
  }

  .upload-icon {
    font-size: 48px;
    color: $primary-color;
    margin-bottom: 16px;
  }

  .upload-text {
    text-align: center;
    margin-bottom: 16px;

    .main-text {
      font-size: 16px;
      color: $text-color;
      margin-bottom: 4px;
    }

    .sub-text {
      font-size: 12px;
      color: $light-text;
    }
  }

  .browse-btn {
    color: $primary-color;
    border-bottom: 1px dashed $primary-color;
    padding: 0;
    height: auto;

    &:hover {
      color: $secondary-color;
      border-color: $secondary-color;
    }
  }
}

// 文件列表容器
.file-list-container {
  margin-bottom: 20px;

  ::v-deep .el-table {
    border-radius: 8px;
    overflow: hidden;
  }

  ::v-deep .el-table__body tr:hover {
    background-color: rgba(25, 137, 250, 0.03);
  }

  ::v-deep .el-table td {
    border-bottom: 1px solid $border-color;
  }
}

.file-name {
  display: flex;
  align-items: center;

  i {
    margin-right: 8px;
    color: $primary-color;
  }

  span {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

// 批量操作栏
.batch-operate {
  display: flex;
  align-items: center;
  gap: 12px;

  .chunk-size-select {
    margin-left: auto;
    width: 120px;
  }
}

// 哈希信息弹窗
.hash-info {
  padding: 10px 0;

  .hash-item {
    display: flex;
    margin-bottom: 16px;
    align-items: flex-start;

    label {
      width: 80px;
      font-weight: 500;
      color: $text-color;
      margin-right: 12px;
    }

    .hash-value {
      flex: 1;
      word-break: break-all;
      color: $text-color;
      cursor: pointer;
      padding: 2px 4px;
      border-radius: 4px;

      &:hover {
        background-color: rgba(25, 137, 250, 0.08);
      }
    }
  }
}

// 帮助弹窗
.help-content {
  font-size: 14px;
  color: $text-color;

  h4 {
    color: $primary-color;
    margin: 16px 0 8px;
    font-size: 15px;
  }

  ul,
  ol {
    margin-left: 20px;
    margin-bottom: 16px;
    line-height: 1.8;
  }

  li {
    margin-bottom: 4px;
  }
}

// 弹窗样式
::v-deep .el-dialog {
  border-radius: 12px;
  overflow: hidden;
}

::v-deep .el-dialog__header {
  background-color: $dark-bg;
  border-bottom: 1px solid $border-color;
}

::v-deep .el-tabs__header {
  margin-bottom: 16px;
}

::v-deep .el-form-item {
  margin-bottom: 20px;
}

// 响应式适配
@media (max-width: 1200px) {
  .channel-group {
    gap: 16px;
  }

  .batch-operate {
    flex-wrap: wrap;
  }
}

@media (max-width: 768px) {
  .top-bar {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .channel-item {
    width: calc(50% - 8px);
    justify-content: center;
  }

  .upload-mask {
    height: 160px;
  }

  .upload-icon {
    font-size: 36px;
  }
}
</style>
