<!-- 学习能力测评题库 -->
<template>
  <div class="learningTestBank">
    <div class="screen_content me-fx-col">
      <div class="career_title">
        <div class="flex">
          <svg-icon icon-class="icon-a-03-logo" class="SvgIcon" />
          <p class="cSize">学习能力测评</p>
        </div>
      </div>
    </div>
    <div class="top_introduction">
      <div class="test_introduction">
        <p class="mb5">学习能力测评是一种评估个体在学习过程中的表现、能力和潜力的方法。</p>
        <p class="mb10">以下是一种简单的学习能力测评，共10道题。请根据自己的实际情况，选择最符合的选项。</p>
        <div class="flex">
          <el-button class="manage-btn mr10 gray3" style=" padding: 5px 8px">单选题</el-button>
          <p class="">(共<span>10</span>道题，每题只选一个答案)</p>
        </div>
      </div>

    </div>
    <!-- 题目 -->
    <div style="border-bottom: 1px solid #e5e5e5;">
      <div class="question_bank">
        <!-- <img src="../../../../static/public/images/workTestBg.png" alt=""> -->
        <div class="test_content">
          <p class="test_title">学习能力测评</p>
          <div v-for="(item, index) in questionBank" :key="index" class="test_problems">
            <p class="cSize mb20"><span>{{ item.orderIndex }}</span>、<span>{{ item.name }}</span></p>
            {{ item.userRecordOptionIds }}
            <el-radio-group v-model="item.userRecordOptionIds">
              <el-radio v-for="info in item.sysCareerTestOptionList" :key="info.id" :label="info.id">{{
                info.optionContent }}</el-radio>
            </el-radio-group>
          </div>
        </div>
      </div>
    </div>
    <div class="test_submit">
      <el-button class="query-btn" @click="handleSubmit">提交</el-button>
    </div>
  </div>
</template>

