<template>
  <div class="login-container">
    <div class="login-card">
      <div class="logo-container">
        <h1 class="logo">MixingBowl</h1>
      </div>
      
      <form @submit.prevent="handleSubmit(isLogin)" class="login-form">
        <h2 class="form-title">{{ isLogin ? '로그인' : '회원가입' }}</h2>
        
        <div class="form-group">
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            placeholder="example@email.com"
            required
            class="form-input"
          />
        </div>
        
        <div class="form-group">
          <div class="password-input-container">
            <input 
              :type="showPassword ? 'text' : 'password'" 
              id="password" 
              v-model="password" 
              required
              class="form-input"
            />
            <button 
              type="button" 
              @click="togglePasswordVisibility" 
              class="password-toggle"
            >
              <span v-if="showPassword">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>
              </span>
              <span v-else>
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
              </span>
            </button>
          </div>
        </div>
        
        <div class="form-actions">
          <label class="remember-me">
            <input type="checkbox" v-model="rememberMe" />
            <span>로그인 상태 유지</span>
          </label>
          <a href="#" class="forgot-password">비밀번호 찾기</a>
        </div>
        
        <button type="submit" class="login-button">{{ isLogin ? '로그인' : '회원가입' }}</button>
      </form>
      
      <div class="divider">
        <span>또는</span>
      </div>
      
      <div class="social-login">
        <button @click="handleSocialLogin('kakao')" class="social-button kakao-button">
          <svg class="social-icon" viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 3C6.48 3 2 6.48 2 10.8C2 13.8 3.9 16.38 6.72 17.7C6.54 18.24 5.88 20.1 5.82 20.34C5.82 20.34 5.76 20.7 6 20.82C6.24 20.94 6.54 20.76 6.54 20.76C6.84 20.58 9.6 18.66 10.32 18.18C10.86 18.24 11.4 18.3 12 18.3C17.52 18.3 22 14.82 22 10.5C22 6.18 17.52 3 12 3Z" />
          </svg>
          카카오톡으로 {{ isLogin ? '로그인' : '가입' }}
        </button>
        
        <button @click="handleSocialLogin('google')" class="social-button google-button">
          <svg class="social-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
              fill="#4285F4"
            />
            <path
              d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
              fill="#34A853"
            />
            <path
              d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
              fill="#FBBC05"
            />
            <path
              d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
              fill="#EA4335"
            />
          </svg>
          Google로 {{ isLogin ? '로그인' : '가입' }}
        </button>
      </div>

      <div class="signup-link">
        <template v-if="isLogin">
          계정이 없으신가요? <a href="#" @click.prevent="isLoginEmit('update:isLogin', false)">회원가입</a>
        </template>
        <template v-else>
          계정이 있으신가요? <a href="#" @click.prevent="isLoginEmit('update:isLogin', true)">로그인</a>
        </template>
        
      </div>
      
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerUser, login } from '../api/auth'

defineProps({ isLogin: Boolean })
const emit = defineEmits(['update:isLogin'])

function isLoginEmit(event, value) {
  emit(event, value)
}

const router = useRouter()

// Form data
const email = ref('')
const password = ref('')
const rememberMe = ref(false)
const showPassword = ref(false)


// Methods
const handleSubmit = async (isLogin) => {
  console.log('handleSubmit isLogin : ', isLogin)
  if (!email.value || !password.value) {
    alert('이메일과 비밀번호를 입력해주세요.')
    return
  }

  // 여기에 실제 로그인 또는 회원가입 API 호출 추가 예정
  console.log(`${isLogin ? '로그인' : '회원가입'} 요청`, { email: email.value, password: password.value })

  if (isLogin) { // 로그인
    try {
      const resLogin = await login(email.value, password.value)

      if (resLogin.data.success) {
        localStorage.setItem('token', resLogin.data.data)
        console.log('로그인 성공')
        router.push('/register')
      } else {
        console.log('로그인 실패')
        console.log(resLogin.data.message)
      }
    } catch (err) {
      console.log('에러')
      console.log(err.response?.data?.message || err.message)
    }
  } else { // 회원가입
    const resRegisterUser = await registerUser(email.value, password.value)
    if (resRegisterUser.data.success) {
      router.push('/')
    } else {
      console.log('회원가입 실패')
      console.log(resRegisterUser.data.message)
    }
  }
}

const handleSocialLogin = (provider) => {
  ///oauth2/authorize
  window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`
}

const handleLogin = () => {
  console.log('Login attempt with:', {
    email: email.value,
    password: password.value,
    rememberMe: rememberMe.value
  })
  // Here you would typically call your authentication API
  alert('로그인 시도: ' + email.value)
}

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
}

const handleKakaoLogin = () => {
  console.log('Kakao login clicked')
  // In a real implementation, this would integrate with Kakao SDK
  alert('카카오톡 로그인을 시도합니다.')
}

const handleGoogleLogin = () => {
  console.log('Google login clicked')
  // In a real implementation, this would integrate with Google OAuth
  alert('Google 로그인을 시도합니다.')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 1rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 2rem;
}

.logo-container {
  text-align: center;
  margin-bottom: 1.5rem;
}

.logo {
  color: #2563eb;
  font-size: 2rem;
  font-weight: 700;
  margin: 0;
}

.form-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1.5rem;
  text-align: center;
}

.form-group {
  margin-bottom: 1.25rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: #4b5563;
}

.form-input {
  box-sizing: border-box;
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  font-size: 1rem;
  color: #1f2937;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.password-input-container {
  position: relative;
}

.password-toggle {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #6b7280;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.password-toggle:hover {
  color: #4b5563;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  font-size: 0.875rem;
}

.remember-me {
  display: flex;
  align-items: center;
  color: #4b5563;
  cursor: pointer;
}

.remember-me input {
  margin-right: 0.5rem;
}

.forgot-password {
  color: #2563eb;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.login-button {
  width: 100%;
  padding: 0.75rem 1rem;
  background-color: #2563eb;
  color: white;
  border: none;
  border-radius: 0.5rem;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.login-button:hover {
  background-color: #1d4ed8;
}

.divider {
  display: flex;
  align-items: center;
  margin: 1.5rem 0;
  color: #6b7280;
  font-size: 0.875rem;
}

.divider::before,
.divider::after {
  content: "";
  flex: 1;
  height: 1px;
  background-color: #e5e7eb;
}

.divider span {
  padding: 0 1rem;
}

.social-login {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.social-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  border: none;
}

.kakao-button {
  background-color: #FEE500;
  color: #000000;
}

.kakao-button:hover {
  background-color: #E6CF00;
}

.google-button {
  background-color: white;
  color: #4b5563;
  border: 1px solid #d1d5db;
}

.google-button:hover {
  background-color: #f3f4f6;
}

.social-icon {
  width: 1.25rem;
  height: 1.25rem;
  margin-right: 0.75rem;
}

.signup-link {
  text-align: center;
  font-size: 0.875rem;
  color: #4b5563;
}

.signup-link a {
  color: #2563eb;
  font-weight: 500;
  text-decoration: none;
}

.signup-link a:hover {
  text-decoration: underline;
}

@media (max-width: 480px) {
  .login-card {
    padding: 1.5rem;
  }
  
  .form-actions {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }
}
</style>