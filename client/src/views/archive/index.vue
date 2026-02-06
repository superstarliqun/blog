<template>
  <div class="wu-wrap">
    <div class="archive-container">
      <!-- 热力图容器 -->
      <div class="heatmap">
        <div ref="heatmap" class="heatmap-container" />
        <div ref="tooltip" class="heatmap-tooltip" />
        <div class="heatmap-years">
          <ul>
            <li v-for="(item, index) in yearOption" :key="index" :class="currentYear == item ? 'active' : ''"
              @click="queryHeatMapYear(item)">{{ item }}</li>
          </ul>
        </div>

      </div>
      <!-- 时间轴视图 -->
      <div class="timeline-container">
        <div class="year-section">
          <div class="timeline-content">
            <div v-for="(item, index) in treeList" :key="index" class="month-group">
              <h3 class="month-title">{{ item.month }} 月</h3>
              <div class="articles-list">
                <article v-for="(item2, index2) in item.data" :key="index2" class="article-item"
                  @click="goToArticle(item2.id)">
                  <div class="article-date">
                    <span class="day">{{ new Date(item2.createTime.replace(/-/g, '/')).getDate() }}</span>
                    <span class="weekday">{{ getWeekday(item2.createTime) }}</span>
                  </div>
                  <div class="article-content">
                    <h4 class="article-title">{{ item2.title }}</h4>
                    <p class="article-excerpt">{{ item2.summary == null ? '作者很懒没有生成各种摘要' : item2.summary }}</p>
                    <div class="article-meta">
                      <span v-for="(item3, index3) in item2.tagsList" :key="index3" class="meta-item">
                        <el-tag size="mini">tag#{{ item3 }}</el-tag>
                      </span>
                      <span class="meta-item"><i class="icon-word" /> {{ countMdText(item2.content) }} 字</span>
                    </div>
                  </div>
                </article>
              </div>
            </div>
            <div v-if="treeList.length == 0 || treeList == null">
              <el-skeleton :rows="6" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import * as d3 from 'd3'