<script>
// import { publicTestMethod } from '../requestTools/requestData'
// import { publicUidtoken } from '../../../../tools/mixin'
export default {
  data() {
    return {
      questionBank: [],
      uid_token: '',
      timer: null
    }
  },
  mounted() {
    this.questionBank = this.data1().data
  },
  methods: {
    // 提交
    handleSubmit() {
      const param = []
      console.log(this.questionBank)
      this.questionBank.forEach(item => {
        console.log(item.userRecordOptionIds, '我选的')
        item.sysCareerTestOptionList.forEach(info => {
          console.log(info)
          // 选中的选项数据
          if (item.userRecordOptionIds === info.id) {
            const obj = {
              userId: 1,
              libraryType: item.libraryType,
              questionType: item.questionType,
              sysTestQuestionId: info.sysTestQuestionId,
              sysTestOptionId: info.id,
              isRight: info.isRight === null ? 0 : info.isRight,
              score: info.score
            }
            param.push(obj)
          }
        })
      })
    },
    data1() {
      return { 'code': 200, 'msg': '成功', 'data': [{ 'id': 3, 'name': '我能够快速理解新知识。', 'libraryType': 5, 'questionType': 4, 'orderIndex': 1, 'content': '', 'sysCareerTestOptionList': [{ 'id': 13, 'sysTestQuestionId': 3, 'orderIndex': 1, 'optionContent': '完全不符合', 'isRight': null, 'rightContent': '', 'score': 0, 'optionImg': '' }, { 'id': 14, 'sysTestQuestionId': 3, 'orderIndex': 2, 'optionContent': '不太符合', 'isRight': 1, 'rightContent': '', 'score': 5, 'optionImg': '' }, { 'id': 15, 'sysTestQuestionId': 3, 'orderIndex': 3, 'optionContent': '一般', 'isRight': null, 'rightContent': '', 'score': 0, 'optionImg': '' }, { 'id': 16, 'sysTestQuestionId': 3, 'orderIndex': 4, 'optionContent': '比较符合', 'isRight': null, 'rightContent': '', 'score': 0, 'optionImg': '' }, { 'id': 17, 'sysTestQuestionId': 3, 'orderIndex': 5, 'optionContent': '非常符合', 'isRight': null, 'rightContent': '', 'score': 0, 'optionImg': '' }], 'userCareerTestRecordId': '89', 'userRecordOptionIds': '' }, { 'id': 4, 'name': '我擅长总结和归纳所学内容。', 'libraryType': 5, 'questionType': 4, 'orderIndex': 2, 'content': '', 'sysCareerTestOptionList': [{ 'id': 18, 'sysTestQuestionId': 4, 'orderIndex': 1, 'optionContent': '完全符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 19, 'sysTestQuestionId': 4, 'orderIndex': 2, 'optionContent': '不太符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 20, 'sysTestQuestionId': 4, 'orderIndex': 3, 'optionContent': '一般', 'isRight': 1, 'rightContent': null, 'score': 5, 'optionImg': null }, { 'id': 21, 'sysTestQuestionId': 4, 'orderIndex': 4, 'optionContent': '比较符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 22, 'sysTestQuestionId': 4, 'orderIndex': 5, 'optionContent': '非常符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }], 'userCareerTestRecordId': '90', 'userRecordOptionIds': '' }, { 'id': 5, 'name': '我能够记住并运用所学知识。', 'libraryType': 5, 'questionType': 4, 'orderIndex': 3, 'content': '', 'sysCareerTestOptionList': [{ 'id': 23, 'sysTestQuestionId': 5, 'orderIndex': 1, 'optionContent': '完全不符合', 'isRight': 1, 'rightContent': null, 'score': 5, 'optionImg': null }, { 'id': 24, 'sysTestQuestionId': 5, 'orderIndex': 2, 'optionContent': '不太符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 25, 'sysTestQuestionId': 5, 'orderIndex': 3, 'optionContent': '一般', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 26, 'sysTestQuestionId': 5, 'orderIndex': 4, 'optionContent': '比较符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 27, 'sysTestQuestionId': 5, 'orderIndex': 5, 'optionContent': '非常符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }], 'userCareerTestRecordId': '91', 'userRecordOptionIds': '' }, { 'id': 6, 'name': '我能够记住并运用所学知识。', 'libraryType': 5, 'questionType': 4, 'orderIndex': 4, 'content': '', 'sysCareerTestOptionList': [{ 'id': 28, 'sysTestQuestionId': 6, 'orderIndex': 1, 'optionContent': '完全不符合', 'isRight': 0, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 29, 'sysTestQuestionId': 6, 'orderIndex': 2, 'optionContent': '不太符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 30, 'sysTestQuestionId': 6, 'orderIndex': 3, 'optionContent': '一般', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 31, 'sysTestQuestionId': 6, 'orderIndex': 4, 'optionContent': '比较符合', 'isRight': 1, 'rightContent': null, 'score': 10, 'optionImg': null }, { 'id': 32, 'sysTestQuestionId': 6, 'orderIndex': 5, 'optionContent': '非常符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }], 'userCareerTestRecordId': '92', 'userRecordOptionIds': '' }, { 'id': 7, 'name': '我能够有效地与同学或老师交流学习心得。', 'libraryType': 5, 'questionType': 4, 'orderIndex': 5, 'content': '', 'sysCareerTestOptionList': [{ 'id': 33, 'sysTestQuestionId': 7, 'orderIndex': 1, 'optionContent': '完全不符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 34, 'sysTestQuestionId': 7, 'orderIndex': 2, 'optionContent': '不太符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 35, 'sysTestQuestionId': 7, 'orderIndex': 3, 'optionContent': '一般', 'isRight': 1, 'rightContent': null, 'score': 10, 'optionImg': null }, { 'id': 36, 'sysTestQuestionId': 7, 'orderIndex': 4, 'optionContent': '比较符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }, { 'id': 37, 'sysTestQuestionId': 7, 'orderIndex': 5, 'optionContent': '非常符合', 'isRight': null, 'rightContent': null, 'score': 0, 'optionImg': null }], 'userCareerTestRecordId': '93', 'userRecordOptionIds': '' }], 'rootToken': null, 'uidtoken': null, 'rootSessionId': null }
    }
  }
}
</script>

<style lang="scss" scoped>
.learningTestBank {
  width: 100%;
  padding: 15px;
  background: #fff;

  .screen_content {
    box-sizing: border-box;
    margin-bottom: 15px;

    .career_title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      border-bottom: 1px solid #e5e5e5;
      padding-bottom: 15px;

      .SvgIcon {
        width: 30px;
        height: 29px;
        margin-right: 10px;
      }
    }
  }

  .top_introduction {
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 15px;

    .test_introduction {
      padding: 15px;
      background: #f6f7f9;
      border-radius: 5px;
    }
  }

  .question_bank {
    background: #f9fbff;
    border-radius: 5px;
    margin-bottom: 15px;

    .test_content {
      padding: 20px;

      .test_title {
        text-align: center;
        font-size: 30px;
      }

      .test_problems {
        margin-bottom: 30px;
        // /deep/.el-radio__label {
        //   font-size: 16px;
        //   color: #5b5b5b;
        // }
        // /deep/.el-radio__inner {
        //   width: 18px;
        //   height: 18px;
        // }
      }
    }
  }

  .test_submit {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 15px;
  }
}
</style>
