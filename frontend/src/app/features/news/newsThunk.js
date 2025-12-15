import {createAsyncThunk} from '@reduxjs/toolkit';
import api from "../../../api/axios.js";

export const getNewsThunk = createAsyncThunk(
  'news/getNews',
  async (_, thunkAPI) => {
    try {
      const response = await api.get('news/getNews');
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
  }
);

// todo: можно ли обработать общие ошибки в одном месте?
