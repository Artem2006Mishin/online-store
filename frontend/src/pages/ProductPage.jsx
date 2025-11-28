import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { getProducts } from '../app/features/products/productsThunk';

const ProductPage = () => {
	const { id } = useParams();
	const dispatch = useDispatch();

	const items = useSelector((state) => state.products.items);
	console.log(items);

	useEffect(() => {
		dispatch(getProducts(id));
	}, [id, dispatch]);
};

export default ProductPage;
