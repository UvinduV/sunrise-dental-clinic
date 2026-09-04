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

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('loginUsername').value.trim();
        const password = document.getElementById('loginPassword').value;

        try {
            await ApiClient.post('/api/auth/login', { username, password });
            ApiClient.setCredentials(username, password);
            showToast(`Welcome back, ${username}!`, 'success');
            refreshNavState();
            showSection('dashboard-section');
            loginForm.reset();
            loadDashboardSection();
        } catch (err) {
            showToast(err.message, 'error');
        }
    });

    // --- Signup ---
    const signupForm = document.getElementById('signupForm');

    signupForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('signupUsername').value.trim();
        const password = document.getElementById('signupPassword').value;
        const confirmPassword = document.getElementById('signupConfirmPassword').value;
        const role = document.getElementById('signupRole').value;

        if (password !== confirmPassword) {
            showToast('Passwords do not match.', 'error');
            return;
        }

        try {
            await ApiClient.post('/api/auth/register', { username, password, role });
            showToast('Account created — you can log in now.', 'success');
            signupForm.reset();
            setTimeout(() => {
                signupCard.hidden = true;
                loginCard.hidden = false;
                document.getElementById('loginUsername').value = username;
                document.getElementById('loginPassword').focus();
            }, 1200);
        } catch (err) {
            showToast(err.message, 'error');
        }
    });
});
