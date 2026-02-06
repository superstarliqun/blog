<template>
  <div class="wrap">
    <div class="head">头部导航栏</div>
    <div class="container">
      <div class="menu">功能菜单</div>
      <div class="content">
        <div class="breadcrumb">面包屑</div>
        <div class="main">
          <div class="top">动态标题不一定高度</div>
          <div class="right-container">
            <!-- 关键：给left容器加个相对定位的父层，让按钮基于left定位 -->
            <div class="left-wrapper">
              <div class="left">
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
                列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列
                表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列
                表列表表列列表列表列表列表列表表列列表列表列表列表列表表列列表列表列表列表列表表
              </div>
              <!-- 操作按钮放在left-wrapper里，基于left定位 -->
              <div class="caozuo">
                <button>11111</button>
              </div>
            </div>
            <div class="right">处理过程</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {}
</script>

<style lang="scss" scoped>
.wrap {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .head {
    background-color: #135b87;
    height: 34px;
    color: white;
    text-align: center;
  }

  .container {
    flex: 1;
    display: flex;

    .menu {
      width: 200px;
      background-color: #ffdbdb;
    }

    .content {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin: 8px;

      .breadcrumb {
        padding: 8px;
        background-color: #fff;
        border-radius: 8px;
      }

      .main {
        display: flex;
        flex-direction: column;
        background-color: #fff;
        padding: 8px;
        border-radius: 8px;
        overflow: hidden;
        height: calc(100vh - 108px);

        .top {
          width: 100%;
          height: 200px;
          background-color: rgb(242, 242, 242);
          text-align: center;
          margin: 0 auto;
        }

        .right-container {
          display: flex;
          flex: 1;
          overflow: hidden;

          .left-wrapper {
            flex: 0 0 66.666%;
            position: relative;
            padding-bottom: 40px;

            .left {
              height: 100%;
              background-color: rgb(242, 255, 224);
              overflow-y: auto;
            }

            .caozuo {
              position: absolute;
              bottom: 0;
              right: 0;
              padding: 8px;
            }
          }

          .right {
            flex: 1;
            background-color: rgb(206, 243, 255);
            overflow-y: auto;
          }
        }
      }
    }
  }
}
</style>
