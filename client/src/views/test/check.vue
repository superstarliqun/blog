<template>
  <div>
    <el-button plain @click="handleCheckAllChange">全选/取消全选</el-button>
    <div v-for="(item, index) in infoBlockArrData" :key="index" class="selected_content">
      <div class="selected_head">
        {{ item.name }}
      </div>
      <div class="selected_body">
        <el-checkbox v-for="(info, j) in item.content_list" :key="j" :label="info.cn" :value="info.check" @change="handleCheckedChange(info)">{{ info.cn }}{{ info.check }}</el-checkbox>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 源数据
      tableData: [
        { dimCn: '客户维', dimEn: 'cubeTT1', en: 'customer', cn: '顾客' },
        { dimCn: '单位维', dimEn: 'cubeTT2', en: 'customer1', cn: '货币码' },
        { dimCn: '单位维', dimEn: 'cubeTT2', en: 'customer2', cn: '计量单位' },
        { dimCn: '销售维度', dimEn: 'cubeTT3', en: 'customer3', cn: '日历日' },
        { dimCn: '销售维度', dimEn: 'cubeTT3', en: 'customer4', cn: '日历月' },
        { dimCn: '销售维度', dimEn: 'cubeTT3', en: 'customer5', cn: '日历年' }
      ],
      // 重构数据
      infoBlockArrData: [],
      // check-group
      checkArr: []
    }
  },
  mounted() {
    this.requestData()
    // this.requestData1()
  },
  methods: {
    requestData() {
      const groupedData = {}
      this.tableData.forEach(item => {
        if (!groupedData[item.dimCn]) {
          groupedData[item.dimCn] = []
        }
        groupedData[item.dimCn].push({ en: item.en, cn: item.cn, check: false })
      })
      const result = []
      for (const name in groupedData) {
        result.push({ name, content_list: groupedData[name] })
      }
      this.infoBlockArrData = result
      console.log(this.infoBlockArrData)
    },
    requestData1() {
      const tableDataArr = this.tableData
      const tempArr = []
      const that = this
      for (let i = 0; i < tableDataArr.length; i++) {
        tableDataArr[i].check = false
        if (tempArr.indexOf(tableDataArr[i].dimCn) === -1) {
          that.infoBlockArrData.push({
            name: tableDataArr[i].dimCn,
            content_list: [tableDataArr[i]]
          })
          tempArr.push(tableDataArr[i].dimCn)
        } else {
          for (let j = 0; j < that.infoBlockArrData.length; j++) {
            if (that.infoBlockArrData[j].name === tableDataArr[i].dimCn) {
              that.infoBlockArrData[j].content_list.push(tableDataArr[i])
              break
            }
          }
        }
      }
      console.log(this.infoBlockArrData)
    },
    //
    handleCheckedChange(val) {
      val.check = !val.check
    },
    // 全选
    handleCheckAllChange() {
      this.infoBlockArrData.forEach(element => {
        element.content_list.forEach(item => {
          item.check = !item.check
        })
      })
    }
  }
}
</script>

<style>
</style>
