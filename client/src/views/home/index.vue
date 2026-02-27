<template>
  <div class="wu-wrap">
    <div class="wu-content">
      <div class="wu-home-body">
        <div v-if="false" class="banner-container" style="position: relative;box-shadow:0 8px 16px -4px #2c2d300c;">
          <img class="left-banner" style="" src="https://p.zhheo.com/nI40Ii25391281766720093204.webp!blogimg_1600w">
          <div class="banner-text">
            <a href="#">用腾讯CoDesign打造专属SVG图标库</a>
            <span
              class="desc jianjie">腾讯CoDesign支持用户上传自定义SVG图标文件，帮助设计团队建立专属图标库。通过简单的拖拽操作即可批量上传SVG文件，支持保留或去除颜色，实现图标资源的统一管理和高效调用。</span>
            <p class="banner-list-footer">
              <span># 2026-01-14 </span>
              <span>Java</span>
              <span class="icon-container"><i class="iconfont icon-huifu" />3</span>
              <span class="icon-container"><i class="iconfont icon-guankan" /> 1035</span>
            </p>
          </div>
        </div>
        <div v-if="false" class="hot-container">
          <p class="hot-title container-title">热门文章</p>
          <ul class="left-images-module">
            <li v-for="value in 3" :key="value">
              <img src="	https://p.zhheo.com/ic92k123391281765529373270.webp!cover" alt="">
              <div class="images-module-container">
                <a href="#">热门标题中心控制呢</a>
                <span class="desc">这篇文章介绍了如何在 macOS 上使用快捷指令结合 shell 命令来进行媒体文件的格式转换。文章首先讲解了安装 Homebrew 和 ffmpeg
                  的方法，使系统具备音视频处理能力；随后详细介绍了创建快速操作快捷指令的步骤，包括选择媒体类型、添加 shell 脚本、设置参数</span>
                <p class="left-list-footer">
                  <span># 2026-01-14</span>
                  <span>Java</span>
                  <span class="icon-container"><i class="iconfont icon-huifu" />3</span>
                  <span class="icon-container"> <i class="iconfont icon-guankan" />1035</span>
                </p>
              </div>
            </li>
          </ul>
        </div>
        <div class="new-container">
          <p v-if="false" class="hot-title container-title">最新文章</p>
          <ul v-if="total > 0" class="article-content">
            <li v-for="item in tableData" :key="item.id" class="article-item">
              <div class="article-item-inner">
                <div class="article-item-title" @click="toDetails(item)">
                  {{ item.title }}
                </div>
                <div class="article-item-media">
                  {{
                    item.summary == null ? '这篇文章没有留下点什么' : item.summary
                  }}
                </div>
                <div class="article-item-bottom">
                  <div class="article-item-category">
                    {{ converter(item.category) }}
                  </div>
                  <div class="article-item-trends">
                    <span>观看 {{ item.views }}</span>
                    <span>回复 {{ item.comments }}</span>
                    <span>点赞 {{ item.likes }}</span>
                  </div>
                </div>
              </div>
            </li>
            <li v-if="loadingShow">
              <el-skeleton class="loading-icon" :rows="3" animated />
            </li>

          </ul>
          <div v-else class="source">
            <el-skeleton :rows="6" />
          </div>
          <Page v-if="total" :total="total" :current-page="currentPage" @current-change="handleCurrentPage" />
          <!-- 分页组件 -->
        </div>
      </div>
      <div class="wu-home-right">
        <Blogger />
        <Category v-if="categoryList.length > 0" v-model="categoryList" />
        <Tags />
        <Love />
      </div>
    </div>
  </div>
</template>

