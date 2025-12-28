import { combineReducers } from '@reduxjs/toolkit';
import newsReducer from './features/news/newsSlice';
import categoriesReducer from './features/categories/categoriesSlice';
import productsReducer from './features/products/productSlice';
import usersReducer from './features/users/usersSlice';
import cartReducer from './features/cart/cartSlice';
import timeReducer from './features/time/timeSlice';
import orderReducer from './features/order/orderSlice';

export const rootReducer = combineReducers({
	news: newsReducer,
	categories: categoriesReducer,
	products: productsReducer,
	users: usersReducer,
	cart: cartReducer,
	time: timeReducer,
	orders: orderReducer,
});
