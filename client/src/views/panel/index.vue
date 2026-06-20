<template>
  <div class="panel">
    <div class="card">
      <h1 class="title">ME Frp 控制面板</h1>

      <div class="row">
        <label>预设正则：</label>
        <el-select v-model="selected" placeholder="请选择" size="mini" style="width: 100%" @change="onPresetChange">
          <el-option v-for="(p, i) in presets" :key="i" :label="p.name" :value="i" />
        </el-select>
      </div>

      <div class="row">
        <label>编辑 / 应用：</label>
        <div style="display: flex; gap: 10px">
          <el-input v-model="current" size="mini" placeholder="输入或编辑正则，例如: \d+" style="width: 100%" />
          <button class="apply" @click="applyCron">应用</button>
        </div>
      </div>

      <div class="controls">
        <el-button type="primary" plain size="small" @click="runTask">
          <span v-if="!runningNow">立刻执行任务</span>
          <span v-else>执行中...</span>
        </el-button>
        <el-button size="small" @click="toggleSchedule">
          <span v-if="!scheduled">监听实时日志 (每 {{ interval }}s)</span>
          <span v-else>停止定时</span>
        </el-button>

        <div class="status">
          <div>已应用正则: <b>0 0 2 * * ?(每天凌晨2点)</b></div>
          <div>
            状态:
            <b :class="{ ok: scheduled, off: !scheduled }">{{
              scheduled ? '定时已启动' : '定时已停止'
            }}</b>
          </div>
        </div>
      </div>

      <div class="tester">
        <div class="notice-title">Demo文本 (演示数据)</div>
        <textarea v-model="sample" rows="5" disabled style="margin-top: 4px; color: #bebebe" />
        <div class="matches">
          <div v-for="(m, i) in matches" :key="i" class="match">
            {{ i + 1 }}. {{ m }}
          </div>
          <div v-if="matches.length === 0" class="empty">
            当前时间：{{ new Date() }}
          </div>
        </div>
        <div class="matches empty copyLink" style="margin-top: 8px">
          <span class="copyLabel" @click="copyUrl">
            <span v-if="copyLoading == 0">复制访问路径</span>
            <span v-if="copyLoading == 1">复制ing{{ copyDots }}</span>
            <span v-if="copyLoading == 2">复制完成</span>
            <i v-if="copyLoading == 0" class="el-icon-copy-document" />
            <i v-if="copyLoading == 2" class="el-icon-circle-check" />
          </span>
          <span @click="autoDump">自动跳转</span>
        </div>
      </div>

      <div class="notice-title">内存日志（保留30条）</div>
      <div class="logs">
        <div v-for="(l, i) in logs" :key="i" class="log">
          <span class="time">{{ l.time }}</span>
          <span class="text">{{ l.text }}</span>
        </div>
        <div v-if="logs.length === 0" class="empty center">
          当前暂无运行日志记录
        </div>
      </div>
    </div>

    <!-- subtle animated background -->
    <div class="bg" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      presets: [
        { name: '每 10 分钟执行一次', pattern: '0 */10 * * * *' },
        { name: '每 30 分钟执行一次', pattern: '0 */30 * * * *' },
        { name: '每小时整点执行', pattern: '0 0 * * * *' },
        { name: '每天凌晨 1 点执行', pattern: '0 0 1 * * *' },
        { name: '每周一到周五 8 点执行', pattern: '0 0 8 * * 1-5' }
      ],
      selected: 0,
      current: '',
      applied: '',
      sample:
        '11:27:35 [定时任务-立刻执行] 状态：成功 | 日志：cron表达式：[0 * * * *]，耗时32ms' +
        '\n11:28:00 [定时任务-实时日志] 状态：成功 | 日志：启动成功，耗时87ms' +
        '\n11:28:00 [定时任务-实时日志] 状态：成功 | 日志：成功关闭，耗时132ms' +
        '\n11:28:00 [定时任务-修改cron] 状态：成功 | 日志：cron表达式：[*/30 * * * *]，耗时187ms',
      logs: [],
      matches: [],
      scheduled: false,
      interval: 15,
      intervalId: null,
      runningNow: false,
      copyLoading: 0,
      copyDots: '',
      dumpUrl: ''
    }
  },
  created() {
    this.requestData()
  },
  beforeDestroy() {
    if (this.intervalId) clearInterval(this.intervalId)
  },
  methods: {
    requestData() {
      // 初始化：加载第一个预设
      this.current = this.presets[this.selected].pattern
      const raw = localStorage.getItem('mefrp-logs')
      if (raw) {
        const arr = JSON.parse(raw)
        if (arr != null) {
          this.logs = arr
        }
        this.flushLogs()
      }
    },
    onPresetChange() {
      this.current = this.presets[this.selected].pattern
    },
    runTask() {
      // 防抖：短时间内不重复触发
      if (this.runningNow) return
      this.runningNow = true
      var startTime = Date.now()
      this.$get(this.$urls.execute).then((res) => {
        var endTime = Date.now()
        this.pushLog(
          '[定时任务-立刻执行] 状态：' +
          (res.code === 0 ? '成功' : '失败') +
          ' | 日志：耗时' +
          (endTime - startTime) +
          'ms'
        )
        this.runningNow = false
      })
    },
    toggleSchedule() {
      if (!this.scheduled) {
        this.intervalId = setInterval(() => {
          this.runTask()
        }, this.interval * 1000)
        this.scheduled = true
        this.pushLog('已启动监听日志')
      } else {
        clearInterval(this.intervalId)
        this.intervalId = null
        this.scheduled = false
        this.pushLog('已停止监听日志')
      }
    },
    applyCron() {
      var startTime = Date.now()
      this.$get(this.$urls.updateCorn, { cron: this.current }).then((res) => {
        var endTime = Date.now()
        this.pushLog(
          '[定时任务-修改cron] 状态：' +
          (res.code === 0 ? '成功' : '失败') +
          ' | 日志：耗时' +
          (endTime - startTime) +
          'ms'
        )
      })
    },
    pushLog(text) {
      const t = new Date().toLocaleTimeString()
      this.logs.unshift({ time: t, text })
      if (this.logs.length > 30) this.logs.pop()
      this.flushLogs()
    },
    // 同步放置到本地存储(localStorage)，以便刷新页面后保留
    flushLogs() {
      if (this.logs.length > 30) this.logs.splice(30)
      localStorage.setItem('mefrp-logs', JSON.stringify(this.logs))
    },
    async copyUrl() {
      this.copyLoading = 1
      this.startDotTimer()
      await this.$get(this.$urls.getUrl).then((res) => {
        navigator.clipboard.writeText(res.data + '/Eyb7sxFXft')
        this.dumpUrl = res.data + '/Eyb7sxFXft'
        this.copyLoading = 2
        setTimeout(() => {
          this.copyLoading = 0
        }, 2000)
      })
    },
    // 启动点的循环定时器
    startDotTimer() {
      // 先清除旧定时器（避免重复）
      this.clearDotTimer()
      // 定义点的循环数组
      const dotList = ['', '.', '..', '...']
      let index = 0
      // 200ms 切换一次点的显示
      this.dotTimer = setInterval(() => {
        index = (index + 1) % dotList.length // 循环索引
        this.copyDots = dotList[index]
      }, 200)
    },
    // 清除定时器
    clearDotTimer() {
      if (this.dotTimer) {
        clearInterval(this.dotTimer)
        this.dotTimer = null
        this.copyDots = '' // 重置点的显示
      }
    },
    autoDump() {
      window.location.href(this.dumpUrl, '_blank')
    }
  }
}
</script>

