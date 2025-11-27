import styles from './Detail.module.css';

const Detail = ({ title, value }) => {
	return (
		<div className={styles.detail}>
			<div className={styles.detail__title}>{title}: </div>
			<div className={styles.detail__value}>{value}</div>
		</div>
	);
};

export default Detail;
