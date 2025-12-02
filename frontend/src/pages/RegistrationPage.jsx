import { registerSchema } from '../components/schema';
import { useDispatch, useSelector } from 'react-redux';
import Input from '../components/Input/Input';
import Form from '../components/Form/Form';
import Button from '../components/Button/Button';
import { saveUser } from '../app/features/users/usersThunk';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

const RegistrationPage = () => {
	const defaultValues = {
		email: '',
		password: '',
		confirmPassword: '',
	};

	const dispatch = useDispatch();
	const onSubmit = data => {
		const { confirmPassword: _, ...dataToSend } = data;
		dispatch(saveUser({ url: '/auth/register', userData: dataToSend }));
	};

	const navigate = useNavigate();
	const status = useSelector(state => state.users.status);
	useEffect(() => {
		if (status === 'success') navigate('/profile', { replace: true });
	}, [status, navigate]);

	return (
		<>
			{status === 'loading' && <Loading title='профиль' />}
			{status !== 'loading' && (
				<Form
					onSubmit={onSubmit}
					defaultValues={defaultValues}
					schema={registerSchema}
				>
					<Input label='Электронная почта' name='email' type='email' />
					<Input label='Пароль' name='password' type='password' />
					<Input
						label='Подтверждение пароля'
						name='confirmPassword'
						type='password'
					/>

					<Button type='submit' label='Зарегистрироваться' />
					<Button type='button' label='Вернуться обратно' to='/auth' />
				</Form>
			)}
		</>
	);
};

export default RegistrationPage;
