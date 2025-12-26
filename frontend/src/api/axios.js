import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

// Добавляем токен во все запросы, кроме тех где skipAuth: true
api.interceptors.request.use(
  (config) => {
    if (config.skipAuth) {
      delete config.skipAuth;
      return config;
    }

    const token = localStorage.getItem("token");
    if (token) {
      config.headers = config.headers || {};
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// Глобальная обработка ошибок
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // network error (нет ответа от сервера)
    if (error.request && !error.response) error.isNetworkError = true;

    // если токен невалидный/просрочен — разлогиниваем
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      // опционально: редирект на страницу входа
      // window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

export default api;
