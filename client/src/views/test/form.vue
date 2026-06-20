<template>
  <div>
    <el-form ref="piecemealForm" :model="piecemealForm" :rules="piecemealRules" label-width="90px">
      <div v-for="(item, index) in piecemealData" :key="index" class="whole_box">
        <el-form-item :label="'名称'+(index+1)" :prop="item.name">
          <el-input v-model="item.name" />
        </el-form-item>
        <el-form-item label="文件上传：" :prop="item.file">
          <el-upload class="upload" action="#" :file-list="item.fileList" :show-file-list="false" :on-change="handleChange(index)" :on-remove="handleRemove">
            <el-input v-model="item.fileName" />
          </el-upload>
        </el-form-item>
        <el-button v-if="piecemealData.length != index+1" size="mini" class="update-btn" @click="handleData(index,1)">-</el-button>
        <el-button v-else size="mini" class="update-btn" @click="handleData(index,2)">+</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      piecemealForm: {},
      piecemealRules: {},
      piecemealData: [
        { name: null, fileList: [], fileName: null }
      ]
    }
  },
  methods: {
    // 假如这是上传成功
    handleChange(index) {
      return (file, fileList) => {
        this.piecemealData[index].fileName = file.name
        this.piecemealData[index].fileList
      }
    },
    handleRemove() { },
    handleData(index, status) {
      if (status === 1) {
        this.piecemealData.splice(index, 1)
      } else {
        this.piecemealData.push({ name: null, fileList: [], fileName: null })
      }
    },
    abc(file, index) {
      this.filelist.push(file)
      this.piecemealData[index].fileList = this.filelist
    }
  }
}
</script>

<style>
.whole_box {
  display: flex;
  align-items: center;
  margin-top: 10px;
}
.el-button--mini {
  margin-bottom: 21px;
  margin-left: 10px;
}
</style>
