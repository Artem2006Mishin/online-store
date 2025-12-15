import { Outlet } from 'react-router-dom';
import Section from '../components/Section/Section';

const CategoriesLayout = () => {
	return (
		<Section>
			<Outlet />
		</Section>
	);
};

export default CategoriesLayout;

// TODO: здесь будет рендериться корзина товаров
