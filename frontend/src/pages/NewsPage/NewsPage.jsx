import NewsList from '../../components/News/NewsList';
import { useDispatch, useSelector } from 'react-redux';
import styles from './NewsPage.module.css';
import { useEffect } from 'react';
import { getNews } from '../../app/features/news/newsThunk';
import Header from '../../components/Header/Header';

const NewsPage = () => {
	const news = useSelector((state) => state.news.news);
	const status = useSelector((state) => state.news.status);
	const error = useSelector((state) => state.news.error);

	const dispatch = useDispatch();

	useEffect(() => {
		if (status === 'idle') dispatch(getNews());
	}, [status, dispatch]);

	return (
		<section className={styles.container}>
			<Header title='Новости' />

			{status === 'loading' && (
				<div className={styles.loading}>
					<p className={styles.loading__text}>loading news...</p>
				</div>
			)}
			{status === 'success' && <NewsList news={news} />}
			{status === 'error' && (
				<div className={styles.error}>
					<h3 className={styles.error__title}>{error.status}</h3>
					<p className={styles.error__text}>{error.message}</p>
				</div>
			)}
		</section>
	);
};

export default NewsPage;

// TODO: посмотреть с хуком useState будут ли сохраняться данные при переходахъ между страницами