<script>
import listMixin from '@/utils/mixins/listMixin'
import Page from './component/page.vue'
import Blogger from './component/blogger'
import Category from './component/category'
import Tags from './component/tags'
import Love from './component/love'
export default {
  metaInfo: {
    title: '灵光一刻',
    meta: [
      { name: 'description', content: '专注分享Java后端、前端开发、系统运维、面试经验等技术干货，提供编程实战、学习笔记与项目总结，帮助开发者提升技能、高效成长。' },
      { name: 'keywords', content: '个人博客,Java,后端开发,前端,Vue,系统运维,面试总结,技术教程,原创博客' }
    ]
  },
  components: { Page, Blogger, Category, Tags, Love },
  mixins: [listMixin],
  data() {
    return {
      tableData: [],
      categoryList: [],
      loadingShow: false,
      timer: new Date(),
      timerEntity: {
        year: null,
        month: null,
        day: null,
        hour: null,
        minute: null,
        second: null
      },
      isMobile: false
    }
  },
  created() {
    this.requestData()
  },
  mounted() {
    this.checkDevice()
    window.addEventListener('resize', this.checkDevice)
    if (this.isMobile) {
      window.addEventListener('scroll', this.handleScroll)
    }
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    // 获取文章列表 支持分页参数
    requestData() {
      const abc = Number(this.$route.params.id)
      if (isNaN(abc)) {
        this.currentPage = 1
      } else {
        this.currentPage = abc
      }
      this.loadingShow = true
      // 只有第一次加载或需要时才查分类，没必要每次分页都查
      if (this.categoryList.length === 0) {
        this.$get(this.$urls.getDefaultCategory).then((res) => {
          if (res.code === 0) this.categoryList = res.data
        })
      }
      // 关键：明确获取当前页码（优先使用 Mixin 或 Data 中的 currentPage）
      const pageNumber = this.$route.params.id || this.currentPage || 1
      // 查询文章列表
      this.$get(this.$urls.articlePage, this.searchForm, [
        pageNumber,
        this.size
      ]).then((result) => {
        if (result.code === 0) {
          const newList = result.data.list
          // 累加数据而非覆盖
          this.tableData = [...this.tableData, ...newList]
          this.total = result.data.total
        }
        this.loadingShow = false
      })
        .catch(() => {
          this.loadingShow = false
        })
    },
    // 精确文章分类查询列表
    handleSearchValue(index) {
      this.searchForm.category = index
      this.tableData = []
      window.scrollTo({ top: 0, behavior: 'smooth' })
      this.handleSearch()
    },
    // 跳转详情
    toDetails(row) {
      if (row.id) {
        this.$router.push({
          name: 'postViewer',
          params: { id: row.id }
        })
      }
    },
    // 滑动底部监听
    handleScroll() {
      if (this.loadingShow) return // 正在加载中，跳过
      // 文章的高度
      const arricleHeight = document.querySelector('.wu-home-body').scrollHeight
      // 屏幕滑动距离
      const slidingDistance = document.documentElement.scrollTop
      // 当前屏幕展示高度
      const windowHeight = window.innerHeight
      // 235 是头部以及底部总共的距离
      if (arricleHeight - 200 <= slidingDistance + windowHeight - 235) {
        this.loadMoreData()
      }
    },
    // 加载更多判断条件
    loadMoreData() {
      if (this.tableData.length < this.total) {
        this.currentPage++ // Mixin 里的变量
        this.requestData()
      }
    },
    goToTool(route) {
      window.open('/nas', '_blank')
    },
    // 转化类型
    converter(id) {
      const target = this.categoryList.filter((item) => {
        return item.id === id
      })
      if (target.length > 0) {
        return target[0].categoryName
      } else {
        return null
      }
    },
    checkDevice() {
      this.isMobile = window.innerWidth <= 768 // 常用阈值
    },
    handleCurrentPage(number) {
      window.location.href = number === 1 ? '/' : `/page/${number}`
    }
  }
}
</script>

<style lang="scss" scoped>
.wu-wrap {
  min-height: 100%;
}

