import styles from './Card.module.css';

const Card = ({ data, type, onClick }) => {
	return (
		<div className={styles.card}>
			<img
				className={`${styles.card__image} ${styles[type]}`}
				src={`http://localhost:8080${data.imageURL}`}
				alt={data.title}
				onClick={onClick}
			/>
			<div className={styles.description}>
				<h3 className={styles.description__title}>{data.title}</h3>
				{data.text && <p className={styles.description__text}>{data.text}</p>}
			</div>
		</div>
	);
};

export default Card;
