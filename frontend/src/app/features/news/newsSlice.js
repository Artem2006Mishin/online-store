import { createSlice } from '@reduxjs/toolkit';
import { getNews } from './newsThunk';

const newsSlice = createSlice({
	name: 'news',
	initialState: {
		news: [],
		status: 'idle',
		error: null,
	},
	reducers: {},
	extraReducers: (builder) => {
		builder
			.addCase(getNews.pending, (state) => {
				state.status = 'loading';
			})
			.addCase(getNews.fulfilled, (state, action) => {
				state.status = 'success';
				state.news = action.payload;
			})
			.addCase(getNews.rejected, (state, action) => {
				state.status = 'error';
				state.error = action.payload;
			});
	},
});

export default newsSlice.reducer;
