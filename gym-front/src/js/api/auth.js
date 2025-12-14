import axios from "axios";

const API_BASE_URL = 'http://localhost:8080/api'

const api = axios.create({
    baseUrl: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
})

const submitLoginButton = document.getElementById('login-form')
submitLoginButton.addEventListener('submit', (event) => {
    console.log("HI")
    event.preventDefault()
    const form = event.target
    const email = form.querySelector('input[name="email"]')?.value?.trim()
    const password = form.querySelector('input[name="password"]')?.value?.trim()
    login({email, password})
})

async function login(userData) {
    try {
        const response = await api.post('/v1/auth/login', userData)
        localStorage.setItem("jwt_token", response.data)
        return response.data
    } catch (error) {
        throw error.response?.data || error.message
    }
}

async function register(userData) {
    try {
        const response = await api.post('/v1/auth/register', userData)
        return response.data
    } catch (error) {
        throw error.response?.data || error.message
    }

}
