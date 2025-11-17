import styles from './Header.module.css';

const Header = ({ title }) => {
	return (
		<div>
			<h1 className={styles.title}>{title}</h1>
			<hr />
		</div>
	);
};

export default Header;
