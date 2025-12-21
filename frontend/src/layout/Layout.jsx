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

	return (
		<>
			<header>
        <Navbar data={unregisteredUserData} />
			</header>

			<main>
				<Outlet />
			</main>
		</>
	);
};

export default Layout;
