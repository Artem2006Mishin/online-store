import { createSlice } from '@reduxjs/toolkit';
import { getCategories } from './categoriesThunk';
import { handleGettingElements } from '../default/defaultExtraReducer';

const categoriesSlice = createSlice({
	name: 'categories',
	initialState: {
		items: [],
		status: 'idle',
		error: null,
	},
	reducers: {},
	extraReducers: (builder) => {
		handleGettingElements(builder, getCategories);
	},
});

export default categoriesSlice.reducer;
