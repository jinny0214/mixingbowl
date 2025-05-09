<template>
  <div>
    <h2>Register</h2>
    <AuthForm :isLogin="false" />
  </div>
</template>

<script setup>
import AuthForm from '../components/AuthForm.vue'
</script>
