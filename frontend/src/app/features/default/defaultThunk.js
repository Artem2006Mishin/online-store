import api from '../../../api/axios';

export const getElements = async (url, thunkAPI) => {
	try {
		const response = await api.get(url);
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
};
