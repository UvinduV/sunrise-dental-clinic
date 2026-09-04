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
    const generateError = document.getElementById('generateBillError');

    generateForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        generateError.hidden = true;

        const appointmentNo = document.getElementById('genBillApptNo').value.trim();

        try {
            const bill = await ApiClient.post('/api/bills', { appointmentNo });
            renderBill(bill);
            generateForm.reset();
        } catch (err) {
            generateError.textContent = err.message;
            generateError.hidden = false;
        }
    });

    // --- View by ID ---
    const viewForm = document.getElementById('viewBillForm');
    const viewError = document.getElementById('viewBillError');

    viewForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        viewError.hidden = true;

        const id = document.getElementById('viewBillId').value;

        try {
            const bill = await ApiClient.get(`/api/bills/${id}`);
            renderBill(bill);
        } catch (err) {
            viewError.textContent = err.message;
            viewError.hidden = false;
        }
    });
});
