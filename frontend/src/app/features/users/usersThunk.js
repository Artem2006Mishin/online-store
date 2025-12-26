import {createAsyncThunk} from '@reduxjs/toolkit';
import api from '../../../api/axios';

export const authUserThunk = createAsyncThunk(
  'user/authUser',
  async ({url, userData}, thunkAPI) => {
    try {
      const response = await api.post(`auth/${url}`, userData, {skipAuth: true});
      localStorage.setItem('token', response.data.token) // todo: когда будет настоящий JWT-токен, то нужно будет сериализовать его в json.

      console.log(response.data);

      return response.data;
    } catch (error) {
      if (error.response) {
        if (error.response.status === 401) {
          return thunkAPI.rejectWithValue({
            status: 'INVALID_PASSWORD',
            message: 'Неправильный пароль',
          });
        }

        if (error.response.status === 404) {
          return thunkAPI.rejectWithValue({
            status: 'EMAIL_NOT_FOUND',
            message: 'Пользователь с таким email не найден',
          });
        }

        if (error.response.status === 409) {
          return thunkAPI.rejectWithValue({
            status: 'EMAIL_BUSY',
            message: 'Пользователь с таким email уже существует',
          });
        }
      }

      if (error.request) {
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

export const getUserThunk = createAsyncThunk(
  'user/authUser',
  async (payload, thunkAPI) => {
  try {
    const response = await api.get(`auth/getData`);
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
