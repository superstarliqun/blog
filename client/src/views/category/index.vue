<template>
  <div class="container-main">
    <div class="container">
      <div class="category-top-category fixed-top">
        <a v-for="(item, index) in list" :key="index" :class="{ 'active-selected': activeName === item.categoryName }"
          @click="handleSearchValue(item)">
          {{ item.categoryName }}
        </a>
      </div>
      <div v-if="!isLoading" class="transition1">
        <div v-for="(item, index) in postList" :key="index" class="category-card" @click="toDetails(item)">
          <div class="category-title">
            {{ item.title }}
          </div>

          <p class="category-summary">
            {{ item.summary }}
          </p>

          <div class="category-footer">
            <div class="author-info">
              <span class="icon">{{ item.createBy }}</span>
              <span class="date">{{ item.date }}</span>
            </div>

            <div class="stats-tags">
              <span class="stat">
                💬 {{ item.comments }}
              </span>
              <span class="stat">
                👍 {{ item.likes }}
              </span>
            </div>
          </div>

          <hr class="separator">
        </div>
      </div>
      <div v-else class="transition2">
        <el-skeleton :rows="20" animated />
      </div>
    </div>
  </div>
</template>

<script>
import CryptoJS from 'crypto-js'
import listMixin from '@/utils/mixins/listMixin'
export default {
  name: 'Category',
  mixins: [listMixin],
  data() {
    return {
      list: [],
      postList: [],
      SHARED_SECRET: 'YourCustomSecretKey2025',
      activeName: null, // 用于存储激活的分类名称
      isLoading: false // 【新增】加载状态
    }
  },
  async mounted() {
    await this.requestData()
    var categoryId = null
    this.activeName = this.$route.params.categoryName
    this.list.forEach((category) => {
      if (category.categoryName === this.activeName) {
        categoryId = category.id
      }
    })
    if (categoryId) {
      this.requestArticleList(Number(categoryId))
    } else {
      this.requestArticleList(1) // 默认加载第一个分类的文章列表
    }
  },
  methods: {
    // 文章后台签名生成函数
    generateSign(categoryId) {
      // 1. 获取当前时间戳（精确到分钟，与后端保持一致）
      const timestamp = Math.floor(Date.now() / 60000).toString()
      // 这里我们简化，约定顺序为：categoryId + timestamp + SHARED_SECRET
      const rawString = `${categoryId}${timestamp}${this.SHARED_SECRET}`
      // 3. 计算 SHA-256 哈希
      const hash = CryptoJS.SHA256(rawString).toString(CryptoJS.enc.Hex)
      return {
        timestamp: timestamp,
        sign: hash
      }
    },
    // 获取文章分类列表
    async requestData(id) {
      await this.$get(this.$urls.getDefaultCategory).then((res) => {
        if (res.code === 0) {
          this.list = res.data
        }
      }).catch(() => { })
    },
    // 获取文章列表
    requestArticleList(id) {
      this.isLoading = true // 【开始加载】设置为 true
      this.searchForm.category = id || this.activeId
      this.$get(this.$urls.articlePage, this.searchForm, [this.currentPage, this.size], { 'X-Request-Sign': id }).then((res) => {
        this.postList = res.data.list
        this.total = res.data.total
      }).catch(() => {
        console.log('错误!')
      }).finally(() => {
        this.isLoading = false
      })
    },
    // 选中分类
    handleSearchValue(row) {
      this.activeId = row.id
      this.activeName = row.categoryName
      const pathParam = this.$route.params.categoryName
      if (row.categoryName === pathParam) return
      this.requestArticleList(this.activeId)
      this.$router.push({
        name: 'CategoryDetail',
        params: { categoryName: row.categoryName }
      })
    },
    // 跳转详情
    toDetails(row) {
      if (row.id) {
        this.$router.push({
          name: 'postViewer',
          params: { id: row.id }
        })
      }
    }
  }
}
</script>
<style scoped lang="scss" src="./index.scss"></style>
