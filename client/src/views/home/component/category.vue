<template>
  <div class="card-widget card-category">
    <div class="card-title">
      <span>Categories</span>
      <router-link to="/category" class="card-title-more">more</router-link>
    </div>
    <ul class="category-container">
      <li v-for="item in cats" :key="item.id" @click="handleTo(item)">
        <div class="category-name">
          <img :src="replaceImg(item.id)" alt="">
          {{ item.categoryName }}
        </div>
        <span class="category-number">{{ item.number }}篇</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      cats: []
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    requestData() {
      this.cats = this.value
    },
    replaceImg(index) {
      return require(`@/assets/img/category/${index}.png`)
    },
    handleTo(row) {
      this.$router.push({
        name: 'CategoryDetail',
        params: { categoryName: row.categoryName }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.card-widget {
  background: white;
  border-radius: 6px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
  padding: 8px 16px;
  font-size: 16px;

  .card-title {
    display: flex;
    justify-content: space-between;
    align-items: center;

    :first-child {
      font-weight: 600;
    }

    .card-title-more {
      font-weight: unset;
      cursor: pointer;
      font-size: 12px;
      color: #959595;

      &::after {
        content: '\e756';
        font-family: 'iconfont';
        font-size: 12px;
      }

      &:hover {
        color: #4b9bcb;
      }

      &>i {
        font-weight: 12px;
      }
    }
  }
}

.category-container {
  margin-top: 12px;
  font-size: 14px;
  display: flex;
  flex-direction: column;
  gap: 4px;

  li {
    line-height: 2;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 0.6px solid #e7e9f0;
    padding: 4px 8px;
    border-radius: 4px;
    color: #191d22;
    cursor: pointer;

    &:hover {
      background-color: #f2f7fd;
    }

    img {
      width: 26px;
      height: 26px;
      object-fit: cover;
      flex-shrink: 0;
      border-radius: 4px;
    }

    .category-number {
      color: #99a1af;
      font-size: 12px;

      &::after {
        content: '\e756';
        font-family: 'iconfont';
        font-size: 12px;
        margin-left: 6px;
        color: #99a1af;
      }
    }
  }

  .category-name {
    display: flex;
    gap: 12px;
    align-items: center;
  }
}
</style>
