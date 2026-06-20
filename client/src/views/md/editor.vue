<template>
  <div class="markdown-warp">
    <div class="markdown-warp-edit">
      <span style="width: 100px;line-height: 40px;font-size: 16px;">博客标题：</span>
      <el-input v-model="form.title" placeholder="请为你的文章设置一个心仪的标题吧" style="margin:0 10px" />
      <el-button type="primary" @click="release">发布博客</el-button>
    </div>
    <releaseDialog v-if="releaseShow" :row="form" @close="releaseShow = false" />
    <v-md-editor v-model="form.content" class="poster-content" :include-level="[1, 2, 3]" :disabled-menus="[]"
                 height="calc(100vh - 108px)" :toolbar="toolbar" :left-toolbar="leftToolbar" :toolbar-config="toolbarConfig"
                 @copy-code-success="handleCopyCodeSuccess" @upload-image="handleUploadImage"
    />
  </div>
</template>
<script>
import Vue from 'vue'
import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor'
import '@kangc/v-md-editor/lib/style/codemirror-editor.css'

import creatPrismTheme from '@kangc/v-md-editor/lib/theme/prism'
// 代码高亮主题
import Prism from 'prismjs'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-yaml'
import 'prismjs/components/prism-bash'
import 'prismjs/components/prism-sql'
import 'prismjs/components/prism-json'
import 'prismjs/components/prism-css'
import 'prismjs/components/prism-cshtml'
import 'prismjs/components/prism-python'
import './css/index.scss'

// 快捷复制代码
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index'
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css'
// 内容定位
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align'
// 任务列表
import createTodoListPlugin from '@kangc/v-md-editor/lib/plugins/todo-list/index'
import '@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css'
// codemirror 编辑器的相关资源
import Codemirror from 'codemirror'
// mode
import 'codemirror/mode/markdown/markdown'
import 'codemirror/mode/javascript/javascript'
import 'codemirror/mode/css/css'
import 'codemirror/mode/htmlmixed/htmlmixed'
import 'codemirror/mode/vue/vue'
// edit
import 'codemirror/addon/edit/closebrackets'
import 'codemirror/addon/edit/closetag'
import 'codemirror/addon/edit/matchbrackets'
// placeholder
import 'codemirror/addon/display/placeholder'
// active-line
import 'codemirror/addon/selection/active-line'
// scrollbar
import 'codemirror/addon/scroll/simplescrollbars'
import 'codemirror/addon/scroll/simplescrollbars.css'
// style
import 'codemirror/lib/codemirror.css'

VMdEditor.Codemirror = Codemirror

const prismTheme = creatPrismTheme({
  Prism,
  config: {
    toc: {
      includeLevel: [1, 2, 3]
    }
  }
})
prismTheme.extend((md) => {
  // md为 markdown-it 实例，可以在此处进行修改配置,并使用 plugin 进行语法扩展
  // md.set(option).use(createCopyCodePlugin())
})
VMdEditor.theme(prismTheme)
VMdEditor.use(createCopyCodePlugin())
VMdEditor.use(createAlignPlugin())
VMdEditor.use(createTodoListPlugin())

