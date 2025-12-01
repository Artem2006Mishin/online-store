import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

import { getCategories } from '../app/features/categories/categoriesThunk';
import List from '../components/List/List';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import Section from '../components/Section/Section';
import Card from '../components/Card/Card';
import Header from '../components/Header/Header';

const CatalogPage = () => {
	const items = useSelector(state => state.categories.items);
	const status = useSelector(state => state.categories.status);
	const error = useSelector(state => state.categories.error);

	const dispatch = useDispatch();
	useEffect(() => {
		if (status === 'idle') dispatch(getCategories());
	}, [status, dispatch]);

	const navigate = useNavigate();
	const handleClick = id => {
		navigate(`/catalog/${id}`);
	};

	return (
		<Section>
			<Header title='Каталог' />

			{status === 'loading' && <Loading title={'каталог'} />}
			{status === 'success' && (
				<List
					dataList={items}
					renderItem={data => (
						<Card
							key={data.id}
							data={data}
							type='catalog'
							onClick={() => handleClick(data.id)}
						/>
					)}
				/>
			)}
			{status === 'error' && <Errors error={error} />}
		</Section>
	);
};

export default CatalogPage;
