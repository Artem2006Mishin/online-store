import { NavLink } from 'react-router-dom';
import styles from './Navbar.module.css';
import { useSelector } from 'react-redux';

const Navbar = ({ data }) => {
	const { serverTime } = useSelector((state) => state.time);

	return (
		<nav className={styles.menu}>
			<div className={styles.links}>
				{data.map((elem) => (
					<NavLink key={elem.text} className={styles.menu__link} to={elem.to}>
						{elem.text}
					</NavLink>
				))}
			</div>

			<div className={styles.time}>
				{serverTime ? new Date(serverTime).toLocaleTimeString() : ''}
			</div>
		</nav>
	);
};

export default Navbar;
