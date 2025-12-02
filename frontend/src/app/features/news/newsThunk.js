import { createAsyncThunk } from '@reduxjs/toolkit';
import { getElements } from '../default/defaultThunk';

export const getNews = createAsyncThunk('news/get', getElements);
