import { useDispatch } from 'react-redux';
import { deleteNewsThunk } from '../../app/features/news/newsThunk';
import styles from './Card.module.css';

const Card = ({ data, type, onClick, onEdit, showButtons }) => {
	const dispatch = useDispatch();

	const handleDelete = (e) => {
		e.stopPropagation(); // Предотвращаем клик на карточке
		dispatch(deleteNewsThunk(data.id));
	};

	const handleEdit = (e) => {
		e.stopPropagation();
		if (onEdit) onEdit(data);
	};

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
				{type === 'news' && showButtons && (
					<div className={styles.buttons}>
						<button className={styles.editButton} onClick={handleEdit}>
							Редактировать
						</button>
						<button className={styles.deleteButton} onClick={handleDelete}>
							Удалить
						</button>
					</div>
				)}
			</div>
		</div>
	);
};

export default Card;
