import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {clearCart} from "../app/features/cart/cartSlice.js";
import {placeOrderThunk, getOrdersThunk} from "../app/features/order/orderThunk.js";
import {useEffect} from "react";

const OrderPage = () => {
  const {total, items} = useSelector(state => state.cart);
  const {currentOrderStatus} = useSelector(state => state.orders);

  const dispatch = useDispatch();
  const navigate = useNavigate();
  
  const handleClick = () => {
    if (total > 0) {
      const orderData = {
        productIds: items.map((item) => item.productId)
      };
      console.log(orderData);

      dispatch(placeOrderThunk(orderData));
      dispatch(clearCart());
    }
  }

  // Обновляем список заказов после успешного создания заказа
  useEffect(() => {
    if (currentOrderStatus === 'succeeded') {
      dispatch(getOrdersThunk());
      navigate('/');
    }
  }, [currentOrderStatus, dispatch, navigate]);

  return (
    <div style={{ width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '20px' }}>
      <h3>Общая стоимость: {total}$</h3>
      <ul>
        {items.map(item => (
          <li key={item.productId}>{item.productName} — {item.productPrice}$</li>
        ))}
      </ul>
      <button onClick={handleClick}>заказать</button>
    </div>
  )
}

export default OrderPage;