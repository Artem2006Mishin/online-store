import {createAsyncThunk} from "@reduxjs/toolkit";
import api from "../../../api/axios.js";

export const getTimeThunk = createAsyncThunk(
  'time/getTime',
  async (payload, thunkAPI) => {
    try {
      const response = await api.get(`/api/time`);
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