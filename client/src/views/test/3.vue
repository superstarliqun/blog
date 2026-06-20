<template>
  <div class="studentRanking">
    <!-- <teacherLayout3></teacherLayout3> -->
    <div class="stu-ranking">
      <div class="info-head">
        <div class="info-left">
          <p>学生排名</p>
        </div>
        <div class="info-center">
          <div class="info-showtop">
            <div class="info-exp">
              <p>
                <span class="info-title">实验名称：</span>
                <span>{{ expData.expName }}</span>
              </p>
            </div>
            <div class="info-exp">
              <p>
                <span class="info-title">用户在线：</span>
                <span>学生</span>
              </p>
            </div>
            <div class="info-exp">
              <p>
                <span class="info-title">产品：</span>
                <span>{{ expData.productName }}</span>
              </p>
            </div>
            <div class="info-exp">
              <p>
                <span class="info-title">实验状态：</span>
                <span>{{ expData.expStateWord }}</span>
              </p>
            </div>
            <div v-if="expData.expTime && expData.expCyc" class="info-exp">
              <p>
                <span class="info-title">进度：</span>
              </p>
              <p class="progress-bar">
                <el-progress v-if=" !isNaN(parseInt((expData.expTime / expData.expCyc) * 100)) " :text-inside="true" :stroke-width="8" :percentage="parseInt(expData.expTime / expData.expCyc) * 100" :format="format" />
              </p>
            </div>
          </div>
        </div>
        <div class="info-right">
          <div class="back-list" @click="backExpList">
            <!-- <img src="../../../static/teacher/images/back_list.png"> -->
            <span>返回实验列表</span>
          </div>
        </div>
      </div>
      <!-- 时间选择 -->
      <div class="selet-info">
        <div class="experiment-condition">
          <p class="caption">时间选择：</p>
          <!-- <commonSelect :list="monthsData" :value.sync="search_expTime" class="select-condition" @change="getStudentRankingList" /> -->
        </div>
        <!-- 导出成绩 -->
        <el-button type="primary" @click="exportExcel">导出成绩</el-button>
      </div>
      <!-- 表格数据 -->
      <div class="table-list">
        <div class="table-head">
          <div class="table-td">排名</div>
          <div class="table-td">学生名</div>
          <div class="table-td">公司名</div>
          <div class="table-td">现金+固定资产</div>
          <div class="table-td">直销销售额</div>
          <div class="table-td">分销销售额</div>
          <div class="table-td">招投标销售额</div>
          <div class="table-td">产品值</div>
          <div class="table-td">利润</div>
          <div class="table-td other2">现金+固定资产得分</div>
          <div class="table-td">直销销售额得分</div>
          <div class="table-td">分销销售额得分</div>
          <div class="table-td">招投标销售额得分</div>
          <div class="table-td">产品值得分</div>
          <div class="table-td">利润得分</div>
          <div class="table-td">总分</div>
        </div>
        <template v-if="rankingListData&&rankingListData.length > 0">
          <div v-for="(item, index) in rankingListData" :key="index" class="table-content">
            <div class="table-td">{{ item.pm }}</div>
            <div class="table-td">{{ item.studentName }}</div>
            <div class="table-td">{{ item.companyName }}</div>
            <div class="table-td">{{ item.cash }}</div>
            <div class="table-td">{{ item.salesShop }}</div>
            <div class="table-td">{{ item.salesDistributor }}</div>
            <div class="table-td">{{ item.salesBid }}</div>
            <div class="table-td">{{ item.productValue }}</div>
            <div class="table-td">{{ item.profit }}</div>
            <div class="table-td">{{ item.cashScore }}</div>
            <div class="table-td">{{ item.salesShopScore }}</div>
            <div class="table-td">{{ item.salesDistributorScore }}</div>
            <div class="table-td">{{ item.salesBidScore }}</div>
            <div class="table-td">{{ item.productValueScore }}</div>
            <div class="table-td">{{ item.profitScore }}</div>
            <div class="table-td">{{ item.sumScore }}</div>
          </div>
        </template>
        <!-- 暂无实验 -->
        <div v-else class="noexp">
          <div>
            <!-- <img src="../../../static/teacher/images/noexp.png"> -->
          </div>
          <p>暂无数据</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import _ from 'lodash'
