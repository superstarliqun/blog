import { asyncRoutes, constantRoutes } from '@/router'

/**
 * 根据路由meta判断当前用户是否有该权限
 * @param menuCodes
 * @param route
 */
function hasPermission(menuCodes, route) {
  if (route.meta) {
    return menuCodes.some(code => {
      if (route.meta.code === code.url) {
        route.meta.opSequence = code.opSequence
        route.meta.title = code.operationName
        return true
      }
    })
  }
}

/**
 * 用递归过滤需要加载的动态路由
 */
export function filterAsyncRoutes(routes, menuCodes) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(menuCodes, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, menuCodes)
      }
      res.push(tmp)
    }
  })
  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      // const accessedRoutes = asyncRoutes
      // 对路由排序
      sortRoute(accessedRoutes)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}
// 对路由进行排序
// eslint-disable-next-line no-unused-vars
function sortRoute(list) {
  list.sort(handleSort)
  if (list && list.length > 0) {
    list.forEach(item => {
      if (item.children) {
        sortRoute(item.children)
      }
    })
  }
}
function handleSort(a, b) {
  return a.meta.opSequence - b.meta.opSequence
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
