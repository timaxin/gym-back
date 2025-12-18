import { api } from '../utils/http-util.js'

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
        await login({email, password});
        // Redirect or update UI on successful login
        console.log('Login successful!');
        window.location.assign('/user_main_page.html')

    } catch (error) {
        console.error('Login failed:', error);
        // Show error message to user
    }
}

export async function login(userData) {
    try {
        const response = await api.post('/v1/auth/login', userData);
        localStorage.setItem("jwt_token", response.data);
        return response.data;
    } catch (error) {
        throw error.response?.data || error.message;
    }
}