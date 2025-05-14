<template>
  <div class="recipe-search-container">
    <header class="header">
      <div class="header-container">
        <div class="header-logo">
          <img
            src="@/assets/favicon.png"
            alt="Blog Search Logo"
            class="header-logo-img"
          />
          <h1 class="header-title">MixingBowl</h1>
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
    <div class="modal-overlay" v-if="showLoginModal" @click="closeLoginModal">
      <div class="login-modal" @click.stop>
        <button class="modal-close" @click="closeLoginModal">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>

        <div class="login-header">
          <h2>Login</h2>
          <!-- <p>Access your favorite recipes and save new ones</p> -->
        </div>

        <div class="login-tabs">
          <button
            :class="['login-tab', { active: loginTab === 'email' }]"
            @click="loginTab = 'email'"
          >
            Email Login
          </button>
          <button
            :class="['login-tab', { active: loginTab === 'social' }]"
            @click="loginTab = 'social'"
          >
            Social Login
          </button>
        </div>

        <div v-if="loginTab === 'email'" class="login-form">
          <div class="form-group">
            <label for="email">Email</label>
            <input
              type="email"
              id="email"
              v-model="loginEmail"
              placeholder="your@email.com"
              class="login-input"
            />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <div class="password-input-wrapper">
              <input
                :type="showPassword ? 'text' : 'password'"
                id="password"
                v-model="loginPassword"
                placeholder="Enter your password"
                class="login-input"
              />
              <button
                class="toggle-password"
                @click="showPassword = !showPassword"
                type="button"
              >
                <svg
                  v-if="!showPassword"
                  xmlns="http://www.w3.org/2000/svg"
                  width="20"
                  height="20"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                  <circle cx="12" cy="12" r="3"></circle>
                </svg>
                <svg
                  v-else
                  xmlns="http://www.w3.org/2000/svg"
                  width="20"
                  height="20"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path
                    d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"
                  ></path>
                  <line x1="1" y1="1" x2="23" y2="23"></line>
                </svg>
              </button>
            </div>
          </div>

          <div class="forgot-password">
            <a href="#">Forgot password?</a>
          </div>

          <button class="login-button">Login</button>
        </div>

        <div v-if="loginTab === 'social'" class="social-login">
          <button class="social-button google">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <circle cx="12" cy="12" r="10"></circle>
              <path d="M8 12 L16 12"></path>
              <path d="M12 8 L12 16"></path>
            </svg>
            Continue with Google
          </button>

          <button class="social-button kakao">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <circle cx="12" cy="12" r="10"></circle>
              <path d="M8 15 L12 9 L16 15"></path>
            </svg>
            Continue with Kakao
          </button>

          <button class="social-button naver">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <polygon points="5 3 19 12 5 21 5 3"></polygon>
            </svg>
            Continue with Naver
          </button>
        </div>

        <div class="signup-link">
          Don't have an account?
          <a href="#">Sign up</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, computed } from 'vue'
  import axios from 'axios'
  import RecipeSearchForm from '@/components/RecipeSearchForm.vue'
  import RecipeResults from '@/components/RecipeResults.vue'

  // State
  const isLoading = ref(false)
  const blogResults = ref([])
  const totalResults = ref(0)
  const currentPage = ref(1)
  const resultsPerPage = ref(2)
  const noResults = ref(false)
  const lastSearchQuery = ref('')

  // Login Modal State
  const showLoginModal = ref(false)
  const loginTab = ref('email')
  const loginEmail = ref('')
  const loginPassword = ref('')
  const showPassword = ref(false)

  // Computed properties
  const totalPages = computed(() => {
    return Math.ceil(totalResults.value / resultsPerPage.value)
  })

  // Login Modal Methods
  const openLoginModal = () => {
    console.log('부모 openLoginModal 호출됨')
    showLoginModal.value = true
    document.body.style.overflow = 'hidden' // Prevent scrolling when modal is open
  }

  const closeLoginModal = () => {
    showLoginModal.value = false
    document.body.style.overflow = '' // Restore scrolling
  }

  const handleLogin = () => {
    // Implement login logic here
    console.log('Login with:', loginEmail.value, loginPassword.value)
    closeLoginModal()
    // After successful login, proceed with search
    searchRecipes()
  }

  const selectTag = (tag) => {
    searchQuery.value = tag
    openLoginModal() // Open login modal instead of direct search
  }

  // Search Methods
  const handleSearch = async (query) => {
    console.log('부모 search 버튼 이벤트 받음')
    if (!query.trim()) return

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
    await new Promise((resolve) => setTimeout(resolve, 1500))

    try {
      const response = await axios.post('http://localhost:5001/search', {
        text: query,
      })

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

  /* Login Modal Styles */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    padding: 1rem;
  }

  .login-modal {
    background-color: white;
    border-radius: 12px;
    width: 100%;
    max-width: 450px;
    padding: 2rem;
    position: relative;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
    animation: modalFadeIn 0.3s ease-out;
  }

  .modal-close {
    position: absolute;
    top: 1rem;
    right: 1rem;
    background: none;
    border: none;
    color: #aaa;
    cursor: pointer;
    padding: 0.5rem;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
  }

  .modal-close:hover {
    background-color: #f5f5f5;
    color: #666;
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
    .login-modal {
      padding: 1.5rem;
    }
  }

  @media (max-width: 480px) {
    .login-modal {
      padding: 1.25rem;
    }

    .login-header h2 {
      font-size: 1.25rem;
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
