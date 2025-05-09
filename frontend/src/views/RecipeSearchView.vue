<template>
  <div class="recipe-search-container">
    <div class="search-card">
      <h1 class="search-title">Find a Recipe</h1>
      
      <div class="search-form">
        <div class="input-wrapper">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="Enter ingredients or recipe name..." 
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
        
        <button @click="searchRecipes" class="search-button">
          <span class="button-text">Search</span>
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
        </button>
      </div>
      
      <div class="search-suggestions" v-if="!isSearching && searchQuery.length === 0">
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
      
      <div v-if="isSearching" class="loading-indicator">
        <svg class="spinner" viewBox="0 0 50 50">
          <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
        </svg>
        <span>Searching recipes...</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const searchQuery = ref('')
const isSearching = ref(false)
const popularTags = ref([
  'Pasta', 'Chicken', 'Vegetarian', 'Quick Meals', 'Desserts', 
  'Breakfast', 'Healthy', 'Korean', 'Italian'
])

const searchRecipes = async () => {
  if (searchQuery.value.trim() === '') return
  
  isSearching.value = true
  
  console.log('input value : ', searchQuery.value)

  try {
    const res = await axios.post('http://localhost:5001/search', {
      text: searchQuery.value
    })
    isSearching.value = false
    console.log('response: ', res)
  } catch (err) {
    console.log('err..', err)
  }

  // Simulate API call
  /*
  setTimeout(() => {
    console.log('Searching for:', searchQuery.value)
    isSearching.value = false
    // Here you would typically call your recipe search API
    alert(`Searching for recipes with: ${searchQuery.value}`)
  }, 1500)
  */
}

const clearSearch = () => {
  searchQuery.value = ''
}

const selectTag = (tag) => {
  searchQuery.value = tag
  searchRecipes()
}
</script>

<style scoped>
.recipe-search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f7ff;
  padding: 1rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

.search-card {
  width: 100%;
  max-width: 600px;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 78, 179, 0.1);
  padding: 2.5rem;
}

.search-title {
  color: #1a56db;
  font-size: 2.25rem;
  font-weight: 700;
  margin-bottom: 2rem;
  text-align: center;
}

.search-form {
  display: flex;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.input-wrapper {
  position: relative;
  flex-grow: 1;
}

.search-input {
  box-sizing: border-box;
  width: 100%;
  padding: 0.875rem 1rem;
  padding-right: 2.5rem;
  border: 2px solid #d1ddf0;
  border-radius: 8px;
  font-size: 1rem;
  color: #1e3a8a;
  transition: all 0.2s;
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.25);
}

.search-input::placeholder {
  color: #94a3b8;
}

.clear-button {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #94a3b8;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.25rem;
  border-radius: 50%;
}

.clear-button:hover {
  color: #64748b;
  background-color: #f1f5f9;
}

.search-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0 1.25rem;
  background-color: #2563eb;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-button:hover {
  background-color: #1d4ed8;
}

.button-text {
  display: inline-block;
}

.search-suggestions {
  margin-top: 2rem;
}

.suggestions-title {
  font-size: 1rem;
  font-weight: 600;
  color: #1e3a8a;
  margin-bottom: 1rem;
}

.suggestion-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.suggestion-tag {
  background-color: #e0edff;
  color: #1e40af;
  border: none;
  border-radius: 20px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
}

.suggestion-tag:hover {
  background-color: #bfdbfe;
}

.loading-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
  color: #1e40af;
}

.spinner {
  animation: rotate 2s linear infinite;
  width: 40px;
  height: 40px;
}

.path {
  stroke: #3b82f6;
  stroke-linecap: round;
  animation: dash 1.5s ease-in-out infinite;
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

@media (max-width: 640px) {
  .search-card {
    padding: 1.5rem;
  }
  
  .search-title {
    font-size: 1.75rem;
    margin-bottom: 1.5rem;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .search-button {
    height: 3rem;
  }
}
</style>