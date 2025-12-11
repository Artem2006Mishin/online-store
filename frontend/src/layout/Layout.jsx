import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar/Navbar';
import { useSelector } from 'react-redux';
import { useEffect } from 'react';

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

	// useEffect(

	// );

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

// FIXME: оптимизировать логику рендера navbar. спросить у такашкина.
