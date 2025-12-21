import {Outlet, useNavigate} from 'react-router-dom';
import Header from "../components/Header/Header.jsx";
import {useDispatch, useSelector} from "react-redux";
import {removeFromCart} from "../app/features/cart/cartSlice.js";

const CategoriesLayout = () => {
  const cartItems = useSelector((state) => state.cart.items);
  const total = useSelector((state) => state.cart.total);

  const dispatch = useDispatch();
  const handleClick = (productId) => {
    dispatch(removeFromCart(productId));
  }

  const navigate = useNavigate();
  const handleOrder = () => {
    navigate('/order');
  }

  return (
    <div style={{width: '100%', display: 'flex', flexDirection: 'column', gap: '30px'}}>
      <Header title='Каталог'/>
      <div style={{display: 'flex', justifyContent: 'space-between', gap: '30px'}}>
        <Outlet/>
        <div style={{width: '30%', display: 'flex', flexDirection: 'column', justifyContent: 'space-between'}}>
          <div>
            <h4 style={{margin: '0 0 20px 0'}}>корзина: {total}$</h4>
            <div style={{display: 'flex', flexDirection: 'column', gap: '10px'}}>
              {cartItems.map(item => (
                <div key={item.productId} style={{display: 'flex', gap: '10px'}}>
                  <h3>{item.productName}: {item.productPrice}$</h3>
                  <button onClick={() => handleClick(item.productId)}>удалить</button>
                </div>
              ))}
            </div>
          </div>
          <button disabled={total === 0} onClick={handleOrder}
                  style={{cursor: total === 0 ? 'not-allowed' : 'pointer'}}>оформить заказ
          </button>
        </div>
      </div>
    </div>
  );
};

export default CategoriesLayout;
