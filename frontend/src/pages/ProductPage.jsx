import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { getProducts } from '../app/features/products/productsThunk';
import Section from '../components/Section/Section';
import Header from '../components/Header/Header';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import List from '../components/List/List';
import Product from '../components/Product/Product';

const ProductPage = () => {
	const { id } = useParams();
	const { status, items, error } = useSelector((state) => state.products);
	const [title, setTitle] = useState('название категории');

	const dispatch = useDispatch();
	useEffect(() => {
		switch (id) {
			case '1':
				setTitle('Компьютеры');
				break;
			case '2':
				setTitle('Смартфоны');
				break;
			case '3':
				setTitle('Бытовые товары');
				break;
			default:
				setTitle('название категории');
		}

		dispatch(getProducts(id));
	}, [id, dispatch]);

	// FIXME: заменить switch на получение данных из categories.title, которые приходят из /catalog
	// Как это надо сделать знает такташкин!!

	return (
		<>
			<Header title={title} />

			{status === 'success' && (
				<List
					dataList={items}
					renderItem={(data) => <Product key={data.id} data={data} />}
				/>
			)}
			{status === 'loading' && <Loading title={'каталог'} />}
			{status === 'error' && <Errors error={error} />}
		</>
	);
};

export default ProductPage;
