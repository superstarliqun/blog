<template>
  <div class="explorer-container">
    <!-- 顶部导航栏 -->
    <div class="explorer-header">
      <div class="path-bar">
        <button class="path-btn" @click="goBack">←</button>
        <span class="current-path">{{ currentPath }}</span>
      </div>
      <div class="view-controls">
        <button
          class="view-btn"
          :class="{ active: viewMode === 'list' }"
          @click="viewMode = 'list'"
        >
          列表
        </button>
        <button
          class="view-btn"
          :class="{ active: viewMode === 'icon' }"
          @click="viewMode = 'icon'"
        >
          图标
        </button>
      </div>
    </div>
    <!-- 主体内容 -->
    <div class="explorer-body">
      <!-- 左侧导航栏 -->
      <div class="sidebar">
        <ul class="disk-list">
          <li class="disk-item" @click="selectDisk('C:')">
            <i class="icon disk-icon">🖥️</i>
            <span>C: 本地磁盘</span>
          </li>
          <li class="disk-item" @click="selectDisk('D:')">
            <i class="icon disk-icon">🖥️</i>
            <span>D: 数据磁盘</span>
          </li>
          <li class="disk-item" @click="selectDisk('E:')">
            <i class="icon disk-icon">🖥️</i>
            <span>E: 娱乐磁盘</span>
          </li>
        </ul>
        <el-divider></el-divider>
        <ul class="folder-tree">
          <li
            v-for="(folder, index) in folderList"
            :key="index"
            class="folder-item"
          >
            <div>
              <i
                class="icon folder-toggle"
                :class="{ expanded: folder.expanded }"
                @click="toggleFolder(folder)"
              >
                {{ folder.expanded ? '▼' : '▶' }}
              </i>
              <i class="icon folder-icon">📁</i>
              <span @click="openFolder(folder.path)">{{ folder.name }}</span>
            </div>
            <!-- 子文件夹 -->
            <ul
              v-if="folder.expanded && folder.children.length"
              class="sub-folder-list"
            >
              <li
                v-for="(sub, subIndex) in folder.children"
                :key="subIndex"
                class="sub-folder-item"
              >
                <i class="icon folder-icon">📁</i>
                <span @click="openFolder(sub.path)">{{ sub.name }}</span>
              </li>
            </ul>
          </li>
        </ul>
        <div class="aggregate">
          <div class="user-info">
            <!-- <i class="icon user-icon">👤</i> -->
            <span>👤{{ currentUser.name }} ({{ currentUser.account }})</span>
          </div>
          <div class="disk-space-info">
            <div class="storage-container">
              <!-- 容量信息区 -->
              <div class="storage-info">
                <div class="upgrade-link">
                  <span>容量统计</span>
                </div>
                <!-- 进度条 -->
                <div class="progress-container">
                  <div
                    class="progress-bar"
                    :style="{ width: usageRate + '%' }"
                  ></div>
                </div>

                <!-- 容量统计 -->
                <div class="capacity-stats">
                  <span class="used-capacity"
                    >{{ usedCapacity }}/{{ totalCapacity }}</span
                  >
                  <span class="usage-percent">{{ usageRate }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧文件列表 -->
      <div class="file-content">
        <!-- 面包屑导航 -->
        <div class="breadcrumb">
          <span
            v-for="(crumb, index) in breadcrumbList"
            :key="index"
            class="breadcrumb-item"
            @click="jumpToPath(crumb.path)"
          >
            {{ crumb.name }}
            <span v-if="index < breadcrumbList.length - 1"> > </span>
          </span>
        </div>

        <!-- 列表视图 -->
        <div v-if="viewMode === 'list'" class="file-list-view">
          <table class="file-table">
            <thead>
              <tr>
                <th>名称</th>
                <th>类型</th>
                <th>修改日期</th>
                <th>大小</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(item, index) in currentFiles"
                :key="index"
                :class="'file-row' + (index % 2 !== 0 ? ' file-row-lan' : '')"
                @click="openItem(item)"
                @contextmenu.prevent="onContextmenu"
              >
                <td>
                  <i class="icon">{{ item.type === 'folder' ? '📁' : '📄' }}</i>
                  {{ item.name }}
                </td>
                <td>{{ item.type === 'folder' ? '文件夹' : '文本文档' }}</td>
                <td>{{ item.modifyTime }}</td>
                <td>{{ item.size }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 图标视图 -->
        <div v-if="viewMode === 'icon'" class="file-icon-view">
          <div
            v-for="(item, index) in currentFiles"
            :key="index"
            class="file-icon-item"
            @click="openItem(item)"
          >
            <div class="icon-wrapper">
              <i class="icon large-icon">{{
                item.type === 'folder' ? '📁' : '📄'
              }}</i>
            </div>
            <div class="file-name">{{ item.name }}</div>
          </div>
        </div>

        <!-- 左下角状态信息栏 -->
        <div class="status-bar">
          <div class="file-count">
            <span>{{ currentFiles.length }} 个项目</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 当前路径
      currentPath: 'C:/',
      // 视图模式：list-列表，icon-图标
      viewMode: 'list',
      // 文件夹树数据
      folderList: [
        {
          name: '桌面',
          path: 'C:/桌面',
          expanded: false,
          children: [
            { name: '我的文档', path: 'C:/桌面/我的文档' },
            { name: '下载文件', path: 'C:/桌面/下载文件' }
          ]
        },
        {
          name: '文档',
          path: 'C:/文档',
          expanded: false,
          children: [
            { name: '工作文件', path: 'C:/文档/工作文件' },
            { name: '个人笔记', path: 'C:/文档/个人笔记' }
          ]
        },
        {
          name: '图片',
          path: 'C:/图片',
          expanded: false,
          children: [
            { name: '壁纸', path: 'C:/图片/壁纸' },
            { name: '截图', path: 'C:/图片/截图' }
          ]
        }
      ],
      // 面包屑导航
      breadcrumbList: [],
      // 当前路径下的文件/文件夹列表
      currentFiles: [],
      // 当前用户信息
      currentUser: {
        name: '张三',
        account: 'zhangsan',
        role: '管理员'
      },
      // 磁盘空间信息
      diskSpaceData: {
        'C:': {
          name: 'C盘 (系统盘)',
          total: '100 GB',
          used: '65.2 GB',
          free: '34.8 GB',
          usageRate: 65.2
        },
        'D:': {
          name: 'D盘 (数据盘)',
          total: '500 GB',
          used: '280.5 GB',
          free: '219.5 GB',
          usageRate: 56.1
        },
        'E:': {
          name: 'E盘 (娱乐盘)',
          total: '1000 GB',
          used: '450.8 GB',
          free: '549.2 GB',
          usageRate: 45.1
        }
      },
      // 当前选中磁盘的空间信息
      currentDiskInfo: {},
      // 容量数据
      usedCapacity: '1.2T',
      totalCapacity: '2.0T',
      usageRate: 57 // 使用率百分比
    }
  },
  created() {
    // 初始化加载C盘数据
    this.loadPathData(this.currentPath)
    // 初始化磁盘信息
    this.currentDiskInfo = this.diskSpaceData['C:']
  },
  methods: {
    // 加载指定路径的文件数据
    loadPathData(path) {
      this.currentPath = path
      // 更新面包屑
      this.updateBreadcrumb()
      // 更新磁盘信息
      const disk = path.split('/')[0]
      if (this.diskSpaceData[disk]) {
        this.currentDiskInfo = this.diskSpaceData[disk]
      }
      // 模拟获取文件数据（实际项目中替换为接口请求）
      this.currentFiles = this.mockFileData(path)
    },

    // 模拟文件数据
    mockFileData(path) {
      const baseData = [
        // 通用文件夹
        {
          name: '系统文件',
          type: 'folder',
          modifyTime: '2025/11/28 10:30',
          size: '-',
          path: `${path}/系统文件`
        },
        {
          name: '用户数据',
          type: 'folder',
          modifyTime: '2025/11/27 15:45',
          size: '-',
          path: `${path}/用户数据`
        },
        // 通用文件
        {
          name: '日志文件.txt',
          type: 'file',
          modifyTime: '2025/11/29 09:12',
          size: '2.5 KB',
          path: `${path}/日志文件.txt`
        },
        {
          name: '配置文件.ini',
          type: 'file',
          modifyTime: '2025/11/29 08:45',
          size: '1.2 KB',
          path: `${path}/配置文件.ini`
        }
      ]

      // 根据不同路径添加个性化数据
      if (path.includes('桌面')) {
        baseData.push({
          name: '快捷方式.lnk',
          type: 'file',
          modifyTime: '2025/11/28 16:30',
          size: '1.0 KB',
          path: `${path}/快捷方式.lnk`
        })
      } else if (path.includes('图片')) {
        baseData.push({
          name: '风景.jpg',
          type: 'file',
          modifyTime: '2025/11/26 14:20',
          size: '1.8 MB',
          path: `${path}/风景.jpg`
        })
      }

      return baseData
    },

    // 更新面包屑导航
    updateBreadcrumb() {
      const pathArr = this.currentPath.split('/').filter((item) => item)
      this.breadcrumbList = pathArr.map((item, index) => {
        const path = pathArr.slice(0, index + 1).join('/')
        return {
          name: item,
          path: index === 0 ? `${item}/` : `/${path}/`
        }
      })
    },

    // 选择磁盘
    selectDisk(disk) {
      this.loadPathData(`${disk}/`)
    },

    // 展开/折叠文件夹
    toggleFolder(folder) {
      folder.expanded = !folder.expanded
    },

    // 打开文件夹/文件
    openItem(item) {
      if (item.type === 'folder') {
        this.loadPathData(item.path)
      } else {
        // 模拟打开文件
        alert(`打开文件：${item.name}`)
      }
    },

    // 打开指定文件夹
    openFolder(path) {
      this.loadPathData(path)
    },

    // 面包屑跳转
    jumpToPath(path) {
      this.loadPathData(path)
    },

    // 返回上一级
    goBack() {
      const pathArr = this.currentPath.split('/').filter((item) => item)
      if (pathArr.length <= 1) return // 已经是根目录

      const newPath = pathArr.slice(0, -1).join('/') + '/'
      this.loadPathData(newPath)
    },
    onContextmenu(event) {
      this.$contextmenu({
        items: [
          {
            label: '下载',
            icon: 'el-icon-download',
            onClick: () => {
              console.log('返回(B)')
            }
          },
          {
            label: '返回(B)',
            onClick: () => {
              console.log('返回(B)')
            }
          },
          { label: '重命名', disabled: true },
          { label: '重新加载(R)', divided: true, icon: 'el-icon-refresh' },
          { label: '另存为(A)...' },
          { label: '打印(P)...', icon: 'el-icon-printer' },
          { label: '投射(C)...', divided: true },
          {
            label: '使用网页翻译(T)',
            divided: true,
            minWidth: 0,
            children: [{ label: '翻译成简体中文' }, { label: '翻译成繁体中文' }]
          },
          {
            label: '截取网页(R)',
            minWidth: 0,
            children: [
              {
                label: '截取可视化区域',
                onClick: () => {
                  this.message = '截取可视化区域'
                  console.log('截取可视化区域')
                }
              },
              { label: '截取全屏' }
            ]
          },
          { label: '查看网页源代码(V)', icon: 'el-icon-view' },
          { label: '检查(N)' }
        ],
        event, // 鼠标事件信息
        customClass: 'custom-class', // 自定义菜单 class
        zIndex: 3, // 菜单样式 z-index
        minWidth: 230 // 主菜单最小宽度
      })
      return false
    }
  }
}
</script>

