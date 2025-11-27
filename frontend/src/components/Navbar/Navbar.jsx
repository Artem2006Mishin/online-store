import { NavLink } from 'react-router-dom';
import styles from './Navbar.module.css';

const Navbar = ({ data }) => {
	return (
		<nav className={styles.menu}>
			{data.map((elem) => (
				<NavLink key={elem.text} className={styles.menu__link} to={elem.to}>
					{elem.text}
				</NavLink>
			))}
		</nav>
	);
};

export default Navbar;
