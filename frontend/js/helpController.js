// help get content
async function loadHelpSection() {
    const content = document.getElementById('helpContent');
    content.innerHTML = '<p class="text-muted">Loading…</p>';

    try {
        const sections = await ApiClient.get('/api/help');

        content.innerHTML = sections.map(s => `
            <div class="app-card mb-3">
                <h5 class="mb-3">${s.title}</h5>
                <ol class="mb-0">
                    ${s.steps.map(step => `<li>${step}</li>`).join('')}
                </ol>
            </div>
        `).join('');
    } catch (err) {
        content.innerHTML = `<div class="alert alert-danger">${err.message}</div>`;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-section="help-section"]').forEach((el) => {
        el.addEventListener('click', loadHelpSection);
    });
});
