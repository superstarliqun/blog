<template>
  <div id="wu-wrap" class="wu-wrap">
    <div class="wu-content">
      <div id="body" class="wu-art-body">
        <div class="wu-art-content">
          <div class="art-head">
            <div class="art-title">{{ formData.title }}</div>
            <div class="art-title-bottom">
              <span>{{ formData.createBy }}</span>
              <span>{{ formData.createTime }}</span>
              <span>阅读 {{ formData.watch }}</span>
              <div class="op-col">
                <span class="pointer padding-right5" @click="handCloseDirectory()">{{ directoryShow === true ? '关闭' :
                  '显示' }}目录</span>
                <div v-if="showEdit" class="pointer switch-edit" @click="toEdit(formData.id)"> 编辑 </div>
              </div>
            </div>
          </div>
          <div class="art-content">
            <div v-if="directoryShow" class="contents">
              <p v-for="(link, index) in tocList" :key="link.id" class="contents-item"
                :style="{ 'padding-left': link.level * 20 + 'px' }" @click="goDirectory('head-' + index)"
                v-html="link.text" />
            </div>
            <betymd v-if="formData.content" v-model="formData.content" display="view" @tocList="tocListData" />
          </div>
        </div>
      </div>
      <div id="aaa" class="wu-art-right">
        <div class="sticky-box top10 card-shadow author">
          <div class="toc-title-container">
            <span class="decoration-bar">目录</span>
          </div>
          <div class="toc-list">
            <div v-for="(link, index) in tocList" :key="link.id" class="toc-list-container" :style="{
              'padding-left': link.level * 20 + 'px',
              color: index === highlight ? '#409eff' : ''
            }" @click="goDirectory('head-' + index)" v-html="link.text" />
          </div>
        </div>
      </div>
    </div>
    <el-backtop class="backtop" target=".wu-wrap" />
  </div>
</template>
<script>
import { getUserInfo } from '@/utils/auth'
import Betymd from './betymd'
// import _ from 'lodash'
export default {
  components: {
    Betymd
  },
  data() {
    return {
      timer: '',
      formData: {},
      tocList: [],
      curTab: 0,
      showEdit: false,
      directoryWidth: 0,
      screenHeight: 0,
      scrollAmount: 0,
      scroll: 0,
      highlight: null,
      // 阅读百分比
      readPercentage: 0,
      // 目录隐藏标志
      directoryShow: true
    }
  },
  created() {
    this.getDetails()
  },
  methods: {
    // 获取详情信息
    getDetails() {
      this.$get(this.$urls.articleGet, null, [this.$getParma('pps')])
        .then((result) => {
          this.formData = result.data
          const userInfo = getUserInfo()

          if (userInfo.id === this.formData.authorId) {
            this.showEdit = true
          }
          this.timer = new Date().getTime()
        })
        .catch(() => {
          console.log('错误请求!')
        })
    },
    // 获取并处理目录数据
    tocListData(value) {
      // 移除第一个包含'theme'或'highlight'的元素
      const filteredValue = value.filter((element, index) => {
        return (
          index !== 0 ||
          !(
            element.text.includes('theme') || element.text.includes('highlight')
          )
        )
      })
      // 为每个元素添加级别属性
      const tocListWithLevel = filteredValue.map((element) => {
        const level = ['h1', 'h2', 'h3'].indexOf(element.tagName) + 1
        return { ...element, level }
      })
      this.tocList = tocListWithLevel
    },
    handCloseDirectory() {
      this.directoryShow = !this.directoryShow
    },
    // 点击文档顶部进行跳转指定锚点
    goDirectory(sectionId) {
      const section = document.getElementById(sectionId)
      if (section) {
        section.scrollIntoView({ behavior: 'smooth' })
      }
    },
    // 跳转编辑页面
    toEdit(id) {
      this.$get(this.$urls.getUserInfo).then((res) => {
        if (res.code === 0) {
          this.$router.push({
            name: 'tool',
            params: { id: id }
          })
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.wu-wrap {
  min-height: 100%;

  .wu-art-body {
    width: 800px;
    background-color: #fff;
    box-shadow: 0 2px 4px 0 rgb(3 27 78 / 6%);
    padding: 8px 24px;
    margin-right: 20px;

    .contents {
      color: #4ea1db;
      margin-top: 40px;

      .contents-item {
        cursor: pointer;
        font-size: 16px;
        line-height: 24px;
      }
    }
  }

  .art-head {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    border-bottom: 1px solid #dadada;
    padding-bottom: 16px;
  }

  .art-title {
    font-size: 28px;
    font-weight: bold;
    line-height: 38px;
  }

  .art-title-bottom {
    margin-top: 12px;
    font-size: 14px;
    color: #8a8a8a;

    span {
      margin-right: 10px;
    }
  }

  .wu-art-right {
    flex: 1;
  }

  .author {
    padding: 10px 18px;
    background-color: #fff;
  }

  .el-backtop {
    margin-bottom: 20px;
  }

  ::v-deep .cherry {
    box-shadow: none;
  }

  ::v-deep .cherry-previewer {
    background-color: #ffffff;
    border-left: 0;
  }

  .op-col {
    float: right;
    display: flex;
  }

  .directory {
    max-height: 448px;
    position: fixed;
    top: 0;
    margin-top: 20px;
    overflow: auto;

    div {
      overflow-x: hidden;
      width: 100%;
      white-space: nowrap;
      /* 防止换行 */
      text-overflow: ellipsis;
      /* 超出部分用省略号代替 */
    }
  }

  .showNone {
    display: none;
  }
}

.toc {
  box-shadow: 0 2px 4px 0 rgba(3, 27, 78, 0.06);
  background-color: var(--text-100);
}

.highlight {
  color: #409eff;
}

// 固钉样式
.sticky-box {
  position: sticky;
  top: 10px;
  z-index: 10;
  background-color: #fff;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