<style scoped>
/* 整体容器 */
.explorer-container {
  width: 100%;
  height: 100%;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  display: flex;
  flex-direction: column;
}

/* 顶部导航 */
.explorer-header {
  height: 40px;
  background-color: #f1f8ff;
  border-bottom: 1px solid #ccc;
  padding: 0 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.path-bar {
  display: flex;
  align-items: center;
}

.path-btn {
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 2px;
  cursor: pointer;
  margin-right: 8px;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.current-path {
  background-color: #fff;
  border: 1px solid #ccc;
  padding: 4px 8px;
  border-radius: 2px;
  min-width: 300px;
}

.view-controls {
  display: flex;
  gap: 5px;
}

.view-btn {
  padding: 4px 10px;
  border: 1px solid #ccc;
  background-color: #e8e8e8;
  cursor: pointer;
  border-radius: 2px;
}

.view-btn.active {
  background-color: #d0d0d0;
  border-color: #999;
}

/* 主体内容 */
.explorer-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 左侧侧边栏 */
.sidebar {
  width: 250px;
  background-color: #fff;
  border-right: 1px solid #ccc;
  padding: 10px 0;
  overflow-y: auto;
  position: relative;
}

.disk-list,
.folder-tree {
  list-style: none;
  padding: 0;
  margin: 0;
}

.disk-item {
  padding: 6px 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.disk-item:hover {
  background-color: #e8e8e8;
}

.folder-tree {
  /* margin-top: 15px; */
}

.folder-item {
  padding: 4px 15px;
  cursor: default;
  display: flex;
  align-items: flex-start;
  flex-direction: column;
  gap: 6px;
}

.sub-folder-list {
  list-style: none;
  padding-left: 25px;
  margin: 0;
}

.sub-folder-item {
  padding: 4px 0;
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}

.sub-folder-item:hover {
  background-color: #e8e8e8;
}

.folder-toggle {
  cursor: pointer;
  font-size: 12px;
  width: 16px;
  display: inline-block;
  text-align: center;
}

/* 右侧文件内容区 */
.file-content {
  flex: 1;
  background-color: #fff;
  padding: 10px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* 面包屑 */
.breadcrumb {
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.breadcrumb-item {
  cursor: pointer;
  color: #0066cc;
}

.breadcrumb-item:hover {
  text-decoration: underline;
}

/* 列表视图 */
.file-list-view {
  width: 100%;
  flex: 1;
  overflow-y: auto;
}

.file-table {
  width: 100%;
  font-size: 14px;
  border-collapse: collapse;
}

.file-table th {
  text-align: left;
  padding: 8px;
  /* background-color: #e8e8e8; */
  /* border: 1px solid #ccc; */
  border-bottom: 1px solid #ccc;
  color: #0066cc;
  font-weight: unset;
}

.file-table td {
  padding: 2px;
}

.file-row {
  cursor: pointer;
}

.file-row-lan {
  background-color: #f4f5f5;
}

.file-row:hover {
  background-color: #e8f4ff;
}

/* 图标视图 */
.file-icon-view {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 10px;
  flex: 1;
  overflow-y: auto;
}

.file-icon-item {
  width: 100px;
  height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 5px;
  border-radius: 2px;
}

.file-icon-item:hover {
  background-color: #e8f4ff;
}

.large-icon {
  font-size: 36px;
  margin-bottom: 8px;
}

.file-name {
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

/* 左下角状态信息栏 */
.status-bar {
  height: 30px;
  background-color: #e8e8e8;
  border-top: 1px solid #ccc;
  padding: 0 10px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  font-size: 12px;
  color: #333;
  margin-top: auto;
  margin-left: -10px;
  margin-right: -10px;
  margin-bottom: -10px;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 6px 32px;
  font-size: 12px;
  color: #494949;
  font-weight: bold;
  border-top: 1px dashed #e0e0e0;
}

.disk-space-info {
  display: flex;
  align-items: center;
  gap: 6px;
}

.file-count {
  color: #666;
}

/* 通用样式 */
.icon {
  font-style: normal;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
}

.user-icon,
.disk-space-icon {
  font-size: 14px;
}

.el-divider--horizontal {
  margin: unset;
}

.aggregate {
  width: 100%;
  position: absolute;
  bottom: 28px;
}

.storage-container {
  width: 100%;
  border-top: 1px dashed #e0e0e0;
  padding: 4px 32px;
  background-color: #fff;
}

.add-icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  line-height: 20px;
  border-radius: 50%;
  background-color: #eee;
  color: #666;
  margin-right: 6px;
  font-weight: bold;
}

.drop-text {
  color: #666;
  font-size: 14px;
}

/* 容量信息区 */
.storage-info {
  margin-top: 10px;
}

.upgrade-link {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 12px;
  font-weight: bold;
}

.upgrade-link span:first-child {
  color: #333;
}

.price {
  color: #ff6700;
  /* 橙色价格 */
  font-weight: 500;
}

/* 进度条 */
.progress-container {
  height: 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-bar {
  height: 100%;
  background-color: #2196f3;
  /* 蓝色进度 */
  border-radius: 4px;
  transition: width 0.3s ease;
}

/* 容量统计 */
.capacity-stats {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #666;
}

.used-capacity {
  color: #333;
}

.usage-percent {
  font-weight: 500;
}
</style>
