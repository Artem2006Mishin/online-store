import * as yup from 'yup';

export const loginSchema = yup.object().shape({
	email: yup.string().email('Некорректный email').required('Email обязателен'),
	password: yup
		.string()
		.min(6, 'Минимальная длина — 6 символов')
		.required('Пароль обязателен'),
});

export const registerSchema = yup.object().shape({
	email: yup.string().email('Некорректный email').required('Email обязателен'),
	password: yup
		.string()
		.min(6, 'Минимальная длина — 6 символов')
		.required('Пароль обязателен'),
	confirmPassword: yup
		.string()
		.oneOf([yup.ref('password'), null], 'Пароли должны совпадать')
		.required('Подтверждение пароля обязательно'),
	avatar: yup
		.mixed()
		.nullable()
		.test('fileSize', 'Файл слишком большой', (value) => {
			if (!value || value.length === 0) return true;
			return value[0].size <= 2 * 1024 * 1024;
		})
		.test('fileType', 'Неподдерживаемый формат', (value) => {
			if (!value || value.length === 0) return true;
			return ['image/jpeg', 'image/png', 'image/webp'].includes(value[0].type);
		}),
});

export const updateProfileSchema = yup.object().shape({
	email: yup.string().email('Некорректный email'),
	password: yup.string().min(6, 'Минимальная длина — 6 символов'),
	confirmPassword: yup
		.string()
		.oneOf([yup.ref('password'), null], 'Пароли должны совпадать'),
	avatar: yup
		.mixed()
		.nullable()
		.test('fileSize', 'Файл слишком большой', (value) => {
			if (!value || value.length === 0) return true;
			return value[0].size <= 2 * 1024 * 1024;
		})
		.test('fileType', 'Неподдерживаемый формат', (value) => {
			if (!value || value.length === 0) return true;
			return ['image/jpeg', 'image/png', 'image/webp'].includes(value[0].type);
		}),
});
