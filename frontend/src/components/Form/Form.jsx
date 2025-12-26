import { FormProvider, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import styles from './Form.module.css';

const Form = ({ children, onSubmit, defaultValues, schema }) => {
	const error = useSelector((state) => state.users.error);
	const methods = useForm({
		resolver: yupResolver(schema),
		defaultValues,
	});

	const submitForm = (data) => {
		onSubmit(data);
	};

	useEffect(() => {
		if (!error) {
			methods.reset();
			return;
		}
		switch (error.status) {
			case 'INVALID_PASSWORD':
				methods.setError('password', {
					type: 'custom',
					message: error.message,
				});
				break;
			case 'EMAIL_NOT_FOUND':
				methods.setError('email', {
					type: 'custom',
					message: error.message,
				});
				break;
			case 'EMAIL_BUSY':
				methods.setError('email', {
					type: 'custom',
					message: error.message,
				});
				break;
			default:
				methods.setError('root', {
					type: 'server',
					message: 'Произошла неизвестная ошибка',
				});
		}
	}, [error, methods]);

	return (
		<FormProvider {...methods}>
			<form className={styles.form} onSubmit={methods.handleSubmit(submitForm)}>
				{children}
			</form>
		</FormProvider>
	);
};

export default Form;