import XLSX from 'xlsx'
import FileSaver from 'file-saver'
import XLSXS from 'xlsx-js-style'
export default {
  name: 'StudentRanking',
  data() {
    return {
      expId: this.$route.query.expId,
      expTime: Number(this.$route.query.expTime),
      expData: {}, // 实验环境数据
      monthsData: [], // 时间数组
      search_expTime: Number(this.$route.query.expTime), // 选中时间
      rankingListData: {}// 存放学生排名数据
    }
  },
  created() {
    for (let i = this.expTime; i >= 1; i--) {
      const obj = { value: i, label: i + '月' }
      this.monthsData.push(obj)
    }
    this.getStudentRankingList()
  },
  methods: {
    // 导出表格
    exportExcel() {
      const headers = ['排名', '学生名', '公司名', '现金+固定资产', '直销销售额', '分销销售额', '招投标销售额', '产品值', '利润', '现金+固定资产得分', '直销销售额得分', '分销销售额得分', '招投标销售额得分', '产品值得分', '利润得分', '总分']
      const sheet1data = []
      sheet1data.push(headers)
      _.each(this.rankingListData, (item) => {
        sheet1data.push([
          item.pm,
          item.studentName,
          item.companyName,
          item.cash,
          item.salesShop,
          item.salesDistributor,
          item.salesBid,
          item.productValue,
          item.profit,
          item.cashScore,
          item.salesShopScore,
          item.salesDistributorScore,
          item.salesBidScore,
          item.productValueScore,
          item.profitScore,
          item.sumScore
        ])
      })
      const wb = XLSX.utils.book_new()
      const ws = XLSX.utils.aoa_to_sheet(sheet1data)
      XLSX.utils.book_append_sheet(wb, ws, 'Sheet1') // 将数据添加到工作薄
      // 设置单元格宽度
      ws['!cols'] = [{ wch: 10 }, { wch: 30 }, { wch: 30 }, { wch: 30 }, { wch: 40 }, { wch: 40 }, { wch: 15 }, { wch: 15 }, { wch: 15 }, { wch: 10 }, { wch: 10 }, { wch: 10 }, { wch: 10 }, { wch: 10 }, { wch: 10 }, { wch: 10 }]
      ws['!rows'] = [{ hpx: 30 }]
      // 可以遍历全部单元格,进行样式设置
      console.log(ws)
      for (const i in ws) {
        if (i !== '!cols' && i !== '!rows' && i !== '!ref') {
          ws[i].s = {
            // 居中
            alignment: {
              horizontal: 'center',
              vertical: 'center',
              wrapText: true
            }
          }
        }
      }
      // 导出Excel, 注意这里用到的是XLSXS对象
      const wbout = XLSXS.write(wb, { bookType: 'xlsx', bookSST: false, type: 'binary' })
      const fileName = `【${this.expData.expName}】` + `【${this.search_expTime}月份】` + '实验排名'
      FileSaver.saveAs(new Blob([this.s2ab(wbout)], { type: 'application/octet-stream' }), `${fileName}.xlsx`)
    },
    s2ab(s) {
      const buf = new ArrayBuffer(s.length)
      const view = new Uint8Array(buf)
      for (let i = 0; i !== s.length; ++i) { view[i] = s.charCodeAt(i) & 0xFF }
      return buf
    },
    // 返回实验列表
    backExpList() {
      this.$router.push({ path: '/teacher/experiment/expList' })
    },
    // 计算进度条进度
    format() {
      return `${this.expData.expTime}/${this.expData.expCyc}`
    },
    // 获取学生排名数据
    getStudentRankingList() {
      const res = { 'code': 200, 'msg': '成功', 'data': [{ 'id': 1508, 'expId': 11, 'companyId': 207, 'expTime': 7, 'cash': 1239326012.12, 'salesShop': 1602033697.00, 'salesDistributor': 7750923.00, 'salesBid': 662321.00, 'productValue': 294.00, 'profit': 780083224.12, 'cashScore': 15.00, 'salesShopScore': 15.00, 'salesDistributorScore': 7.35, 'salesBidScore': 4.35, 'productValueScore': 5.00, 'profitScore': 35.00, 'sumScore': 81.70, 'companyName': 'zz1', 'studentName': '朱星一', 'pm': 1 }, { 'id': 1495, 'expId': 11, 'companyId': 176, 'expTime': 7, 'cash': 743613182.38, 'salesShop': 981697142.50, 'salesDistributor': 9449999.70, 'salesBid': 1137120.00, 'productValue': 294.00, 'profit': 519189914.38, 'cashScore': 9.00, 'salesShopScore': 9.15, 'salesDistributorScore': 9.00, 'salesBidScore': 7.35, 'productValueScore': 5.00, 'profitScore': 23.10, 'sumScore': 62.60, 'companyName': '果源', 'studentName': '龙季涛', 'pm': 2 }, { 'id': 1515, 'expId': 11, 'companyId': 216, 'expTime': 7, 'cash': 200794720.62, 'salesShop': 324495006.00, 'salesDistributor': 15725917.60, 'salesBid': 2279047.00, 'productValue': 294.00, 'profit': 155195858.62, 'cashScore': 2.40, 'salesShopScore': 3.00, 'salesDistributorScore': 15.00, 'salesBidScore': 15.00, 'productValueScore': 5.00, 'profitScore': 6.65, 'sumScore': 47.05, 'companyName': '玉衡Alioth', 'studentName': '王一鸣', 'pm': 3 }, { 'id': 1499, 'expId': 11, 'companyId': 185, 'expTime': 7, 'cash': 577101999.00, 'salesShop': 799439617.50, 'salesDistributor': 138766.50, 'salesBid': 222000.00, 'productValue': 294.00, 'profit': 525840912.00, 'cashScore': 6.90, 'salesShopScore': 7.35, 'salesDistributorScore': 0.00, 'salesBidScore': 1.35, 'productValueScore': 5.00, 'profitScore': 23.45, 'sumScore': 44.05, 'companyName': '春光食品', 'studentName': '陈之讯', 'pm': 4 }, { 'id': 1491, 'expId': 11, 'companyId': 171, 'expTime': 7, 'cash': 830480880.70, 'salesShop': 1236990493.10, 'salesDistributor': 0.00, 'salesBid': 923472.00, 'productValue': 294.00, 'profit': 178544546.70, 'cashScore': 10.05, 'salesShopScore': 11.55, 'salesDistributorScore': 0.00, 'salesBidScore': 6.00, 'productValueScore': 5.00, 'profitScore': 7.70, 'sumScore': 40.30, 'companyName': '破烂大王', 'studentName': '叶佳权', 'pm': 5 }, { 'id': 1497, 'expId': 11, 'companyId': 182, 'expTime': 7, 'cash': 409060099.20, 'salesShop': 552418822.20, 'salesDistributor': 1968325.00, 'salesBid': 684560.00, 'productValue': 294.00, 'profit': 370780647.20, 'cashScore': 4.95, 'salesShopScore': 5.10, 'salesDistributorScore': 1.80, 'salesBidScore': 4.50, 'productValueScore': 5.00, 'profitScore': 16.45, 'sumScore': 37.80, 'companyName': '颂祁股份有限公司', 'studentName': '哈又嘉', 'pm': 6 }, { 'id': 1503, 'expId': 11, 'companyId': 199, 'expTime': 7, 'cash': 431078762.98, 'salesShop': 573967489.00, 'salesDistributor': 6992447.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 317168582.98, 'cashScore': 5.10, 'salesShopScore': 5.25, 'salesDistributorScore': 6.60, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 14.00, 'sumScore': 35.95, 'companyName': '谢源集团', 'studentName': '周世雄', 'pm': 7 }, { 'id': 1513, 'expId': 11, 'companyId': 214, 'expTime': 7, 'cash': 353046763.00, 'salesShop': 492526720.00, 'salesDistributor': 0.00, 'salesBid': 377234.00, 'productValue': 294.00, 'profit': 326685580.00, 'cashScore': 4.20, 'salesShopScore': 4.50, 'salesDistributorScore': 0.00, 'salesBidScore': 2.40, 'productValueScore': 5.00, 'profitScore': 14.35, 'sumScore': 30.45, 'companyName': '白帝食品有限公司', 'studentName': '王子轩', 'pm': 8 }, { 'id': 1498, 'expId': 11, 'companyId': 184, 'expTime': 7, 'cash': 734323007.22, 'salesShop': 976905292.00, 'salesDistributor': 187839.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 90989693.22, 'cashScore': 8.85, 'salesShopScore': 9.00, 'salesDistributorScore': 0.15, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 3.85, 'sumScore': 26.85, 'companyName': 'hh', 'studentName': '朱朝捷', 'pm': 9 }, { 'id': 1512, 'expId': 11, 'companyId': 213, 'expTime': 7, 'cash': 279701420.90, 'salesShop': 382955757.90, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 257347122.90, 'cashScore': 3.30, 'salesShopScore': 3.45, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 11.20, 'sumScore': 22.95, 'companyName': 'BBQ果蔬公司', 'studentName': '田秋枫', 'pm': 10 }, { 'id': 1507, 'expId': 11, 'companyId': 206, 'expTime': 7, 'cash': 351157785.25, 'salesShop': 493653419.50, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 173740238.25, 'cashScore': 4.20, 'salesShopScore': 4.50, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 7.70, 'sumScore': 21.40, 'companyName': '如意', 'studentName': '刘顺鑫', 'pm': 11 }, { 'id': 1511, 'expId': 11, 'companyId': 212, 'expTime': 7, 'cash': 293547863.20, 'salesShop': 415875923.20, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 244.00, 'profit': 216210560.20, 'cashScore': 3.45, 'salesShopScore': 3.75, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 4.10, 'profitScore': 9.45, 'sumScore': 20.75, 'companyName': '华东果蔬公司', 'studentName': '揭晴悦', 'pm': 12 }, { 'id': 1496, 'expId': 11, 'companyId': 180, 'expTime': 7, 'cash': 192726438.00, 'salesShop': 260689733.00, 'salesDistributor': 0.00, 'salesBid': 505000.00, 'productValue': 294.00, 'profit': 136636570.00, 'cashScore': 2.25, 'salesShopScore': 2.40, 'salesDistributorScore': 0.00, 'salesBidScore': 3.30, 'productValueScore': 5.00, 'profitScore': 5.95, 'sumScore': 18.90, 'companyName': '易得股份有限公司', 'studentName': '吕思珂', 'pm': 13 }, { 'id': 1502, 'expId': 11, 'companyId': 194, 'expTime': 7, 'cash': 214092646.00, 'salesShop': 276341495.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 178619775.00, 'cashScore': 2.55, 'salesShopScore': 2.55, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 7.70, 'sumScore': 17.80, 'companyName': '小白', 'studentName': '郑颖霞', 'pm': 14 }, { 'id': 1518, 'expId': 11, 'companyId': 231, 'expTime': 7, 'cash': 168236742.55, 'salesShop': 224336052.50, 'salesDistributor': 316880.00, 'salesBid': 49870.00, 'productValue': 294.00, 'profit': 140105557.55, 'cashScore': 1.95, 'salesShopScore': 2.10, 'salesDistributorScore': 0.30, 'salesBidScore': 0.30, 'productValueScore': 5.00, 'profitScore': 5.95, 'sumScore': 15.60, 'companyName': '小猫果蔬', 'studentName': '何唐翼', 'pm': 15 }, { 'id': 1516, 'expId': 11, 'companyId': 218, 'expTime': 7, 'cash': 205402099.00, 'salesShop': 277948411.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 115.00, 'profit': 174115525.00, 'cashScore': 2.40, 'salesShopScore': 2.55, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 1.95, 'profitScore': 7.70, 'sumScore': 14.60, 'companyName': 'lq1', 'studentName': '邢林青', 'pm': 16 }, { 'id': 1510, 'expId': 11, 'companyId': 210, 'expTime': 7, 'cash': 44394900.66, 'salesShop': 78257786.00, 'salesDistributor': 5010377.00, 'salesBid': 151200.00, 'productValue': 294.00, 'profit': 37568154.66, 'cashScore': 0.45, 'salesShopScore': 0.60, 'salesDistributorScore': 4.65, 'salesBidScore': 0.90, 'productValueScore': 5.00, 'profitScore': 1.40, 'sumScore': 13.00, 'companyName': '恬源集团', 'studentName': '萧潇飞雪', 'pm': 17 }, { 'id': 1514, 'expId': 11, 'companyId': 215, 'expTime': 7, 'cash': 106611687.26, 'salesShop': 150946312.00, 'salesDistributor': 802587.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 54846737.26, 'cashScore': 1.20, 'salesShopScore': 1.35, 'salesDistributorScore': 0.75, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 2.45, 'sumScore': 10.75, 'companyName': '存的果蔬公司', 'studentName': '宋雨珊', 'pm': 18 }, { 'id': 1505, 'expId': 11, 'companyId': 202, 'expTime': 7, 'cash': 92357877.00, 'salesShop': 120582001.00, 'salesDistributor': 166740.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 76351858.00, 'cashScore': 1.05, 'salesShopScore': 1.05, 'salesDistributorScore': 0.15, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 3.15, 'sumScore': 10.40, 'companyName': 'Aa麦当当叔叔果蔬批发', 'studentName': '桂佳丽', 'pm': 19 }, { 'id': 1506, 'expId': 11, 'companyId': 205, 'expTime': 7, 'cash': 4783148.31, 'salesShop': 9816196.00, 'salesDistributor': 3872850.00, 'salesBid': 88347.00, 'productValue': 294.00, 'profit': 450659.31, 'cashScore': 0.00, 'salesShopScore': 0.00, 'salesDistributorScore': 3.60, 'salesBidScore': 0.45, 'productValueScore': 5.00, 'profitScore': 0.00, 'sumScore': 9.05, 'companyName': '绝不破产集团', 'studentName': '柯思妍', 'pm': 20 }, { 'id': 1517, 'expId': 11, 'companyId': 229, 'expTime': 7, 'cash': 100003756.00, 'salesShop': 151306144.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 294.00, 'profit': 33190777.00, 'cashScore': 1.20, 'salesShopScore': 1.35, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 5.00, 'profitScore': 1.40, 'sumScore': 8.95, 'companyName': 'S集团', 'studentName': '沈明娇', 'pm': 21 }, { 'id': 1504, 'expId': 11, 'companyId': 201, 'expTime': 7, 'cash': 91055434.65, 'salesShop': 120294407.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 216.00, 'profit': 69402646.65, 'cashScore': 1.05, 'salesShopScore': 1.05, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 3.65, 'profitScore': 2.80, 'sumScore': 8.55, 'companyName': '008', 'studentName': '欧阳菱子', 'pm': 22 }, { 'id': 1500, 'expId': 11, 'companyId': 191, 'expTime': 7, 'cash': 41979814.86, 'salesShop': 47934227.90, 'salesDistributor': 142538.40, 'salesBid': 0.00, 'productValue': 267.00, 'profit': 32659814.86, 'cashScore': 0.45, 'salesShopScore': 0.30, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 4.50, 'profitScore': 1.40, 'sumScore': 6.65, 'companyName': '亿禾创意空间', 'studentName': '严婉琪', 'pm': 23 }, { 'id': 1494, 'expId': 11, 'companyId': 175, 'expTime': 7, 'cash': 57138288.00, 'salesShop': 51765371.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 173.00, 'profit': 29604718.00, 'cashScore': 0.60, 'salesShopScore': 0.45, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 2.90, 'profitScore': 1.05, 'sumScore': 5.00, 'companyName': '好又来股份有限公司', 'studentName': '雷阳', 'pm': 24 }, { 'id': 1501, 'expId': 11, 'companyId': 192, 'expTime': 7, 'cash': 12360772.40, 'salesShop': 20992520.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 184.00, 'profit': 9360772.40, 'cashScore': 0.00, 'salesShopScore': 0.15, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 3.10, 'profitScore': 0.35, 'sumScore': 3.60, 'companyName': '世界皆舞台', 'studentName': '张曦宇', 'pm': 25 }, { 'id': 1493, 'expId': 11, 'companyId': 174, 'expTime': 7, 'cash': 14556464.20, 'salesShop': 24103450.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 184.00, 'profit': 1156464.20, 'cashScore': 0.15, 'salesShopScore': 0.15, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 3.10, 'profitScore': 0.00, 'sumScore': 3.40, 'companyName': '许氏集团', 'studentName': '许卓', 'pm': 26 }, { 'id': 1492, 'expId': 11, 'companyId': 173, 'expTime': 7, 'cash': 4955060.96, 'salesShop': 10573524.20, 'salesDistributor': 858422.00, 'salesBid': 0.00, 'productValue': 103.00, 'profit': 1568689.96, 'cashScore': 0.00, 'salesShopScore': 0.00, 'salesDistributorScore': 0.75, 'salesBidScore': 0.00, 'productValueScore': 1.75, 'profitScore': 0.00, 'sumScore': 2.50, 'companyName': '付氏集团', 'studentName': '付颖异', 'pm': 27 }, { 'id': 1519, 'expId': 11, 'companyId': 233, 'expTime': 7, 'cash': 3606575.00, 'salesShop': 3508740.00, 'salesDistributor': 288685.00, 'salesBid': 0.00, 'productValue': 115.00, 'profit': 524317.00, 'cashScore': 0.00, 'salesShopScore': 0.00, 'salesDistributorScore': 0.15, 'salesBidScore': 0.00, 'productValueScore': 1.95, 'profitScore': 0.00, 'sumScore': 2.10, 'companyName': '乐加股份有限公司', 'studentName': '姜依妮', 'pm': 28 }, { 'id': 1509, 'expId': 11, 'companyId': 209, 'expTime': 7, 'cash': 1665495.00, 'salesShop': 775104.00, 'salesDistributor': 776665.00, 'salesBid': 0.00, 'productValue': 49.00, 'profit': -1511509.00, 'cashScore': 0.00, 'salesShopScore': 0.00, 'salesDistributorScore': 0.60, 'salesBidScore': 0.00, 'productValueScore': 0.80, 'profitScore': 0.00, 'sumScore': 1.40, 'companyName': '甜啦啦果业公司', 'studentName': '丁静婷', 'pm': 29 }, { 'id': 1490, 'expId': 11, 'companyId': 170, 'expTime': 7, 'cash': 2446400.00, 'salesShop': 0.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 10.00, 'profit': -1123600.00, 'cashScore': 0.00, 'salesShopScore': 0.00, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 0.15, 'profitScore': 0.00, 'sumScore': 0.15, 'companyName': 'S集团', 'studentName': '沈明娇', 'pm': 30 }, { 'id': 1520, 'expId': 11, 'companyId': 178, 'expTime': 7, 'cash': 0.00, 'salesShop': 0.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 0.00, 'profit': 0.00, 'cashScore': 0.00, 'salesShopScore': 0.00, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 0.00, 'profitScore': 0.00, 'sumScore': 0.00, 'companyName': '世雄集团', 'studentName': '张谢源', 'pm': 31 }, { 'id': 1521, 'expId': 11, 'companyId': 223, 'expTime': 7, 'cash': 0.00, 'salesShop': 0.00, 'salesDistributor': 0.00, 'salesBid': 0.00, 'productValue': 0.00, 'profit': 0.00, 'cashScore': 0.00, 'salesShopScore': 0.00, 'salesDistributorScore': 0.00, 'salesBidScore': 0.00, 'productValueScore': 0.00, 'profitScore': 0.00, 'sumScore': 0.00, 'companyName': '华为', 'studentName': '梅俊福', 'pm': 31 }], 'token': null, 'uidtoken': null }
      // const url = '/teacher/teachExp/getCycrank'
      // const params = {
      //   token: localStorage.uesrToken,
      //   expTime: this.search_expTime
      // }
      // this.reqM1Service(url, params)
      //   .then(res => {
      //     if (res.code === 200) {
      this.rankingListData = res.data
      //   } else {
      //     this.$message.error(res.msg)
      //   }
      // })
      // .catch(failResponse => { })
    }
  }
}
</script>

