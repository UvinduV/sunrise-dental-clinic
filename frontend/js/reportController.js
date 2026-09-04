// Daily Report / Revenue Report

async function loadDailyReport(date) {
    const errorBox = document.getElementById('dailyReportError');
    const result = document.getElementById('dailyReportResult');
    const countEl = document.getElementById('dailyReportCount');
    const tbody = document.getElementById('dailyReportTableBody');

    errorBox.hidden = true;
    result.hidden = true;

    try {
        const report = await ApiClient.get(`/api/reports/daily-appointments?date=${encodeURIComponent(date)}`);

        countEl.textContent = report.totalAppointments;

        if (report.appointments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted">No appointments on this date.</td></tr>';
        } else {
            tbody.innerHTML = report.appointments.map(a => `
                <tr>
                    <td>${a.appointmentNo}</td>
                    <td>${a.patientName}</td>
                    <td>${a.dentistName}</td>
                    <td>${a.treatmentName}</td>
                    <td>${a.time}</td>
                    <td><span class="badge text-bg-secondary">${a.status}</span></td>
                </tr>
            `).join('');
        }

        result.hidden = false;
    } catch (err) {
        errorBox.textContent = err.message;
        errorBox.hidden = false;
    }
}

async function loadRevenueReport(from, to) {
    const errorBox = document.getElementById('revenueReportError');
    const result = document.getElementById('revenueReportResult');
    const billCountEl = document.getElementById('revenueReportBillCount');
    const totalEl = document.getElementById('revenueReportTotal');

    errorBox.hidden = true;
    result.hidden = true;

    if (from > to) {
        errorBox.textContent = '"From" date cannot be after "To" date.';
        errorBox.hidden = false;
        return;
    }

    try {
        const report = await ApiClient.get(`/api/reports/revenue?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`);

        billCountEl.textContent = report.totalBills;
        totalEl.textContent = report.totalRevenue;

        result.hidden = false;
    } catch (err) {
        errorBox.textContent = err.message;
        errorBox.hidden = false;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const dailyDateInput = document.getElementById('dailyReportDate');
    const revenueFromInput = document.getElementById('revenueFromDate');
    const revenueToInput = document.getElementById('revenueToDate');

    // Default both load daily report in first time open
    document.querySelectorAll('[data-section="reports-section"]').forEach((el) => {
        el.addEventListener('click', () => {
            const today = new Date().toISOString().slice(0, 10);
            if (!dailyDateInput.value) {
                dailyDateInput.value = today;
            }
            if (!revenueFromInput.value) {
                revenueFromInput.value = today;
            }
            if (!revenueToInput.value) {
                revenueToInput.value = today;
            }
            loadDailyReport(dailyDateInput.value);
        });
    });

    document.getElementById('dailyReportForm').addEventListener('submit', (e) => {
        e.preventDefault();
        loadDailyReport(dailyDateInput.value);
    });

    document.getElementById('revenueReportForm').addEventListener('submit', (e) => {
        e.preventDefault();
        loadRevenueReport(revenueFromInput.value, revenueToInput.value);
    });
});
