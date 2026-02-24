<template>
  <div class="love-card">
    <div class="right-notice">
      <div class="love-container">
        <div class="love-titile">这是我们在一起的</div>
        <div class="love-time">
          第
          <span class="timer-item">{{ addZero(timerEntity.year) }}</span>年
          <span class="timer-item">{{ addZero(timerEntity.month) }}</span>月 <span class="timer-item">{{
            addZero(timerEntity.day) }}</span>天 <span class="timer-item">{{ addZero(timerEntity.hour) }}</span>时
          <span class="timer-item">{{ addZero(timerEntity.minute) }}</span>分
          <span class="timer-item">{{ addZero(timerEntity.second) }}</span>秒
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import listMixin from '@/utils/mixins/listMixin'
export default {
  mixins: [listMixin],
  data() {
    return {
      pageNumber: null,
      jumpNumber: '',
      timerEntity: {
        year: null,
        month: null,
        day: null,
        hour: null,
        minute: null,
        second: null
      }
    }
  },
  created() {
    this.updateTimeDifference()
  },
  methods: {
    updateTimeDifference() {
      const currentDate = new Date()
      const fixedDate = new Date('2023-02-15 19:36:00')

      let years = currentDate.getFullYear() - fixedDate.getFullYear()
      let months = currentDate.getMonth() - fixedDate.getMonth()
      let days = currentDate.getDate() - fixedDate.getDate()

      // 如果天数不足，从月份中借1个月（30天）
      if (days < 0) {
        months--
        days += 30 // 假设一个月30天，这里可以更精确地处理
      }

      // 如果月份不足，从年份中借1年（12个月）
      if (months < 0) {
        years--
        months += 12
      }

      // 计算小时、分钟和秒
      const diff = Math.abs(fixedDate - currentDate)
      const seconds = Math.floor(diff / 1000)
      const minutes = Math.floor(seconds / 60)
      const hours = Math.floor(minutes / 60)

      this.timerEntity = {
        year: years,
        month: months,
        day: days,
        hour: hours % 24,
        minute: minutes % 60,
        second: seconds % 60
      }
    },
    addZero(index) {
      if (index / 10 < 1) {
        return '0' + index
      }
      return index
    }
  }
}
</script>

<style lang="scss" scoped>
.love-card {
  background: var(--card-background);
  border-radius: 6px;
  border: var(--style-border);
  box-shadow: var(--box-shadow)
}

.love-container {
  text-align: center;

  .love-titile {
    font-size: 19px;
    font-weight: 600;
    letter-spacing: 0.2rem;
    // line-height: 4rem;

    background-image: linear-gradient(270deg,
        #ff4500,
        orange,
        gold,
        #90ee90,
        #0ff,
        #1e90ff,
        #9370db,
        #ff69b4,
        #ff4500);
    -webkit-background-clip: text;
    -webkit-animation: jianBian 60s linear infinite;
    animation: jianBian 60s linear infinite;
    color: transparent;
  }

  .love-time {
    height: 100%;
    font-size: 12px;

    .timer-item {
      font-size: 14px;
    }
  }
}
</style>
