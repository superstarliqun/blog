<template>
  <el-dialog title="发布文章" :visible.sync="dialogVisible" width="40%" :before-close="handleClose">
    <el-form ref="formData" :model="formData" label-width="80px">
      <el-form-item label="文章标签">
        <el-tag v-for="tag in dynamicTags" :key="tag" closable :disable-transitions="false"
                @close="handleCloseTag(tag)"
        >
          {{ tag }}
        </el-tag>
        <el-input v-if="inputVisible" ref="saveTagInput" v-model="inputValue" class="input-new-tag"
                  :class="dynamicTags.length > 0 ? 'marginleft10' : ''" size="small" @keyup.enter.native="handleInputConfirm"
                  @blur="handleInputConfirm"
        />
        <el-button v-else class="button-new-tag" :class="dynamicTags.length > 0 ? 'marginleft10' : ''" size="small"
                   @click="showInput"
        >+ 添加标签</el-button>
      </el-form-item>
      <el-form-item label="摘要">
        <el-input v-model="formData.summary" type="textarea" :rows="6" placeholder="请输入摘要" />
      </el-form-item>
      <el-form-item label="专栏">
        <el-select v-model="formData.category" placeholder="请选择专栏" style="width: 100%;">
          <el-option v-for="item in articleCategory" :key="item.id" :label="item.categoryName" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="loading" @click="save">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    row: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: true,
      formData: {},
      // 文章专栏集合
      articleCategory: [],
      loading: false,
      dynamicTags: [],
      inputVisible: false,
      inputValue: ''
    }
  },
  mounted() {
    this.requestCategory()
  },
  methods: {
    save() {
      this.loading = true
      this.formData.title = this.row.title
      this.formData.content = this.row.content
      this.formData.tagsList = this.dynamicTags
      this.$post(this.$urls.articleSave, this.formData).then(result => {
        if (result.code === 0) {
          this.$message.success('发布成功！')
          // 如果发布成功需要删除草稿箱内容
          localStorage.removeItem('backContent')
          setTimeout(() => {
            this.$router.push('/')
          }, 1000)
        } else {
          this.$message.error(result.message || result.msg)
          this.loading = false
          this.$emit('close')
        }
      }).catch(() => {
        this.$message.error('')
        this.loading = false
        this.$emit('close')
      })
    },
    // 获取文章分类
    requestCategory() {
      this.$get(this.$urls.getDefaultCategory, null).then(res => {
        this.articleCategory = res.data
      })
      this.formData = this.row
      this.dynamicTags = this.row.tagsList || []
    },
    handleClose() {
      this.$emit('close')
    },
    handleCloseTag(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
    },
    showInput() {
      this.inputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleInputConfirm() {
      const inputValue = this.inputValue.trim()
      if (inputValue) {
        if (this.dynamicTags.includes(inputValue)) {
          this.$message.warning('标签内容重复') // 给予用户反馈
        } else {
          this.dynamicTags.push(inputValue)
        }
      }
      this.inputVisible = false
      this.inputValue = ''
    }
  }
}
</script>

<style>
.el-tag+.el-tag {
  margin-left: 10px;
}

.marginleft10 {
  margin-left: 10px;
}

.button-new-tag {
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  vertical-align: bottom;
}
</style>
