import { Outlet, useLocation } from 'react-router-dom';
import Header from '../components/Header/Header';
import Section from '../components/Section/Section';

const AuthLayout = () => {
	const location = useLocation();
	const subtitle = location.pathname === '/auth' ? 'вход' : 'регистрация';

	return (
		<Section>
			<Header title={`Аутентификация / ${subtitle}`} />
			<Outlet />
		</Section>
	);
};

export default AuthLayout;
