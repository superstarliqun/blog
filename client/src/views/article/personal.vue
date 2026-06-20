<!-- 作者自己的页面 -->
<template>
  <div class="wu-wrap">
    <div class="wu-content">
      <div class="wu-home-body">
        <ul v-if="total > 0">
          <li v-for="(item, index) in tableData" :key="index" class="art-item">
            <div class="art-item-inner">
              <div class="art-item-title" @click="toDetails(item)">
                {{ item.title }}
              </div>
              <div class="art-item-media">{{ item.summary }}</div>
              <div class="art-item-bottom">
                <span class="wu-host">{{ item.createBy }}</span>
                <span class="item-date">{{ item.createTime }}</span>
                <div class="art-item-trends">
                  <span>观看 {{ item.views }}</span>
                  <span>回复 {{ item.comments }}</span>
                  <span>点赞 {{ item.likes }}</span>
                </div>
              </div>
              <span class="show-button edit" @click="gotoEdit(item)">编辑</span>
              <span class="show-button remove" @click="remove(item.id)">删除</span>
            </div>
          </li>
        </ul>
        <div v-else class="source">
          <el-skeleton />
        </div>
        <!-- 添加无限滚动组件 -->
        <infinite-loading
          :wrapper="'wu-content'"
          :distance="0"
          @infinite="loadMoreData"
        >
          <div slot="spinner" />
          <div slot="no-more" />
          <div slot="no-results" />
        </infinite-loading>
      </div>
      <div class="wu-home-right">
        <div class="group-info-container">
          <div class="group-info">
            <div class="group-image">
              <img
                src="https://wx.zsxq.com/dweb2/assets/images/group_bg/1.png"
              >
            </div>
            <div class="shadow" />
            <div class="owner-info">
              <div class="name">简单小屋的博客</div>
              <div class="date">卬明月而太息兮，步列星而极明</div>
            </div>
            <img
              class="avatar"
              src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"
            >
          </div>
          <div class="group-text">
            <a href="https://space.bilibili.com/352095550"><i class="iconfont icon-bilibili-line" /></a>
            <a href="https://blog.csdn.net/liqun_super"><i class="iconfont icon-csdn" /></a>
            <a href="#"><i class="iconfont icon-QQ" /></a>
            <a href="https://github.com/superstarliqun"><i class="iconfont icon-github-fill" /></a>
            <a href="https://www.zhihu.com/people/superstarliqun"><i class="iconfont icon-shejiaotubiao" /></a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import listMixin from '@/utils/mixins/listMixin'
