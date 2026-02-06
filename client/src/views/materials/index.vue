<template>
  <div class="notes-app">
    <aside class="sidebar">
      <div class="sidebar-top">
        <h1 class="logo">笔记</h1>
        <div class="search">
          <input v-model="query" placeholder="搜索笔记 / 文件名">
          <button title="新建" @click="createNote">＋</button>
        </div>
      </div>

      <ul class="file-list">
        <li v-for="note in filteredNotes" :key="note.id" :class="{ active: note.id === currentId }"
          @click="selectNote(note.id)">
          <div class="accent" :style="{ background: note.color }"></div>
          <div class="meta">
            <div class="title">{{ note.title }}</div>
            <div class="time">{{ formatTime(note.updatedAt) }}</div>
          </div>
        </li>
      </ul>

      <div class="sidebar-footer">
        <small>共 {{ notes.length }} 条笔记</small>
      </div>
    </aside>

    <main class="editor">
      <div v-if="current" class="editor-header">
        <input v-model="current.title" class="title-input">
        <div class="actions">
          <button @click="togglePreview">{{ preview ? '编辑' : '预览' }}</button>
          <button class="danger" @click="deleteNote(current.id)">删除</button>
        </div>
      </div>

      <div v-else class="empty-state">
        <div class="empty-card">
          <h2>还没有笔记</h2>
          <p>点击左上角 “＋” 新建一个笔记。</p>
        </div>
      </div>

      <transition name="fade">
        <section v-if="current" class="editor-body">
          <div class="card">
            <div v-if="preview" class="preview" v-html="renderedHtml"></div>

            <textarea v-else v-model="current.content" class="editor-textarea"
              placeholder="在此输入 Markdown… 支持简单的标题、粗体、斜体、代码、列表与链接。"></textarea>
          </div>
        </section>
      </transition>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'

const STORAGE_KEY = 'soft-emoji-notes-v1'

function uid() {
  return Date.now().toString(36) + Math.random().toString(36).slice(2, 8)
}

const defaultNotes = [
  {
    id: uid(),
    title: '欢迎',
    content:
      '# 欢迎使用\n\n这是一个炫酷的笔记页面示例。\n\n- 左侧列出所有文件\n- 点击文件查看 / 编辑右侧内容\n\n```\n// 代码块示例\nconsole.log("hello")\n```\n\n**祝你写笔记愉快** 🎉',
    color: 'linear-gradient(135deg,#89f7fe 0%,#66a6ff 100%)',
    updatedAt: Date.now()
  },
  {
    id: uid(),
    title: '示例笔记',
    content:
      '## 示例\n\n这是一个示例笔记，支持简单 Markdown 渲染。\n\n- 支持列表\n- 支持标题\n- 支持 `行内代码` 和代码块\n\n更多功能可自行扩展。',
    color: 'linear-gradient(135deg,#f6d365 0%,#fda085 100%)',
    updatedAt: Date.now() - 1000 * 60 * 60
  }
]

const notes = ref([])
const currentId = ref(null)
const query = ref('')
const preview = ref(false)
let saveTimer = null

onMounted(() => {
  const raw = localStorage.getItem(STORAGE_KEY)
  if (raw) {
    try {
      notes.value = JSON.parse(raw)
    } catch {
      notes.value = defaultNotes
    }
  } else {
    notes.value = defaultNotes
  }
  if (notes.value.length) currentId.value = notes.value[0].id
})

watch(
  notes,
  (nv) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(nv))
  },
  { deep: true }
)

watch(
  () => currentId.value,
  () => {
    preview.value = false
  }
)

const current = computed(() => notes.value.find((n) => n.id === currentId.value) || null)

function selectNote(id) {
  currentId.value = id
}

function createNote() {
  const newNote = {
    id: uid(),
    title: '新笔记',
    content: '',
    color: randomGradient(),
    updatedAt: Date.now()
  }
  notes.value.unshift(newNote)
  currentId.value = newNote.id
  // persist quickly
  localStorage.setItem(STORAGE_KEY, JSON.stringify(notes.value))
}

function deleteNote(id) {
  const idx = notes.value.findIndex((n) => n.id === id)
  if (idx !== -1) {
    notes.value.splice(idx, 1)
    if (notes.value.length) currentId.value = notes.value[0].id
    else currentId.value = null
  }
}

