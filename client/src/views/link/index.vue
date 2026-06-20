<template>
  <div class="body-wrap">
    <div class="card-container">
      <div class="link-container">
        <span class="link-title">友情链接</span>
        <a class="apply-link" href="#jump-application">申请链接</a>
      </div>
      <div class="friend-list">
        <div v-for="(item, index) in list" :key="index" class="item" @click="toUrl(item.url)">
          <div class="bg">
            <div class="el-image" style="width: 100%; height: 100%">
              <img :src="item.avatar" class="el-image__inner" style="object-fit: cover">
            </div>
          </div>
          <div class="head">
            <div class="el-image">
              <img :src="item.avatar" class="el-image__inner">
            </div>
          </div>
          <div class="nickname">{{ item.name }}</div>
          <div class="personal-signature">{{ item.description }}</div>
        </div>
      </div>
      <span class="link-title">在线工具</span>
      <div class="friend-list">
        <div v-for="(item, index) in tools" :key="index" class="other-item" @click="toUrl(item.url)">
          <img :src="item.avatar || item.icon" alt=""> <!-- 修复：tools数组里是icon不是avatar -->
          <div class="tools-item">
            <span>{{ item.name }}</span>
            <span class="description">{{ item.description }}</span>
          </div>
        </div>
      </div>
      <span id="jump-application" class="link-title">申请地址</span>
      <div class="apply-link-container">
        <div class="container-left">
          <span>添站要求</span>
          <p>1. 很高兴能和非常多的朋友们交流，如果你也想加入友链，可以在右方申请，大概会在3个工作日内通过</p>
          <p>2. 域名规范：需顶级域名（如.com/.cn），无端口号、非他人分发的二级域名，支持 SSL 证书（https）</p>
          <p>3. 运营状态：稳定运行 3 个月以上，有搜索引擎收录记录，定期更新内容</p>
          <p>4. 内容要求：原创占比≥80%，无违法、色情、政治敏感等不良内容，符合中国大陆法律法规</p>
          <p>5. 友链规则：申请前需先添加对方博客链接，不使用含 nofollow 标签且未将对方加入白名单的链接</p>
          <div class="copy-link">
            <span>本站链接</span>
            <p>网站名称：灵光一刻|SUPERSTAR<i class="el-icon-document-copy" @click="copyFunction('灵光一刻|SUPERSTAR')" /></p>
            <p>网站链接：https://wuliqun.cn <i class="el-icon-document-copy" @click="copyFunction('https://wuliqun.cn')" />
            </p>
            <p>网站描述：只为了记录每个时刻的想法<i class="el-icon-document-copy" @click="copyFunction('只为了记录每个时刻的想法')" /></p>
            <p>头像链接：https://wuliqun.cn/star.png<i class="el-icon-document-copy"
                                                  @click="copyFunction('https://wuliqun.cn/star.png')"
            /></p>
          </div>
        </div>
        <div class="container-right">
          <el-form ref="form" :model="form" label-width="90px">
            <el-form-item label="网站名称：">
              <el-input v-model="form.name" size="mini" placeholder="灵光一刻|SUPERSTAR" />
            </el-form-item>
            <el-form-item label="网站链接：">
              <el-input v-model="form.url" size="mini" placeholder="www.wuliqun.cn" />
            </el-form-item>
            <el-form-item label="网站描述：">
              <el-input v-model="form.description" size="mini" placeholder="只为了记录每个时刻的想法" />
            </el-form-item>
            <el-form-item label="头像链接：">
              <el-input v-model="form.avatar" size="mini" placeholder="https://wuliqun.cn/star.png" />
            </el-form-item>
            <el-form-item label="联系邮箱：">
              <el-input v-model="form.email" size="mini" placeholder="superstarliqun@gmail.com" />
            </el-form-item>
            <el-form-item>
              <div style="display: flex;flex-direction: column;align-items: flex-start;">
                <el-checkbox v-model="checked" size="mini"><i class="el-icon-back" />我同意左面提出的所有要求</el-checkbox>
                <el-button type="primary" plain size="mini" :loading="loading" @click="onSubmit">申请</el-button>
                <!-- 修复：添加loading状态 -->
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import urls from '@/utils/urls'
const CryptoJS = require('crypto-js')
export default {
  data() {
    return {
      list: [],
      tools: [
        { icon: 'https://mintlify.s3.us-west-1.amazonaws.com/mcp/logo/light.svg', name: 'MCP官方文档', url: 'https://modelcontextprotocol.io/docs/tools/inspector' },
        { icon: 'https://www.gstatic.com/lamda/images/gemini_sparkle_aurora_33f86dc0c0257da337c63.svg', name: 'Gemini', url: 'https://gemini.google.com/app' }
      ],
      checked: false,
      loading: false,
      form: {
        name: '',
        description: '',
        avatar: '',
        url: '',
        email: ''
      }
    }
  },
  mounted() {
    this.getRandomColor()
    this.requestData()
  },
  methods: {
    requestData() {
      this.$get(this.$urls.getLinkList).then((res) => {
        if (res.code === 0) {
          this.list = res.data
        }
      }).catch(() => { })

      this.$get(this.$urls.getLinkList, { type: 1 }).then((res) => {
        if (res.code === 0) {
          this.tools = res.data
        }
      }).catch(() => { })
    },
    // 提交申请
    onSubmit() {
      if (!this.validate()) {
        return
      }
      this.loading = true
      this.$refs.form.validate((valid) => {
        if (!valid) {
          this.loading = false
          return
        }
        const service = axios.create({
          baseURL: urls.BASE_URL,
          timeout: 60000
        })
        // 加密
        const secret = 'fixed_salt'
        const ts = Date.now().toString()
        // 拼接规则：时间戳 + "." + MD5(时间戳 + 固定密钥)
        const sign = CryptoJS.MD5(ts + secret).toString()
        const finalStr = `${ts}.${sign}`
        const ciphertext = CryptoJS.AES.encrypt(finalStr, 'witpot-norsoz-hAvqa2').toString()
        service({
          url: this.$urls.addLink,
          method: 'post',
          data: this.form,
          headers: {
            'Accept-Apply-Link': ciphertext
          }
        }).then((res) => {
          console.log('res', res.data.code)
          if (res.data.code === 0) {
            this.$message.success('申请已提交，等待审核')
          } else {
            this.$message.error(res.message || '申请失败，请稍后重试')
          }
        }).catch(() => {
          this.$message.error('申请失败，请稍后重试')
        }).finally(() => {
          this.loading = false
          this.form = {
            name: '',
            description: '',
            avatar: '',
            url: '',
            email: ''
          } // 修复：清空表单时重置为初始空对象，避免响应式问题
          this.checked = false
        })
      })
    },
    // 验证格式
    validate() {
      const { name, url, description, avatar, email } = this.form

      if (!name) {
        this.$message.warning('起个响亮的网站名吧')
        return false
      }
      // URL 简单校验：必须包含 http 且有点
      if (!url || !url.startsWith('http') || !url.includes('.')) {
        this.$message.warning('网址格式不正确（需包含 http/https）')
        return false
      }
      if (!description) {
        this.$message.warning('介绍一下你的小窝吧')
        return false
      }
      if (!avatar) {
        this.$message.warning('没有头像可没法并肩作战')
        return false
      }
      const emailReg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
      if (!emailReg.test(email)) {
        this.$message.warning('邮箱格式有误，收不到审核通知哦')
        return false
      }
      if (!this.checked) {
        this.$message.warning('请先勾选同意协议哦')
        return false // 拦截
      }

      return true // 全校验通过
    },
    getRandomColor() {
      // 设置最低亮度阈值。例如：160 (十六进制的 A0)
      // R, G, B 的值都会在 160 (0xA0) 到 255 (0xFF) 之间。
      const min = 160
      const range = 256 - min // 随机生成范围 (256 - 160 = 96)

      // 1. 分别生成 R、G、B 三个分量的十进制值 (160 ~ 255)
      const rDec = Math.floor(Math.random() * range) + min
      const gDec = Math.floor(Math.random() * range) + min
      const bDec = Math.floor(Math.random() * range) + min

      // 2. 将十进制值转换为十六进制字符串
      let rHex = rDec.toString(16)
      let gHex = gDec.toString(16)
      let bHex = bDec.toString(16)

      // 3. 补零 (确保每个分量都是两位数，例如 'a' 变成 '0a')
      if (rHex.length === 1) rHex = '0' + rHex
      if (gHex.length === 1) gHex = '0' + gHex
      if (bHex.length === 1) bHex = '0' + bHex

      // 4. 拼接并返回浅色十六进制颜色码
      return '#' + rHex + gHex + bHex
    },
    toUrl(url) {
      window.open(url, '_blank')
    },
    copyFunction(text) {
      navigator.clipboard.writeText(text).then(() => {
        this.$message.success('已复制到剪贴板')
      }).catch(() => {
        this.$message.error('复制失败，请手动复制')
      })
    }
  }
}
</script>

