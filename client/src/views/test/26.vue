<template>
  <div ref="heatmap" class="heatmap-container" />
</template>

<script>
import * as d3 from 'd3'

export default {
  name: 'GithubHeatmap',
  props: {
    data: { type: Array, required: true }
  },
  mounted() {
    this.renderHeatmap()
  },
  methods: {
    renderHeatmap() {
      const cellSize = 12
      const padding = 2
      const margin = { top: 20, right: 0, bottom: 0, left: 30 } // 预留坐标轴空间

      const svg = d3.select(this.$refs.heatmap)
        .append('svg')
        .attr('width', 850)
        .attr('height', 150)
        .append('g')
        .attr('transform', `translate(${margin.left}, ${margin.top})`)

      // 1. 颜色比例尺
      const colorScale = d3.scaleThreshold()
        .domain([1, 10, 20, 30])
        .range(['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39'])

      // 2. 绘制纵坐标 (星期)
      const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
      svg.selectAll('.dayLabel')
        .data(days)
        .enter()
        .append('text')
        .filter((d, i) => i % 2 !== 0) // 只显示单数日，防止拥挤
        .text(d => d)
        .attr('x', -5)
        .attr('y', (d, i) => i * (cellSize + padding) + 10)
        .style('text-anchor', 'end')
        .style('font-size', '10px')
        .style('fill', '#767676')

      // 3. 绘制横坐标 (月份)
      // 逻辑：找到每月中第一周的 index
      const months = d3.timeMonths(
        new Date(this.data[0].date),
        new Date(this.data[this.data.length - 1].date)
      )

      svg.selectAll('.monthLabel')
        .data(months)
        .enter()
        .append('text')
        .text(d => d3.timeFormat('%b')(d)) // 格式化为 Jan, Feb...
        .attr('x', d => {
          // 计算该月份第一天在数组中的索引，从而确定 X 位置
          const dayIndex = d3.timeDay.count(new Date(this.data[0].date), d)
          return Math.floor(dayIndex / 7) * (cellSize + padding)
        })
        .attr('y', -5)
        .style('font-size', '10px')
        .style('fill', '#767676')

      // 4. 绘制方块 (Rects)
      svg.selectAll('rect')
        .data(this.data)
        .enter()
        .append('rect')
        .attr('width', cellSize)
        .attr('height', cellSize)
        .attr('x', (d, i) => Math.floor(i / 7) * (cellSize + padding))
        .attr('y', (d, i) => (i % 7) * (cellSize + padding))
        .attr('fill', d => colorScale(d.count))
        .attr('rx', 2)
    }
  }
}
</script>
