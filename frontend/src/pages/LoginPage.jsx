import Input from '../components/Input/Input';
import Form from '../components/Form/Form';
import Button from '../components/Button/Button';
import Loading from '../components/Loading/Loading';
import { loginSchema } from '../components/schema';
import {authUserThunk} from '../app/features/users/usersThunk';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';

const LoginPage = () => {
	const defaultValues = {
		email: '',
		password: '',
	};

	const dispatch = useDispatch();
	const onSubmit = data => {
		dispatch(authUserThunk({ url: 'login', userData: data }));
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
					schema={loginSchema}
				>
					<Input label='Электронная почта' name='email' type='email' />
					<Input label='Пароль' name='password' type='password' />

					<Button type='submit' label='Войти' />
					<Button
						type='button'
						label='Еще не зарегистрирован?'
						to='/auth/reg'
					/>
				</Form>
			)}
		</>
	);
};

export default LoginPage;
