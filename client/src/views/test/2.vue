<!-- 专业分数线专业修正 -->
<template>
  <div class="public-box">
    <div class="r_btn">
      <div class="r_title">专业分数线专业修正</div>
    </div>
    <div class="main_content">
      <div class="top_box">
        <p class="content_title">专业分数线专业修正</p>
        <div class="content_selection">
          <div class="year_selection">
            <p class="text_title">年份：</p>
            <el-checkbox-group v-model="yearData">
              <el-checkbox v-for="(item, index) in yearList" :key="index" :label="item" :disabled="isDisabled(item)" />
            </el-checkbox-group>
          </div>
          <div class="province_selection other">
            <p class="text_title">数据省份：</p>
            <div class="radio_box">
              <el-radio-group v-model="provinceId">
                <el-radio v-for="item in provinceList" :key="item.id" :label="item.id">{{ item.name }}</el-radio>
              </el-radio-group>
            </div>
          </div>

        </div>
        <div class="btn_box" @click="queryProfessionInfo">
          <el-button class="update-btn">查询&对比专业信息</el-button>
        </div>
      </div>
      <div v-if="pageDataList && pageDataList.length > 0" class="main-container">
        <div class="table_container">
          <div v-for="(item, index) in pageDataList" :key="index">
            <div v-if="item.firstYearList.length > 0 && item.lastYearList.length > 0">
              <div class="school-name">
                <p>{{ item.universityName }}</p>
              </div>
              <div class="year_box">
                <p class="first_year">年份：<span>{{ item.firstYear }}</span></p>
                <p class="other_tip">数据同步</p>
                <p class="last_year">年份：<span>{{ item.lastYear }}</span></p>
              </div>
              <div class="container">
                <!-- 左侧表格 -->
                <div class="left-table">
                  <el-table :key="isUpdate" :data="item.firstYearList" border tooltip-effect="dark">
                    <el-table-column label="往年修正专业id" align="center">
                      <template slot-scope="scope">
                        <el-input v-if="scope.row.isEdit" v-model="scope.row.lastId"
                                  @input="sgradeInp(scope.$index, scope.row, index)"
                        />
                        <p v-else>{{ scope.row.lastId }}<i class="el-icon-edit"
                                                           style="font-size: 16px;cursor: pointer;padding-left: 10px;"
                                                           @click="getFirstYearData(scope.row, scope.$index)"
                        /></p>
                      </template>
                    </el-table-column>
                    <el-table-column prop="specialityName" label="专业名称" show-overflow-tooltip align="center" />
                    <el-table-column prop="specialityNotes" label="专业备注" show-overflow-tooltip align="center" />
                  </el-table>
                </div>
                <!-- 右侧表格 -->
                <div class="right-table">
                  <el-table :data="item.lastYearList" border>
                    <el-table-column prop="id" label="id" align="center" />
                    <el-table-column prop="specialityName" label="专业名称" show-overflow-tooltip align="center" />
                    <el-table-column prop="specialityNotes" label="专业备注" show-overflow-tooltip align="center" />
                  </el-table>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="operationBtn">
          <el-button size="mini" class="submit-btn">数据修正</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
