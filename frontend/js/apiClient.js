// Shared API client: every controller (auth, appointment, bill, patient, report, help)

const API_BASE_URL = 'http://localhost:8081';
const SESSION_KEY = 'sunriseDentalCredentials';

function setCredentials(username, password) {
    sessionStorage.setItem(SESSION_KEY, JSON.stringify({ username, password }));
}

function getCredentials() {
    const raw = sessionStorage.getItem(SESSION_KEY);
    return raw ? JSON.parse(raw) : null;
}

function clearCredentials() {
    sessionStorage.removeItem(SESSION_KEY);
}

function isLoggedIn() {
    return getCredentials() !== null;
}

async function apiRequest(method, path, body) {
    const headers = { 'Content-Type': 'application/json' };
    const creds = getCredentials();
    if (creds) {
        headers['Authorization'] = 'Basic ' + btoa(creds.username + ':' + creds.password);
    }

    const response = await fetch(API_BASE_URL + path, {
        method,
        headers,
        body: body !== undefined ? JSON.stringify(body) : undefined
    });

    const text = await response.text();
    let data = null;
    if (text) {
        try {
            data = JSON.parse(text);
        } catch (e) {
            data = text;
        }
    }

    if (!response.ok) {
        const message = (data && data.message) ? data.message
            : (typeof data === 'string' ? data : `Request failed (${response.status})`);
        console.error(`[API] ${method} ${path} -> ${response.status}:`, message);
        throw new Error(message);
    }

    console.log(`[API] ${method} ${path} -> ${response.status}`, data);
    return data;
}

const ApiClient = {
    get: (path) => apiRequest('GET', path),
    post: (path, body) => apiRequest('POST', path, body),
    put: (path, body) => apiRequest('PUT', path, body),
    del: (path) => apiRequest('DELETE', path),
    setCredentials,
    getCredentials,
    clearCredentials,
    isLoggedIn
};
