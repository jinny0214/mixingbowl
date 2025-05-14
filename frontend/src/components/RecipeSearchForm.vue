<template>
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
          @keyup.enter="searchRecipe"
        />
        <button
          v-if="searchQuery.length > 0"
          @click="clearSearch"
          class="clear-button"
          aria-label="Clear search"
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
          >
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
      </div>

      <button @click="handleSearch" class="search-button" :disabled="isLoading">
        <span v-if="!isLoading" class="button-text">Search</span>
        <span v-else class="button-text">Searching...</span>
        <svg
          v-if="!isLoading"
          xmlns="http://www.w3.org/2000/svg"
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <circle cx="11" cy="11" r="8"></circle>
          <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
        </svg>
        <svg v-else class="spinner" viewBox="0 0 24 24">
          <circle
            class="spinner-path"
            cx="12"
            cy="12"
            r="10"
            fill="none"
            stroke-width="3"
          ></circle>
        </svg>
      </button>
    </div>

    <div
      class="search-suggestions"
      v-if="!isLoading && searchQuery.length === 0 && !props.hasResults"
    >
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
</template>

<script setup>
  import { ref } from 'vue'

  const props = defineProps({
    isLoading: Boolean,
    hasResults: Boolean,
  })

  const emit = defineEmits(['search', 'clear', 'openLoginModal'])

  const searchQuery = ref('')
  const popularTags = ref([
    '김치찌개',
    '된장찌개',
    '비빔밥',
    '불고기',
    '떡볶이',
    '파스타',
    '피자',
    '샐러드',
    '스테이크',
    '디저트',
  ])

  const handleSearch = () => {
    console.log('form 에서 search 버튼 클릭')
    // 로그인 안되어 있으면 로그인 모달 창 띄우기
    emit('openLoginModal')
    if (searchQuery.value.trim()) {
      emit('search', searchQuery.value)
    }
  }

  const clearSearch = () => {
    searchQuery.value = ''
    emit('clear')
  }

  const selectTag = (tag) => {
    searchQuery.value = tag
    handleSearch()
  }
</script>

<style scoped>
  .search-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 3rem 1.5rem;
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
    background-color: #407ef2; /* color 수정 필요 */
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

  @media (max-width: 768px) {
    .search-form {
      flex-direction: column;
    }

    .search-button {
      height: 3rem;
    }
  }

  @media (max-width: 480px) {
    .search-section {
      padding: 2rem 1rem;
    }
  }
</style>
