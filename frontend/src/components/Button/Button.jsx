import { useNavigate } from 'react-router-dom';
import styles from './Button.module.css';

const Button = ({ type, label, to, onClick }) => {
	const navigate = useNavigate();
	const handleClick = () => {
		if (onClick) onClick();
		if (to) navigate(to);
	};

	return (
		<button
			className={styles.btn}
			type={type}
			onClick={type === 'submit' ? undefined : handleClick}
		>
			{label}
		</button>
	);
};

export default Button;
