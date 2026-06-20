import Cookies from 'js-cookie'

const LOCK_DURATION = 6 * 60 * 60 * 1000 // 6小时

const state = {
  dataTheme: Cookies.get('dataTheme') || 'light',
  // 手动切换时间戳，用于判断是否处于手动锁定期间
  lastManualThemeTime: Cookies.get('lastManualThemeTime') ? Number(Cookies.get('lastManualThemeTime')) : 0
}

const mutations = {
  SET_THEME: (state, theme) => {
    state.dataTheme = theme
    Cookies.set('dataTheme', theme)
    document.documentElement.setAttribute('data-theme', theme)
  },
  SET_LAST_MANUAL_THEME_TIME: (state, time) => {
    state.lastManualThemeTime = time
    Cookies.set('lastManualThemeTime', time)
  },
  INIT_THEME: (state) => {
    document.documentElement.setAttribute('data-theme', state.dataTheme)
  }
}

const actions = {
  // 设置主题（手动切换）
  setTheme({ state, commit, dispatch }, theme) {
    commit('SET_THEME', theme)
    // 记录手动切换时间
    const now = Date.now()
    commit('SET_LAST_MANUAL_THEME_TIME', now)
    // 6小时后自动恢复时间判断
    setTimeout(() => {
      dispatch('autoThemeByTime')
    }, LOCK_DURATION)
  },
  // 初始化：刷新页面时判断
  initTheme({ state, commit }) {
    commit('INIT_THEME')
    applyThemeByTime(state, commit)
  },
  // 自动主题判断
  autoThemeByTime({ state, commit }) {
    applyThemeByTime(state, commit)
  },
  getTheme: ({ state }) => {
    return state.dataTheme || 'light'
  }
}

/**
 * 根据当前时间应用主题
 * 规则：6:00 ~ 18:00 → light，其他 → dark
 * 但如果用户手动切换后6小时内，不覆盖
 */
function applyThemeByTime(state, commit) {
  // 检查是否处于手动锁定期间
  const elapsed = Date.now() - state.lastManualThemeTime
  if (elapsed < LOCK_DURATION) {
    return
  }

  const hour = new Date().getHours()
  const theme = (hour >= 6 && hour < 18) ? 'light' : 'dark'
  if (state.dataTheme !== theme) {
    commit('SET_THEME', theme)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
