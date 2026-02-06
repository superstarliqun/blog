<template>
  <div>
    <!-- <el-table :data="tableData" :span-method="arraySpanMethod" border style="width: 100%"> -->
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="id" label="学校名称" width="180">
        <template slot-scope="{row}">
          {{ row.name + row.logoUrl }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="招生区域" width="180">
      </el-table-column>
      <el-table-column prop="specialityGroup" label="专业组别" width="180" class="aaa">
        <template slot-scope="{row}">
          <div class="borderP">
            <p v-for="item in row.list" :key="item.key">
              {{ item.specialityGroup }}
            </p>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="recruitSpeciality" label="招生专业">
        <template slot-scope="{row}">
          <div class="borderP">
            <p v-for="item in row.list" :key="item.key">
              {{ item.recruitSpeciality }}
            </p>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="entryTime" label="报名时间" width="180">
      </el-table-column>
      <el-table-column prop="amount3" label="资料查看" width="180">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: []
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    requestData() {
      const treeData = this.data1().data
      this.tableData = treeData
    },

    data1() {
      return { 'code': 200, 'msg': '成功', 'data': [{ 'recruitAreas': '全国', 'materialUrl': 'https://www.ccuut.edu.cn/xxcj', 'entryTime': '4月20日至4月30日', 'recruitUrl': 'https://www.gotopku.cn/index/detail/1335.html', 'name': '北京大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10001', 'list': [{ 'id': 1, 'name': '北京大学', 'logoUrl': '/upload/university/img/133/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '理工类 (Ⅰ组)', 'recruitSpeciality': '数学类、物理学类、化学类、力学类、生物科学类、历史学类、考古学、哲学类、中国语言文学类（古文字学方向）', 'entryTime': '4月20日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10001', 'materialUrl': 'https://www.ccuut.edu.cn/xxcj', 'recruitUrl': 'https://www.gotopku.cn/index/detail/1335.html', 'universityId': 133 }, { 'id': 2, 'name': '北京大学', 'logoUrl': '/upload/university/img/133/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '文史类 (Ⅱ组)', 'recruitSpeciality': '历史学类、考古学、哲学类、中国语言文学类（古文字学方向）', 'entryTime': '4月20日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10001', 'materialUrl': 'https://www.ccuut.edu.cn/xxcj', 'recruitUrl': 'https://www.gotopku.cn/index/detail/1335.html', 'universityId': 133 }, { 'id': 3, 'name': '北京大学', 'logoUrl': '/upload/university/img/133/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '医学组', 'recruitSpeciality': '基础医学（八年制）', 'entryTime': '4月20日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10001', 'materialUrl': 'https://www.ccuut.edu.cn/xxcj', 'recruitUrl': 'https://www.gotopku.cn/index/detail/1335.html', 'universityId': 133 }], 'logoUrl': '/upload/university/img/133/logo.jpeg' }, { 'recruitAreas': '北京、天津、河北、山西、辽宁、吉林、黑龙江、上海、江苏、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、重庆、四川、云南、陕西', 'materialUrl': '#', 'entryTime': '4月8日至4月30日', 'recruitUrl': 'https://rdzs.ruc.edu.cn/cms/item/1911.html', 'name': '中国人民大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10002', 'list': [{ 'id': 4, 'name': '中国人民大学', 'logoUrl': '/upload/university/img/136/logo.jpeg', 'recruitAreas': '北京、天津、河北、山西、辽宁、吉林、黑龙江、上海、江苏、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、重庆、四川、云南、陕西', 'specialityGroup': '文史类', 'recruitSpeciality': '哲学类、汉语言文学（古文字学方向）、历史学类', 'entryTime': '4月8日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10002', 'materialUrl': '#', 'recruitUrl': 'https://rdzs.ruc.edu.cn/cms/item/1911.html', 'universityId': 136 }], 'logoUrl': '/upload/university/img/136/logo.jpeg' }, { 'recruitAreas': '全国', 'materialUrl': 'https://admission.join-tsinghua.edu.cn', 'entryTime': '4月20日至4月30日', 'recruitUrl': 'https://join-tsinghua.edu.cn/info/1033/1768.htm', 'name': '清华大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10003', 'list': [{ 'id': 5, 'name': '清华大学', 'logoUrl': '/upload/university/img/132/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '理工类', 'recruitSpeciality': '数学与应用数学、物理学、化学、生物科学、信息与计算科学、数理基础科学、化学生物学、理论与应用力学', 'entryTime': '4月20日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10003', 'materialUrl': 'https://admission.join-tsinghua.edu.cn', 'recruitUrl': 'https://join-tsinghua.edu.cn/info/1033/1768.htm', 'universityId': 132 }, { 'id': 6, 'name': '清华大学', 'logoUrl': '/upload/university/img/132/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '文史类', 'recruitSpeciality': '中国语言文学类（古文字学方向）、历史学类、哲学类专业', 'entryTime': '4月20日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10003', 'materialUrl': 'https://admission.join-tsinghua.edu.cn', 'recruitUrl': 'https://join-tsinghua.edu.cn/info/1033/1768.htm', 'universityId': 132 }], 'logoUrl': '/upload/university/img/132/logo.jpeg' }, { 'recruitAreas': '全国', 'materialUrl': '#', 'entryTime': '4月11日至4月30日', 'recruitUrl': 'https://zs.buaa.edu.cn/info/1008/2871.htm', 'name': '北京航空航天大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10006', 'list': [{ 'id': 7, 'name': '北京航空航天大学', 'logoUrl': '/upload/university/img/134/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '理工类', 'recruitSpeciality': '数学与应用数学、信息与计算科学、应用物理学、化学、工程力学、飞行器动力工程', 'entryTime': '4月11日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10006', 'materialUrl': '#', 'recruitUrl': 'https://zs.buaa.edu.cn/info/1008/2871.htm', 'universityId': 134 }], 'logoUrl': '/upload/university/img/134/logo.jpeg' }, { 'recruitAreas': '北京、天津、河北、山西、内蒙古、辽宁、吉林、黑龙江、上海、江苏、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、广西、重庆、四川、贵州、云南、陕西、宁夏、新疆', 'materialUrl': '#', 'entryTime': '4月8日至30日', 'recruitUrl': 'https://admission.bit.edu.cn/f/newsCenter/article/f01f55f6fc0e4e5ab03bc7650ea6c206', 'name': '北京理工大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10007', 'list': [{ 'id': 8, 'name': '北京理工大学', 'logoUrl': '/upload/university/img/135/logo.jpeg', 'recruitAreas': '北京、天津、河北、山西、内蒙古、辽宁、吉林、黑龙江、上海、江苏、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、广西、重庆、四川、贵州、云南、陕西、宁夏、新疆', 'specialityGroup': '理工类', 'recruitSpeciality': '数学与应用数学、应用物理学、工程力学、智能无人系统技术、化学', 'entryTime': '4月8日至30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10007', 'materialUrl': '#', 'recruitUrl': 'https://admission.bit.edu.cn/f/newsCenter/article/f01f55f6fc0e4e5ab03bc7650ea6c206', 'universityId': 135 }], 'logoUrl': '/upload/university/img/135/logo.jpeg' }, { 'recruitAreas': '北京、天津、河北、山西、内蒙古、辽宁、吉林、黑龙江、江苏、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、广西、重庆、四川、云南、陕西、甘肃', 'materialUrl': '#', 'entryTime': '4月10日至4月30日', 'recruitUrl': 'https://jwzs.cau.edu.cn/art/2023/4/10/art_42844_942518.html', 'name': '中国农业大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10019', 'list': [{ 'id': 9, 'name': '中国农业大学', 'logoUrl': '/upload/university/img/139/logo.jpeg', 'recruitAreas': '北京、天津、河北、山西、内蒙古、辽宁、吉林、黑龙江、江苏、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、广西、重庆、四川、云南、陕西、甘肃', 'specialityGroup': '#', 'recruitSpeciality': '生物科学、生物育种科学', 'entryTime': '4月10日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10019', 'materialUrl': '#', 'recruitUrl': 'https://jwzs.cau.edu.cn/art/2023/4/10/art_42844_942518.html', 'universityId': 139 }], 'logoUrl': '/upload/university/img/139/logo.jpeg' }, { 'recruitAreas': '北京、天津、河北、山西、辽宁、吉林、黑龙江、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、广西、重庆、四川、云南、新疆', 'materialUrl': '#', 'entryTime': '4月11日至30日', 'recruitUrl': 'https://admission.bnu.edu.cn/tslx/qjjh/a1506e67f0c34e00b25e4cdbc2c75fe7.html', 'name': '北京师范大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10027', 'list': [{ 'id': 10, 'name': '北京师范大学', 'logoUrl': '/upload/university/img/137/logo.jpeg', 'recruitAreas': '北京、天津、河北、山西、辽宁、吉林、黑龙江、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、广西、重庆、四川、云南、新疆', 'specialityGroup': '文史类', 'recruitSpeciality': '历史学、哲学', 'entryTime': '4月11日至30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10027', 'materialUrl': '#', 'recruitUrl': 'https://admission.bnu.edu.cn/tslx/qjjh/a1506e67f0c34e00b25e4cdbc2c75fe7.html', 'universityId': 137 }, { 'id': 11, 'name': '北京师范大学', 'logoUrl': '/upload/university/img/137/logo.jpeg', 'recruitAreas': '北京、天津、河北、山西、辽宁、吉林、黑龙江、浙江、安徽、福建、江西、山东、河南、湖北、湖南、广东、广西、重庆、四川、云南、新疆', 'specialityGroup': '理工类', 'recruitSpeciality': '数学与应用数学、物理学、化学、生物科学', 'entryTime': '4月11日至30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10027', 'materialUrl': '#', 'recruitUrl': 'https://admission.bnu.edu.cn/tslx/qjjh/a1506e67f0c34e00b25e4cdbc2c75fe7.html', 'universityId': 137 }], 'logoUrl': '/upload/university/img/137/logo.jpeg' }, { 'recruitAreas': '北京、河北、辽宁、江苏、浙江、福建、安徽、山东、河南、湖南、四川、云南、陕西', 'materialUrl': '#', 'entryTime': '4月10日至4月30日', 'recruitUrl': 'https://zb.muc.edu.cn/content/zs/bkyk/44e85037-905b-11ea-a561-6c92bf4353bb.htm', 'name': '中央民族大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10052', 'list': [{ 'id': 12, 'name': '中央民族大学', 'logoUrl': '/upload/university/img/159/logo.jpeg', 'recruitAreas': '北京、河北、辽宁、江苏、浙江、福建、安徽、山东、河南、湖南、四川、云南、陕西', 'specialityGroup': '文史类', 'recruitSpeciality': '历史学、哲学', 'entryTime': '4月10日至4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10052', 'materialUrl': '#', 'recruitUrl': 'https://zb.muc.edu.cn/content/zs/bkyk/44e85037-905b-11ea-a561-6c92bf4353bb.htm', 'universityId': 159 }], 'logoUrl': '/upload/university/img/159/logo.jpeg' }, { 'recruitAreas': '全国', 'materialUrl': '#', 'entryTime': '4月11日至4月28日', 'recruitUrl': 'https://zsb.nankai.edu.cn/2023-04-10/1600.html', 'name': '南开大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10055', 'list': [{ 'id': 13, 'name': '南开大学', 'logoUrl': '/upload/university/img/1281/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '文史类', 'recruitSpeciality': '历史学、哲学、汉语言文学（古文字学）', 'entryTime': '4月11日至4月28日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10055', 'materialUrl': '#', 'recruitUrl': 'https://zsb.nankai.edu.cn/2023-04-10/1600.html', 'universityId': 1281 }, { 'id': 14, 'name': '南开大学', 'logoUrl': '/upload/university/img/1281/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '理工类', 'recruitSpeciality': '数学与应用数学、物理学、化学、生物科学', 'entryTime': '4月11日至4月28日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10055', 'materialUrl': '#', 'recruitUrl': 'https://zsb.nankai.edu.cn/2023-04-10/1600.html', 'universityId': 1281 }], 'logoUrl': '/upload/university/img/1281/logo.jpeg' }, { 'recruitAreas': '全国', 'materialUrl': '#', 'entryTime': '4月10日-4月30日', 'recruitUrl': 'https://zs.tju.edu.cn/info/1116/2132.htm', 'name': '天津大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10056', 'list': [{ 'id': 15, 'name': '天津大学', 'logoUrl': '/upload/university/img/1292/logo.jpeg', 'recruitAreas': '全国', 'specialityGroup': '理工类', 'recruitSpeciality': '数学与应用数学、应用物理学、应用化学、生物科学、工程力学', 'entryTime': '4月10日-4月30日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10056', 'materialUrl': '#', 'recruitUrl': 'https://zs.tju.edu.cn/info/1116/2132.htm', 'universityId': 1292 }], 'logoUrl': '/upload/university/img/1292/logo.jpeg' }, { 'recruitAreas': '全国', 'materialUrl': '#', 'entryTime': '4月19日至5月4日', 'recruitUrl': 'https://zs.dlut.edu.cn/info/1050/2536.htm', 'name': '大连理工大学', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10141', 'list': [{ 'id': 16, 'name': '大连理工大学', 'logoUrl': '', 'recruitAreas': '全国', 'specialityGroup': '理工类', 'recruitSpeciality': '数学与应用数学、应用物理学、工程力学、应用化学、生物工程', 'entryTime': '4月19日至5月4日', 'entryUrl': 'https://bm.chsi.com.cn/jcxkzs/sch/10141', 'materialUrl': '#', 'recruitUrl': 'https://zs.dlut.edu.cn/info/1050/2536.htm', 'universityId': 2453 }], 'logoUrl': '' }], 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
    }
  }
}
</script>

<style lang="scss" scoped>
.borderP {
  p {
    border-bottom: 1px solid #ebeef5;
    padding: 0 10px 10px;
  }
  p:last-child {
    border-bottom: 0px solid #ebeef5;
    padding: 0 10px;
  }
}
::v-deep td.el-table_1_column_3.el-table__cell > .cell {
  padding-left: 0px;
  padding-right: 0px;
}
::v-deep td.el-table_1_column_4.el-table__cell > .cell {
  padding-left: 0px;
  padding-right: 0px;
}
</style>
