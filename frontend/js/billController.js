// Generate / view bills

function renderBill(bill) {
    const receipt = document.getElementById('billReceipt');
    receipt.innerHTML = `
        <div class="app-card" style="max-width: 480px; margin: 0 auto;">
            <div class="text-center mb-3">
                <h4 class="mb-0">Sunrise Dental Clinic</h4>
                <small class="text-muted">Receipt — Bill #${bill.id}</small>
            </div>
            <div class="receipt-line"><span>Appointment No.</span><span>${bill.appointmentNo}</span></div>
            <div class="receipt-line"><span>Patient</span><span>${bill.patientName}</span></div>
            <div class="receipt-line"><span>Treatment</span><span>${bill.treatmentName}</span></div>
            <div class="receipt-line"><span>Issued Date</span><span>${bill.issuedDate}</span></div>
            <div class="receipt-line"><span>Treatment Fee</span><span>Rs. ${bill.treatmentFee}</span></div>
            <div class="receipt-line"><span>Consultation Fee</span><span>Rs. ${bill.consultationFee}</span></div>
            <div class="receipt-line total"><span>Total</span><span>Rs. ${bill.totalAmount}</span></div>
            <button type="button" class="btn btn-outline-primary w-100 mt-3 no-print" onclick="window.print()">
                Print Receipt
            </button>
        </div>`;
    receipt.hidden = false;
}

document.addEventListener('DOMContentLoaded', () => {
    // --- Generate ---
    const generateForm = document.getElementById('generateBillForm');

    generateForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const appointmentNo = document.getElementById('genBillApptNo').value.trim();

        try {
            const bill = await ApiClient.post('/api/bills', { appointmentNo });
            renderBill(bill);
            showToast(`Bill generated for ${appointmentNo}.`, 'success');
            generateForm.reset();
        } catch (err) {
            showToast(err.message, 'error');
        }
    });

    // --- View by ID ---
    const viewForm = document.getElementById('viewBillForm');

    viewForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = document.getElementById('viewBillId').value;

        try {
            const bill = await ApiClient.get(`/api/bills/${id}`);
            renderBill(bill);
        } catch (err) {
            showToast(err.message, 'error');
        }
    });
});
