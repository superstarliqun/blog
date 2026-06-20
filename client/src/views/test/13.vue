<!-- MBTI测评报告 -->
<template>
  <div>
    <div class="report_head mt20">
      <div class="flex-between">
        <p>测试者：<span>{{ testName }}</span></p>
        <p>测评日期：<span>{{ qBankObj.testDateStr }}</span></p>
      </div>
      <p class="tCenter gSize bold black pt10">MBTI职业性格测评报告</p>
    </div>
    <p class="line" />
    <!-- 图表展示 -->
    <div class="flex-between">
      <div />
      <!-- MBTI倾向示意图 -->
      <div class="charts">
        <p class="bold tCenter dSize">MBTI倾向示意图</p>
        <div v-for="(item, index) in progressList" :key="index" class="flex mt30">
          <span>{{ item.leftName }}</span>
          <div class="progress">
            <div class="progress-container">
              <p v-if="item.leftWidth" class="left_bar plr5" :style="{ width: item.leftWidth * 2 + '%' }">{{ item.leftWidth
                +'%' }}</p>
            </div>
            <div class="progress-container">
              <p v-if="item.rightWidth" class="right_bar plr5" :style="{ width: item.rightWidth * 2 + '%' }">{{
                item.rightWidth+'%' }}</p>
            </div>

          </div>
          <span>{{ item.rightName }}</span>
        </div>
      </div>
    </div>
    <!-- 维度分析 -->
    <div class="mt20">
      <div class="flex">
        <p class="title_line" />
        <p class="bold bSize pl10">维度分析</p>
      </div>
      <div class="pl15 black2 sSize">
        <div>
          <p>
            MBTI（Myers-Briggs Type
            Indicator）性格测试是一种基于心理学家卡尔·荣格的心理类型理论而开发的测试工具。它旨在帮助人们更好地了解自己的性格特点、兴趣爱好、擅长的工作；帮助个体了解自己的优势和劣势，从而在职业发展过程中做出更明智的决策。
          </p>
          <p>MBTI将人的性格划分为四个维度：外向（E）与内向（I），感觉（S）与直觉（N），思考（T）与情感（F），判断（J）与知觉（P）。在这四个维度上，每个人都会有不同的倾向，从而形成16种不同的性格类型。</p>
        </div>
        <div class="mt15 me-fx-row-sb-c">
          <div>
            <p class="black2 sSize bold">维度一：外向—内向（E—I）</p>
            <div class="xg_introduce" style="background:#539cff;">
              <p class="xg_analysis">根据个体生命能量的指向性，该维度将性格区分为外向与内向两种类型。这是区分个体性格的最基本维度。 </p>
              <p class="xg_analysis">
                外向的人倾向于将注意力和精力投注在外部世界，如外在的人、物和环境等；而内向的人则较为关注自我的内部状况，如内心的情感和思想等。两种类型的个体在自己偏好的世界里会感觉自在、充满活力，而到相反的世界里则会不安、疲惫。
              </p>
            </div>
          </div>
          <div>
            <p class="black2 sSize bold">维度二：感觉—直觉（S—N）</p>
            <div class="xg_introduce" style="background:#56be86;">
              <p class="xg_analysis">根据个人收集信息的方式，该维度将性格区分为感觉与直觉两种类型。 </p>
              <p class="xg_analysis">
                感觉型的人关注的是事实本身、注重细节，而直觉型的人注重的是基于事实的含义、关系和结论；感觉型的人信赖五官听到、看到、闻到、感觉到、尝到的实实在在的、有形有据的事实和信息，而直觉型的人注重“第六感觉”，注重“弦外之音”。
              </p>
            </div>
          </div>
        </div>
        <div class="mt15 me-fx-row-sb-c">
          <div>
            <p class="black2 sSize bold">维度三：思考—情感（T—F）</p>
            <div class="xg_introduce" style="background:#ff9a1e;">
              <p class="xg_analysis">根据个体做决定的方式，该维度将性格区分为思考与情感两种类型。 </p>
              <p class="xg_analysis">
                主要表现在二者在做决定时的依据不一样.情感型的人常从自我的价值观念出发，变通地贯彻规章制度，做出一些自己认定是对的决策，比较关注决策可能给他人带来的情绪体验，人情味较浓；思考型的人则比较注重依据客观事实的分析，一以贯之、一视同仁地贯彻规章制度，不太习惯根据人情因素变通。
              </p>
            </div>
          </div>
          <div>
            <p class="black2 sSize bold">维度四：判断——知觉（J—P）</p>
            <div class="xg_introduce" style="background:#837eff;">
              <p class="xg_analysis">
                根据个体最感舒适的生活方式，该维度将性格区分为判断与知觉两种类型。在处事方式上，判断型的人目的性较强，一板一眼，他们喜欢有计划、有条理的世界，更愿意以比较有序的方式生活。知觉型的人好奇性、适宜性强，他们会不断关注新的信息，喜欢变化，也会考虑许多可能的变化因素，更愿意以比较灵活、随意、开放的方式生活。
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 性格类型维度分析 -->
    <div class="mt20">
      <div class="flex">
        <p class="title_line" />
        <p class="bold bSize pl10">性格类型维度分析</p>
      </div>
      <div class="pl15 black2 sSize">
        <p class="pt10">{{ mbtiMany.name }}</p>
        <div class="mt15 me-fx-row-sb-c fWrap">
          <div v-for="(item, index) in mbtiOneList" :key="index" class="xg_introduce2">
            <p class="xg_analysis">{{ item.content }}</p>
          </div>
        </div>
        <p>{{ mbtiMany.summarizeContent }}</p>
      </div>
    </div>
    <!-- 性格优点 -->
    <div class="mt20">
      <div class="flex">
        <p class="title_line" />
        <p class="bold bSize pl10">性格优点</p>
      </div>
      <div class="pl15 black2 sSize">
        <p class="pt10">{{ mbtiMany.advantageTop }}</p>
        <div id="container" v-html="mbtiMany.advantageContent" />
        <p class="pt5">{{ mbtiMany.advantageEnd }}</p>
      </div>
    </div>
    <!--性格可能的不足  -->
    <div class="mt20">
      <div class="flex">
        <p class="title_line" />
        <p class="bold bSize pl10">性格可能的不足</p>
      </div>
      <div class="pl15 black2 sSize">
        <p class="pt10">{{ mbtiMany.dimensionTop }}</p>
        <div id="container" v-html="mbtiMany.disadvantageContent" />
        <p class="pt5">{{ mbtiMany.dimensionEnd }}</p>
      </div>
    </div>
    <!-- 职场工作 -->
    <div class="mt20">
      <div class="flex">
        <p class="title_line" />
        <p class="bold bSize pl10">职场工作</p>
      </div>
      <div class="pl15 black2 sSize">
        <p class="pt10">{{ mbtiMany.workTop }}</p>
        <div id="container" v-html="mbtiMany.workContent" />
        <p class="pt5">{{ mbtiMany.workEnd }}</p>
      </div>
    </div>
    <!-- 建议 -->
    <div class="mt20">
      <div class="flex">
        <p class="title_line" />
        <p class="bold bSize pl10">建议</p>
      </div>
      <p class="pl15 pt10 black2 sSize">
        需要注意的是，每个个体都是独一无二的，他们的性格特征和行为可能会因个人经历和环境而有所不同。因此，了解自己的MBTI类型只是一个起点，个体还需要不断地自我探索和发展，以实现个人潜力的最大化。</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      testName: '刘盼盼', // 用户姓名
      progressList: [
        {
          leftName: '外向（E）',
          rightName: '内向（I）',
          leftWidth: null,
          rightWidth: null
        },
        {
          leftName: '感觉（S）',
          rightName: '直觉（N）',
          leftWidth: null,
          rightWidth: null
        },
        {
          leftName: '外向（T）',
          rightName: '情感（F）',
          leftWidth: null,
          rightWidth: null
        },
        {
          leftName: '判断（J）',
          rightName: '知觉（P）',
          leftWidth: null,
          rightWidth: null
        }
      ],
      qBankObj: this.data1().data
    }
  },
  mounted() {
    this.mbtiMany()
    this.mbtiOneList()
  },
  methods: {
    mbtiMany() {
      // 使用正则表达式添加 <p> 标签
      const info = this.qBankObj.mbtiMany
      console.log(this.qBankObj)
      this.progressList.forEach((element, index) => {
        switch (index) {
          case 0:
            element.leftWidth = this.qBankObj.eNum
            element.rightWidth = this.qBankObj.iNum
            break
          case 1:
            element.leftWidth = this.qBankObj.sNum
            element.rightWidth = this.qBankObj.nNum
            break
          case 2:
            element.leftWidth = this.qBankObj.tNum
            element.rightWidth = this.qBankObj.fNum
            break
          case 3:
            element.leftWidth = this.qBankObj.jNum
            element.rightWidth = this.qBankObj.pNum
            break
        }
      })

      // 性格优势
      const newText = '<p>' + info.advantageContent.replace(/(\r\n|\n|\r)+/g, '</p>\n\n<p>') + '</p>'
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.qBankObj.mbtiMany.advantageContent = newText

      // 性格不足
      const newText2 = '<p>' + info.disadvantageContent.replace(/(\r\n|\n|\r)+/g, '</p>\n\n<p>') + '</p>'
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.qBankObj.mbtiMany.disadvantageContent = newText2

      // 职场工作
      const newText3 = '<p>' + info.workContent.replace(/(\r\n|\n|\r)+/g, '</p>\n\n<p>') + '</p>'
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.qBankObj.mbtiMany.workContent = newText3
      return this.qBankObj.mbtiMany
    },
    mbtiOneList() {
      return this.qBankObj.mbtiOneList
    },
    data1() {
      return { 'code': 200, 'msg': '成功', 'data': { 'mbtiTestNum': 8, 'mbtiTestStep': 2, 'pNum': 40, 'sNum': 30, 'tNum': 20, 'mbtiMany': { 'id': 1, 'libraryType': null, 'name': '总经理型人格', 'code': 'ESTJ', 'dimensionTop': 'ESTJ组合型以注重实际、有组织、决断力强和喜欢掌握控制著称，被称为&amp;ldquo;管理者&amp;rdquo;。', 'dimensionEnd': '总的来说， ESTJ型的个体通常在工作和社交中都表现得非常自信，乐于承担责任。他们善于处理现实问题，注重实用性和效率，对于达成目标会付出很大的努力。', 'advantageTop': 'ESTJ倾向于注重传统价值观，对规则和秩序有很强的信仰，喜欢在组织中扮演领导角色，具备出色的组织和管理能力。', 'advantageContent': '以下是ESTJ性格类型的特点和分析：\n1. 责任感强：ESTJ以其强烈的责任感和对工作的承诺而著称，通常非常可靠，能够确保任务按时完成。\n2. 组织能力强：ESTJ通常非常善于组织和规划，喜欢将工作安排得井井有条，并确保所有细节都得到妥善处理。\n3. 决策果断：ESTJ通常非常果断，能够迅速做出决策，并坚持自己的立场。\n4. 遵守传统和规则：ESTJ非常尊重传统和规则，倾向于遵循已经证明有效的方法和标准。\n5. 领导能力：ESTJ个体通常具备很强的领导能力，能够激励他人，并带领团队达成目标。', 'advantageEnd': '总的来说，ESTJ性格类型的优点包括责任感强、组织能力强、实际务实、决策果断、遵守传统和规则、领导能力、适应能力强、善于人际关系、乐观积极以及良好的问题解决者。', 'disadvantageContent': '1. 过于严格：ESTJ有时可能过于严格和拘泥于细节，这可能导致他们在处理灵活性和适应性方面遇到困难。\n2. 过于传统：ESTJ有时可能过于传统，这可能导致他们抵制新的想法和方法，即使这些新的想法和方法可能更有效。\n4. 过于直接：ESTJ通常非常直接和坦率，这可能导致他们在需要更微妙和细腻的沟通方式时遇到困难。\n5. 缺乏灵活性：由于ESTJ通常非常坚守自己的原则和标准，可能在需要灵活性和适应性方面遇到挑战。', 'summarizeContent': '总的来说，ESTJ是实际、有组织和负责任的类型。他们通常在组织和管理方面表现出色，并注重纪律和秩序。然而，需要注意他们可能过于关注细节和规则，可能会对变化和不确定性感到不适应。', 'workTop': '在职业选择方面，ESTJ可能倾向于选择那些能够让他们发挥领导力和组织能力的职业生涯。', 'workContent': '在职业选择上，ESTJ类型的人通常具有以下特点：\n1. 实际、务实：ESTJ倾向于关注实际、务实的事物，注重实际的结果和成果，喜欢解决问题并采取切实可行的行动。\n2. 组织者：ESTJ以其出色的组织能力而著称，具备出色的组织和管理能力，喜欢制定计划、设定目标，能够有效地管理时间和资源，确保任务按时完成。\n3. 遵守规则：ESTJ通常具有强烈的责任感，非常重视纪律和秩序，喜欢遵循规则和程序，并且希望周围的人也能遵守。他们能够在有序和规律的环境中表现出最佳状态。\n4. 决策能力强：ESTJ在做决策时通常更倾向于运用逻辑和客观分析，而不是情感或个人偏好。他们喜欢评估事实和数据，并根据理性的思考做出决策。\n5. 责任感强：ESTJ非常重视履行自己的责任和义务。他们通常会全力以赴完成任务，并且对自己的工作负责。', 'workEnd': '总的来说，ESTJ个体在需要组织、领导、管理、监督和执行能力的职业中表现出色，尤其是在行政管理、教育、法律、金融、管理咨询等领域。' }, 'nNum': 50, 'iNum': 46, 'jNum': 35, 'eNum': 39, 'fNum': 11, 'testDateStr': '2024-03-11', 'totalNum': 8, 'mbtiOneList': [{ 'name': 'E', 'content': '外向（E）：喜欢在社交活动中展现自己，喜欢与他人互动并在社交场合中感到兴奋和活跃。他们倾向于从外部世界获取能量，喜欢和他人交往并且很容易与人建立联系。' }, { 'name': 'S', 'content': '感觉(S)：注重当前的现实和具体细节，喜欢从已知的信息中获取准确的数据。他们是实践主义者，更喜欢处理已经被验证和证明有效的方法。' }, { 'name': 'T', 'content': '思考（T）：倾向于使用逻辑和客观分析，而不是依赖情感或个人偏好。他们做决策时通常会考虑到各种因素，喜欢通过分析和推理来解决问题，并注重事实和证据。' }, { 'name': 'J', 'content': '判断（J）：喜欢有结构和组织的生活，喜欢制定计划并按计划行事，通常会提前做好准备。他们是有责任心和可靠性的人，总是努力保持秩序和完成任务。' }] }, 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
    }

  }
}
</script>

