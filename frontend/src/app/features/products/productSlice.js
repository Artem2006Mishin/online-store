import { createSlice } from '@reduxjs/toolkit';
import { getProducts } from './productsThunk';
import { handleGettingElements } from '../default/defaultExtraReducer';

const productSlice = createSlice({
	name: 'products',
	initialState: {
		items: [],
		status: 'idle',
		error: null,
	},
	reducers: {},
	extraReducers: (builder) => {
		handleGettingElements(builder, getProducts);
	},
});

export default productSlice.reducer;
