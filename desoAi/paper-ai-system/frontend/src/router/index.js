import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/workspace',
    children: [
      { path: 'workspace', component: () => import('../views/Workspace.vue') },
      { path: 'papers', component: () => import('../views/MyPapers.vue') },
      { path: 'settings', component: () => import('../views/AISettings.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 登录守卫
router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/login' && to.path !== '/register') {
    return '/login'
  }
  if (token && (to.path === '/login' || to.path === '/register')) {
    return '/workspace'
  }
})

export default router
