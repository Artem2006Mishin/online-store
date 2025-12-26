import {getTimeThunk} from "./timeThunk.js";
import {createSlice} from "@reduxjs/toolkit";

const timeSlice = createSlice({
  name: 'time',
  initialState: {
    serverTimeStatus: 'inactive',
    serverTime: null,
    serverTimeError: null,
  },
  reducers: {
    tick(state) {
      if (!state.serverTime) return;
      const current = new Date(state.serverTime);

      if (isNaN(current.getTime())) return;
      state.serverTime = new Date(
        current.getTime() + 1000
      ).toISOString();
    }
  },
  extraReducers: builder => {
    builder
      .addCase(getTimeThunk.pending, (state) => {
        state.status = 'loading';
      })
      .addCase(getTimeThunk.fulfilled, (state, action) => {
        state.status = 'success';
        state.serverTime = action.payload.serverTime;
      })
      .addCase(getTimeThunk.rejected, (state, action) => {
        state.status = 'error';
        state.error = action.payload;
      });
  },
});

export default timeSlice.reducer;
export const { tick } = timeSlice.actions;