export default {
  name: 'ArchivePage',
  data() {
    return {
      currentYear: new Date().getFullYear(),
      currentDate: null,
      yearOption: [2026, 2025, 2024, 2023], // 热力图右侧年份集合
      articles: [],
      endDate: new Date(), // 今天
      dataset: [],
      width: 1000,
      height: 180,
      margin: 30,
      weekBoxWidth: 20,
      monthBoxHeight: 20,
      treeList: []
    }
  },
  computed: {
    years() {
      const years = [...new Set(this.articles.map(article => new Date(article.date).getFullYear()))]
      return years.sort((a, b) => b - a)
    }
  },
  mounted() {
    this.requestData()
    this.loadArticles()
    this.handleScroll()
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    queryHeatMapYear(year) {
      this.currentYear = year
      this.requestData()
      this.loadArticles()
    },
    // 初始化数据
    requestData() {
      this.$get(this.$urls.dailyCounts, { searchYear: this.currentYear }).then((res) => {
        const fill = res.data.reduce((acc, { day, count }) => {
          acc[day] = count
          return acc
        }, {})
        this.dataset = this.generateDataset(12, {
          fill: fill,
          endDate: this.currentYear + '-12-21'
        })
        this.createHeatMap()
      })
    },
    // 加载文章数据
    async loadArticles() {
      this.$get(this.$urls.queryArchiveList, { searchYear: this.currentYear, searchDate: this.currentDate }).then((res) => {
        // 1. 按月份分组
        const group = {}
        res.data.forEach(item => {
          const m = new Date(item.createTime).getMonth() + 1 // 1-12
            ; (group[m] || (group[m] = [])).push(item) // 丢进对应月份
        })

        // 2. 转成形如 [{ month: 1, data: [...] }, ...] 的数组
        const topList = Object.keys(group)
          .map(Number)
          .sort((a, b) => a - b)
          .map(month => ({ month, data: group[month] }))
        this.treeList = topList
      })
    },
    // 生成模拟数据
    generateMockArticles() {
      const articles = []
      const titles = [
        'Vue3 组合式API最佳实践',
        '前端性能优化实战指南',
        'CSS Grid布局完全指南',
        'JavaScript异步编程详解',
        'React Hooks深入浅出',
        'Node.js微服务架构设计',
        'TypeScript高级类型系统',
        'Webpack5配置优化技巧',
        '前端安全防护策略',
        '移动端适配方案总结',
        'Git工作流最佳实践',
        '设计模式在前端的应用',
        '浏览器渲染原理剖析',
        '前端监控体系建设',
        '小程序开发经验分享',
        '跨平台开发框架对比',
        '前端测试策略与实践',
        '代码规范与质量保障',
        '团队技术分享总结',
        '个人项目开发心得'
      ]

      const categories = ['前端开发', '技术分享', '项目总结', '学习笔记', '工具推荐']
      const currentDate = new Date()
      for (let i = 0; i < 50; i++) {
        const date = new Date(currentDate.getTime() - Math.random() * 365 * 24 * 3600 * 1000)
        const category = categories[Math.floor(Math.random() * categories.length)]

        articles.push({
          id: i + 1,
          title: titles[i % titles.length],
          excerpt: '这是一篇技术文章的摘要内容，概括了文章的主要观点和核心知识点，帮助读者快速了解文章内容...',
          date: date,
          category: category,
          comments: Math.floor(Math.random() * 50),
          words: Math.floor(Math.random() * 3000) + 500,
          tags: ['Vue', 'JavaScript', '前端']
        })
      }
      return articles.sort((a, b) => new Date(b.date) - new Date(a.date))
    },
    // 按年份获取月份
    getMonthsByYear(year) {
      const months = this.articles
        .filter(article => new Date(article.date).getFullYear() === year)
        .map(article => new Date(article.date).getMonth() + 1)
      return [...new Set(months)].sort((a, b) => b - a)
    },
    // 按月份获取文章
    getArticlesByMonth(year, month) {
      return this.articles.filter(article => {
        const date = new Date(article.date)
        return date.getFullYear() === year && date.getMonth() + 1 === month
      })
    },
    // 处理滚动事件
    handleScroll() {
      // 更新当前年份
      for (const year of this.years) {
        const ref = this.$refs[`year-${year}`]
        if (!ref || !ref[0]) continue // ← 加这一行
        const rect = ref[0].getBoundingClientRect()
        if (rect.top <= 100) this.currentYear = year
      }
    },
    // 跳转到文章详情
    goToArticle(id) {
      this.$router.push({
        name: 'postViewer',
        params: { id: id }
      })
    },
    // 获取日期
    getDay(date) {
      return new Date(date).getDate()
    },
    // 获取星期
    getWeekday(date) {
      const weekdays = ['日', '一', '二', '三', '四', '五', '六']
      return '周' + weekdays[new Date(date).getDay()]
    },
    generateDataset(forwardMonth, options) {
      const config = Object.assign({}, {
        endDate: null,
        fill: {}
      }, options)

      const months = []
      const days = []

      // 计算需要填充的日期
      for (let i = forwardMonth; i > 0; i--) {
        const referDate = config.endDate
          ? new Date(config.endDate)
          : new Date()

        referDate.setMonth(referDate.getMonth() - i + 2)
        referDate.setDate(0)

        let month = referDate.getMonth() + 1
        month = month < 10 ? '0' + month : month

        for (let d = 1; d <= referDate.getDate(); d++) {
          const day = d < 10 ? '0' + d : d
          const data = {
            date: referDate.getFullYear() + '-' + month + '-' + day
          }

          // eslint-disable-next-line no-prototype-builtins
          if (config.fill.hasOwnProperty(data.date)) {
            data.total = config.fill[data.date]
          }
          days.push(data)
        }
        months.push(referDate.getFullYear() + '-' + month)
      }
      // 确保第一个日期是从星期一开始
      // 不是星期一就向前追加相应的天数
      const firstDate = days[0].date
      const d = new Date(firstDate)
      let day = d.getDay()
      if (day === 0) {
        day = 7
      }
      for (let i = 1; i < day; i++) {
        const d = new Date(firstDate)
        d.setDate(d.getDate() - i)
        const v = [d.getFullYear(), d.getMonth() + 1, d.getDate()]
        if (v[1] < 10) {
          v[1] = '0' + v[1]
        }
        if (v[2] < 10) {
          v[2] = '0' + v[2]
        }
        days.unshift({ date: v.join('-') })
      }
      return { days: days, months: months }
    },
    countMdText(md, mode = 'char') {
      // 1. 删掉代码块（```xxx``` 和 `xxx`）
      let text = md.replace(/```[\s\S]*?```/g, '')
        .replace(/`[^`]*`/g, '')

      // 2. 删掉图片  ![alt](url)
      text = text.replace(/!\[[^\]]*\]\([^)]*\)/g, '')

      // 3. 删掉链接  [text](url)  只保留 text
      text = text.replace(/\[([^\]]+)\]\([^)]*\)/g, '$1')

      // 4. 去掉标题符号、列表符号、粗体斜体、多余空白
      text = text.replace(/^[#*+-]\s+/gm, '') // 标题、无序列表
        .replace(/^\d+\.\s+/gm, '') // 有序列表
        .replace(/[*_]{1,2}([^*_]+)[*_]{1,2}/g, '$1') // 粗体斜体
        .replace(/\s+/g, ' ')
        .trim()

      // 5. 如还有 HTML 标签，一并剥掉
      text = text.replace(/<[^>]+>/g, '')

      // 6. 统计
      if (mode === 'char') {
        return [...text].length // 含中文、标点、数字
      }
      // word 模式：按空格分词
      return text ? text.split(/\s+/).filter(Boolean).length : 0
    },
    // 生成热力图svg对象
    createHeatMap() {
      d3.select(this.$refs.heatmap).select('svg').remove()
      const svg = d3.select(this.$refs.heatmap).append('svg').attr('width', this.width).attr('height', this.height)
      // 绘制月份坐标
      const monthBox = svg.append('g').attr('transform', 'translate(' + (this.margin + this.weekBoxWidth) + ', ' + this.margin + ')')
      // 设置周坐标数据
      const weeks = ['一', '三', '五', '日']
      // 绘制周坐标
      const weekBox = svg.append('g').attr('transform', 'translate(' + (this.margin - 10) + ', ' + (this.margin + this.monthBoxHeight) + ')')
      const weekScale = d3.scaleLinear().domain([0, weeks.length]).range([0, this.height - this.margin - this.monthBoxHeight + 14])
      weekBox.selectAll('text').data(weeks).enter().append('text').text(v => { return v }).attr('font-size', '0.85em').attr('fill', '#CCC').attr('y', (v, i) => { return weekScale(i) })
      const monthScale = d3.scaleLinear().domain([0, this.dataset.months.length]).range([0, this.width - this.margin - this.weekBoxWidth])
      monthBox.selectAll('text').data(this.dataset.months).enter()
        .append('text')
        .text(v => { return v })
        .attr('font-size', '14px')
        .attr('font-family', 'monospace')
        .attr('fill', '#999')
        .attr('x', (v, i) => {
          return monthScale(i)
        })
      const cellBox = svg.append('g').attr('transform', 'translate(' + (this.margin + this.weekBoxWidth) + ', ' + (this.margin + 10) + ')')
      const cellMargin = 3
      // 计算方块大小
      const cellSize = (this.height - this.margin - this.monthBoxHeight - cellMargin * 6 - 10) / 7
      // 方块列计数器
      var cellCol = 0
      var cell = cellBox.selectAll('rect').data(this.dataset.days).enter()
        .append('rect')
        .attr('width', cellSize)
        .attr('height', cellSize)
        .attr('rx', 3)
        .attr('cursor', 'pointer')
        .attr('fill', v => {
          if (v.total === undefined) return '#EFEFEF'
          if (v.total <= 1) return '#9be9a8'
          if (v.total <= 2) return '#40c463'
          if (v.total <= 3) return '#30a14e'
          return '#216e39'
        })
        .attr('x', (v, i) => {
          if (i % 7 === 0) { cellCol++ }
          var x = (cellCol - 1) * cellSize
          return cellCol > 1 ? x + cellMargin * (cellCol - 1) : x
        }).attr('y', (v, i) => {
          var y = i % 7
          return y > 0 ? y * cellSize + cellMargin * y : y * cellSize
        })
      const tooltip = d3.select(this.$refs.tooltip)
      cell.on('mouseenter', function (event, d) {
        const msg = d.total ? `有 ${d.total} 篇内容` : '没有内容'
        tooltip.style('display', 'block').html(`${d.date}<br>${msg}`)
      }).on('mousemove', function (event) {
        tooltip.style('left', (event.pageX + 10) + 'px').style('top', (event.pageY + 10) + 'px')
      }).on('mouseleave', function () {
        tooltip.style('display', 'none')
      }).on('click', (e, d) => this.onCellClick(d))
    },
    onCellClick(d) {
      this.currentDate = d.date
      this.loadArticles()
    }
  }
}
</script>

<style lang="scss" scoped>
.wu-wrap {
  background-color: #f7f8fa;
}

.archive-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 14px 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.heatmap {
  display: flex;

  .heatmap-container {
    display: table-caption;
    border: 1px solid #ececec;
    border-radius: 8px;
    background-color: #fff;
    padding-right: 16px;
  }

  .heatmap-tooltip {
    position: absolute;
    pointer-events: none;
    display: none;
    background: #333;
    color: #fff;
    padding: 4px 6px;
    border-radius: 3px;
    font-size: 12px;
    white-space: pre-line
  }
}

/* 时间轴容器 */
.timeline-container {
  background: #f7f8fa;
  background-color: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.year-section {
  margin-bottom: 50px;
}

.month-group {
  margin-bottom: 40px;
}

.month-title {
  font-size: 20px;
  font-weight: 500;
  color: #409EFF;
  margin: 0 0 20px 0;
}

.articles-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-item {
  display: flex;
  gap: 20px;
  padding: 20px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.article-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 60px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 6px;
}

.day {
  font-size: 24px;
  font-weight: 600;
  color: #409EFF;
}

.weekday {
  font-size: 12px;
  color: #95a5a6;
}

.article-content {
  flex: 1;
}

.article-title {
  font-size: 18px;
  font-weight: 500;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.article-excerpt {
  color: #7f8c8d;
  font-size: 14px;
  line-height: 1.5;
  margin: 0 0 12px 0;
}

.article-meta {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #95a5a6;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 回到顶部按钮 */
.back-to-top {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  background: #409EFF;
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.back-to-top:hover {
  background: #66b1ff;
  transform: translateY(-2px);
}

/* 图标样式 */
.icon-category::before {
  content: "📁";
}

.icon-comment::before {
  content: "💬";
}

.icon-word::before {
  content: "📝";
}

.heatmap-years {
  width: 100%;

  ul {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    // gap: 8px;

    li {
      // background-color: #0969DA;
      padding: 8px 16px;
      cursor: pointer;
      width: 120px;
      border-radius: 4px;
      text-align: center;
    }

    li:hover {
      background-color: #f5f7f9;
      padding: 8px 16px;
      cursor: pointer;
      width: 120px;
      border-radius: 4px;
      text-align: center;
    }

    li.active {
      background-color: #0969DA;
      color: #FFFFFF;
      font-weight: 600;
    }
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .wu-wrap {
    padding-top: 10px;
    padding-bottom: 10px
  }

  .archive-container {
    margin-top: 50px;
    padding: 0 10px;
  }

  #blog-calendar {
    max-width: 100%;
    margin: 10px;
  }

  .day-cell {
    padding: 8px 4px;
    font-size: 12px;
    min-height: 35px;
  }

  .heatmap {
    display: none;
  }

  .timeline-nav,
  .timeline-container,

  .article-item {
    flex-direction: column;
    gap: 15px;
  }

  .article-date {
    flex-direction: row;
    gap: 10px;
    min-width: auto;
  }

  .year-list {
    gap: 8px;
  }

}
</style>
