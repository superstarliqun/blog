<template>
  <div class="markdown-warp">
    <div class="markdown-warp-edit">
      <span style="width: 100px;line-height: 40px;">博客标题：</span>
      <el-input v-model="form.title" placeholder="「 无标题 」" style="margin:0 10px"></el-input>
      <el-button type="primary" @click="release">发布博客</el-button>
    </div>
    <releaseDialog v-if="releaseShow" :row="form" @close="releaseShow = false"></releaseDialog>
    <betymd :key="timer" v-model="form.content" display="edit"></betymd>
  </div>
</template>

<script>
import Betymd from './betymd'
import releaseDialog from './release'
export default {
  components: {
    // CherryMarkdown,
    Betymd,
    releaseDialog
  },
  data() {
    return {
      input: null,
      timer: '',
      releaseShow: false,
      form: {
        mdContent: null
      }
    }
  },
  created() {
    const id = this.$route.params.id
    if (id != null || id != null) {
      this.getDetails(id)
    }
    this.reply()
  },
  beforeMount() {
    window.addEventListener('beforeunload', this.handleBeforeUnload)
  },
  beforeDestroy() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload)
  },
  methods: {
    // 获取文章详情
    getDetails(id) {
      this.$get(this.$urls.articleGet, null, [id]).then(result => {
        if (result.code === 0) {
          this.timer = new Date().getTime()
          this.form = result.data
        }
      })
    },
    // 发布博客
    release() {
      if (this.checkContent()) {
        if (this.form.title === '' || this.form.title === undefined || this.form.title === null) {
          this.$message.warning('请填写文章标题')
          return false
        }
        // 只要markdown格式
        if (this.form.content === '' || this.form.content === undefined || this.form.content === null) {
          this.$message.warning('请填写文章内容')
          return false
        }
        this.row = this.form
        this.releaseShow = true
      }
    },
    // 检测用户是否登录
    checkContent() {
      const token = this.userOnline()
      if (token === null || token === undefined) {
        this.$confirm('系统检测登录用户已经失效，但是您的文章已经编写完成，是否保存到草稿箱?', '提示', {
          confirmButtonText: '保存',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          localStorage.setItem('draft', this.form.content)
          this.$message.warning('系统5秒后跳转登录页面！')
          setTimeout(() => {
            this.$router.push('/login')
          }, 5000)
        }).catch(() => {
          this.$router.push('/login')
        })
        return false
      }
      return true
    },
    // 恢复文章内容
    reply() {
      var data = localStorage.getItem('draft')
      if (data != null && data !== undefined) {
        this.$confirm('检测草稿箱存在数据是否恢复?', '提示', {
          confirmButtonText: '恢复',
          cancelButtonText: '清空',
          type: 'warning'
        }).then(() => {
          this.form.content = data
          localStorage.removeItem('draft')
          this.timer = new Date().getTime()
        }).catch(() => {
          localStorage.removeItem('draft')
          this.form.content = null
          this.timer = new Date().getTime()
        })
      }
    },
    handleBeforeUnload(event) {
      // 在事件处理程序中添加保存提示的逻辑
      event.preventDefault()
      event.returnValue = '系统可能不会保存您所做的更改。'
    },
    // 判断用户是否正确
    userOnline() {
      let result = false
      this.$get(this.$urls.getUserInfo).then(res => {
        if (res.code === 0) {
          result = true
        }
      })
      return result
    }
  }
}
</script>

<style>
.markdown-warp-edit {
  display: flex;
  padding: 10px;
}

.cherry-previewer {
  background-color: #ffffff;
  border-left: none;
}

.bytemd {
  height: calc(100vh - 108px);
}
</style>
