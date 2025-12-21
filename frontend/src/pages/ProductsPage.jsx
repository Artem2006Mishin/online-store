import {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';

import {getProductsThunk} from '../app/features/products/productsThunk';

import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import List from '../components/List/List';
import Product from '../components/Product/Product';
import {addToCart} from "../app/features/cart/cartSlice.js";

const ProductsPage = () => {
  const {name} = useParams();
  const {status, productsList, error} = useSelector((state) => state.products);

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getProductsThunk(name));
  }, [name, dispatch]);

  const cartItems = useSelector(state => state.cart.items);
  const cartIds = new Set(cartItems.map(item => item.productId));

  const handleClick = (productData) => {
    if (!cartIds.has(productData.id)) {
      dispatch(addToCart(productData));
    }
  };

  return (
    <>
      {status === 'loading' && <Loading title={'каталог'}/>}
      {status === 'error' && <Errors error={error}/>}

      {status === 'success' && (
        <List
          dataList={productsList}
          renderItem={(data) => <Product key={data.id} data={data} isInCart={cartIds.has(data.id)}
                                         onClick={() => handleClick(data)}/>}
        />
      )}
    </>
  );
};

export default ProductsPage;
