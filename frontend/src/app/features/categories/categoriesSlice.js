import { createSlice } from '@reduxjs/toolkit';
import {getCategoriesThunk} from './categoriesThunk';

const categoriesSlice = createSlice({
	name: 'categories',
	initialState: {
    categoriesList: [],
		status: 'inactive',
		error: null,
	},
  extraReducers: (builder) => {
    builder
      .addCase(getCategoriesThunk.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(getCategoriesThunk.fulfilled, (state, action) => {
        state.status = 'success';
        state.categoriesList = action.payload;
      })
      .addCase(getCategoriesThunk.rejected, (state, action) => {
        state.status = 'error';
        state.error = action.payload;
      });
  },
});

export default categoriesSlice.reducer;
