<!-- 新增&修改志愿表 -->
<template>
  <div class="public_box">
    <el-dialog :title="dialogTitle" :visible="true" width="1200px" :before-close="handleClose">
      <el-form ref="volunteerForm" :model="volunteerForm" label-width="90px" :rules="rules">
        <div class="basic_choose">
          <el-form-item label="省份：" prop="sysProvinceId">
            <el-select v-model="volunteerForm.sysProvinceId" @change="getQueryProvince">
              <el-option v-for="item in batchProList" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <p class="info"> 年份：<span>{{ paramObj.yearText }}</span>
          </p>
        </div>
        <div v-if="batchListArr && batchListArr.length > 0" class="container">
          <div v-for="(item,index) in batchListArr" :key="index" class="moudle_container">
            <el-form :ref="'ruleFormRef'+index" :model="batchListArr[index]" label-width="90px" :rules="rules">
              <p class="batch-type">批次：<span>{{ item.name }}</span></p>
              <div class="moudle_volunteer">
                <!-- 删除按钮 -->
                <div class="close-icon" @click="handleDelete(index)">
                  <i class="el-icon-circle-close icon"></i>
                </div>
                <div class="radio-container">
                  <el-form-item label="志愿投档模式：" prop="voluntMode">
                    <div class="radio-box">
                      <el-radio-group :key="timer" v-model="item.voluntMode" @change="changeRadio">
                        <el-radio :label="1">平行志愿</el-radio>
                        <el-radio :label="2">顺序志愿</el-radio>
                      </el-radio-group>
                    </div>
                  </el-form-item>
                  <el-form-item label="志愿填报单位：" prop="voluntUnit">
                    <div class="radio-box">
                      <el-radio-group :key="timer" v-model="item.voluntUnit" @change="changeRadio">
                        <el-radio :label="1">院校专业组</el-radio>
                        <el-radio :label="2">专业类+学校</el-radio>
                      </el-radio-group>
                    </div>
                  </el-form-item>
                </div>
                <div class="radio3">
                  <el-form-item label="是否志愿组形式：" prop="isGroup">
                    <el-radio-group :key="timer" v-model="item.isGroup" @change="changeRadio">
                      <el-radio :label="1">是</el-radio>
                      <el-radio :label="0">否</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </div>
                <el-form-item label="志愿数量：" prop="voluntNum">
                  <el-input v-model="item.voluntNum" maxlength="30"></el-input>
                </el-form-item>
                <el-form-item label="志愿组专业数量：" prop="voluntGroupSpecialityNum">
                  <el-input v-if="item.isGroup === 0" v-model="initNum" readonly disabled></el-input>
                  <el-input v-else v-model="item.voluntGroupSpecialityNum" maxlength="30"></el-input>
                </el-form-item>
                <div class="radio3">
                  <el-form-item label="调剂志愿：" prop="adjustVolunt">
                    <el-radio-group :key="timer" v-model="item.adjustVolunt" @change="changeRadio">
                      <el-radio :label="0">无</el-radio>
                      <el-radio :label="1">有</el-radio>
                      <el-radio :label="2">多选</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </div>
              </div>
            </el-form>

          </div>
        </div>
        <div v-else class="noData" style="margin:30px auto;">
          <p>暂无批次数据</p>
        </div>
      </el-form>
      <div class="operationBtn">
        <el-button size="mini" class="submit-btn" @click="submitVolunteerForm">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    paramObj: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      ruleForm: [],
      resultArr: [],
      initNum: 0,
      batchProList: [], // 省份
      batchListArr: [], // 批次列表
      timer: null,
      volunteerForm: {
        sysProvinceId: '', // 省份
        token: localStorage.rootToken,
        uid_token: this.paramObj.uidToken
      }, // 表单
      rules: {
        sysProvinceId: [
          { required: true, message: '请选择省份', trigger: 'blur' }
        ],
        voluntNum: [
          { required: true, message: '请填写志愿数量', trigger: 'blur' },
          { pattern: /^[0-9]\d*$/, message: '请输入数值', trigger: 'blur' }
        ],
        voluntGroupSpecialityNum: [
          { required: true, message: '请填写志愿组专业数量', trigger: 'blur' },
          { pattern: /^[0-9]\d*$/, message: '请输入数值', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.paramObj.tipSuccess === 1 ? '新增志愿表' : '编辑志愿表'
    }
  },
  mounted() {
    this.queryProvince()
    if (this.paramObj.tipSuccess === 2) {
      // 编辑时才调用回显接口
      this.getEchoData()
    }
  },
  methods: {
    checkForm(formName) {
      const that = this
      console.log(formName, 'formName')
      const result = new Promise((resolve, reject) => {
        that.$refs[formName][0].validate((valid) => {
          if (valid) {
            resolve()
          } else {
            reject()
          }
        })
      })
      console.log('校验结果', result)
      that.resultArr.push(result) // push 得到promise的结果
    },
    // 提交志愿表单
    submitVolunteerForm() {
      console.log(this.batchListArr)
      this.batchListArr.forEach((el, index) => {
        this.checkForm(`ruleFormRef${index}`) // 验证多个表单
      })

      Promise.all(this.resultArr)
        .then((values) => {
          // 多个表单都通过验证
          console.log('通过')
        })
        .catch((error) => {
          // 打印拒绝的具体错误信息
          console.error('驳回:', error)
        })
      this.resultArr = []
      // this.$refs.volunteerForm.validate(valid => {
      //   if (valid) {
      //     if (this.paramObj.tipSuccess === 1) {
      //       // 新增保存
      //       this.handleAddForm()
      //     } else {
      //       // 编辑保存
      //       this.handleEditingForm()
      //     }
      //   } else {
      //     return false
      //   }
      // })
    },
    // 编辑回显数据
    getEchoData() {
      const url = '/admin/sysProVoluntTable/getById'
      const params = {
        id: this.paramObj.volunteerId
      }
      this.reqM1Service(url, params)
        .then((res) => {
          if (res.code === 200) {
            res.data.name = res.data.sysProBatchName
            // 回显弹窗数据
            this.batchListArr.push(res.data)
            this.volunteerForm = res.data
            this.volunteerForm.sysProvinceId = res.data.sysProvinceId
            this.volunteerId = res.data.id
            this.$forceUpdate()
          } else {
            this.$message.error(res.msg)
          }
        })
        .catch((failResponse) => {})
    },
    // 获取批次列表
    getBatchList() {
      // const url = '/admin/sysProBatch/getList'
      // const params = {
      //   search_provinceId: this.volunteerForm.sysProvinceId,
      //   search_time: this.paramObj.yearText,
      //   token: localStorage.rootToken
      // }
      // // ->调用第一个接口的请求服务
      // this.reqM1Service(url, params).then(res => {
      const res = this.data2()
      if (res.code === 200) {
        this.batchListArr = res.data
        this.batchListArr.forEach((item) => {
          if (item.voluntMode === undefined) {
            item.voluntMode = 1
          }
          if (item.voluntUnit === undefined) {
            item.voluntUnit = 1
          }
          if (item.isGroup === undefined) {
            item.isGroup = 1
          }
          if (item.adjustVolunt === undefined) {
            item.adjustVolunt = 1
          }
        })
      } else {
        this.$message.error(res.msg)
      }
      // })
    },
    // 省份查询
    queryProvince() {
      // const url = '/admin/sysCity/getAllProvinceData'
      // const params = {
      //   token: localStorage.rootToken
      // }
      // // ->调用第一个接口的请求服务
      // this.reqM1Service(url, params).then(res => {
      const res = this.data1()
      if (res.code === 200) {
        this.batchProList = res.data
        // 父组件省份id有值时赋值给表单省份sysProvinceId，否则sysProvinceId为空(新增时操作)
        if (
          this.paramObj.provinceId !== '0' &&
          this.paramObj.tipSuccess !== 2
        ) {
          this.volunteerForm.sysProvinceId = this.paramObj.provinceId
          this.getBatchList()
        }
      } else {
        this.$message.error(res.msg)
      }
      // })
    },
    // 弹窗中省份切换
    getQueryProvince() {
      this.getBatchList()
    },
    // 删除新增弹窗中的志愿表
    handleDelete(index) {
      this.batchListArr.splice(index, 1)
    },
    // 刷新单选按钮属性
    changeRadio() {
      this.timer = new Date().getTime()
    },

    // 新增保存
    handleAddForm() {
      const dataArr = []
      this.batchListArr.forEach((item) => {
        if (item.isGroup === 0) {
          item.voluntGroupSpecialityNum = 0
        }
        const obj = {
          sysProvinceId: item.sysProvinceId,
          sysProBatchId: item.id,
          voluntTime: item.time,
          voluntNum: Number(item.voluntNum),
          voluntGroupSpecialityNum: Number(item.voluntGroupSpecialityNum),
          voluntMode: item.voluntMode,
          voluntUnit: item.voluntUnit,
          isGroup: item.isGroup,
          adjustVolunt: item.adjustVolunt
        }
        dataArr.push(obj)
      })
      const isVoluntNum = dataArr.every(
        (item) => item.voluntNum !== undefined && !isNaN(item.voluntNum)
      )
      const isSpecialtyNum = dataArr.every(
        (item) =>
          item.voluntGroupSpecialityNum !== undefined &&
          !isNaN(item.voluntGroupSpecialityNum)
      )
      // 验证志愿数量和志愿组专业数量不能为空并且只能为数值
      if (isVoluntNum === true && isSpecialtyNum === true) {
        // 用于新增志愿表传参赋值
        this.volunteerForm.sysProVoluntTableArray = dataArr
        const url = '/admin/sysProVoluntTable/save'
        const params = { ...this.volunteerForm }
        this.reqM1Service(url, params)
          .then((res) => {
            if (res.code === 200) {
              if (this.paramObj.tipSuccess === 1) {
                this.$message.success('添加成功!')
              } else {
                this.$message.success('编辑成功!')
              }
              this.$parent.getPageList()
              this.handleClose()
            } else {
              this.$message.error(res.msg)
            }
            this.operateDialog = false
          })
          .catch((failResponse) => {})
      } else if (isVoluntNum === false) {
        this.$message.error('志愿数量不能为空并且只能输入数值')
      } else if (isSpecialtyNum === false) {
        this.$message.error('志愿组专业数量不能为空并且只能输入数值')
      }
    },
    // 编辑保存
    handleEditingForm() {
      const url = '/admin/sysProVoluntTable/update'
      this.volunteerForm.sysProVoluntTableArray = []
      if (this.volunteerForm.isGroup === 0) {
        this.volunteerForm.voluntGroupSpecialityNum = 0
      }
      const params = { ...this.volunteerForm }
      const pattern = /^\d+$/
      if (
        params.voluntNum === null ||
        params.voluntGroupSpecialityNum === null ||
        !pattern.test(params.voluntNum) ||
        !pattern.test(params.voluntGroupSpecialityNum)
      ) {
        this.$message.error('志愿数量不能为空并且只能输入数值')
        return false
      } else {
        this.reqM1Service(url, params)
          .then((res) => {
            if (res.code === 200) {
              if (this.paramObj.tipSuccess === 1) {
                this.$message.success('添加成功!')
              } else {
                this.$message.success('编辑成功!')
              }
              this.$parent.getPageList()
              this.handleClose()
            } else {
              this.$message.error(res.msg)
            }
            this.operateDialog = false
          })
          .catch((failResponse) => {})
      }
    },
    handleClose() {
      this.$emit('close')
    },
    data1() {
      return {
        code: 200,
        msg: '成功',
        data: [
          { id: 1, name: '北京市', sysCityList: null },
          { id: 2, name: '上海市', sysCityList: null },
          { id: 3, name: '天津市', sysCityList: null },
          { id: 4, name: '重庆市', sysCityList: null },
          { id: 5, name: '香港特别行政区', sysCityList: null },
          { id: 6, name: '澳门特别行政区', sysCityList: null },
          { id: 7, name: '黑龙江省', sysCityList: null },
          { id: 8, name: '吉林省', sysCityList: null },
          { id: 9, name: '辽宁省', sysCityList: null },
          { id: 10, name: '河北省', sysCityList: null },
          { id: 11, name: '陕西省', sysCityList: null },
          { id: 12, name: '河南省', sysCityList: null },
          { id: 13, name: '山东省', sysCityList: null },
          { id: 14, name: '山西省', sysCityList: null },
          { id: 15, name: '青海省', sysCityList: null },
          { id: 16, name: '甘肃省', sysCityList: null },
          { id: 17, name: '安徽省', sysCityList: null },
          { id: 18, name: '湖北省', sysCityList: null },
          { id: 19, name: '湖南省', sysCityList: null },
          { id: 20, name: '江苏省', sysCityList: null },
          { id: 21, name: '四川省', sysCityList: null },
          { id: 22, name: '贵州省', sysCityList: null },
          { id: 23, name: '云南省', sysCityList: null },
          { id: 24, name: '浙江省', sysCityList: null },
          { id: 25, name: '江西省', sysCityList: null },
          { id: 26, name: '广东省', sysCityList: null },
          { id: 27, name: '福建省', sysCityList: null },
          { id: 28, name: '台湾省', sysCityList: null },
          { id: 29, name: '海南省', sysCityList: null },
          { id: 30, name: '内蒙古自治区', sysCityList: null },
          { id: 31, name: '广西壮族自治区', sysCityList: null },
          { id: 32, name: '西藏自治区', sysCityList: null },
          { id: 33, name: '宁夏回族自治区', sysCityList: null },
          { id: 34, name: '新疆维吾尔自治区', sysCityList: null }
        ],
        rootToken: null,
        uidtoken: null,
        rootSessionId: null
      }
    },
    data2() {
      return {
        code: 200,
        msg: '成功',
        data: [
          {
            id: 85,
            sysProvinceId: 2,
            time: 2023,
            name: '本科批',
            batchZsMateName: '',
            batchSocreMatchName: '',
            sysProBatchLine: null,
            examType: null,
            batchScore: null,
            specialityLevelId: 1,
            specialityLevelName: '本科'
          }
        ],
        rootToken: null,
        uidtoken: null,
        rootSessionId: null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-form {
  padding-right: 10px;
  padding-left: 10px;
  padding-top: 10px;
  .el-input {
    width: 200px;
  }
  .el-form-item__content {
    display: flex;
    align-items: center;
  }
  .el-form-item__label {
    white-space: nowrap;
    margin-right: 40px;
  }
  .basic_choose {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-left: 10px;
    .el-input {
      width: 100px;
    }
    .el-input--suffix {
      width: 100px;
    }
    .el-input__inner {
      width: 100px;
    }
    .el-form-item__label {
      white-space: nowrap;
      margin-right: 0px;
    }
    .el-form-item {
      margin-bottom: 0px !important;
    }
    .info {
      font-size: 16px;
      color: #000;
      padding-left: 100px;
    }
  }
  .container {
    padding-left: 100px;
    margin-bottom: 10px;
    margin-top: 10px;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    max-height: 570px;
    overflow-y: auto;
    .moudle_container {
      width: 465px;
      margin-right: 50px;
      .batch-type {
        padding: 10px 0;
        font-size: 14px;
        font-weight: 700;
        color: #000;
      }
      .moudle_volunteer {
        padding: 10px;
        border: 1px solid #ccc;
        .close-icon {
          text-align: end;
          cursor: pointer;
          .icon {
            font-size: 22px;
            color: red;
          }
        }
        .radio-container {
          margin-top: 10px;
          .radio-box {
            width: 300px;
            border: 1px solid #ccc;
            padding: 0 15px;
            .el-radio__label {
              padding-left: 0;
            }
          }
        }
        .radio3 {
          .el-form-item {
            display: flex;
            align-items: center;
            margin-bottom: 0 !important;
          }
          .el-form-item__content {
            margin-left: 0 !important;
          }
        }
      }
    }
  }
}
</style>
