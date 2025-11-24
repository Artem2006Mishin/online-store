import { loginSchema } from '../components/schema';
import Input from '../components/Input/Input';
import Form from '../components/Form/Form';
import Button from '../components/Button/Button';

const LoginPage = () => {
	const defaultValues = {
		email: '',
		password: '',
	};

	const onSubmit = async (data) => {
		console.log(data);
	};

	return (
		<Form
			onSubmit={onSubmit}
			defaultValues={defaultValues}
			schema={loginSchema}
		>
			<Input label='Электронная почта' name='email' type='email' />
			<Input label='Пароль' name='password' type='password' />

			<Button type='submit' label='Войти' />
		</Form>
	);
};

export default LoginPage;
