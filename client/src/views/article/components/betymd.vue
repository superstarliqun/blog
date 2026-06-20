<template>
  <div class="details">
    <Editor v-if="display ==='edit'" class="editos" :value="value" :plugins="plugins" :locale="zhHans" :upload-images="uploadImage" @change="handleChange" />
    <Viewer v-else class="viewer" :tabindex="2" :sanitize="23" :value="value" :plugins="plugins" :locale="zhHans" />
  </div>
</template>

<script>
import { Editor, Viewer } from '@bytemd/vue'

import gfm from '@bytemd/plugin-gfm'
import highlight from '@bytemd/plugin-highlight'
import breaks from '@bytemd/plugin-breaks'
import footnotes from '@bytemd/plugin-footnotes'
import frontmatter from '@bytemd/plugin-frontmatter'
import gemoji from '@bytemd/plugin-gemoji'
import mediumZoom from '@bytemd/plugin-medium-zoom'
import { getProcessor } from 'bytemd'
import zhHans from 'bytemd/locales/zh_Hans.json'
import gfmLocale from '@bytemd/plugin-gfm/locales/zh_Hans.json'
import 'bytemd/dist/index.css'
import 'highlight.js/styles/vs.css'
import 'juejin-markdown-themes/dist/juejin.min.css'
import theme from '@/assets/bytemd/plugin-theme'

const plugins = [
  gfm({ locale: gfmLocale }),
  breaks(),
  highlight(),
  frontmatter(),
  footnotes(),
  gemoji(),
  mediumZoom(),
  theme({
    theme: 'github', // 默认掘金主题
    highlight: 'github' // 默认代码高亮样式
  })
]
export default {
  components: { Editor, Viewer }, // 组件注册
  props: {
    value: {
      type: String,
      default: ''
    },
    // 编辑器的显示模式 view|edit
    display: {
      type: String,
      default: 'edit'
    }
  },
  data() {
    return {
      content: '',
      plugins, // 插件
      zhHans, // 简体中文
      tocList: []
    }
  },
  created() {
    this.getTocList()
  },

  methods: {
    // 获取书写文档内容
    handleChange(value) {
      this.$emit('input', value)
      this.getTocList()
    },
    // 上传图片 点击触发上传图片事件，大家获取文件把图片上传服务器然后返回url既可
    async uploadImage(files) {
      var resultList = []
      for (let i = 0; i < files.length; i++) {
        const formDate = new FormData()
        formDate.append('file', files[i])
        const res = await this.$uploadFile(this.$urls.uploadFile, formDate)
        resultList.push({
          url: res.data,
          title: files[i].name
        })
      }
      return resultList
    },
    // 获取toc前3级列表
    getTocList() {
      getProcessor({
        plugins: [
          {
            rehype: p =>
              p.use(() => tree => {
                var items = []
                if (tree && tree.children.length) {
                  tree.children.filter(v => v.type === 'element').forEach(node => {
                    if ((node.tagName === 'h1' || node.tagName === 'h2' || node.tagName === 'h3') && node.children.length > 0) {
                      items.push({
                        tagName: node.tagName,
                        text: this.stringifyHeading(node)
                      })
                    }
                  })
                }
                this.$emit('tocList', items)
              })
          }
        ]
      }).processSync(this.value)
    },
    // 获取文本值
    stringifyHeading(node) {
      let result = ''
      node.children.forEach(item => {
        if (item.type === 'text') {
          result += item.value
        }
      })
      return result
    }
  }
}
</script>
<style lang="scss">
.bytemd {
  height: calc(100vh);
}
.hljs-keyword,
.hljs-selector-class,
.hljs-symbol {
  color: #b3ae60;
}
.hljs-bullet,
.hljs-string {
  color: #5f826b;
  background: #f6f8fa;
}

.hljs-literal,
.hljs-name,
.hljs-number {
  color: #2aacb8;
  font-weight: bolder;
}
</style>
