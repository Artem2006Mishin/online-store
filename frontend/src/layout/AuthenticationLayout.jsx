import { Outlet } from 'react-router-dom';
import Header from '../components/Header/Header';
import Section from '../components/Section/Section';

const AuthenticationLayout = () => {
	return (
		<Section>
			<Header title='Аутентификация' />
			<Outlet />
		</Section>
	);
};

export default AuthenticationLayout;