<style lang="scss" scoped>
.studentRanking {
  position: relative;
  width: 100%;
  min-width: 1280px;
  height: 100vh;
  background-size: cover;
  background-repeat: no-repeat;
  // background-image: url("../../../static/teacher/images/expList_bg.png");

  @media screen and (max-height: 920px) {
    height: 100%;
    min-height: 1080px;
  }
  .stu-ranking {
    width: 90%;
    margin: 10px auto 0px;
    max-height: 820px;
    overflow-y: auto;
    background: #ede8dd;
    border-radius: 20px;
    padding: 10px 20px;
    .info-head {
      display: flex;
      align-items: center;
      white-space: nowrap;
      padding: 10px 10px 5px;
      .info-left {
        display: flex;
        align-items: center;
        img {
          margin-top: 5px;
        }
        p {
          font-size: 18px;
          font-weight: 700;
          color: #ffffff;
          letter-spacing: 1.8px;
          padding-left: 6px;
          position: relative;
          z-index: 0;
          white-space: nowrap;
          text-shadow: 1.03px 2.82px 3px rgba(74, 80, 82, 0.94);
        }
        p::before {
          content: "学生排名";
          position: absolute;
          z-index: -1;
          -webkit-text-stroke: 4px #415863;
        }
      }
      .info-center {
        flex: 1;
        .info-showtop {
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 14px;
          .info-exp {
            display: flex;
            align-items: center;
            padding-right: 50px;

            @media screen and (max-width: 1400px) {
              padding-right: 35px;
            }
            .info-title {
              font-weight: 700;
              color: #ef6617;
              padding-left: 5px;
            }
            .progress-bar {
              width: 125px;
            }
          }
        }
      }
      .info-right {
        position: relative;
        width: 140px;
        .back-list {
          cursor: pointer;
        }
        span {
          position: absolute;
          top: 8px;
          left: 41px;
          color: #ffffff;
          font-size: 14px;
          font-weight: 400;
        }
      }
    }
    .selet-info {
      width: 100%;
      height: calc(100% - 61px);
      padding: 15px;
      background: #e6dbc9;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .experiment-condition {
        display: flex;
        align-items: center;
      }
    }
    .table-list {
      margin-top: 25px;
      .table-head,
      .table-content {
        min-height: 40px;
        background: #00a0e9;
        border-radius: 5px;
        text-align: center;
        display: flex;
        align-items: center;
        margin-top: 10px;
        .table-td {
          position: relative;
          flex: 1;
          color: #ffffff;
          font-size: 14px;
          font-weight: 700;
          padding: 0px 10px;
          box-sizing: border-box;
          &::after {
            position: absolute;
            content: "|";
            top: 0;
            left: 0;
            bottom: 0;
            right: 0;
            width: 1px;
            display: flex;
            align-items: center;
            justify-content: center;
          }
          &:first-child {
            &::after {
              content: none;
            }
          }
          &.other2 {
            flex: none;
            width: 110px;
          }
        }
      }
      .table-content {
        background: #fcf9f4;
        .table-td {
          color: #7b4f03;
          font-weight: 400;
        }
      }
      .noexp {
        text-align: center;
        padding: 30px;
        p {
          font-size: 16px;
          font-weight: 400;
          color: #c25411;
          padding: 24px 0px 17px;
        }
      }
    }
  }
}
</style>
<!-- 顶部进度条样式 -->
<style lang="scss">
.studentRanking {
  .info-head {
    .progress-bar {
      .el-progress-bar {
        border: 5px solid #eeeeee;
        border-radius: 8px;
        background: #e5e5e5;
      }
      .el-progress-bar__outer {
        background-color: #e5e5e5;
        overflow: visible;
        position: relative;
      }
      .el-progress-bar__innerText {
        color: #424242;
        position: absolute;
        left: 53px;
        top: 5px;
        transform: translate(-50%, -50%);
      }
    }
  }
}
</style>
