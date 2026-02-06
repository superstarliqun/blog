import Vue from 'vue'
import VueRouter from 'vue-router'
import routeMap from './routeMap.js'

Vue.use(VueRouter)

/*
设置了语言方案时  属性name必加
*/
const routes = [...routeMap]

const router = new VueRouter({
  mode: 'history',
  base: '/',
  routes
})

// 路由拦截
const beforeEach = async (toRoute, fromRoute, next) => {
  // 网页标题
  if (toRoute.meta.title) document.title = toRoute.meta.title
  next()
}

router.beforeEach(beforeEach)

export default router
