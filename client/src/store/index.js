import Vue from 'vue'
import Vuex from 'vuex'
import { action } from './actions'
import { mutations } from './mutations'
import { getters } from './getters'
import { session, storage } from '@/utils'
import language from '@/language'
import app from './modules/app'

Vue.use(Vuex)

const token = session.get('token') || ''
const lang = storage.get('lang') || 'cn'

export default new Vuex.Store({
  state: {
    // 语言
    language,
    local: lang,
    count: 1,
    token: token,
    show: false
  },
  mutations: mutations,
  actions: action,
  getters: getters,
  // 2. 显式注册模块
  modules: {
    app
  }
})
