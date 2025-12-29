import { createAsyncThunk } from '@reduxjs/toolkit';
import api from '../../../api/axios';

export const authUserThunk = createAsyncThunk(
  'users/authUser',
  async ({ url, userData, isMultipart }, thunkAPI) => {
    try {
      let response;
      if (isMultipart) {
        const config = {
          skipAuth: true,
          headers: { 'Content-Type': 'multipart/form-data' }
        };
        response = await api.post(`auth/${url}`, userData, config);
      } else {
        response = await api.post(`auth/${url}`, userData, { skipAuth: true });
      }
  localStorage.setItem('token', response.data.token);
      return response.data;
    } catch (error) {
      if (error.response) {
        if (error.response.status === 401) {
          return thunkAPI.rejectWithValue({
            status: 'INVALIDPASSWORD',
            message: 'Неверный пароль'
          });
        }
        if (error.response.status === 404) {
          return thunkAPI.rejectWithValue({
            status: 'EMAILNOTFOUND',
            message: `email ${error.response.data.email}`
          });
        }
        if (error.response.status === 409) {
          return thunkAPI.rejectWithValue({
            status: 'EMAILBUSY',
            message: `email ${error.response.data.email}`
          });
        }
      }
      if (error.request) {
        return thunkAPI.rejectWithValue({
          status: 'NETWORKERROR',
          message: 'Нет связи с бэкендом.'
        });
      }
      return thunkAPI.rejectWithValue({
        status: 'UNKNOWNERROR',
        message: error.message
      });
    }
  }
);

export const getUserThunk = createAsyncThunk(
  'users/getUser',
  async (_, thunkAPI) => {
    try {
      const response = await api.get('/profile');
      return response.data;
    } catch (error) {
      if (error.isNetworkError) {
        return thunkAPI.rejectWithValue({
          status: 'NETWORKERROR',
          message: 'Нет связи с бэкендом.'
        });
      }
      if (error.response?.status === 401) {
        return thunkAPI.rejectWithValue({
          status: 'UNAUTHORIZED',
          message: 'Не авторизован'
        });
      }
      return thunkAPI.rejectWithValue({
        status: 'UNKNOWNERROR',
        message: error.message
      });
    }
  }
);

export const updateProfileThunk = createAsyncThunk(
  'users/updateProfile',
  async (userData, { isMultipart }, thunkAPI) => {
    try {
      let response;
      if (isMultipart) {
        const config = {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        };
        response = await api.put('/profile', userData, config);
      } else {
        response = await api.put('/profile', userData);
      }
  return response.data;
    } catch (error) {
      if (error.response) {
        if (error.response.status === 409) {
          return thunkAPI.rejectWithValue({
            status: 'EMAILBUSY',
            message: `email ${error.response.data.email}`
          });
        }
        if (error.response.status === 400) {
          return thunkAPI.rejectWithValue({
            status: 'BADREQUEST',
            message: error.response.data
          });
        }
      }
      if (error.request) {
        return thunkAPI.rejectWithValue({
          status: 'NETWORKERROR',
          message: 'Нет связи с бэкендом.'
        });
      }
      return thunkAPI.rejectWithValue({
        status: 'UNKNOWNERROR',
        message: error.message
      });
    }
  }
);

// Добавить в конец usersThunk.js
export const deleteUserThunk = createAsyncThunk(
  'users/deleteUser',
  async (userId, thunkAPI) => {
    try {
      await api.delete(`/profile/users/${userId}`);
      return userId;
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response?.data || error.message);
    }
  }
);

export const changeUserPasswordThunk = createAsyncThunk(
  'users/changeUserPassword',
  async ({ userId, password }, thunkAPI) => {
    try {
      await api.put(`/profile/users/${userId}/password`, { password });
      return { userId, password };
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response?.data || error.message);
    }
  }
);

export const changeUserRoleThunk = createAsyncThunk(
  'users/changeUserRole',
  async ({ userId, role }, thunkAPI) => {
    try {
      await api.put(`/profile/users/${userId}/role`, { role });
      return { userId, role };
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response?.data || error.message);
    }
  }
);

