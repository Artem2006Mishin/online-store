import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios';

export const authUserThunk = createAsyncThunk(
	'user/authUser',
	async ({ url, userData, isMultipart }, thunkAPI) => {
		try {
			let response;

			if (isMultipart) {
				// КОСТЫЛИ
				const config = {
					skipAuth: true,
					headers: {},
				};

				if (isMultipart) {
					config.headers['Content-Type'] = 'multipart/form-data';
				}

				response = await api.post(`auth/${url}`, userData, config);
			} else {
				response = await api.post(`auth/${url}`, userData, { skipAuth: true });
			}

			localStorage.setItem('token', response.data.token);
			return response.data;
		} catch (error) {
			if (error.response) {
				if (error.response.status === 401) {
					return thunkAPI.rejectWithValue({
						status: 'INVALID_PASSWORD',
						message: 'Неправильный пароль',
					});
				}

				if (error.response.status === 404) {
					return thunkAPI.rejectWithValue({
						status: 'EMAIL_NOT_FOUND',
						message: 'Пользователь с таким email не найден',
					});
				}

				if (error.response.status === 409) {
					return thunkAPI.rejectWithValue({
						status: 'EMAIL_BUSY',
						message: 'Пользователь с таким email уже существует',
					});
				}
			}

			if (error.request) {
				return thunkAPI.rejectWithValue({
					status: 'NETWORK_ERROR',
					message:
						'Сервер не доступен. Проверьте соединение или запустите backend.',
				});
			}

			return thunkAPI.rejectWithValue({
				status: 'UNKNOWN_ERROR',
				message: error.message,
			});
		}
	}
);

export const getUserThunk = createAsyncThunk(
	'user/getUser',
	async (payload, thunkAPI) => {
		try {
			const response = await api.get(`/profile`);
			return response.data;
		} catch (error) {
			if (error.isNetworkError) {
				return thunkAPI.rejectWithValue({
					status: 'NETWORK_ERROR',
					message:
						'Сервер не доступен. Проверьте соединение или запустите backend.',
				});
			}

			if (error.response?.status === 401) {
				return thunkAPI.rejectWithValue({
					status: 'UNAUTHORIZED',
					message: 'Вы не зарегистрированы.',
				});
			}

			return thunkAPI.rejectWithValue({
				status: 'UNKNOWN_ERROR',
				message: error.message,
			});
		}
	}
);
