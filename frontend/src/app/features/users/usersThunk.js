import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios';
import { getElements } from '../default/defaultThunk';

export const saveUser = createAsyncThunk(
	'user/saveUser',
	async ({ url, userData }, thunkAPI) => {
		try {
			const response = await api.post(url, userData, { skipAuth: true });
			localStorage.setItem('token', response.data.token); // TODO: когда будет настоящий
			// JWT-токен, то нужно будет сериализовать его в json.
			return response.data;
		} catch (error) {
			// ответ есть
			if (error.response) {
				if (error.response.status == 401) {
					return thunkAPI.rejectWithValue({
						status: 'INVALID_PASSWORD',
						message: 'Неправильный пароль',
					});
				}

				if (error.response.status == 404) {
					return thunkAPI.rejectWithValue({
						status: 'EMAIL_NOT_FOUND',
						message: 'Пользователь с таким email не найден',
					});
				}

				if (error.response.status == 409) {
					return thunkAPI.rejectWithValue({
						status: 'EMAIL_BUSY',
						message: 'Пользователь с таким email уже существует',
					});
				}
			}

			// запрос есть
			if (error.request) {
				return thunkAPI.rejectWithValue({
					status: 'NETWORK_ERROR',
					message:
						'Сервер не доступен. Проверьте соединение или запустите backend.',
				});
			}

			// обработка стандартной ошибки
			return thunkAPI.rejectWithValue({
				status: 'UNKNOWN_ERROR',
				message: error.message,
			});
		}
	}
);

export const getUser = createAsyncThunk('user/getUser', getElements);
