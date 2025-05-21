import { createRouter, createWebHistory } from "vue-router";

const routes = [
  { path: '/', name: 'Home', component: () => import('@/views/RecipeSearchView.vue') },
  { path: '/search', name: 'Search', component: () => import('@/views/RecipeSearchView.vue') },
  // { path: '/oauth2/redirect', name: 'OAuth2Redirect', component: () => import('@/components/HelloWorld.vue')}
  { path: '/oauth2/redirect', name: 'OAuth2Redirect', component: () => import('@/views/RecipeSearchView.vue')}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// router.beforeEach((to, from, next) => {
// })

export default router