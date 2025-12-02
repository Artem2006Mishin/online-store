import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';

import { getNews } from '../app/features/news/newsThunk';
import Header from '../components/Header/Header';
import List from '../components/List/List';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import Section from '../components/Section/Section';
import Card from '../components/Card/Card';

const NewsPage = () => {
	const items = useSelector(state => state.news.items);
	const status = useSelector(state => state.news.status);
	const error = useSelector(state => state.news.error);

	const dispatch = useDispatch();

	useEffect(() => {
		if (status === 'idle') dispatch(getNews('/'));
	}, [status, dispatch]);

	return (
		<Section>
			<Header title='Новости' />

			{status === 'loading' && <Loading title={'новости'} />}
			{status === 'success' && (
				<List
					dataList={items}
					renderItem={data => <Card key={data.id} data={data} type='news' />}
				/>
			)}
			{status === 'error' && <Errors error={error} />}
		</Section>
	);
};

export default NewsPage;
