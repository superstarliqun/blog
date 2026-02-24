<template>
  <div class="comment-section">
    <div class="comment-button">
      <span :class="show == 1 ? 'active' : ''" @click="show = 1">发表评论</span>
      <span :class="show == 2 ? 'active' : ''" @click="show = 2">查看评论</span>
    </div>
    <!-- 评论输入框 -->
    <div v-if="show == 1" class="comment-editor">
      <div class="editor-header">
        <span class="editor-title">发表评论</span>
      </div>
      <!-- <textarea v-model="newComment" placeholder="写下你的评论..." class="comment-textarea" rows="3" /> -->
      <Input v-model="newComment" @user="handleUser" />
      <div class="editor-footer">
        <div class="editor-actions">
          <button class="btn-submit" @click="submitMainComment()">发表评论</button>
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div v-if="show == 2" class="comment-list">
      <!-- <div class="list-header">
        <span class="comment-count" />
      </div> -->

      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <!-- 评论主体 -->
        <div class="comment-main">
          <div class="comment-avatar">
            <img :src="comment.avatar" :alt="comment.avatar">
          </div>
          <div class="comment-content">
            <div class="comment-header">
              <a :href="comment.site.startsWith('http') ? comment.site : `https://${comment.site}`" target="_blank"
                rel="noopener noreferrer" style="  color: inherit;text-decoration: none;">
                <span class="author-name">{{ comment.nickname }}</span>
              </a>
              <span v-if="comment.isAuthor" class="author-badge">作者</span>
              <!-- <span>辽宁</span>
              <span>Windows 11</span>
              <span>Chrome 142.0.0.0</span> -->
            </div>
            <div class="comment-text" v-html="$emoji(comment.content)" />
            <div class="comment-actions">
              <span class="comment-time">{{ comment.createTime }}</span>
              <span class="action-btn" @click="toggleReply(comment.id, comment)">回复</span>
            </div>
          </div>
        </div>
        <!-- 回复输入框 -->
        <div v-if="comment.showReply" class="reply-editor">
          <!-- <textarea v-model="comment.replyContent" :placeholder="'回复 @' + comment.nickname" class="reply-textarea"
            rows="2" /> -->
          <Input v-model="comment.replyContent" @user="handleUser" />
          <div class="reply-actions">
            <button class="btn-cancel" @click="cancelReply(comment)">取消</button>
            <button class="btn-submit" @click="submitReplyComment(comment)">回复</button>
          </div>
        </div>
        <!-- 子评论列表 -->
        <Item v-for="item in comment.children" :key="item.id" :item="item"
          :parent-info="{ id: comment.id, postId: comment.postId, nickname: comment.nickname }" />
      </div>
    </div>
  </div>
</template>

