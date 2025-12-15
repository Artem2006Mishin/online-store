import { createSlice } from '@reduxjs/toolkit';
import {authUserThunk} from "./usersThunk.js";

const userSlice = createSlice({
	name: 'users',
	initialState: {
		userData: {},
		status: 'inactive',
		error: null,
	},
  extraReducers: (builder) => {
    builder
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
      });
  },
});

export default userSlice.reducer;
