import styles from './Error.module.css';

const Errors = ({ error }) => {
	return (
		<div className={styles.error}>
			<h3 className={styles.error__title}>{error.status}</h3>
			<p className={styles.error__text}>{error.message}</p>
		</div>
	);
};

export default Errors;
