import {createAsyncThunk} from "@reduxjs/toolkit";
import api from "../../../api/axios.js";

export const getTimeThunk = createAsyncThunk(
  'time/getTime',
  async (payload, thunkAPI) => {
    try {
  // request that should not include Authorization header for anonymous users
  const response = await api.get(`/api/time`, { skipAuth: true });
      return response.data;
    } catch (error) {
      if (error.isNetworkError) {
        return thunkAPI.rejectWithValue({
          status: 'NETWORK_ERROR',
          message:
            'Сервер не доступен. Проверьте соединение или запустите backend.',
        });
      }

      return thunkAPI.rejectWithValue({
        status: 'UNKNOWN_ERROR',
        message: error.message,
      });
    }
  });