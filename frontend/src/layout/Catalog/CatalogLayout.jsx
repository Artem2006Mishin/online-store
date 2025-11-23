import { Outlet } from 'react-router-dom';
import Header from '../../components/Header/Header';
import styles from './CatalogLayout.module.css';

const CatalogLayout = () => {
	return (
		<section className={styles.container}>
			<Header title='Каталог' />
			<Outlet />
		</section>
	);
};

export default CatalogLayout;
