import { createSlice } from '@reduxjs/toolkit';
import { getNewsThunk, createNewsThunk } from './newsThunk.js';

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
			});
	},
});

export default newsSlice.reducer;
