<template>
  <div>
    <el-table :data="tableData" row-key="id" :row-class-name="tableRowClassName">
      <el-table-column v-for="(item, index) in col" :key="`col_${index}`" :prop="item.prop" :label="item.label" :width="item.prop == 'sort'?'100px':''">
        <!-- 自定义渲染模版 -->
        <template slot-scope="scope">
          <!-- 当列key为 sort 时，渲染一个手柄icon -->
          <template v-if="item.prop =='sort'">
            <div class="handle_move">
              <i :class="scope.row['sort']" />
            </div>
          </template>
          <template v-else>
            <span style="margin-left: 10px">{{ scope.row[`${item.prop}`] }}</span>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import Sortable from 'sortablejs'
export default {
  data () {
    return {
      tableData: [
        {
          id: '1',
          date: '2016-05-02',
          name: '王小虎1',
          address: '上海市普陀区金沙江路 100 弄',
          sort: 'el-icon-d-caret'
        },
        {
          id: '2',
          date: '2016-05-04',
          name: '王小虎2',
          address: '上海市普陀区金沙江路 200 弄',
          sort: 'el-icon-d-caret'
        },
        {
          id: '3',
          date: '2016-05-01',
          name: '王小虎3',
          address: '上海市普陀区金沙江路 300 弄',
          sort: 'el-icon-d-caret'
        },
        {
          id: '4',
          date: '2016-05-03',
          name: '王小虎4',
          address: '上海市普陀区金沙江路 400 弄',
          sort: 'el-icon-d-caret'
        }
      ],
      oldIndex: null,
      newIndex: null,
      col: [
        { label: '日期', prop: 'date' },
        { label: '姓名', prop: 'name' },
        { label: '地址', prop: 'address' },
        { label: '排序', prop: 'sort' }
      ]
    }
  },
  mounted () {
    // 页面挂载完成，开始创建拖拽
    this.init()
  },
  methods: {
    init () {
      const el = document.querySelector('.el-table__body-wrapper tbody')
      var ops = {
        animation: 1000,
        // 拖动结束
        onEnd: function (evt) {
          console.log(evt)
          // 获取拖动后的排序
          var arr = sortable.toArray()
          alert(JSON.stringify(arr))
        }
      }
      // 初始化
      var sortable = Sortable.create(el, ops)
    },
    // 目标位置高亮
    // 给目标行 添加高亮样式
    tableRowClassName ({ row, rowIndex }) {
      if (rowIndex === this.newIndex) {
        return 'warning-row'
      }
      return ''
    }
  }

}
</script>

<style scoped>
.table {
  padding: 20px;
}
.handle_move {
  cursor: move;
  width: 100px;
}

.list {
  width: 550px;
  display: flex;
  flex-wrap: wrap;
}
.list .item {
  width: 130px;
  height: 130px;
  border: #000 solid 1px;
  flex-shrink: 0;
  margin: 20px;
}
</style>
