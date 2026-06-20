<template>
  <div>
    <el-date-picker v-model="param.addTime" type="datetimerange" range-separator="至" start-placeholder="开始日期"
                    end-placeholder="结束日期" size="mini" value-format="yyyy-MM-dd HH:mm:ss"
    />
    <el-button plain @click="getQuery">获取数据</el-button>
    <el-input v-model="firstValue" @input="firstValue = firstValue.toString().replace(/^(0+)|[^\d]+/g, '')" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      firstValue: null,

      firstYearList: [
        { id: 1, name: '将进酒' },
        { id: 2, name: 'Item 2' },
        { id: 3, name: 'Item 3' },
        { id: 4, name: 'Item 4' }
      ],
      lastYearList: [
        { id: 2, name: 'Item 2' },
        { id: 3, name: 'Item 3' },
        { id: 4, name: 'Item 4' },
        { id: 5, name: 'Item 5' },
        { id: 6, name: 'Item 6' },
        { id: 7, name: 'Item 7' }
      ],
      param: {
        addTime: null
      }
    }
  },
  created() {
    this.requestData()
  },
  methods: {
    getQuery() {
      console.log(this.param.addTime)
    },
    requestData() {
      for (let index = 0; index < this.lastYearList.length; index++) {
        if (this.firstYearList.length >= index + 1) {
          this.firstYearList[index].lastId = this.lastYearList[index].id
        }
      }
      console.log(this.firstYearList, this.lastYearList)
    }
  }
}
</script>

<style></style>
