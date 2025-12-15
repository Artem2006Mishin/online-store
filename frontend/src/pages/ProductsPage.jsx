import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import {getProductsThunk} from '../app/features/products/productsThunk';

import Header from '../components/Header/Header';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import List from '../components/List/List';
import Product from '../components/Product/Product';

const ProductsPage = () => {
	const { name } = useParams();
	const { status, productsList, error } = useSelector((state) => state.products);

	const dispatch = useDispatch();
	useEffect(() => {
		dispatch(getProductsThunk(name));
	}, [name, dispatch]);

	return (
		<>
			<Header title={name} />

      {status === 'loading' && <Loading title={'каталог'} />}
      {status === 'error' && <Errors error={error} />}

			{status === 'success' && (
				<List
					dataList={productsList}
					renderItem={(data) => <Product key={data.id} data={data} />}
				/>
			)}
		</>
	);
};

export default ProductsPage;
