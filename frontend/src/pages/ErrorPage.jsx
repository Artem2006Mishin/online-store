import { useRouteError } from 'react-router-dom';

const ErrorPage = () => {
	const error = useRouteError();

	return (
		<div>
			<p>Что-то пошло не так.</p>
			<div>{error.statusText || error.message}</div>
		</div>
	);
};

export default ErrorPage;

// TODO: добавить стили для страницы
