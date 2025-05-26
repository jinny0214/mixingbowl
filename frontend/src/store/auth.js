import { defineStore } from 'pinia'
import axios from 'axios'

axios.defaults.withCredentials = true

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    isAuthenticated: false // 로그인여부
  }),
  actions: {
    async login(email, password) {
      try {
        const response = await axios.post('http://localhost:8080/api/user/login', {
          email,
          password
        })
        this.user = response.data.data
        await this.fetchUser(); // 토큰으로 사용자 정보 가져오기
      } catch (error) {
        console.log('Login failed', error)
        throw error
      }
    },

    async logout() {
      try {
        await axios.post('http://localhost:8080/api/user/logout')
        this.user = null
        this.isAuthenticated = false
      } catch (error) {
        console.log('Logout failed:', error)
      }
    },

    async fetchUser() {
      try {
        const response = await axios.get('http://localhost:8080/api/user/check')
        this.isAuthenticated = true

      } catch (error) {
        this.user = null
        this.isAuthenticated = false
      }
    },
  }
})
