/**
 * 通用防抖函数
 * @param {Function} func - 执行函数
 * @param {Number} wait - 延迟时间(ms)
 * @param {Object} options - 配置项
 * @returns {Function} 防抖函数（带cancel方法）
 */
export function debounce(func, wait, options = {}) {
  let timeout = null
  let lastCall = 0
  const { leading = false, trailing = true, maxWait = Infinity } = options

  const debounced = function (...args) {
    const now = Date.now()
    const self = this

    if (!lastCall && leading) func.apply(self, args)
    if (timeout) clearTimeout(timeout)

    const remaining = wait - (now - lastCall)
    if (remaining <= 0 || remaining > maxWait) {
      clearTimeout(timeout)
      timeout = null
      lastCall = now
      if (!leading) func.apply(self, args)
    } else if (trailing) {
      timeout = setTimeout(() => {
        lastCall = leading ? Date.now() : 0
        timeout = null
        func.apply(self, args)
      }, remaining)
    }
    lastCall = now
  }

  debounced.cancel = () => {
    clearTimeout(timeout)
    timeout = null
    lastCall = 0
  }

  return debounced
}

/**
 * 通用节流函数
 * @param {Function} func - 执行函数
 * @param {Number} wait - 间隔时间(ms)
 * @param {Object} options - 配置项
 * @returns {Function} 节流函数（带cancel方法）
 */
export function throttle(func, wait, options = {}) {
  let timeout = null
  let lastExec = 0
  const { leading = true, trailing = false } = options

  const throttled = function (...args) {
    const now = Date.now()
    const self = this

    if (!lastExec && !leading) lastExec = now
    const remaining = wait - (now - lastExec)

    if (remaining <= 0) {
      if (timeout) clearTimeout(timeout)
      timeout = null
      lastExec = now
      func.apply(self, args)
    } else if (trailing && !timeout) {
      timeout = setTimeout(() => {
        lastExec = leading ? Date.now() : 0
        timeout = null
        func.apply(self, args)
      }, remaining)
    }
  }

  throttled.cancel = () => {
    clearTimeout(timeout)
    timeout = null
    lastExec = 0
  }

  return throttled
}
