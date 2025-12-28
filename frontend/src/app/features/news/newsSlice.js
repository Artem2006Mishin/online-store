import { createSlice } from '@reduxjs/toolkit';
import {
	getNewsThunk,
	createNewsThunk,
	deleteNewsThunk,
	updateNewsThunk,
} from './newsThunk.js';

const newsSlice = createSlice({
	name: 'news',
	initialState: {
		newsList: [],
		status: 'inactive',
		error: null,
	},
	extraReducers: (builder) => {
		builder
			.addCase(getNewsThunk.pending, (state) => {
				state.status = 'loading';
			})
			.addCase(getNewsThunk.fulfilled, (state, action) => {
				state.status = 'success';
				state.newsList = action.payload;
			})
			.addCase(getNewsThunk.rejected, (state, action) => {
				state.status = 'error';
				state.error = action.payload;
			})
			.addCase(createNewsThunk.pending, (state) => {
				// Можно добавить статус создания, но для простоты оставим
			})
			.addCase(createNewsThunk.fulfilled, (state, action) => {
				// Добавляем новую новость в список
				state.newsList.push(action.payload);
			})
			.addCase(createNewsThunk.rejected, (state, action) => {
				// Можно обработать ошибку создания
			})
			.addCase(deleteNewsThunk.fulfilled, (state, action) => {
				// Удаляем новость из списка
				state.newsList = state.newsList.filter(
					(news) => news.id !== action.payload
				);
			})
			.addCase(deleteNewsThunk.rejected, (state, action) => {
				// Можно обработать ошибку удаления
			})
			.addCase(updateNewsThunk.fulfilled, (state, action) => {
				// Обновляем новость в списке
				const index = state.newsList.findIndex(
					(news) => news.id === action.payload.id
				);
				if (index !== -1) {
					state.newsList[index] = action.payload;
				}
			})
			.addCase(updateNewsThunk.rejected, (state, action) => {
				// Можно обработать ошибку обновления
			});
	},
});

export default newsSlice.reducer;
