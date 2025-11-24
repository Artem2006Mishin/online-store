import * as yup from 'yup';

export const loginSchema = yup.object().shape({
	email: yup.string().email('Некорректный email').required('Email обязателен'),
	password: yup
		.string()
		.min(6, 'Минимальная длина — 6 символов')
		.required('Пароль обязателен'),
});
