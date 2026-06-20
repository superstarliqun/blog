<template>
  <div>
    <common :index="'2'" />
    2
  </div>
</template>

<script>
import common from './common'
export default {
  components: {
    common
  },
  data() {
    return {

    }
  },
  created() {
    this.requestData()
  },
  methods: {
    requestData() {
    }

  }
}
</script>

<style lang="scss" scoped>
</style>
