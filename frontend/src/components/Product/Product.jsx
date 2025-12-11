import style from './Product.module.css';

const Product = ({ data }) => {
	return (
		<div className={style.productCard}>
			<img src={`http://localhost:8080${data.imageURL}`} alt='img' />

			<div className={style.productInfo}>
				<h3>{data.name}</h3>
				<p>{data.description}</p>

				<div className={style.productBtn}>
					<span>{data.price}$</span>
					<button>в корзину</button>
				</div>
			</div>
		</div>
	);
};

export default Product;
