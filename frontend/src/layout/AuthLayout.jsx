import { Outlet } from 'react-router-dom';
import Header from '../components/Header/Header';
import Section from '../components/Section/Section';

const AuthLayout = () => {
	return (
		<Section>
			<Header title='Аутентификация' />
			<Outlet />
		</Section>
	);
};

export default AuthLayout;
