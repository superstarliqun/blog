<template>
  <div class="main-header">
    <header class="header-wrapper">
      <!-- 新增汉堡按钮 -->
      <div v-show="!isMenuOpen" class="hamburger" @click="toggleMenu">
        <i class="iconfont icon-hanbaocaidan" />
      </div>
      <h1 class="font-logo">
        <span>S</span><span>U</span><span>P</span><span>E</span><span>R</span>
        <span>S</span><span>T</span><span>A</span><span>R</span>
      </h1>
      <div v-show="isMenuOpen" class="menu-mask" @click="isMenuOpen = false" />
      <ul class="navbar" :class="{ 'show-menu': isMenuOpen }">
        <li @click="isMenuOpen = false">
          <router-link :key="Date.now()" to="/" exact>首页</router-link>
        </li>
        <li @click="isMenuOpen = false">
          <router-link to="/category">分类</router-link>
        </li>
        <li @click="isMenuOpen = false">
          <router-link to="/tags">标签</router-link>
        </li>
        <li @click="isMenuOpen = false">
          <router-link to="/archive">归档</router-link>
        </li>
        <!-- <li>
          <router-link to="/feedback">留言</router-link>
        </li> -->
        <li @click="isMenuOpen = false">
          <router-link to="/link">友链</router-link>
        </li>
      </ul>
      <div class="avatar-container">
        <i class="iconfont icon-search" @click="clickSearch" />
        <el-dropdown v-if="tokenLink">
          <span class="create-center el-dropdown-link">创作者中心</span>
          <el-dropdown-menu slot="dropdown">
            <div class="dorpdown-menu">
              <div class="menu-card">
                <div class="menu-card-info">
                  <el-avatar>用户</el-avatar>
                  <div class="menu-card-name">
                    <p>故里明月</p>
                    <p>暂未认证，去认证</p>
                  </div>
                </div>
                <el-divider />
                <div class="recommend-card">
                  <img class="recommend-img" src="https://static.nowcoder.com/fe/file/oss/1660831936829GCLLO.png">
                  博客会员
                </div>
                <ul class="menu-item">
                  <li @click="toPath(1)">
                    <i class="iconfont icon-dengji" />
                    <span>去创作</span>
                  </li>
                  <li @click="toPath(2)">
                    <i class="iconfont icon-neirong" />
                    <span>文章内容</span>
                  </li>
                  <li>
                    <i class="iconfont icon-baogao" />
                    <span>草稿箱</span>
                  </li>
                  <li>
                    <i class="iconfont icon-cunchu" />
                    <span>我的收藏</span>
                  </li>
                  <li>
                    <i class="iconfont icon-pinglun" />
                    <span>我的评论</span>
                  </li>
                  <li>
                    <i class="iconfont icon-genzong" />
                    <span>订单处理</span>
                  </li>
                  <li>
                    <i class="iconfont icon-bianqian" />
                    <!-- 控制博客主页资源管理 -->
                    <span>资源便签</span>
                  </li>
                  <li @click="toPath(8)">
                    <i class="iconfont icon-chakandingdan" />
                    <span>控制面板</span>
                  </li>
                </ul>
                <div class="caozuo">
                  <span>账号设置</span>
                  <el-divider direction="vertical" />
                  <span @click="logout">退出登录</span>
                </div>
              </div>
            </div>
          </el-dropdown-menu>
        </el-dropdown>
        <div v-else class="no-login">
          <span class="toLogin" @click="toPath(1)">登录</span>
        </div>
      </div>
    </header>
    <search v-if="searchOpen" @close="searchOpen = false" />
  </div>
</template>

