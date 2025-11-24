import { Outlet } from 'react-router-dom';
import Header from '../components/Header/Header';
import Section from '../components/Section/Section';

const CatalogLayout = () => {
	return (
		<Section>
			<Header title='Каталог' />
			<Outlet />
		</Section>
	);
};

export default CatalogLayout;

// TODO: убрать стили из layout, использовать только компоненты тут
