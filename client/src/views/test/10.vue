<template>
  <div v-cloak id="app">
    <!-- 内容栏 -->
    <div class="layout-main">
      <div class="ibox-search">
        <!-- 循环表头 -->
        <el-table ref="table" :data="tableData" border @cell-click="cellClick">
          <el-table-column v-for="(item, index) in headData" :key="index" :index="index" :label="item.name"
            align="center" show-overflow-tooltip :prop="item.key">
            <template slot-scope="{row}">
              <p :class="displayClass(row[item.key].id)">id:{{ row[item.key].id }}|{{ row[item.key].value }}</p>
            </template>
          </el-table-column>
        </el-table>
        <div class="submit-bottom">
          <el-button class="submit-id" type="primary" plain @click="handleData()">提交获取ID</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      headData: [],
      // 选中的值
      selectValue: [],
      i: 0
    }
  },
  mounted() {
    this.searchList()
  },
  methods: {
    // 获取表格数据
    searchList() {
      const res = this.data1()
      // 获取选中的数据
      console.log(res)
      res.data.forEach(element => {
        if (element.userRecordOptionIds != null && element.userRecordOptionIds !== undefined) {
          this.selectValue.push(element.userRecordOptionIds)
        }
      })
      // 表头
      this.headData = res.data.map((item) => {
        return {
          id: item.id,
          key: item.name,
          name: item.name
        }
      })
      // 数据整合
      this.tableData = []
      res.data[0].sysCareerTestOptionList.forEach((item, index) => {
        const object = {}
        res.data.forEach((item1, index1) => {
          object[item1.name] = {
            id: item1.sysCareerTestOptionList[index].id,
            value: item1.sysCareerTestOptionList[index].optionContent,
            index: index,
            x: index1,
            y: index
          }
        })
        this.tableData.push(object)
      })
    },
    // calss回显
    displayClass(id) {
      let result = null
      this.selectValue.forEach(element => {
        if (element === id) {
          result = 'active'
        }
      })
      return result
    },
    // 获取选中的数据
    handleData() {
      console.log(this.selectValue)
    },
    // 点击单元格事件
    cellClick(row, column, cell, event) {
      // 循环遍历删除class
      const parentParentElement = cell.parentElement.parentNode
      for (let i = 0; i < parentParentElement.children.length; i++) {
        const child = parentParentElement.children[i]
        let dom = child.children[column.index]
        dom = dom.children[0].children[0]
        dom.className = dom.className.replace(' active', '')
      }
      // 设置new的class
      cell = cell.children[0].children[0]
      cell.className = cell.className + ' active'
      // 获取点击这一行column
      let currentColumn = []
      const res = this.data1().data
      res.forEach(element => {
        if (element.name === column.label) {
          currentColumn = element.sysCareerTestOptionList
        }
      })
      // 获取一列只能选择一个
      const col = []
      currentColumn.forEach(element => {
        col.push(element.id)
      })
      const hasIntersection = col.some(item => this.selectValue.includes(item))
      if (!hasIntersection) {
        this.selectValue.push(row[column.label].id)
      } else {
        this.selectValue.forEach((element, index) => {
          currentColumn.forEach(item => {
            if (item.id === element) {
              this.selectValue.splice(index, 1, row[column.label].id)
            }
          })
        })
      }
    },
    data1() {
      return { 'code': 200, 'msg': '成功', 'data': [{ 'id': 43, 'name': 'R', 'libraryType': 2, 'questionType': 2, 'orderIndex': 1, 'content': '', 'sysCareerTestOptionList': [{ 'id': 189, 'sysTestQuestionId': 43, 'orderIndex': 1, 'optionContent': '7', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 190, 'sysTestQuestionId': 43, 'orderIndex': 2, 'optionContent': '6', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 191, 'sysTestQuestionId': 43, 'orderIndex': 3, 'optionContent': '5', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 192, 'sysTestQuestionId': 43, 'orderIndex': 4, 'optionContent': '4', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 193, 'sysTestQuestionId': 43, 'orderIndex': 5, 'optionContent': '3', 'isRight': 1, 'rightContent': '', 'score': 1, 'optionImg': '' }, { 'id': 194, 'sysTestQuestionId': 43, 'orderIndex': 6, 'optionContent': '2', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 195, 'sysTestQuestionId': 43, 'orderIndex': 7, 'optionContent': '1', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }], 'userCareerTestRecordId': null, 'userRecordOptionIds': null }, { 'id': 44, 'name': 'I', 'libraryType': 2, 'questionType': 2, 'orderIndex': 2, 'content': '', 'sysCareerTestOptionList': [{ 'id': 196, 'sysTestQuestionId': 44, 'orderIndex': 1, 'optionContent': '7', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 197, 'sysTestQuestionId': 44, 'orderIndex': 2, 'optionContent': '6', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 198, 'sysTestQuestionId': 44, 'orderIndex': 3, 'optionContent': '5', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 199, 'sysTestQuestionId': 44, 'orderIndex': 4, 'optionContent': '4', 'isRight': 1, 'rightContent': null, 'score': 1, 'optionImg': null }, { 'id': 200, 'sysTestQuestionId': 44, 'orderIndex': 5, 'optionContent': '3', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 201, 'sysTestQuestionId': 44, 'orderIndex': 6, 'optionContent': '2', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 202, 'sysTestQuestionId': 44, 'orderIndex': 7, 'optionContent': '1', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }], 'userCareerTestRecordId': null, 'userRecordOptionIds': null }, { 'id': 45, 'name': 'A', 'libraryType': 2, 'questionType': 2, 'orderIndex': 3, 'content': '', 'sysCareerTestOptionList': [{ 'id': 203, 'sysTestQuestionId': 45, 'orderIndex': 1, 'optionContent': '7', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 204, 'sysTestQuestionId': 45, 'orderIndex': 2, 'optionContent': '6', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 205, 'sysTestQuestionId': 45, 'orderIndex': 3, 'optionContent': '5', 'isRight': 1, 'rightContent': null, 'score': 1, 'optionImg': null }, { 'id': 206, 'sysTestQuestionId': 45, 'orderIndex': 4, 'optionContent': '4', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 207, 'sysTestQuestionId': 45, 'orderIndex': 5, 'optionContent': '3', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 208, 'sysTestQuestionId': 45, 'orderIndex': 6, 'optionContent': '2', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 209, 'sysTestQuestionId': 45, 'orderIndex': 7, 'optionContent': '1', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }], 'userCareerTestRecordId': null, 'userRecordOptionIds': null }, { 'id': 46, 'name': 'S', 'libraryType': 2, 'questionType': 2, 'orderIndex': 4, 'content': '', 'sysCareerTestOptionList': [{ 'id': 210, 'sysTestQuestionId': 46, 'orderIndex': 1, 'optionContent': '7', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 211, 'sysTestQuestionId': 46, 'orderIndex': 2, 'optionContent': '6', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 212, 'sysTestQuestionId': 46, 'orderIndex': 3, 'optionContent': '5', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 213, 'sysTestQuestionId': 46, 'orderIndex': 4, 'optionContent': '4', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 214, 'sysTestQuestionId': 46, 'orderIndex': 5, 'optionContent': '3', 'isRight': 1, 'rightContent': null, 'score': 1, 'optionImg': null }, { 'id': 215, 'sysTestQuestionId': 46, 'orderIndex': 6, 'optionContent': '2', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 216, 'sysTestQuestionId': 46, 'orderIndex': 7, 'optionContent': '1', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }], 'userCareerTestRecordId': null, 'userRecordOptionIds': null }, { 'id': 47, 'name': 'E', 'libraryType': 2, 'questionType': 2, 'orderIndex': 5, 'content': '', 'sysCareerTestOptionList': [{ 'id': 217, 'sysTestQuestionId': 47, 'orderIndex': 1, 'optionContent': '7', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 218, 'sysTestQuestionId': 47, 'orderIndex': 2, 'optionContent': '6', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 219, 'sysTestQuestionId': 47, 'orderIndex': 3, 'optionContent': '5', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 220, 'sysTestQuestionId': 47, 'orderIndex': 4, 'optionContent': '4', 'isRight': 1, 'rightContent': '', 'score': 1, 'optionImg': '' }, { 'id': 221, 'sysTestQuestionId': 47, 'orderIndex': 5, 'optionContent': '3', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 222, 'sysTestQuestionId': 47, 'orderIndex': 6, 'optionContent': '2', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }, { 'id': 223, 'sysTestQuestionId': 47, 'orderIndex': 7, 'optionContent': '1', 'isRight': null, 'rightContent': '', 'score': null, 'optionImg': '' }], 'userCareerTestRecordId': null, 'userRecordOptionIds': null }, { 'id': 48, 'name': 'C', 'libraryType': 2, 'questionType': 2, 'orderIndex': 6, 'content': '', 'sysCareerTestOptionList': [{ 'id': 224, 'sysTestQuestionId': 48, 'orderIndex': 1, 'optionContent': '7', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 225, 'sysTestQuestionId': 48, 'orderIndex': 2, 'optionContent': '6', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 226, 'sysTestQuestionId': 48, 'orderIndex': 3, 'optionContent': '5', 'isRight': 1, 'rightContent': null, 'score': 1, 'optionImg': null }, { 'id': 227, 'sysTestQuestionId': 48, 'orderIndex': 4, 'optionContent': '4', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 228, 'sysTestQuestionId': 48, 'orderIndex': 5, 'optionContent': '3', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 229, 'sysTestQuestionId': 48, 'orderIndex': 6, 'optionContent': '2', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }, { 'id': 230, 'sysTestQuestionId': 48, 'orderIndex': 7, 'optionContent': '1', 'isRight': null, 'rightContent': null, 'score': null, 'optionImg': null }], 'userCareerTestRecordId': null, 'userRecordOptionIds': null }], 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
    }
  }
}
</script>

<style lang="scss" scoped>
.layout-main {
  padding: 20px;
  height: 1000px;
}

.circle {
  display: inline-block;
  border-radius: 50%;
  border: 2px solid black;
  width: 30px;
  height: 30px;
  text-align: center;
  line-height: 30px;
}

.active {
  background-color: red;
}

.submit-bottom {
  text-align: center;

  .submit-id {
    margin: 10px 0;
    width: 200px;
  }
}
</style>