<script>
import { getAccessToken, removeToken } from '@/utils/auth'
import parser from '@/utils/ua-parser'
import search from '../search/index.vue'
export default {
  components: { search },
  data() {
    return {
      tokenLink: getAccessToken(),
      size: 'small',
      isHovered: false,
      keywords: undefined,
      system: undefined,
      searchOpen: false, // 全局搜索框显示状态
      isMenuOpen: false // 新增：控制移动端菜单显示隐藏
    }
  },
  mounted() {
    // 添加键盘事件监听
    window.addEventListener('keydown', this.handleKeyCombination)
  },
  beforeUnmount() {
    // 组件销毁时移除监听
    window.removeEventListener('keydown', this.handleKeyCombination)
  },
  created() {
    const parseritem = new parser.UAParser(window.navigator.userAgent)
    const res = parseritem.getResult()
    this.system = res.os.name
  },
  methods: {
    // 检查是否按下 Ctrl 或 Command 以及 K
    handleKeyCombination(event) {
      if ((event.ctrlKey || event.metaKey) && event.key === 'k') {
        this.searchOpen = !this.searchOpen
        // 阻止默认行为
        event.preventDefault()
      }
    },
    clickSearch() {
      this.searchOpen = !this.searchOpen
    },
    // 查询文章记录
    querySearchAsync(queryString, cb) {
      const target = []
      this.$get(this.$urls.elasticSearchFindHighlight, {
        keywords: queryString
      }).then((res) => {
        if (res.data.length > 0) {
          res.data.forEach((element) => {
            const param = {
              value: element.title,
              id: element.mysqlId
            }
            target.push(param)
          })
          cb(target)
        } else {
          cb([{ value: '未查询到记录' }])
        }
      })
    },
    handleSelect(item) {
      if (item.id) {
        const routeData = this.$router.resolve({
          path: '/details',
          query: { pps: item.id }
        })
        window.open(routeData.href, '_blank')
      } else {
        this.keywords = null
      }
    },
    showCard() {
      this.isHovered = true
    },
    hideCard() {
      this.isHovered = false
    },
    // 启动下拉
    openDownSelect(index) {
      if (index === 1) {
        this.drawer = true
      }
      if (index === 0) {
        setTimeout(() => {
          this.drawer = false
        }, 200)
      }
      if (index === null || index === undefined) {
        this.drawer = !this.drawer
      }
    },
    handleClose() {
      this.drawer = false
    },
    // 跳转方法
    toPath(index) {
      if (index === 1) {
        this.$get(this.$urls.getUserInfo).then((res) => {
          if (res.code === 0) {
            window.location.href = '/tool'
          } else {
            window.location.href = '/login'
          }
        })
      } else if (index === 2) {
        window.location.href = '/user/manager'
      } else if (index === 8) {
        window.location.href = '/panel'
      } else {
        window.location.href = '/login'
      }
    },
    // 退出登录
    logout() {
      removeToken()
      this.$router.push('/login')
    },
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen
    }
  }
}
</script>

<style lang="scss" scoped>
* {
  margin: 0px;
  padding: 0px;
}

// 新增：汉堡按钮样式
.hamburger {
  color: white;
  font-size: 20px;
  cursor: pointer;
  display: none; // 桌面端隐藏
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  position: absolute;
  right: 1.5rem;
  top: 50%;
  transform: translateY(-50%);

  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 4px;
  }
}

.search {
  margin-right: 40px;
}

::v-deep .search .el-input__inner {
  border-radius: 8px;
  background-color: #283b42;
  height: 34px;
  line-height: 34px;
  border: 1px solid #575757;
  color: #ffffff;
  width: 160px;
}

::v-deep .search .el-input__inner:hover {
  overflow: visible;
  white-space: normal;
}

::v-deep .el-autocomplete-suggestion .el-popper {
  width: unset !important;
}

