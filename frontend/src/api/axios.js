import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

api.interceptors.request.use(config => {
  if (config.skipAuth) {
    delete config.skipAuth;
    return config;
  }

  const token = localStorage.getItem('token');
  config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default api;

api.interceptors.response.use(
  response => response,
  error => {
    if (error.request && !error.response) error.isNetworkError = true;
    return Promise.reject(error);
  }
);