watch(
  current,
  (note) => {
    if (!note) return
    // auto update timestamp with debounce
    if (saveTimer) clearTimeout(saveTimer)
    saveTimer = setTimeout(() => {
      note.updatedAt = Date.now()
      localStorage.setItem(STORAGE_KEY, JSON.stringify(notes.value))
    }, 600)
  },
  { deep: true }
)

const filteredNotes = computed(() => {
  const q = query.value.trim().toLowerCase()
  if (!q) return notes.value
  return notes.value.filter(
    (n) => n.title.toLowerCase().includes(q) || (n.content && n.content.toLowerCase().includes(q))
  )
})

function formatTime(ts) {
  if (!ts) return ''
  const d = new Date(ts)
  return d.toLocaleString()
}

function randomGradient() {
  const presets = [
    'linear-gradient(135deg,#ff9a9e 0%,#fecfef 100%)',
    'linear-gradient(135deg,#a18cd1 0%,#fbc2eb 100%)',
    'linear-gradient(135deg,#89f7fe 0%,#66a6ff 100%)',
    'linear-gradient(135deg,#f6d365 0%,#fda085 100%)',
    'linear-gradient(135deg,#cfd9df 0%,#e2ebf0 100%)'
  ]
  return presets[Math.floor(Math.random() * presets.length)]
}

function togglePreview() {
  preview.value = !preview.value
}

/* 简易 Markdown 转 HTML（非完全实现，仅常用语法） */
function escapeHtml(str) {
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
}

