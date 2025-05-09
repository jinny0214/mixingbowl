<template>
  <form @submit.prevent="handleSubmit">
    <div>
      <input v-model="email" type="email" placeholder="Email" required />
    </div>
    <div>
      <input v-model="password" type="password" placeholder="Password" required minlength="6" />
    </div>
    <div>
      <button type="submit">{{ isLogin ? 'Login' : 'Register' }}</button>
    </div>
    <div class="social-login">
      <p>또는 소셜 계정으로 {{ isLogin ? '로그인' : '회원가입' }}</p>
      <button @click.prevent="handleSocialLogin('google')">구글로 {{ isLogin ? '로그인' : '가입' }}</button>
      <!-- 필요시 카카오/네이버도 추가 가능 -->
    </div>
  </form>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

defineProps({ isLogin: Boolean })

const email = ref('')
const password = ref('')
const router = useRouter()

const handleSubmit = () => {
  if (!email.value || !password.value) {
    alert('이메일과 비밀번호를 입력해주세요.')
    return
  }

  // 여기에 실제 로그인 또는 회원가입 API 호출 추가 예정
  console.log(`${isLogin ? '로그인' : '회원가입'} 요청`, { email: email.value, password: password.value })
  router.push('/')
}

const handleSocialLogin = (provider) => {
  window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`
}
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-width: 300px;
  margin: auto;
}

button {
  padding: 8px;
}

.social-login {
  text-align: center;
}
</style>
