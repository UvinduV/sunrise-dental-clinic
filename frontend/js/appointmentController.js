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

//appointment has can status change in after past due date and time
function isPastDue(appointment) {
    return new Date(`${appointment.date}T${appointment.time}`) < new Date();
}

function statusCell(a) {
    if (a.status !== 'SCHEDULED' || !isPastDue(a)) {
        return `<span class="badge ${statusBadgeClass(a.status)}">${a.status}</span>`;
    }
    return `
        <select class="form-select form-select-sm status-select" style="width: auto;"
                data-appointment-no="${a.appointmentNo}">
            <option value="SCHEDULED" selected>Scheduled</option>
            <option value="COMPLETED">Completed</option>
            <option value="CANCELLED">Cancelled</option>
        </select>`;
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
                <td class="text-end">${statusCell(a)}</td>
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
    const newAppointmentModal = new bootstrap.Modal(document.getElementById('newAppointmentModal'));

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

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
            showToast(`Appointment ${appointment.appointmentNo} registered successfully.`, 'success');
            registerForm.reset();
            newAppointmentModal.hide();
            await loadAppointmentsTable();
        } catch (err) {
            showToast(err.message, 'error');
        }
    });

    // --- Search ---
    const searchForm = document.getElementById('searchForm');
    const searchResult = document.getElementById('searchResult');

    searchForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        searchResult.hidden = true;

        const apptNo = document.getElementById('searchApptNo').value.trim();

        try {
            const a = await ApiClient.get(`/api/appointments/${encodeURIComponent(apptNo)}`);
            searchResult.innerHTML = `
                <div class="app-card">
                    <strong>${a.appointmentNo}</strong> — <span class="badge ${statusBadgeClass(a.status)}">${a.status}</span>
                    <p class="mb-1 mt-2">${a.patientName} — ${a.contactNumber}</p>
                    <p class="mb-1">${a.address}</p>
                    <p class="mb-0">${a.dentistName} · ${a.treatmentName} · ${a.date} ${a.time}</p>
                </div>`;
            searchResult.hidden = false;
        } catch (err) {
            showToast(err.message, 'error');
        }
    });

    // --- Manual refresh ---
    document.getElementById('refreshAppointmentsBtn').addEventListener('click', loadAppointmentsTable);

    // --- Status change (past-due scheduled appointments only) ---
    document.getElementById('appointmentsTableBody').addEventListener('change', async (e) => {
        const select = e.target.closest('.status-select');
        if (!select) return;

        const appointmentNo = select.dataset.appointmentNo;
        const status = select.value;

        try {
            await ApiClient.put(`/api/appointments/${appointmentNo}/status`, { status });
            showToast(`${appointmentNo} marked ${status.toLowerCase()}.`, 'success');
            await loadAppointmentsTable();
        } catch (err) {
            showToast(err.message, 'error');
        }
    });
});
