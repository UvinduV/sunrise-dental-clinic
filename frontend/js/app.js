//section switching + nav visibility

function showSection(id) {
    document.querySelectorAll('.app-section').forEach((sec) => {
        sec.hidden = sec.id !== id;
    });
    document.getElementById('navLinks').classList.remove('show'); // collapse mobile nav
}

function refreshNavState() {
    const loggedIn = ApiClient.isLoggedIn();
    document.getElementById('navAuthed').style.display = loggedIn ? '' : 'none';
    document.getElementById('navGuest').style.display = loggedIn ? 'none' : '';
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

    document.getElementById('logoutLink').addEventListener('click', (e) => {
        e.preventDefault();
        ApiClient.clearCredentials();
        refreshNavState();
        showSection('home-section');
    });
});
