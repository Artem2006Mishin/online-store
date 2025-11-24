import { useFormContext } from 'react-hook-form';
import styles from './Input.module.css';

const Input = ({ label, name, type }) => {
	const {
		register,
		formState: { errors },
	} = useFormContext();

	return (
		<div className={styles.filed}>
			<div>
				<label className={styles.filed__title}>{label}</label>
			</div>
			<input className={styles.filed__input} type={type} {...register(name)} />
			{errors[name] && <p style={{ color: 'red' }}>{errors[name].message}</p>}
		</div>
	);
};

export default Input;