Vue.use(VMdEditor)
import releaseDialog from '@/views/article/components/release.vue'
export default {
  components: {
    releaseDialog
  },
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      leftToolbar: 'undo redo clear | h bold italic strikethrough quote | ul ol table hr | link image code | blockLeft blockCenter blockRight',
      toolbarConfig: {
        'image-link': {
          insertWithSize: true
        }
      },
      text: undefined,
      input: null,
      timer: '',
      releaseShow: false,
      elAlert: false,
      form: {
        mdContent: null,
        content: ''
      },
      toolbar: {}
    }
  },
  watch: {
    text(newValue, oldValue) {
      console.log(newValue, oldValue)
    },
    // 方案1：只监听 content 字段
    'form.content'(newVal, oldVal) {
      if (newVal) { // 空字符串不存
        localStorage.setItem('backContent', newVal)
      }
    }
  },
  created() {
    const id = this.$route.params.id
    if (id != null || id != null) {
      this.getDetails(id)
    }
    const content = localStorage.getItem('backContent')
    if (content) {
      this.elAlert = true
    }
    this.toolbar = {
      blockLeft: {
        title: '块左对齐',
        icon: 'iconfont icon-edit-left',
        action(editor) {
          editor.insert(function (selected) {
            const prefix = '::: align-left\n'
            const suffix = '\n:::'
            const placeholder = '文本'
            const content = selected || placeholder

            return {
              text: `${prefix}${content}${suffix}`,
              selected: content
            }
          })
        }
      },
      blockCenter: {
        title: '块居中',
        icon: 'iconfont icon-edit-center',
        action(editor) {
          editor.insert(function (selected) {
            const prefix = '::: align-center\n'
            const suffix = '\n:::'
            const placeholder = '文本'
            const content = selected || placeholder

            return {
              text: `${prefix}${content}${suffix}`,
              selected: content
            }
          })
        }
      },
      blockRight: {
        title: '块右对齐',
        icon: 'iconfont icon-edit-right',
        action(editor) {
          editor.insert(function (selected) {
            const prefix = '::: align-right\n'
            const suffix = '\n:::'
            const placeholder = '文本'
            const content = selected || placeholder

            return {
              text: `${prefix}${content}${suffix}`,
              selected: content
            }
          })
        }
      }
    }
  },
  beforeMount() {
    window.addEventListener('beforeunload', this.handleBeforeUnload)
  },
  beforeDestroy() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload)
  },
  methods: {
    // 上传图片方法
    async handleUploadImage(event, insertImage, files) {
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      console.log(files)
      // 此处只做示例
      for (let i = 0; i < files.length; i++) {
        const formDate = new FormData()
        formDate.append('file', files[i])
        const res = await this.$uploadFile(this.$urls.uploadFile, formDate)
        insertImage({
          url: res.data,
          desc: files[i].name
        })
      }
    },
    // 代码复制方法
    handleCopyCodeSuccess(code) { },
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
    handleStore(type) {
      if (type === 1) {
        const content = localStorage.getItem('backContent')
        this.form.content = content
      } else if (type === 2) {
        localStorage.removeItem('backContent')
        this.$message.success('删除成功！')
      }
      this.elAlert = false
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
<style lang="scss" scoped>
.markdown-warp {
  min-height: 100vh;
  background-color: var(--background);
}

::v-deep .markdown-body {
  margin: 0 24px;
}

.markdown-warp-edit {
  display: flex;
  padding: 10px;
  background-color: var(--card-background);
  border-bottom: var(--style-border);
  color: var(--text-color);

  span {
    color: var(--text-color);
  }
}

.cherry-previewer {
  background-color: #ffffff;
  border-left: none;
}

.bytemd {
  height: calc(100vh - 108px);
}

.alert-tags {
  p {
    cursor: pointer;
  }
}

// Override codemirror editor theme for dark mode
::v-deep .CodeMirror {
  background: var(--card-background) !important;
  color: var(--text-color) !important;
}

::v-deep .CodeMirror-gutters {
  background: var(--background) !important;
  border-right: var(--style-border) !important;
}

::v-deep .CodeMirror-cursor {
  border-left: 2px solid var(--text-hover) !important;
}

::v-deep .CodeMirror-activeline-background {
  background: var(--tag-background) !important;
}

::v-deep .el-input__inner {
  background: var(--card-background) !important;
  color: var(--text-color) !important;
  border: var(--style-border) !important;
}

::v-deep .v-md-editor {
  background: var(--background);
}

::v-deep .v-md-editor__toolbar {
  background: var(--card-background) !important;
  border-bottom: var(--style-border) !important;
}

::v-deep .v-md-editor__toolbar-item {
  color: var(--text-color) !important;
}
::v-deep .v-md-editor__toolbar-item:hover {
  color: var(--text-hover) !important;
}
</style>
