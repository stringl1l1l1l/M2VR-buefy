import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '@/views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
  {
    // Document title tag
    // We combine it with defaultDocumentTitle set in `src/main.js` on router.afterEach hook
    meta: {
      title: 'Dashboard'
    },
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    meta: {
      title: 'Mark'
    },
    path: '/Mark',
    name: 'mark',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "tables" */ '@/views/MarkView.vue')
  },
  {
    meta: {
      title: 'Workflow'
    },
    path: '/workflow',
    name: 'workflow',
    component: () => import(/* webpackChunkName: "tables" */ '@/views/WorkflowView.vue'),
    children: [
      {
        path: 'topic',
        component: () => import(/* webpackChunkName: "tables" */ '@/views/TopicView.vue'),
      },
      {
        path: 'video',
        component: () => import(/* webpackChunkName: "tables" */ '@/views/VideoMarkView.vue'),
      },
      {
        path: 'mark',
        component: () => import(/* webpackChunkName: "tables" */ '@/views/MarkView.vue'),
      },
    ]
  },
  {
    meta: {
      title: 'Video'
    },
    path: '/video',
    name: 'video',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "tables" */ '@/views/VideoView.vue')
  }
]

const router = new VueRouter({
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  }
})

export default router

export function useRouter() {
  return router
}
