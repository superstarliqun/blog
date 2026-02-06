<template>
  <div class="content">
    <el-button type="primary" @click="handelFormData()">POST Form Data</el-button>
    <el-button type="primary" @click="handelRequestPayload()">POST Request Payload</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        // list: [
        //   {
        //     fileName: '王五',
        //     fileType: '男',
        //     fileUrl: 28
        //   },
        //   {
        //     fileName: '赵六',
        //     fileType: '女',
        //     fileUrl: 13
        //   },
        //   {
        //     fileName: '张三',
        //     fileType: '男',
        //     fileUrl: 21
        //   },
        //   {
        //     fileName: '李四',
        //     fileType: '女',
        //     fileUrl: 23
        //   }
        // ],
        ids: [1, 2, 3, 4]
      }

    }
  },
  mounted() {

  },
  methods: {
    handelFormData() {
      this.$postForm(this.$urls.test, [1, 2, 3, 4]).then(result => {
        console.log(result)
      })
    },
    handelRequestPayload() {
      this.$post(this.$urls.test, this.array).then(result => {
        console.log(result)
      })
    }
  }
}
</script>

<style>
.content {
  height: 600px;
  width: 100%;
  background-color: rgb(239, 255, 242);
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
