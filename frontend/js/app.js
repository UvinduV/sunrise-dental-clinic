//section switching + nav visibility

function showSection(id) {
    document.querySelectorAll('.app-section').forEach((sec) => {
        sec.hidden = sec.id !== id;
    });
    document.querySelectorAll('#mainNav [data-section]').forEach((el) => {
        el.classList.toggle('active', el.dataset.section === id);
    });
    document.getElementById('navLinks').classList.remove('show'); // collapse mobile nav
}

// Maps an appointment status to a Bootstrap badge color class.
function statusBadgeClass(status) {
    switch (status) {
        case 'COMPLETED': return 'text-bg-success';
        case 'CANCELLED': return 'text-bg-danger';
        case 'SCHEDULED': return 'text-bg-info';
        default: return 'text-bg-secondary';
    }
}

// Shows a popup notification. type: 'success' | 'error'
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');

    const toastEl = document.createElement('div');
    toastEl.className = `toast app-toast app-toast-${type}`;
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');
    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>`;

    container.appendChild(toastEl);
    const toast = new bootstrap.Toast(toastEl, { delay: 4000 });
    toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
    toast.show();
}

function refreshNavState() {
    const loggedIn = ApiClient.isLoggedIn();
    document.getElementById('navAuthed').style.display = loggedIn ? '' : 'none';
    document.getElementById('navGuest').style.display = loggedIn ? 'none' : '';
    // Brand click goes to the Dashboard once logged in, otherwise the public home page.
    document.getElementById('navBrand').dataset.section = loggedIn ? 'dashboard-section' : 'home-section';
}

document.addEventListener('DOMContentLoaded', () => {
    refreshNavState();
    showSection('home-section');

    document.querySelectorAll('[data-section]').forEach((el) => {
        el.addEventListener('click', (e) => {
            e.preventDefault();
            showSection(el.dataset.section);
        });
    });

    const logoutModal = new bootstrap.Modal(document.getElementById('logoutConfirmModal'));

    document.getElementById('logoutLink').addEventListener('click', (e) => {
        e.preventDefault();
        logoutModal.show();
    });

    document.getElementById('confirmLogoutBtn').addEventListener('click', () => {
        logoutModal.hide();
        ApiClient.clearCredentials();
        refreshNavState();
        showSection('home-section');
        showToast('You have been logged out.', 'success');
    });

    // Show/hide password toggle button
    document.querySelectorAll('.toggle-password').forEach((btn) => {
        btn.addEventListener('click', () => {
            const input = document.getElementById(btn.dataset.target);
            const icon = btn.querySelector('i');
            const showing = input.type === 'text';
            input.type = showing ? 'password' : 'text';
            icon.classList.toggle('fa-eye', showing);
            icon.classList.toggle('fa-eye-slash', !showing);
        });
    });
});