<style scoped>
.body-wrap {
  background-color: var(--background);
  height: 100vh;
  overflow: hidden;
}

.card-container {
  max-width: 1240px;
  margin: 0 auto;
  padding: 20px;
  scroll-behavior: smooth;
  overscroll-behavior: contain;
  box-sizing: border-box;
}

.friend-list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
  margin: 0 auto;
  border-radius: 8px;
  padding-bottom: 60px;
}

.item {
  min-width: 180px;
  text-align: center;
  box-shadow: var(--box-shadow);
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid var(--card-background);
  cursor: pointer;
  background-color: var(--card-background);
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  }

  .bg {
    height: 70px;
    background-position: 50%;
    background-size: cover;
    background-repeat: no-repeat;
    filter: blur(5px);
  }

  .head {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: var(--tag-background);
    margin: auto;
    margin-top: -50px;
    position: relative;
    z-index: 2;
    overflow: hidden;
    border: 2px solid var(--card-background);

    .el-image {
      width: 100%;
      height: 100%;

      img {
        transition: transform 0.8s ease-in-out;
        transform-style: preserve-3d;
      }
    }

    .el-image img:hover {
      transform: rotateZ(360deg);
    }
  }

  .nickname {
    font-size: 14px;
    font-weight: bold;
    line-height: 36px;
    color: var(--text-color);
  }

  .personal-signature {
    font-size: 12px;
    margin-bottom: 10px;
    color: var(--text-color3);
  }
}

