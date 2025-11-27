import { createSlice } from '@reduxjs/toolkit';
import { handleGettingElements } from '../default/defaultExtraReducer';
import { saveUser } from './usersThunk';

const usersSlice = createSlice({
	name: 'users',
	initialState: {
		items: {},
		status: 'idle',
		error: null,
	},
	reducers: {},
	extraReducers: (builder) => {
		handleGettingElements(builder, saveUser);
	},
});

export default usersSlice.reducer;
