<template>
  <div class="recipe-search-container">
    <header class="header">
      <div class="header-container">
        <div class="header-logo">
          <img src="@/assets/favicon.png" alt="Blog Search Logo" class="header-logo-img" />
          <h1 class="header-title">MixingBowl</h1>
        </div>
      </div>
    </header>

    <div class="search-section">
      <!-- <div class="logo-container">
        <div class="logo-icon">
          <img src="@/assets/favicon.png" alt="calendar icon" width="70" height="70">

        </div>
        <h1 class="search-title">Find a Recipe</h1>
      </div> -->
      
      <div class="search-form">
        <div class="input-wrapper">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="Enter recipe name..." 
            class="search-input"
            @keyup.enter="searchRecipes"
          />
          <button 
            v-if="searchQuery.length > 0" 
            @click="clearSearch" 
            class="clear-button"
            aria-label="Clear search"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
        </div>
        
        <button 
          @click="searchRecipes" 
          class="search-button"
          :disabled="isLoading"
        >
          <span v-if="!isLoading" class="button-text">Search</span>
          <span v-else class="button-text">Searching...</span>
          <svg v-if="!isLoading" xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
          <svg v-else class="spinner" viewBox="0 0 24 24">
            <circle class="spinner-path" cx="12" cy="12" r="10" fill="none" stroke-width="3"></circle>
          </svg>
        </button>
      </div>
      
      <div class="search-suggestions" v-if="!isLoading && searchQuery.length === 0 && !blogResults.length">
        <h3 class="suggestions-title">Popular Searches</h3>
        <div class="suggestion-tags">
          <button 
            v-for="(tag, index) in popularTags" 
            :key="index" 
            @click="selectTag(tag)" 
            class="suggestion-tag"
          >
            {{ tag }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- Results Section -->
    <div v-if="blogResults.length > 0" class="results-section">
      <h2 class="results-title">Recipe Blog Results</h2>
      <p class="results-count">Found {{ totalResults }} results for "{{ lastSearchQuery }}"</p>
      
      <div class="blog-list">
        <div v-for="(blog, index) in blogResults" :key="index" class="blog-card">
          <div class="blog-content">
            <h3 class="blog-title" v-html="blog.title"></h3>
            <p class="blog-description" v-html="blog.description"></p>
            <div class="blog-meta">
              <span class="blog-author">{{ blog.bloggername }}</span>
              <span class="blog-date">{{ formatDate(blog.postdate) }}</span>
            </div>
          </div>
          <a :href="blog.link" target="_blank" rel="noopener noreferrer" class="blog-link">
            Read More
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"></line>
              <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
          </a>
        </div>
      </div>
      
      <div class="pagination" v-if="blogResults.length > 0">
        <button 
          @click="loadPreviousPage" 
          class="pagination-button" 
          :disabled="currentPage === 1"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
          Previous
        </button>
        <span class="page-info">Page {{ currentPage }} of {{ totalPages }}</span>
        <button 
          @click="loadNextPage" 
          class="pagination-button" 
          :disabled="currentPage >= totalPages"
        >
          Next
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"></polyline>
          </svg>
        </button>
      </div>
    </div>
    
    <!-- Loading State -->
    <div v-if="isLoading && !blogResults.length" class="loading-container">
      <svg class="loading-spinner" viewBox="0 0 50 50">
        <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
      </svg>
      <p class="loading-text">Searching for recipes...</p>
    </div>
    
    <!-- No Results State -->
    <div v-if="noResults" class="no-results">
      <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="10"></circle>
        <line x1="12" y1="8" x2="12" y2="12"></line>
        <line x1="12" y1="16" x2="12.01" y2="16"></line>
      </svg>
      <h3>No recipes found</h3>
      <p>Try different keywords or check out our popular searches</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'

// State
const searchQuery = ref('')
const lastSearchQuery = ref('')
const isLoading = ref(false)
const blogResults = ref([])
const totalResults = ref(0)
const currentPage = ref(1)
const resultsPerPage = ref(2)
const noResults = ref(false)

const popularTags = ref([
  '김치찌개', '된장찌개', '비빔밥', '불고기', '떡볶이', 
  '파스타', '피자', '샐러드', '스테이크', '디저트'
])

// Computed properties
const totalPages = computed(() => {
  return Math.ceil(totalResults.value / resultsPerPage.value)
})

// Methods
const searchRecipes = async () => {
  if (searchQuery.value.trim() === '') return
  
  isLoading.value = true
  noResults.value = false
  currentPage.value = 1
  lastSearchQuery.value = searchQuery.value
  
  try {
    await fetchBlogResults()
    
    // Scroll to results if on first page
    if (currentPage.value === 1 && blogResults.value.length > 0) {
      
      setTimeout(() => {
        scrollToResults ()
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
  console.log(data)

  // Calculate total and paginate
  const total = data.length
  const startIndex = (page - 1) * perPage
  const endIndex = startIndex + perPage
  const paginatedResults = data.slice(startIndex, endIndex)
  
  return {
    lastBuildDate: new Date().toISOString(),
    total: total,
    start: startIndex + 1,
    display: paginatedResults.length,
    items: paginatedResults
  }

}

const fetchBlogResults = async () => {
  await new Promise(resolve => setTimeout(resolve, 1500))

  isLoading.value = true
  
  try {
    // This would typically be a server-side API call to avoid exposing your API keys

    const response = await axios.post('http://localhost:5001/search', {
      text: searchQuery.value
    })

    const data = generateBlogResult(response.data.blog_data.items, currentPage.value, resultsPerPage.value)
    
    blogResults.value = data.items
    totalResults.value = data.total
    
    if (blogResults.value.length === 0) {
      noResults.value = true
    }
  } catch (error) {
    console.error('Error fetching blog results:', error)
    throw error
  } finally {
    isLoading.value = false
  }
}

const loadNextPage = async () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    isLoading.value = true
    
    try {
      await fetchBlogResults()
    } catch (error) {
      console.error('Error loading next page:', error)
    } finally {
      isLoading.value = false
      scrollToResults()
    }
  }
}

const loadPreviousPage = async () => {
  if (currentPage.value > 1) {
    currentPage.value--
    isLoading.value = true
    
    try {
      await fetchBlogResults()
    } catch (error) {
      console.error('Error loading previous page:', error)
    } finally {
      isLoading.value = false
      scrollToResults()
    }
  }
}

const scrollToResults = () => {
  document.querySelector('.results-section')?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

const clearSearch = () => {
  searchQuery.value = ''
}

const selectTag = (tag) => {
  searchQuery.value = tag
  searchRecipes()
}

const formatDate = (dateString) => {
  // Convert YYYYMMDD to YYYY-MM-DD
  if (dateString && dateString.length === 8) {
    const year = dateString.substring(0, 4)
    const month = dateString.substring(4, 6)
    const day = dateString.substring(6, 8)
    return `${year}-${month}-${day}`
  }
  return dateString
}

</script>

<style scoped>
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

.recipe-search-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #fafafa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

.search-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1.5rem;
  /*
  background-color: #f0f0f0;
  background-image: linear-gradient(135deg, #e6e6e6 0%, #f5f5f5 100%);
  color: #555;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  */
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 2rem;
}

.logo-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 1rem;
  color: #666;
  padding: 10px;
}

.logo-icon svg {
  width: 100%;
  height: 100%;
}

.search-title {
  font-size: 2.5rem;
  font-weight: 700;
  text-align: center;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  color: #555;
}

.search-form {
  display: flex;
  width: 100%;
  max-width: 700px;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.input-wrapper {
  position: relative;
  flex-grow: 1;
}

.search-input {
  width: 100%;
  padding: 1rem 1.25rem;
  padding-right: 2.5rem;
  border: none;
  box-sizing: border-box;
  border-radius: 8px;
  font-size: 1rem;
  color: #555;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.2s;
}

.search-input:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.05), 0 2px 8px rgba(0, 0, 0, 0.05);
}

.search-input::placeholder {
  color: #aaa;
}

.clear-button {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #aaa;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.25rem;
  border-radius: 50%;
}

.clear-button:hover {
  color: #888;
  background-color: #f8f8f8;
}

.search-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0 1.5rem;
  background-color: #4db8ed;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.search-button:hover:not(:disabled) {
  background-color: #666;
}

