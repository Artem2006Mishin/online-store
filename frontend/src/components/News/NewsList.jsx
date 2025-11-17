import NewsCard from './NewsCard';
import styles from './News.module.css';

const NewsList = ({ news }) => {
	return (
		<div className={styles.wrapper}>
			{news.map((n) => (
				<NewsCard key={n.id} title={n.title} text={n.text} />
			))}
		</div>
	);
};

export default NewsList;
