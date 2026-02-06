<template>
  <div class="card-widget card-tag">
    <div class="card-title">
      <span>Tags</span>
      <router-link to="/tags" class="card-title-more">more</router-link>
    </div>
    <div class="tag-list">
      <span v-for="item in tags" :key="item.id" class="tag">
        <router-link v-slot="{ navigate, href }" :to="'/tags/' + item.tagsName" custom>
          <span class="your-span-class" :href="href" @click="navigate">
            {{ item.tagsName }}
            <span class="tag-number">{{ item.number }}</span>
          </span>
        </router-link>
      </span>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      tags: []
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    requestData() {
      this.$get(this.$urls.getTagList).then((res) => {
        this.tags = res.data
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.card-widget {
  padding: 16px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
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

.card-tag {
  .tag-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px 10px;
    margin-top: 10px;

    .tag {
      display: inline-block;

      span {
        display: flex;
        align-items: center;
        padding: 2px 10px;
        background-color: #f6f8fa;
        color: #4c4948;
        border-radius: 15px;
        font-size: 13px;
        transition: all 0.2s ease-in-out;
        cursor: pointer;

        &:hover {
          background-color: #49b1f5;
          color: #fff;

          .tag-number {
            background-color: #fff;
            color: #49b1f5;
          }
        }
      }

      .tag-number {
        margin-left: 5px;
        background-color: #a3abc1;
        color: #fff;
        font-size: 11px;
        height: 16px;
        min-width: 16px;
        padding: 0 5px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.2s;
      }
    }
  }
}
</style>
