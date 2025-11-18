import { combineReducers } from '@reduxjs/toolkit';
import newsReducer from './features/news/newsSlice';
import navbarReducer from './features/navbarSlice';

export const rootReducer = combineReducers({
	news: newsReducer,
	navbar: navbarReducer,
});
