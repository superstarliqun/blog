<template>
  <div class="content">
    <plugin :value="data"></plugin>
  </div>
</template>

<script>
import plugin from './components/progress-plugin'
export default {
  components: {
    plugin
  },
  data() {
    return {
      data: 0
    }
  },
  created() {
    this.requestData()
  },
  methods: {
    requestData() { }
  }

}
</script>

<style lang="scss" scoped>
.content {
  margin: 50px;
  height: 1000px;
}
</style>
