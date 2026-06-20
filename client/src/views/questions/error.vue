<template>
  <super-container>
    <h1>
      错题集合
      <el-button type="primary" plain @click="requestData">查询</el-button>
    </h1>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column
        prop="questionId"
        label="题目编号"
        show-overflow-tooltip
        width="80"
      />
      <el-table-column prop="question" label="题目" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="创建时间" width="170" />
      <el-table-column prop="updatedAt" label="更新时间" width="170">
        <template slot-scope="scope">
          {{ scope.row.updatedAt || '-' }}
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click.native.prevent="open(scope.row)"
          >
            查看答案
          </el-button>
          <el-button
            type="text"
            style="color: red"
            size="small"
            @click.native.prevent="remove(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :page.sync="page"
      :limit.sync="limit"
      background
      layout="prev, pager, next"
      :total="total"
      @current-change="handleChange"
    />
    <!-- 答案弹出框 -->
    <el-dialog
      title="错题弹出框"
      :visible.sync="dialogVisible"
      width="70%"
      :before-close="handleClose"
    >
      <span>{{ formData.questionId }}、</span>
      <span v-for="(item, index) in formData.questionList" :key="index">
        {{ item }}
        <span style="color: red">{{ formData.answer.split('、')[index] }}</span>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </super-container>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      limit: 10,
      param: {},
      dialogVisible: false,
      formData: {}
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    requestData() {
      this.param = {
        pageNo: this.page,
        pageSize: this.limit
      }
      this.$get(this.$urls.getQuestionsError, this.param).then((result) => {
        if (result.data != null) {
          this.tableData = result.data.list
          this.total = result.data.total
        }
      })
    },
    open(row) {
      this.formData = row
      this.formData.questionList = this.formData.question.split('__')
      this.dialogVisible = true
    },
    // 删除错误
    remove(row) {
      this.$post(this.$urls.removeQuestionsError + '/' + row.id, null).then(
        (result) => {
          if (result.code === 0) {
            this.requestData()
            this.$message.success('删除成功')
          }
        }
      )
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleChange(val) {
      this.page = val
      this.requestData()
    }
  }
}
</script>

<style lang="scss" scoped>
super-container {
  max-width: 1440px;
  padding: 0 1.5rem;
  height: 100%;
  margin: 0 auto;
  display: block;
}

.question-list {
  display: flex;
  gap: 20px;
  margin: 0 auto;
  justify-content: center;

  li {
    background-color: #ddebfd;
    height: 200px;
    width: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    font-size: 28px;
    cursor: pointer;
  }
}

// ::v-deep.el-pagination {
//     display: flex;
//     flex-direction: row-reverse;
//     margin: 10px 0;
// }
</style>
