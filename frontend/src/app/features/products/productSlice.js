import { createSlice } from '@reduxjs/toolkit';
import {getProductsThunk} from "./productsThunk.js";

const productSlice = createSlice({
	name: 'products',
	initialState: {
		productsList: [],
		status: 'inactive',
		error: null,
	},
  extraReducers: (builder) => {
    builder
      .addCase(getProductsThunk.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(getProductsThunk.fulfilled, (state, action) => {
        state.status = 'success';
        state.productsList = action.payload;
      })
      .addCase(getProductsThunk.rejected, (state, action) => {
        state.status = 'error';
        state.error = action.payload;
      });
  },
});

export default productSlice.reducer;
