import Cookies from 'js-cookie'

const state = {
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  // 字体状态：默认 'default'
  fontTheme: Cookies.get('fontTheme') || 'default'
}

const mutations = {
  TOGGLE_SIDEBAR: (state) => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  SET_FONT_THEME: (state, theme) => {
    state.fontTheme = theme
    Cookies.set('fontTheme', theme)
    // 核心：直接操作根节点属性，让 CSS 变量生效
    document.documentElement.setAttribute('data-font-theme', theme)
  },
  // 为了确保页面刷新后立即生效，建议在初始化时调用的逻辑
  INIT_FONT_THEME: (state) => {
    document.documentElement.setAttribute('data-font-theme', state.fontTheme)
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  // 设置全局字体主题
  setFontTheme({ commit }, theme) {
    commit('SET_FONT_THEME', theme)
  },
  initFontTheme({ commit }) {
    commit('INIT_FONT_THEME')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
