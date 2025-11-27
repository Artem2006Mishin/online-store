import { useNavigate } from 'react-router-dom';
import styles from './Button.module.css';

const Button = ({ type, label, to }) => {
	const navigate = useNavigate();
	const handleClick = () => {
		if (to) navigate(to);
	};

	return (
		<button className={styles.btn} type={type} onClick={handleClick}>
			{label}
		</button>
	);
};

export default Button;
