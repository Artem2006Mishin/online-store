import {createSlice} from "@reduxjs/toolkit";
import {getOrdersThunk, placeOrderThunk} from "./orderThunk.js";

const orderSlice = createSlice({
  name: 'orders',
  initialState: {
    ordersList: [],
    currentOrderStatus: 'inactive',
    currentOrderError: null,
    getOrdersStatus: 'inactive',
    getOrdersError: null,
  },
  extraReducers: (builder) => {
    builder
      // placeOrderThunk
      .addCase(placeOrderThunk.pending, (state) => {
        state.currentOrderStatus = 'loading';
        state.currentOrderError = null;
      })
      .addCase(placeOrderThunk.fulfilled, (state, action) => {
        state.currentOrderStatus = 'succeeded';
        state.ordersList.push(action.payload);
      })
      .addCase(placeOrderThunk.rejected, (state, action) => {
        state.currentOrderStatus = 'failed';
        state.currentOrderError = action.payload;
      })

      // getOrdersThunk
      .addCase(getOrdersThunk.pending, (state) => {
        state.getOrdersStatus = 'loading';
        state.getOrdersError = null;
      })
      .addCase(getOrdersThunk.fulfilled, (state, action) => {
        state.getOrdersStatus = 'succeeded';
        state.ordersList = action.payload;
      })
      .addCase(getOrdersThunk.rejected, (state, action) => {
        state.getOrdersStatus = 'failed';
        state.getOrdersError = action.payload;
      });
  }
});

export default orderSlice.reducer;