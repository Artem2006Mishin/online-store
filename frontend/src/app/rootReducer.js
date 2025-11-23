import { combineReducers } from '@reduxjs/toolkit';
import newsReducer from './features/news/newsSlice';
import categoriesReducer from './features/categories/categoriesSlice';

export const rootReducer = combineReducers({
	news: newsReducer,
	categories: categoriesReducer,
});
