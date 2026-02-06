// 1. 先加载所有表情资源（和你原来的逻辑一致）
const req = require.context('@/assets/img/emoji', false, /\.png$/)
const emojiMap = {}
req.keys().forEach((k) => {
  const name = k.replace('./', '').replace('.png', '')
  emojiMap[name] = req(k)
})

/**
 * 将 [表情名] 格式的文本转换为带 img 标签的 HTML
 * @param {string} text - 包含[表情名]的原始文本
 * @returns {string} 转换后的HTML字符串
 */
export function parseEmojiToHtml(text) {
  text = '<div class="emoji-out">' + text + '</div>'
  if (!text) return ''
  return text.replace(/\[([^\]]+)\]/g, (match, name) => {
    if (emojiMap[name]) {
      return `<img src="${emojiMap[name]}" data-char="${name}" class="emoji-item">`
    }
    return match
  })
}

/**
 * 将带 img 标签的 HTML 还原为 [表情名] 格式的文本（可选，用于反向解析）
 * @param {string} html - 包含表情img标签的HTML
 * @returns {string} 还原后的文本
 */
export function parseHtmlToEmojiText(html) {
  if (!html) return ''
  return html
    .replace(/<img[^>]+data-char=['"]([^'"]+)['"][^>]*>/g, '[$1]')
    .replace(/<(?:.|\n)*?>/gm, '')
}

// 导出表情映射表（方便其他地方使用）
export { emojiMap }
