<template>
  <div class="results-section">
    <h2 class="results-title">Recipe Blog Results</h2>
    <p class="results-count">
      Found {{ totalResults }} results for "{{ searchQuery }}"
    </p>

    <div class="blog-list">
      <div v-for="(blog, index) in results" :key="index" class="blog-card">
        <div class="blog-content">
          <h3 class="blog-title" v-html="blog.title"></h3>
          <p class="blog-description" v-html="blog.description"></p>
          <div class="blog-meta">
            <span class="blog-author">{{ blog.bloggername }}</span>
            <span class="blog-date">{{ formatDate(blog.postdate) }}</span>
          </div>
        </div>
        <a
          :href="blog.link"
          target="_blank"
          rel="noopener noreferrer"
          class="blog-link"
        >
          Read More
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="5" y1="12" x2="19" y2="12"></line>
            <polyline points="12 5 19 12 12 19"></polyline>
          </svg>
        </a>
      </div>
    </div>

    <div class="pagination" v-if="results.length > 0">
      <button
        @click="$emit('page-change', currentPage - 1)"
        class="pagination-button"
        :disabled="currentPage === 1"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <polyline points="15 18 9 12 15 6"></polyline>
        </svg>
        Previous
      </button>
      <span class="page-info">Page {{ currentPage }} of {{ totalPages }}</span>
      <button
        @click="$emit('page-change', currentPage + 1)"
        class="pagination-button"
        :disabled="currentPage >= totalPages"
      >
        Next
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <polyline points="9 18 15 12 9 6"></polyline>
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup>
  defineProps({
    results: {
      type: Array,
      required: true,
    },
    totalResults: {
      type: Number,
      required: true,
    },
    searchQuery: {
      type: String,
      required: true,
    },
    currentPage: {
      type: Number,
      required: true,
    },
    totalPages: {
      type: Number,
      required: true,
    },
  })

  defineEmits(['page-change'])

  const formatDate = (dateString) => {
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

  @media (max-width: 768px) {
    .results-title {
      font-size: 1.5rem;
    }
  }

  @media (max-width: 480px) {
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