.right-notice {
  padding: 10px 18px;
  background-color: white;

  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
  border-radius: 6px;

  .notice-title {
    position: relative;
    font-size: 18px;
    margin-left: 10px;
  }

  .notice-title::after {
    position: absolute;
    content: ' ';
    left: -8px;
    top: 50%;
    margin-top: -8px;
    width: 3px;
    height: 14px;
    background: linear-gradient(135deg, #2791ff, #03a7ff 98%);
    border-radius: 8px;
  }
}

.source {
  padding: 24px;
}

::v-deep .infinite-status-prompt {
  color: #8a8a8a;
  padding: 10px 0;
  font-size: 14px;
}

.class-article {
  width: 100%;
  // box-shadow: 0 2px 4px 0 rgba(3, 27, 78, 0.06);
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
  background-color: rgb(255, 255, 255);
  display: flex;
  /* 水平排列 li 元素 */
  gap: 15px;
  /* 设置列表项之间的间距 */
  list-style-type: none;
  /* 去掉默认的列表样式 */
  padding: 10px 15px 10px 15px;
  /* 上下边距 */
  font-size: 16px;
  /* 字体大小 */
  margin-bottom: 10px;

  li {
    cursor: pointer;
    /* 鼠标指针为手形 */
    color: #000;
    /* 默认文字颜色 */
    font-weight: bold;
    /* 字体加粗 */
    padding: 5px 10px;
    /* 内边距 */
    transition: color 0.3s ease;
    /* 颜色渐变 */
  }

  li:hover {
    color: #03a7ff;
  }

  li:active {
    color: #03a7ff;
  }
}

.show-button {
  color: #8a8a8a;
  position: absolute;
  top: 10px;
  right: 0px;
  font-size: 14px;
  cursor: pointer;
  display: none;
}

.article-item-inner:hover .show-button {
  display: block;
}

.edit {
  margin-right: 40px;
}

.loading-icon {
  padding: 24px;
}

// 固钉样式
// .sticky-box {
//   position: sticky;
//   top: 10px;
//   z-index: 10;
//   background-color: #fff;
//   box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
// }

// 右侧分类样式
.category_list {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-top: 14px;
}

.category_item {
  width: 45%;
  cursor: pointer;
}

.category_item a {
  border-radius: 8px;
  margin: 4px 0;
  display: flex;
  flex-direction: column;
  align-content: space-between;
  border: 1px solid #e3e8f7;
  text-align: center;
  padding: 10px;
  color: black;
}

.category_item a:hover {
  color: white;
  font-weight: 700;
  background-color: var(--theme-color3);
}

// 右侧工具栏样式
/* 容器：使用 Grid 实现多列图标对齐 */
.app-icon-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 6px 8px;
  padding-top: 8px;
  background-color: transparent;
}

/* 单个工具项 */
.tool-item {
  display: flex;
  flex-direction: column;
  /* 垂直排列：图标在上，文字在下 */
  align-items: center;
  /* 水平居中 */
  cursor: pointer;
  padding: 10px 0;
  /* 增加点击区域的垂直缓冲 */
  border-radius: 8px;
  transition: background-color 0.2s;
}

/* 鼠标悬停效果：轻微背景色变化 */
.tool-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
  /* 浅色半透明背景，如果有深色底会很好看 */
}

/* 图标包裹层，确保图标区域独立 */
.icon-wrapper {
  margin-bottom: 8px;
  /* 图标和文字之间的间距 */
}

/* 实际的图标样式：实现圆角矩形和阴影效果 */
.tool-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  /* 柔和的圆角 */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  color: white;
  /* 模拟图标的文字或占位符颜色 */

  /* 关键：轻微的 Box Shadow 模拟图标的层次感和光泽 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
}

/* 工具名称文字 */
.tool-name {
  font-size: 12px;
  text-align: center;
  color: #333;
  line-height: 1.4;
}

::v-deep .router-link-active {
  text-decoration: none;
}

::v-deep a {
  color: unset;
  text-decoration: none;
}

.wu-home-body {
  background: none;
  box-shadow: unset;
}

.wu-home-right {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

// 文章列表样式
.article-content {
  background-color: var(--card-background);
  box-shadow: var(--box-shadow);
  border: var(--style-border);
  border-radius: 8px;
}

// 文章列表下#分类悬浮效果
.article-item-category::before {
  padding-right: 8px;
  content: '#';
  font-size: 12px;
  opacity: 0.4;
}

.article-item-category:hover {
  cursor: pointer;
  color: var(--theme-color2);
}

// 文章列表页 右侧公告
.notice-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-top: 6px;
}

