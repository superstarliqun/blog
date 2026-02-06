<template>
  <div class="comment-container">
    <div class="comment-input-container">
      <div id="commentEditor" ref="editor" contenteditable="true" placeholder="请输入评论..." @input="handleInput" />
      <div class="emoji-picker">
        <img v-for="(src, name) in emojiMap" :key="name" class="emoji-item" :src="src" @click="insertEmoji(name)">
      </div>
      <span class="introduction">灵光一刻|SUPERSTAR 博客使用</span>
    </div>
    <div class="comment-user">
      <span><b>昵称</b><input v-model="nickname" type="text" @input="handleUser"></span>
      <span><b>邮箱</b><input v-model="email" type="text" @input="handleUser"></span>
      <span><b>网址</b><input v-model="site" type="text" @input="handleUser"></span>
    </div>
  </div>
</template>

<script>
const req = require.context('@/assets/img/emoji', false, /\.png$/)
const emojiMap = {}
req.keys().forEach((k) => {
  const name = k.replace('./', '').replace('.png', '')
  emojiMap[name] = req(k)
})
export default {
  props: {
    value: { type: String, default: '' }
  },
  data() {
    return {
      emojiMap,
      nickname: null,
      email: null,
      site: null
    }
  },
  mounted() {
    const user = localStorage.getItem('SUPERSTAR_USER')
    if (user != null && user !== undefined) {
      const item = JSON.parse(user)
      this.nickname = item.nickname
      this.email = item.email
      this.site = item.site
      this.$emit('user', item)
    }
  },
  methods: {
    // 处理输入问题
    handleInput(e) {
      const html = e.target.innerHTML
      // 匹配 <img ... data-char="表情名" ...> 提取表情名
      const textValue = html.replace(
        /<img[^>]+data-char=['"]([^'"]+)['"][^>]*>/g,
        '[$1]'
      )
      // 如果需要纯文本（去除 HTML 标签如 <div> 等），可以进一步处理
      const result = textValue.replace(/<(?:.|\n)*?>/gm, '')
      this.$emit('input', result)
    },
    // 插入表情包
    insertEmoji(name) {
      const editor = document.getElementById('commentEditor')
      editor.focus()

      const src = this.emojiMap[name]
      const imgHtml = `<img src="${src}" data-char="${name}" class="emoji-item">`

      // 1. 保存当前选区（标准 + IE 双兼容）
      const sel = window.getSelection()
      if (sel.rangeCount === 0) {
        // 如果编辑器里本来就没有选区，先随便放一个
        const range = document.createRange()
        range.selectNodeContents(editor)
        range.collapse(false)
        sel.removeAllRanges()
        sel.addRange(range)
      }
      const range = sel.getRangeAt(0)

      // 2. 用 execCommand 把图片插进去
      document.execCommand('insertHTML', false, imgHtml)

      // 3. 立刻找到刚才插入的那张图
      const img = editor.querySelector(
        `img[src="${src}"][data-char="${name}"]:not([data-placed])`
      )
      if (!img) return // 极端情况容错

      // 4. 把光标放到图片后面
      range.setStartAfter(img)
      range.collapse(true)
      sel.removeAllRanges()
      sel.addRange(range)

      // 5. 打标记，防止下次再误抓到同一张图
      img.setAttribute('data-placed', '1')
    },
    handleUser() {
      const user = {
        nickname: this.nickname,
        email: this.email,
        site: this.site
      }
      this.$emit('user', user)
      localStorage.setItem('SUPERSTAR_USER', JSON.stringify(user))
    }
  }
}
</script>
