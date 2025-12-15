import { createSlice } from '@reduxjs/toolkit';
import {getNewsThunk} from "./newsThunk.js";

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
      });
	},
});

export default newsSlice.reducer;
