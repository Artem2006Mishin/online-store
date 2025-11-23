import styles from './Loading.module.css';

const Loading = ({ title }) => {
	return (
		<div className={styles.loading}>
			<p className={styles.loading__text}>Загружаем {title}...</p>
		</div>
	);
};

export default Loading;