import InfiniteLoading from 'vue-infinite-loading'
export default {
  components: {
    InfiniteLoading
  },
  mixins: [listMixin],
  data() {
    return {
      tableData: [],
      dateData: {
        year: '',
        month: '',
        date: '',
        hour: '',
        minute: '',
        second: ''
      }
    }
  },
  created() {
    this.requestData()
  },
  methods: {
    // 加载更多
    loadMoreData($state) {
      setTimeout(() => {
        if (this.currentPage * this.size < this.total) {
          this.currentPage++
          this.$get(this.$urls.articleMyPage, null, [
            this.currentPage,
            this.size
          ]).then((res) => {
            this.tableData = this.tableData.concat(res.data.records)
            $state.loaded()
          })
        } else {
          $state.complete()
        }
      }, 1000)
    },
    // 获取文章列表
    requestData() {
      this.$get(this.$urls.articleMyPage, null, [this.currentPage, this.size])
        .then((res) => {
          this.tableData = res.data.records
          this.total = res.data.total
        })
        .catch(() => {
          console.log('错误!')
        })
    },
    // 删除文章
    remove(id) {
      this.$post(this.$urls.articleRemove, { id: id }).then((res) => {
        if (res.code === 0) {
          this.requestData()
          this.$message.success('删除成功')
        }
      })
    },
    // 跳转详情
    toDetails(row) {
      if (row.id) {
        const routeData = this.$router.resolve({
          path: '/details',
          query: { pps: row.id }
        })
        window.open(routeData.href, '_blank')
      }
    },
    gotoEdit(row) {
      this.$router.push({
        name: 'tool',
        params: { id: row.id }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.art-item-inner {
  position: relative;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  padding: 24px 0;
  margin: 0 24px;
  border-bottom: 1px solid #f0f0f2;
  .art-item-title {
    font-size: 18px;
    font-weight: 500;
    line-height: 24px;
    color: #222226;
    overflow: hidden;
    white-space: normal;
    word-break: break-word;
    cursor: pointer;
  }
  .art-item-title:hover {
    color: #2791ff;
  }
  .art-item-media {
    margin-top: 8px;
    font-size: 14px;
    color: #555666;
    overflow: hidden; //多出的隐藏
    text-overflow: ellipsis; //多出部分用...代替
    display: -webkit-box; //定义为盒子模型显示
    -webkit-line-clamp: 2; //用来限制在一个块元素显示的文本的行数
    -webkit-box-orient: vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）
    word-break: break-word;
  }
}
.art-item-bottom {
  display: flex;
  color: #8a8a8a;
  font-size: 12px;
  margin-top: 14px;
  .item-date {
    margin-left: 24px;
    position: relative;
  }
  .item-date::before {
    position: absolute;
    content: ' ';
    left: -15px;
    top: 50%;
    margin-top: -1px;
    width: 6px;
    height: 2px;
    background: linear-gradient(135deg, #8a8a8a, #8a8a8a 98%);
    border-radius: 8px;
  }
  .art-item-trends {
    display: flex;
    justify-content: space-between;
    margin-left: auto;
    span {
      margin-left: 10px;
    }
  }
}
// 右侧 个人信息
.group-info-container {
  width: 100%;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 6px;
  box-shadow: 0 2px 4px 0 rgb(3 27 78 / 6%);
  .group-info {
    position: relative;
    height: 200px;
    .owner-info {
      position: absolute;
      bottom: -31px;
      left: 20px;
      width: 260px;
      margin-right: 20px;
      .name {
        font-size: 16px;
        color: #fff;
        height: 31px;
        line-height: 31px;
      }
      .date {
        font-size: 12px;
        color: #8b8e9d;
        height: 31px;
        line-height: 31px;
      }
    }
    .shadow {
      position: absolute;
      bottom: 0px;
      width: 100%;
      height: 72px;
      background-image: linear-gradient(
        -180deg,
        rgba(0, 0, 0, 0) 4%,
        rgba(0, 0, 0, 0.6) 98%
      );
    }
    .group-image {
      background: #f5f6f7;
      height: 200px;
      img {
        width: 100%;
        height: 200px;
        object-fit: cover;
      }
    }
    .avatar {
      position: absolute;
      bottom: -31px;
      right: 20px;
      width: 60px;
      height: 60px;
      border-radius: 50%;
      border: 3px solid #ffffff;
      box-shadow: 0 2px 8px #0000001a;
      background-color: #f5f6f7;
      display: block;
    }
  }
  .group-text {
    padding: 40px 20px 20px;
    background: #fff;
    overflow: hidden;
    color: #5a5c66;
    a {
      color: #5a5c66;
    }
  }
}
// 图标 个人信息
.iconfont {
  font-size: 26px;
  margin-right: 12px;
  cursor: pointer;
  color: #5a5c66;
}

.content {
  padding: 20px 10% 0 10%;
  background-color: #d1dddb;
}
.source {
  padding: 24px;
}
.show-button {
  color: #8a8a8a;
  position: absolute;
  top: 10px;
  right: 0px;
  font-size: 14px;
  cursor: pointer;
  display: none;
}
.art-item-inner:hover .show-button {
  display: block;
}
.edit {
  margin-right: 40px;
}

/* 隐藏滚动条 */
// ::-webkit-scrollbar {
//   width: 0 !important;
// }
// ::-webkit-scrollbar {
//   width: 0 !important;
//   height: 0;
// }
</style>
