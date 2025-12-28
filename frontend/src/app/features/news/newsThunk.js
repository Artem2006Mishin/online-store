import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios.js';

export const getNewsThunk = createAsyncThunk(
	'news/getNews',
	async (_, thunkAPI) => {
		try {
			const response = await api.get('news/getNews');
			return response.data;
		} catch (error) {
			if (error.isNetworkError) {
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

export const createNewsThunk = createAsyncThunk(
	'news/createNews',
	async (newsData, thunkAPI) => {
		try {
			const formData = new FormData();

			Object.entries(newsData).forEach(([key, value]) => {
				if (key === 'image' && value && value.length > 0) {
					formData.append('image', value[0]);
				} else if (key !== 'image') {
					formData.append(key, value);
				}
			});

			const config = {
				headers: {
					'Content-Type': 'multipart/form-data',
				},
			};

			const response = await api.post('news/create', formData, config);
			return response.data;
		} catch (error) {
			if (error.isNetworkError) {
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

// todo: можно ли обработать общие ошибки в одном месте?
