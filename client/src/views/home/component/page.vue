<template>
  <div id="pagination">
    <div class="pagination">
      <a v-if="currentPage != 1" class="extend prev" :href="link(currentPage - 1)">
        <i class="iconfont icon-page-left" />
        <div class="pagination_tips_prev">上页</div>
      </a>
      <a v-if="currentPage > 3" class="page-number" :href="link(1)">1</a>
      <span v-if="currentPage > 3" class="space">…</span>
      <a v-if="currentPage - 2 >= 1" class="page-number" :href="link(currentPage - 2)">{{ currentPage - 2 }}</a>
      <a v-if="currentPage - 1 >= 1" class="page-number" :href="link(currentPage - 1)">{{ currentPage - 1 }}</a>
      <a class="page-number current" :href="link(currentPage)">{{ currentPage }}</a>
      <a v-if="currentPage + 1 <= pageNumber" class="page-number" :href="link(currentPage + 1)">{{ currentPage +
        1 }}</a>
      <a v-if="currentPage + 2 <= pageNumber" class="page-number" :href="link(currentPage + 2)">{{ currentPage +
        2 }}</a>

      <span v-if="currentPage + 2 < pageNumber" class="space">…</span>
      <a v-if="currentPage + 2 < pageNumber" class="page-number" :href="link(pageNumber)">{{ pageNumber }}</a>
      <div class="toPageGroup" data-tip="跳转到指定页面">
        <input id="toPageText" v-model="jumpNumber" maxlength="3" @input="e => jumpNumber = e.target.value.replace(/\D/g, '').slice(0, 3) > pageNumber
          ? String(pageNumber)
          : e.target.value.replace(/\D/g, '').slice(0, 3)">
        <a id="toPageButton" :class="jumpNumber != '' ? 'haveValue' : ''" @click="jumpTo()"> <i
            class="iconfont icon-double-left-1" /></a>
      </div>
      <a v-if="currentPage != pageNumber" class="extend next" :href="link(currentPage + 1)">
        <div class="pagination_tips_next">下页</div>
        <i class="iconfont icon-page-right" />
      </a>
    </div>

  </div>
</template>

<script>
import listMixin from '@/utils/mixins/listMixin'
export default {
  mixins: [listMixin],
  props: {
    totalNumber: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      pageNumber: null,
      jumpNumber: ''
    }
  },
  created() {
    this.requestData()
  },
  methods: {
    requestData() {
      if (this.$route.params.id) {
        this.currentPage = Number(this.$route.params.id)
      } else {
        this.currentPage = 1
      }
      this.pageNumber = Math.ceil(this.totalNumber / this.size)
    },
    jumpTo() {
      // 不能用路由跳转，因为路由如果同一个只有参数变化，不会渲染
      window.location.href = this.link(this.jumpNumber)
    },
    link(page) {
      return page === 1 ? '/' : `/page/${page}`
    }
  }
}
</script>

<style lang="scss" scoped>
@media screen and (min-width: 768px) {

  #pagination {
    text-align: center;
    position: relative;
    display: flex;
    justify-content: center;
    gap: 8px;
    margin-top: 0.5rem;
    font-size: 16px;
    width: 100%;

    .pagination {
      text-align: center;
      position: relative;
      display: flex;
      justify-content: center;
      gap: 8px;
      width: 100%;

      a.extend.next,
      a.extend.prev {
        width: 4rem;
        height: 2rem;
        line-height: 1.9rem;
        border-radius: 8px !important;
        background: #fff;
        box-shadow: 0 8px 16px -4px #2c2d300c;
        position: absolute;
        border: 1px solid #e3e8f7;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
        transition: .3s;
      }

      a.extend.prev {
        left: 0;
      }

      a.extend.next {
        right: 0;
      }

      a.extend.next:hover,
      a.extend.prev:hover {
        color: #425AEF;
        border: 1px solid #425AEF !important;
        box-shadow: 0 8px 12px -3px #4259ef23 !important;
      }

      .page-number {
        width: 2rem;
        background: #fff;
        height: 2rem;
        line-height: calc(2rem - 2px);
        border-radius: 8px !important;
        box-shadow: 0 8px 16px -4px #2c2d300c;
        border: 1px solid #e3e8f7;
        transition: .3s;
      }

      .page-number.current {
        box-shadow: 0 8px 12px -3px #4259ef23;
        background: #425AEF;
        border: 1px solid #425AEF !important;
        color: white;
      }
    }

  }

}

.toPageGroup {
  display: flex;
  position: relative;

  &>input:focus,
  &:hover input {
    border: 1px solid #425AEF !important;
    outline-style: none;
    width: 100px;
  }

  &:focus-within a#toPageButton,
  &:hover a#toPageButton {
    width: 30px;
    height: 30px;
    right: 4px;
    top: 5px;
    background: #fff;

    border: 1px solid #00000000;
    border-radius: 4px;
    opacity: .2
  }

  &:focus-within a#toPageButton.haveValue {
    opacity: 1;
    cursor: pointer
  }

  a#toPageButton.haveValue {
    opacity: 1 !important;
    cursor: pointer !important;
    pointer-events: all
  }

  a#toPageButton.haveValue:hover {
    background-color: #475ae7;
    color: white;
  }

  input {
    width: 40px;
    height: 40px;
    border-radius: 8px;
    border: 1px solid #e3e8f7;
    transition: .3s;
    outline-style: none;
    font-size: 16px;
    padding-left: 12px;
    background: #f7f7f9;
    color: #363636;
  }

  a#toPageButton {
    display: flex;
    position: absolute;
    width: 40px;
    height: 40px;
    right: 0;
    top: 0;
    border-radius: 8px;
    justify-content: center;
    align-items: center;
    transition: .3s;
    background: #fff;
    border: 1px solid #e3e8f7;
    cursor: text;
    pointer-events: none;
  }
}

#pagination a.extend.prev .pagination_tips_prev {
  margin-right: -32px;
  transition: .3s ease-out 0s;
  opacity: 0;
}

#pagination a.extend.next .pagination_tips_next {
  margin-left: -32px;
  transition: .3s ease-out 0s;
  opacity: 0;
}

#pagination a.extend.prev:hover .pagination_tips_prev {
  margin-right: 2px;
  opacity: 1;
  white-space: nowrap;
}

#pagination a.extend.next:hover .pagination_tips_next {
  margin-left: 2px;
  opacity: 1;
  white-space: nowrap;
}
</style>
