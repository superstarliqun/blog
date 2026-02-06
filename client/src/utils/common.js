// utils.js

// 获取url路径参数
export function getParma(param) {
  // 获取当前页面的 URL
  const url = new URL(window.location.href)
  // 获取特定参数的值
  return url.searchParams.get(param)
}
