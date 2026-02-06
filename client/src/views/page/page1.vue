<template>
  <div>
    <button @click="goto">goto1111</button>
    <div>下面的内容</div>
  </div>
</template>

<script>
// import common from './common'
export default {
  // components: {
  //   common
  // },
  data() {
    return {

    }
  },
  created() {
    this.requestData()
  },
  methods: {
    goto() {
      this.$router.push('/faq/page2')
    }
  }
}
</script>

<style lang="scss" scoped></style>
