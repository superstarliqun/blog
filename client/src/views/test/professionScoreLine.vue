<!-- 专业分数线 -->
<template>
  <div class="profile-info">
    <p class="profile-title">专业分数线</p>
    <div class="content">
      <div class="query-box flex">
        <div class="flex mr10">
          <span>地区：</span>
          <el-select v-model="param.provinceId" class="zyType" @change="getQuery">
            <el-option v-for="item in provinceList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </div>
        <div class="flex mr10">
          <span>年份：</span>
          <el-select v-model="param.time" class="zyType" @change="changeYear">
            <el-option v-for="(item,index) in yearList" :key="index" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </div>
        <div v-if="subjectTypeList && subjectTypeList.length > 0" class="flex mr10">
          <span>首选：</span>
          <el-select v-model="param.subjectId" class="zyType" @change="changeSubject">
            <el-option v-for="(item,index) in subjectTypeList" :key="index" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </div>
        <div v-if="batchList && batchList.length > 0" class="flex mr10">
          <span>批次：</span>
          <el-select v-model="param.batchId" class="zyType" @change="changeBatch">
            <el-option v-for="(item,index) in batchList" :key="index" :label="item.name" :value="item.name"></el-option>
          </el-select>
        </div>
        <div v-if="specialityGroupList && specialityGroupList.length > 0" class="flex mr10">
          <span>专业组：</span>
          <el-select v-model="param.groupId" class="zyType" @change="getPageData">
            <el-option value="" label="全部专业组"></el-option>
            <el-option v-for="(item,index) in specialityGroupList" :key="index" :label="item" :value="item"></el-option>
          </el-select>
        </div>
      </div>
      <!-- 表格数据 -->
      <div class="mt20">
        <el-table :data="tableData" border tooltip-effect="dark">
          <el-table-column prop="sysSpecialityStr" label="专业名称" width="400px" show-overflow-tooltip>
            <template slot-scope="scope">
              <span class="pr5">{{ scope.row.sysSpecialityStr || "-" }}</span>
              <span>{{ scope.row.sysSpecialityNote || "(暂无专业备注)" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="batchIndex" label="录取批次" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              <span>{{ scope.row.batchIndex || "-" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="avgGrade" label="平均分" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.avgGrade || "-" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="最低分/最低分位次" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.lowGrade || "-" }}/{{ scope.row.lowIndex || "-" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="" label="录取率" align="center">
            <template slot-scope="scope">
              <p class="pointer" style="color: #39a1dc;" @click="testAdmission(scope.row)">录取率</p>
            </template>
          </el-table-column>
          <el-table-column v-if="isShowSubjectAsk" prop="specialityGroup" align="center" show-overflow-tooltip>
            <template slot="header">
              <p>专业组</p>
            </template>
            <template slot-scope="scope">
              <span>{{ scope.row.specialityGroup || "-" }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="isShowSubjectAsk" prop="subjectAsk" label="选科要求" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              <span>{{ scope.row.subjectAsk || "-" }}</span>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <div v-if="tableData && tableData.length > 10" class="me-fx-row-c-c mt20">
          <el-pagination layout="prev, pager, next,total" :page-size="pagination.pageSize" :current-page="pagination.pageNum" :total="pagination.total" @current-change="currentPage">
          </el-pagination>
        </div>
      </div>
    </div>
    <login v-if="isShowLogin" @closeLogin="isShowLogin = false" />
  </div>
</template>

<script>
export default {
  props: {
    uniId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      isShowLogin: false,
      timer: null,
      param: {
        provinceId: '',
        time: '',
        subjectId: '',
        batchId: '',
        groupId: ''
      },
      provinceList: [],
      yearList: [],
      subjectTypeList: [],
      batchList: [],
      specialityGroupList: [],
      tableData: [],
      isShowSubjectAsk: true,
      isShowGroup: true,
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.getQuery()
    // this.getProvinceList(this.uniId, null, 1, res => {
    //   let proName = null
    //   if (this.userStuInfo === null) {
    //     // 用户选择的省份(未登录时)
    //     proName = localStorage.getItem('currentProvince')
    //   } else {
    //     // 已登录
    //     proName = this.userStuInfo.sysProvinceName
    //   }
    //   // 根据用户所选省份获取对应省份id
    //   const initData = res.data.find(item => item.name === proName)
    //   this.provinceList = res.data
    //   if (initData !== undefined) {
    //     this.param.provinceId = initData.id
    //   } else {
    //     this.param.provinceId = proName
    //   }

    // })
  },
  methods: {
    // 切换年份
    changeYear(val) {
      const data = this.yearList.find(item => item.name === val)
      // 科目
      this.getSubjects(data)
      // 批次：
      this.getBatches(data.subjectTypeList, 0)
      // 专业组：
      this.getMajorGroups(data.subjectTypeList[0].batchIndexList, 0)
      this.param.groupId = ''
      this.getPageData()
    },
    // 切换首选
    changeSubject(val) {
      const data = this.subjectTypeList.find(item => item.name === val)
      // 批次
      this.getBatches(data)
      // 专业组：
      this.getMajorGroups(data.batchIndexList, 0)
      this.param.groupId = ''
      this.getPageData()
    },
    // 切换批次
    changeBatch(val) {
      const data = this.batchList.find(item => item.name === val)
      // 专业组：
      this.getMajorGroups(data)
      this.param.groupId = ''
      this.getPageData()
    },
    // 获取科类的逻辑
    getSubjects(data, index) {
      data = index === undefined ? data : data[0]
      const subData = data.subjectTypeList
      if (subData.length > 0) {
        this.subjectTypeList = subData
        this.param.subjectId = subData[0].name
      } else {
        this.subjectTypeList = []
        this.param.subjectId = ''
      }
    },
    // 获取批次的逻辑
    getBatches(data, index) {
      data = index === undefined ? data : data[0]
      const batchData = data.batchIndexList
      if (batchData.length > 0) {
        this.batchList = batchData
        this.param.batchId = batchData[0].name
      } else {
        this.batchList = []
        this.param.batchId = ''
      }
    },
    // 获取专业组的逻辑
    getMajorGroups(data, index) {
      console.log(data, index)
      data = index === undefined ? data : data[0]
      const groupData = data.specialityGroupsList
      if (groupData.length > 0) {
        this.specialityGroupList = groupData
      } else {
        this.specialityGroupList = []
      }
    },
    // 获取其他查询条件
    getQuery() {
      // const url = '/user/university/getSelectOtherCondition'
      // const params = {
      //   search_universityId: this.uniId,
      //   search_provinceId: this.param.provinceId,
      //   search_needScore: 1,
      //   token: localStorage.token
      // }
      // this.reqM1Service(url, params).then(res => {
      const res = this.data1()
      if (res.code === 200) {
        // 初始化时默认年份、科类、批次、专业组
        // 年份
        this.yearList = res.data
        this.param.time = res.data[0].name

        // 科类
        const subData = res.data[0].subjectTypeList
        if (subData.length > 0) {
          this.subjectTypeList = subData
          this.param.subjectId = subData[0].name
        } else {
          this.subjectTypeList = []
          this.param.subjectId = ''
        }

        // 批次：
        const batchData = subData[0].batchIndexList
        if (batchData.length > 0) {
          this.batchList = batchData
          this.param.batchId = batchData[0].name
        } else {
          this.batchList = []
          this.param.batchId = ''
        }

        // 专业组：
        const groupData = batchData[0].specialityGroupsList
        if (groupData.length > 0) {
          this.specialityGroupList = groupData
        } else {
          this.specialityGroupList = []
        }
        this.param.groupId = ''
        this.$nextTick(() => {
          this.getPageData()
        })
      } else {
        this.$message.error(res.msg)
      }
      // }).catch(failResponse => { })
    },
    // 获取分页数据
    getPageData() {
      // const url = '/user/university/getZsListInScorePlan'
      // const params = {
      //   search_universityId: this.uniId,
      //   search_provinceId: this.param.provinceId,
      //   search_time: this.param.time,
      //   search_subjectType: this.param.subjectId,
      //   search_batchIndex: this.param.batchId,
      //   search_specialityGroup: this.param.groupId,
      //   search_needScore: 1,
      //   search_pageSize: this.pagination.pageSize,
      //   search_pageNum: this.pagination.pageNum
      // }
      // this.reqM1Service(url, params).then(res => {
      const res = this.data2()
      if (res.code === 200) {
        // 判断是否显示专业组和选科要求
        this.isShowSubjectAsk = res.data.hasSubjectAsk === 'true'
        this.isShowGroup = res.data.hasGroup === 'true'
        this.timer = new Date()
        const pageData = res.data.pageData
        if (pageData.pageSize > 0) {
          this.pagination.pageSize = pageData.pageSize
        }
        this.pagination.total = pageData.total
        this.tableData = pageData.list
      } else {
        this.$message.error(res.msg)
      }
      // }).catch(failResponse => { })
    },
    // 切换分页
    currentPage(val) {
      this.pagination.pageNum = val
      this.getPageData()
    },
    // 录取概率
    testAdmission(row) {
      if (!this.isLogin) {
        this.isShowLogin = true
        return
      }
      const { href } = this.$router.resolve(
        {
          name: 'tzy',
          query: { universityName: row.universityName }
        })
      window.open(href, '_blank')
    },
    data1() {
      return { 'code': 200, 'msg': '成功', 'data': [{ 'name': 2020, 'subjectTypeList': [{ 'name': '理科', 'batchIndexList': [{ 'specialityGroupsList': [], 'name': '本科批' }] }, { 'name': '文科', 'batchIndexList': [{ 'specialityGroupsList': [], 'name': '本科批' }] }] }, { 'name': 2021, 'subjectTypeList': [{ 'name': '物理', 'batchIndexList': [{ 'specialityGroupsList': ['2973（W999）', '2973（W500）'], 'name': '本科批' }] }, { 'name': '历史', 'batchIndexList': [{ 'specialityGroupsList': ['2973（L999）'], 'name': '本科批' }] }] }, { 'name': 2022, 'subjectTypeList': [{ 'name': '物理', 'batchIndexList': [{ 'specialityGroupsList': ['3005（W999）', '3005（W500）'], 'name': '本科批' }, { 'specialityGroupsList': ['999'], 'name': '本科提前批' }] }, { 'name': '历史', 'batchIndexList': [{ 'specialityGroupsList': ['3005（L999）'], 'name': '本科批' }, { 'specialityGroupsList': ['700'], 'name': '本科提前批' }] }] }, { 'name': 2023, 'subjectTypeList': [{ 'name': '历史', 'batchIndexList': [{ 'specialityGroupsList': ['3022（L999）'], 'name': '本科批' }] }, { 'name': '物理', 'batchIndexList': [{ 'specialityGroupsList': ['3022（W500）', '3022（W999）'], 'name': '本科批' }] }] }], 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
    },
    data2() {
      // eslint-disable-next-line object-curly-spacing
      return { 'code': 200, 'msg': '成功', 'data': { 'hasSubjectAsk': 'false', 'hasGroup': 'false', 'pageData': { 'total': 16, 'list': [{ 'id': 49, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 0, 'sysSpecialitySecondId': 0, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 700, 'highGrade': 700, 'lowGrade': 699, 'lowIndex': 45, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '', 'sysSpecialitySecondStr': '', 'sysSpecialityStr': '理科试验班类', 'sysSpecialityNote': '(新雅书院)(含物理学等各专业)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 55, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 10, 'sysSpecialitySecondId': 70, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 8, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 696, 'highGrade': 696, 'lowGrade': 696, 'lowIndex': 45, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '医学', 'sysSpecialitySecondStr': '临床医学类', 'sysSpecialityStr': '临床医学类', 'sysSpecialityNote': '(协和)(含临床医学)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 52, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 8, 'sysSpecialitySecondId': 37, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 697, 'highGrade': 700, 'lowGrade': 695, 'lowIndex': 45, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '工学', 'sysSpecialitySecondStr': '自动化类', 'sysSpecialityStr': '自动化类', 'sysSpecialityNote': '(含自动化/信息管理与信息系统)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 53, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 8, 'sysSpecialitySecondId': 38, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 701, 'highGrade': 711, 'lowGrade': 694, 'lowIndex': 45, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '工学', 'sysSpecialitySecondStr': '计算机类', 'sysSpecialityStr': '计算机类', 'sysSpecialityNote': '(含计算机科学与技术/软件工程/计算机科学与技术(计算机科学实验班)/计算机科学与技术(智班))', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 45, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 0, 'sysSpecialitySecondId': 0, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 693, 'highGrade': 693, 'lowGrade': 693, 'lowIndex': 45, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '', 'sysSpecialitySecondStr': '', 'sysSpecialityStr': '理科试验班类', 'sysSpecialityNote': '(化生)(含化学/化学生物学/生物科学/药学/化学工程与工业生物工程/高分子材料与工程)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 51, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 8, 'sysSpecialitySecondId': 36, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 696, 'highGrade': 703, 'lowGrade': 692, 'lowIndex': 45, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '工学', 'sysSpecialitySecondStr': '电子信息类', 'sysSpecialityStr': '电子信息类', 'sysSpecialityNote': '(含电子信息科学与技术/微电子科学与工程/生物医学工程)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 46, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 0, 'sysSpecialitySecondId': 0, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 697, 'highGrade': 702, 'lowGrade': 691, 'lowIndex': 61, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '', 'sysSpecialitySecondStr': '', 'sysSpecialityStr': '理科试验班类', 'sysSpecialityNote': '(经济/金融与管理)(含金融学类/信息管理与信息系统)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 44, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 0, 'sysSpecialitySecondId': 0, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 691, 'highGrade': 692, 'lowGrade': 691, 'lowIndex': 61, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '', 'sysSpecialitySecondStr': '', 'sysSpecialityStr': '工科试验班类', 'sysSpecialityNote': '(能源与电气)(含建筑环境与能源应用工程/电气工程及其自动化/电气工程及其自动化(能源互联网国际班)/能源与动力工程)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 54, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 8, 'sysSpecialitySecondId': 57, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 5, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 691, 'highGrade': 692, 'lowGrade': 690, 'lowIndex': 71, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '工学', 'sysSpecialitySecondStr': '建筑类', 'sysSpecialityStr': '建筑类', 'sysSpecialityNote': '(含建筑学/城乡规划/风景园林)', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }, { 'id': 43, 'time': 2020, 'sysUniversityId': 132, 'sysSubjectId': 9, 'sysExamTypeId': 4, 'sysProBatchId': 124, 'specialityGroup': '', 'isSpecialityGroup': 0, 'sysSpecialityFirstId': 0, 'sysSpecialitySecondId': 0, 'sysSpecialityId': 0, 'zsNum': 0, 'eduSystem': 4, 'eduMoney': 5000, 'parentZsPlanId': null, 'rootZsPlanId': null, 'zsPlanSyncState': 1, 'zsScoreSyncState': 4, 'zsPlanSource': null, 'avgGrade': 691, 'highGrade': 692, 'lowGrade': 690, 'lowIndex': 71, 'provinceId': null, 'proSubjectId': null, 'sysSubjectName': null, 'sysProBatchName': null, 'sysSpecialityFirstName': null, 'sysSpecialitySecondName': null, 'sysSpecialityName': null, 'sysProZsPlanChoseSubjectList': null, 'zsPlanGroup': null, 'zsGroupByYearInfo': null, 'matchSubjectName': null, 'matchBatchName': null, 'matchSubjectAskName': null, 'matchSpecialityName': null, 'matchFristName': null, 'matchSecondName': null, 'subjectType': '理科', 'batchIndex': '本科批', 'subjectAsk': '', 'sysSpecialityFirstStr': '', 'sysSpecialitySecondStr': '', 'sysSpecialityStr': '工科试验班类', 'sysSpecialityNote': '(机械/航空与动力)(含机械工程/机械工程(实验班)/测控技术与仪器/能源与动力工程/车辆工程/车辆工程(电子信息方向)/车辆工程(车身方向)/工业工程/航空航天类/工程力学(钱学森力学班))', 'universityType': '', 'rsCode': '5708', 'universityName': '清华大学', 'sysProZsPlanInfo': null, 'diffZsPlans': null, 'dataType': null, 'lowGradeGroup': null, 'lowIndexGroup': null, 'provinceBatchScore': null, 'mateType': null, 'matchState': false, 'batchIndexChange': null, 'batchIndexLevelIndex': null, 'probability': null }], 'pageNum': 1, 'pageSize': 10, 'size': 10, 'startRow': 1, 'endRow': 10, 'pages': 2, 'prePage': 0, 'nextPage': 2, 'isFirstPage': true, 'isLastPage': false, 'hasPreviousPage': false, 'hasNextPage': true, 'navigatePages': 8, 'navigatepageNums': [1, 2], 'navigateFirstPage': 1, 'navigateLastPage': 2, 'firstPage': 1, 'lastPage': 2 } }, 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-info {
  margin-top: 20px;
  border: 1px solid #dcdcdc;
  .profile-title {
    color: #2a2a2a;
    font-size: 16px;
    font-weight: 700;
    padding: 15px 30px;
    background: #f9f9f9;
    border-bottom: 1px solid #dcdcdc;
  }
  .content {
    padding: 15px;
    color: #2a2a2a;
    line-height: 30px;
    font-size: 14px;
    .query-box {
      width: 100%;
      .zyType {
        width: 140px;
      }
    }
    .el-table {
      width: 100%;
      .el-table__cell {
        height: 40px;
        padding: 0;
      }
      .is-leaf {
        color: #2a2a2a;
        background: #f9f9f9;
      }
    }
  }
}
</style>
