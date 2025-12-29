import style from './Product.module.css';

const Product = ({data, onClick, isInCart}) => {
  return (
    <div className={style.productCard} style={{backgroundColor: isInCart ? 'grey' : "white"}}>
      {data.imageURL ? (
        <img
          src={`http://localhost:8080${data.imageURL}`}
          alt={data.name || 'img'}
          onError={(e) => {
            // hide broken image and show placeholder
            e.target.style.display = 'none';
          }}
        />
      ) : (
        <div style={{width: '100%', height: '200px', display: 'flex', alignItems: 'center', justifyContent: 'center', background: '#f3f4f6'}}>
          Нет изображения
        </div>
      )}

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
