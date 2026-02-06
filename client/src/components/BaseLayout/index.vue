<template>
  <div class="baseLayout">
    <HeaderComponent v-if="!$route.meta.hideHeader" />
    <transition name="fade" mode="out-in">
      <router-view style="flex: 1;" />
    </transition>
    <FooterComponent v-if="!$route.meta.hideFooter" />
  </div>
</template>

<script>
import HeaderComponent from '@/components/Header'
import FooterComponent from '@/components/Bottom'
export default {
  components: {
    HeaderComponent: HeaderComponent,
    FooterComponent: FooterComponent
  }
}
</script>

<style lang="scss" scoped>
.baseLayout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;

  .main-content {
    height: 100%;
  }
}
</style>