<script>
import Item from './item.vue'
import Input from './input.vue'
const req = require.context('@/assets/img/emoji', false, /\.png$/)
const emojiMap = {}
req.keys().forEach(k => {
  const name = k.replace('./', '').replace('.png', '')
  emojiMap[name] = req(k)
})
export default {
  name: 'CommentSection',
  components: { Item, Input },
  data() {
    return {
      newComment: '',
      sortBy: 'time',
      show: 2,
      reply: null,
      comments: [],
      commentCount: 0,
      commentUser: {}
    }
  },
  created() {
    this.requestData()
  },
  methods: {
    requestData() {
      this.$get(this.$urls.getComment, null, [this.$route.params.id]).then((res) => {
        if (res.data.length > 0 && res.data != null) {
          this.comments = res.data
          this.commentCount = res.data[0].total
        }
        this.$emit('call-back', this.commentCount)
      })
    },
    // 主题评论提交
    submitMainComment() {
      const param = {
        postId: this.$route.params.id,
        content: this.newComment,
        parentId: 0,
        nickname: this.commentUser.nickname,
        email: this.commentUser.email,
        site: this.commentUser.site
      }
      this.$post(this.$urls.addComment, param).then(res => {
        if (res.code === 0) {
          this.requestData()
          this.$message.success('评论成功')
          this.show = 2
          this.newComment = null
        }
      })
    },
    // 回复评论提交
    submitReplyComment(element) {
      const param = {
        postId: this.$route.params.id,
        content: element.replyContent,
        parentId: element.id,
        nickname: this.commentUser.nickname,
        email: this.commentUser.email,
        site: this.commentUser.site
      }
      this.$post(this.$urls.addComment, param).then(res => {
        if (res.code === 0) {
          this.requestData()
          this.$message.success('评论成功')
          element.showReply = false
        }
      })
    },

    submitReply(comment) {
      if (!comment.replyContent.trim()) return

      const reply = {
        id: Date.now(),
        author: '当前用户',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=current',
        content: comment.replyContent,
        time: Date.now(),
        likes: 0,
        liked: false,
        isAuthor: true,
        toAuthor: comment.author,
        showReply: false,
        replyContent: ''
      }

      if (!comment.replies) {
        this.$set(comment, 'replies', [])
      }
      comment.replies.push(reply)
      comment.replyContent = ''
      comment.showReply = false
    },

    // 新增：回复子评论的方法
    submitReplyToReply(reply, parentComment) {
      if (!reply.replyContent.trim()) return

      const newReply = {
        id: Date.now(),
        author: '当前用户',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=current',
        content: reply.replyContent,
        time: Date.now(),
        likes: 0,
        liked: false,
        isAuthor: true,
        toAuthor: reply.author, // 关键修复：回复的是reply的作者，而不是parentComment的作者
        showReply: false,
        replyContent: ''
      }

      if (!parentComment.replies) {
        this.$set(parentComment, 'replies', [])
      }
      parentComment.replies.push(newReply)
      reply.replyContent = ''
      reply.showReply = false
    },

    toggleLike(item) {
      if (item.liked) {
        item.likes--
        item.liked = false
      } else {
        item.likes++
        item.liked = true
      }
    },

    toggleReply(commentId, element) {
      this.$set(element, 'showReply', !element.showReply)
      this.reply = commentId
    },

    cancelReply(comment) {
      this.$set(comment, 'showReply', false)
      this.$set(comment, 'replyContent', '')
    },

    clearComment() {
      this.newComment = ''
    },

    formatTime(timestamp) {
      const now = Date.now()
      const diff = now - timestamp

      if (diff < 60000) {
        return '刚刚'
      } else if (diff < 3600000) {
        return Math.floor(diff / 60000) + ' 分钟前'
      } else if (diff < 86400000) {
        return Math.floor(diff / 3600000) + ' 小时前'
      } else {
        return Math.floor(diff / 86400000) + ' 天前'
      }
    },
    textToHtml(text) {
      if (!text) return ''
      // 匹配 [xxx] 格式
      return text.replace(/\[([^\[\]]+)\]/g, (match, name) => {
        const src = this.emojiMap[name]
        // 如果 emojiMap 里存在这个表情，则替换为 img 标签，否则保留原样
        return src
          ? `<img src="${src}" data-char="${name}" class="emoji-item">`
          : match
      })
    },
    // 评论用户信息监听
    handleUser(item) {
      this.commentUser = item
    }
  }
}
</script>

<style scoped>
.comment-section {
  margin: 0 auto;
  background: var(--card-background);
  border-radius: 8px;
}

/* 评论编辑器 */
.comment-editor {
  padding: 20px;
  border-bottom: 1px solid #f0f2f7;
}

.editor-header {
  margin-bottom: 12px;
}

.editor-title {
  font-size: 16px;
  font-weight: 500;
  color: #121212;
}

.comment-textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #e7eaf1;
  border-radius: 6px;
  resize: vertical;
  font-size: 14px;
  line-height: 1.5;
  outline: none;
  transition: border-color 0.2s;
}

.comment-textarea:focus {
  border-color: #0066ff;
}

