import { Outlet } from 'react-router-dom';
import Section from '../components/Section/Section';

const CatalogLayout = () => {
	return (
		<Section>
			<Outlet />
		</Section>
	);
};

export default CatalogLayout;

// TODO: здесь будет рендериться корзина товаров
