import NewsList from '../../components/News/NewsList';
import { useDispatch, useSelector } from 'react-redux';
import styles from './NewsPage.module.css';
import { useEffect } from 'react';
import { getNews } from '../../app/features/news/newsThunk';

const NewsPage = () => {
	const news = useSelector((state) => state.news.news);
	const status = useSelector((state) => state.news.status);
	const dispatch = useDispatch();

	useEffect(() => {
		if (status === 'idle') dispatch(getNews());
	}, [status, dispatch]);

	return (
		<section className={styles.container}>
			{status === 'loading' && <div>loading news...</div>}
			{status === 'success' && <NewsList news={news} />}
			{status === 'error' && <div>error</div>}
		</section>
	);
};

export default NewsPage;

// TODO: посмотреть с хуком useState будут ли сохраняться данные при переходахъ между страницами