.search-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spinner {
  animation: rotate 2s linear infinite;
  width: 18px;
  height: 18px;
}

.spinner-path {
  stroke: white;
  stroke-linecap: round;
  animation: dash 1.5s ease-in-out infinite;
}

.search-suggestions {
  width: 100%;
  max-width: 700px;
  margin-top: 1rem;
}

.suggestions-title {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: #666;
}

.suggestion-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.suggestion-tag {
  background-color: rgba(0, 0, 0, 0.05);
  color: #666;
  border: none;
  border-radius: 20px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
}

.suggestion-tag:hover {
  background-color: rgba(0, 0, 0, 0.08);
}

/* Results Section */
.results-section {
  padding: 2rem 1.5rem;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
}

.results-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #555;
  margin-bottom: 0.5rem;
}

.results-count {
  color: #888;
  margin-bottom: 2rem;
  font-size: 0.95rem;
}

.blog-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.blog-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.03);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s, box-shadow 0.2s;
}

.blog-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
}

.blog-content {
  padding: 1.5rem;
  flex-grow: 1;
}

.blog-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #555;
  margin-bottom: 0.75rem;
  line-height: 1.4;
}

.blog-title :deep(b) {
  color: #666;
}

.blog-description {
  color: #777;
  margin-bottom: 1rem;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.blog-description :deep(b) {
  color: #666;
  font-weight: 600;
}

.blog-meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 0.875rem;
}

.blog-link {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 1rem;
  background-color: #f8f8f8;
  color: #666;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.2s;
}

.blog-link:hover {
  background-color: #f0f0f0;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
}

.pagination-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: white;
  border: 1px solid #eee;
  border-radius: 6px;
  color: #666;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
}

.pagination-button:hover:not(:disabled) {
  background-color: #f8f8f8;
  color: #555;
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #888;
  font-size: 0.875rem;
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

/* Responsive Design */
@media (max-width: 768px) {
  .logo-container {
    flex-direction: column;
    gap: 1rem;
  }
  
  .logo-icon {
    margin-right: 0;
  }
  
  .search-title {
    font-size: 2rem;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .search-button {
    height: 3rem;
  }
  
  .results-title {
    font-size: 1.5rem;
  }
}

@media (max-width: 480px) {
  .search-section {
    padding: 2rem 1rem;
  }
  
  .logo-icon {
    width: 40px;
    height: 40px;
  }
  
  .search-title {
    font-size: 1.75rem;
  }
  
  .blog-content {
    padding: 1.25rem;
  }
  
  .blog-title {
    font-size: 1.125rem;
  }
  
  .pagination {
    flex-direction: column;
    gap: 0.75rem;
  }
}
</style>