function markdownToHtml(md) {
  if (!md) return ''
  let text = md.replace(/\r\n/g, '\n')

  // 代码块 ```...```
  text = text.replace(/```([\s\S]*?)```/g, (_, code) => {
    return `<pre class="code-block"><code>${escapeHtml(code)}</code></pre>`
  })

  // 行内代码
  text = text.replace(/`([^`]+)`/g, (_, code) => `<code class="inline-code">${escapeHtml(code)}</code>`)

  // 标题 ### ## #
  text = text.replace(/^######\s?(.*)$/gm, '<h6>$1</h6>')
  text = text.replace(/^#####\s?(.*)$/gm, '<h5>$1</h5>')
  text = text.replace(/^####\s?(.*)$/gm, '<h4>$1</h4>')
  text = text.replace(/^###\s?(.*)$/gm, '<h3>$1</h3>')
  text = text.replace(/^##\s?(.*)$/gm, '<h2>$1</h2>')
  text = text.replace(/^#\s?(.*)$/gm, '<h1>$1</h1>')

  // 粗体 **text**
  text = text.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')

  // 斜体 *text*
  text = text.replace(/(^|[^*])\*([^*][\s\S]*?)\*([^*]|$)/g, (m, a, b, c) => {
    return (a || '') + '<em>' + b + '</em>' + (c || '')
  })

  // 链接 [text](url)
  text = text.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener">$1</a>')

  // 无序列表 - item
  // 将连续的 - 行转换成 <ul>
  text = text.replace(/(^|\n)(?:- .+(?:\n|$))+/g, (block) => {
    const items = block
      .trim()
      .split(/\n/)
      .map((l) => l.replace(/^- /, ''))
      .map((i) => `<li>${i}</li>`)
      .join('')
    return `<ul>${items}</ul>`
  })

  // 段落（剩余非块元素的行包裹 p）
  const lines = text.split('\n')
  const out = []
  for (let i = 0; i < lines.length; i++) {
    const line = lines[i]
    if (
      /^<\/?(h1|h2|h3|h4|h5|h6|ul|pre|li|p|blockquote|code)/.test(line) ||
      line.trim() === ''
    ) {
      out.push(line)
    } else {
      out.push(`<p>${line}</p>`)
    }
  }

  return out.join('\n')
}

const renderedHtml = computed(() => {
  return markdownToHtml(current.value ? current.value.content : '')
})
</script>

<style scoped>
:root {
  --bg: linear-gradient(180deg, #0f2027 0%, #203a43 50%, #2c5364 100%);
  --card: rgba(255, 255, 255, 0.04);
  --glass: rgba(255, 255, 255, 0.06);
  --accent: #66a6ff;
}

* {
  box-sizing: border-box;
}

.notes-app {
  display: flex;
  height: 100vh;
  background: var(--bg);
  color: #e6f7ff;
  overflow: hidden;
}

/* Sidebar */
.sidebar {
  width: 320px;
  padding: 20px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.02), rgba(255, 255, 255, 0.01));
  border-right: 1px solid rgba(255, 255, 255, 0.03);
  display: flex;
  flex-direction: column;
}

.logo {
  margin: 0;
  font-size: 20px;
  letter-spacing: 1px;
  font-weight: 700;
  color: black;
}

.search {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.search input {
  flex: 1;
  padding: 8px 10px;
  border: 1px solid #b7ccfc;
  color: black;
  border-radius: 8px;
  outline: none;
}

.search button {
  width: 36px;
  border-radius: 8px;
  cursor: pointer;
  border: 1px solid #b7ccfc;
  transition: transform .12s;
  background: none
}

.search button:active {
  transform: scale(.98);
}

.file-list {
  list-style: none;
  padding: 12px 0;
  margin: 12px 0 0;
  overflow: auto;
  flex: 1;
}

.file-list li {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 10px;
  margin-bottom: 8px;
  border-radius: 10px;
  background: transparent;
  cursor: pointer;
  transition: transform .12s, background .12s;
  border: 1px solid transparent;
}

.file-list li:hover {
  transform: translateX(6px);
  background: rgba(255, 255, 255, 0.02);
}

.file-list li.active {
  /* border-color: rgba(255, 255, 255, 0.06); */
  background: #9fe1ff
    /* box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4), inset 0 0 30px rgba(255, 255, 255, 0.02); */
}

.file-list .accent {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  flex-shrink: 0;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.4), inset 0 -10px 30px rgba(255, 255, 255, 0.02);
}

.meta .title {
  font-weight: 600;
  font-size: 14px;
  color: black;
}

.meta .time {
  font-size: 12px;
  color: black;
}

/* Editor area */
.editor {
  flex: 1;
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.title-input {
  flex: 1;
  padding: 10px 12px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.03);
  color: black;
  font-size: 18px;
  outline: none;
}

.actions button {
  margin-left: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  background: linear-gradient(90deg, #8ec5ff, #7ee8fa);
  color: #062a3b;
  font-weight: 700;
}

.actions button.danger {
  background: linear-gradient(90deg, #ff7e7e, #ffb199);
  color: #3b0000;
}

.editor-body {
  flex: 1;
  display: flex;
  gap: 20px;
}

.card {
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.02), rgba(255, 255, 255, 0.01));
  border-radius: 16px;
  padding: 20px;
  width: 100%;
  height: 100%;
  overflow: auto;
  border: 1px solid rgba(255, 255, 255, 0.03);
  /* box-shadow: 0 10px 30px rgba(2, 6, 23, 0.6), inset 0 1px 0 rgba(255, 255, 255, 0.02); */
  border: 1px solid #062a3b;
}

/* Textarea */
.editor-textarea {
  width: 100%;
  height: 100%;
  resize: none;
  border: none;
  background: transparent;
  color: black;
  outline: none;
  font-size: 15px;
  line-height: 1.8;
}

/* Preview styling */
.preview {
  color: black;
}

.preview h1,
.preview h2,
.preview h3 {
  margin: 8px 0;
  color: black;
  ;
}

.preview p {
  color: black;
  margin: 8px 0;
}

.preview ul {
  padding-left: 18px;
  margin: 6px 0;
}

.preview li {
  margin: 6px 0;
}

.inline-code {
  background: rgba(0, 0, 0, 0.25);
  padding: 2px 6px;
  border-radius: 6px;
}

.code-block {
  background: rgba(0, 0, 0, 0.35);
  padding: 12px;
  border-radius: 10px;
  overflow: auto;
  border: 1px solid rgba(255, 255, 255, 0.03);
  margin: 12px 0;
}

/* Empty State */
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
}

.empty-card {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.03), rgba(255, 255, 255, 0.01));
  padding: 36px;
  border-radius: 14px;
  text-align: center;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.5);
}

/* Footer */
.sidebar-footer {
  padding-top: 10px;
  text-align: center;
  color: rgba(230, 247, 255, 0.6);
  font-size: 12px;
}

/* Transitions */
.fade-enter-active,
.fade-leave-active {
  transition: opacity .2s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 900px) {
  .sidebar {
    display: none;
  }

  .editor {
    padding: 12px;
  }
}
</style>
