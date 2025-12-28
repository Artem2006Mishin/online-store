import {createSlice} from '@reduxjs/toolkit';
import {authUserThunk, getUserThunk, updateProfileThunk} from "./usersThunk.js";

const initialState = {
  userData: {},
  status: 'inactive',
  error: null,
}

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
        state.error = null;
      })
      .addCase(getUserThunk.rejected, (state, action) => {
        // Если ошибка 401 (неавторизован), сбрасываем статус в inactive
        if (action.payload?.status === 'UNAUTHORIZED') {
          state.status = 'inactive';
          state.userData = {};
        } else {
          state.status = 'error';
        }
        state.error = action.payload;
      })

      // updateProfileThunk
      .addCase(updateProfileThunk.pending, (state) => {
        state.status = 'loading';
        state.error = null;
      })
      .addCase(updateProfileThunk.fulfilled, (state, action) => {
        state.status = 'success';
        state.userData = action.payload;
        state.error = null;
      })
      .addCase(updateProfileThunk.rejected, (state, action) => {
        // При ошибке UNAUTHORIZED сбрасываем статус (пользователь разлогинен)
        if (action.payload?.status === 'UNAUTHORIZED') {
          state.status = 'inactive';
          state.userData = {};
        } else {
          // При других ошибках (например, EMAIL_BUSY) оставляем статус 'success',
          // чтобы пользователь оставался на странице и мог увидеть ошибку
          state.status = 'success';
        }
        state.error = action.payload;
      });
  },
});

export default userSlice.reducer;
export const {resetCart} = userSlice.actions;