.font-logo {
  height: 100%;
  display: flex;
  font-size: 1.3em;
  font-weight: 600;
  align-items: center;

  span:nth-child(1) {
    color: #ff6b6b;
  }

  /* 珊瑚红 */
  span:nth-child(2) {
    color: #ffd166;
  }

  /* 金黄 */
  span:nth-child(3) {
    color: #06d6a0;
  }

  /* 薄荷绿 */
  span:nth-child(4) {
    color: #118ab2;
  }

  /* 蓝绿 */
  span:nth-child(5) {
    color: #f4a261;
  }

  /* 暖橙 */
  span:nth-child(6) {
    color: #9f86c0;
  }

  /* 浅紫 */
  span:nth-child(7) {
    color: #e76f51;
  }

  /* 红橙 */
  span:nth-child(8) {
    color: #ff9f1c;
  }

  /* 橘黄 */
  span:nth-child(9) {
    color: #2ec4b6;
  }

  /* 绿蓝 */
}

// 左侧导航分类
.main-header {
  height: 48px;
  background: #283b42;
  position: relative;

  .header-wrapper {
    max-width: 1440px;
    padding: 0 1.5rem;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    margin: 0 auto;
    position: relative;

    // logo绝对定位到左侧
    .font-logo {
      position: absolute;
      left: 1.5rem;
      top: 50%;
      transform: translateY(-50%);
    }

    // 右侧头像容器绝对定位到右侧
    .avatar-container {
      height: 100%;
      position: absolute;
      right: 1.5rem;
      top: 50%;
      transform: translateY(-50%);
    }

    // ul标签选项样式
    .navbar {
      height: 100%;
      list-style-type: none;
      display: flex;
      gap: 35px;
      justify-content: center;

      li {
        float: left;
        display: flex;
        align-items: center;
        height: 100%;
        // margin-right: 48px;
        font-weight: bold;
        font-size: 15px;
      }

      .icon-sousuo {
        color: #85b8cb;
        cursor: pointer;
      }
    }

    // 右侧
    .avatar-container {
      display: flex;
      align-items: center;

      .create-center {
        display: inline-block;
        line-height: 1;
        white-space: nowrap;
        border: 1px solid #fff;
        color: #fff;
        text-align: center;
        box-sizing: border-box;
        font-weight: 500;
        padding: 10px 16px;
        font-size: 14px;
        border-radius: 4px;
        margin-left: 16px;
        cursor: pointer;
      }

      // 未登录样式
      .no-login {
        text-align: center;
        justify-content: center;
        display: flex;

        .toLogin {
          font-size: 0.8rem;
          color: white;
          margin: 0 8px;
          cursor: pointer;
        }
      }
    }
  }
}

// 卡片样式
.dorpdown-menu {
  .menu-card {
    position: absolute;
    top: -4px;
    right: 0;
    border-radius: 18px;
    z-index: 9999;
    width: 350px;
    background-color: white;
    border: 1px solid #ccc;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

    .menu-card-info {
      padding: 20px;
      display: flex;
    }

    .menu-card-name {
      margin-left: 24px;

      p:first-child {
        font-size: 18px;
        font-weight: bold;
      }

      p:last-child {
        color: #4e4e4e;
        font-size: 14px;
        margin-top: 6px;
      }
    }

    .recommend-card {
      background-size: cover;
      border-radius: 12px;
      box-sizing: border-box;
      height: 78px;
      margin-left: 24px;
      margin-top: 12px;
      padding-left: 16px;
      padding-top: 18px;
      position: relative;
      width: 288px;
    }

    .recommend-img {
      border-radius: 12px;
      height: 78px;
      left: 0;
      position: absolute;
      top: 0;
      width: 288px;
      z-index: -1;
    }

    .menu-item {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      align-content: flex-start;
      padding: 10px 30px;
      border-bottom: 1px solid #dcdfe6;

      li {
        height: 70px;
        width: 70px;
        border-radius: 8px;
        color: #555;
        text-align: center;
        display: flex;

        flex-direction: column;
        align-items: center;
        justify-content: center;
        cursor: pointer;

        i {
          margin-bottom: 16px;
          margin: 0 auto;
          font-size: 28px;
        }

        span {
          font-size: 14px;
          display: block;
          color: black;
        }
      }

      li:hover {
        border: 1px solid #b7ccfc;
        color: #425b7e;
        background-color: #f0f6ff;
      }
    }

    .caozuo {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 20px 30px 16px 30px;
      font-size: 14px;

      .el-divider {
        margin: 0 10px;
      }

      span {
        width: 100%;
        text-align: center;
        cursor: pointer;
      }

      span:hover {
        color: #425b7e;
      }
    }
  }
}

