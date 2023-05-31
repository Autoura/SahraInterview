// Functions and Mixins that assist with accessing APIs (either our own or 3rd parties)

import axios from 'axios';

function APIrootPublicAPI () {
    return 'https://api.autoura.com/api/';
}

export const HttpPublic = axios.create({
    baseURL: APIrootPublicAPI(),
    timeout: 25000
});

HttpPublic.interceptors.request.use(function (config) {
    config.headers.Authorization = 'Bearer 4lnzPPVGMCrQiMFjSgAlguXxL4k';
    return config;
}, function (err) {
    return Promise.reject(err);
});


// ------------------------------ AUTOURA PUBLIC & DEVELOPMENT API ----------------------------------------------------
// Uses Dev in development, Public in public

function APIrootPublicDevAPI () {
    if (window.location.hostname === 'localhost') {
        // Development
        return window.location.protocol + '//' + window.location.hostname + '/Autoura/AutouraCore/api/';
    } else {
        // Production
        return 'https://api.autoura.com/api/';
    }
}

export const HttpPublicDev = axios.create({
    baseURL: APIrootPublicDevAPI(),
    timeout: 25000
});

HttpPublicDev.interceptors.request.use(function (config) {
    config.headers.Authorization = 'Bearer 4lnzPPVGMCrQiMFjSgAlguXxL4k';
    return config;
}, function (err) {
    return Promise.reject(err);
});