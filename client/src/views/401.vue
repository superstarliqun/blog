<template>
  <div class="unauth-root">
    <div class="card">
      <div class="icon">🔒</div>
      <h1>暂无权限</h1>
      <p>您尚未登录或无权访问此页面</p>
      <el-link :underline="false" type="primary" @click="gotoLogin">将于 {{ countdown }} 秒后自动跳转到登录页面,或点击直接跳转</el-link>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      countdown: 10,
      timer: null,
      redirectTimer: null
    }
  },

  beforeMount() {
    if (this.timer) clearInterval(this.timer)
    if (this.redirectTimer) clearTimeout(this.redirectTimer)
  },
  mounted() {
    // 倒计时每秒更新
    this.timer = setInterval(() => {
      if (this.countdown > 0) this.countdown--
    }, 1000)
    // 等待2秒后跳转
    this.redirectTimer = setTimeout(() => {
      this.gotoLogin()
    }, 2000)
  },
  methods: {
    gotoLogin() {
      // 直接替换，不保留当前历史记录
      // this.$router.push('/login')
    }
  }
}

</script>

<style scoped>
.unauth-root {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  padding: 20px;
  box-sizing: border-box;
}

.card {
  width: 100%;
  max-width: 420px;
  text-align: center;
  background: #fff;
  border-radius: 8px;
  padding: 36px 28px;
  box-shadow: 0 8px 24px rgba(20, 36, 51, 0.08);
}

.icon {
  font-size: 48px;
  margin-bottom: 12px;
}

h1 {
  margin: 0 0 8px;
  font-size: 20px;
  color: #1f2d3d;
}

p {
  margin: 0 0 8px;
  color: #6b7280;
}

.hint {
  margin-top: 12px;
  color: #9aa4b2;
}

.actions {
  margin-top: 18px;
}

button {
  background: #3b82f6;
  color: #fff;
  border: none;
  padding: 10px 18px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

button:hover {
  background: #2563eb;
}
</style>
