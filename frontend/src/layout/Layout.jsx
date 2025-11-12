import { Outlet } from 'react-router-dom';
import styles from './Layout.module.css';
import Navbar from '../../components/Navbar/Navbar';

const Layout = () => {
	return (
		<>
			<header className={styles.header}>
				<Navbar />
			</header>

			<main>
				<Outlet />
			</main>
		</>
	);
};

export default Layout;
