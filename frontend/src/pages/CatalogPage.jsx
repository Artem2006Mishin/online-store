import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from 'react';

import { getCategories } from '../app/features/categories/categoriesThunk';
import Header from '../components/Header/Header';
import List from '../components/List/List';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import Section from '../components/Section/Section';

const CatalogPage = () => {
	const items = useSelector((state) => state.categories.items);
	const status = useSelector((state) => state.categories.status);
	const error = useSelector((state) => state.categories.error);

	const dispatch = useDispatch();
	useEffect(() => {
		if (status === 'idle') dispatch(getCategories());
	}, [status, dispatch]);

	return (
		<Section>
			{status === 'loading' && <Loading title={'каталог'} />}
			{status === 'success' && <List dataList={items} cardType={'catalog'} />}
			{status === 'error' && <Errors error={error} />}
		</Section>
	);
};

export default CatalogPage;
