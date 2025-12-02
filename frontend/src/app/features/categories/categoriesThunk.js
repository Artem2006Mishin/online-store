import { createAsyncThunk } from '@reduxjs/toolkit';
import { getElements } from '../default/defaultThunk';

export const getCategories = createAsyncThunk('categories/get', getElements);
