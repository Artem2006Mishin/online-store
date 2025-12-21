import {createAsyncThunk} from "@reduxjs/toolkit";
import api from "../../../api/axios.js";

export const placeOrderThunk = createAsyncThunk(
  'orders/placeOrder',
  async (orderData, thunkAPI) => {
    try {
      console.log(orderData);

      const response = await api.post('orders/placeOrder', orderData);
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

export const getOrdersThunk = createAsyncThunk(
  'orders/getOrders',
  async (orderData, thunkAPI) => {
    try {
      const response = await api.get('orders/getOrders');
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