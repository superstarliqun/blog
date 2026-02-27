import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import { $post, $get, $postForm, $uploadFile } from './utils/request.js'
import urls from './utils/urls'
import { session } from '@/utils'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
import enLocale from 'element-ui/lib/locale/lang/en'
import Contextmenu from 'vue-contextmenujs'
import VueMeta from 'vue-meta'

// global css
import './assets/css/theme.css'
import './assets/css/common.css'
import '../public/index.scss'
import './assets/css/index.css'
import './assets/css/color.css'
// 引入emoji样式
import './assets/css/emoji.scss'

// 引入表情解析工具（之前封装的）
import { parseEmojiToHtml } from './utils/emojiParser.js'

// global js
import { getParma } from '@/utils/common.js'
// highlight 的样式，依赖包，组件
/* 完整引入 */
if (store.state.local === 'en') {
  Vue.use(ElementUI, { locale: enLocale })
} else {
  Vue.use(ElementUI, { locale: zhLocale })
}
Vue.use(Contextmenu)
// 获取url路径参数
Vue.prototype.$getParma = getParma
/* 全局挂载 */
Vue.prototype.$session = session

Vue.prototype.$post = $post
Vue.prototype.$get = $get
Vue.prototype.$postForm = $postForm
Vue.prototype.$uploadFile = $uploadFile
Vue.prototype.$urls = urls
Vue.prototype.$show = false
// 全局拦截开关
Vue.prototype.$isShowLoading = true
Vue.config.productionTip = false

// 忽略标签·
Vue.config.ignoredElements = ['super-sidebar-right', 'super-container', 'super-header', 'super-main', 'super-content', 'super-sidebar-left', /^super-/]
Vue.prototype.$emoji = parseEmojiToHtml

// 页面元信息
Vue.use(VueMeta)
Vue.config.productionTip = false
// 注册组件
new Vue({
  router,
  store,
  render: (h) => h(App)
}).$mount('#app')
