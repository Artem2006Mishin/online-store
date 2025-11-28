import { Outlet, useParams } from 'react-router-dom';
import Header from '../components/Header/Header';
import Section from '../components/Section/Section';
import { useSelector } from 'react-redux';

const CatalogLayout = () => {
	const { id } = useParams();
	const categories = useSelector((state) => state.categories.items);
	const current = categories.find((category) => category.id === Number(id));

	return (
		<Section>
			<Header title={id ? current.title : 'Каталог'} />
			<Outlet />
		</Section>
	);
};

export default CatalogLayout;

// TODO: убрать стили из layout, использовать только компоненты тут
