<template>
  <div class="login">
    <div class="login-box">
      <div class="login-title">
        <p>{{ lang.title }}</p>
        <p>{{ lang.desc }}</p>
      </div>
      <div class="login-form flex">
        <div class="img-box flex align-center">
          <img src="@/assets/img/login_logo.png" alt="">
        </div>
        <div class="form flex align-center justify-center">
          <el-form v-if="show === 1" ref="ruleForm" :model="loginFrom">
            <el-input v-model="loginFrom.username" class="mt20" prefix-icon="el-icon-user" type="text"
              :placeholder="lang.user" />
            <el-input v-model="loginFrom.password" class="mt20" prefix-icon="el-icon-lock" type="password"
              :placeholder="lang.pwd" />
            <el-button class="mt20 w_100" type="primary" :loading="loading" @click="submitForm()">{{ lang.btn
              }}</el-button>
            <el-divider><span class="other-line">其他方式登录</span></el-divider>
            <div class="other">
              <div class="other-item" @click="toWx()">微信</div>
              <div class="other-item" />
              <div class="other-item" />
              <div class="other-item" />
            </div>
          </el-form>
          <div v-if="show === 2">
            <div class="wxhead">微信扫码</div>
            <el-image :src="url" />
            <div class="wxbottom"><span>输入验证码 <span class="captcha">{{ number }}</span><i v-if="number != null"
                  class="el-icon-refresh refresh" @click="refreshCode" /></span></div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { setRefreshToken, setAccessToken, setUserInfo } from '../../utils/auth'
import { Encrypt } from '../../utils/secret'
export default {
  data() {
    return {
      lang: {
        title: '博客系统',
        desc: '登录emoping畅享更多权益',
        btn: '菜鸟登录'
      },
      show: 1,
      url: 'https://wuliqun.cn/resources/image/41c7321820ec4921dac9d0d40a8aa2d1dcd8bc8ff89ec1a2bf1d72afc63d6c4dqrcode_for_gh_240cd131cc84_258.jpg',
      loginFrom: {
        username: null,
        password: null
      },
      loading: false,
      number: null,
      domain: process.env.VUE_APP_BASE_API,
      uuidSerialize: '',
      eventSource: null
    }
  },
  mounted() {
    document.addEventListener('keydown', this.handleEnterKey)
  },
  beforeDestroy() {
    document.removeEventListener('keydown', this.handleEnterKey)
  },
  methods: {
    handleEnterKey(event) {
      if (event.key === 'Enter') {
        this.submitForm()
      }
    },
    submitForm() {
      if (!this.loginFrom.username) {
        this.$message.warning('请输入用户名')
        return false
      }
      if (!this.loginFrom.password) {
        this.$message.warning('请输入密码')
        return false
      }
      this.loading = true
      this.loginFrom.password = Encrypt(this.loginFrom.password)
      this.loginFrom.remember_me = 'on'
      this.$postForm(this.$urls.login, this.loginFrom).then(res => {
        if (res.code === 0) {
          setRefreshToken(res.refreshToken)
          setAccessToken(res.accessToken)
          this.queryUserInfo()
        } else {
          this.$notify({
            title: '登录错误提示',
            message: res.message,
            type: 'error'
          })
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 弹出微信二维码
    toWx() {
      this.uuidSerialize = this.generateUUID()
      this.sendSSE()
      this.show = 2
    },
    // 保存用户信息
    queryUserInfo() {
      this.$get(this.$urls.getUserInfo).then(res => {
        this.$notify({
          title: '成功',
          message: '欢迎用户' + res.data.nickname + '回家',
          type: 'success'
        })
        setUserInfo(res.data)
        this.$router.push('/')
      })
    },
    // 发送半长链接
    sendSSE() {
      this.eventSource = new EventSource(this.domain + this.$urls.sseWxLink + '?deviceId=' + this.uuidSerialize)
      // 监听事件
      this.eventSource.addEventListener('message', (event) => {
        const text = event.data
        if (text.startsWith('init#')) {
          // 初始化链接
          setTimeout(() => {
            this.queryCode()
          }, 500)
        } else if (text.startsWith('query#')) {
          // 发送验证码链接 query#666
        } else if (text.startsWith('login#')) {
          // 登录成功链接 login#token
          // const res = text.split('#')
          // setToken(res[1])
          this.queryUserInfo()
          // 关闭SSE连接
          this.eventSource.close()
        }
      })
      // 处理错误
      this.eventSource.addEventListener('error', (error) => {
        console.error('EventSource failed:', error)
      })
      setTimeout(() => {
        this.queryCode()
      }, 500)
    },
    // 获取验证码
    queryCode() {
      const param = {}
      param.deviceId = this.uuidSerialize
      this.$get(this.$urls.sseWxcode, param).then(res => {
        this.number = res.data
      })
    },
    // 随机序列化
    generateUUID() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (Math.random() * 16) | 0
        var v = c === 'x' ? r : (r & 0x3) | 0x8
        return v.toString(16)
      })
    },
    // 刷新验证码
    refreshCode() {
      this.$get(this.$urls.sseWxRefresh, { deviceId: this.uuidSerialize }).then(res => {
        this.number = res.data
      })
    }
  }

}
</script>

<style lang="scss" scoped>
.refresh {
  cursor: pointer;
}

.login {
  position: absolute;
  width: 100%;
  height: 100%;
  background: bg-img("login_bg.jpg") no-repeat;
  background-size: 100% 100%;

  .img-box {
    width: 60%;
    padding: 0 20px;

    img {
      width: 100%;
      display: block;
    }
  }

  .form {
    width: 40%;
    padding: 0 40px;
  }

  .login-box {
    position: absolute;
    top: 20%;
    left: 50%;
    transform: translateX(-50%);

    .login-title {
      text-align: center;
      color: #fff;
      padding-bottom: 30px;

      &>p:nth-child(1) {
        font-size: 30px;
      }

      &>p:nth-child(2) {
        font-size: 14px;
        margin-top: 10px;
      }
    }

    .login-form {
      width: 800px;
      height: 340px;
      background-color: #fff;
      box-shadow: 0 0 4px 0 #ccc;
    }
  }
}

.other-line {
  color: #8c92a4;
  white-space: nowrap;
  display: block;
}

.other {
  display: flex;
  justify-content: space-between;

  .other-item {
    color: #8c92a4;
    cursor: pointer;
    // background-color: red;
  }
}

.wxhead {
  font-size: 22px;
  font-weight: 500;
  text-align: center;
  color: green;
}

.wxbottom {
  text-align: center;
  font-size: 14px;

  .captcha {
    color: red;
    font-size: 18px;
    font-weight: bold;
  }
}
</style>
