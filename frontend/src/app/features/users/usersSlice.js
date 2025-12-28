import { createSlice } from '@reduxjs/toolkit';
import {
	authUserThunk,
	getUserThunk,
	updateProfileThunk,
} from './usersThunk.js';

const initialState = {
	userData: {},
	status: 'inactive',
	error: null,
};

const userSlice = createSlice({
	name: 'users',
	initialState,
	reducers: {
		resetCart: () => initialState,
	},
	extraReducers: (builder) => {
		builder
			// authUserThunk
			.addCase(authUserThunk.pending, (state) => {
				state.status = 'loading';
			})
			.addCase(authUserThunk.fulfilled, (state, action) => {
				state.status = 'success';
				state.userData = action.payload;
			})
			.addCase(authUserThunk.rejected, (state, action) => {
				state.status = 'error';
				state.error = action.payload;
			})

			// getUserThunk
			.addCase(getUserThunk.pending, (state) => {
				state.status = 'loading';
			})
			.addCase(getUserThunk.fulfilled, (state, action) => {
				state.status = 'success';
				state.userData = action.payload;
			})
			.addCase(getUserThunk.rejected, (state, action) => {
				state.status = 'error';
				state.error = action.payload;
			})

			// updateProfileThunk
			.addCase(updateProfileThunk.pending, (state) => {
				state.status = 'loading';
			})
			.addCase(updateProfileThunk.fulfilled, (state, action) => {
				state.status = 'success';
				state.userData = action.payload;
			})
			.addCase(updateProfileThunk.rejected, (state, action) => {
				state.status = 'error';
				state.error = action.payload;
			});
	},
});

export default userSlice.reducer;
export const { resetCart } = userSlice.actions;
