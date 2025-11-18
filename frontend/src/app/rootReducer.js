import { combineReducers } from '@reduxjs/toolkit';
import newsReducer from './features/news/newsSlice';

export const rootReducer = combineReducers({
	news: newsReducer,
});
