import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios.js';

export const placeOrderThunk = createAsyncThunk(
	'orders/placeOrder',
	async (orderData, thunkAPI) => {
		try {
			const response = await api.post('/orders', orderData);
			console.log(response.data);
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
					message: 'Вы не авторизованы.',
				});
			}

			return thunkAPI.rejectWithValue({
				status: 'UNKNOWN_ERROR',
				message: error.message,
			});
		}
	}
);

export const getOrdersThunk = createAsyncThunk(
	'orders/getOrders',
	async (_, thunkAPI) => {
		try {
			const response = await api.get('/orders');
			console.log(response.data);
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
					message: 'Вы не авторизованы.',
				});
			}

			return thunkAPI.rejectWithValue({
				status: 'UNKNOWN_ERROR',
				message: error.message,
			});
		}
	}
);
