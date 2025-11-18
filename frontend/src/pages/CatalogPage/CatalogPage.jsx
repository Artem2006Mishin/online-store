import { Outlet } from 'react-router-dom';
import Header from '../../components/Header/Header';
import styles from './CatalogPage.module.css';

const CatalogPage = () => {
	return (
		<section className={styles.container}>
			<Header title='Каталог' />
			<Outlet />
		</section>
	);
};

export default CatalogPage;
