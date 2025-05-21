import axios from "axios";

axios.defaults.withCredentials = true

export const register = async (email, password) => {
  return await axios.post('http://localhost:8080/api/user/register', {
    email,
    password
  })
}

export const login = async (email, password) => {
  return await axios.post('http://localhost:8080/api/user/login', {
    email,
    password
  })
}