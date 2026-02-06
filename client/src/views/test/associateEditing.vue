<!-- 批次关联编辑 -->
<template>
  <div class="public-box">
    <el-dialog :title="title" :visible="true" :before-close="handleClose" width="1400px">
      <div class="container">
        <p class="pro_name">{{ paramObj.provinceName }}</p>
        <p class="size16 bold">关联批次颜色一致</p>
        <div class="table_box">
          <div v-for="(item,index) in tableData" :key="index">
            <el-table :data="item.valueList" border :row-style="tableStyle">
              <el-table-column v-for="column in item.title" :key="column.name" align="center" :prop="column.name" :label="column.value.toString()" width="100">
                <template slot-scope="scope">
                  <div v-if="scope.row[column.name] === false">
                    <el-checkbox v-if="column.name ==='titleFirst'" :key="timer" v-model="scope.row.checked1" @change="handleChecked"></el-checkbox>
                    <el-checkbox v-if="column.name ==='titleLast'" :key="timer" v-model="scope.row.checked2" @change="handleChecked"></el-checkbox>
                  </div>
                  <div v-else-if="scope.row[column.name] === true">
                    <el-button size="mini" class="add-btn other-btn" @click="handleDisassociation(column.value,scope.row)">解除关联</el-button>
                  </div>
                  <p v-else> {{ scope.row[column.name] }}</p>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div class="operationBtn">
        <el-button size="mini" class="submit-btn" @click="handleBuildAssociation">建立关联</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    paramObj: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      title: '',
      tableData: [],
      timer: null,
      colors: ['#BDD1F9', '#BCEED5', '#E2F7FB', '#F9E3A0', '#EFC4B7', '#B3E1F5', '#FAD4E6', '#B1DBDB']
    }
  },
  mounted() {
    if (this.paramObj.index === 0) {
      this.title = '修改关联'
    } else {
      this.title = '新增关联'
    }
    this.getTableData()
  },
  methods: {
    // 关联批次颜色一致
    tableStyle({ row, rowIndex }) {
      let color = null
      for (var i = 0; i < this.tableData.length - 1; i++) {
        this.tableData[i].valueList.forEach((item, index) => {
          this.tableData[i + 1].valueList.forEach((data, num) => {
            if (item.titleLast === true && data.titleFirst === true) {
              color = this.colors[rowIndex % this.colors.length]
            } else {
              color = null
            }
          })
        })
      }
      return { 'background-color': color }
    },
    // 获取表格数据
    getTableData() {
      // const url = '/admin/sysProBatchRelation/getEditData'
      // const params = {
      //   provinceId: this.paramObj.provinceId
      // }
      // this.reqM1Service(url, params).then(res => {
      const res = this.data1()
      if (res.code === 200) {
        this.tableData = res.data
        const tableInfo = res.data
        for (var i = 0; i < tableInfo.length; i++) {
          tableInfo[i].valueList.forEach((element) => {
            if (element.titleFirst === false) {
              element.checked1 = false
            }
            if (element.titleLast === false) {
              element.checked2 = false
            }
          })
        }
      } else {
        this.$message.error(res.msg)
      }
      // }).catch(failResponse => { })
    },
    // 建立关联
    handleBuildAssociation() {
      // 关联数据信息
      const relationList = []
      for (var i = 0; i < this.tableData.length - 1; i++) {
        const leftData = this.tableData[i].valueList
        const rightData = this.tableData[i + 1].valueList
        const leftNum = leftData.filter(item => item.checked2 === true)
        const rightNum = rightData.filter(item => item.checked1 === true)
        if ((leftNum.length >= 1 && rightNum.length === 1) || (leftNum.length === 1 && rightNum.length >= 1)) {
          leftData.forEach(data => {
            if (data.checked2 === true) {
              const obj = {}
              obj.parentPbId = data.batchId
              obj.syncYear = data.time
              rightData.forEach(item => {
                if (item.checked1 === true) {
                  const newObj = { ...obj } // 创建一个新的对象，并将 obj 的属性复制到新对象中
                  newObj.childPbId = item.batchId
                  newObj.beSyncYear = item.time
                  relationList.push(newObj)
                }
              })
            }
          })
        }
      }
      if (relationList.length > 0) {
        const url = '/admin/sysProBatchRelation/createRelation'
        const params = {
          sysProvinceId: this.paramObj.provinceId,
          relationList: relationList
        }
        this.$confirm('确定关联选择数据吗, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 调用第一个接口的请求服务
          this.reqM1Service(url, params).then(res => {
            if (res.code === 200) {
              this.$message.success('关联成功')
              this.getTableData()
              this.$parent.getBasicData()
              this.$nextTick(() => {
                this.handleClose()
              })
            } else {
              this.$message.error(res.msg)
            }
          }).catch(failResponse => { })
        }).catch(failResponse => { })
      } else {
        this.$message.error('请选择正确的关联数据')
      }
    },
    // 解除关联
    handleDisassociation(headVal, tableVal) {
      // 拆分文本和数值
      const regex = /([\u4e00-\u9fa5]+)(\d+)/
      const matches = headVal.match(regex)
      let numberVal = null
      if (matches) {
        numberVal = matches[2] // 匹配到的数字部分
      }
      let dataType = null// 解除关系类型 1-下一年 2-上一年
      if (tableVal.time > Number(numberVal)) {
        dataType = 2
      } else {
        dataType = 1
      }
      const url = '/admin/sysProBatchRelation/dissolution'
      const params = {
        time: tableVal.time,
        dissolutionType: dataType,
        dissolutionId: tableVal.batchId
      }
      this.$confirm('此操作将解除数据关系, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用第一个接口的请求服务
        this.reqM1Service(url, params).then(res => {
          if (res.code === 200) {
            this.$message.success('解除成功')
            this.getTableData()
            this.$parent.getBasicData()
            this.handleClose()
          } else {
            this.$message.error(res.msg)
          }
        }).catch(failResponse => { })
      })
    },
    handleChecked() {
      this.timer = new Date().getTime()
    },
    // 关闭弹窗
    handleClose() {
      this.$emit('close')
    },
    data1() {
      return { 'code': 200, 'msg': '成功', 'data': [{ 'valueList': [{ 'firstIdList': [], 'titleLast': true, 'lastIdList': [210, 211], 'name': '本科批', 'time': 2023, 'batchId': 208, 'titleFirst': false }, { 'firstIdList': [], 'titleLast': false, 'lastIdList': [], 'name': '专科批', 'time': 2023, 'batchId': 209, 'titleFirst': false }], 'title': [{ 'name': 'name', 'value': 2023 }, { 'name': 'titleLast', 'value': '关联2022' }] }, { 'valueList': [{ 'firstIdList': [208], 'titleLast': false, 'lastIdList': [], 'name': '本科批', 'time': 2022, 'batchId': 210, 'titleFirst': true }, { 'firstIdList': [208], 'titleLast': false, 'lastIdList': [], 'name': '专科批', 'time': 2022, 'batchId': 211, 'titleFirst': true }], 'title': [{ 'name': 'titleFirst', 'value': '关联2023' }, { 'name': 'name', 'value': 2022 }, { 'name': 'titleLast', 'value': '关联2021' }] }, { 'valueList': [{ 'firstIdList': [], 'titleLast': true, 'lastIdList': [215, 216], 'name': '本科批', 'time': 2021, 'batchId': 212, 'titleFirst': false }, { 'firstIdList': [], 'titleLast': true, 'lastIdList': [214], 'name': '专科批', 'time': 2021, 'batchId': 213, 'titleFirst': false }], 'title': [{ 'name': 'titleFirst', 'value': '关联2022' }, { 'name': 'name', 'value': 2021 }, { 'name': 'titleLast', 'value': '关联2020' }] }, { 'valueList': [{ 'firstIdList': [213], 'titleLast': false, 'lastIdList': [], 'name': '本科一批', 'time': 2020, 'batchId': 214, 'titleFirst': true }, { 'firstIdList': [212], 'titleLast': false, 'lastIdList': [], 'name': '本科二批', 'time': 2020, 'batchId': 215, 'titleFirst': true }, { 'firstIdList': [212], 'titleLast': false, 'lastIdList': [], 'name': '专科批', 'time': 2020, 'batchId': 216, 'titleFirst': true }], 'title': [{ 'name': 'titleFirst', 'value': '关联2021' }, { 'name': 'name', 'value': 2020 }] }], 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-dialog {
  padding: 20px;
}
.container {
  border: 1px solid #ccc;
  padding: 10px 20px;
  margin: 20px;
  .pro_name {
    text-align: center;
    font-weight: bold;
    font-size: 16px;
    color: #000;
    padding-bottom: 10px;
  }
  .table_box {
    width: 100%;
    margin-top: 10px;
    display: flex;
    align-items: flex-start;
    justify-content: space-evenly;
    .el-table {
      width: 100%;
      td {
        font-size: 14px;
        font-weight: bold;
        color: #000;
        height: 36px;
        padding: 0;
      }
      th {
        height: 36px;
        padding: 0;
      }
    }
  }
}
</style>
