// Login and Signup api

document.addEventListener('DOMContentLoaded', () => {
    const loginCard = document.getElementById('loginCard');
    const signupCard = document.getElementById('signupCard');

    document.getElementById('showSignup').addEventListener('click', (e) => {
        e.preventDefault();
        loginCard.hidden = true;
        signupCard.hidden = false;
    });

    document.getElementById('showLogin').addEventListener('click', (e) => {
        e.preventDefault();
        signupCard.hidden = true;
        loginCard.hidden = false;
    });

    // --- Login ---
    const loginForm = document.getElementById('loginForm');
    const loginError = document.getElementById('loginError');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        loginError.hidden = true;

        const username = document.getElementById('loginUsername').value.trim();
        const password = document.getElementById('loginPassword').value;

        try {
            await ApiClient.post('/api/auth/login', { username, password });
            ApiClient.setCredentials(username, password);
            refreshNavState();
            showSection('appointments-section');
            loginForm.reset();
            loadAppointmentsSection();
        } catch (err) {
            loginError.textContent = err.message;
            loginError.hidden = false;
        }
    });

    // --- Signup ---
    const signupForm = document.getElementById('signupForm');
    const signupError = document.getElementById('signupError');
    const signupSuccess = document.getElementById('signupSuccess');

    signupForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        signupError.hidden = true;
        signupSuccess.hidden = true;

        const username = document.getElementById('signupUsername').value.trim();
        const password = document.getElementById('signupPassword').value;
        const confirmPassword = document.getElementById('signupConfirmPassword').value;
        const role = document.getElementById('signupRole').value;

        if (password !== confirmPassword) {
            signupError.textContent = 'Passwords do not match.';
            signupError.hidden = false;
            return;
        }

        try {
            await ApiClient.post('/api/auth/register', { username, password, role });
            signupSuccess.textContent = 'Account created — you can log in now.';
            signupSuccess.hidden = false;
            signupForm.reset();
            setTimeout(() => {
                signupCard.hidden = true;
                loginCard.hidden = false;
                document.getElementById('loginUsername').value = username;
                document.getElementById('loginPassword').focus();
            }, 1200);
        } catch (err) {
            signupError.textContent = err.message;
            signupError.hidden = false;
        }
    });
});
