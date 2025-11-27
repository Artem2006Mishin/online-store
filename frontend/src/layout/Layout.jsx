import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar/Navbar';
import { useSelector } from 'react-redux';

const Layout = () => {
	const token = useSelector((state) => state.users.items.token);

	const unregisteredUserData = [
		{ to: '/', text: 'НОВОСТИ' },
		{ to: '/catalog', text: 'КАТАЛОГ' },
		{ to: '/auth', text: 'ВОЙТИ В АККАУНТ' },
	];

	const registeredUserData = [
		{ to: '/', text: 'НОВОСТИ' },
		{ to: '/catalog', text: 'КАТАЛОГ' },
		{ to: '/profile', text: 'ПРОФИЛЬ' },
	];

	return (
		<>
			<header>
				{!token && <Navbar data={unregisteredUserData} />}
				{token && <Navbar data={registeredUserData} />}
			</header>

			<main>
				<Outlet />
			</main>
		</>
	);
};

export default Layout;

// TODO: оптимизировать логику рендера navbar
