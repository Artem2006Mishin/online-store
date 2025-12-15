import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar/Navbar';
// import { useSelector } from 'react-redux';

const Layout = () => {
	// const { token } = useSelector((state) => state.userData.userData);


	const unregisteredUserData = [
		{ to: '/', text: 'НОВОСТИ' },
		{ to: '/categories', text: 'КАТАЛОГ' },
		{ to: '/auth', text: 'ВОЙТИ В АККАУНТ' },
	];

	// const registeredUserData = [
	// 	{ to: '/', text: 'НОВОСТИ' },
	// 	{ to: '/categories', text: 'КАТАЛОГ' },
	// 	{ to: '/profile', text: 'ПРОФИЛЬ' },
	// ];

	return (
		<>
			<header>
        <Navbar data={unregisteredUserData} />
				{/*{!token && }*/}
				{/*{token && <Navbar data={registeredUserData} />}*/}
			</header>

			<main>
				<Outlet />
			</main>
		</>
	);
};

export default Layout;
