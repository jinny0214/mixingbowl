<template>
  <div>
    <AuthForm v-model:isLogin="isLogin" />
  </div>
</template>

<script setup>
import AuthForm from '../components/AuthForm.vue'
import { ref } from 'vue'

const isLogin = ref(true)
</script>
