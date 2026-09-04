// Patient create , Update , delete

async function loadPatientsSection() {
    const tbody = document.getElementById('patientsTableBody');
    try {
        const patients = await ApiClient.get('/api/patients');
        if (patients.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No patients yet.</td></tr>';
            return;
        }
        tbody.innerHTML = patients.map(p => `
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>
                <td>${p.address}</td>
                <td>${p.contactNumber}</td>
                <td class="text-end">
                    <button type="button" class="btn btn-sm btn-outline-primary patient-edit-btn"
                            data-id="${p.id}" data-name="${p.name}" data-address="${p.address}" data-contact="${p.contactNumber}">
                        Edit
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-danger patient-delete-btn" data-id="${p.id}">
                        Delete
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">${err.message}</td></tr>`;
    }
}

function enterEditMode(id, name, address, contact) {
    document.getElementById('patientEditingId').value = id;
    document.getElementById('patientName').value = name;
    document.getElementById('patientAddress').value = address;
    document.getElementById('patientContactNumber').value = contact;
    document.getElementById('patientFormTitle').textContent = `Edit Patient #${id}`;
    document.getElementById('patientSubmitBtn').textContent = 'Update Patient';
    document.getElementById('patientCancelEdit').hidden = false;
}

function exitEditMode() {
    document.getElementById('patientForm').reset();
    document.getElementById('patientEditingId').value = '';
    document.getElementById('patientFormTitle').textContent = 'Add Patient';
    document.getElementById('patientSubmitBtn').textContent = 'Add Patient';
    document.getElementById('patientCancelEdit').hidden = true;
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-section="patients-section"]').forEach((el) => {
        el.addEventListener('click', loadPatientsSection);
    });
    document.getElementById('refreshPatientsBtn').addEventListener('click', loadPatientsSection);

    document.getElementById('patientCancelEdit').addEventListener('click', (e) => {
        e.preventDefault();
        exitEditMode();
    });

    // Edit / Delete buttons
    const deleteModal = new bootstrap.Modal(document.getElementById('patientDeleteConfirmModal'));
    const deleteConfirmText = document.getElementById('patientDeleteConfirmText');
    let patientIdPendingDelete = null;

    document.getElementById('patientsTableBody').addEventListener('click', async (e) => {
        const editBtn = e.target.closest('.patient-edit-btn');
        if (editBtn) {
            enterEditMode(editBtn.dataset.id, editBtn.dataset.name, editBtn.dataset.address, editBtn.dataset.contact);
            return;
        }

        const deleteBtn = e.target.closest('.patient-delete-btn');
        if (deleteBtn) {
            const id = deleteBtn.dataset.id;
            const name = deleteBtn.dataset.name;

            try {
                const hasAppointments = await ApiClient.get(`/api/patients/${id}/has-appointments`);
                if (hasAppointments) {
                    showToast(`${name} has appointment(s) and cannot be deleted.`, 'error');
                    return;
                }
            } catch (err) {
                showToast(err.message, 'error');
                return;
            }

            patientIdPendingDelete = id;
            deleteConfirmText.textContent = `Delete ${name}? This cannot be undone.`;
            deleteModal.show();
        }
    });

    document.getElementById('confirmPatientDeleteBtn').addEventListener('click', async () => {
        const id = patientIdPendingDelete;
        deleteModal.hide();

        try {
            await ApiClient.del(`/api/patients/${id}`);
            showToast(`Patient #${id} deleted.`, 'success');
            await loadPatientsSection();
        } catch (err) {
            showToast(err.message, 'error');
        }
    });

    // Create / Update
    const patientForm = document.getElementById('patientForm');

    patientForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const request = {
            name: document.getElementById('patientName').value.trim(),
            address: document.getElementById('patientAddress').value.trim(),
            contactNumber: document.getElementById('patientContactNumber').value.trim()
        };

        if (!/^0[0-9]{9}$/.test(request.contactNumber)) {
            showToast('Contact number must be 10 digits (eg: 0771234567)', 'error');
            return;
        }

        const editingId = document.getElementById('patientEditingId').value;

        try {
            if (editingId) {
                await ApiClient.put(`/api/patients/${editingId}`, request);
                showToast('Patient updated successfully.', 'success');
            } else {
                await ApiClient.post('/api/patients', request);
                showToast('Patient added successfully.', 'success');
            }
            exitEditMode();
            await loadPatientsSection();
        } catch (err) {
            showToast(err.message, 'error');
        }
    });
});
