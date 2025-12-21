import style from './Product.module.css';

const Product = ({data, onClick, isInCart}) => {
  return (
    <div className={style.productCard} style={{backgroundColor: isInCart ? 'grey' : "white"}}>
      <img src={`http://localhost:8080${data.imageURL}`} alt='img'/>

      <div className={style.productInfo}>
        <h3>{data.name}</h3>
        <p>{data.description}</p>

        <div className={style.productBtn}>
          <span>{data.price}$</span>
          <button disabled={isInCart} onClick={onClick} style={{cursor: isInCart ? 'not-allowed' : 'pointer'}}>
            {isInCart ? 'В корзине' : 'Добавить'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Product;
