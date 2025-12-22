import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {clearCart} from "../app/features/cart/cartSlice.js";
import {placeOrderThunk} from "../app/features/order/orderThunk.js";

const OrderPage = () => {
  const {total, items} = useSelector(state => state.cart);

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const handleClick = () => {
    if (total > 0) { // todo: я заебался
      const orderData = {
        items: items.map((item) => item.productName), // todo: в дальшейм расширить до адреса и оплаты
      }
      dispatch(placeOrderThunk(orderData));

      dispatch(clearCart());
      navigate('/');
    }
  }

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