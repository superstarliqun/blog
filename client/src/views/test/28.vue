<template>
  <div class="font-settings">
    <span>字体设置</span>
    <el-radio-group v-model="currentFont" @change="handleFontChange">
      <el-radio label="default">系统默认</el-radio>
      <el-radio label="serif">书卷气息 (衬线)</el-radio>
      <el-radio label="mono">极客模式 (等宽)</el-radio>
    </el-radio-group>
  </div>
</template>

<script>
export default {
  name: 'FontSettings',
  data() {
    return {
      currentFont: null
    }
  },
  methods: {
    handleFontChange(val) {
      if (val === 'serif') {
        console.log('切换到书卷气息')

        this.$store.dispatch('app/setFontTheme', 'MingYue')
      } else if (val === 'mono') {
        console.log('切换到极客模式')
        this.$store.dispatch('app/setFontTheme', 'mono')
      } else {
        this.$store.dispatch('app/setFontTheme', 'default')
      }
      this.$message.success(`字体已切换`)
    }
  }
}
</script>
