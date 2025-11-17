import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios';

export const getNews = createAsyncThunk('news/get', async (_, thunkAPI) => {
	try {
		const response = await api.get('/');
		return response.data;
	} catch (error) {
		return thunkAPI.rejectWithValue(error.response.data.message);
	}
});

// TODO: узнать какая именно ошибка возвращается в thunkAPI.rejectWithValue с backend
