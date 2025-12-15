import { createAsyncThunk } from '@reduxjs/toolkit';
import api from "../../../api/axios.js";

export const getCategoriesThunk = createAsyncThunk(
  'categories/getCategories',
  async (_, thunkAPI) => {
    try {
      const response = await api.get('categories/getCategories');
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
