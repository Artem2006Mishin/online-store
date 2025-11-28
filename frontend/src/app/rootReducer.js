import { combineReducers } from '@reduxjs/toolkit';
import newsReducer from './features/news/newsSlice';
import categoriesReducer from './features/categories/categoriesSlice';
import usersReducer from './features/users/usersSlice';
import productsReducer from './features/products/productSlice';

export const rootReducer = combineReducers({
	news: newsReducer,
	categories: categoriesReducer,
	products: productsReducer,
	users: usersReducer,
});
