<template>
  <div class="comment-section">
    <!-- 评论输入框 -->
    <div class="comment-editor">
      <div class="editor-header">
        <span class="editor-title">发表评论</span>
      </div>
      <textarea v-model="newComment" placeholder="写下你的评论..." class="comment-textarea" rows="3" />
      <div class="editor-footer">
        <div class="editor-actions">
          <button class="btn-cancel" @click="clearComment">取消</button>
          <button class="btn-submit" @click="submitComment">发表评论</button>
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <!-- <div class="list-header">
        <span class="comment-count">{{ comments.length }} 条评论</span>
        <div class="sort-options">
          <button :class="{ active: sortBy === 'time' }" @click="sortBy = 'time'">
            时间排序
          </button>
          <button :class="{ active: sortBy === 'hot' }" @click="sortBy = 'hot'">
            热度排序
          </button>
        </div>
      </div> -->

      <div v-for="comment in sortedComments" :key="comment.id" class="comment-item">
        <!-- 评论主体 -->
        <div class="comment-main">
          <div class="comment-avatar">
            <img :src="comment.avatar" :alt="comment.author">
          </div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="author-name">{{ comment.author }}</span>
              <span v-if="comment.isAuthor" class="author-badge">作者</span>
              <span class="comment-time">{{ formatTime(comment.time) }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
            <div class="comment-actions">
              <button class="action-btn" :class="{ active: comment.liked }" @click="toggleLike(comment)">
                <i class="icon-like" />
                <span v-if="comment.likes > 0">{{ comment.likes }}</span>
              </button>
              <button class="action-btn" @click="toggleReply(comment)">
                <i class="icon-reply" />
                回复
              </button>
              <button class="action-btn">
                <i class="icon-share" />
                分享
              </button>
              <button class="action-btn">
                <i class="icon-report" />
                举报
              </button>
            </div>
          </div>
        </div>

        <!-- 回复输入框 -->
        <div v-if="comment.showReply" class="reply-editor">
          <textarea v-model="comment.replyContent" :placeholder="'回复 @' + comment.author" class="reply-textarea"
            rows="2" />
          <div class="reply-actions">
            <button class="btn-cancel" @click="cancelReply(comment)">取消</button>
            <button class="btn-submit" @click="submitReply(comment)">回复</button>
          </div>
        </div>

        <!-- 子评论列表 -->
        <div v-if="comment.replies && comment.replies.length > 0" class="replies">
          <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
            <div class="reply-avatar">
              <img :src="reply.avatar" :alt="reply.author">
            </div>
            <div class="reply-content">
              <div class="reply-header">
                <span class="author-name">{{ reply.author }}</span>
                <span v-if="reply.isAuthor" class="author-badge">作者</span>
                <span class="reply-time">{{ formatTime(reply.time) }}</span>
              </div>
              <div class="reply-text">
                <span class="reply-to">@{{ reply.toAuthor }}</span>
                {{ reply.content }}
              </div>
              <div class="reply-actions">
                <button class="action-btn" :class="{ active: reply.liked }" @click="toggleLike(reply)">
                  <i class="icon-like" />
                  <span v-if="reply.likes > 0">{{ reply.likes }}</span>
                </button>
                <button class="action-btn" @click="toggleReply(reply, comment)">
                  回复
                </button>
              </div>
            </div>

            <!-- 回复子评论的输入框 -->
            <div v-if="reply.showReply" class="reply-editor reply-to-reply">
              <textarea v-model="reply.replyContent" :placeholder="'回复 @' + reply.author" class="reply-textarea"
                rows="2" />
              <div class="reply-actions">
                <button class="btn-cancel" @click="cancelReply(reply)">取消</button>
                <button class="btn-submit" @click="submitReplyToReply(reply, comment)">回复</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CommentSection',
  data() {
    return {
      newComment: '',
      sortBy: 'time', // 'time' or 'hot'
      comments: [
        {
          id: 1,
          author: '张三',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=1',
          content: '这个观点很有意思，我认为值得深入探讨。从实际应用的角度来看，确实有很多值得学习的地方。',
          time: Date.now() - 3600000,
          likes: 23,
          liked: false,
          isAuthor: false,
          showReply: false,
          replyContent: '',
          replies: [
            {
              id: 11,
              author: '李四',
              avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
              content: '同意你的观点，特别是关于实际应用的部分。',
              time: Date.now() - 1800000,
              likes: 5,
              liked: false,
              isAuthor: false,
              toAuthor: '张三',
              showReply: false,
              replyContent: ''
            }
          ]
        },
        {
          id: 2,
          author: '王五',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          content: '我觉得这个方案还有优化的空间，建议从用户体验的角度再考虑一下。',
          time: Date.now() - 7200000,
          likes: 15,
          liked: true,
          isAuthor: true,
          showReply: false,
          replyContent: '',
          replies: []
        }
      ]
    }
  },
  computed: {
    sortedComments() {
      const comments = [...this.comments]
      if (this.sortBy === 'time') {
        return comments.sort((a, b) => b.time - a.time)
      } else if (this.sortBy === 'hot') {
        return comments.sort((a, b) => b.likes - a.likes)
      }
      return comments
    }
  },
  methods: {
    submitComment() {
      if (!this.newComment.trim()) return

      const comment = {
        id: Date.now(),
        author: '当前用户',
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=current',
        content: this.newComment,
        time: Date.now(),
        likes: 0,
        liked: false,
        isAuthor: true,
        showReply: false,
        replyContent: '',
        replies: []
      }

      this.comments.unshift(comment)
      this.newComment = ''
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

    toggleReply(comment, parentComment = null) {
      if (parentComment) {
        // 如果是回复子评论，只切换该子评论的回复状态
        this.$set(comment, 'showReply', !comment.showReply)
      } else {
        // 如果是回复主评论，切换主评论的回复状态
        this.$set(comment, 'showReply', !comment.showReply)
      }
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
    }
  }
}
</script>

<style scoped>
.comment-section {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
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
  padding: 6px 16px;
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
  border-bottom: 1px solid #f0f2f7;
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
  border-bottom: 1px solid #f0f2f7;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.author-name {
  font-size: 15px;
  font-weight: 500;
  color: #121212;
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
  color: #121212;
  margin-bottom: 12px;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.action-btn {
  background: none;
  border: none;
  color: #8590a6;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
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
  margin-top: 12px;
  margin-left: 52px;
  background: #f6f6f6;
  padding: 12px;
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
  margin-bottom: 8px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 子评论 */
.replies {
  margin-top: 12px;
  margin-left: 52px;
  padding-left: 12px;
  border-left: 2px solid #f0f2f7;
}

.reply-item {
  display: flex;
  gap: 8px;
  padding: 8px 0;
}

.reply-avatar img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

.reply-content {
  flex: 1;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.reply-time {
  font-size: 13px;
  color: #8590a6;
}

.reply-text {
  font-size: 14px;
  line-height: 1.5;
  color: #121212;
}

.reply-to {
  color: #0066ff;
  margin-right: 4px;
}

.reply-actions {
  display: flex;
  gap: 12px;
  margin-top: 4px;
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
  background: #f9f9f9;
}

/* 确保回复输入框在子评论内部 */
.reply-item {
  position: relative;
}
</style>
