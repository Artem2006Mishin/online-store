import { createAsyncThunk } from "@reduxjs/toolkit";
import api from "../../../api/axios";

export const getProductsThunk = createAsyncThunk(
  "products/getProducts",
  async (payload, thunkAPI) => {
    try {
      const response = await api.get(`/products/category/${payload}`);
      return response.data;
    } catch (error) {
      if (error.isNetworkError) {
        return thunkAPI.rejectWithValue({
          status: "NETWORK_ERROR",
          message: "Сервер не доступен. Проверьте соединение или запустите backend.",
        });
      }
      return thunkAPI.rejectWithValue({
        status: "UNKNOWN_ERROR",
        message: error.message,
      });
    }
  }
);

// НОВЫЕ THUNKS ДЛЯ АДМИНА
export const createProductThunk = createAsyncThunk(
  "products/createProduct",
  async (productData, thunkAPI) => {
    try {
      const formData = new FormData();
      Object.entries(productData).forEach(([key, value]) => {
        if (value) formData.append(key, value);
      });
      const response = await api.post("/products", formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response?.data || error.message);
    }
  }
);

export const updateProductThunk = createAsyncThunk(
  "products/updateProduct",
  async ({ id, productData }, thunkAPI) => {
    try {
      const formData = new FormData();
      Object.entries(productData).forEach(([key, value]) => {
        if (value) formData.append(key, value);
      });
      const response = await api.put(`/products/${id}`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response?.data || error.message);
    }
  }
);

export const deleteProductThunk = createAsyncThunk(
  "products/deleteProduct",
  async (id, thunkAPI) => {
    try {
      await api.delete(`/products/${id}`);
      return id;
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response?.data || error.message);
    }
  }
);

export const getAllProductsThunk = createAsyncThunk(
  "products/getAllProducts",
  async (_, thunkAPI) => {
    try {
      const response = await api.get("/products");
      return response.data;
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response?.data || error.message);
    }
  }
);
