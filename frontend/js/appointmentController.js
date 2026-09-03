// Register / Search / List appointments
let dentistsCache = [];
let treatmentsCache = [];

async function loadAppointmentsSection() {
    await Promise.all([loadDentistsAndTreatments(), loadAppointmentsTable()]);
}

async function loadDentistsAndTreatments() {
    const dentistSelect = document.getElementById('apDentist');
    const treatmentSelect = document.getElementById('apTreatment');

    try {
        [dentistsCache, treatmentsCache] = await Promise.all([
            ApiClient.get('/api/dentists'),
            ApiClient.get('/api/treatments')
        ]);

        dentistSelect.innerHTML = '<option value="" disabled selected>Select a dentist</option>' +
            dentistsCache.map(d => `<option value="${d.id}">${d.name} (${d.specialization})</option>`).join('');

        treatmentSelect.innerHTML = '<option value="" disabled selected>Select a treatment</option>' +
            treatmentsCache.map(t => `<option value="${t.id}">${t.name} — Rs. ${t.fee}</option>`).join('');
    } catch (err) {
        dentistSelect.innerHTML = '<option value="" disabled selected>Failed to load</option>';
        treatmentSelect.innerHTML = '<option value="" disabled selected>Failed to load</option>';
    }
}

async function loadAppointmentsTable() {
    const tbody = document.getElementById('appointmentsTableBody');
    try {
        const appointments = await ApiClient.get('/api/appointments');
        if (appointments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted">No appointments yet.</td></tr>';
            return;
        }
        tbody.innerHTML = appointments.map(a => `
            <tr>
                <td>${a.appointmentNo}</td>
                <td>${a.patientName}</td>
                <td>${a.dentistName}</td>
                <td>${a.treatmentName}</td>
                <td>${a.date}</td>
                <td>${a.time}</td>
                <td><span class="badge text-bg-secondary">${a.status}</span></td>
            </tr>
        `).join('');
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">${err.message}</td></tr>`;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // Refresh dropdowns + table every time the Appointments nav link is opened.
    document.querySelectorAll('[data-section="appointments-section"]').forEach((el) => {
        el.addEventListener('click', loadAppointmentsSection);
    });

    // --- Register ---
    const registerForm = document.getElementById('registerForm');
    const registerError = document.getElementById('registerError');
    const registerSuccess = document.getElementById('registerSuccess');

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        registerError.hidden = true;
        registerSuccess.hidden = true;

        const request = {
            patientName: document.getElementById('apPatientName').value.trim(),
            address: document.getElementById('apAddress').value.trim(),
            contactNumber: document.getElementById('apContactNumber').value.trim(),
            dentistId: Number(document.getElementById('apDentist').value),
            treatmentId: Number(document.getElementById('apTreatment').value),
            date: document.getElementById('apDate').value,
            time: document.getElementById('apTime').value + ':00'
        };

        try {
            const appointment = await ApiClient.post('/api/appointments', request);
            registerSuccess.textContent = `Appointment ${appointment.appointmentNo} registered successfully.`;
            registerSuccess.hidden = false;
            registerForm.reset();
            await loadAppointmentsTable();
        } catch (err) {
            registerError.textContent = err.message;
            registerError.hidden = false;
        }
    });

    // --- Search ---
    const searchForm = document.getElementById('searchForm');
    const searchError = document.getElementById('searchError');
    const searchResult = document.getElementById('searchResult');

    searchForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        searchError.hidden = true;
        searchResult.hidden = true;

        const apptNo = document.getElementById('searchApptNo').value.trim();

        try {
            const a = await ApiClient.get(`/api/appointments/${encodeURIComponent(apptNo)}`);
            searchResult.innerHTML = `
                <div class="app-card">
                    <strong>${a.appointmentNo}</strong> — <span class="badge text-bg-secondary">${a.status}</span>
                    <p class="mb-1 mt-2">${a.patientName} — ${a.contactNumber}</p>
                    <p class="mb-1">${a.address}</p>
                    <p class="mb-0">${a.dentistName} · ${a.treatmentName} · ${a.date} ${a.time}</p>
                </div>`;
            searchResult.hidden = false;
        } catch (err) {
            searchError.textContent = err.message;
            searchError.hidden = false;
        }
    });

    // --- Manual refresh ---
    document.getElementById('refreshAppointmentsBtn').addEventListener('click', loadAppointmentsTable);
});
