import styles from './News.module.css';

const NewsCard = ({ title, text }) => {
	return (
		<article className={styles.card}>
			<h3 className={styles.card__title}>{title}</h3>
			<div className={styles.card__description}>
				<p>{text}</p>
			</div>
		</article>
	);
};

export default NewsCard;
