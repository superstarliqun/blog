import Cookies from 'js-cookie'

const state = {
  // 主题默认是 light | dark
  dataTheme: Cookies.get('dataTheme') || 'light'
}

const mutations = {
  SET_THEME: (state, theme) => {
    state.dataTheme = theme
    Cookies.set('dataTheme', theme)
    document.documentElement.setAttribute('data-theme', theme)
  },
  INIT_THEME: (state) => {
    document.documentElement.setAttribute('data-theme', state.dataTheme)
  }
}

const actions = {
  // 设置主题
  setTheme({ commit }, theme) {
    commit('SET_THEME', theme)
  },
  // 初始化主题
  initTheme({ commit }) {
    commit('INIT_THEME')
  },
  getTheme: ({ state }) => {
    return state.dataTheme || 'light'
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
