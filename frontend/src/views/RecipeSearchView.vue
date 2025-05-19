<template>
  <div class="recipe-search-container">
    <header class="header">
      <div class="header-container">
        <div class="header-content">
          <div class="header-logo">
            <img
              src="@/assets/favicon.png"
              alt="Blog Search Logo"
              class="header-logo-img"
            />
            <h1 class="header-title">MixingBowl</h1>
          </div>
          <div class="header-actions">
            <button
              v-if="!auth.isAuthenticated"
              @click="openLoginModal()"
              class="login-button-header"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="login-icon"
              >
                <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"></path>
                <polyline points="10 17 15 12 10 7"></polyline>
                <line x1="15" y1="12" x2="3" y2="12"></line>
              </svg>
              Login
            </button>
            <div v-else class="user-menu">
              <button class="user-button">
                <span class="user-name">{{ auth.user?.name || 'Logout' }}</span>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="18"
                  height="18"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="user-icon"
                >
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                  <circle cx="12" cy="7" r="4"></circle>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>

    <RecipeSearchForm
      :isLoading="isLoading"
      :hasResults="blogResults.length > 0"
      @search="handleSearch"
      @clear="clearSearch"
      @openLoginModal="openLoginModal"
    />

    <RecipeResults
      v-if="blogResults.length > 0"
      :results="blogResults"
      :totalResults="totalResults"
      :searchQuery="lastSearchQuery"
      :currentPage="currentPage"
      :totalPages="totalPages"
      @page-change="handlePageChange"
    />

    <!-- Loading State -->
    <div v-if="isLoading && !blogResults.length" class="loading-container">
      <svg class="loading-spinner" viewBox="0 0 50 50">
        <circle
          class="path"
          cx="25"
          cy="25"
          r="20"
          fill="none"
          stroke-width="5"
        ></circle>
      </svg>
      <p class="loading-text">Searching for recipes...</p>
    </div>

    <!-- No Results State -->
    <div v-if="noResults" class="no-results">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="48"
        height="48"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <circle cx="12" cy="12" r="10"></circle>
        <line x1="12" y1="8" x2="12" y2="12"></line>
        <line x1="12" y1="16" x2="12.01" y2="16"></line>
      </svg>
      <h3>No recipes found</h3>
      <p>Try different keywords or check out our popular searches</p>
    </div>

    <!-- Login Modal -->
    <LoginModal
      :show="showLoginModal"
      @close="closeLoginModal"
      @singup="handleSignup"
      @login-success="handleLoginSuccess"
      :isSignupMode="isSignupMode"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import RecipeSearchForm from '@/components/RecipeSearchForm.vue'
import RecipeResults from '@/components/RecipeResults.vue'
import LoginModal from '@/components/LoginModal.vue'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

// State
const isLoading = ref(false)
const blogResults = ref([])
const totalResults = ref(0)
const currentPage = ref(1)
const resultsPerPage = ref(2)
const noResults = ref(false)
const lastSearchQuery = ref('')
const pendingSearchQuery = ref('')

// Login Modal State
const showLoginModal = ref(false)
const isSignupMode = ref(false)

const socialAccount = ref('')

onMounted(async () => {
  const isNewUser = route.query.isNewUser === 'true'
  const email = route.query.email

  router.replace({ path: route.path })

  try {
    if (isNewUser) {
      // 회원 가입 창 띄우기
      showLoginModal.value = true
      isSignupMode.value = true
      socialAccount.value = email

      // router.replace({
      //   path: '/',
      //   query: { showSignup: true },
      // })
    } else {
      // 기존 유저라면 그대로
      // const redirectPath = route.query.redirect || '/'
      // router.replace(redirectPath)
    }
  } catch (error) {
    console.error('OAuth2 리다이렉트 처리 중 오류:', error)
  }
})

// Computed properties
const totalPages = computed(() => {
  return Math.ceil(totalResults.value / resultsPerPage.value)
})

// Login Modal Methods
const openLoginModal = (query) => {
  pendingSearchQuery.value = query
  showLoginModal.value = true
  document.body.style.overflow = 'hidden' // Prevent scrolling when modal is open
}

const closeLoginModal = () => {
  // search 진행 필요
  showLoginModal.value = false
  document.body.style.overflow = '' // Restore scrolling
}

const handleLoginSuccess = () => {
  if (pendingSearchQuery.value) {
    handleSearch(pendingSearchQuery.value)
    pendingSearchQuery.value = ''
  }
}

const handleSignup = () => {
  router.push('/register')
}

// Search Methods
const handleSearch = async (query) => {
  if (!query.trim()) return

  if (!auth.isAuthenticated) {
    openLoginModal(query)
    return
  }

  isLoading.value = true
  noResults.value = false
  currentPage.value = 1
  lastSearchQuery.value = query

  try {
    await fetchBlogResults(query)

    if (currentPage.value === 1 && blogResults.value.length > 0) {
      setTimeout(() => {
        scrollToResults()
      }, 100)
    }
  } catch (error) {
    console.error('Error searching blogs:', error)
    alert('검색 중 오류가 발생했습니다. 다시 시도해주세요.')
  } finally {
    isLoading.value = false
  }
}