.notice-item {
  display: flex;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px dashed rgba(99, 102, 241, 0.1);
  cursor: pointer;
}

.notice-item:last-child {
  border-bottom: none;
}

.icon-code {
  display: inline-block;
  vertical-align: middle;
  transition: transform 0.3s ease;
  font-weight: bold;

  margin-right: 4px;
  font-size: 14px;
}

.notice-item:hover {
  .icon-code {
    transform: scale(1.2);
  }
}

.bgc {
  position: relative;
  z-index: 2;
}

.bgc::before {
  content: '';
  position: absolute;
  width: 100%;
  height: 138px;
  top: -9px;
  border-radius: 17px;
  right: 0;
  z-index: -1;
  background: url('@/assets/img/0c48561553638208a7d9.png') 50% no-repeat !important;
}

.article-item {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 2;
  border-bottom: 1px solid var(--article-border-bottom);
  margin: 0 24px;
  transition: all 0.2s ease-in-out 0s;

  img {
    // width: 220px;
    height: 110px;
    border-radius: 8px;
    text-align: center;
    justify-items: center;
  }
}

.article-item-inner {
  display: flex;
  position: relative;
  z-index: 2;
  justify-content: space-between;
  flex-direction: column;
  padding: 18px 0;
  margin-left: 0.8rem;
  flex: 1;

  .article-item-title {
    font-size: 18px;
    font-weight: 600;
    line-height: 24px;
    color: var(--text-color);
    overflow: hidden;
    white-space: normal;
    word-break: break-word;
    cursor: pointer;
  }

  .article-item-title:hover {
    color: #2791ff;
  }

  .article-item-media {
    min-height: 3em;
    margin-top: 8px;
    font-size: 14px;
    color: var(--text-color2);
    overflow: hidden;
    text-overflow: ellipsis;
    word-break: break-word;
  }
}

