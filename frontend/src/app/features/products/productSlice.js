import { createSlice } from '@reduxjs/toolkit';
import {
  getProductsThunk,
  createProductThunk,
  updateProductThunk,
  deleteProductThunk,
  getAllProductsThunk
} from "./productsThunk.js";

const productSlice = createSlice({
  name: 'products',
  initialState: {
    productsList: [],
    allProductsList: [],  // ← Все товары для админа
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
      })
      // Админ CRUD
      .addCase(getAllProductsThunk.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(getAllProductsThunk.fulfilled, (state, action) => {
        state.status = 'success';
        state.allProductsList = action.payload;
      })
      .addCase(createProductThunk.fulfilled, (state, action) => {
        state.allProductsList.push(action.payload);
        state.status = 'success';
      })
      .addCase(updateProductThunk.fulfilled, (state, action) => {
        const index = state.allProductsList.findIndex(p => p.id === action.payload.id);
        if (index !== -1) {
          state.allProductsList[index] = action.payload;
        }
        state.status = 'success';
      })
      .addCase(deleteProductThunk.fulfilled, (state, action) => {
        state.allProductsList = state.allProductsList.filter(p => p.id !== action.payload);
        state.status = 'success';
      });
  },
});

export default productSlice.reducer;
