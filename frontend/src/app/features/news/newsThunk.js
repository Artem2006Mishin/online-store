import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios';

export const getNews = createAsyncThunk('news/get', async (_, thunkAPI) => {
	try {
		const response = await api.get('/');
		return response.data;
	} catch (error) {
		// запрос сделан, но ответа нет
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
});

// TODO: узнать какая именно ошибка возвращается в thunkAPI.rejectWithValue с backend
