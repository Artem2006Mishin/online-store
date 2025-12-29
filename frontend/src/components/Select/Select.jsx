import { useFormContext } from 'react-hook-form';
import styles from '../Input/Input.module.css';

const Select = ({ label, name, options, placeholder = 'Выберите...' }) => {
	const {
		register,
		formState: { errors },
	} = useFormContext();

	return (
		<div className={styles.filed}>
			<div>
				<label className={styles.filed__title}>{label}</label>
			</div>
			<select
				className={styles.filed__input}
				{...register(name)}
			>
				<option value="">{placeholder}</option>
				{options.map((option) => (
					<option key={option.value} value={option.value}>
						{option.label}
					</option>
				))}
			</select>
			{errors[name] && <p style={{ color: 'red' }}>{errors[name].message}</p>}
		</div>
	);
};

export default Select;