.article-item-bottom {
  display: flex;
  color: var(--text-color2);
  font-size: 12px;
  margin-top: 14px;

  .item-date {
    margin-left: 24px;
    position: relative;
  }

  .item-date::before {
    position: absolute;
    content: ' ';
    left: -15px;
    top: 50%;
    margin-top: -1px;
    width: 6px;
    height: 2px;
    background: linear-gradient(135deg, #8a8a8a, #8a8a8a 98%);
    border-radius: 8px;
  }

  .article-item-trends {
    display: flex;
    justify-content: space-between;
    margin-left: auto;

    span {
      margin-left: 10px;
    }
  }
}

.user-card {
  padding-top: 8px;
  background-color: #fff;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);

  // 顶部信息区域
  .header {
    display: flex;
    align-items: center;
    margin-top: 16px;
    margin-bottom: 20px;

    .avatar-container {
      width: 60px;
      height: 60px;
      margin-right: 15px;

      .avatar {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        object-fit: cover;
      }
    }

    .info-content {
      flex-grow: 1;

      .nickname {
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin: 0 0 5px 0;
      }

      .tags {
        display: flex;
        align-items: center;

        .tag {
          font-size: 12px;
          padding: 2px 8px;
          border-radius: 4px;
          margin-right: 8px;
          white-space: nowrap;
        }

        // 博客等级标签
        .level {
          background-color: #fcebeb;
          color: #e64a4a;
          border: 1px solid #e64a4a;
        }

        // 码龄标签
        .years {
          background-color: #f0f0f0;
          color: #666;
        }
      }
    }
  }

  // 中间创作者标签
  .creator-badge {
    background-color: #fff8e1;
    color: #c99c33;
    padding: 8px 10px;
    border-radius: 4px;
    margin-bottom: 20px;
    font-size: 14px;
    display: flex;
    align-items: center;

    .badge-icon {
      margin-right: 5px;
      font-size: 16px;
    }
  }

  // 底部数据统计区域
  .stats-container {
    display: flex;
    justify-content: space-between;
    text-align: center;
    margin-bottom: 10px;

    .stat-item {
      flex: 1;
      padding: 0 5px;

      .number {
        font-size: 16px;
        // font-weight: bold;
        color: #333;
        margin-bottom: 4px;
      }

      .label {
        font-size: 12px;
        color: #888;
      }
    }
  }
}

// 每个容器的标题
.container-title {
  align-items: center;
  font-weight: 600;
  font-size: 20px;
  margin-bottom: 8px;
}

.banner-container {
  .left-banner {
    display: block;
    box-shadow: 0 8px 16px -4px #2c2d300c;
    width: 100%;
    height: 420px;
    object-fit: cover;
    border-radius: 8px;
    position: relative;
  }

  .banner-text {
    position: absolute;
    bottom: 0;
    left: 0;
    padding: 20px;
    color: white;
    display: flex;
    flex-direction: column;
    gap: 10px;
    font-size: 14px;
    line-height: 1;

    background: rgba(0, 0, 0, 0.4);
    backdrop-filter: blur(4px) saturate(180%);
    border-radius: 0 0 8px 8px;
    border-top: 1px solid rgba(127, 127, 127, 0.5);

    &::before {
      /* 注入 SVG 噪点数据 */
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
      /* 透明度必须极低，建议 0.03 - 0.08 */
      opacity: 0.05;
      pointer-events: none;
      border-radius: 8px;
    }

    .banner-list-footer {
      display: flex;
      gap: 8px;
      line-height: 1;
      color: #d3d3d3;

      .iconfont {
        font-size: 14px;
      }
    }

    .jianjie {
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      overflow: hidden;
      text-overflow: ellipsis;

      line-height: 1.6;
      max-height: calc(1.6em * 2);
      color: #d3d3d3;
    }

    a {
      color: white;
      text-decoration: none;
      cursor: default;
      outline: none;
      font-weight: 600;
      font-size: 34px;
    }
  }
}

.hot-container {
  padding: 16px 0;

  .left-images-module {
    font-size: 12px;
    color: #76788d;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(270px, 1fr));
    gap: 16px;
    cursor: pointer;

    li {
      display: flex;
      flex-direction: column;
      // gap: 12px;
      background-color: #fff;
      box-shadow: 0 8px 16px -4px #2c2d300c;
      border-radius: 8px;

      .images-module-container {
        padding: 10px;
        display: flex;
        flex-direction: column;
        gap: 12px;
        font-size: 12px;
        justify-content: space-between;

        .desc {
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          line-clamp: 2;
          overflow: hidden;
          text-overflow: ellipsis;
          line-height: 1.6;
          max-height: 3.2em;
        }

        a {
          color: black;
          font-size: 18px;
          font-weight: 600;
        }

        .iconfont {
          font-size: 14px;
        }
      }

    }

    img {
      width: 100%;
      border-radius: 8px 8px 0 0;
    }
  }
}
</style>

<style lang="scss" scoped>
/* 手机端样式 */
@media only screen and (max-width: 768px) {

  /* 深度选择器穿透scoped */
  :deep(.article-top-category) {
    position: relative;
    top: 39px;
    margin-bottom: 40px;
  }

  :deep(.wu-home-right) {
    display: none;
  }

  .wu-wrap {
    padding-top: 50px !important;
    padding-bottom: 0;
  }

  .wu-content {
    padding: 0;
  }

  #pagination {
    display: none;
  }
}

.love-container {
  text-align: center;

  .love-titile {
    font-size: 19px;
    font-weight: 600;
    letter-spacing: 0.2rem;
    // line-height: 4rem;

    background-image: linear-gradient(270deg,
        #ff4500,
        orange,
        gold,
        #90ee90,
        #0ff,
        #1e90ff,
        #9370db,
        #ff69b4,
        #ff4500);
    -webkit-background-clip: text;
    -webkit-animation: jianBian 60s linear infinite;
    animation: jianBian 60s linear infinite;
    color: transparent;
  }

  .love-time {
    height: 100%;
    font-size: 12px;

    .timer-item {
      font-size: 14px;
    }
  }
}
</style>
