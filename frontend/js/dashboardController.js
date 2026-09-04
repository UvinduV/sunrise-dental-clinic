// Dashboard — summary stats, logged-in landing page

async function loadDashboardSection() {
    const today = new Date().toISOString().slice(0, 10);

    try {
        const [patients, appointments, dailyReport, revenueReport] = await Promise.all([
            ApiClient.get('/api/patients'),
            ApiClient.get('/api/appointments'),
            ApiClient.get(`/api/reports/daily-appointments?date=${today}`),
            ApiClient.get(`/api/reports/revenue?from=2000-01-01&to=${today}`)
        ]);

        document.getElementById('statTotalPatients').textContent = patients.length;
        document.getElementById('statTotalAppointments').textContent = appointments.length;
        document.getElementById('statTodayAppointments').textContent = dailyReport.totalAppointments;
        document.getElementById('statTotalRevenue').textContent = revenueReport.totalRevenue;
    } catch (err) {
        showToast(err.message, 'error');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-section="dashboard-section"]').forEach((el) => {
        el.addEventListener('click', loadDashboardSection);
    });
});
