<template>
  <div class="con">
    <el-timeline>
      <el-timeline-item timestamp="2018/4/12" placement="top">
        <el-card>
          <h4>更新 Github 模板</h4>
          <p>王小虎 提交于 2018/4/12 20:46</p>
        </el-card>
      </el-timeline-item>
      <el-timeline-item timestamp="2018/4/3" placement="top">
        <el-card>
          <h4>更新 Github 模板</h4>
          <p>王小虎 提交于 2018/4/3 20:46</p>
        </el-card>
      </el-timeline-item>
      <el-timeline-item timestamp="2018/4/2" placement="top">
        <el-card>
          <h4>更新 Github 模板</h4>
          <p>王小虎 提交于 2018/4/2 20:46</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>

</template>

<script>

export default {
  data() {
    return {
      reverse: false,
      activities: [
        { content: '超级消息！SUPERSTAR 1.0版本正式发布！', timestamp: '2024-04-10' },
        { content: '超级消息！SUPERSTAR 2.0版本正式发布！', timestamp: '2025-03-21' }

      ]
    }
  },
  methods: {

  }
}
</script>

<style lang="scss" scoped>
.con {
  padding: 100px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  // height: calc(100vh - 389px);
}
</style>
