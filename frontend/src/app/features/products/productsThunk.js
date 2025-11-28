import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios';

export const getProducts = createAsyncThunk(
	'/products/get',
	async (payload, thunkAPI) => {
		try {
			const response = await api.get(`/catalog/${payload}`);
			return response.data;
		} catch (error) {
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
