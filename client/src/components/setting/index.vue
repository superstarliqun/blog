<template>
  <el-dialog title="博客中控台" :visible.sync="dialogVisible" width="30%" :before-close="handleClose" append-to-body
    :modal-append-to-body="true">
    <p>字体设置</p>
    <el-radio-group v-model="currentFont" @change="handleFontChange">
      <el-radio label="default">系统默认</el-radio>
      <el-radio label="MingYue">自研字体</el-radio>
      <el-radio label="PingFang">苹方字体、微软雅黑字体</el-radio>
    </el-radio-group>

    <p>主题设置</p>
    <el-radio-group v-model="currentFont" @change="handleFontChange">
      <el-radio label="default">浅色模式</el-radio>
      <el-radio label="MingYue">深色模式</el-radio>
    </el-radio-group>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      currentFont: null
    }
  },
  created() { },
  methods: {
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then((_) => {
          done()
        })
        .catch((_) => { })
    },
    handleFontChange(val) {
      if (val === 'default') {
        this.$store.dispatch('app/setFontTheme', 'default')
      } else if (val === 'MingYue') {
        this.$store.dispatch('app/setFontTheme', 'MingYue')
      } else {
        this.$store.dispatch('app/setFontTheme', 'PingFang')
      }
    }
  }
}
</script>

<style></style>
