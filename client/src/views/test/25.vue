<template>
  <div ref="container" class="container">
    <!-- 热力图容器 -->
    <div ref="heatmap" />
    <!-- 在 <template> 里放一个绝对定位的提示框 -->
    <div ref="tooltip" style="position:absolute;pointer-events:none;display:none;
            background:#333;color:#fff;padding:4px 6px;border-radius:3px;
            font-size:12px;white-space:pre-line"
    />
  </div>
</template>

<script>
import * as d3 from 'd3'

export default {
  data() {
    return {
      dataset: [],
      width: 1492,
      height: 180,
      margin: 30,
      weekBoxWidth: 20,
      monthBoxHeight: 20
    }
  },
  mounted() {
    this.requestData()
  },

  methods: {
    requestData() {
      this.dataset = this.generateDataset(12, {
        // 定义要高亮显示的日期数据
        fill: {
          '2025-01-03': 80,
          '2025-01-04': 2,
          '2025-02-07': 1,
          '2025-03-12': 1,
          '2025-03-13': 1,
          '2025-04-23': 2,
          '2025-04-24': 1,
          '2025-04-25': 1,
          '2025-04-26': 1,
          '2025-04-27': 3,
          '2025-04-28': 1,
          '2025-04-29': 1,
          '2025-04-30': 5
        }
      })
      this.createHeatMap()
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
    createHeatMap() {
      console.log(this.$refs.heatmap)

      const svg = d3.select(this.$refs.heatmap).append('svg').attr('width', this.width).attr('height', this.height)
      // 绘制月份坐标
      const monthBox = svg.append('g').attr('transform', 'translate(' + (this.margin + this.weekBoxWidth) + ', ' + this.margin + ')')
      // 设置周坐标数据
      const weeks = ['一', '三', '五', '日']
      // 绘制周坐标
      const weekBox = svg.append('g').attr(
        'transform',
        'translate(' + (this.margin - 10) + ', ' + (this.margin + this.monthBoxHeight) + ')')
      const weekScale = d3.scaleLinear()
        .domain([0, weeks.length])
        .range([0, this.height - this.margin - this.monthBoxHeight + 14])
      weekBox.selectAll('text').data(weeks).enter().append('text').text(v => { return v }).attr('font-size', '0.85em').attr('fill', '#CCC').attr('y', (v, i) => { return weekScale(i) })
      const monthScale = d3.scaleLinear().domain([0, this.dataset.months.length]).range([0, this.width - this.margin - this.weekBoxWidth + 10])
      monthBox.selectAll('text').data(this.dataset.months).enter()
        .append('text')
        .text(v => { return v })
        .attr('font-size', '0.9em')
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
        .attr('fill', v => {
          if (v.total === undefined) {
            return '#EFEFEF'
          }
          if (v.total < 1) {
            return '#F96'
          }
          return '#FC9'
        })
        .attr('x', (v, i) => {
          if (i % 7 === 0) {
            cellCol++
          }
          var x = (cellCol - 1) * cellSize
          return cellCol > 1 ? x + cellMargin * (cellCol - 1) : x
        }).attr('y', (v, i) => {
          var y = i % 7
          return y > 0 ? y * cellSize + cellMargin * y : y * cellSize
        })
      const tooltip = d3.select(this.$refs.tooltip)
      cell.on('mouseenter', function (event, d) {
        const msg = d.total ? `有 ${d.total} 篇内容` : '没有内容'
        tooltip
          .style('display', 'block')
          .html(`${d.date}<br>${msg}`)
      }).on('mousemove', function (event) {
        // 让提示框跟着鼠标走
        tooltip
          .style('left', (event.pageX + 10) + 'px')
          .style('top', (event.pageY + 10) + 'px')
      }).on('mouseleave', function () {
        tooltip.style('display', 'none')
      })
    }
  }
}
</script>

<style scoped></style>