const generateBlogResult = (data, page, perPage) => {
  const total = data.length
  const startIndex = (page - 1) * perPage
  const endIndex = startIndex + perPage
  const paginatedResults = data.slice(startIndex, endIndex)

  return {
    lastBuildDate: new Date().toISOString(),
    total: total,
    start: startIndex + 1,
    display: paginatedResults.length,
    items: paginatedResults,
  }
}

const fetchBlogResults = async (query) => {
  try {
    const response = await axios.post(
      'http://localhost:5001/search',
      {
        text: query,
      },
      {
        withCredentials: false,
      }
    )

    const data = generateBlogResult(
      response.data.blog_data.items,
      currentPage.value,
      resultsPerPage.value
    )

    blogResults.value = data.items
    totalResults.value = data.total

    if (blogResults.value.length === 0) {
      noResults.value = true
    }
  } catch (error) {
    console.error('Error fetching blog results:', error)
    throw error
  }
}

const handlePageChange = async (page) => {
  currentPage.value = page
  isLoading.value = true

  try {
    await fetchBlogResults(lastSearchQuery.value)
  } catch (error) {
    console.error('Error loading page:', error)
  } finally {
    isLoading.value = false
    scrollToResults()
  }
}

const scrollToResults = () => {
  document
    .querySelector('.results-section')
    ?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

const clearSearch = () => {
  blogResults.value = []
  totalResults.value = 0
  noResults.value = false
  lastSearchQuery.value = ''
}
</script>

<style scoped>
.recipe-search-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #fafafa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen,
    Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

.header {
  background-color: white;
  border-bottom: 1px solid #f0f0f0;
  padding: 1rem 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-logo {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.header-logo-img {
  height: 40px;
  width: 40px;
  border-radius: 8px;
}

.header-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #555;
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
}

.login-button-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: #4db8ed;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.login-button-header:hover {
  background-color: #666;
}

.login-icon {
  stroke: white;
}

.user-menu {
  position: relative;
}

.user-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: white;
  color: #555;
  border: 1px solid #eee;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.user-button:hover {
  background-color: #f8f8f8;
}

.user-name {
  max-width: 100px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-icon {
  stroke: #555;
}

/* Loading State */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 1rem;
  color: #777;
}

.loading-spinner {
  width: 60px;
  height: 60px;
  animation: rotate 2s linear infinite;
  margin-bottom: 1rem;
}

.loading-text {
  font-size: 1.125rem;
  color: #666;
}

.path {
  stroke: #777;
  stroke-linecap: round;
  animation: dash 1.5s ease-in-out infinite;
}

/* No Results State */
.no-results {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 1rem;
  color: #888;
  text-align: center;
}

.no-results svg {
  margin-bottom: 1rem;
  color: #ccc;
}

.no-results h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #666;
  margin-bottom: 0.5rem;
}

.no-results p {
  color: #888;
  max-width: 400px;
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}

.login-header {
  text-align: center;
  margin-bottom: 1.5rem;
}

.login-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #555;
  margin-bottom: 0.5rem;
}

.login-header p {
  color: #888;
  font-size: 0.95rem;
}

.login-tabs {
  display: flex;
  border-bottom: 1px solid #eee;
  margin-bottom: 1.5rem;
}

.login-tab {
  flex: 1;
  background: none;
  border: none;
  padding: 0.75rem 0;
  font-size: 1rem;
  color: #888;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.login-tab.active {
  color: #4db8ed;
  font-weight: 500;
}

.login-tab.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #4db8ed;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-size: 0.95rem;
  font-weight: 500;
  color: #666;
}

.login-input {
  width: 100%;
  padding: 0.75rem 1rem;
  box-sizing: border-box;
  border: 1px solid #eee;
  border-radius: 8px;
  font-size: 1rem;
  color: #555;
  transition: all 0.2s;
}

.login-input:focus {
  outline: none;
  border-color: #4db8ed;
  box-shadow: 0 0 0 3px rgba(77, 184, 237, 0.1);
}

.password-input-wrapper {
  position: relative;
}

.toggle-password {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #aaa;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-password:hover {
  color: #666;
}

.forgot-password {
  text-align: right;
  margin-top: -0.5rem;
}

.forgot-password a {
  color: #888;
  font-size: 0.875rem;
  text-decoration: none;
}

.forgot-password a:hover {
  color: #4db8ed;
  text-decoration: underline;
}

.login-button {
  background-color: #4db8ed;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.875rem;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 0.5rem;
}

.login-button:hover {
  background-color: #666;
}

.social-login {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.social-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  padding: 0.875rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #eee;
  background-color: white;
  color: #555;
}

.social-button:hover {
  background-color: #f8f8f8;
}

.social-button:hover {
  background-color: #f8f8f8;
}

.social-button.google:hover {
  border-color: #ea4335;
  color: #ea4335;
}

.social-button.kakao:hover {
  border-color: #fee500;
  color: #000000;
}

.social-button.naver:hover {
  border-color: #03c75a;
  color: #03c75a;
}

.signup-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #888;
  font-size: 0.95rem;
}

.signup-link a {
  color: #4db8ed;
  text-decoration: none;
  font-weight: 500;
}

.signup-link a:hover {
  text-decoration: underline;
}

/* Responsive Design */
@media (max-width: 768px) {
  .header-title {
    font-size: 1.25rem;
  }
}

@media (max-width: 480px) {
  .header-container {
    padding: 0 1rem;
  }
}

@keyframes modalFadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
