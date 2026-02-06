/**
 * @author MingYue
 * @date 2025-12-16
 * @Description: axios基本配置
 */

import axios from 'axios'
import { setAccessToken, setRefreshToken, getAccessToken, getRefreshToken } from './auth'
import urls from './urls'
import qs from 'qs'
import { MessageBox } from 'element-ui'

// 是否已经提醒token过期
var isWarnToken = false
const service = axios.create({
  baseURL: urls.BASE_URL,
  // withCredentials: true,
  timeout: 60000
})

// 请求拦截
service.interceptors.request.use(
  (config) => {
    if (getAccessToken() && config.url.indexOf('/auth/refresh') === -1) {
      config.headers['Authorization'] = getAccessToken()
    }
    config.headers.common['X-Requested-With'] = 'XMLHttpRequest'
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)
// 返回拦截
service.interceptors.response.use(
  (response) => {
    const res = response.data
    res.msg = res.msg || res.message
    if (res.code === 401) {
      if (!isWarnToken) {
        tokenLoseEfficacy()
      }
      return res
    } else {
      return res
    }
  },
  async (error) => {
    const { response } = error
    // const result = error.response
    const config = response.config
    // 第一次刷新token失败
    if (response.status === 401 && !config._retry && config.url.indexOf('/auth/refresh') === -1) {
      const accessToken = await refreshToken()
      if (accessToken === undefined) {
        return false
      }
      config._retry = true
      config.headers.Authorization = accessToken
      return service(config)
    } else if (response.status === 500) {
      console.log('TODO @MingYue：这是接口拦截后期做')
    } else {
      // const token = getAccessToken()
      // window.location.href = '/401'
      tokenLoseEfficacy()
      return Promise.reject(error)
    }
    // else {`
    //   tokenLoseEfficacy()
    //   return Promise.reject(error)
    // }
  }
)
// 通过长token获取短token
function refreshToken() {
  return service({
    url: '/auth/refresh',
    method: 'post',
    headers: { Authorization: 'Bearer ' + getRefreshToken() },
    paramsSerializer: (params) => qs.stringify(params, { indices: false })
  }).then((res) => {
    if (res.code === 0) {
      setAccessToken(res.data.accessToken)
      if (res.data.refreshToken != null) {
        setRefreshToken(res.data.refreshToken)
      }
      return res.data.accessToken
    } else {
      tokenLoseEfficacy()
    }
  })
}

// token失效
function tokenLoseEfficacy() {
  MessageBox.alert('您的登录信息已失效,请重新登录', '确认退出', {
    confirmButtonText: '确定',
    callback: (action) => {
      if (action === 'confirm') {
        window.location.href = '/login'
      }
      if (action === 'cancel') {
        console.log('暂时不处理')
      }
    }
  })
}
// post请求
const $post = (url, data) =>
  service({
    url: url,
    method: 'post',
    data: data
  })
// post form
const $postForm = (url, data) =>
  service({
    url: url,
    method: 'post',
    // data: qs.stringify(data, { arrayFormat: 'indices', allowDots: true })
    data: qs.stringify(data)
  })
// get请求
const $get = (url, params, suffixUrl, headers) => {
  if (suffixUrl) {
    suffixUrl.forEach((item, index) => {
      url = url.concat(`/${item}`)
    })
  }
  if (params) {
    for (const i in params) {
      if (params[i] === null || params[i] === undefined || params[i] === '') {
        delete params[i]
      }
    }
  }
  return service({
    url: url,
    method: 'get',
    params,
    headers: headers,
    paramsSerializer: (params) => {
      return qs.stringify(params, {
        indices: false
      })
    }
  })
}
// post请求
const $uploadFile = (url, data) =>
  service({
    method: 'post',
    url: url,
    headers: { 'Content-Type': 'multipart/form-data' },
    data: data
  })

// 下载文件
function $downloadFile(url, fileName, params) {
  service({
    method: 'get',
    url: url,
    params,
    responseType: 'blob'
  })
    .then((res) => {
      const blob = new Blob([res], { type: 'application/download' })
      const objectUrl = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.download = fileName
      a.href = objectUrl
      a.click()
    })
    .catch((e) => {
      console.error(e)
      return e
    })
}

export { $post, $get, $postForm, $uploadFile, $downloadFile, tokenLoseEfficacy }
