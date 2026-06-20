<template>
  <div class="tag-page-wrapper">
    <div class="tag-page-container">
      <!-- 标签展示区域 -->
      <div class="tags-container">
        <div
          v-for="tag in list"
          :key="tag.id"
          class="tag-item"
          :class="getTagSizeClass(tag.number)"
          @click="selectTag(tag)"
        >
          <span class="tag-name"># {{ tag.tagsName }}</span>
          <span class="tag-count">{{ tag.number }}</span>
        </div>
      </div>

      <!-- 选中标签的文章列表 -->
      <div v-if="selectedTag" class="tag-articles">
        <h3 class="articles-title">"{{ selectedTag.tagsName }}" 相关的文章</h3>
        <div class="articles-list">
          <article
            v-for="article in tagArticles"
            :key="article.id"
            class="article-item"
            @click="goToArticle(article)"
          >
            <h4 class="article-title">{{ article.title }}</h4>
            <p class="article-excerpt">{{ article.summary }}</p>
            <div class="article-meta">
              <span class="article-date">测试占位符</span>
              <span class="article-read-time">更新时间：{{
                article.updateTime == null
                  ? ''
                  : article.updateTime.slice(0, 10)
              }}</span>
            </div>
          </article>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import listMixin from '@/utils/mixins/listMixin'
export default {
  mixins: [listMixin],
  data() {
    return {
      list: [],
      selectedTag: null,
      tagArticles: [],
      sortedTags: []
    }
  },
  mounted() {
    const tagsName = this.$route.params.name
    if (tagsName != null) {
      this.selectedTag = {
        tagsName: this.$route.params.name
      }
      this.selectTag(this.selectedTag)
    }
    this.requestData()
  },
  methods: {
    requestData() {
      this.$get(this.$urls.getTagList).then((res) => {
        this.list = res.data
      })
    },
    getTagSizeClass(count) {
      if (count >= 20) return 'tag-large'
      if (count >= 10) return 'tag-medium'
      return 'tag-small'
    },
    selectTag(tag) {
      this.selectedTag = tag
      // 模拟获取标签相关文章
      this.$get(this.$urls.articlePage, { tags: tag.tagsName }, [
        this.currentPage,
        this.size
      ]).then((res) => {
        this.tagArticles = res.data.list
        if (this.$route.params.name !== tag.tagsName) {
          this.$router.replace({
            name: 'tagsName',
            params: { name: tag.tagsName }
          })
        }
      })
    },
    goToArticle(row) {
      // 跳转到文章详情页
      if (row.id) {
        this.$router.push({
          name: 'postViewer',
          params: { id: row.id }
        })
      }
    },
    formatDate(date) {
      return date.toLocaleDateString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.tag-page-wrapper {
  background-color: var(--background);
  min-height: 100vh;
}
.tag-page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

/* 页面头部 */
.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 36px;
  font-weight: 300;
  color: var(--text-color);
  margin: 0 0 10px 0;
}

.page-subtitle {
  font-size: 16px;
  color: var(--text-color2);
  margin: 0;
}

/* 标签容器 */
.tags-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 15px;
  margin-bottom: 60px;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  padding: 8px 16px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: var(--tag-background);
  border: var(--tag-border);
  color: var(--text-color);
}

.tag-item:hover {
  transform: translateY(-2px);
  box-shadow: var(--box-shadow);
}

.tag-name {
  font-weight: 500;
  margin-right: 8px;
}

.tag-count {
  background: var(--tag-background);
  color: var(--tag-text);
  padding: 2px 6px;
  border-radius: 12px;
  font-size: 12px;
}

/* 标签大小 */
.tag-small {
  font-size: 14px;
}

.tag-medium {
  font-size: 16px;
  padding: 10px 18px;
}

.tag-large {
  font-size: 18px;
  padding: 12px 20px;
}

/* 文章列表 */
.tag-articles {
  margin-top: 60px;
  padding-top: 40px;
  border-top: var(--style-border);
}

.articles-title {
  font-size: 24px;
  font-weight: 400;
  color: var(--text-color);
  margin-bottom: 30px;
  text-align: center;
}

.articles-list {
  display: grid;
  gap: 20px;
}

.article-item {
  padding: 20px;
  background-color: var(--card-background);
  border: var(--tag-border);
  color: var(--text-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  box-shadow: var(--box-shadow);
  transform: translateY(-2px);
}

.article-title {
  font-size: 18px;
  font-weight: 500;
  color: var(--text-color);
  margin: 0 0 10px 0;
}

.article-excerpt {
  color: var(--text-color2);
  line-height: 1.6;
  margin: 0 0 15px 0;
}

.article-meta {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: var(--text-color3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .tag-page-wrapper {
    padding-top: 48px;
  }
  .tag-page-container {
    padding: 20px 15px;
  }

  .page-title {
    font-size: 28px;
  }

  .tags-container {
    gap: 10px;
  }

  .tag-item {
    padding: 6px 12px;
  }
}

</style>
