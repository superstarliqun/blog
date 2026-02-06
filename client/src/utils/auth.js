import Cookies from 'js-cookie'
import axios from 'axios'

const refreshToken = 'blog_refresh_token'
const accessToken = 'blog_access_token'
const storageKey = '_EMOPING_BOKE_LIQUN'
const enterLoginAttr = '_EMOPING_BOKE_LIQUN_LOGIN_ARRT'
import urls from '@/utils/urls'
const service = axios.create({
  withCredentials: true,
  // baseURL: 'https://apiuser.futurelab.tv',
  baseURL: urls.userCenterApi,
  timeout: 2 * 60 * 1000
})
const storage = JSON.parse(localStorage.getItem(storageKey))

export function getAccessToken() {
  return localStorage.getItem(accessToken)
}

export function getRefreshToken() {
  return localStorage.getItem(refreshToken)
}

// 新增短token存储
export function setAccessToken(token) {
  return localStorage.setItem(accessToken, token)
}

// 新增长token存储
export function setRefreshToken(token) {
  return localStorage.setItem(refreshToken, token)
}

// 删除所有用户信息
export function removeToken() {
  removeUserInfo()
  localStorage.removeItem(accessToken)
  localStorage.removeItem(refreshToken)
  Cookies.remove(accessToken)
  return Cookies.remove(refreshToken)
}

// 存储用户信息
export function setUserInfo(userInfo) {
  localStorage.setItem(storageKey, JSON.stringify(userInfo))
}
// 获取个人用户信息
export function getUserInfo() {
  const storage = JSON.parse(localStorage.getItem(storageKey)) || {}
  if (storage) {
    return storage
  }
}
export function getTicket(platformCode) {
  return service({
    url: '/center/user/getTicketByCookie/' + platformCode,
    method: 'get'
  })
}
export function removeUserInfo() {
  localStorage.removeItem(storageKey)
}
// 存储登录信息属性
export function saveLoginAttr(attr) {
  localStorage.setItem(enterLoginAttr, JSON.stringify(attr))
}
export function getLoginAttr() {
  return JSON.parse(localStorage.getItem(enterLoginAttr))
}
// 获取个人菜单权限
export function getMenuCode() {
  const storage = JSON.parse(localStorage.getItem(storageKey))
  if (storage) {
    return storage.menus
  }
}
// 判断是否有操作权限
export function havePermission(code) {
  if (!code) {
    return true
  }
  if (storage) {
    const codeList = storage.buttonCodeList
    const isExist = codeList.some((item) => {
      if (item === code) {
        return true
      }
    })
    return isExist
  }
  return false
}
