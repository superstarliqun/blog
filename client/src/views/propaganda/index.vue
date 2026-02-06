<template>
  <div class="container">
    <div class="sub-container">
      <div v-if="lives != null" class="weather-banner">
        <div class="weather-banner-header">
          <p class="weather-banner-header-left">
            <span class="city">{{ lives.province }} · {{ lives.city }}</span>
          </p>
          <p class="weather-banner-header-right">
            <span>{{ getWeekday(lives.reporttime) }}</span>
            <!-- <span>{{ lives ? lives.reporttime.substring(0, 10) : '' }}</span> -->
            <!-- <span>{{ convertToLunar(lives.reporttime.substring(0, 10)) }}</span> -->
          </p>
        </div>
        <div class="weather-banner-content">
          <div class="weather-banner-content-left">
            <div class="weather-banner-content-temperature">{{ lives.temperature }}°</div>
          </div>
          <div class="weather-banner-content-right">
            <div class="weather-banner-content-pm25">
              <span>湿度{{ lives.humidity }}</span>
              <span v-if="lives.humidity >= 40 && lives.humidity <= 60"> 优良 </span>
              <span v-else-if="lives.humidity > 60"> 较高 </span>
              <span v-else> 较低 </span>
            </div>
            <div class="weather-banner-content-wind">
              <i class="iconfont icon-duoyun weather-banner-content-svg"></i>
              <span>{{ lives.weather }}</span>
              <span>{{ lives.winddirection }}风</span>
              <span>{{ lives.windpower }}级 </span>
            </div>
          </div>
        </div>
      </div>
      <div class="weather-15day">
        <div class="weather-15day-header">
          <p>近3天天气预报</p>
          <ul>
            <li v-for="(item,index) in forecasts" :key="index">
              <p>{{ item.date }}</p>
              <p>{{ getWeekday(item.date) }}</p>
              <p>气象：{{ item.dayweather=== item.nightweather ? item.dayweather : item.dayweather+'-'+item.nightweather }}</p>
              <p>温度：{{ item.daytemp }}-{{ item.nighttemp }}°C</p>
              <p>风向：{{ item.daywind === item.nightwind ?item.daywind+"风":item.daywind+"风转"+item.nightwind+"风" }}</p>
              <p>风力：{{ item.daypower === item.nightpower ?item.daypower+"级":item.daypower+"级-"+item.nightpower+"级" }}</p>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LunarCalendar from 'lunar-calendar'
import Vue from 'vue'
export default {
  data() {
    return {
      lives: [],
      forecasts: []
    }
  },
  created() {
    this.getWeather()
  },
  methods: {
    // 获取天气信息
    getWeather() {
      Vue.prototype.$isShowLoading = false
      this.$get(this.$urls.getWeather, null, ['base']).then(res => {
        this.lives = res.data.lives[0] || {}
      }).catch(() => {
      })
      this.$get(this.$urls.getWeather, null, ['all']).then(res => {
        this.forecasts = res.data.forecasts[0].casts || []
        this.forecasts.shift()
      }).catch(() => {
      })
    },
    // 转化星期
    getWeekday(date) {
      const datenew = new Date(date)
      const weekday = datenew.getDay()
      // 将星期转换为对应的中文名称
      const weekdaysCN = ['日', '一', '二', '三', '四', '五', '六']
      const weekdayCN = weekdaysCN[weekday]
      return `星期${weekdayCN}`
    },
    // 转化农历
    convertToLunar(date) {
      const [year, month, day] = date.split('-').map(Number)
      const lunarInfo = LunarCalendar.solarToLunar(year, month, day)
      return '农历' + lunarInfo.lunarMonthName + lunarInfo.lunarDayName
    }
  }
}

</script>

<style lang="scss" scoped>
.weather-banner {
  width: 900px;
  padding: 30px 0;
  margin: auto 0;
  color: #fff;
  .weather-banner-header {
    height: 20px;
    line-height: 20px;
    .weather-banner-header-left {
      margin: 0;
      float: left;
      .city {
        font-size: 30px;
      }
      span {
        font-size: 18px;
        margin-right: 15px;
      }
    }
    .weather-banner-header-right {
      margin: 0;
      float: right;
      span {
        font-size: 18px;
        margin-right: 15px;
      }
    }
  }
  .weather-banner-content {
    margin-top: 45px;
    height: 79px;
    .weather-banner-content-left {
      float: left;
      .weather-banner-content-temperature {
        position: relative;
        font-size: 79px;
        line-height: 79px;
      }
    }
    .weather-banner-content-right {
      margin-left: 30px;
      float: left;
      height: 53px;
      margin-top: 20px;
      .weather-banner-content-pm25 {
        background: rgb(64, 192, 87);
        display: inline-block;
        padding: 0 6px;
        height: 19px;
        line-height: 19px;
        font-size: 12px;
        border-radius: 9.5px;
        text-align: center;
        span {
          margin-right: 0;
        }
      }
      .weather-banner-content-wind {
        margin-top: 10px;
        line-height: 14px;
        font-size: 14px;
        vertical-align: middle;
        .weather-banner-content-svg {
          position: relative;
          top: -2px;
          width: 20px;
          height: 20px;
        }
        span {
          margin-right: 10px;
        }
      }
    }
  }
  .weather-banner-footer {
    margin-top: 30px;
    line-height: 14px;
    font-size: 14px;
    span {
      margin-right: 20px;
    }
  }
}
.weather-15day {
  position: relative;
  z-index: 999;
  width: 895px;
  color: #fff;
  margin: 0 auto;
  padding: 10px 16px 0;
  box-sizing: border-box;
  border-radius: 3px;
  font-size: 0;
  background-color: rgba(0, 0, 0, 0.1);
  .weather-15day-header {
    padding: 0;
    margin: 0;
    list-style: none;
    p {
      font-size: 18px;
      line-height: 25px;
      margin-top: 8px;
      margin-bottom: 27px;
    }
    ul {
      height: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      li {
        flex-direction: column;
        text-align: center;
      }
    }
  }
}
.container {
  background: linear-gradient(rgb(55, 66, 76), rgb(133, 154, 171));
}
.sub-container {
  width: 900px;
  min-height: calc(100vh - 48px);
  padding: 30px 0;
  margin: 0 auto;
}
</style>