export default {
  data() {
    return {
      pageDataList: [], // 分页列表数据
      yearList: [], // 年份数据
      provinceList: [], // 省份数据
      yearData: [], // 选中的年份数据
      provinceId: '', // 选中的省份
      pagination: {
        pageSize: 10,
        pageNum: 1,
        total: 0
      }, // 分页数据
      isUpdate: false

    }
  },
  mounted() {
    this.getPageData()
    this.getSelectionData()
  },
  methods: {
    sgradeInp(e, row, index) {
      const result = this.pageDataList[index].firstYearList
      result.forEach((item, index) => {
        if (item.lastId === Number(row.lastId) && item.id !== row.id) {
          alert('此处数据与第' + (index + 1) + '行的数据冲突')
          item.lastId = null
          return false
        }
      })
      const list = Vue.util.extend({}, this.pageDataList[index].firstYearList[e])
      this.$set(this.pageDataList[index].firstYearList, e, list)
    },
    // 获取第一年对应的单元格数据
    getFirstYearData(row, index) {
      row.isEdit = true
      this.isUpdate = !this.isUpdate
    },
    // 选择年份
    isDisabled(item) {
      // 如果已选择的数量大于等于2并且当前项未被选中，则禁用当前项
      return this.yearData.length >= 2 && !this.yearData.includes(item)
    },
    // 查询&对比专业信息
    queryProfessionInfo() {
      if (this.yearData.length > 1 && this.provinceId !== '') {
        this.getPageData()
      } else {
        this.$message.error('请选择年份和省份')
      }
    },
    // 获取分页列表数据
    getPageData() {
      const res = { 'code': 200, 'msg': '成功', 'data': { 'total': 1822, 'list': [{ 'zyScoreId': null, 'universityId': 29, 'zyScoreIds': null, 'universityName': '深圳大学', 'provinceId': null, 'firstYear': 2022, 'lastYear': 2021, 'universityIds': null, 'firstYearList': [{ 'id': 1413, 'sysProZyScoreId': 1414, 'specialityName': '行政管理', 'specialityNotes': '', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 1395, 'sysProZyScoreId': 1396, 'specialityName': '材料科学与工程', 'specialityNotes': '', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 1393, 'sysProZyScoreId': 1394, 'specialityName': '新能源科学与工程', 'specialityNotes': '', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 1396, 'sysProZyScoreId': 1397, 'specialityName': '生物医学工程', 'specialityNotes': '', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 1411, 'sysProZyScoreId': 1412, 'specialityName': '经济学', 'specialityNotes': '(卓越班)', 'universityId': 29, 'time': null, 'lastUpdateId': null }], 'lastYearList': [{ 'id': 30902, 'sysProZyScoreId': 30904, 'specialityName': '预防医学', 'specialityNotes': '5年，7000元/年，在丽湖校区办学', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 30917, 'sysProZyScoreId': 30919, 'specialityName': '建筑学', 'specialityNotes': '5年，6250元/年，在粤海校区办学', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 27210, 'sysProZyScoreId': 27212, 'specialityName': '德语', 'specialityNotes': '德英，4年，6250元/年，在粤海校区办学', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 30909, 'sysProZyScoreId': 30911, 'specialityName': '电子信息工程', 'specialityNotes': '4年，6850元/年，在粤海校区办学', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 30900, 'sysProZyScoreId': 30902, 'specialityName': '环境工程', 'specialityNotes': '4年，6250元/年，在丽湖校区办学', 'universityId': 29, 'time': null, 'lastUpdateId': null }, { 'id': 30905, 'sysProZyScoreId': 30907, 'specialityName': '网络与新媒体', 'specialityNotes': '4年，5500元/年，在粤海校区办学', 'universityId': 29, 'time': null, 'lastUpdateId': null }], 'requestList': null, 'type': null }, { 'zyScoreId': null, 'universityId': 220, 'zyScoreIds': null, 'universityName': '清华大学', 'provinceId': null, 'firstYear': 2022, 'lastYear': 2021, 'universityIds': null, 'firstYearList': [], 'lastYearList': [], 'requestList': null, 'type': null }, { 'zyScoreId': null, 'universityId': 221, 'zyScoreIds': null, 'universityName': '北京大学', 'provinceId': null, 'firstYear': 2022, 'lastYear': 2021, 'universityIds': null, 'firstYearList': [{ 'id': 32, 'sysProZyScoreId': 33, 'specialityName': '城乡规划', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 28, 'sysProZyScoreId': 29, 'specialityName': '公共管理类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 19, 'sysProZyScoreId': 20, 'specialityName': '经济学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 34, 'sysProZyScoreId': 35, 'specialityName': '文科试验班类', 'specialityNotes': '(全校教学资源范围内自由选择专业)', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 24, 'sysProZyScoreId': 25, 'specialityName': '计算机类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 37, 'sysProZyScoreId': 38, 'specialityName': '社会学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 40, 'sysProZyScoreId': 41, 'specialityName': '英语', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 21, 'sysProZyScoreId': 22, 'specialityName': '物理学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 17, 'sysProZyScoreId': 18, 'specialityName': '工商管理类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 36, 'sysProZyScoreId': 37, 'specialityName': '法学', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 18, 'sysProZyScoreId': 19, 'specialityName': '心理学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26, 'sysProZyScoreId': 27, 'specialityName': '人文科学试验班', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 39, 'sysProZyScoreId': 40, 'specialityName': '考古学', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 31, 'sysProZyScoreId': 32, 'specialityName': '国际政治', 'specialityNotes': '(国政、外交学、国际政经)', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 38, 'sysProZyScoreId': 39, 'specialityName': '经济学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 20, 'sysProZyScoreId': 21, 'specialityName': '数学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 33, 'sysProZyScoreId': 34, 'specialityName': '工商管理类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 23, 'sysProZyScoreId': 24, 'specialityName': '生物科学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 25, 'sysProZyScoreId': 26, 'specialityName': '中国语言文学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27, 'sysProZyScoreId': 28, 'specialityName': '信息管理与信息系统', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 29, 'sysProZyScoreId': 30, 'specialityName': '历史学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 30, 'sysProZyScoreId': 31, 'specialityName': '哲学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 35, 'sysProZyScoreId': 36, 'specialityName': '新闻传播学类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 22, 'sysProZyScoreId': 23, 'specialityName': '理科试验班类', 'specialityNotes': '', 'universityId': 221, 'time': null, 'lastUpdateId': null }], 'lastYearList': [{ 'id': 26255, 'sysProZyScoreId': 26257, 'specialityName': '人文科学试验班', 'specialityNotes': '4年，5000元/年，包含专业艺术史论、戏剧与影视学类', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26263, 'sysProZyScoreId': 26265, 'specialityName': '城乡规划', 'specialityNotes': '4年，5000元/年', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27742, 'sysProZyScoreId': 27744, 'specialityName': '计算机类', 'specialityNotes': '4年，5300元/年，包含专业计算机科学与技术、智能科学与技术', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27743, 'sysProZyScoreId': 27745, 'specialityName': '工商管理类', 'specialityNotes': '4年，5000元/年，包含专业金融学、金融学(金融经济学)、会计学、市场营销', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26259, 'sysProZyScoreId': 26261, 'specialityName': '中国语言文学类', 'specialityNotes': '4年，5000元/年，包含专业汉语言文学、汉语言、古典文献学', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26260, 'sysProZyScoreId': 26262, 'specialityName': '英语', 'specialityNotes': '4年，5000元/年', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26258, 'sysProZyScoreId': 26260, 'specialityName': '国际政治', 'specialityNotes': '国政、外交学、国际政经，4年，5000元/年', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26266, 'sysProZyScoreId': 26268, 'specialityName': '公共管理类', 'specialityNotes': '4年，5000元/年，包含专业城市管理、行政管理', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27738, 'sysProZyScoreId': 27740, 'specialityName': '法学', 'specialityNotes': '4年，5000元/年', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27741, 'sysProZyScoreId': 27743, 'specialityName': '电子信息类', 'specialityNotes': '4年，5300元/年，包含专业电子信息科学与技术、微电子科学与工程', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26257, 'sysProZyScoreId': 26259, 'specialityName': '法学', 'specialityNotes': '4年，5000元/年', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26265, 'sysProZyScoreId': 26267, 'specialityName': '工商管理类', 'specialityNotes': '4年，5000元/年，包含专业金融学、金融学(金融经济学)、会计学、市场营销', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26264, 'sysProZyScoreId': 26266, 'specialityName': '信息管理与信息系统', 'specialityNotes': '4年，5000元/年', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26262, 'sysProZyScoreId': 26264, 'specialityName': '考古学', 'specialityNotes': '4年，5000元/年', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26256, 'sysProZyScoreId': 26258, 'specialityName': '经济学类', 'specialityNotes': '4年，5000元/年，包含专业经济学、金融学、国际经济与贸易、保险学(风险管理与保险)、财政学、资源与环境经济学', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27739, 'sysProZyScoreId': 27741, 'specialityName': '数学类', 'specialityNotes': '4年，5000元/年，包含专业数学与应用数学(数学)、数学与应用数学(概率统计)、数学与应用数学(科学与工程计算)、数学与应用数学(信息科学)、数学与应用数学(金融)', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27737, 'sysProZyScoreId': 27739, 'specialityName': '理科试验班类', 'specialityNotes': '元培，4年，5300元/年，包含专业数学类、物理学类、天文学、地球物理学类、心理学类、计算机类、电子信息类', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26261, 'sysProZyScoreId': 26263, 'specialityName': '新闻传播学类', 'specialityNotes': '4年，5000元/年，包含专业新闻学、广播电视学、广告学、编辑出版学', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 27740, 'sysProZyScoreId': 27742, 'specialityName': '生物科学类', 'specialityNotes': '4年，5300元/年，包含专业生物科学、生物技术', 'universityId': 221, 'time': null, 'lastUpdateId': null }, { 'id': 26254, 'sysProZyScoreId': 26256, 'specialityName': '文科试验班类', 'specialityNotes': '元培，4年，5300元/年，包含专业中国语言文学类、考古学、历史学类、哲学类、社会学类、法学、信息管理与信息系统、新闻传播学类、经济学类、工商管理类、公共管理类', 'universityId': 221, 'time': null, 'lastUpdateId': null }], 'requestList': null, 'type': null }, { 'zyScoreId': null, 'universityId': 222, 'zyScoreIds': null, 'universityName': '北京航空航天大学', 'provinceId': null, 'firstYear': 2022, 'lastYear': 2021, 'universityIds': null, 'firstYearList': [{ 'id': 233, 'sysProZyScoreId': 234, 'specialityName': '工科试验班类', 'specialityNotes': '(信息类)', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 235, 'sysProZyScoreId': 236, 'specialityName': '工科试验班类', 'specialityNotes': '(航空H航天类)', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 236, 'sysProZyScoreId': 237, 'specialityName': '理科试验班类', 'specialityNotes': '(华罗庚数学拔尖计划)', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 238, 'sysProZyScoreId': 239, 'specialityName': '社会科学试验班', 'specialityNotes': '', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 234, 'sysProZyScoreId': 235, 'specialityName': '工科试验班类', 'specialityNotes': '(未来空天领军计划)', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 237, 'sysProZyScoreId': 238, 'specialityName': '理科试验班类', 'specialityNotes': '', 'universityId': 222, 'time': null, 'lastUpdateId': null }], 'lastYearList': [{ 'id': 27794, 'sysProZyScoreId': 27796, 'specialityName': '工科试验班类', 'specialityNotes': '医工交叉试验班，4年，5500元/年，在学院路校区办学，包含专业生物医学工程', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 27791, 'sysProZyScoreId': 27793, 'specialityName': '理科试验班类', 'specialityNotes': '4年，5500元/年，在沙河校区办学，包含专业信息管理与信息系统、工业工程、经济统计学、会计学、工商管理、金融学、工程管理、能源经济、数学与应用数学、信息与计算科学、统计学、信息安全、应用物理学、核物理、物理学、环境工程、空间科学与技术、化学、应用化学、化学生物学', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 27790, 'sysProZyScoreId': 27792, 'specialityName': '工科试验班类', 'specialityNotes': '航空航天类，4年，5500元/年，在沙河校区办学，包含专业智能飞行器技术、飞行器控制与信息工程、智能制造工程、飞行器设计与工程、飞行器动力工程、机器人工程、材料科学与工程、纳米材料与技术、能源与动力工程、工程力学、飞行器环境与生命保障工程、机械工程、材料成型及控制工程、飞行器制造工程、微机电系统工程、工业设计、生物医学工程、车辆工程、交通运输、飞行器适航技术、土木工程、飞行器质量与可靠性、安全工程、探测制导与控制技术、无人驾驶航空器系统工程', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 27792, 'sysProZyScoreId': 27794, 'specialityName': '工科试验班类', 'specialityNotes': '未来空天领军计划，4年，5500元/年，在学院路校区办学，包含专业人工智能、智能飞行器技术、智能感知工程、虚拟现实技术、网络空间安全、计算机科学与技术、智能制造工程、飞行器设计与工程、飞行器动力工程、机器人工程、材料科学与工程、能源与动力工程、飞行器环境与生命保障工程、生物医学工程、交通运输、飞行器质量与可靠性、探测制导与控制技术、电子信息工程、通信工程、集成电路设计与集成系统、电磁场与无线技术、软件工程、自动化、信息安全、测控技术与仪器', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 27795, 'sysProZyScoreId': 27797, 'specialityName': '理科试验班类', 'specialityNotes': '华罗庚数学拔尖计划，4年，5500元/年，在学院路校区办学，包含专业数学与应用数学', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 26281, 'sysProZyScoreId': 26283, 'specialityName': '社会科学试验班', 'specialityNotes': '4年，5000元/年，在学院路校区办学，包含专业经济学、行政管理、英语、翻译、德语、法学', 'universityId': 222, 'time': null, 'lastUpdateId': null }, { 'id': 27793, 'sysProZyScoreId': 27795, 'specialityName': '工科试验班类', 'specialityNotes': '信息类，4年，5500元/年，在学院路校区办学，包含专业人工智能、计算机科学与技术、虚拟现实技术、智能感知工程、网络空间安全、电子信息工程、通信工程、电子科学与技术、集成电路设计与集成系统、光电信息科学与工程、电磁场与无线技术、交通运输、软件工程、自动化、电气工程及其自动化、机器人工程、信息对抗技术、信息安全、微电子科学与工程、测控技术与仪器、探测制导与控制技术、遥感科学与技术', 'universityId': 222, 'time': null, 'lastUpdateId': null }], 'requestList': null, 'type': null }, { 'zyScoreId': null, 'universityId': 223, 'zyScoreIds': null, 'universityName': '中国人民大学', 'provinceId': null, 'firstYear': 2022, 'lastYear': 2021, 'universityIds': null, 'firstYearList': [{ 'id': 269, 'sysProZyScoreId': 270, 'specialityName': '工商管理类', 'specialityNotes': '(含国际三学士学位项目、双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 264, 'sysProZyScoreId': 265, 'specialityName': '社会科学试验班', 'specialityNotes': '(管理学科类，含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 272, 'sysProZyScoreId': 273, 'specialityName': '社会科学试验班', 'specialityNotes': '(管理学科类，含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 266, 'sysProZyScoreId': 267, 'specialityName': '金融学类', 'specialityNotes': '(含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 270, 'sysProZyScoreId': 271, 'specialityName': '新闻传播学类', 'specialityNotes': '(含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 273, 'sysProZyScoreId': 274, 'specialityName': '经济学类', 'specialityNotes': '(含双学士学位项目，拔尖人才培养基地)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 262, 'sysProZyScoreId': 263, 'specialityName': '理科试验班', 'specialityNotes': '(基础学科类，含实验班项目、拔尖人才基地)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 261, 'sysProZyScoreId': 262, 'specialityName': '法学', 'specialityNotes': '(含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 267, 'sysProZyScoreId': 268, 'specialityName': '人力资源管理', 'specialityNotes': '(含4加1本硕国际项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 259, 'sysProZyScoreId': 260, 'specialityName': '人力资源管理', 'specialityNotes': '(含4加1本硕国际项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 265, 'sysProZyScoreId': 266, 'specialityName': '经济学类', 'specialityNotes': '(含双学士学位项目，拔尖人才培养基地)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 260, 'sysProZyScoreId': 261, 'specialityName': '工商管理类', 'specialityNotes': '(含国际三学士学位项目、双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 274, 'sysProZyScoreId': 275, 'specialityName': '金融学类', 'specialityNotes': '(含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 271, 'sysProZyScoreId': 272, 'specialityName': '法学', 'specialityNotes': '(含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 263, 'sysProZyScoreId': 264, 'specialityName': '理科试验班类', 'specialityNotes': '(含双学士学位项目，拔尖人才培养基地)', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 268, 'sysProZyScoreId': 269, 'specialityName': '国际政治', 'specialityNotes': '(含双学士学位项目)', 'universityId': 223, 'time': null, 'lastUpdateId': null }], 'lastYearList': [{ 'id': 27748, 'sysProZyScoreId': 27750, 'specialityName': '金融学类', 'specialityNotes': '4年，5000元/年，包含专业金融学金融工程、信用管理、保险学', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 27744, 'sysProZyScoreId': 27746, 'specialityName': '社会科学试验班', 'specialityNotes': '管理学科类，4年，5000元/年，包含专业行政管理、城市管理、土地资源管理、信息资源管理、档案学、农林经济管理、农村区域发展、资源与环境经济学、公共事业管理(环境与资源管理)，含应用经济农村区域发展管理双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26273, 'sysProZyScoreId': 26275, 'specialityName': '工商管理类', 'specialityNotes': '4年，5000元/年，包含专业工商管理、财务管理、会计学、市场营销、管理科学、贸易经济，含工商管理法学双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26272, 'sysProZyScoreId': 26274, 'specialityName': '新闻传播学类', 'specialityNotes': '4年，5000元/年，包含专业新闻学、广播电视学、广告学、传播学、国际新闻与传播，含法学新闻学双学士学位项目、', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 27745, 'sysProZyScoreId': 27747, 'specialityName': '理科试验班', 'specialityNotes': '4年，5000元/年，包含专业统计学、应用统计学、经济统计学、数据科学与大数据技术、计算机科学与技术、信息管理与信息系统、信息安全、软件工程、数学与应用数学、人工智能，含应用经济数据科学双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26267, 'sysProZyScoreId': 26269, 'specialityName': '社会科学试验班', 'specialityNotes': '管理学科类，4年，5000元/年，包含专业行政管理、城市管理、土地资源管理、信息资源管理、档案学、农林经济管理、农村区域发展、资源与环境经济学、公共事业管理(环境与资源管理)，含应用经济农村区域发展管理双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26268, 'sysProZyScoreId': 26270, 'specialityName': '经济学类', 'specialityNotes': '4年，5000元/年，包含专业经济学、国际经济与贸易、能源经济、国民经济管理，含应用经济数据科学双学士学位项目、应用经济农村区域发展管理双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 27747, 'sysProZyScoreId': 27749, 'specialityName': '经济学类', 'specialityNotes': '4年，5000元/年，包含专业经济学国际经济与贸易、能源经济、国民经济管理，含应用经济数据科学双学士学位项目、应用经济农村区域发展管理双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 27749, 'sysProZyScoreId': 27751, 'specialityName': '工商管理类', 'specialityNotes': '4年，5000元/年，包含专业工商管理、财务管理、会计学、市场营销、管理科学、贸易经济，含工商管理法学双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 27746, 'sysProZyScoreId': 27748, 'specialityName': '理科试验班', 'specialityNotes': '基础学科类，4年，5000元/年，包含专业物理学、材料物理、化学、应用心理学、环境科学与工程', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26274, 'sysProZyScoreId': 26276, 'specialityName': '人力资源管理', 'specialityNotes': '4年，5000元/年', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26271, 'sysProZyScoreId': 26273, 'specialityName': '国际政治', 'specialityNotes': '4年，5000元/年', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 27750, 'sysProZyScoreId': 27752, 'specialityName': '人力资源管理', 'specialityNotes': '4年，5000元/年', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26270, 'sysProZyScoreId': 26272, 'specialityName': '法学', 'specialityNotes': '4年，5000元/年，含法学新闻学双学士学位项目、工商管理法学双学士学位项目', 'universityId': 223, 'time': null, 'lastUpdateId': null }, { 'id': 26269, 'sysProZyScoreId': 26271, 'specialityName': '金融学类', 'specialityNotes': '4年，5000元/年，包含专业金融学、金融工程、信用管理、保险学', 'universityId': 223, 'time': null, 'lastUpdateId': null }], 'requestList': null, 'type': null }], 'pageNum': 1, 'pageSize': 5, 'size': 5, 'startRow': 1, 'endRow': 5, 'pages': 365, 'prePage': 0, 'nextPage': 2, 'isFirstPage': true, 'isLastPage': false, 'hasPreviousPage': false, 'hasNextPage': true, 'navigatePages': 8, 'navigatepageNums': [1, 2, 3, 4, 5, 6, 7, 8], 'navigateFirstPage': 1, 'navigateLastPage': 8, 'lastPage': 8, 'firstPage': 1 }, 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
      if (res.code === 200) {
        this.pageDataList = res.data.list
        this.pageDataList.forEach(item => {
          const lastYear = item.lastYearList
          for (let i = 0; i < lastYear.length; i++) {
            if (item.firstYearList.length >= i + 1) {
              item.firstYearList[i].lastId = lastYear[i].id
            }
          }
          for (let j = 0; j < item.firstYearList.length; j++) {
            item.firstYearList[j].isEdit = false
          }
        })
      } else {
        this.$message.error(res.msg)
      }
    },
    // 获取年份 省份数据
    getSelectionData() {
      const res = { 'code': 200, 'msg': '成功', 'data': { 'provinceList': [{ 'id': 1, 'name': '北京市', 'sysCityList': null }, { 'id': 2, 'name': '上海市', 'sysCityList': null }, { 'id': 3, 'name': '天津市', 'sysCityList': null }, { 'id': 4, 'name': '重庆市', 'sysCityList': null }, { 'id': 5, 'name': '香港特别行政区', 'sysCityList': null }, { 'id': 6, 'name': '澳门特别行政区', 'sysCityList': null }, { 'id': 7, 'name': '黑龙江省', 'sysCityList': null }, { 'id': 8, 'name': '吉林省', 'sysCityList': null }, { 'id': 9, 'name': '辽宁省', 'sysCityList': null }, { 'id': 10, 'name': '河北省', 'sysCityList': null }, { 'id': 11, 'name': '陕西省', 'sysCityList': null }, { 'id': 12, 'name': '河南省', 'sysCityList': null }, { 'id': 13, 'name': '山东省', 'sysCityList': null }, { 'id': 14, 'name': '山西省', 'sysCityList': null }, { 'id': 15, 'name': '青海省', 'sysCityList': null }, { 'id': 16, 'name': '甘肃省', 'sysCityList': null }, { 'id': 17, 'name': '安徽省', 'sysCityList': null }, { 'id': 18, 'name': '湖北省', 'sysCityList': null }, { 'id': 19, 'name': '湖南省', 'sysCityList': null }, { 'id': 20, 'name': '江苏省', 'sysCityList': null }, { 'id': 21, 'name': '四川省', 'sysCityList': null }, { 'id': 22, 'name': '贵州省', 'sysCityList': null }, { 'id': 23, 'name': '云南省', 'sysCityList': null }, { 'id': 24, 'name': '浙江省', 'sysCityList': null }, { 'id': 25, 'name': '江西省', 'sysCityList': null }, { 'id': 26, 'name': '广东省', 'sysCityList': null }, { 'id': 27, 'name': '福建省', 'sysCityList': null }, { 'id': 28, 'name': '台湾省', 'sysCityList': null }, { 'id': 29, 'name': '海南省', 'sysCityList': null }, { 'id': 30, 'name': '内蒙古自治区', 'sysCityList': null }, { 'id': 31, 'name': '广西壮族自治区', 'sysCityList': null }, { 'id': 32, 'name': '西藏自治区', 'sysCityList': null }, { 'id': 33, 'name': '宁夏回族自治区', 'sysCityList': null }, { 'id': 34, 'name': '新疆维吾尔自治区', 'sysCityList': null }], 'years': ['2023', '2022', '2021', '2020'] }, 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
      if (res.code === 200) {
        this.yearList = res.data.yearList
        this.provinceList = res.data.provinceList
        this.provinceId = this.provinceList[0].id
      } else {
        this.$message.error(res.msg)
      }
    }

  }
}
</script>

<style lang="scss" scoped>
.main_content {
  // max-height: 760px;
  padding: 20px;

  .top_box {
    .content_title {
      text-align: center;
      font-size: 20px;
      font-weight: 700;
    }

    .content_selection {
      margin-left: 200px;

      .year_selection,
      .province_selection {
        display: flex;
        align-items: center;
        font-size: 16px;
        margin-bottom: 20px;

        .text_title {
          width: 80px;
          text-align: end;
          white-space: nowrap;
        }

        .radio_box {
          width: 1200px;
          max-height: 106px;
          overflow-y: auto;

          .el-radio {
            margin-bottom: 10px;
          }
        }
      }

      .other {
        align-items: flex-start;
      }
    }

    .btn_box {
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .main-container {
    width: 100%;
    margin-top: 20px;
    border: 1px solid #ccc;
    background: #fff;

    .table_container {
      padding: 20px;
      max-height: 600px;
      overflow-y: auto;

      .school-name {
        font-size: 14px;
        font-weight: bold;
      }

      .year_box {
        display: flex;
        align-items: center;
        justify-content: space-around;
        font-size: 14px;
        font-weight: bold;
        margin-bottom: 10px;
      }

      .container {
        display: flex;
        align-items: flex-start;
        justify-content: space-between;

        .left-table,
        .right-table {
          width: 600px;

          .el-input {
            height: 30px;
          }

          .el-input__inner {
            height: 30px;
          }
        }
      }
    }

    .operationBtn {
      padding: 10px 0;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}
</style>
