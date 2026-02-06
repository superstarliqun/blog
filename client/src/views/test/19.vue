<template>
    <div>
        <div v-model="onlyFile" @change="uploadChange"></div>
        <div v-model="multFIle" @change="uploadChange"></div>
    </div>
</template>

<script>
export default {

    data() {
        return {
            onlyFile: [],
            multFIle: [],
            tempFileId: null,
            addFIle: [],
            delFile: []
        }
    },
    created() {
        this.requestData()
    },
    methods: {
        beforupload() {
            // 如果上传单一文件中存在文件的情况下
            if (this.onlyFile.length > 0) {
                this.addFIle.forEach((item) => {
                    if (item.id === this.onlyFile[0].id) {
                        item.remove()
                    }
                })
                this.addFIle.替换掉当前fileID = this.onlyFile[0].id
                this.delFile.push(this.onlyFile[0].id)
            }
        },
        uploadChange(e) {
            this.addFIle.push('1')
        },
        submit() {

        }
    }

}
</script>

<style></style>
