import styles from './Button.module.css';

const Button = ({ type, label }) => {
	return (
		<button className={styles.btn} type={type}>
			{label}
		</button>
	);
};

export default Button;
