<template>
  <div class="notice_box ios">
    <div class="notice_title">版权与免责声明</div>
    <ul class="notice_wrapper">
      <li>1. 本网站内容仅供个人学习、研究和欣赏，未经授权禁止用于任何商业用途。</li>
      <li>2. 网站中的代码示例仅用于教育目的，使用时请遵循相关开源协议和授权规定。</li>
      <li>3. 转载或引用本站内容请注明出处，尊重原创，共同维护良好的创作环境。</li>
      <li>4. 网站评论区欢迎理性讨论，请勿发表违反法律法规的言论，共建和谐社区。</li>
      <li>5. 如有内容侵犯您的权益，请通过博客联系方式告知，将立即核实并处理。</li>
      <li>6. 使用本站资源时产生的任何问题与后果需自行承担，请谨慎操作。</li>
    </ul>
  </div>
</template>

<style scoped>
.notice_box.ios {
  margin: 32px 0;
  padding: 14px 16px;
  background: var(--card-background);
  border-radius: 12px;
  border: 0.4px solid #d1d9e0;
  counter-reset: notice-counter;
}

.notice_wrapper li::marker {
  color: #6e6e73;
  font-weight: 500;
}

.notice_title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-color);
  margin-bottom: 8px;
}

.notice_wrapper {
  list-style: none;
  padding: 0;
  margin: 0;

  font-size: 13px;
  line-height: 1.6;
  color: var(--text-color2);
  /* iOS secondary label */

  display: flex;
  flex-direction: column;
  gap: 6px;
}

.notice_wrapper li {
  word-break: break-word;
  text-indent: -0.5em;
  padding-left: 0.5em;
}
</style>
