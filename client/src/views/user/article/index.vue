<template>
  <div>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="title" label="文章标题" show-overflow-tooltip width="400" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="createTime" label="修改时间" width="180" />
      <el-table-column prop="summary" label="简介" show-overflow-tooltip />
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" plain @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :page.sync="page" :limit.sync="limit" pager-count="3" background layout="next, pager, prev, total"
      :total="total" @current-change="handleChange" />
  </div>

</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      total: 0,
      page: 1,
      limit: 10
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    // 获取文章列表
    requestData() {
      this.$get(this.$urls.articleMyPage, null, [this.page, this.limit]).then((result) => {
        console.log(result)
        this.tableData = result.data.records
        this.total = result.data.total
        this.page = result.data.current
        this.size = result.data.size
      }).catch(() => {
        console.log('错误!')
      })
    },
    handleChange(val) {
      console.log(val)
      this.page = val
      this.requestData()
    },
    // 文章删除
    handleDelete(id) {
      this.$confirm('此操作将永久删该文章, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$post(this.$urls.articleRemove, { id: id }).then(res => {
          if (res.code === 0) {
            this.requestData()
            this.$message.success('删除成功')
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep.el-pagination {
  display: flex;
  flex-direction: row-reverse;
  margin: 10px 0;
}
</style>
