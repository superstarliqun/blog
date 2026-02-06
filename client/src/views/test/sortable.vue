<template>
  <div class="wrap">
    <div class="div1">
      <h1>模板</h1>
      <el-table ref="list1" :data="list1" row-key="id" border default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column prop="cn" label="模板" />
        <el-table-column prop="en" label="技术名称" />
      </el-table>
    </div>
    <div class="div2">
      <h1>数据存储</h1>
      <el-table ref="list2" :data="list2" row-key="id" border default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column prop="cn" label="数据存储对象" width="200" />
        <el-table-column prop="en" label="技术名称/值" />
        <el-table-column prop="en1" label="数据类型" />
        <el-table-column prop="en2" label="长度" />
      </el-table>
    </div>
    <div class="div2">
      <h1>获取结果集</h1>
      <el-button type="primary" plain @click="log()">获取结果集</el-button>
    </div>
  </div>
</template>

<script>
import Sortable from 'sortablejs'
import tableData from './components/data'
import { deepClone } from './components/index'
export default {
  data: () => ({
    initStatus: {
      newlist1: true,
      newlist2: true
    },
    fromItem: '',
    list1: [],
    list2: [],
    newlist1: [],
    newlist2: []
  }),
  created() {
    this.list1 = tableData.list1
    this.list2 = tableData.list2
  },
  mounted() {
    this.sortList1()
    this.sortList2()
  },
  methods: {
    sortList1() {
      const el = this.$refs.list1.$el.querySelectorAll('.el-table__body-wrapper > table> tbody')[0]
      Sortable.create(el, {
        ghostClass: 'sortable-ghost',
        sort: false,
        animation: 150,
        group: {
          name: 'person',
          pull: true,
          put: false
        },
        setData: function (
          /** DataTransfer */ dataTransfer,
          /** HTMLElement*/ dragEl
        ) {
          dataTransfer.setData('Text', dragEl.textContent) // `dataTransfer` object of HTML5 DragEvent
        },
        onStart: () => {
          this.useInitData('newlist1', this.list1)// 初始化
        },
        onAdd: (evt) => {
          this.useAddNewData(evt, 'newlist1', this.list1)
        },
        onEnd: (ev) => {
          const data = deepClone(this.list1)
          this.newlist1 = deepClone(this.list1)
          this.list1 = []
          this.$nextTick(() => {
            this.list1 = data
          })
        }
      })
    },
    sortList2() {
      const el = this.$refs.list2.$el.querySelectorAll('.el-table__body-wrapper > table> tbody')[0]
      Sortable.create(el, {
        ghostClass: 'sortable-ghost',
        sort: false,
        animation: 150,
        group: {
          name: 'person',
          pull: true,
          put: true
        },
        setData: function (
          /** DataTransfer */ dataTransfer,
          /** HTMLElement*/ dragEl
        ) {
          dataTransfer.setData('Text', dragEl.textContent)
        },
        onStart: () => {
          this.useInitData('newlist2', this.list2) // 初始化
        },
        onAdd: (evt) => {
          this.useAddNewData(evt, 'newlist2', this.list2)
        },
        onEnd: (ev) => {
          console.log(ev)
          const data = deepClone(this.list2)
          this.newlist2 = deepClone(this.list2)
          this.list2 = []
          this.$nextTick(() => {
            this.list2 = data
          })
        }
      })
    },
    // 封装初始化数据
    useInitData(fromItem, oldData) {
      this.fromItem = fromItem
      this[fromItem] = this.initStatus[fromItem] ? deepClone(oldData) : deepClone(this[fromItem])
      this.initStatus[fromItem] = false
    },
    // 封装添加数据
    useAddNewData(evt, newData, oldData) {
      // 树形结构 深度>广度
      const oldArray = this.flattenData(this[this.fromItem])
      // 添加项
      const item = oldArray[evt.oldIndex]
      // 不知道干什么的
      this[newData] = this.initStatus[newData] ? deepClone(oldData) : deepClone(this[newData])
      this.initStatus[newData] = false
      // 放入拖拽的数据结果集中
      const newArray = this.flattenData(oldData)
      // 通过放入的位置知道父级是谁，将父级添加上,并且id需要随机一个，否则控制台会报错
      if (evt.newIndex - 1 < 0) {
        this.$message({ message: '类型不匹配', type: 'error' })
        return false
      }
      // 判断数据重复
      const reData = newArray.find(e => e.id === item.id)
      if (reData != null) {
        this.$message({ message: '数据重复', type: 'error' })
        return false
      }
      const row = newArray[evt.newIndex - 1]
      item.pid = row.icon ? row.id : row.pid
      // 判断类型是否正确
      const result = newArray.find(e => e.id === item.pid)
      if (result.icon === 5 && item.type === 1 || result.icon === 4 && item.type === 2) {
        // 数据结构化然后放回去
        newArray.splice(evt.newIndex, 0, item)
        oldData = this.structuringData(newArray, 0)
      } else {
        this.$message({ message: '类型不匹配', type: 'error' })
      }
    },
    // 数据扁平化
    flattenData(treeData, childKey = 'children') {
      const arr = []
      const expand = (data) => {
        data.forEach((item) => {
          arr.push(item)
          if (item[childKey] && item[childKey].length > 0) {
            expand(item[childKey])
          }
        })
      }
      expand(treeData)
      return arr
    },
    // 数据结构化
    structuringData(dataList, pid) {
      const tree = []
      for (const node of dataList) {
        if (node.pid === pid) {
          const children = this.structuringData(dataList, node.id)
          if (children.length) {
            node.children = children
          }
          tree.push(node)
        }
      }
      return tree
    },
    // 删除数据
    remove(data, index) {
      if (data != null) {
        const list = this.flattenData(data)
        list.splice(index, 1)
        data = this.structuringData(list, 0)
        this.list2 = data
        deepClone(this.list2)
      }
    },
    // 打印数据
    log() {
      console.log(this.list1, this.list2)
    }
  }
}
</script>

<style lang="scss" scoped>
.wrap {
  display: flex;
}
.div1,
.div2 {
  width: 400px;
  height: 800px;
}
.div2 {
  width: 500px;
  margin-left: 20px;
}
</style>
