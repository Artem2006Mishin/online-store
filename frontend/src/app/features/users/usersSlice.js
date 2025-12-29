import { createSlice } from '@reduxjs/toolkit';
import {
  authUserThunk,
  getUserThunk,
  updateProfileThunk,
  deleteUserThunk,
  changeUserPasswordThunk,
  changeUserRoleThunk
} from './usersThunk.js';

const initialState = {
  userData: null,
  allUsersList: [],
  status: 'inactive',
  error: null,
};

const userSlice = createSlice({
  name: 'users',
  initialState,
  reducers: {
    resetCart: (state) => initialState,
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
        state.userData = action.payload.currentUser;
        state.allUsersList = action.payload.allUsers || [];
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
        if (action.payload.token) {
          localStorage.setItem('token', action.payload.token);
        }
      })
      .addCase(updateProfileThunk.rejected, (state, action) => {
        state.status = 'error';
        state.error = action.payload;
      })
      // deleteUserThunk — НОВОЕ
      .addCase(deleteUserThunk.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(deleteUserThunk.fulfilled, (state, action) => {
        state.status = 'success';
        state.allUsersList = state.allUsersList.filter(user => user.id !== action.payload);
      })
      .addCase(deleteUserThunk.rejected, (state, action) => {
        state.status = 'error';
        state.error = action.payload;
      })
      // changeUserPasswordThunk — НОВОЕ
      .addCase(changeUserPasswordThunk.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(changeUserPasswordThunk.fulfilled, (state) => {
        state.status = 'success';
        // Пароль не показываем в UI
      })
      .addCase(changeUserPasswordThunk.rejected, (state, action) => {
        state.status = 'error';
        state.error = action.payload;
      })
      .addCase(changeUserRoleThunk.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(changeUserRoleThunk.fulfilled, (state, action) => {
        state.status = 'success';
        const { userId, role } = action.payload;
        const idx = state.allUsersList.findIndex(u => u.id === userId);
        if (idx !== -1) state.allUsersList[idx].role = role;
      })
      .addCase(changeUserRoleThunk.rejected, (state, action) => {
        state.status = 'error';
        state.error = action.payload;
      });
  },
});

export default userSlice.reducer;
export const { resetCart } = userSlice.actions;
