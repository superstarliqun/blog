<template>
  <div class="wu-wrap">
    <div class="wu-content">
      <div class="wu-home-body">
        <ul v-if="total > 0">
          <li v-for="item in 10" :key="item" class="article-item" @click="toDetails">
            <div class="article-item-inner">
              <div class="article-item-title">【Spring专栏】基于WebListener实现实时在线人数统计</div>
              <div class="article-item-media">这篇文章之前只有星球用户才能看哦，你看到了就是赚了。很多同学对技术派项目非常感兴趣，目前技术派教程已经更新了三十篇，今天就给大家讲讲技术派的架构设计，让你对该项目有一个整体的认识。</div>
              <div class="article-item-infos">
                <span>楼仔 · </span>
                <span> 2023年03月22日 · </span>
                <span> 07:31 · </span>
                <span> 观看583 · </span>
                <span> 回复 0 · </span>
                <span> 点赞4</span>
              </div>
            </div>
          </li>
        </ul>
        <div v-else class="source">
          <el-skeleton />
        </div>
      </div>
      <div class="wu-home-right">
        <div class="my-card">
          <span class="care-title">关于系统</span>
          <div class="my-card-context">
            <div class="my-card-title">
              <p>系统迭代更新v1.0版本正式上线</p>
            </div>
            <div class="poster">
              <div class="panel-subhd">
                <h3 class="panel-subtitle">官方通知栏</h3>
              </div>
            </div>
            <img style="width: 100%;" src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif" alt="">
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  methods: {
    toDetails() {
      const routeData = this.$router.resolve({ path: '/details' })
      window.open(routeData.href, '_blank')
    }
  }
}
</script>

<style lang="scss" scoped>
.wu-wrap {
  background-color: #f7f8fa;
  min-height: calc(100% - 90px);
  padding-top: 20px;
}
.wu-home-body {
  flex: 1;
  margin-right: 20px;
  background-color: #fff;
  padding: 0 20px;
  margin-bottom: 20px;
}
.wu-home-right {
  width: 24%;
  border-radius: 20px;
  position: relative;
}
// width小于960px,有侧边栏消失
@media screen and (max-width: 960px) {
  .wu-home-right {
    display: none;
  }
}
// 正文
.article-item {
  cursor: pointer;
  border-bottom: 1px solid #d1dddb;
  .article-item-inner {
    margin: 12px;
    display: flex;
    flex-direction: column;
    .article-item-title {
      height: 40px;
      display: flex;
      align-items: center;
    }
    .article-item-media {
      margin: 8px 0;
    }
    .article-item-infos {
      height: 40px;
      display: flex;
      align-items: center;
    }
  }
}
ul {
  li {
    list-style-type: none;
  }
}
// 侧边
.my-card {
  background-color: #fff;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px 0 rgb(3 27 78 / 6%);
  border-radius: 2px;
  .care-title {
    width: 100%;
    height: 42px;
    display: flex;
    align-items: center;
    background: linear-gradient(to left, #e3f0ee, #1d6a96);
    padding: 0 20px;
    color: #fff;
    font-weight: bold;
  }
  .my-card-context {
    padding: 20px;
    font-size: 14px;
    .marginbottom8 {
      margin-bottom: 8px;
    }
  }
  .poster {
    background-color: #fff;
    border-radius: 4px;
    .panel-subhd {
      position: relative;
      margin-top: 20px;
      margin-bottom: 12px;
      line-height: 20px;
      color: var(--pai-color-999-gray);
    }
    .panel-subhd:before {
      content: "";
      position: absolute;
      left: 0;
      top: 50%;
      width: 100%;
      height: 1px;
      background-color: #e5e5e5;
    }
    .panel-subtitle {
      font-size: 12px;
      position: relative;
      display: inline-block;
      box-sizing: border-box;
      max-width: 100%;
      font-weight: 400;
      padding-right: 8px;
      color: #999;
      background-color: #fff;
    }
  }
}
</style>