<style scoped>
.panel {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f7f8fa;
  position: relative;
  padding: 40px;
  color: #0f2130;
}

.notice-title {
  position: relative;
  margin-top: 40px;
  margin-left: 8px;
}

.notice-title::after {
  position: absolute;
  content: ' ';
  left: -8px;
  top: 50%;
  margin-top: -8px;
  width: 3px;
  height: 14px;
  background: linear-gradient(135deg, #2791ff, #03a7ff 98%);
  border-radius: 8px;
}

.card {
  width: 820px;
  max-width: calc(100% - 40px);
  background: #ffffff;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 8px 30px rgba(16, 32, 56, 0.06),
    0 1px 0 rgba(255, 255, 255, 0.6) inset;
  border: 1px solid rgba(16, 32, 56, 0.06);
  position: relative;
  overflow: hidden;
}

.title {
  margin: 0 0 14px 0;
  font-size: 20px;
  letter-spacing: 0.2px;
  color: #0b324d;
}

/* rows and form controls */
.row {
  display: grid;
  grid-template-columns: 100px 1fr;
  align-items: center;
  margin-bottom: 12px;
}

.row label {
  /* 保持固定宽度且右对齐，避免文本宽度影响布局 */
  justify-self: end;
  color: #3b556b;
  font-size: 13px;
  padding-right: 8px;
}

select,
input,
textarea {
  background: #f3f6f9;
  border: 1px solid rgba(16, 32, 56, 0.06);
  padding: 8px 10px;
  color: #0f2a3a;
  border-radius: 8px;
  outline: none;
  flex: 1;
  font-size: 14px;
}

input::placeholder,
textarea::placeholder {
  color: rgba(15, 42, 58, 0.35);
}

/* Apply button - accent */
.apply {
  width: 120px;
  background: linear-gradient(90deg, #2eb8b0, #6b8cf0);
  border: none;
  color: white;
  padding: 4px 28px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  box-shadow: 0 6px 18px rgba(46, 184, 176, 0.12);
}

/* Controls layout */
.controls {
  display: flex;
  gap: 12px;
  align-items: center;
  margin: 18px 0;
}

.neon {
  background: linear-gradient(90deg, #2b98ff, #3aa1a8);
  color: white;
  border: none;
  padding: 12px 18px;
  border-radius: 999px;
  cursor: pointer;
  font-weight: 700;
  box-shadow: 0 8px 30px rgba(42, 122, 255, 0.08),
    0 0 12px rgba(42, 122, 255, 0.06);
  transition: transform 0.12s ease;
}

.center {
  text-align: center;
}

.neon:active {
  transform: translateY(2px);
}

.toggle {
  background: #ffffff;
  border: 1px solid rgba(16, 32, 56, 0.06);
  padding: 10px 14px;
  border-radius: 10px;
  color: #294154;
  cursor: pointer;
}

/* status */
.status {
  margin-left: auto;
  text-align: right;
  color: #486879;
  font-size: 13px;
}

.status .ok {
  color: #0f9d58;
}

.status .off {
  color: #c33a2b;
}

/* tester */
.tester {
  margin-top: 8px;
}

textarea {
  width: 100%;
  resize: vertical;
  border-radius: 8px;
}

.matches {
  margin-top: 8px;
  max-height: 80px;
  overflow: auto;
  padding: 8px;
  background: #f6f9fc;
  border-radius: 8px;
  border: 1px solid rgba(16, 32, 56, 0.03);
}

.match {
  color: #0b3b2e;
  padding: 2px 0;
  font-size: 13px;
}

.empty {
  color: #bebebe;
  font-size: 13px;
  padding: 6px 0;
}

/* logs */
.logs {
  margin-top: 4px;
  max-height: 160px;
  overflow: auto;
  padding: 10px;
  border-radius: 10px;
  background: linear-gradient(180deg,
      rgba(247, 248, 250, 1),
      rgba(247, 248, 250, 1));
  border: 1px solid rgba(16, 32, 56, 0.03);
}

.log {
  font-size: 13px;
  color: #153642;
  display: flex;
  gap: 8px;
  padding: 6px 0;
  border-bottom: 1px dashed rgba(16, 32, 56, 0.03);
}

.time {
  color: #4a6b7f;
  min-width: 90px;
}

/* subtle decorative background tuned for light theme */
.bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  background-image: radial-gradient(closest-side at 10% 20%,
      rgba(43, 152, 255, 0.04),
      transparent 20%),
    radial-gradient(closest-side at 80% 80%,
      rgba(107, 140, 240, 0.03),
      transparent 20%);
  mix-blend-mode: normal;
}

/* responsive */
@media (max-width: 640px) {
  .card {
    padding: 16px;
  }

  .status {
    display: none;
  }

  .controls {
    flex-direction: column;
    align-items: stretch;
  }
}

.el-icon-copy-document {
  margin-left: 4px;
}

.copyLink {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  cursor: pointer;
  color: #1677ff;
  padding: 14px 0;
}

.copyLabel {
  padding: 8px;
}
</style>
