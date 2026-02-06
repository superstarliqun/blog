<template>
  <div>
    <ul>
      <router-link to="/faq/page1">
        <li :class="{ 'selected': index === '1' }">测试1</li>
      </router-link>
      <router-link to="/faq/page2">
        <li :class="{ 'selected': index === '2' }">测试2</li>
      </router-link>
      <router-link to="/faq/page3">
        <li :class="{ 'selected': index === '3' }">测试3</li>
      </router-link>
      <router-link to="/faq/page4">
        <li :class="{ 'selected': index === '4' }">测试4</li>
      </router-link>
      <router-link to="/faq/page5">
        <li :class="{ 'selected': index === '5' }">测试5</li>
      </router-link>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    index: {
      // eslint-disable-next-line vue/require-prop-type-constructor
      type: Number | String,
      default: null
    }
  },
  data() {
    return {
      data: null
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

<style>
.selected {
  color: yellow;
}
</style>