.link-title {
  position: relative;
  font-size: 18px;
  font-weight: bold;
  margin: 20px 10px;
  display: block;
  color: var(--text-color);
}

.link-title::after {
  position: absolute;
  content: " ";
  left: -8px;
  top: 50%;
  margin-top: -8px;
  width: 3px;
  height: 14px;
  background: linear-gradient(135deg, #2791ff, #03a7ff 98%);
  border-radius: 8px;
}

.other-item {
  min-height: 114.5px;
  min-width: 180px;
  min-width: 0;
  text-align: center;
  box-shadow: var(--box-shadow);
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  padding: 14px;
  gap: 16px;
  cursor: pointer;
  background-color: var(--card-background);
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  }

  img {
    flex-shrink: 0;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    border: 2px solid var(--card-background);
    transition: transform 0.8s ease-in-out;
    transform-style: preserve-3d;

    &:hover {
      transform: rotateZ(360deg);
    }
  }

  .tools-item {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    flex: 1;
    min-width: 0;

    .description {
      width: 100%;
      font-size: 12px;
      color: var(--text-color3);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      text-align: left;
    }
  }
}

.link-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.apply-link {
  cursor: pointer;
  color: var(--text-hover);
}

.apply-link-container {
  position: relative;
  max-width: 1440px;
  margin: 0 auto;
  margin-top: 20px;
  border-radius: 12px;
  background-color: var(--card-background);
  box-shadow: var(--box-shadow);
  border: var(--style-border);
  display: flex;
  width: 100%;
  min-height: 80px;
  padding: 18px;
  gap: 20px;
  box-sizing: border-box;

  &::before {
    content: "";
    border-left: 1px dashed var(--text-color3);
    height: calc(100% - 40px);
    top: 10px;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    padding: 10px 0;
    pointer-events: none;
  }

  span {
    margin-bottom: 4px;
    display: block;
    color: var(--text-color);
  }

  .container-left {
    flex: 1;
    padding-top: 8px;

    &>p {
      font-size: 12px;
      line-height: 2;
      color: var(--text-color2);
    }

    .copy-link {
      margin-top: 20px;

      p {
        color: var(--text-color2);
        font-size: 14px;
        display: flex;
        align-items: center;
        margin: 16px 0;
      }

      i {
        color: var(--text-hover);
        cursor: pointer;
        margin-left: 8px;
      }
    }
  }

  .container-right {
    flex: 1;

    .el-form-item {
      margin-bottom: 10px;
    }
  }
}
</style>
