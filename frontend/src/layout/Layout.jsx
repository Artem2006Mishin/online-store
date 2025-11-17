import { Outlet, useLocation } from 'react-router-dom';

import styles from './Layout.module.css';
import Navbar from '../components/Navbar/Navbar';
import Header from '../components/Header/Header';

const Layout = () => {
	const location = useLocation();
	let headerData = {};
	if (location.pathname.startsWith('/catalog')) {
		headerData = { title: 'Каталог' };
	} else {
		headerData = { title: 'Новости' };
	}

	return (
		<>
			<header className={styles.header}>
				<Navbar />
				<Header {...headerData} />
			</header>

			<main className={styles.main}>
				<Outlet />
			</main>
		</>
	);
};

export default Layout;
