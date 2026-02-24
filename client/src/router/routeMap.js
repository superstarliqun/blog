import BaseLayout from '@/components/BaseLayout'

const routeMap = [
  /* 全屏路由 */
  {
    path: '/login',
    component: () => import('@/views/login'),
    meta: { hideHeader: false, hideFooter: false }
  },
  /* 带头部和菜单的路由 */
  {
    path: '/',
    redirect: '/archives',
    component: BaseLayout,
    children: [
      {
        path: '/',
        name: 'home',
        component: () => import('@/views/home')
      },
      {
        path: '/page/:id',
        name: 'homePage',
        component: () => import('@/views/home')
      },
      {
        path: '/propaganda',
        name: 'propaganda',
        component: () => import('@/views/propaganda')
      },
      {
        path: '/materials',
        name: 'materials',
        component: () => import('@/views/materials')
      },
      {
        path: '/tags',
        name: 'tags',
        component: () => import('@/views/tags')
      },
      {
        path: '/tags/:name',
        name: 'tagsName',
        component: () => import('@/views/tags')
      },
      {
        path: '/category',
        name: 'category',
        component: () => import('@/views/category')
      },
      {
        path: '/category/:categoryName',
        name: 'CategoryDetail',
        component: () => import('@/views/category')
      },
      {
        path: '/link',
        name: 'link',
        component: () => import('@/views/link')
      },
      {
        path: '/feedback',
        name: 'feedback',
        component: () => import('@/views/feedback')
      },
      {
        path: '/archive',
        name: 'archive',
        component: () => import('@/views/archive')
      },
      {
        name: 'perdetailssonal',
        path: '/detailsItem',
        component: () => import('@/views/article/components/detail')
      },
      {
        name: 'postViewer',
        path: '/posts/:id',
        component: () => import('@/views/md/preview')
      },
      {
        path: '/tool',
        name: 'tool',
        // component: () => import('@/views/article/components/edit'),
        component: () => import('@/views/md/editor'),
        meta: { hideHeader: false, hideFooter: true }
      },
      {
        path: '/personal',
        name: 'personal',
        component: () => import('@/views/article/personal')
      },
      {
        path: '/free-api',
        name: 'freeApi',
        component: () => import('@/views/tool/freeApi'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/version-history',
        name: 'VersionHistory',
        component: () => import('@/views/versionHistory')
        // meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/user/manager',
        name: 'articleManager',
        component: () => import('@/views/user/index.vue')
        // meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/questions',
        name: 'questions',
        component: () => import('@/views/questions/doc.vue'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/questions/:id?',
        name: 'questionsById',
        component: () => import('@/views/questions/index.vue'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/error-questions',
        name: 'errorQuestions',
        component: () => import('@/views/questions/error.vue'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/test',
        name: 'test',
        component: () => import('@/views/test/test')
      },
      {
        path: '/1',
        name: '1',
        component: () => import('@/views/test/1')
      },
      {
        path: '/2',
        name: '2',
        component: () => import('@/views/test/2')
      },
      {
        path: '/3',
        name: '3',
        component: () => import('@/views/test/3')
      },
      {
        path: '/4',
        name: '4',
        component: () => import('@/views/test/4')
      },
      {
        path: '/5',
        name: '5',
        component: () => import('@/views/test/associateEditing')
      },
      {
        path: '/6',
        name: '6',
        component: () => import('@/views/test/6')
      },
      {
        path: '/7',
        name: '7',
        component: () => import('@/views/test/volunteerEditing')
      },
      {
        path: '/8',
        name: '8',
        component: () => import('@/views/test/learningTestBank')
      },
      {
        path: '/9',
        name: '9',
        component: () => import('@/views/test/9')
      },
      {
        path: '/10',
        name: '10',
        component: () => import('@/views/test/10')
      },
      {
        path: '/11',
        name: '11',
        component: () => import('@/views/test/progress/index')
      },
      {
        path: '/12',
        name: '12',
        component: () => import('@/views/test/professionScoreLine')
      },
      {
        path: '/13',
        name: '13',
        component: () => import('@/views/test/13')
      },
      {
        path: '/14',
        name: '14',
        component: () => import('@/views/test/fatherPage')
      },
      {
        path: '/15',
        name: '15',
        component: () => import('@/views/test/15')
      },
      {
        path: '/16',
        name: '16',
        component: () => import('@/views/test/16')
      },
      {
        path: '/17',
        name: '17',
        component: () => import('@/views/test/17')
      },
      {
        path: '/18',
        name: '18',
        component: () => import('@/views/test/18')
      },
      {
        path: '/20',
        name: '20',
        component: () => import('@/views/test/20')
      },
      {
        path: '/22',
        name: '22',
        component: () => import('@/views/test/22'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/23',
        name: '23',
        component: () => import('@/views/test/23'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/24',
        name: '24',
        component: () => import('@/views/test/24'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/25',
        name: '25',
        component: () => import('@/views/test/25'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/27',
        name: '27',
        component: () => import('@/views/test/27'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/26',
        name: '26',
        component: () => import('@/views/test/26'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/28',
        name: '28',
        component: () => import('@/views/test/28'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/29',
        name: '29',
        component: () => import('@/views/test/29'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/30',
        name: '30',
        component: () => import('@/views/test/30')
      },
      {
        path: '/31',
        name: '31',
        component: () => import('@/views/test/31')
      },
      {
        path: '/32',
        name: '32',
        component: () => import('@/views/test/32')
      },
      {
        path: '/33',
        name: '33',
        component: () => import('@/views/test/33'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/34',
        name: '34',
        component: () => import('@/views/test/34'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/35',
        name: '35',
        component: () => import('@/views/test/35'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/36',
        name: '36',
        component: () => import('@/views/test/36'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/37',
        name: '37',
        component: () => import('@/views/test/37'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/md',
        name: 'md',
        component: () => import('@/views/md/editor'),
        meta: { hideHeader: false, hideFooter: true }
      },
      {
        path: '/md-preview',
        name: 'md-preview',
        component: () => import('@/views/md/preview')
      },
      {
        path: '/panel',
        name: 'panel',
        component: () => import('@/views/panel')
      },
      {
        path: '/video',
        name: 'video',
        component: () => import('@/views/video')
      },
      {
        path: '/file',
        name: 'file',
        component: () => import('@/views/file')
      },
      {
        path: '/401',
        name: '401',
        component: () => import('@/views/401.vue'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/nas',
        name: 'nas',
        component: () => import('@/views/file/nas'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/file2',
        name: 'file2',
        component: () => import('@/views/file/index2'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/file3',
        name: 'file3',
        component: () => import('@/views/file/index3'),
        meta: { hideHeader: true, hideFooter: true }
      },
      {
        path: '/21',
        name: '21',
        component: () => import('@/views/test/21'),
        meta: { hideHeader: true, hideFooter: true }
      }
    ]
  }
]

export default routeMap
