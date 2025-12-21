import {createSlice} from "@reduxjs/toolkit";

const cartSlice = createSlice({
  name: 'cart',
  initialState: {
    total: 0,
    items: []
  },
  reducers: {
    addToCart(state, action) {
      const {id, name, price} = action.payload;

      const exists = state.items.some(
        item => item.productId === id
      );
      if (exists) return;

      state.total += price;
      state.items.push({
        productId: id,
        productName: name,
        productPrice: price,
      });
    },

    removeFromCart(state, action) {
      const item = state.items.find(
        item => item.productId === action.payload
      );

      if (!item) return;

      state.total -= item.productPrice;
      state.items = state.items.filter(
        item => item.productId !== action.payload
      );
    },

    clearCart(state) {
      state.items = [];
      state.total = 0;
    }
  }
});

export const {
  addToCart,
  removeFromCart,
  clearCart
} = cartSlice.actions

export default cartSlice.reducer;