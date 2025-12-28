import { useDispatch } from 'react-redux';
import Form from '../Form/Form';
import Input from '../Input/Input';
import Button from '../Button/Button';
import { newsSchema } from '../schema';
import { createNewsThunk } from '../../app/features/news/newsThunk';

const NewsForm = ({ onCancel }) => {
	const defaultValues = {
		title: '',
		text: '',
		image: null,
	};

	const dispatch = useDispatch();
	const onSubmit = (data) => {
		dispatch(createNewsThunk(data));
		// После отправки можно закрыть форму
		onCancel();
	};

	return (
		<Form onSubmit={onSubmit} defaultValues={defaultValues} schema={newsSchema}>
			<Input label='Заголовок' name='title' type='text' />
			<Input label='Текст' name='text' type='textarea' />
			<Input label='Изображение' name='image' type='file' accept='image/*' />

			<Button type='submit' label='Создать новость' />
			<Button type='button' label='Отмена' onClick={onCancel} />
		</Form>
	);
};

export default NewsForm;
