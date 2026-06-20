<!-- 公用部分 -->
<template>
  <el-container id="admin-common">
    <!-- 头部 s -->
    <el-header>
      <div class="header-content clearfix">
        <div class="logo fl clearfix">
          <div class="project-name fl">
            <h4>
              <p class="logot1">{{ softName }}</p>
            </h4>
            <p class="administrators">管理员端</p>
          </div>
        </div>
        <ul class="nav-info fr clearfix">
          <li class="fl"><i class="el-icon-user" /> 欢迎您,{{ userName }}</li>
          <li class="fl">|</li>
          <li class="fl">
            <a @click="goExit()"><i class="el-icon-switch-button" />退出系统</a>
          </li>
        </ul>
      </div>
    </el-header>
    <!-- 主体布局 -->
    <el-container>
      <!-- 左侧侧边栏 -->
      <el-aside>
        <el-menu :default-active="$route.path" class="el-menu-vertical-demo" unique-opened router
                 @select="handleSelect"
        >
          <el-submenu v-for="item in menulist" :key="item.id" :index="item.id + ''">
            <!-- 第一级 -->
            <template slot="title">
              <span>{{ item.autName }}</span>
            </template>
            <!-- 第二级 -->
            <div v-for="item1 in item.children" :key="item1.id" :index="item1.id + ''">
              <template v-if="item1.children">
                <el-submenu>
                  <template slot="title">
                    <span>{{ item1.autName }}</span>
                  </template>
                  <!-- 第三级 -->
                  <el-menu-item-group v-for="item2 in item1.children" :key="item2.id" :index="item2.id + ''">
                    <el-menu-item :index="item2.id">{{ item2.autName }}</el-menu-item>
                  </el-menu-item-group>
                </el-submenu>
              </template>
              <template v-else>
                <el-menu-item-group>
                  <el-menu-item :index="item1.id">{{ item1.autName }}</el-menu-item>
                </el-menu-item-group>
              </template>
            </div>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 右侧内容 s -->
      <el-main>
        <div class="right-main">
          <section class="app-main">
            <transition name="fade-transform" mode="out-in">
              <router-view :key="key" />
            </transition>
          </section>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      // 当前激活菜单
      activeIndex: null,
      softName: null,
      userName: localStorage.uesrName,
      menulist: [
        {
          id: 5,
          autName: '首页',
          children: [
            {
              id: '5-1',
              autName: '首页',
              path: '/admin/index/index'
            }
          ]
        },
        {
          id: 1,
          autName: '系统设置',
          children: [
            {
              id: '1-1',
              autName: '人员管理',
              path: '/admin/systemSettings/perManagement'
            },
            {
              id: '1-2',
              autName: '功能模块管理',
              path: '/admin/systemSettings/funcModuleManage'
            },
            {
              id: '1-3',
              autName: '权限管理',
              path: '/admin/systemSettings/authManagement'
            },
            {
              id: '1-4',
              autName: '人员区域管理',
              path: '/admin/systemSettings/personAreaManagement'
            }

          ]
        },
        {
          id: '6',
          autName: '销售管理',
          path: '/admin/salesManage',
          children: [
            {
              id: '6-1',
              autName: '销售类型&渠道',
              path: '/admin/salesManage/salesTypes'
            },
            {
              id: '6-2',
              autName: 'VIP卡种类',
              path: '/admin/salesManage/vipCardType'
            },
            {
              id: '6-3',
              autName: 'VIP电子兑换卡',
              path: '/admin/salesManage/vipElecExchangeCard'
            }
          ]
        },
        {
          id: 2,
          autName: '基础数据',
          children: [
            {
              id: '2-1',
              autName: '城市',
              path: '/admin/basicData/cities'
            },
            {
              id: '2-2',
              autName: '考生类别',
              path: '/admin/basicData/examinationType'
            },
            // {
            //   id: "2-3",
            //   autName: "考生类型",
            //   path: "/admin/basicData/candidateType",
            // },
            {
              id: '2-4',
              autName: '科目',
              path: '/admin/basicData/subject'
            },
            {
              id: '2-5',
              autName: '考试模式',
              path: '/admin/basicData/examinationMode'
            },
            {
              id: '2-6',
              autName: '考试模式选科',
              path: '/admin/basicData/examinationModeOptional'
            },
            {
              id: '2-7',
              autName: '专业层次',
              path: '/admin/basicData/specialityLevel'
            },
            {
              id: '2-8',
              autName: '专业',
              path: '/admin/basicData/speciality'
            },
            {
              id: '2-9',
              autName: '高校',
              path: '/admin/basicData/collegesAndUniversities'
            }
          ]
        },
        {
          id: 3,
          autName: '省相关',
          children: [
            {
              id: '3-1',
              autName: '批次&批次线',
              path: '/admin/provincialRelated/batchAndBatchLine'
            },
            {
              id: '3-2',
              autName: '批次关联',
              path: '/admin/provincialRelated/batchAssociation'
            },
            {
              id: '3-3',
              autName: '概率估值',
              path: '/admin/provincialRelated/probabilityValuation'
            },
            {
              id: '3-4',
              autName: '概率对比',
              path: '/admin/provincialRelated/probabilityComparison'
            },
            {
              id: '3-5',
              autName: '志愿表',
              path: '/admin/provincialRelated/volunteerForm'
            },
            {
              id: '3-6',
              autName: '考试模式',
              path: '/admin/provincialRelated/optional'
            },
            {
              id: '3-7',
              autName: '一分一段表',
              path: '/admin/provincialRelated/piecemealTable'
            },
            {
              id: '3-8',
              autName: '招生计划',
              path: '/admin/provincialRelated/recruitStudents'
            },
            {
              id: '3-9',
              autName: '招生计划数据预处理',
              path: '/admin/provincialRelated/zsPlanPreprocessing'
            },
            {
              id: '3-10',
              autName: '招生计划【自动】匹配',
              path: '/admin/provincialRelated/zsPlanAutoMatching'
            },
            {
              id: '3-11',
              autName: '招生计划【手动】匹配',
              path: '/admin/provincialRelated/manualSynchronization'
            },
            {
              id: '3-12',
              autName: '专业分数线',
              path: '/admin/provincialRelated/professionalScoreLine'
            },
            {
              id: '3-13',
              autName: '专业分数线数据预处理',
              path: '/admin/provincialRelated/specialtyPreprocessing'
            },
            {
              id: '3-14',
              autName: '【招生计划】自动匹配【专业分数线】',
              path: '/admin/provincialRelated/specialtyAutoMatching'
            },
            {
              id: '3-15',
              autName: '【招生计划】手动匹配【专业分数线】',
              path: '/admin/provincialRelated/recruitsSynchronizedScoreLine'
            },
            {
              id: '3-16',
              autName: '招生计划录取概率',
              path: '/admin/provincialRelated/zsPlanAdProbabilityIndex'
            }
          ]
        },
        {
          id: 4,
          autName: '生涯规划',
          children: [
            {
              id: '4-1',
              autName: '职业',
              path: '/admin/careerPlanning/career'
            },
            {
              id: '4-2',
              autName: '测试题库',
              path: '/admin/careerPlanning/testQuestionBank'
            },
            {
              id: '4-3',
              autName: '测试题库结果-霍兰德',
              path: '/admin/careerPlanning/systemTestingHld'
            },
            {
              id: '4-4',
              autName: '测试题库结果',
              path: '/admin/careerPlanning/systemTesting'
            },
            {
              id: '4-5',
              autName: '毕业选择',
              path: '/admin/careerPlanning/graduationChoice'
            },
            {
              id: '4-6',
              autName: '智商测试对照表',
              path: '/admin/careerPlanning/IQTestTable'
            },
            {
              id: '4-7',
              autName: '综合招生',
              children: [
                {
                  id: '4-7-1',
                  autName: '强基计划',
                  path: '/admin/careerPlanning/strongFoundationPlan'
                },
                {
                  id: '4-7-2',
                  autName: '综合评价',
                  path: '/admin/careerPlanning/strongFoundationPlan'
                },
                {
                  id: '4-7-3',
                  autName: '专项计划',
                  path: '/admin/careerPlanning/strongFoundationPlan'
                }
              ]
            },
            {
              id: '4-8',
              autName: '高考分留学',
              path: '/admin/careerPlanning/scoreStudyAbroad'
            }
          ]
        }
      ]
    }
  },
  computed: {
    key() {
      return this.$route.path
    }
  },
  created() {
  },
  methods: {
    handleSelect(index) {
      if (this.$route.path !== index) {
        this.$router.push(index)
      }
      // 切换路由时清空生涯规划测试题库和智商测试对照表存储的选中选项卡下标
      if (index === '/admin/careerPlanning/IQTestTable' || index === '/admin/careerPlanning/testQuestionBank') {
        sessionStorage.removeItem('tabSelected')
        sessionStorage.removeItem('tabIndex')
      }
    },
    goExit() {
      // 退出系统
      window.localStorage.clear()
      // 此处拼接内容
      // const url = domain.Base_M2_URL + '/admin/adminExit'
      const url = '/admin/adminExit'
      window.location.href = url
    }
  }
}
</script>

<style lang="scss" scoped>
.el-menu-item-group__title {
  padding: 0;
}

.el-submenu__title {
  padding-left: 10px !important;
}

.el-menu-item {
  padding-left: 20px !important;
  font-size: 12px;
}

.children_node {
  .el-submenu__title {
    padding-left: 20px !important;
  }

  .el-menu-item {
    padding-left: 25px !important;
  }
}

.mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); // 设置遮罩层的颜色和透明度
  z-index: 999; // 将遮罩层置于侧边栏之下
}

.el-container {
  height: 1000px;
}
</style>
