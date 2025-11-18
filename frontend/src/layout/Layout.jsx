import { Outlet, useLocation } from 'react-router-dom';
import { useDispatch } from 'react-redux';

import styles from './Layout.module.css';
import Navbar from '../components/Navbar/Navbar';
import { switchingCatalog, switchingNews } from '../app/features/navbarSlice';
import { useEffect } from 'react';

const Layout = () => {
	const dispatch = useDispatch();
	const location = useLocation();

	useEffect(() => {
		if (location.pathname.startsWith('/catalog')) {
			dispatch(switchingCatalog());
		} else {
			dispatch(switchingNews());
		}
	}, [location.pathname, dispatch]);

	return (
		<>
			<header className={styles.header}>
				<Navbar />
			</header>

			<main className={styles.main}>
				<Outlet />
			</main>
		</>
	);
};

export default Layout;
