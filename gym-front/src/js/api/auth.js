import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

async function handleLogin(event) {
    event.preventDefault();
    const form = event.target;
    const email = form.querySelector('input[name="email"]')?.value?.trim();
    const password = form.querySelector('input[name="password"]')?.value?.trim();
    
    try {
        await login({ email, password });
        // Redirect or update UI on successful login
        console.log('Login successful!');
    } catch (error) {
        console.error('Login failed:', error);
        // Show error message to user
    }
}

async function login(userData) {
    try {
        const response = await api.post('/v1/auth/login', userData);
        localStorage.setItem("jwt_token", response.data);
        return response.data;
    } catch (error) {
        throw error.response?.data || error.message;
    }
}

export { login };