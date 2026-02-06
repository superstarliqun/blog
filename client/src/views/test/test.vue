<template>
  <div class="wrap">
    <span :class="index == 1 ? 'active' : ''" @click="handle(1)">分类1</span>
    <span :class="index == 2 ? 'active' : ''" @click="handle(2)">分类2</span>
  </div>
</template>

<script>
export default {
  data() {
    return {
      data: null,
      index: ''
    }
  },
  methods: {
    handle(index) {
      this.index = index
    }
  }
}
</script>

<style lang="scss">
.wrap {
  width: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
  gap: 10px;

  span {

    padding: 2px 8px;
    border-radius: 4px;
    border: 1px solid rgb(221, 221, 221);
    cursor: pointer;
  }

  span.active {
    background-color: greenyellow;
  }
}
</style>
