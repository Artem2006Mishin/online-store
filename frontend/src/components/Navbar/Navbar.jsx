import { NavLink } from 'react-router-dom';
import styles from './Navbar.module.css';

const Navbar = () => {
	return (
		<nav className={styles.menu}>
			<NavLink className={styles.menu__link} to='/'>
				НОВОСТИ
			</NavLink>
			<NavLink className={styles.menu__link} to='/catalog'>
				КАТАЛОГ
			</NavLink>
		</nav>
	);
};

export default Navbar;
