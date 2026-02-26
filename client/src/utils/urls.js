/**
 * @author wlq
 * @date 2023/07/29
 * @Description: 管理接口路径
 */

const urls = {
  BASE_URL: '/api',

  // 登录接口
  login: '/login',
  // 数字验证码
  verifyCode: '/verifyCode.jpg',
  // 上传文件
  uploadFile: '/minio/upload',
  // 获取用户信息
  getUserInfo: '/getUserInfo',
  // 获取天气接口
  getWeather: '/portal/weather/get',
  // 文章分页
  articlePage: '/portal/article/list',
  // 文章分页
  articleMyPage: '/article/list',
  // 文章详情
  articleGet: '/portal/article/get',
  // 文章保存
  articleSave: '/article/save',
  // 文章删除
  articleRemove: '/article/remove',
  // 高亮搜索
  elasticSearchFindHighlight: '/search/findHighlight',
  // 微信扫码半长连接
  sseWxLink: '/subscribe',
  // 微信验证码
  sseWxcode: '/login/fetch',
  // 微信重新获取验证码
  sseWxRefresh: '/refresh',
  // 文章主题分类
  getDefaultCategory: '/portal/categories',
  // 获取书标签
  getBookmarksList: '/bookmarks/list',
  // 删除书标签
  deleteBookmarks: '/bookmarks/delete',
  // 保存JSON文件
  saveJsonApi: '/api/save',
  // 保存JSON文件
  getQuestions: '/questions',
  // 获取错误集合
  getQuestionsError: '/questionsError/page',
  // 保存错误集合
  saveQuestionsError: '/questionsError/add',
  // 删除错误集合
  removeQuestionsError: '/questionsError/delete',
  // panel更新corn
  updateCorn: '/panel/updateCorn',
  // panel执行任务
  execute: 'panel/execute',
  // 获取地址
  getUrl: 'panel/getUrl',
  // 获取地址
  getTagList: 'tags/list',
  // 获取友链
  getLinkList: 'link/list',
  // 申请友链
  addLink: 'link/add',
  // 查询指定年份每天文章数量
  dailyCounts: 'archive/posts/calendar/daily-counts',
  // 查询归档列表
  queryArchiveList: 'archive/fromArchiveList',
  // 查询归档列表
  getComment: '/comment',
  // 查询归档列表
  addComment: '/comment/add',
  // rss列表
  rss: '/rss/list'
}

export default urls
