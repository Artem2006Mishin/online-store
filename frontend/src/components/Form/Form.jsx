import { FormProvider, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import styles from './Form.module.css';

const Form = ({ children, onSubmit, defaultValues, schema }) => {
	const methods = useForm({
		resolver: yupResolver(schema),
		defaultValues,
	});

	return (
		<FormProvider {...methods}>
			<form className={styles.form} onSubmit={methods.handleSubmit(onSubmit)}>
				{children}
			</form>
		</FormProvider>
	);
};

export default Form;