.editor-footer {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.editor-actions {
  display: flex;
  gap: 12px;
}

.btn-cancel,
.btn-submit {
  padding: 4px 12px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel {
  background: #f6f6f6;
  color: #8590a6;
}

.btn-cancel:hover {
  background: #e7eaf1;
}

.btn-submit {
  background: #0066ff;
  color: white;
}

.btn-submit:hover {
  background: #0052cc;
}

/* 评论列表 */
.comment-list {
  padding: 20px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
}

.comment-count {
  font-size: 16px;
  font-weight: 500;
  color: #121212;
}

.sort-options {
  display: flex;
  gap: 16px;
}

.sort-options button {
  background: none;
  border: none;
  color: #8590a6;
  font-size: 14px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s;
}

.sort-options button.active {
  color: #0066ff;
  background: rgba(0, 102, 255, 0.1);
}

.sort-options button:hover:not(.active) {
  color: #606a78;
}

/* 评论项 */
.comment-item {
  padding: 16px 0;
  border-bottom: 1px solid var(--article-border-bottom);
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment-avatar img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 10px;
  color: #3c3c3c;

  >* {
    display: inline-flex;
    align-items: center;
    line-height: 15px;
  }

  &>span {
    background-color: #f7f7f9;
    border: 1px solid #e3e8f7;
    padding: 0px 8px;
    border-radius: 8px;
    vertical-align: middle;
  }

}

.author-name {
  font-size: 15px;
  font-weight: 600;
  margin-top: 0px;
  color: var(--text-color);
  line-height: 1;
}

.author-badge {
  background: #0066ff;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 3px;
}

.comment-time {
  font-size: 13px;
  color: #8590a6;
}

.comment-text {
  font-size: 15px;
  line-height: 1.6;
  color: var(--text-color);
}

.comment-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.action-btn {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #8590a6;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s;
}

.action-btn:hover {
  background: #f6f6f6;
  color: #606a78;
}

.action-btn.active {
  color: #0066ff;
}

/* 回复编辑器 */
.reply-editor {
  margin-left: 48px;
  border-radius: 6px;
}

.reply-textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e7eaf1;
  border-radius: 4px;
  resize: vertical;
  font-size: 14px;
  line-height: 1.5;
  outline: none;
}

.reply-actions {
  display: flex;
  gap: 8px;
}

/* 子评论 */
.replies {
  margin-top: 8px;
  margin-left: 48px;
}

.reply-item {
  display: flex;
  gap: 8px;
  padding: 8px 0;
}

.reply-avatar img {
  width: 28px;
  height: 28px;
  border-radius: 50%;
}

.reply-content {
  flex: 1;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.reply-time {
  font-size: 13px;
  color: #8590a6;
}

.reply-text {
  font-size: 14px;
  line-height: 1;
  color: #121212;
}

.reply-to {
  color: #0066ff;
  margin-right: 4px;
}

.reply-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
  align-items: center;
  float: right;
}

/* 图标样式 */
.icon-like::before {
  content: "👍";
  font-size: 14px;
}

.icon-reply::before {
  content: "💬";
  font-size: 14px;
}

.icon-share::before {
  content: "📤";
  font-size: 14px;
}

.icon-report::before {
  content: "⚠️";
  font-size: 14px;
}

/* 新增：回复子评论的输入框样式 */
.reply-to-reply {
  margin-left: 0;
  margin-top: 8px;
}

/* 确保回复输入框在子评论内部 */
.reply-item {
  position: relative;
}

.comment-button {
  display: flex;
  cursor: pointer;
  font-size: 14px;
  line-height: 24px;

  span {
    transition: color 0.25s ease,
      background-color 0.25s ease,
      padding 0.25s ease;
  }

  & .active {
    color: white;
    background-color: rgb(140, 197, 255);
    padding: 0 2px;
    border-radius: 4px;
    border: 1px solid rgb(140, 197, 255);
  }

  & span:first-child {
    position: relative;
    margin-right: 20px;
  }

  & span:first-child::after {
    content: "";
    position: absolute;
    right: -10px;
    top: 50%;
    transform: translateY(-50%);
    width: 2px;
    height: 18px;
    background: #42a1c7;
    border-radius: 4px;
  }
}
</style>