<style lang="scss" scoped>
.line {
  width: 904px;
  height: 10px;
  opacity: 0.3;
  background: #0f809e;
  margin: 30px auto;
}

.charts {
  width: 500px;
  height: 280px;
  padding: 15px;
  background: #fefefe;
  border: 5px solid #f7f7f7;
  border-radius: 20px;

  .progress {
    width: 300px;
    height: 20px;
    line-height: 20px;
    background: #e5e5e5;
    border-radius: 10px;
    margin-right: 10px;
    display: flex;
    align-items: center;
    justify-content: center;

    .progress-container {
      width: 50%;
    }

    .left_bar {
      height: 100%;
      background: #539cff;
      border-radius: 10px 0px 0px 10px;
      text-align: center;
      color: #fff;
      margin-left: auto;
    }

    .right_bar {
      height: 100%;
      background: #837eff;
      border-radius: 0px 10px 10px 0px;
      text-align: center;
      color: #fff;
    }
  }
}

.title_line {
  width: 6px;
  height: 20px;
  background: #e93131;
  border-radius: 3px;
}

.xg_introduce {
  width: 440px;
  height: 120px;
  margin-top: 10px;
  padding: 10px 15px;
  border-radius: 20px 0px 20px 0px;

  .xg_analysis {
    text-indent: 2em;
    color: #fff;
  }
}

.xg_introduce2 {
  width: 440px;
  height: 65px;
  padding: 10px 15px;
  background: #f1f6ff;
  border-radius: 20px 0px 20px 0px;
  margin-bottom: 12px;
  font-size: 12px;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
