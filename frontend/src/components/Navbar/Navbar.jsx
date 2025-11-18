import { useSelector } from 'react-redux';
import { NavLink } from 'react-router-dom';
import styles from './Navbar.module.css';

const Navbar = () => {
	const data = useSelector((state) => state.navbar.items);

	return (
		<nav className={styles.menu}>
			{data.map((elem) => (
				<NavLink key={elem.text} className={styles.menu__link} to={elem.link}>
					{elem.text}
				</NavLink>
			))}
		</nav>
	);
};

export default Navbar;
