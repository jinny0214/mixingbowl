<template>
  <div class="modal-overlay" v-if="show" @click="handleClose">
    <div class="login-modal" @click.stop>
      <button class="modal-close" @click="handleClose">
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
        <h2>{{ isSignupMode ? 'Sign Up' : 'Login' }}</h2>
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

      <form @submit.prevent="handleLogin()">
        <div v-if="loginTab === 'email'" class="login-form">
          <div class="form-group">
            <label for="email">Email</label>
            <input
              ref="emailInput"
              type="email"
              id="email"
              v-model="email"
              placeholder="your@email.com"
              class="login-input"
              required
            />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <div class="password-input-wrapper">
              <input
                :type="showPassword ? 'text' : 'password'"
                id="password"
                v-model="password"
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

          <!-- <div class="forgot-password" v-if="!isSignupMode">
            <a href="#">Forgot password?</a>
          </div> -->

          <!-- <button type="submit" class="login-button" @click="handleLogin"> -->
          <button type="submit" class="login-button">
            {{ isSignupMode ? 'Sign Up' : 'Login' }}
          </button>
        </div>
      </form>

      <div v-if="loginTab === 'social'" class="social-login">
        <button
          class="social-button google"
          @click="handleSocialLogin('google')"
        >
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

        <button class="social-button kakao" @click="handleSocialLogin('kakao')">
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

        <button class="social-button naver" @click="handleSocialLogin('naver')">
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
        <template v-if="isSignupMode">
          Already have an account?
          <a href="#" @click.prevent="isSignupMode = false">Log in</a>
        </template>
        <template v-else>
          Don't have an account?
          <a href="#" @click.prevent="isSignupMode = true">Sign up</a>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { useAuthStore } from '@/store/auth'
import { register } from '@/services/auth'

const auth = useAuthStore()

const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
  isSignupMode: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['close', 'signup', 'login-success'])

const loginTab = ref('email')
const email = ref('')
const password = ref('')
const showPassword = ref(false)
const isSignupMode = ref(props.isSignupMode)
const emailInput = ref(null)

watch(
  () => props.isSignupMode,
  (newValue) => {
    isSignupMode.value = newValue
  }
)

watch(
  () => props.show,
  (newValue) => {
    if (newValue) {
      nextTick(() => {
        if (emailInput.value) {
          emailInput.value.focus()
        }
      })
    }
  }
)

const handleClose = () => {
  emit('close')
  isSignupMode.value = false
  email.value = ''
  password.value = ''
}

const handleLogin = async () => {
  try {
    if (isSignupMode.value) {
      await register(email.value, password.value)
      isSignupMode.value = false
      // handleClose()
    } else {
      await auth.login(email.value, password.value)
      emit('login-success')
      handleClose()
    }
  } catch (error) {
    console.error('Login failed:', error)
    alert(error.response.data.message)
  }
}

const handleSocialLogin = (provider) => {
  // TODO: Implement social login logic
  console.log('Social login with:', provider)
  // provider == 'google'
  window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`

  handleClose()
}

</script>

<style scoped>
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
  background-color: #407ef2;
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
