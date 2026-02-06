<template>
  <div class="node-container" :style="item.level == 2 ? 'margin-left: 48px;' : ''">
    <div class="comment-main">
      <div class="comment-avatar">
        <img :src="item.author" :alt="item.author">
      </div>
      <div class="comment-content">
        <div class="comment-header">
          <span class="author-name">{{ item.nickname }}</span>
          <span v-if="item.isAuthor" class="author-badge">作者</span>
        </div>
        <div class="comment-text"><span class="mark">@{{ parentInfo.nickname }}:</span>
          <div v-html="$emoji(item.content)" />
        </div>
        <div class="comment-actions">
          <span class="comment-time">{{ item.createTime }}</span>
          <span class="action-btn" @click="handleReply(item)">回复</span>
        </div>
      </div>
    </div>
    <!-- 回复输入框 -->
    <div v-if="item.showReply" class="reply-editor">
      <!-- <textarea v-model="newComment" :placeholder="'回复 @' + item.nickname" class="reply-textarea" rows="2" /> -->
      <Input v-model="newComment" @user="handleUser" />
      <div class="reply-actions">
        <span class="btn-cancel" @click="handleReply(item)">取消</span>
        <span class="btn-submit" @click="submit(item)">回复</span>
      </div>
    </div>
    <template v-if="item.children && item.children.length">
      <comment-item v-for="(child, index) in item.children" :key="index" :item="child"
        :parent-info="{ id: item.id, postId: item.postId, nickname: item.nickname }" />
    </template>
  </div>
</template>

<script>
import Input from './input.vue'
export default {
  name: 'CommentItem',
  components: { Input }, // 必须定义 name 才能在模板中递归调用
  props: ['item', 'parentInfo'],
  data() {
    return {
      newComment: '',
      commentUser: {}
    }
  },
  methods: {
    handleReply(item) {
      if (typeof item.showReply === 'undefined') {
        // 参数：对象, 属性名, 初始值
        this.$set(item, 'showReply', true)
      } else {
        item.showReply = !item.showReply
      }
    },
    submit(item) {
      const param = {
        postId: item.postId,
        content: this.newComment,
        parentId: item.id,
        nickname: this.commentUser.nickname,
        email: this.commentUser.email,
        site: this.commentUser.site
      }
      this.$post(this.$urls.addComment, param).then(res => {
        if (res.code === 0) {
          const now = new Date()
          // 修正时区偏移并格式化
          const formatted = new Date(now.getTime() - now.getTimezoneOffset() * 60000).toISOString().replace('T', ' ').substring(0, 19)
          param.createTime = formatted
          param.nickname = res.data.nickname
          param.author = 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
          if (item.children != null && item.children.length > 0) {
            item.children.unshift(param)
          } else {
            this.$set(item, 'children', [])
            item.children.unshift(param)
          }
          this.$message.success('评论成功')
          item.showReply = !item.showReply
        }
      })
    },
    // 评论用户信息监听
    handleUser(item) {
      this.commentUser = item
    }
  }
}
</script>

<style lang="scss" scoped>
.node-container {
  margin-top: 8px;
}

.comment-avatar img {
  width: 28px;
  height: 28px;
  border-radius: 50%;
}

.comment-main {
  display: flex;
  gap: 12px;

  .comment-header {
    display: flex;
    align-items: center;
    margin-bottom: 4px;

    .author-name {
      font-size: 15px;
      font-weight: 600;
      margin-top: 0px;
      color: #121212;
      line-height: 1;
    }
  }

  .comment-content {
    flex: 1;
  }

  .comment-time {
    font-size: 13px;
    color: #8590a6;
  }

  .comment-text {
    font-size: 15px;
    line-height: 1.6;
    color: #121212;
    display: flex;

    .mark {
      color: #409EFF;
      margin-right: 8px;
      line-height: 34px;
    }
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
}

.reply-editor {
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
    margin-top: 10px;
    align-items: center;
    float: right;

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
  }
}
</style>
