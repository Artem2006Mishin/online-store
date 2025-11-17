import { store } from '../../store';
import { getNews } from './newsThunk';

export const loadAllNews = async () => {
	const resultAction = await store.dispatch(getNews());

	if (getNews.fulfilled.match(resultAction)) {
		return resultAction.payload;
	} else {
		throw new Response('Ошибка загрузки пользователей', { status: 500 });
	}
};

// TODO: заменить new Response на ответ от сервера
