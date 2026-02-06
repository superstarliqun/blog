/**
 * @author Liu Yin
 * @date 2020/2/11
 * @Description: 列表页面混入
 */

export default {
  data() {
    return {
      // 是否展示新增编辑弹窗
      showDialog: false,
      showUser: false,
      userTitle: '',
      // 弹窗标题
      dialogTitle: '',
      // table列表数据
      tableData: [],
      // 是否展示详情
      showDetail: false,
      // 查询总条数
      total: 0,
      // 条目数据
      rowData: null,
      // 条目ID
      rowId: null,
      // 当前页数
      currentPage: 1,
      // 查询条数
      size: 10,
      // 搜索条件
      searchForm: {},
      // 批量选择
      multipleSelection: []
    }
  },
  methods: {
    // 新增
    handleAdd() {
      this.dialogTitle = '新增'
      this.rowData = null
      this.showDialog = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑'
      this.rowData = this.clone(row)
      this.showDialog = true
    },
    // 详情
    handleShowDetail(id) {
      this.rowId = id
      this.showDetail = true
    },
    // 翻页
    handleCurrentChanges(val) {
      this.currentPage = val
      this.requestData()
    },
    // 删除提醒
    warningRemove(url, id) {
      this.$confirm('此操作将删除该信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.handleRemove(url, id)
        })
        .catch((e) => {})
    },
    // 删除
    handleRemove(url, id) {
      this.toggleLoading(true)
      this.$post(url, { id: id })
        .then((result) => {
          this.toggleLoading(false)
          if (result.code === 0) {
            this.$message.success('删除成功')
            this.requestData()
          } else {
            this.$message.error(result.message || result.msg)
          }
        })
        .catch(() => {
          this.toggleLoading(false)
        })
    },
    // 执行搜索
    handleSearch() {
      this.currentPage = 1
      this.requestData()
    },
    // 清空搜索条件
    clearSearchForm() {
      this.searchForm = {}
      this.requestData()
    },
    // 存储当前滚动位置
    setScrollPosition() {
      const scrollY = window.scrollY
      this.$store.dispatch('app/setScrollY', scrollY)
      window.scrollTo(0, 0)
    },
    // 滚动到指定位置
    scrollToPosition() {
      const scrollY = this.$store.getters.scrollY
      this.$nextTick(() => {
        window.scrollTo(0, scrollY)
      })
    },
    /**
     * 批量删除提醒
     * @param url 接口
     * @param key id 对应的可以\
     */
    handleBatchWarning(url, key) {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('此操作将批量删除已选数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.handleBatchRemove(url, key)
        })
        .catch((e) => {})
    },
    /**
     * 批量删除
     * @param url 接口
     * @param key id 对应的可以
     */
    handleBatchRemove(url, key) {
      const ids = this.multipleSelection.reduce((all, item, index) => {
        if (index < this.multipleSelection.length - 1) {
          return all + item[key] + ','
        } else {
          return all + item[key]
        }
      }, '')
      this.toggleLoading(true)
      this.$post(url, { ids })
        .then((result) => {
          this.toggleLoading(false)
          if (result.code === 0) {
            this.$message.success('删除成功')
            this.requestData()
          } else {
            this.$message.error(result.message || result.msg)
          }
        })
        .catch(() => {
          this.toggleLoading(false)
        })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    }
  }
}