/* 消除小三角 */
.el-popper[x-placement^='bottom'] .popper__arrow {
  border: none;
}

.el-popper[x-placement^='bottom'] .popper__arrow::after {
  border: none;
}

.el-dropdown-menu {
  border: none !important;
}

// 导航选中状态
.router-link-active {
  text-decoration: none;
  color: #fff;
}

.icon-search {
  color: white;
  font-size: 18px;
  cursor: pointer;
  margin: 0 8px;
}
</style>
<style lang="scss" scoped>
/* 手机端样式 */
@media only screen and (max-width: 768px) {
  // 关键：用:deep()穿透scoped，识别动态添加的类

  :deep(.navbar.show-menu) {
    display: flex !important;
    flex-direction: column;
    gap: 0 !important;
    height: auto !important;
    padding: 10px 0;
  }

  // 移动端默认隐藏navbar
  :deep(.navbar) {
    position: absolute;
    top: 48px;
    left: 0;
    right: 0;
    background-color: #283b42;
    display: none !important; // 默认隐藏
  }

  .main-header {
    // 移动端头部固定，防止滚动时消失
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 999;
  }

  .header-wrapper {
    justify-content: space-between !important; // 汉堡按钮和头像（如果显示）分列两侧

    // 显示汉堡按钮
    .hamburger {
      display: flex !important;
    }

    .menu-mask {
      position: fixed;
      top: 48px;
      /* 头部高度 */
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.4);
      /* 蒙层半透明 */
      z-index: 5;
      /* 位于 navbar (z-index: 10) 之下 */
    }

    // 原导航栏默认隐藏，点击后显示
    .navbar {
      position: absolute;
      top: 48px; // 紧贴头部下方
      left: 0;
      right: 0;
      background-color: #283b42;
      flex-direction: column; // 垂直排列
      gap: 0 !important; // 取消水平间距
      height: auto !important;
      padding: 10px 0;
      display: none; // 默认隐藏

      // 点击后显示
      &.show {
        display: flex !important;
      }

      li {
        width: 100%;
        padding: 6px 20px;
        justify-content: flex-start; // 文字左对齐

        &:hover {
          background-color: rgba(255, 255, 255, 0.1);
        }
      }
    }

    // 移动端头像容器调整（可选，目标网站可能隐藏）
    .avatar-container {
      display: none !important;

      span:first-child {
        // display: none; // 隐藏搜索图标
        margin-right: 58px;
      }
    }
  }

  // 给body加padding-top，防止内容被固定头部遮挡
  body {
    padding-top: 48px;
  }

  // 动态控制菜单显示：通过isMenuOpen变量控制navbar的show类
  :deep(.navbar) {
    display: flex;

    &.show {
      display: flex !important;
    }
  }

  :deep(.article-top-category) {
    /* 深度选择器穿透scoped */
    background: red;
    position: relative !important;
    top: 50px !important;
    margin-bottom: 48px !important;
  }

  // 手机端隐藏创作者中心按钮
  .create-center {
    display: none !important;
  }

  .avatar-container {
    // display: none;
    right: 4.5rem !important;
  }

  // 设置半透明
  .show-menu {
    /* 黑色 (0, 0, 0), 透明度 0.5 (50%) */
    background-color: rgba(44, 59, 65, 0.9) !important;
    /* 确保内容与背景层叠 */
    z-index: 10 !important;
  }
}
</style>
