<template>
  <div>
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useAuthStore } from '@/store/auth'

const authStore = useAuthStore()

onMounted(async () => {
  try {
    await authStore.fetchUser()
  } catch (e) {
    console.log('Not logged in')
  }
})
</script>
