<template>
  <div class="bottom-content">
    <div class="bottom-head">
      <div class="js-left">
        <span>灵光一刻 | SUPERSTAR</span>
        <span>向明月</span>
        <span class="c-position">后端 - Java</span>
        <div class="icon-logo">
          <a href="#" target="_blank"><i class="iconfont icon-footer-qq" /></a>
          <a href="https://weibo.com/u/6881639976" target="_blank"><i class="iconfont icon-footer-weibo" /></a>
          <a href="#" target="_blank"><i class="iconfont icon-footer-weixin" /></a>
          <a href="https://blog.csdn.net/liqun_super" target="_blank"><i class="iconfont icon-footer-csdn" /></a>
        </div>
      </div>
      <div class="js-list">
        <div class="js-title">统计信息</div>
        <div class="js-item"><a href="#" target="_blank">访问量</a></div>
        <div class="js-item"><a href="#" target="_blank">热门文章</a></div>
        <div class="js-item"><a href="/version-history" target="_blank">版本历史</a></div>
      </div>
      <div class="js-list">
        <div class="js-title">资源链接</div>
        <div class="js-item"><a href="#" target="_blank">推荐书籍</a></div>
        <div class="js-item"><a href="#" target="_blank">推荐网站</a></div>
        <div class="js-item"><a href="#" target="_blank">推荐工具</a></div>
      </div>
      <div class="js-list">
        <div class="js-title">订阅服务</div>
        <div class="js-item"><a href="#" target="_blank">RSS订阅</a></div>
        <div class="js-item"><a href="#" target="_blank">邮件订阅</a></div>
      </div>
    </div>
    <div class="bottom-footer">
      <span class="foot">©2022-2026</span>
      <span class="beian"><img src="/beian-icon.png"> <a href="https://beian.miit.gov.cn/">
          辽ICP备2021011784号-1</a></span>
      <span class="time">
        <div class="status-light" />博客运行正常
      </span>
    </div>
  </div>
</template>

<script>
export default {
  methods: {

  }
}
</script>
<style lang="scss" scoped>
.bottom-content {
  background: #1d2328;
  font-size: 12px;

  .bottom-head {
    display: none;
    justify-content: space-evenly;
    color: #999aaa;
    font-size: 16px;
    padding: 24px 0;

    .js-left {
      span {
        display: block;
        line-height: 38px;
      }

      .iconfont {
        line-height: 37px;
        font-size: 25px;
        background-color: #fff;
        margin: 5px;
        padding: 6px;
        border-radius: 50%;
        box-sizing: border-box;
        cursor: pointer;
      }

      .c-position {
        color: #935936;
        font-size: 18px;
      }

      .icon-logo {
        line-height: 90px;
      }
    }

    .js-list {
      .js-title {
        font-size: 18px;
        color: white;
        margin-bottom: 12px;
      }

      .js-item {
        font-size: 16px;
        line-height: 32px;
      }
    }
  }

  .bottom-footer {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 45px;
    color: #ddd;
    background-color: #161c1f;
    gap: 28px;

    .beian {
      display: flex;
      align-items: center;
      gap: 4px;

      img {
        width: 12px;
        height: 13px;
      }
    }

    .foot {}

    .time {
      display: flex;
      align-items: center;
      margin: 0px;
      gap: 3px;

      .status-light {
        width: 14px;
        height: 14px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #57bd6a56;

        &::after {
          content: "";
          width: 8px;
          height: 8px;
          border-radius: 50%;
          background: #57bd6a;
        }
      }
    }
  }

  // 去除a标签的下划线样式
  a {
    text-decoration: none;
    color: inherit;
    background-color: transparent;
    padding: 0;
  }
}

@media only screen and (max-width: 768px) {
  .main-header .wu-wrap {
    padding-top: 50px !important;
  }

  .bottom-head {
    display: none !important;
  }
}
</style>
