import { api } from '../utils/http-util.js'
import {login} from "./auth.js";

document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
});

async function handleRegister(event) {
    event.preventDefault();
    const form = event.target;
    const email = form.querySelector('input[name="email"]')?.value?.trim();
    const password = form.querySelector('input[name="password"]')?.value?.trim();
    const firstName = form.querySelector('input[name="firstName"]')?.value?.trim();
    const lastName = form.querySelector('input[name="lastName"]')?.value?.trim();

    try {
        await register({email, password, lastName, firstName});
        // Redirect or update UI on successful login
        console.log('Register successful, redirecting to login page...');
        window.location.assign('/index.html')
    } catch (error) {
        console.error('Register failed:', error);
        // Show error message to user
    }

}

export async function register(userData) {
    try {
        const response = await api.post('/v1/auth/register', userData);
        return response.data;
    } catch (error) {
        throw error.response?.data || error.message;
    }
}

